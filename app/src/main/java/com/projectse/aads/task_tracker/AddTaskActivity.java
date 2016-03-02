package com.projectse.aads.task_tracker;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.Switch;
import android.widget.TimePicker;
import android.widget.Toast;

import com.projectse.aads.task_tracker.Adapters.SubtasksAdapter;
import com.projectse.aads.task_tracker.DBService.DatabaseHelper;
import com.projectse.aads.task_tracker.Models.TaskModel;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

/**
 * Created by Davlatbek Isroilov on 1/31/2016.
 * Innopolis University
 */
public class AddTaskActivity extends TaskActivity {
    private Long parent_id = -1L;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addtask);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        db = DatabaseHelper.getsInstance(this);
        parent_id = getIntent().getLongExtra("parent_id", -1L);
        task = new TaskModel();
        getViews();
        fillData();
    }

    @Override
    protected void getViews() {
        super.getViews();
        if(getIntent().getBooleanExtra("hide_subtasks",false)){
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
            Intent intent = new Intent();
            intent.putExtra("task_id", (Long)task_id);
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

        if(!(parent_id < 0))
            task.setParentTaskId(parent_id);

        return db.addTask(task);
    }
}