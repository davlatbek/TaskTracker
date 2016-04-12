package com.projectse.aads.task_tracker.Fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Switch;

import com.projectse.aads.task_tracker.DBService.DatabaseHelper;
import com.projectse.aads.task_tracker.Models.SettingsModel;
import com.projectse.aads.task_tracker.R;

/**
 * Created by Andrey Zolin on 20.03.2016.
 */
public class SettingsFragment extends Fragment {
    protected SettingsModel settingsModel = new SettingsModel();
    DatabaseHelper db;
    Switch startDateSwitch;
    Switch dueDateSwitch;
    EditText beforeStartDate;
    EditText beforeDueDate;
    EditText notSpecefiedStartDate;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        getActivity().setTitle("Settings");
        db = new DatabaseHelper(getActivity().getApplicationContext());
        checkSettings();
        View view = inflater.inflate(R.layout.fargment_settings, container, false);
        // InputFields
        beforeStartDate = (EditText) view.findViewById(R.id.startTime);
        beforeDueDate = (EditText) view.findViewById(R.id.dueTime);
        notSpecefiedStartDate = (EditText) view.findViewById(R.id.notSpecefiedStartDate);

        // Switches
        startDateSwitch = (Switch) view.findViewById(R.id.startDateSwitch);
        dueDateSwitch = (Switch) view.findViewById(R.id.dueDateSwitch);

        settingsModel = db.getSettings();
        startDateSwitch.setChecked(settingsModel.getAlwaysNotifyStartTime());
        dueDateSwitch.setChecked(settingsModel.getAlwaysNotifyDeadLine());

        // set text fields
        beforeStartDate.setText(settingsModel.getNotifyStartTimeBefore() + "");
        beforeDueDate.setText(settingsModel.getNotifyDeadLineBefore() + "");
        notSpecefiedStartDate.setText(settingsModel.getINSSSD() + "");

        beforeStartDate.setSelection(beforeStartDate.getText().length());
        beforeDueDate.setSelection(beforeDueDate.getText().length());
        notSpecefiedStartDate.setSelection(notSpecefiedStartDate.getText().length());

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
        return view;
    }

    protected void checkSettings() {
        if (db.getSettings().getSettingsId() != null) {
            settingsModel = db.getSettings();
        } else {
            settingsModel = new SettingsModel();
            db.addSettings(settingsModel);
        }
    }

    protected void storeSettings() {
        settingsModel.setNotifyStartTimeBefore(Integer.valueOf(beforeStartDate.getText().toString()));
        settingsModel.setNotifyDeadLineBefore(Integer.valueOf(beforeDueDate.getText().toString()));
        settingsModel.setINSSSD(Integer.valueOf(notSpecefiedStartDate.getText().toString()));
        db.updateSettings(settingsModel);
    }

    @Override
    public void onPause() {
        super.onPause();
        storeSettings();
    }
}
