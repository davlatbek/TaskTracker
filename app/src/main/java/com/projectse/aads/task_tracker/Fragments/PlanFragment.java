package com.projectse.aads.task_tracker.Fragments;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import com.projectse.aads.task_tracker.R;

import java.util.Calendar;
import java.util.List;

/**
 * Created by Andrey Zolin on 20.03.2016.
 */
public class PlanFragment extends Fragment {

    WeekSliderFragment.onWeekSliderEventListener someEventListener;

    @Override
    public void onAttach(Activity context) {
        super.onAttach(context);
        Activity activity = context;
        try {
            someEventListener = (WeekSliderFragment.onWeekSliderEventListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString() + " must implement onSomeEventListener");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        getActivity().setTitle("Plan");
//        return super.onCreateView(inflater, container, savedInstanceState);
        View v = inflater.inflate(R.layout.fragment_plan_view, null);

        Calendar week_first_day = Calendar.getInstance();
        week_first_day.setFirstDayOfWeek(Calendar.MONDAY);
        week_first_day.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
        if(someEventListener != null) someEventListener.setWeek(week_first_day);

        return v;
    }
}
