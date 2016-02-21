package com.projectse.aads.task_tracker;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.ToggleButton;

import com.projectse.aads.task_tracker.DBService.DatabaseHelper;
import com.projectse.aads.task_tracker.Models.TaskModel;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import static com.projectse.aads.task_tracker.R.layout.activity_taskoverview;

/**
 * Created by Davlatbek Isroilov on 2/16/2016.
 * Innopolis University
 */
public class TaskOverviewActivity extends AppCompatActivity {

    // Views
    private EditText nameView;
    private EditText descView;
    private static EditText startTimeDateView;
    private static EditText startTimeTimeView;
    private static EditText deadlineDateView;
    private static EditText deadlineTimeView;
    private EditText durationView;
    private Switch isStartTimeNotifyView, isDeadlineNotifyView;
    private Button deleteButton;
    private Button editButton;
    private Button buttonStartDate, buttonDeadline, buttonStartTime, buttonDeadlineTime;
    private Switch switchFinished;
    private Spinner spinner;

    // Current task
    private static TaskModel task = null;

    //Database instance
    DatabaseHelper db = null;

    private static java.text.DateFormat dateFormat = new SimpleDateFormat("dd-MM-yy");
    private static java.text.DateFormat timeFormat = new SimpleDateFormat("HH:mm");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(activity_taskoverview);
        getViews();
        Long task_id = getIntent().getLongExtra("task_id", -1);
        db = DatabaseHelper.getsInstance(getApplicationContext());
        task = db.getTask(task_id);
        if (task != null)
            fillData();
        switchFinished.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                task.setIsDone(isChecked);
            }
        });

        /*editButton.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN: {
                        editButton.setBackgroundColor(Color.GREEN);
                        break;
                    }
                }
                return true;
            }
        });*/
    }

    private void getViews() {
        nameView = (EditText) findViewById(R.id.txtName);
        descView = (EditText) findViewById(R.id.txtDescription);
        startTimeDateView = (EditText) findViewById(R.id.txtDateStartTime);
        startTimeTimeView = (EditText) findViewById(R.id.txtTimeStartTime);
        deadlineDateView = (EditText) findViewById(R.id.txtDateDeadline);
        deadlineTimeView = (EditText) findViewById(R.id.txtTimeDeadline);
        isStartTimeNotifyView = (Switch) findViewById(R.id.swtStartTimeNotification);
        isDeadlineNotifyView = (Switch) findViewById(R.id.swtDeadlineNotification);
        durationView = (EditText) findViewById(R.id.txtDuration);
        switchFinished = (Switch) findViewById(R.id.switchFinished);
        editButton = (Button) findViewById(R.id.editTaskButton);
        deleteButton = (Button) findViewById(R.id.deleteTaskButton);
        editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callEditTaskActivity(task);
            }
        });
        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createDeleteDialog();
            }
        });
        nameView.setFocusable(false);
        descView.setFocusable(false);
        isDeadlineNotifyView.setFocusable(false);
        isStartTimeNotifyView.setFocusable(false);
        buttonStartDate = (Button) findViewById(R.id.btnDateStartTime);
        buttonStartTime = (Button) findViewById(R.id.btnTimeStartTime);
        buttonDeadline = (Button) findViewById(R.id.btnDateDeadline);
        buttonDeadlineTime = (Button) findViewById(R.id.btnTimeDeadline);
        buttonStartDate.setVisibility(View.INVISIBLE);
        buttonStartTime.setVisibility(View.INVISIBLE);
        buttonDeadline.setVisibility(View.INVISIBLE);
        buttonDeadlineTime.setVisibility(View.INVISIBLE);
        durationView.setFocusable(false);
        spinner = (Spinner) findViewById(R.id.spinnerCourseName);
        spinner.setFocusable(false);
    }

    private void createDeleteDialog() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
        alertDialog.setMessage("Are you sure you want to delete this task?");

        alertDialog.setCancelable(false);
        alertDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Long task_id = getIntent().getLongExtra("task_id",-1);
                db.deleteTask(task_id);
                callPlanActivity();
            }
        });

        alertDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });

        alertDialog.create().show();
    }

    private void fillData() {
        switchFinished.setChecked(task.getIsDone());
        if (task.getName() != null ) nameView.setText(task.getName());
        if (task.getDescription() != null ) descView.setText(task.getDescription());
        if (task.getStartTime() != null ) {
            setDateTime(startTimeDateView, startTimeTimeView, task.getStartTime().getTimeInMillis());
        }
        isStartTimeNotifyView.setChecked(task.getIsNotifyStartTime());
        if (task.getDeadline() != null ) {
            setDateTime(deadlineDateView, deadlineTimeView, task.getDeadline().getTimeInMillis());
        }
        isDeadlineNotifyView.setChecked(task.getIsNotifyDeadline());
        if (task.getDuration() > 0L ) durationView.setText(task.getDuration().toString());
    }

    /**
     * Set Calendar date to views.
     * @param dateTxt - date view.
     * @param timeTxt - time view.
     * @param calInMillis - time, that will be set.
     */
    private static void setDateTime(EditText dateTxt, EditText timeTxt,long calInMillis){
        Calendar time = Calendar.getInstance();
        time.setTimeInMillis(calInMillis);
        if(dateTxt != null) {
            dateTxt.setText(dateFormat.format(time.getTime()));
        }
        if(timeTxt != null) {
            timeTxt.setText(timeFormat.format(time.getTime()));
        }
    }

    public void callEditTaskActivity(TaskModel task){
        Intent intent = new Intent (this, TaskEditActivity.class);
        intent.putExtra("task_id", task.getId());
        startActivityForResult(intent, 0);
    }

    private void callPlanActivity() {
        db.updateTask(task);
        Intent intent = new Intent(this, PlanActivity.class);
        startActivity(intent);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
    }

    @Override
    protected void onResume() {
        super.onResume();
        getViews();
        Long task_id = getIntent().getLongExtra("task_id", -1);
        db = DatabaseHelper.getsInstance(getApplicationContext());
        task = db.getTask(task_id);
        if (task != null)
            fillData();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                db.updateTask(task);
                Intent intent = new Intent (this, PlanActivity.class);
                startActivity(intent);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
