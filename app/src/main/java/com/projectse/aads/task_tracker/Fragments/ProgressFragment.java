package com.projectse.aads.task_tracker.Fragments;

import android.app.FragmentManager;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.projectse.aads.task_tracker.MainActivity;
import com.projectse.aads.task_tracker.PieChartFragment;
import com.projectse.aads.task_tracker.R;

/**
 * Created by Andrey Zolin on 20.03.2016.
 */
public class ProgressFragment extends Fragment {
    private PieChartFragment pieChartFragment;
    private static View view;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FragmentManager fm = getChildFragmentManager();

        pieChartFragment = new PieChartFragment();
        fm.beginTransaction().replace(R.id.fragment_chart, pieChartFragment).commit();

        fm.executePendingTransactions();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        getActivity().setTitle("Progress");
        if (view != null) {
            ViewGroup parent = (ViewGroup) view.getParent();
            if (parent != null)
                parent.removeView(view);
        }
        view = inflater.inflate(R.layout.fragment_progress, null);

        return view;
    }
}
