package com.projectse.aads.task_tracker.Fragments;

import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.projectse.aads.task_tracker.MainActivity;

/**
 * Created by Andrey Zolin on 20.03.2016.
 */
public class ProgressFragment extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        getActivity().setTitle("Progress");
        if(getActivity() instanceof MainActivity){
            ((MainActivity)getActivity()).callPieChartActivity();
        }
        return super.onCreateView(inflater, container, savedInstanceState);
    }
}
