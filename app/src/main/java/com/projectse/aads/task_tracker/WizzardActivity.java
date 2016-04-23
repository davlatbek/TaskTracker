package com.projectse.aads.task_tracker;

import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

import com.projectse.aads.task_tracker.DBService.DatabaseHelper;
import com.projectse.aads.task_tracker.Interfaces.WizzardManager;
import com.projectse.aads.task_tracker.Models.TaskModel;
import com.projectse.aads.task_tracker.WizzardFragments.AllocateFragment;
import com.projectse.aads.task_tracker.WizzardFragments.IntroFragment;
import com.projectse.aads.task_tracker.WizzardFragments.PreviewFragment;
import com.projectse.aads.task_tracker.WizzardFragments.TasksFragment;
import com.projectse.aads.task_tracker.WizzardFragments.WeekFragment;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by smith on 4/19/16.
 */
public class WizzardActivity extends AppCompatActivity implements WizzardManager {
    private android.support.v7.widget.Toolbar toolbar;
    private double standard_duration = 0;
    public Map<Integer,Load> loadByDay = new HashMap<>();
    public List<TaskModel> selected_tasks = new ArrayList<>();

    public void setWeek(Calendar first_day_of_week) {
        this.first_day_of_week = first_day_of_week;
    }

    private Calendar first_day_of_week = Calendar.getInstance();

    private List<TaskModel> getTasksToWeek() {
        List<TaskModel> taskList = null;
        List<TaskModel> taskToAWeekList = null;
        DatabaseHelper db = DatabaseHelper.getsInstance(getApplicationContext());
        taskList = db.getOverdueTasks(first_day_of_week);

       // taskToAWeekList = db.getTasksBetweenDates(first_day_of_week, )

        return taskList;
    }

    private List<TaskModel> getAllTasks() {
        List<TaskModel> actualTaskList = null;
        List<TaskModel> taskList = null;
        DatabaseHelper db = DatabaseHelper.getsInstance(getApplicationContext());
        actualTaskList = db.getActualTasks(first_day_of_week);
        taskList = db.getOverdueTasks(first_day_of_week);

        for (TaskModel task: actualTaskList) {
            taskList.add(task);
        }

        return taskList;
    }

    public void calculateDefaultDuration() {
        double total = 0;
        int count = 0;
        double minScore = 8.0; // 8 free hours per day
        for(Integer day : loadByDay.keySet()){
            Load load = loadByDay.get(day);
            total += load.getScore();

            if (minScore > load.getScore()) {
                minScore = load.getScore();
            }
        }

        for(TaskModel task : selected_tasks){
            if( !(task.getDuration() > 0) ){
                count++;
            }else{
                total -= task.getDuration();
            }
        }

        standard_duration = total/count;
        if (standard_duration > minScore) {
            standard_duration = minScore;
        }
    }

    public class Load{
        private List<TaskModel> tasks = new ArrayList<>();
        private double score;

        public List<TaskModel> getTasks() {
            return tasks;
        }

        public void setTasks(List<TaskModel> tasks) {
            this.tasks = tasks;
        }

        public boolean addTask(TaskModel task){
            if(
                    ( (getLeftScore() > task.getDuration()) && (task.getDuration() > 0))
                    ||
                            ( (getLeftScore() > standard_duration) && (task.getDuration() == 0))
                    ) {
                this.tasks.add(task);
                return true;
            }else
                return false;
        }

        public double getScore() {
            return score;
        }

        public double getLeftScore() {
            return score - getBusyHours();
        }

        public double getBusyHours(){
            double busy_hours = 0;
            for(TaskModel t : tasks){
                if(t.getDuration() > 0)
                    busy_hours += t.getDuration();
                else
                    busy_hours += standard_duration;
            }
            return busy_hours;
        }

        public void setScore(double score) {
            this.score = score;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        first_day_of_week.set(Calendar.DAY_OF_WEEK,Calendar.MONDAY);
        setContentView(R.layout.activity_wizzard);

        //Set a toolbar to replace the Actionbar
        toolbar = (android.support.v7.widget.Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setCurrentFragment(new IntroFragment());

        for(int day = Calendar.SUNDAY; day <= Calendar.SATURDAY; day++){
            loadByDay.put(day,new Load());
        }
    }

    public void setCurrentFragment(Fragment fragment) {
        FragmentManager fragmentManager = getFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.flContent, fragment).commit();
        fragmentManager.executePendingTransactions();
    }

    @Override
    public void closeWizzard() {
        Intent intent = new Intent();
//        intent.putExtra() if it's needed
        setResult(RESULT_OK, intent);
        finish();
    }

    @Override
    public void callIntroFragment() {
        setCurrentFragment(new IntroFragment());
    }

    @Override
    public void callWeekFragment() {
        setCurrentFragment(new WeekFragment());
    }

    @Override
    public void callTasksFragment() {
        setCurrentFragment(new TasksFragment());
    }

    @Override
    public void callAllocateFragment() {
        setCurrentFragment(new AllocateFragment());
    }

    @Override
    public void callManualAllocateFragment() {
        throw new InternalError("Implement this");
    }

    @Override
    public void callPreviewFragment() {
        PreviewFragment frag =  new PreviewFragment();
        setCurrentFragment(frag);
        frag.setWeek(first_day_of_week);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                closeWizzard();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void allocateToStart(){
        //TODO allocate
        boolean notAddedTasksFinded = false;
        for(TaskModel task : selected_tasks){
            boolean added = false;
            for(int day = Calendar.MONDAY; day <= Calendar.SATURDAY; day++){
                Load load = loadByDay.get(day);
                if(load.addTask(task)){
                    added = true;
                    break;
                }
            }
            if(!added) {
                Load sunday = loadByDay.get(Calendar.SUNDAY);
                added = sunday.addTask(task);
            }
            if(!added)
                notAddedTasksFinded = true;
        }
        //TODO react, if notAddedTasksFinded = true
        callPreviewFragment();
    }

    public void allocateEvenly(){
        //TODO allocate
        callPreviewFragment();
    }

    public void allocateToEnd(){
        //TODO allocate
        boolean notAddedTasksFinded = false;
        for(TaskModel task : selected_tasks){
            boolean added = false;
            Load sunday = loadByDay.get(Calendar.SUNDAY);
            added = sunday.addTask(task);

            if(!added) {
                for(int day = Calendar.SATURDAY; day >= Calendar.MONDAY; day--){
                    Load load = loadByDay.get(day);
                    if(load.addTask(task)){
                        added = true;
                        break;
                    }
                }
            }
            if(!added)
                notAddedTasksFinded = true;
        }
        //TODO react, if notAddedTasksFinded = true
        callPreviewFragment();
    }

    public void allocateManually(){
        callManualAllocateFragment();
    }
}
