package com.projectse.aads.task_tracker.Fragments;

import android.os.Bundle;
import android.app.Fragment;
import android.app.FragmentManager;
import android.view.InflateException;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.projectse.aads.task_tracker.R;

/**
 * Created by Andrey Zolin on 20.03.2016.
 */
public class WeeklyViewFragment extends Fragment {
    private static View view;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        getActivity().setTitle("Weekly View");
        if (view != null) {
            ViewGroup parent = (ViewGroup) view.getParent();
            if (parent != null)
                parent.removeView(view);
        }
        try {
            view = inflater.inflate(R.layout.fragment_week_view, null);
            FragmentManager fm = getFragmentManager();
            Fragment slider = new WeekSliderFragment();
            fm.beginTransaction().replace(R.id.fragment_week_slider, slider).commit();
        } catch (InflateException e) {
        /* map is already there, just return view as it is */
        }
        return view;
    }

    @Override
    public void onDestroyView() {

        FragmentManager fm = getFragmentManager();

        Fragment xmlFragment3 = fm.findFragmentById(R.id.fragment_week_slider);

        if (xmlFragment3 != null) {
            fm.beginTransaction().remove(xmlFragment3).commit();
        }

        super.onDestroyView();
    }
}
