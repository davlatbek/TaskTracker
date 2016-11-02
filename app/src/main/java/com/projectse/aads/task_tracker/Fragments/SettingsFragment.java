package com.projectse.aads.task_tracker.Fragments;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Switch;

import com.projectse.aads.task_tracker.DBService.DatabaseHelper;
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
    EditText beforeStartDate;
    EditText beforeDueDate;
    EditText notSpecefiedStartDate;
    EditText notSpecDurationTaskEdit;

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

        // Switches
        startDateSwitch = (Switch) view.findViewById(R.id.startDateSwitch);
        dueDateSwitch = (Switch) view.findViewById(R.id.dueDateSwitch);

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
