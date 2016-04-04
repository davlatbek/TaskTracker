package com.projectse.aads.task_tracker.Fragments;

import android.app.FragmentManager;
import android.os.Bundle;
import android.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.projectse.aads.task_tracker.R;

/**
 * Created by Andrey Zolin on 20.03.2016.
 */
public class TasksFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        getActivity().setTitle("Tasks");
        View view = inflater.inflate(R.layout.fragment_tasks, container, false);

        // mapping correction
        LinearLayout buttons_layout = (LinearLayout) view.findViewById(R.id.tasks_categories_buttons);
        int width = getActivity().getResources().getDisplayMetrics().widthPixels;
        buttons_layout.setLayoutParams(new LinearLayout.LayoutParams(width, width));

        return view;
    }
}
