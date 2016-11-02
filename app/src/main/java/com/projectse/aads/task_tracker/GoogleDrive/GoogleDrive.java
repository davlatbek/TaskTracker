package com.projectse.aads.task_tracker.GoogleDrive;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.IntentSender.SendIntentException;
import android.os.Bundle;
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
import com.google.android.gms.drive.DriveFolder;
import com.google.android.gms.drive.DriveResource;
import com.google.android.gms.drive.Metadata;
import com.google.android.gms.drive.MetadataChangeSet;
import com.projectse.aads.task_tracker.DBService.DatabaseHelper;
import com.projectse.aads.task_tracker.R;
import com.projectse.aads.task_tracker.RequestCode;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.Writer;
import java.text.SimpleDateFormat;
import java.util.Calendar;

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

    /**
     * Google API client.
     */
    private GoogleApiClient mGoogleApiClient;

    public GoogleDrive(Context context) {
        this.context = context;
        mGoogleApiClient = new GoogleApiClient.Builder(context)
                .addApi(Drive.API)
                .addScope(Drive.SCOPE_FILE)
                .addScope(Drive.SCOPE_APPFOLDER) // required for App Folder sample
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build();
    }

    public void backup(){
        mGoogleApiClient.connect();
    }
    /**
     * Called when {@code mGoogleApiClient} is connected.
     */
    @Override
    public void onConnected(Bundle connectionHint) {
        // create new contents resource
        Drive.DriveApi.newDriveContents(getGoogleApiClient())
                .setResultCallback(driveContentsCallback);
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
                        showMessage("Error while trying to beckup your data to Google drive.");
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
                        int count = result.getMetadataBuffer().getCount();
                        if(count > 0){
                            Metadata latestBackup = result.getMetadataBuffer().get(0);
                            for(int i = 1; i < count; i++){
                                if(result.getMetadataBuffer().get(i).getCreatedDate().after(latestBackup.getCreatedDate())){
                                    latestBackup = result.getMetadataBuffer().get(i);
                                }
                            }

                            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
                            String latestBackupDate = sdf.format(latestBackup.getCreatedDate());
                            showMessage("Error creating new backup. The previous one is from " + latestBackupDate);
                        } else {
                            showMessage("You don't have a previous backup of your data.");
                        }
                    }

                    close();
                }
            };

    private void close(){
        mGoogleApiClient.disconnect();
    }
}
