package com.projectse.aads.task_tracker;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.nfc.Tag;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ScrollView;
import android.widget.Toast;

import com.projectse.aads.task_tracker.DBService.DatabaseHelper;
import com.projectse.aads.task_tracker.Models.CourseModel;
import com.projectse.aads.task_tracker.Models.TaskModel;
import com.projectse.aads.task_tracker.NotifyService.AlertReceiver;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

/**
 * Created by Davlatbek Isroilov on 1/31/2016.
 * Innopolis University
 */
public class TaskAddActivity extends TaskActivity {
    private Long parent_id = -1L;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addtask);
        setupUI(findViewById(R.id.parentId));
        //getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        db = DatabaseHelper.getsInstance(this);
        parent_id = getIntent().getLongExtra("parent_id", -1L);
        task = new TaskModel();
        course = new CourseModel();
        getViews();
        fillData();

        //List of courses dialog

    }

    @Override
    protected void getViews() {
        super.getViews();
        if (getIntent().getBooleanExtra("hide_subtasks", false)) {
            ScrollView sub_l = (ScrollView) findViewById(R.id.subtasksScrollView);
            sub_l.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * On-click button function for saving created task to database
     * and then going back to PlanActivity
     *
     * @param v
     */
    public void addAndSaveToDb(View v) {
        if (validateTaskFields()) {
            long task_id = addTaskToDatabase();
            long course_id = dialogFragmentBuilder.getCourseId();
            if (course_id != 0) {
                db.addCourseToTask(task_id);
                db.updateCourseToTask(task_id, course_id);
                Log.d("course id", course_id + "");
            }

            Intent intent = new Intent();
            intent.putExtra("task_id", (Long) task_id);
            setResult(RESULT_OK, intent);
            finish();
        }
    }

    /**
     * Validating new task properties
     *
     * @return True - if all required fileds are filled
     */
    public boolean validateTaskFields() {
        Toast toast = new Toast(getApplicationContext());
        toast.setGravity(Gravity.TOP | Gravity.LEFT, 0, 0);

        Calendar dCal = getCalendarFromTxtEditViews(deadlineDateView, deadlineTimeView);
        Calendar stCal = getCalendarFromTxtEditViews(startTimeDateView, startTimeTimeView);
        EditText durationView = (EditText) findViewById(R.id.txtDuration);
        EditText editName = (EditText) findViewById(R.id.txtName);

        if (editName.getText().toString().trim().equals("")) {
            toast.makeText(getApplicationContext(), "Enter the task name!", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (isNoSimilarTasks(editName.getText().toString())) {
            toast.makeText(getApplicationContext(), "Task with this name already exists!",
                    Toast.LENGTH_SHORT).show();
            return false;
        }

        if (deadlineDateView != null) {
            if (deadlineDateView.getText().toString().equals("")) {
                toast.makeText(getApplicationContext(), "Enter the deadline!",
                        Toast.LENGTH_SHORT).show();
                return false;
            }
            if (stCal != null && stCal.after(dCal)) {
                toast.makeText(getApplicationContext(), "Start date must be before deadline!",
                        Toast.LENGTH_SHORT).show();
                return false;
            }
        }

        //in case there is start time and deadline: duration must be less than deadline - start time
        if (stCal != null && !durationView.getText().toString().equals("") &&
                dCal.getTime().getTime() - stCal.getTime().getTime()
                        < Long.parseLong(durationView.getText().toString()) * 60 * 60 * 1000) {
            toast.makeText(getApplicationContext(),
                    "Duration can't be more than time between deadline and start time!",
                    Toast.LENGTH_SHORT).show();
            return false;
        }

        //in case there is no start time: duration must be less than deadline - current time
        if (stCal == null && !durationView.getText().toString().equals("") &&
                dCal.getTime().getTime() - Calendar.getInstance(TimeZone.getTimeZone("UTC"), Locale.getDefault()).getTime().getTime()
                        < Long.parseLong(durationView.getText().toString()) * 60 * 60 * 1000) {
            toast.makeText(getApplicationContext(),
                    "Duration can't be more than time between deadline and current time!",
                    Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    /**
     * Checks for two tasks with the same name
     *
     * @param newTaskName
     * @return
     */
    public boolean isNoSimilarTasks(String newTaskName) {
        List<TaskModel> list = db.getTaskModelList();
        for (TaskModel task : list) {
            if (task.getName().equals(newTaskName))
                return true;
        }
        return false;
    }

    /**
     * Creating task data and adding to local database
     *
     * @return True if all data is successfully recorded to database
     */
    public long addTaskToDatabase() {
        Calendar deadLineCal, startTimeCal;
        deadLineCal = getCalendarFromTxtEditViews(deadlineDateView, deadlineTimeView);

        if (startTimeDateView.getText().toString().equals("") && startTimeTimeView.getText().toString().equals(""))
            startTimeCal = Calendar.getInstance(TimeZone.getTimeZone("UTC"), Locale.getDefault());
        else
            startTimeCal = getCalendarFromTxtEditViews(startTimeDateView, startTimeTimeView);

        task.setName(nameView.getText().toString());
        task.setDescription(descView.getText().toString());
        task.setStartTime(startTimeCal);
        task.setDeadline(deadLineCal);
        task.setIsNotifyStartTime(isStartTimeNotifyView.isChecked());
        task.setIsNotifyDeadline(isDeadlineNotifyView.isChecked());
        if (!durationView.getText().toString().equals(""))
            task.setDuration(Long.parseLong(durationView.getText().toString()));

        if (!(parent_id < 0))
            task.setParentTaskId(parent_id);
        setAlarmNotif();
        return db.addTask(task);

    }


    public static void hideSoftKeyboard(Activity activity) {
        InputMethodManager inputMethodManager = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(), 0);
    }

    public void setAlarmNotif() {
        //time = time + 10 * 1000;
        Long time = new GregorianCalendar().getTimeInMillis() + 5 * 1000;

        Log.d("TIME_NOTIFICATIONS SET", time.toString() + "");
        Intent alertIntent = new Intent(this, AlertReceiver.class);
        AlarmManager alarmManager = (AlarmManager)
                getSystemService(Context.ALARM_SERVICE);
        alarmManager.set(AlarmManager.RTC_WAKEUP, time, PendingIntent.getBroadcast(this, 1, alertIntent, PendingIntent.FLAG_UPDATE_CURRENT));


    }

    public void setupUI(View view) {

        //Set up touch listener for non-text box views to hide keyboard.
        if (!(view instanceof EditText)) {

            view.setOnTouchListener(new View.OnTouchListener() {

                public boolean onTouch(View v, MotionEvent event) {
                    hideSoftKeyboard(TaskAddActivity.this);
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