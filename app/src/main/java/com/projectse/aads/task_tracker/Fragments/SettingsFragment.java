package com.projectse.aads.task_tracker.Fragments;

import android.app.Fragment;
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
        db = new DatabaseHelper(getActivity().getApplicationContext());

        View view = inflater.inflate(R.layout.fargment_settings, container, false);
        // InputFields
        beforeStartDate = (EditText) view.findViewById(R.id.startTime);
        beforeDueDate = (EditText) view.findViewById(R.id.dueTime);
        notSpecefiedStartDate = (EditText) view.findViewById(R.id.notSpecefiedStartDate);

        // Switches
        startDateSwitch = (Switch) view.findViewById(R.id.startDateSwitch);
        dueDateSwitch = (Switch) view.findViewById(R.id.dueDateSwitch);

        setSettings();
        beforeStartDate.setSelection(beforeStartDate.getText().length());
        beforeDueDate.setSelection(beforeDueDate.getText().length());
        notSpecefiedStartDate.setSelection(notSpecefiedStartDate.getText().length());
        startDateSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    settingsModel.setAlwaysNotifyStartTime(true);
                    storeSettings();
                } else {
                    settingsModel.setAlwaysNotifyStartTime(false);
                }
            }
        });
        dueDateSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    settingsModel.setAlwaysNotifyDeadLine(true);
                } else {
                    settingsModel.setAlwaysNotifyDeadLine(false);
                }
            }
        });
        return view;
    }

    protected void setSettings() {
        try {
            if (db.getSettings(1L).getSettingsId() != null) {
                settingsModel = db.getSettings(1L);
                startDateSwitch.setChecked(settingsModel.getAlwaysNotifyStartTime());
                dueDateSwitch.setChecked(settingsModel.getAlwaysNotifyDeadLine());
                // set text fields
                beforeStartDate.setText(settingsModel.getNotifyStartTimeBefore()+"");
                beforeDueDate.setText(settingsModel.getNotifyDeadLineBefore()+"");
                notSpecefiedStartDate.setText(settingsModel.getINSSSD()+"");
            } else {
                settingsModel = new SettingsModel();
                db.addSettings(settingsModel);
                Log.i("ADD ROW", "SETTINGS");
                setSettings();
            }
        } catch (Exception e)  {
            e.getStackTrace().toString();
        }
    }

    protected void storeSettings(){
        SettingsModel settingsModel2 = new SettingsModel();
        settingsModel2.setAlwaysNotifyDeadLine(true);
        settingsModel2.setAlwaysNotifyStartTime(true);
        settingsModel2.setINSSSD(3);
        settingsModel2.setNotifyDeadLineBefore(4);
        settingsModel2.setNotifyStartTimeBefore(4);
        settingsModel2.setSettingsId(1L);
        db.updateSettings(settingsModel2);
        Log.i("WRITE TO DB SETTINGS", "write to db ");
    }

    @Override
    public void onPause() {
        super.onPause();
        storeSettings();
    }
}
