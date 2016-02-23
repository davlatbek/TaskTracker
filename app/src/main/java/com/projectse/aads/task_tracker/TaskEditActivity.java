package com.projectse.aads.task_tracker;

import android.annotation.TargetApi;
import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.widget.CompoundButton;
import android.widget.Toast;

import com.projectse.aads.task_tracker.DBService.DatabaseHelper;

import java.util.Calendar;
/**
 * Shows fields for editing current Task
 */

public class TaskEditActivity extends TaskActivity {



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        db = DatabaseHelper.getsInstance(getApplicationContext());
        setContentView(R.layout.activity_task_edit);
//        getActionBar().setDisplayHomeAsUpEnabled(true);

        getViews();

    }

    private void setListeners(){
        nameView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}

            @Override
            public void afterTextChanged(Editable s) {
                task.setName(s.toString());
            }
        });
        descView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                task.setDescription(s.toString());
            }
        });
        durationView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if((s.toString()).matches("\\d+")) {
                    task.setDuration(Long.parseLong(s.toString()));
                }else{
                    Toast.makeText(getApplicationContext(), "Duration is number only" ,Toast.LENGTH_SHORT).show();
                }
            }
        });
        startTimeTimeView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
//                if(correctTime())
                correctTime();
                task.setStartTime(getCalendarFromTxtEditViews(startTimeDateView, startTimeTimeView));
            }
        });
        startTimeDateView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
//                if(correctTime())
                correctTime();
                task.setStartTime(getCalendarFromTxtEditViews(startTimeDateView, startTimeTimeView));
            }
        });
        deadlineTimeView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
//                if(correctTime())
                correctTime();
                task.setDeadline(getCalendarFromTxtEditViews(deadlineDateView, deadlineTimeView));
            }
        });
        deadlineDateView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
//                if(correctTime())
                correctTime();
                task.setDeadline(getCalendarFromTxtEditViews(deadlineDateView, deadlineTimeView));
            }
        });

        isStartTimeNotifyView.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                task.setIsNotifyStartTime(isChecked);
            }
        });
        isDeadlineNotifyView.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                task.setIsNotifyDeadline(isChecked);
            }
        });
    }

    @Override
    public void onResume(){
        super.onResume();
        Long task_id = getIntent().getLongExtra("task_id", -1);
        task = db.getTask(task_id);

        if (task != null) fillData();
        setListeners();
    }
    // flag for recursion exit. Use in correctTime only.
    private boolean flag = false;
    // return false, if all was correct
    public boolean correctTime(){
        Calendar dCal = getCalendarFromTxtEditViews(deadlineDateView,deadlineTimeView);
        Calendar stCal = getCalendarFromTxtEditViews(startTimeDateView,startTimeTimeView);
        boolean isCorrected = false;
        if(flag == true){flag = false; return false;}

        if(dCal == null && stCal == null)
            return false;
        // Case: deadline cannot be earlier than now.
        // Set default.
        if(Calendar.getInstance().after(dCal)){
            flag = true;
            dCal = Calendar.getInstance();
            dCal.set(Calendar.HOUR_OF_DAY,23);
            dCal.set(Calendar.MINUTE,59);
            dCal.set(Calendar.SECOND,59);
            setDateTime(deadlineDateView, null, dCal.getTimeInMillis());
            setDateTime(null, deadlineTimeView, dCal.getTimeInMillis());
            isCorrected = true;
        }
        // Case: if duration more than difference of deadline and startTime.
        // Don't allow this. Set duration 0.
        if(dCal.getTime().getTime() - stCal.getTime().getTime() < Long.parseLong(durationView.getText().toString())*60*60*1000){
            flag = true;
            Toast.makeText(getApplicationContext(), "Duration cannot be more than defference between start time and deadline.", Toast.LENGTH_SHORT).show();
            durationView.setText(String.valueOf(0));
            isCorrected = true;
        }
        // Case: start time after deadline.
        // Set starttime = deadline.
        if(stCal.after(dCal)){
            flag = true;
            setDateTime(startTimeDateView, null, dCal.getTimeInMillis());
            setDateTime(null, startTimeTimeView, dCal.getTimeInMillis());
            isCorrected = true;
        }
        return isCorrected;
    }

    @Override
    protected void onDestroy(){
        // write changes to base
        DatabaseHelper db = DatabaseHelper.getsInstance(getApplicationContext());
        db.updateTask(task);

        setResult(0);
        super.onDestroy();
        finish();
    }

    public void callPlanActivity(){
        Intent intent = new Intent (getApplicationContext(), PlanActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        startActivity(intent);
    }

    // Conformation form for deleting task
    private void callDeleteConfirmDialog() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
        alertDialog.setMessage("Are you sure you want to delete this task?");

        alertDialog.setCancelable(false);
        alertDialog.setPositiveButton(R.string.confirm, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Long task_id = getIntent().getLongExtra("task_id", -1);
                DatabaseHelper db = DatabaseHelper.getsInstance(getApplicationContext());
                db.deleteTask(task_id);
                callPlanActivity();
            }
        });

        alertDialog.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });

        alertDialog.create().show();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                DatabaseHelper db = DatabaseHelper.getsInstance(getApplicationContext());
                db.updateTask(task);
                Intent intent = new Intent(this, TaskOverviewActivity.class);
                intent.putExtra("task_id", task.getId());
                startActivity(intent);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
