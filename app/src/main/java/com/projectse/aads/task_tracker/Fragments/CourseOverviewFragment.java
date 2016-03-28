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
import com.projectse.aads.task_tracker.R;

/**
 * Created by Andrey Zolin on 27.03.2016.
 */
public class CourseOverviewFragment extends Fragment {
    DatabaseHelper db;
    private TasksListFragment tasksListFragment;
    private long CourseId;
    private AddTaskCaller addTaskCaller;


    private View.OnClickListener requestButtonListener
            = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            addTaskCaller.callAddTask(CourseId, null);
        }
    };

    public void setCourseID(long courseId) {
        this.CourseId = courseId;
        // refresh fragment
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        if (activity instanceof AddTaskCaller) {
            addTaskCaller = (AddTaskCaller) activity;
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        db = DatabaseHelper.getsInstance(getActivity().getApplicationContext());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        try {
            getActivity().setTitle(db.getCourse(CourseId).getName());
        } catch (Exception e) {
            e.printStackTrace();
        }
        View view = inflater.inflate(R.layout.fragment_course_overview, container, false);
        ImageButton addRequestButton = (ImageButton) view.findViewById(R.id.create_request_fab);


        addRequestButton.setOnClickListener(requestButtonListener);
        tasksListFragment = new TasksListFragment();
        FragmentManager fm = getChildFragmentManager();
        fm.beginTransaction().replace(R.id.task_list, tasksListFragment).commit();
        return view;
    }


    public void setDefault() {
        tasksListFragment.setTaskHierarchy(db.getListOfTasks(CourseId));
    }
}
