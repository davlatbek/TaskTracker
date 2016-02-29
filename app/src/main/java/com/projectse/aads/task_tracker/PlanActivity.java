package com.projectse.aads.task_tracker;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ExpandableListView;

import com.projectse.aads.task_tracker.Adapters.PlanAdapter;
import com.projectse.aads.task_tracker.DBService.DatabaseHelper;
import com.projectse.aads.task_tracker.Models.TaskModel;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Shows list of tasks
 */
public class PlanActivity extends AppCompatActivity {
    protected List<TaskModel> taskList = new ArrayList<>();
    protected PlanAdapter tasks_adapter = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);


        
        setContentView(R.layout.activity_plan);
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

//        if(taskList.isEmpty())
//            taskList = db.getTaskModelList();

        ExpandableListView expListview = (ExpandableListView) findViewById(R.id.expListView);
        expListview.setIndicatorBounds(expListview.getWidth()-40,expListview.getWidth());

        Map<TaskModel,List<TaskModel>> task_hierarchy = new HashMap<>();
        for(TaskModel task : taskList)
            if(task.isSupertask())
                task_hierarchy.put(task,new ArrayList<TaskModel>());
        for(TaskModel task : taskList)
            if(task.isSubtask()) {
                for(TaskModel super_task : task_hierarchy.keySet()){
                    if(super_task.getId().compareTo(task.getParentTaskId()) == 0)
                        task_hierarchy.get(super_task).add(task);
                }
            }

        tasks_adapter = new PlanAdapter(this,task_hierarchy);
        expListview.setAdapter(tasks_adapter);
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

    public void callAddTaskActivity(){
        Intent intent = new Intent (getApplicationContext(), AddTaskActivity.class);
        startActivity(intent);
    }

    public void callTaskOverviewActivity(TaskModel taskModel){
        Intent intent = new Intent (getApplicationContext(), TaskOverviewActivity.class);
        intent.putExtra("task_id", taskModel.getId());
        startActivityForResult(intent, 0);
        tasks_adapter.notifyDataSetChanged();
    }
}
