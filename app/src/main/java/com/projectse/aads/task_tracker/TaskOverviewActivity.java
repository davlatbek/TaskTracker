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
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.Toast;
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
public class TaskOverviewActivity extends TaskActivity {

    // Views

    private Button deleteButton;
    private Button editButton;
    private Button buttonStartDate, buttonDeadline, buttonStartTime, buttonDeadlineTime;
    private Switch switchFinished;
    private Spinner spinner;

    //Database instance
    DatabaseHelper db = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(activity_taskoverview);

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

    @Override
    protected void getViews() {
        super.getViews();
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

        Button addSubtasks = (Button) findViewById(R.id.btnAddSubtask);
        Button clearSubtasks = (Button) findViewById(R.id.btnClearSubtasks);
        addSubtasks.setVisibility(View.INVISIBLE);
        clearSubtasks.setVisibility(View.INVISIBLE);
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

    @Override
    protected void fillData() {
        super.fillData();
        switchFinished.setChecked(task.getIsDone());
    }

    public void callEditTaskActivity(TaskModel task){
        Intent intent = new Intent (this, TaskEditActivity.class);
        intent.putExtra("task_id", task.getId());
        startActivityForResult(intent, RequestCode.REQ_CODE_EDITTASK);
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
        switchFinished.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                task.setIsDone(isChecked);
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                Intent intent = new Intent (this, PlanActivity.class);
                startActivity(intent);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(resultCode == RESULT_OK){
            switch (requestCode){
                case RequestCode.REQ_CODE_EDITTASK:
                    Long task_id = data.getLongExtra("task_id", -1L);
                    task = db.getTask(task_id);
                    break;
            }
        }
    }
}