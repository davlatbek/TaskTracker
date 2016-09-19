package com.projectse.aads.task_tracker.Fragments;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.ListFragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.Toast;

import com.projectse.aads.task_tracker.DBService.DatabaseHelper;
import com.projectse.aads.task_tracker.Interfaces.AddTaskCaller;
import com.projectse.aads.task_tracker.Interfaces.ParentFragment;
import com.projectse.aads.task_tracker.Interfaces.WizardCaller;
import com.projectse.aads.task_tracker.Models.TaskModel;
import com.projectse.aads.task_tracker.R;
import com.projectse.aads.task_tracker.RequestCode;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashSet;
import java.util.List;

/**
 * Created by Andrey Zolin on 20.03.2016.
 */
public class PlanFragment extends Fragment
        implements WeekSliderFragment.onWeekSliderEventListener, WeekDaysFragment.onSomeWeekDaysListener,
        ParentFragment
{
    private static View view;
    private WeekDaysFragment weekDaysFragment;
    private TasksListFragment tasksListFragment;
    private WeekSliderFragment sliderFragment;
    private AddTaskCaller addTaskCaller;
    private WizardCaller wizardCaller;

    /**EDIT MODE**/
    private List<TaskModel> selectedTasks = new ArrayList<>();
    private boolean isEditMode;
    private DatabaseHelper db;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        if (activity instanceof AddTaskCaller) {
            addTaskCaller = (AddTaskCaller) activity;
        }
        if (activity instanceof WizardCaller) {
            wizardCaller = (WizardCaller) activity;
        }
    }

    private View.OnClickListener requestButtonListener
            = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Calendar date = (Calendar) sliderFragment.getWeekFirstDay().clone();
            date.set(Calendar.DAY_OF_WEEK,weekDaysFragment.getCurrentDay());
            addTaskCaller.callAddTask(-1L, date);
        }
    };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        db = DatabaseHelper.getsInstance(getActivity().getApplicationContext());
        FragmentManager fm = getChildFragmentManager();

        sliderFragment = new WeekSliderFragment();
        sliderFragment.setSomeEventListener(this);
        fm.beginTransaction().replace(R.id.fragment_week_slider, sliderFragment).commit();

        tasksListFragment = new TasksListFragment();
        fm.beginTransaction().replace(R.id.fragment_tasks_list, tasksListFragment).commit();

        weekDaysFragment = new WeekDaysFragment();
        weekDaysFragment.setSomeEventListener(this);
        fm.beginTransaction().replace(R.id.fragment_week_days, weekDaysFragment).commit();

        fm.executePendingTransactions();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        getActivity().setTitle("Plan");
        if (view != null) {
            ViewGroup parent = (ViewGroup) view.getParent();
            if (parent != null)
                parent.removeView(view);
        }
        view = inflater.inflate(R.layout.fragment_plan_view, null);

        setHasOptionsMenu(true);
        return view;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        getActivity().getMenuInflater().inflate(R.menu.menu_plan_edit, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:
                return true;
        }
        if (item.getTitle().equals("editplan")) {
//            if(wizardCaller != null) wizardCaller.callWizard();
            isEditMode = isEditMode?false:true; //change the state
            if(isEditMode) {
                item.setIcon(R.drawable.save);
                tasksListFragment.collapseAll();
                tasksListFragment.getTasksAdapter().setPlanFragment(this);
                tasksListFragment.getTasksAdapter().setEditMode(true);

            }else {
                item.setIcon(R.drawable.edit_material);
                selectedTasks.clear();
                tasksListFragment.getTasksAdapter().setEditMode(false);
            }
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     *
     * @param weekDay
     */
    @Override
    public void setWeekDay(int weekDay) {
        if(weekDay >= Calendar.SUNDAY && weekDay <= Calendar.SATURDAY){
            if(isEditMode){
                Calendar date = sliderFragment.getWeekFirstDay();
                Calendar first_day = (Calendar) date.clone();
                List<TaskModel> unset_tasks = new ArrayList<>();
                HashSet<String> errors = new HashSet<>();
                for(TaskModel t : selectedTasks){
                    date.set(Calendar.DAY_OF_WEEK,weekDay);
                    try {
                        Calendar today = Calendar.getInstance();
                        today.set(
                                today.get(Calendar.YEAR),
                                today.get(Calendar.MONTH),
                                today.get(Calendar.DAY_OF_MONTH),
                                0,
                                0
                                );
                        if(date.compareTo(today)<0){
                            throw new IllegalArgumentException("You cannot manage start time in the past");
                        }
                        t.setStartTime(date);
                        for(Long id : t.getSubtasks_ids()){
                            TaskModel subtask = db.getTask(id);
                            subtask.setStartTime(date);
                            db.updateTask(subtask);
                        }
                        db.updateTask(t);
                    }catch (IllegalArgumentException e){
                        errors.add(e.getMessage());
                        unset_tasks.add(t);
                    }
                }
                if(unset_tasks.isEmpty())
                    Toast.makeText(getActivity(),"Tasks are moved successfully",Toast.LENGTH_LONG);
                else{
                    StringBuilder stringBuilder = new StringBuilder();
                    for(TaskModel t : unset_tasks){
                        stringBuilder.append("\n");
                        stringBuilder.append(t.getName());
                    }
                    StringBuilder errorsStringBuilder = new StringBuilder();
                    for(String err : errors) {
                        stringBuilder.append("\n");
                        errorsStringBuilder.append(err);
                    }
                    stringBuilder.append("\nBecause of: " + errorsStringBuilder);
                    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                    builder.setMessage(stringBuilder.toString())
                            .setTitle("Tasks aren't moved");
                    builder.setPositiveButton(R.string.ok, null);
                    AlertDialog dialog = builder.create();
                    dialog.show();
                }

                selectedTasks.clear();
                tasksListFragment.setWeekData(first_day);
                int day_of_week = weekDaysFragment.getCurrentDay(); //TODO don't move to selected day
                tasksListFragment.setCurrentDayOfWeek(day_of_week);
            }else {
                weekDaysFragment.setCurrentDay(weekDay);
                tasksListFragment.setCurrentDayOfWeek(weekDay);
            }
        }
    }

    @Override
    public void setWeek(Calendar date_src) {
        Calendar date = (Calendar) date_src.clone();

        if(sliderFragment != null)
            sliderFragment.updateLabel();
        if(tasksListFragment != null) {
            tasksListFragment.setWeekData(date);
            if (weekDaysFragment != null)
                setWeekDay(weekDaysFragment.getCurrentDay());
        }
    }

    /**
     * Child fragments are creating async-ly.
     * To manage async views creation call this method in onViewCreated() of child fragment.
     */
    @Override
    public void onChildCreated(){
        if(
                   weekDaysFragment.getView() != null
                && tasksListFragment.getView() != null
                && sliderFragment.getView() != null
                ) {

            Calendar week_first_day = Calendar.getInstance();
            week_first_day.setFirstDayOfWeek(Calendar.MONDAY);
            week_first_day.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
            setWeek(week_first_day);
            ImageButton addRequestButton = (ImageButton) getView().findViewById(R.id.create_task_btn);
            addRequestButton.setOnClickListener(requestButtonListener);
        }
    }

    public void addSelectedTask(TaskModel selectedTask) {
        this.selectedTasks.add(selectedTask);
    }

    public void removeSelectedTask(TaskModel selectedTask) {
        this.selectedTasks.remove(selectedTask);
    }
}
