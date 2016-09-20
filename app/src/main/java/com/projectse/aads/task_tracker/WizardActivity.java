package com.projectse.aads.task_tracker;

import android.app.Fragment;
import android.app.FragmentManager;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

import com.projectse.aads.task_tracker.DBService.DatabaseHelper;
import com.projectse.aads.task_tracker.Interfaces.WizardManager;
import com.projectse.aads.task_tracker.Models.TaskModel;
import com.projectse.aads.task_tracker.WizardFragments.AllocateFragment;
import com.projectse.aads.task_tracker.WizardFragments.IntroFragment;
import com.projectse.aads.task_tracker.WizardFragments.ManualAllocationFragment;
import com.projectse.aads.task_tracker.WizardFragments.PreviewFragment;
import com.projectse.aads.task_tracker.WizardFragments.TasksFragment;
import com.projectse.aads.task_tracker.WizardFragments.WeekFragment;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;

//TODO: goodbye wizard
/**
 * Created by smith on 4/19/16.
 */
public class WizardActivity extends AppCompatActivity implements WizardManager {
    private android.support.v7.widget.Toolbar toolbar;
    private double standard_duration = 0;
    public Map<Integer,Load> loadByDay = new HashMap<>();
    public List<TaskModel> selected_tasks = new ArrayList<>();

    private Calendar first_day_of_week = Calendar.getInstance();
    private Calendar last_day_of_week = Calendar.getInstance();

    private DatabaseHelper db;

    public void setWeek(Calendar first_day_of_week) {
        this.first_day_of_week = (Calendar) first_day_of_week.clone();
        this.last_day_of_week = (Calendar) first_day_of_week.clone();
        last_day_of_week.add(Calendar.DATE, 6);
    }

