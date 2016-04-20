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
    private List<TaskModel> selected_tasks = new ArrayList<>();

    public void setWeek(Calendar first_day_of_week) {
        this.first_day_of_week = first_day_of_week;
    }

    private Calendar first_day_of_week = Calendar.getInstance();

    public void calculateDefaultDuration() {
        double total = 0;
        int count = 0;
        for(Integer day : loadByDay.keySet()){
            Load load = loadByDay.get(day);
            total += load.getScore();
            for(TaskModel task : load.tasks){
                if( !(task.getDuration() > 0) ){
                    count++;
                }else{
                    total -= task.getDuration();
                }
            }
        }
        standard_duration = total/count;
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
        throw new InternalError("Implement this");
    }

    @Override
    public void callManualAllocateFragment() {
        throw new InternalError("Implement this");
    }

    @Override
    public void callPreviewFragment() {
        setCurrentFragment(new PreviewFragment());
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
}
