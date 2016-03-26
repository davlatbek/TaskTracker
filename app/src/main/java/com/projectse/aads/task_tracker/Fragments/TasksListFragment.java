package com.projectse.aads.task_tracker.Fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;

import com.projectse.aads.task_tracker.Adapters.PlanAdapter;
import com.projectse.aads.task_tracker.DBService.DatabaseHelper;
import com.projectse.aads.task_tracker.Models.TaskModel;
import com.projectse.aads.task_tracker.R;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by smith on 3/21/16.
 */
public class TasksListFragment extends Fragment {
    DatabaseHelper db;
    PlanAdapter tasks_adapter;

    /**
     * Current day's task hierarchy
     */
    Map<TaskModel, List<TaskModel>> task_hierarchy = new HashMap<>();

    Map<Integer,List<TaskModel>> week_task_list = new HashMap<>();


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        db = DatabaseHelper.getsInstance(getActivity().getApplicationContext());
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_tasks_list, container, false);
        ExpandableListView listView = (ExpandableListView) view.findViewById(R.id.listView);
        tasks_adapter = new PlanAdapter(getActivity(),task_hierarchy);
        listView.setAdapter(tasks_adapter);
        return view;
    }

    public void setWeekData(Calendar week_first_day){
        Calendar date = (Calendar) week_first_day.clone();
        week_task_list.clear();

        for(int i = 0; i < 7; i++){
            week_task_list.put(date.get(Calendar.DAY_OF_WEEK),db.getTasksForDay(date));
            date.add(Calendar.DAY_OF_MONTH,1);
        }
    }

    public void setCurrentDayOfWeek(int dayOfWeek){
        if(dayOfWeek >= Calendar.SUNDAY && dayOfWeek <= Calendar.SATURDAY)
            setTaskHierarchy(week_task_list.get(dayOfWeek));
        else throw new IllegalArgumentException();
    }

    private void setTaskHierarchy(List<TaskModel> taskList){
        task_hierarchy.clear();
        if(taskList == null)
            return;
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
        tasks_adapter.notifyDataSetChanged();
    }
}