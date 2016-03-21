package com.projectse.aads.task_tracker;

import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ListView;
import android.widget.AdapterView;


import com.projectse.aads.task_tracker.DBService.DatabaseHelper;
import com.projectse.aads.task_tracker.Models.TaskModel;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Anastasia A. Puzankova on 25-Feb-16.
 */
public class CoursePlanActivity extends AppCompatActivity {
    ArrayList<TaskModel> taskList = new ArrayList<>();
    //StableArrayAdapter adapter = null;
    ListView listview = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        DatabaseHelper db = DatabaseHelper.getsInstance(getApplicationContext());
        setContentView(R.layout.course_plan);

        List<TaskModel> list = db.getListOfTasks(1);
        ListView listview = (ListView) findViewById(R.id.list_of_tasks);
        taskList = (ArrayList<TaskModel>) db.getTaskModelList();
     //      adapter = new StableArrayAdapter(this,
     //             android.R.layout.simple_list_item_1, taskList);

     //     listview.setAdapter(adapter);
    }
}
