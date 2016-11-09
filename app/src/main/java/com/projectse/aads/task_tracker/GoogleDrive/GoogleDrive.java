package com.projectse.aads.task_tracker.GoogleDrive;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.IntentSender.SendIntentException;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.util.Log;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.drive.Drive;
import com.google.android.gms.drive.DriveApi;
import com.google.android.gms.drive.DriveContents;
import com.google.android.gms.drive.DriveFile;
import com.google.android.gms.drive.DriveFolder;
import com.google.android.gms.drive.DriveResource;
import com.google.android.gms.drive.Metadata;
import com.google.android.gms.drive.MetadataChangeSet;
import com.projectse.aads.task_tracker.DBService.DatabaseHelper;
import com.projectse.aads.task_tracker.R;
import com.projectse.aads.task_tracker.RequestCode;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.Writer;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * An abstract activity that handles authorization and connection to the Drive
 * services.
 */
public class GoogleDrive implements
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener {

    private static final String TAG = "GoogleDriveActivity";
    private boolean isBackupSuccessful = false;
    private String backupName = "";
    private Context context;
    private ProgressDialog progressBar;
    public static boolean isBackup = false; //otherwise it is retore
    private String restoredDatabaseDate;

    /**
     * Google API client.
     */
    private GoogleApiClient mGoogleApiClient;

    public GoogleDrive(Context context) {
        this.context = context;
        progressBar = new ProgressDialog(context);
        progressBar.setCancelable(false);
        progressBar.setCanceledOnTouchOutside(false);
        mGoogleApiClient = new GoogleApiClient.Builder(context)
                .addApi(Drive.API)
                .addScope(Drive.SCOPE_FILE)
                .addScope(Drive.SCOPE_APPFOLDER) // required for App Folder sample
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build();
    }

    public void backup(){
        isBackup = true;
        mGoogleApiClient.connect();
    }

    public void restore(){
        isBackup = false;
        mGoogleApiClient.connect();
    }
    /**
     * Called when {@code mGoogleApiClient} is connected.
     */
    @Override
    public void onConnected(Bundle connectionHint) {
        // create new contents resource
        if(isBackup) {
            Drive.DriveApi.newDriveContents(getGoogleApiClient())
                    .setResultCallback(driveContentsCallback);
        } else {
            DriveFolder folder = Drive.DriveApi.getAppFolder(getGoogleApiClient());
            folder.listChildren(getGoogleApiClient()).setResultCallback(childrenRetrievedCallback);
        }

        progressBar.show();
    }

    /**
     * Called when {@code mGoogleApiClient} is disconnected.
     */
    @Override
    public void onConnectionSuspended(int cause) {
        Log.i(TAG, "GoogleApiClient connection suspended");
    }

    /**
     * Called when {@code mGoogleApiClient} is trying to connect but failed.
     * Handle {@code result.getResolution()} if there is a resolution is
     * available.
     */
    @Override
    public void onConnectionFailed(ConnectionResult result) {
        Log.i(TAG, "GoogleApiClient connection failed: " + result.toString());
        if (!result.hasResolution()) {
            // show the localized error dialog.
            GoogleApiAvailability.getInstance().getErrorDialog((Activity) context, result.getErrorCode(), 0).show();
            return;
        }
        try {
            result.startResolutionForResult((Activity) context, RequestCode.REQUEST_CODE_RESOLUTION);
        } catch (SendIntentException e) {
            showMessage("Error connecting to Google drive. " + result.toString());
            Log.e(TAG, "Exception while starting resolution activity", e);
        }
    }

    /**
     * Shows a toast message.
     */
    public void showMessage(String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
            }
        })
        //.setIcon(R.mipmap.ic_launcher)
        //.setTitle(message);
        .setMessage(message);

        builder.create().show();
    }

    /**
     * Getter for the {@code GoogleApiClient}.
     */
    public GoogleApiClient getGoogleApiClient() {
        return mGoogleApiClient;
    }

    final private ResultCallback<DriveApi.DriveContentsResult> driveContentsCallback = new
            ResultCallback<DriveApi.DriveContentsResult>() {
                @Override
                public void onResult(DriveApi.DriveContentsResult result) {
                    if (!result.getStatus().isSuccess()) {
                        showMessage("Error while trying to create new file contents!");
                        close();
                        return;
                    }

                    final DriveContents driveContents = result.getDriveContents();

                    // Perform I/O off the UI thread.
                    new Thread() {
                        @Override
                        public void run() {
                            // write content to DriveContents
                            File dbFile = context.getDatabasePath(DatabaseHelper.DATABASE_NAME);

                            try (OutputStream outputStream = driveContents.getOutputStream();
                                 Writer writer = new OutputStreamWriter(outputStream);
                                 FileInputStream fis = new FileInputStream(dbFile);
                                 Reader reader = new InputStreamReader(fis)) {
                                int read = 0;
                                while ((read = reader.read()) != -1) {
                                    writer.write(read);
                                }
                            } catch (IOException e) {
                                showMessage("There was a problem backing up your data!");
                                Log.e(TAG, e.getMessage());
                            }

                            backupName = DatabaseHelper.DATABASE_NAME + "_" + Calendar.getInstance().getTimeInMillis();
                            MetadataChangeSet changeSet = new MetadataChangeSet.Builder()
                                    .setTitle(backupName)
                                    .setMimeType("application/x-sqlite3")
                                    .setStarred(true).build();

                            Drive.DriveApi.getAppFolder(getGoogleApiClient())
                                    .createFile(getGoogleApiClient(), changeSet, driveContents)
                                    .setResultCallback(fileCallback);
                        }
                    }.start();
                }
            };

    final private ResultCallback<DriveFolder.DriveFileResult> fileCallback = new
            ResultCallback<DriveFolder.DriveFileResult>() {
                @Override
                public void onResult(DriveFolder.DriveFileResult result) {
                    if (!result.getStatus().isSuccess()) {
                        showMessage("Error while trying to backup your data to Google drive.");
                        isBackupSuccessful = false;
                        backupName = "";
                        close();
                        return;
                    }

                    isBackupSuccessful = true;
                    showMessage("The backup of your data was successful");
                    DriveFolder folder = Drive.DriveApi.getAppFolder(getGoogleApiClient());
                    folder.listChildren(getGoogleApiClient()).setResultCallback(childrenRetrievedCallback);

                }
            };

    ResultCallback<DriveApi.MetadataBufferResult> childrenRetrievedCallback = new
            ResultCallback<DriveApi.MetadataBufferResult>() {
                @Override
                public void onResult(DriveApi.MetadataBufferResult result) {
                    if (!result.getStatus().isSuccess()) {
                        Log.d(TAG, "Problem while retrieving files!");
                        return;
                    }

                    //This is a restore
                    if(!isBackup){
                        Metadata latestBackupMeta = getLatestBackupMetadata(result);
                        if(latestBackupMeta == null){
                            showMessage("You haven't backed up your data, yet!");
                            close();
                        } else {
                            DriveFile file = latestBackupMeta.getDriveId().asDriveFile();
                            file.open(mGoogleApiClient, DriveFile.MODE_READ_ONLY, null)
                                    .setResultCallback(contentsOpenedCallback);
                            restoredDatabaseDate = getDateString(latestBackupMeta.getCreatedDate());
                        }

                        return;
                    }

                    if(isBackupSuccessful) {
                        for (Metadata metadata : result.getMetadataBuffer()) {
                            if(!metadata.getTitle().equals(backupName)){
                                if(metadata.isTrashable() && !metadata.isTrashed()){
                                    DriveResource driveResource = metadata.getDriveId().asDriveResource();
                                    driveResource.delete(getGoogleApiClient()).setResultCallback(new ResultCallback<Status>() {
                                        @Override
                                        public void onResult(@NonNull Status status) {
                                            Log.d(TAG, status.isSuccess() ? "Success" : "Sorry mate");
                                            if(!status.isSuccess()){
                                                Log.d(TAG, status.getStatusMessage());
                                            }
                                        }
                                    });

                                    Log.d(TAG + " del", metadata.getTitle() + " " + metadata.getCreatedDate().toString());
                                }
                            }
                        }
                    } else {
                        if(result.getMetadataBuffer().getCount() > 0){
                            Metadata latestBackup = getLatestBackupMetadata(result);

                            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
                            String latestBackupDate = sdf.format(latestBackup.getCreatedDate());
                            showMessage("Error creating new backup. The previous one is from " + getDateString(latestBackup.getCreatedDate()));
                        } else {
                            showMessage("You don't have a previous backup of your data.");
                        }
                    }

                    close();
                }
            };

    ResultCallback<DriveApi.DriveContentsResult> contentsOpenedCallback =
            new ResultCallback<DriveApi.DriveContentsResult>() {
                @Override
                public void onResult(DriveApi.DriveContentsResult result) {
                    if (!result.getStatus().isSuccess()) {
                        showMessage("Sorry, but we couldn't retrieve your backup!");
                        close();
                        return;
                    }
                    // DriveContents object contains pointers
                    // to the actual byte stream
                    final DriveContents contents = result.getDriveContents();
                    new Handler(Looper.getMainLooper()).post(new Thread() {
                        @Override
                        public void run() {
                            File dbFile = context.getDatabasePath(DatabaseHelper.DATABASE_NAME + "_restored_" + Calendar.getInstance().getTimeInMillis());
                            try {
                                if (!dbFile.createNewFile()) {
                                    showMessage("There was a problem restoring your data!");
                                }
                            } catch (IOException e) {
                                showMessage("There was a problem restoring your data!");
                                return;
                            }

                            try (InputStream inputStream = contents.getInputStream();
                                 Reader reader = new InputStreamReader((inputStream));
                                 FileOutputStream fos = new FileOutputStream(dbFile);
                                 Writer writer = new OutputStreamWriter(fos)) {
                                int read = 0;
                                while ((read = reader.read()) != -1) {
                                    writer.write(read);
                                }

                                File dbFolder = dbFile.getParentFile();
                                boolean successDel = true;
                                DatabaseHelper.disconnect();
                                for (File file : dbFolder.listFiles()) {
                                    Log.d(TAG + " f:", file.getName());
                                    if (!file.getName().equals(dbFile.getName())) {
                                        if (!file.delete()) {
                                            Log.d(TAG, "not deleted" + file.getName());
                                            successDel = false;
                                            break;
                                        }
                                    }
                                }

                                if (successDel){
                                    if(dbFile.renameTo(context.getDatabasePath(DatabaseHelper.DATABASE_NAME))){
                                        showMessage("Successfully restored your data from " + restoredDatabaseDate);
                                    } else {
                                        showMessage("Problem: The old database couldn't be replaced with the new one!");
                                    }
                                } else {
                                    showMessage("Sorry, the old database couldn't be replaced with the new one.");
                                }

                                    for (File file : dbFolder.listFiles()) {
                                        Log.d(TAG + " pf:", file.getName());
                                    }
                            } catch (IOException e) {
                                showMessage("There was a problem restoring your data!");
                                Log.e(TAG, e.getMessage());
                            }

                            DatabaseHelper.connect(context);
                            contents.discard(getGoogleApiClient());
                            close();
                        }
                    });
                }
            };

    private Metadata getLatestBackupMetadata(DriveApi.MetadataBufferResult result){
        if(result.getMetadataBuffer().getCount() == 0){
            return null;
        }

        Metadata latestBackup = result.getMetadataBuffer().get(0);
        for(int i = 1; i < result.getMetadataBuffer().getCount(); i++){
            if(result.getMetadataBuffer().get(i).getCreatedDate().after(latestBackup.getCreatedDate())){
                latestBackup = result.getMetadataBuffer().get(i);
            }
        }

        return latestBackup;
    }

    private String getDateString(Date date){
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        String dateStr = sdf.format(date);
        return dateStr;
    }

    private void close(){
        mGoogleApiClient.disconnect();
        progressBar.dismiss();
    }
}
