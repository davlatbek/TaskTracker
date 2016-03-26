package com.projectse.aads.task_tracker.Fragments;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.InflateException;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.projectse.aads.task_tracker.R;

import java.util.Calendar;

/**
 * Created by Andrey Zolin on 20.03.2016.
 */
public class PlanFragment extends Fragment {
    private static View view;

    PlanViewWeekSliderFragment.onWeekSliderEventListener someEventListener;

    @Override
    public void onAttach(Activity context) {
        super.onAttach(context);
        Activity activity = context;
        try {
            someEventListener = (PlanViewWeekSliderFragment.onWeekSliderEventListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString() + " must implement onSomeEventListener");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        getActivity().setTitle("Plan");
//        return super.onCreateView(inflater, container, savedInstanceState);
        if (view != null) {
            ViewGroup parent = (ViewGroup) view.getParent();
            if (parent != null)
                parent.removeView(view);
        }
        view = inflater.inflate(R.layout.fragment_plan_view, null);

        Calendar week_first_day = Calendar.getInstance();
        week_first_day.setFirstDayOfWeek(Calendar.MONDAY);
        week_first_day.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
        if(someEventListener != null) someEventListener.setWeekInPlanView(week_first_day);

        return view;
    }

    @Override
    public void onDestroyView() {

        FragmentManager fm = getActivity().getSupportFragmentManager();

        Fragment xmlFragment1 = fm.findFragmentById(R.id.fragment_tasks_list);
        Fragment xmlFragment2 = fm.findFragmentById(R.id.fragment_week_days);
        Fragment xmlFragment3 = fm.findFragmentById(R.id.fragment_week_slider);
        if (xmlFragment1 != null) {
            fm.beginTransaction().remove(xmlFragment1).commit();
        }
        if (xmlFragment2 != null) {
            fm.beginTransaction().remove(xmlFragment2).commit();
        }
        if (xmlFragment3 != null) {
            fm.beginTransaction().remove(xmlFragment3).commit();
        }

        super.onDestroyView();
    }
}
