package com.projectse.aads.task_tracker;

import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ListView;

import com.projectse.aads.task_tracker.DBService.DatabaseHelper;
import com.projectse.aads.task_tracker.Models.TaskModel;

import java.util.ArrayList;

/**
 * Created by Anastasia A. Puzankova on 25-Feb-16.
 */
public class CourseListActivity extends AppCompatActivity {
    ArrayList<TaskModel> taskList = new ArrayList<>();
    StableArrayAdapter adapter = null;
    ListView listview = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        DatabaseHelper db = DatabaseHelper.getsInstance(getApplicationContext());

        setContentView(R.layout.activity_plan);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        //db.deleteTaskTable(db.getWritableDatabase());
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                callAddTaskActivity();
            }
        });
    }
}
