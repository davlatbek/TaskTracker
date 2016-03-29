package com.projectse.aads.task_tracker.Fragments;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import com.projectse.aads.task_tracker.DBService.DatabaseHelper;
import com.projectse.aads.task_tracker.Interfaces.AddTaskCaller;
import com.projectse.aads.task_tracker.Interfaces.ParentFragment;
import com.projectse.aads.task_tracker.R;

import java.util.Calendar;

public class ActualTasksFragment extends Fragment implements ParentFragment {
    DatabaseHelper db;
    private TasksListFragment tasksListFragment;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        db = DatabaseHelper.getsInstance(getActivity().getApplicationContext());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        getActivity().setTitle(R.string.actuals_title);
        View view = inflater.inflate(R.layout.fragment_course_overview, container, false);
        ImageButton addRequestButton = (ImageButton) view.findViewById(R.id.create_request_fab);
        addRequestButton.setVisibility(View.INVISIBLE);

        tasksListFragment = new TasksListFragment();
        FragmentManager fm = getChildFragmentManager();
        fm.beginTransaction().replace(R.id.task_list, tasksListFragment).commit();
        return view;
    }

    @Override
    public void onChildCreated() {
        tasksListFragment.setTaskHierarchy(db.getActualTasks(Calendar.getInstance()));
    }
}
