package com.projectse.aads.task_tracker;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.ToggleButton;

import com.projectse.aads.task_tracker.Utils.Task;

import java.sql.Date;
import java.sql.Time;

/**
 * Shows fields for editing current Task
 */
public class TaskEditActivity extends AppCompatActivity {

    // Views
    private EditText nameView,descView,
            startTimeDateView,startTimeTimeView,
            deadlineDateView,deadlineTimeView,
            durationView
            ;
    private Switch isStartTimeNotifyView, isDeadlineNotifyView;

    private ToggleButton isDoneView;

    // Current task
    private Task task = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_edit);
//        getActionBar().setDisplayHomeAsUpEnabled(true);

        getViews();

        task = (Task) (getIntent().getParcelableArrayExtra("task_object"))[0];

        if (task != null){
            fillData();
        }

    }

    private void fillData() {

        isDoneView.setChecked(task.getIsDone());

        if (task.getName() != null ) nameView.setText(task.getName());
        if (task.getDescription() != null ) descView.setText(task.getDescription());

        if (task.getStartTime() != null ) {
            startTimeDateView.setText((new Date(task.getStartTime().getTime())).toString());
            startTimeTimeView.setText((new Time(task.getStartTime().getTime())).toString());
        }

        isStartTimeNotifyView.setChecked(task.getIsNotifyStartTime());

        if (task.getDeadline() != null ) {
            deadlineDateView.setText((new Date(task.getDeadline().getTime())).toString());
            deadlineTimeView.setText((new Time(task.getDeadline().getTime())).toString());
        }

        isDeadlineNotifyView.setChecked(task.getIsNotifyDeadline());

        if (task.getDuration() != null ) durationView.setText(task.getDuration().toString());
    }

    private void getViews(){
        nameView = (EditText) findViewById(R.id.txtName);
        descView = (EditText) findViewById(R.id.txtDescription);

        startTimeDateView = (EditText) findViewById(R.id.txtDateStartTime);
        startTimeTimeView = (EditText) findViewById(R.id.txtTimeStartTime);

        deadlineDateView = (EditText) findViewById(R.id.txtDateDeadline);
        deadlineTimeView = (EditText) findViewById(R.id.txtTimeDeadline);

        durationView = (EditText) findViewById(R.id.txtDuration);

        isStartTimeNotifyView = (Switch) findViewById(R.id.swtStartTimeNotification);
        isDeadlineNotifyView = (Switch) findViewById(R.id.swtDeadlineNotification);

        isDoneView = (ToggleButton) findViewById(R.id.btnIsDone);
    }

}
