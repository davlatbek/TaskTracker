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
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.projectse.aads.task_tracker.DBService.DatabaseHelper;
import com.projectse.aads.task_tracker.Models.TaskModel;


import junit.framework.Assert;

import java.util.ArrayList;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        DatabaseHelper db = DatabaseHelper.getsInstance(getApplicationContext());
        ArrayList<Long> subts = new ArrayList<>();
        TaskModel t1 = new TaskModel();
        t1.setName("TestTask1");
        t1.setId(db.addTask(t1));
        subts.add(t1.getId());

        TaskModel t2 = new TaskModel();
        t2.setName("TestTask2");
        t2.setId(db.addTask(t2));
        subts.add(t2.getId());

        TaskModel t = new TaskModel();
        t.setName("TestTaskMaster");
        t.setId(db.addTask(t));

        List<TaskModel> list = db.getTaskModelList();
        Assert.assertTrue(db.getTask(t.getId()).getSubtasks_ids().size() == 0);

//        for(Long id : subts ){
//            TaskModel t_buf = db.getTask(id);
//            t.addSubtask(t_buf);
//        }
        t.setSubtasks_ids(subts);
        try {
            db.updateTask(t);
        } catch (Exception e) {
            e.printStackTrace();
        }
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

    @Override
    protected void onResume() {
        super.onResume();
        final DatabaseHelper db = DatabaseHelper.getsInstance(this);

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
