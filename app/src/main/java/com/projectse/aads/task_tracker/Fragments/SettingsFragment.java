package com.projectse.aads.task_tracker.Fragments;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.projectse.aads.task_tracker.DBService.DatabaseHelper;
import com.projectse.aads.task_tracker.GoogleDrive.AutomaticBackup;
import com.projectse.aads.task_tracker.GoogleDrive.Constants;
import com.projectse.aads.task_tracker.GoogleDrive.GoogleDrive;
import com.projectse.aads.task_tracker.MainActivity;
import com.projectse.aads.task_tracker.Models.SettingsModel;
import com.projectse.aads.task_tracker.R;
import com.projectse.aads.task_tracker.Utils.ShPrefUtils;

/**
 * Created by Andrey Zolin on 20.03.2016.
 */
public class SettingsFragment extends Fragment {
    protected SettingsModel settingsModel = new SettingsModel();
    DatabaseHelper db;
    Switch startDateSwitch;
    Switch dueDateSwitch;
    Switch enableSoundSwitch;
    Switch enableAutomaticBackup;
    EditText beforeStartDate;
    EditText beforeDueDate;
    EditText notSpecefiedStartDate;
    EditText notSpecDurationTaskEdit;
    EditText txtBackupInterval;
    TextView textViewLatestBackup;
    LinearLayout linearLayoutBackupInterval;

    public static void hideSoftKeyboard(Activity activity) {
        InputMethodManager inputMethodManager = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(), 0);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        getActivity().setTitle("Settings");
        db = new DatabaseHelper(getActivity().getApplicationContext());
        checkSettings();
        View view = inflater.inflate(R.layout.fargment_settings, container, false);
        setupUI(view);
        // InputFields
        beforeStartDate = (EditText) view.findViewById(R.id.startTime);
        beforeDueDate = (EditText) view.findViewById(R.id.dueTime);
        notSpecefiedStartDate = (EditText) view.findViewById(R.id.notSpecefiedStartDate);
        notSpecDurationTaskEdit = (EditText) view.findViewById(R.id.notSpecDurationTaskEdit);
        txtBackupInterval = (EditText) view.findViewById(R.id.txtBackupInterval);
        textViewLatestBackup = (TextView) view.findViewById(R.id.textViewLatestBackup);

        // Switches
        startDateSwitch = (Switch) view.findViewById(R.id.startDateSwitch);
        dueDateSwitch = (Switch) view.findViewById(R.id.dueDateSwitch);
        enableAutomaticBackup = (Switch) view.findViewById(R.id.switchAutomaticBackup);

        enableSoundSwitch = (Switch) view.findViewById(R.id.enableSoundSwitch);
        enableSoundSwitch.setChecked(ShPrefUtils.isPlaySounds(getActivity()));

        settingsModel = db.getSettings();
        startDateSwitch.setChecked(settingsModel.getAlwaysNotifyStartTime());
        dueDateSwitch.setChecked(settingsModel.getAlwaysNotifyDeadLine());

        // set text fields
        beforeStartDate.setText(settingsModel.getNotifyStartTimeBefore() + "");
        beforeDueDate.setText(settingsModel.getNotifyDeadLineBefore() + "");
        notSpecefiedStartDate.setText(settingsModel.getINSSSD() + "");
        notSpecDurationTaskEdit.setText(settingsModel.getINSTD() + "");
        linearLayoutBackupInterval = (LinearLayout) view.findViewById(R.id.llSetBackupInterval);
        String latestBackup = PreferenceManager.getDefaultSharedPreferences(getActivity().getApplicationContext())
                .getString(Constants.LAST_BACKUP_KEY, "");
        textViewLatestBackup.setText(latestBackup);
        int backupInterval = PreferenceManager.getDefaultSharedPreferences(getActivity().getApplicationContext())
                .getInt(Constants.BACKUP_INTERVAL_KEY, -1);
        //If it is not the default value it means that automatic backup is enabled
        if(backupInterval != -1){
            enableAutomaticBackup.setChecked(true);
            txtBackupInterval.setText(String.valueOf(backupInterval));
            linearLayoutBackupInterval.setVisibility(View.VISIBLE);
        }

        beforeStartDate.setSelection(beforeStartDate.getText().length());
        beforeStartDate.setSelectAllOnFocus(true);
        beforeDueDate.setSelection(beforeDueDate.getText().length());
        beforeDueDate.setSelectAllOnFocus(true);
        notSpecefiedStartDate.setSelection(notSpecefiedStartDate.getText().length());
        notSpecefiedStartDate.setSelectAllOnFocus(true);
        notSpecDurationTaskEdit.setSelection(notSpecDurationTaskEdit.getText().length());
        notSpecDurationTaskEdit.setSelectAllOnFocus(true);

        startDateSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    settingsModel.setAlwaysNotifyStartTime(true);
                    db.updateSettings(settingsModel);
                } else {
                    settingsModel.setAlwaysNotifyStartTime(false);
                    db.updateSettings(settingsModel);
                }
            }
        });
        dueDateSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    settingsModel.setAlwaysNotifyDeadLine(true);
                    db.updateSettings(settingsModel);
                } else {
                    settingsModel.setAlwaysNotifyDeadLine(false);
                    db.updateSettings(settingsModel);
                }
            }
        });

        enableSoundSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                /*Toast toast = Toast.makeText(getActivity(),
                        "enable sound:" + String.valueOf(isChecked), Toast.LENGTH_SHORT);
                toast.show();*/

                ShPrefUtils.setSoundsPlay(getActivity(), isChecked);

                if (isChecked) {
                    //settingsModel.setAlwaysNotifyDeadLine(true);
                    //db.updateSettings(settingsModel);

                } else {
                    //settingsModel.setAlwaysNotifyDeadLine(false);
                    //db.updateSettings(settingsModel);
                }
            }
        });

        txtBackupInterval.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if ((event.getAction() == KeyEvent.ACTION_DOWN) && (keyCode == KeyEvent.KEYCODE_ENTER)) {
                    String inputInterval = txtBackupInterval.getText().toString();
                    try{
                        int interval = Integer.parseInt(inputInterval);
                        if(interval > 0){
                            PreferenceManager.getDefaultSharedPreferences(getActivity().getApplicationContext())
                                    .edit().putInt(Constants.BACKUP_INTERVAL_KEY, interval).commit();
                            AutomaticBackup.start(getActivity(), false);
                            Toast.makeText(getActivity(), "Successfully set backup interval to " + interval + " hours", Toast.LENGTH_LONG).show();
                        }else{
                            Toast.makeText(getActivity(), "Enter a positive number bigger than 0!", Toast.LENGTH_LONG).show();
                        }
                    }catch(NumberFormatException e){
                        Toast.makeText(getActivity(), "Enter a valid number!", Toast.LENGTH_LONG).show();
                    }
                    return true;
                }

                return false;
            }
        });

        enableAutomaticBackup.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    Toast.makeText(getActivity(), "After the backup now, please set the interval for automatic backup.", Toast.LENGTH_LONG).show();
                    new GoogleDrive(getActivity()).backup();
                    txtBackupInterval.setText("X");
                    linearLayoutBackupInterval.setVisibility(View.VISIBLE);
                }else{
                    linearLayoutBackupInterval.setVisibility(View.GONE);
                    AutomaticBackup.stop(getActivity());
                }
            }
        });

        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        final InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(getView().getWindowToken(), 0);
    }

    public void setupUI(View view) {

        //Set up touch listener for non-text box views to hide keyboard.
        if (!(view instanceof EditText)) {

            view.setOnTouchListener(new View.OnTouchListener() {

                public boolean onTouch(View v, MotionEvent event) {
                    hideSoftKeyboard(getActivity());
                    return false;
                }

            });
        }

        //If a layout container, iterate over children and seed recursion.
        if (view instanceof ViewGroup) {

            for (int i = 0; i < ((ViewGroup) view).getChildCount(); i++) {

                View innerView = ((ViewGroup) view).getChildAt(i);

                setupUI(innerView);
            }
        }
    }

    protected void checkSettings() {
        try {
            if (db.getSettings().getSettingsId() != null) {
                settingsModel = db.getSettings();
            }
        } catch (Exception e) {
            settingsModel = new SettingsModel();
            db.addSettings(settingsModel);
        }
    }

    protected void storeSettings() {
        settingsModel.setNotifyStartTimeBefore(Integer.valueOf(beforeStartDate.getText().toString()));
        settingsModel.setNotifyDeadLineBefore(Integer.valueOf(beforeDueDate.getText().toString()));
        settingsModel.setINSSSD(Integer.valueOf(notSpecefiedStartDate.getText().toString()));
        settingsModel.setINSTD(Integer.valueOf(notSpecDurationTaskEdit.getText().toString()));
        db.updateSettings(settingsModel);
        MainActivity.settings = db.getSettings();
    }

    @Override
    public void onPause() {
        super.onPause();
        storeSettings();
    }
}
