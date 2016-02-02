package com.projectse.aads.task_tracker;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.projectse.aads.task_tracker.Utils.Task;
import com.projectse.aads.task_tracker.Utils.TaskModel;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

/**
 * Shows list of tasks
 */
public class PlanActivity extends AppCompatActivity {

    ArrayList<Task> taskList = new ArrayList<>();

    StableArrayAdapter adapter = null;
    ListView listview = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_plan);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        listview = (ListView) findViewById(R.id.listview);

        taskList = (ArrayList<Task>) TaskModel.toList();
        adapter = new StableArrayAdapter(getBaseContext(),android.R.layout.simple_list_item_1, taskList);
        listview.setAdapter(adapter);

        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, final View view,
                                    int position, long id) {
                final Task item = (Task) parent.getItemAtPosition(position);
                callEditTaskActivity(item);
            }

        });
    }

    /**
     * sub class for taking list item
     */
    private class StableArrayAdapter extends ArrayAdapter<Task> {

        HashMap<Task, Integer> mIdMap = new HashMap<Task, Integer>();

        public StableArrayAdapter(Context context, int textViewResourceId,
                                  List<Task> objects) {
            super(context, textViewResourceId, objects);
            for (int i = 0; i < objects.size(); ++i) {
                mIdMap.put(objects.get(i), i);
            }
        }

        @Override
        public long getItemId(int position) {
            Task item = getItem(position);
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
    public void callEditTaskActivity(Task task){
        Intent intent = new Intent (getApplicationContext(), TaskEditActivity.class);
        intent.putExtra("task_id", task.getId());
//        onPause();
        startActivityForResult(intent,0);
        adapter.notifyDataSetChanged();
//        onResume();
    }
}
