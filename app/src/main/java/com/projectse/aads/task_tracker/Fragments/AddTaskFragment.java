package com.projectse.aads.task_tracker.Fragments;

import android.app.Fragment;
import android.app.FragmentManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.projectse.aads.task_tracker.R;

/**
 * Created by Davlatbek Isroilov on 3/27/2016.
 * Innopolis University
 */
public class AddTaskFragment extends Fragment {
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        getActivity().setTitle("Add Task");
        View view = inflater.inflate(R.layout.shared_content_task_new, container, false);
        return view;
    }
}
