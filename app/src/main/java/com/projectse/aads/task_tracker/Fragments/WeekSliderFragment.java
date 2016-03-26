package com.projectse.aads.task_tracker.Fragments;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.view.InflateException;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.projectse.aads.task_tracker.R;

import java.text.SimpleDateFormat;
import java.util.Calendar;


public class WeekSliderFragment extends XMLFragment {
    Calendar week_first_day = Calendar.getInstance();
    protected static View view;

    public WeekSliderFragment(){
        week_first_day.setFirstDayOfWeek(Calendar.MONDAY);
        week_first_day.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (view != null) {
            ViewGroup parent = (ViewGroup) view.getParent();
            if (parent != null)
                parent.removeView(view);
        }
        try {
            view = inflater.inflate(R.layout.fragment_week_slider, null);
        } catch (InflateException e) {
        /* map is already there, just return view as it is */
        }


        return view;
    }

    private String weekToString(){
        SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
        Calendar week_last_day = (Calendar) week_first_day.clone();
        week_last_day.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY);
        return sdf.format(week_first_day.getTime()) + " - " + sdf.format(week_last_day.getTime());
    }

    public void updateLabel(){
        TextView label = (TextView) getView().findViewById(R.id.week_label);
        label.setText(weekToString());
    }
}