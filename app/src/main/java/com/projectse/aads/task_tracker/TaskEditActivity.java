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
import android.util.Log;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.projectse.aads.task_tracker.DBService.DatabaseHelper;

import java.util.Calendar;
import java.util.Locale;
import java.util.TimeZone;

/**
 * Shows fields for editing current Task
 */

public class TaskEditActivity extends TaskActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        db = DatabaseHelper.getsInstance(getApplicationContext());
        setContentView(R.layout.activity_task_edit);
        setupUI(findViewById(R.id.parentIdEdit));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
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
        long course_id = db.getCourseIdByTaskId(task_id);
        try {
            course = db.getCourse(course_id);
        } catch (Exception e) {
            e.printStackTrace();
        }
        TextView t = (TextView) findViewById(R.id.textSelectedCourse);
        t.setText("Course: "
                + course.getName());
        t.setBackgroundColor(course.getClr());

        ScrollView sub_l = (ScrollView) findViewById(R.id.subtasksScrollView);
        sub_l.setVisibility(View.VISIBLE);
        if( task !=null && task.getParentTaskId()!=null && task.getParentTaskId() > 0 ) {
            sub_l.setVisibility(View.INVISIBLE);
        }
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
        if(flag){flag = false; return false;}

        if(dCal == null && stCal == null)
            return false;
        // Case: deadline cannot be earlier than now.
        // Set default.
        if(Calendar.getInstance(TimeZone.getTimeZone("UTC"), Locale.getDefault()).after(dCal)){
            flag = true;
            dCal = Calendar.getInstance(TimeZone.getTimeZone("UTC"), Locale.getDefault());
            dCal.set(Calendar.HOUR_OF_DAY,23);
            dCal.set(Calendar.MINUTE,59);
            dCal.set(Calendar.SECOND,59);
            setDateTime(deadlineDateView, null, dCal.getTimeInMillis());
            setDateTime(null, deadlineTimeView, dCal.getTimeInMillis());
            isCorrected = true;
        }
        // Case: if duration more than difference of deadline and startTime.
        // Don't allow this. Set duration 0.
        if (!durationView.getText().toString().equals("")) {
            if (dCal.getTime().getTime() - stCal.getTime().getTime() < Long.parseLong(durationView.getText().toString()) * 60 * 60 * 1000) {
                flag = true;
                Toast.makeText(getApplicationContext(), "Duration cannot be more than defference between start time and deadline.", Toast.LENGTH_SHORT).show();
                durationView.setText(String.valueOf(0));
                isCorrected = true;
            }
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
        long sadas=db.updateCourseToTask(task.getId(), dialogFragmentBuilder.getCourseId());
        Log.d("UPDATE COURSE",sadas+"");
        setResult(RESULT_OK);
        super.onDestroy();
        finish();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                DatabaseHelper db = DatabaseHelper.getsInstance(getApplicationContext());
                db.updateTask(task);
                Intent intent = new Intent();
                intent.putExtra("task_id", task.getId());
                setResult(RESULT_OK, intent);
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public static void hideSoftKeyboard(Activity activity) {
        InputMethodManager inputMethodManager = (InputMethodManager)  activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(), 0);
    }

    public void setupUI(View view) {

        //Set up touch listener for non-text box views to hide keyboard.
        if(!(view instanceof EditText)) {

            view.setOnTouchListener(new View.OnTouchListener() {

                public boolean onTouch(View v, MotionEvent event) {
                    hideSoftKeyboard(TaskEditActivity.this);
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
}
