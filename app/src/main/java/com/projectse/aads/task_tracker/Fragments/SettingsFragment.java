package com.projectse.aads.task_tracker.Fragments;

import android.app.Fragment;
import android.media.audiofx.BassBoost;
import android.os.Bundle;
import android.util.Log;
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
    DatabaseHelper db;
    SettingsModel settingsModel;
    Switch startDateSwitch;
    Switch dueDateSwitch;
    EditText beforeStartDate;
    EditText beforeDueDate;
    EditText notSpecefiedStartDate;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        getActivity().setTitle("Settings");
        db = new DatabaseHelper(getActivity());
        settingsModel = db.getAllSettings();
        View view = inflater.inflate(R.layout.fargment_settings, container, false);
        // InputFields
        beforeStartDate = (EditText) view.findViewById(R.id.startTime);
        beforeDueDate = (EditText) view.findViewById(R.id.dueTime);
        notSpecefiedStartDate = (EditText) view.findViewById(R.id.notSpecefiedStartDate);
        // Switches
        startDateSwitch = (Switch) view.findViewById(R.id.startDateSwitch);
        dueDateSwitch = (Switch) view.findViewById(R.id.dueDateSwitch);
        startDateSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    // do something when check is selected
                    settingsModel.setAlwaysNotifyStartTime(true);
                } else {
                    settingsModel.setAlwaysNotifyStartTime(false);
                }
            }
        });
        dueDateSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    // do something when check is selected
                    settingsModel.setAlwaysNotifyDeadLine(true);
                } else {
                    //do something when unchecked
                    settingsModel.setAlwaysNotifyDeadLine(false);
                }
            }
        });
        setSettings();
        return view;
    }

    protected void setSettings() {
        //set switches
        settingsModel = db.getAllSettings();
        startDateSwitch.setChecked(settingsModel.getAlwaysNotifyStartTime());
        dueDateSwitch.setChecked(settingsModel.getAlwaysNotifyDeadLine());
        // set text fields
        beforeStartDate.setText(settingsModel.getNotifyStartTimeBefore()+"");
        beforeDueDate.setText(settingsModel.getNotifyDeadLineBefore()+"");
        notSpecefiedStartDate.setText(settingsModel.getINSSSD()+"");
    }

    @Override
    public void onPause() {
        super.onPause();
        long id = 0;
        settingsModel.setNotifyDeadLineBefore(Integer.valueOf(beforeStartDate.getText().toString()));
        settingsModel.setNotifyDeadLineBefore(Integer.valueOf(beforeDueDate.getText().toString()));
        settingsModel.setNotifyDeadLineBefore(Integer.valueOf(notSpecefiedStartDate.getText().toString()));
        id = db.setSettings(settingsModel);
        Log.i("WRITE TO DB SETTINGS", "write to db "+id);
    }
}
