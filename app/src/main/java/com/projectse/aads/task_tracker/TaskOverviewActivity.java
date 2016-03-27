package com.projectse.aads.task_tracker;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;

import com.projectse.aads.task_tracker.DBService.DatabaseHelper;
import com.projectse.aads.task_tracker.Models.CourseModel;
import com.projectse.aads.task_tracker.Models.TaskModel;

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
    private Spinner spinnerPriority;

    //Database instance
    DatabaseHelper db = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(activity_taskoverview);
        courseView = (TextView) findViewById(R.id.textSelectedCourse);
        spinnerPriority = (Spinner) findViewById(R.id.spinnerPriority);
        final String[] paramPriorities = new String[]{"Low", "Medium", "High"};
        ArrayAdapter<String> priorityAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_dropdown_item, paramPriorities);
        spinnerPriority.setAdapter(priorityAdapter);

        courseView.setText("Course is not selected");

    }

    @Override
    protected void getViews() {
        super.getViews();
        switchFinished = (Switch) findViewById(R.id.switchFinished);
        editButton = (Button) findViewById(R.id.editTaskButton);
        deleteButton = (Button) findViewById(R.id.deleteTaskButton);
        courseView = (TextView) findViewById(R.id.textSelectedCourse);
        editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callEditTaskActivity(task);
            }
        });
        editButton.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN: {
                        v.getBackground().setColorFilter(0xe0f47521, PorterDuff.Mode.SRC_ATOP);
                        v.invalidate();
                        break;
                    }
                    case MotionEvent.ACTION_UP: {
                        v.getBackground().clearColorFilter();
                        v.invalidate();
                        break;
                    }
                }
                return false;
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
        spinnerPriority = (Spinner) findViewById(R.id.spinnerPriority);
        spinnerPriority.setFocusable(false);
        spinnerPriority.setEnabled(false);

        Button buttonSelectCourse = (Button) findViewById(R.id.selectCourse);
        buttonSelectCourse.setVisibility(View.INVISIBLE);

        Button addSubtasks = (Button) findViewById(R.id.btnAddSubtask);
        Button clearSubtasks = (Button) findViewById(R.id.btnClearSubtasks);


        addSubtasks.setVisibility(View.INVISIBLE);
        clearSubtasks.setVisibility(View.INVISIBLE);

    }

    private void createDeleteDialog() {
        final AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
        alertDialog.setMessage("Are you sure you want to delete this task?");

        alertDialog.setCancelable(false);
        alertDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (task.getSubtasks_ids().size() > 0) {
                    AlertDialog.Builder alertDialog1 = new AlertDialog.Builder(alertDialog.getContext());
                    alertDialog1.setMessage("Task contains subtasks.\n They also will be deleted. Are you sure?");

                    alertDialog1.setCancelable(false);
                    alertDialog1.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Long task_id = getIntent().getLongExtra("task_id", -1);
                            for (Long sub_id : task.getSubtasks_ids())
                                db.deleteTask(sub_id);
                            db.deleteTask(task_id);
                            Intent intent = new Intent();
                            intent.putExtra("deleted_task_id", task_id);
                            setResult(RESULT_OK, intent);
                            finish();
                        }
                    });

                    alertDialog1.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    });

                    alertDialog1.create().show();
                } else {
                    Long task_id = getIntent().getLongExtra("task_id", -1);
                    db.deleteTask(task_id);
                    Intent intent = new Intent();
                    intent.putExtra("deleted_task_id", task_id);
                    setResult(RESULT_OK, intent);
                    finish();
                }
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
        try {
            spinnerPriority.setSelection(task.priorityToInt(task.getPriority()));

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void callEditTaskActivity(TaskModel task) {
        Intent intent = new Intent(this, TaskEditActivity.class);
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
    protected void onPause() {
        super.onPause();
        Long task_id = getIntent().getLongExtra("task_id", -1);
        db = DatabaseHelper.getsInstance(getApplicationContext());
        task = db.getTask(task_id);
        Long course_id = db.getCourseIdByTaskId(task_id);
        checkCourse(course_id);
    }

    @Override
    protected void onStart() {
        super.onStart();
        Long task_id = getIntent().getLongExtra("task_id", -1);
        db = DatabaseHelper.getsInstance(getApplicationContext());
        task = db.getTask(task_id);
        Long course_id = db.getCourseIdByTaskId(task_id);
        checkCourse(course_id);
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Long task_id = getIntent().getLongExtra("task_id", -1);
        db = DatabaseHelper.getsInstance(getApplicationContext());
        task = db.getTask(task_id);
        Long course_id = db.getCourseIdByTaskId(task_id);
        checkCourse(course_id);
    }

    @Override
    protected synchronized void onResume() {
        db = DatabaseHelper.getsInstance(getApplicationContext());
        super.onResume();
        getViews();
        //getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        Long task_id = getIntent().getLongExtra("task_id", -1);
        task = db.getTask(task_id);
        Long course_id = db.getCourseIdByTaskId(task_id);
        checkCourse(course_id);
        if (task != null)
            fillData();
        switchFinished.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                task.setIsDone(isChecked);
            }
        });
    }

    public void checkCourse(long course_id) {
        try {
            course = db.getCourse(course_id);
            if (course_id > 0) {
                courseView.setText("Course: " + course.getName());
                courseView.setBackgroundColor(course.getClr());
            } else {
                courseView.setText("Course is not selected");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case RequestCode.REQ_CODE_EDITTASK:
                    Long task_id = data.getLongExtra("task_id", -1L);
                    task = db.getTask(task_id);
                    break;
            }
        }
    }
}