    public Calendar getFirstDayOfWeek() {
        return (Calendar) first_day_of_week.clone();
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

		public boolean addTask(TaskModel task) {
            this.tasks.add(task);
            if (getLeftScore() > 0) {
                return true;
            } else {
                return false;
            }
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

    public List<TaskModel> getTasksToWeek() {
        List<TaskModel> taskList = null;
        List<TaskModel> taskToAWeekList = null;
        DatabaseHelper db = DatabaseHelper.getsInstance(getApplicationContext());
        taskList = db.getOverdueTasks(first_day_of_week);

        Calendar last_day_of_plan = (Calendar)last_day_of_week.clone();
        last_day_of_plan.add(Calendar.DATE, 3);
        taskToAWeekList = db.getTasksBetweenDates(first_day_of_week, last_day_of_plan);

        for (TaskModel task: taskToAWeekList) {
            taskList.add(task);
        }

        return taskList;
    }

    public List<TaskModel> getAllTasks() {
        DatabaseHelper db = DatabaseHelper.getsInstance(getApplicationContext());

        List<TaskModel> taskList = null;
        taskList = db.getOverdueTasks(first_day_of_week);

        List<TaskModel> actualTaskList = db.getActualTasks(first_day_of_week);
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        db = DatabaseHelper.getsInstance(getApplicationContext());
        first_day_of_week.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
        setContentView(R.layout.activity_wizzard);

        //Set a toolbar to replace the Actionbar
        toolbar = (android.support.v7.widget.Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setCurrentFragment(new IntroFragment());
        Locale.setDefault(new Locale("en"));

        for(int day = Calendar.SUNDAY; day <= Calendar.SATURDAY; day++){
            loadByDay.put(day, new Load());
        }
    }

    public void setCurrentFragment(Fragment fragment) {
        FragmentManager fragmentManager = getFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.flContent, fragment).commit();
        fragmentManager.executePendingTransactions();
    }

    public void commitChanges(){
        Calendar date_cursor = (Calendar) first_day_of_week.clone();
        for(int day_of_week = Calendar.SUNDAY; day_of_week <= Calendar.SATURDAY; day_of_week++){
            Load load = loadByDay.get(day_of_week);
            date_cursor.set(Calendar.DAY_OF_WEEK,day_of_week);
            HashSet<String> errors = new HashSet<>();
            for(TaskModel task : load.getTasks()){
                try {
                    task.setStartTime(date_cursor);
                    db.updateTask(task);
                }catch (IllegalArgumentException e){
                    errors.add(e.getLocalizedMessage());
                }
            }
            if(!errors.isEmpty()){
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append(errors.size() + " task(s) haven't been committed chenges, because of: \n");
                for(String err : errors){
                    stringBuilder.append(err);
                    stringBuilder.append("\n");
                }
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setMessage(stringBuilder)
                        .setTitle(getString(R.string.warning));
                builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        closeWizard();
                    }
                });
                AlertDialog dialog = builder.create();
                dialog.show();
                return;
            }
        }
        closeWizard();
    }

    @Override
    public void closeWizard() {
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
        setCurrentFragment(new ManualAllocationFragment(selected_tasks));
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
                closeWizard();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void allocateToStart(){
        for(TaskModel task : selected_tasks){
            Calendar deadline = task.getDeadline();
            boolean added = false;

            Calendar day = (Calendar)first_day_of_week.clone();
            while (day.before(last_day_of_week) || day.equals(last_day_of_week)){
                Load load = loadByDay.get(day.get(Calendar.DAY_OF_WEEK));

                Calendar tomorrow = (Calendar) day.clone();
                tomorrow.add(Calendar.DATE, 1);
                if (deadline.equals(tomorrow) || deadline.before(day)) {
                    load.addTask(task);
                    added = true;
                    break;
                }

                if(!added && (load.getLeftScore()+1 > task.getDuration())){
                    load.addTask(task);
                    added = true;
                    break;
                }

                day.add(Calendar.DATE, 1);
            }

            if(!added) {
                day = (Calendar)first_day_of_week.clone();
                while (day.before(last_day_of_week) || day.equals(last_day_of_week)){
                    Load load = loadByDay.get(day);

                    if (load.getLeftScore()+1 > 0) {
                        load.addTask(task);
                        added = true;
                        break;
                    }

                    day.add(Calendar.DATE, 1);
                }
            }
        }

        callPreviewFragment();
    }

    public void allocateEvenly(){
        for(TaskModel task : selected_tasks){
            boolean added = false;
            Calendar deadline = task.getDeadline();

            double minLoad = 0;
            Calendar day = (Calendar)first_day_of_week.clone();
            while (day.before(last_day_of_week) || day.equals(last_day_of_week)){
                Load load = loadByDay.get(day.get(Calendar.DAY_OF_WEEK));

                if (deadline.after(day) && load.getLeftScore()+1 > minLoad) {
                    minLoad = load.getLeftScore()+1;
                }
                day.add(Calendar.DATE, 1);
            }

            day = (Calendar)first_day_of_week.clone();
            while (day.before(last_day_of_week) || day.equals(last_day_of_week)){
                Load load = loadByDay.get(day.get(Calendar.DAY_OF_WEEK));
                if (load.getLeftScore()+1 == minLoad) {
                    if (deadline.after(day)) {
                        load.addTask(task);
                        added = true;
                        break;
                    }
                }
                day.add(Calendar.DATE, 1);
            }

            if (!added) {
                day = (Calendar)first_day_of_week.clone();
                while (day.before(last_day_of_week) || day.equals(last_day_of_week)){
                    Load load = loadByDay.get(day.get(Calendar.DAY_OF_WEEK));

                    Calendar tomorrow = (Calendar) day.clone();
                    tomorrow.add(Calendar.DATE, 1);
                    if (deadline.equals(tomorrow) || deadline.before(day)) {
                        load.addTask(task);
                        added = true;
                        break;
                    }
                    day.add(Calendar.DATE, 1);
                }
            }
        }

        callPreviewFragment();
    }

    public void allocateToEnd(){
        for(TaskModel task : selected_tasks){
            boolean added = false;
            Calendar deadline = task.getDeadline();


            if (first_day_of_week.after(deadline)) {
                Load load = loadByDay.get(first_day_of_week.get(Calendar.DAY_OF_WEEK));
                load.addTask(task);
                added = true;
                break;
            }

            if(!added) {
                Calendar day = (Calendar)deadline.clone();
                day.add(Calendar.DATE, -1);

                while (day.after(first_day_of_week) || day.equals(first_day_of_week)) {
                    Load load = loadByDay.get(day.get(Calendar.DAY_OF_WEEK));
                    if (load.getLeftScore() + 1 > task.getDuration()) {
                        load.addTask(task);
                        added = true;
                        break;
                    }

                    day.add(Calendar.DATE, -1);
                }
            }


//            while (day.before(last_day_of_week) || day.equals(last_day_of_week)){
//                Calendar tomorrow = (Calendar) day.clone();
//                tomorrow.add(Calendar.DATE, 1);
//                if (deadline.equals(tomorrow) || deadline.before(day)) {
//                    Load load = loadByDay.get(Calendar.DAY_OF_WEEK);
//                    load.addTask(task);
//                    added = true;
//                    break;
//                }
//                day.add(Calendar.DATE, 1);
//            }
//
//            if (!added) {
//                day = (Calendar)first_day_of_week.clone();
//                while (day.after(first_day_of_week) || day.equals(first_day_of_week)){
//                    Load load = loadByDay.get(day.get(Calendar.DAY_OF_WEEK));
//
//                    if (load.getLeftScore()+1 > task.getDuration()) {
//                        load.addTask(task);
//                        added = true;
//                        break;
//                    }
//                    day.add(Calendar.DATE, -1);
//                }
//            }
//
//            if (!added) {
//                day = (Calendar)first_day_of_week.clone();
//                while (day.before(last_day_of_week) || day.equals(last_day_of_week)){
//                    Load load = loadByDay.get(day.get(Calendar.DAY_OF_WEEK));
//
//                    if (load.getLeftScore()+1 > 0) {
//                        load.addTask(task);
//                        added = true;
//                        break;
//                    }
//                    day.add(Calendar.DATE, 1);
//                }
//            }
        }
        callPreviewFragment();
    }

    public void allocateManually(){
        callManualAllocateFragment();
    }
}
