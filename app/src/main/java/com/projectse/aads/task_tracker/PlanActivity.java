package com.projectse.aads.task_tracker;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.method.CharacterPickerDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.projectse.aads.task_tracker.DBService.DatabaseHelper;
import com.projectse.aads.task_tracker.Models.TaskModel;


import junit.framework.Assert;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Shows list of tasks
 */
public class PlanActivity extends AppCompatActivity {
    ArrayList<TaskModel> taskList = new ArrayList<>();
    StableArrayAdapter adapter = null;
    ListView listview = null;
    DatabaseHelper db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        db = DatabaseHelper.getsInstance(getApplicationContext());
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
        setSpinner();
        Log.d("d", log());
    }

    public void setSpinner(){
        Spinner dropdown = (Spinner)findViewById(R.id.spinner1);
        String[] sortParams = new String[]{"Start Time", "Deadline", "Priority"};
        ArrayAdapter<String> adapter2 = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_dropdown_item, sortParams);
        dropdown.setAdapter(adapter2);

        dropdown.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {
                    case 0: {
                        Toast.makeText(getApplicationContext(), "Sorted by start time", Toast.LENGTH_LONG).show();
                        break;
                    }
                    case 1: {
                        taskList = (ArrayList<TaskModel>) db.getTaskModelList();
                        Collections.sort(taskList);
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                adapter.notifyDataSetChanged();
                            }
                        });
                        Toast.makeText(getApplicationContext(), "Sorted by deadline", Toast.LENGTH_LONG).show();
                        break;
                    }
                    case 3: {
                        Toast.makeText(getApplicationContext(), "Sorted by priority", Toast.LENGTH_LONG).show();
                        break;
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }

    private String log() {
        //final DatabaseHelper db = DatabaseHelper.getsInstance(this);
        List<TaskModel> list = db.getTaskModelList();
        StringBuilder s = new StringBuilder();
        s.append("The database content\n===================================\n");
        s.append("Start times\n===================================\n");
        for (TaskModel t : list){
            s.append(t.getName() + "   " + t.getStartTime().getTime().toString() + "\n");
        }
        s.append("Deadlines\n===================================\n");
        for (TaskModel t : list){
            s.append(t.getName() + "   " + t.getDeadline().getTime().toString() + "\n");
        }
        s.append("============================================\n");
        return s.toString();
    }

    @Override
    protected void onResume() {
        super.onResume();
        //final DatabaseHelper db = DatabaseHelper.getsInstance(this);

        ListView listview = (ListView) findViewById(R.id.listview);
        taskList = (ArrayList<TaskModel>) db.getTaskModelList();
        adapter = new StableArrayAdapter(this,
                android.R.layout.simple_list_item_1, taskList);

        listview.setAdapter(adapter);
        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, final View view,
                                    int position, long id) {
                final TaskModel item = (TaskModel) parent.getItemAtPosition(position);
                callTaskOverviewActivity(item);
           }

       });
    }

    /**
     * sub class for taking list item
     */
    private class StableArrayAdapter extends ArrayAdapter<TaskModel> {

        HashMap<TaskModel, Integer> mIdMap = new HashMap<TaskModel, Integer>();

        public StableArrayAdapter(Context context, int textViewResourceId,
                                  ArrayList<TaskModel> objects) {
            super(context, textViewResourceId, objects);
            for (int i = 0; i < objects.size(); ++i) {
                mIdMap.put(objects.get(i), i);
            }
        }

        @Override
        public long getItemId(int position) {
            TaskModel item = getItem(position);
            return mIdMap.get(item);
        }

        @Override
        public boolean hasStableIds() {
            return true;
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_plan, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * sending task object and starting Edit Task activity
     * @param task
     */
    public void callEditTaskActivity(TaskModel task){
        Intent intent = new Intent (getApplicationContext(), TaskEditActivity.class);
        intent.putExtra("task_id", task.getId());
//        onPause();
        startActivityForResult(intent,0);
        adapter.notifyDataSetChanged();
//        onResume();
    }

    public void callAddTaskActivity(){
        Intent intent = new Intent (getApplicationContext(), AddTaskActivity.class);
        startActivity(intent);
    }

    public void callTaskOverviewActivity(TaskModel taskModel){
        Intent intent = new Intent (getApplicationContext(), TaskOverviewActivity.class);
        intent.putExtra("task_id", taskModel.getId());
        startActivityForResult(intent,0);
        adapter.notifyDataSetChanged();
    }
}
