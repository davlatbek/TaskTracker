package com.projectse.aads.task_tracker;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;
import android.widget.ExpandableListView;

import com.projectse.aads.task_tracker.Adapters.PlanAdapter;
import com.projectse.aads.task_tracker.DBService.DatabaseHelper;
import com.projectse.aads.task_tracker.Models.TaskModel;


import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Shows list of tasks
 */
public class PlanActivity extends AppCompatActivity {

    DatabaseHelper db;
    int sortMethod = 0;
    Spinner dropdownSorting;

    protected List<TaskModel> taskList = new ArrayList<>();
    protected PlanAdapter tasks_adapter = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        db = DatabaseHelper.getsInstance(getApplicationContext());

        /*ArrayList<Long> subts = new ArrayList<>();

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
        //Assert.assertTrue(db.getTask(t.getId()).getSubtasks_ids().size() == 0);

        for(Long id : subts ){
            TaskModel t_buf = db.getTask(id);
            t.addSubtask(t_buf);
        }
        t.setSubtasks_ids(subts);
        try {
            db.updateTask(t);
        } catch (Exception e) {
            e.printStackTrace();
            Assert.assertTrue(false);
        }*/

        setContentView(R.layout.activity_plan);
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                callAddTaskActivity();
            }
        });
        setSortSpinner();
        Log.d("d", log());
    }

    public void setSortSpinner() {
        dropdownSorting = (Spinner) findViewById(R.id.spinnerSortTasks);
        final String[] sortParams = new String[] {"Start Time", "Deadline", "Priority", "Name", "Duration"};
        ArrayAdapter<String> adapter2 = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_dropdown_item, sortParams);
        dropdownSorting.setAdapter(adapter2);

        dropdownSorting.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                sortMethod = position;
                onResume();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }

    private void sortTaskList(List<TaskModel> tasks, SortingMethod sortingMethod) {
        switch (sortingMethod) {
            case STARTDATE:
                Collections.sort(tasks, new Comparator<TaskModel>() {
                    @Override
                    public int compare(TaskModel lhs, TaskModel rhs) {
                        return Long.compare(lhs.getStartTime().getTime().getTime(), rhs.getStartTime().getTime().getTime());
                    }
                });
                break;
            case DEADLINE:
                Collections.sort(tasks, new Comparator<TaskModel>() {
                    @Override
                    public int compare(TaskModel lhs, TaskModel rhs) {
                        return Long.compare(lhs.getDeadline().getTime().getTime(), rhs.getDeadline().getTime().getTime());
                    }
                });
                break;
            case PRIORITY:
                Collections.sort(tasks, new Comparator<TaskModel>() {
                    @Override
                    public int compare(TaskModel lhs, TaskModel rhs) {
                        try {
                            return Integer.compare(lhs.priorityToInt(lhs.getPriority()), rhs.priorityToInt(rhs.getPriority()));
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        return 0;
                    }
                });
                break;
            case NAME:
                Collections.sort(tasks, new Comparator<TaskModel>() {
                    @Override
                    public int compare(TaskModel lhs, TaskModel rhs) {
                        return lhs.getName().compareTo(rhs.getName());
                    }
                });
                break;
            case DURATION:
                Collections.sort(tasks, new Comparator<TaskModel>() {
                    @Override
                    public int compare(TaskModel lhs, TaskModel rhs) {
                        return Long.compare(lhs.getDuration(), rhs.getDuration());
                    }
                });
                break;
        }
    }

    public enum SortingMethod {
        STARTDATE,
        DEADLINE,
        PRIORITY,
        NAME,
        DURATION
    }

    private String log() {
        List<TaskModel> list = db.getTaskModelList();
        StringBuilder s = new StringBuilder();
        s.append("The database content\n===================================\n");
        s.append("Start times\n===================================\n");
        for (TaskModel t : list) {
            s.append(t.getName() + "   " + t.getStartTime().getTime().toString() + "\n");
        }
        s.append("Deadlines\n===================================\n");
        for (TaskModel t : list) {
            s.append(t.getName() + "   " + t.getDeadline().getTime().toString() + "\n");
        }
        s.append("============================================\n");
        return s.toString();
    }

    @Override
    protected void onResume() {
        super.onResume();
        taskList = (ArrayList<TaskModel>) db.getTaskModelList();
        switch (sortMethod) {
            case 0:
                sortTaskList(taskList, SortingMethod.STARTDATE);
                Toast.makeText(getApplicationContext(),
                        "Sorted by start time", Toast.LENGTH_SHORT).show();
                break;
            case 1:
                sortTaskList(taskList, SortingMethod.DEADLINE);
                Toast.makeText(getApplicationContext(),
                        "Sorted by deadline", Toast.LENGTH_SHORT).show();
                break;
            case 2:
                sortTaskList(taskList, SortingMethod.PRIORITY);
                Toast.makeText(getApplicationContext(),
                        "Sorted by priority", Toast.LENGTH_SHORT).show();
                break;
            case 3:
                sortTaskList(taskList, SortingMethod.NAME);
                Toast.makeText(getApplicationContext(),
                        "Sorted by name", Toast.LENGTH_SHORT).show();
                break;
            case 4:
                sortTaskList(taskList, SortingMethod.DURATION);
                Toast.makeText(getApplicationContext(),
                        "Sorted by duration", Toast.LENGTH_SHORT).show();
                break;
        }

//        if(taskList.isEmpty())
//            taskList = db.getTaskModelList();

        ExpandableListView expListview = (ExpandableListView) findViewById(R.id.expListView);
        expListview.setIndicatorBounds(expListview.getWidth() - 40, expListview.getWidth());

        Map<TaskModel, List<TaskModel>> task_hierarchy = new HashMap<>();
        for (TaskModel task : taskList)
            if (task.isSupertask())
                task_hierarchy.put(task, new ArrayList<TaskModel>());
        for (TaskModel task : taskList)
            if (task.isSubtask()) {
                for (TaskModel super_task : task_hierarchy.keySet()) {
                    if (super_task.getId().compareTo(task.getParentTaskId()) == 0)
                        task_hierarchy.get(super_task).add(task);
                }
            }

        tasks_adapter = new PlanAdapter(this, task_hierarchy);
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

    public void callAddTaskActivity() {
        Intent intent = new Intent(getApplicationContext(), TaskAddActivity.class);
        startActivity(intent);
    }

    public void callTaskOverviewActivity(TaskModel taskModel) {
        Intent intent = new Intent(getApplicationContext(), TaskOverviewActivity.class);
        intent.putExtra("task_id", taskModel.getId());
        startActivityForResult(intent, 0);
        tasks_adapter.notifyDataSetChanged();
    }
}
