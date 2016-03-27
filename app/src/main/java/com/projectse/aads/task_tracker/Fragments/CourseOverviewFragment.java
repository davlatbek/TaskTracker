package com.projectse.aads.task_tracker.Fragments;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import com.projectse.aads.task_tracker.DBService.DatabaseHelper;
import com.projectse.aads.task_tracker.R;
import com.projectse.aads.task_tracker.TaskAddActivity;

/**
 * Created by Andrey Zolin on 27.03.2016.
 */
public class CourseOverviewFragment extends Fragment {
    DatabaseHelper db = new DatabaseHelper(getActivity());
    private TasksListFragment tasksListFragment;
    private long CourseId;

    public void setCourseID(long courseId) {
        this.CourseId = courseId;
        // refresh fragment
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        db = DatabaseHelper.getsInstance(getActivity().getApplicationContext());
    }

    private View.OnClickListener requestButtonListener
            = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            //fragment a
            TaskAddActivity addTaskFragment = new TaskAddActivity();
            FragmentTransaction ft = getFragmentManager().beginTransaction();
            //ft.replace(R.id.frame_container, addTaskFragment);
            ft.addToBackStack(null);
            ft.commit();
        }
    };
//    private View.OnClickListener requestItemListener = new View.OnClickListener() {
//        @Override
//        public void onClick(View v) {
//            Toast.makeText(getActivity(), ((TextView) v.findViewById(R.id.request_name)).getText() + " item selected", Toast.LENGTH_SHORT).show();
//        }
//    };

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        getActivity().setTitle("Project SE");
        View view = inflater.inflate(R.layout.fragment_course_overview, container, false);
        ImageButton addRequestButton = (ImageButton) view.findViewById(R.id.create_request_fab);
        addRequestButton.setOnClickListener(requestButtonListener);
        tasksListFragment = new TasksListFragment();
        FragmentManager fm = getChildFragmentManager();
        fm.beginTransaction().replace(R.id.task_list, tasksListFragment).commit();


//        for(int i = 0; i < 3; i++) {
//            View requestListItemView = inflater.inflate(R.layout.course_overview_task_list, null);
//            TableLayout requestItemsTable = (TableLayout) view.findViewById(R.id.request_items_table);
//            TextView tv = (TextView)requestListItemView.findViewById(R.id.request_name);
//            tv.setText("Task " + i);
//            requestListItemView.setOnClickListener(requestItemListener);
//            requestItemsTable.addView(requestListItemView);
//        }
        return view;
    }

    public  void setDefault(){
        tasksListFragment.setTaskHierarchy(db.getTaskModelList());
    }
}
