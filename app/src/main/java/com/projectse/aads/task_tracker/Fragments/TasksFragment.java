package com.projectse.aads.task_tracker.Fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.projectse.aads.task_tracker.R;

/**
 * Created by Andrey Zolin on 20.03.2016.
 */
public class TasksFragment extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        getActivity().setTitle("Tasks");
        View view = inflater.inflate(R.layout.fragment_tasks, container, false);
//        return super.onCreateView(inflater, container, savedInstanceState);
        return view;
    }
}
