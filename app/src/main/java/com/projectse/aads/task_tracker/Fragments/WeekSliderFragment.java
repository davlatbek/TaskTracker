package com.projectse.aads.task_tracker.Fragments;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.projectse.aads.task_tracker.R;

import java.text.SimpleDateFormat;
import java.util.Calendar;


public class WeekSliderFragment extends Fragment {
    Calendar week_first_day = Calendar.getInstance();

    public WeekSliderFragment(){
        week_first_day.setFirstDayOfWeek(Calendar.MONDAY);
        week_first_day.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_week_slider, null);

        return v;
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