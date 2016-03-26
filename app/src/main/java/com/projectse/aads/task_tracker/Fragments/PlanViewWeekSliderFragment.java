package com.projectse.aads.task_tracker.Fragments;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.projectse.aads.task_tracker.R;

import java.text.SimpleDateFormat;
import java.util.Calendar;


public class PlanViewWeekSliderFragment extends WeekSliderFragment {
    public PlanViewWeekSliderFragment(){
        super();
    }

    public interface onWeekSliderEventListener {
        public void setWeekInPlanView(Calendar date);
    }

    onWeekSliderEventListener someEventListener;

    @Override
    public void onAttach(Activity context) {
        super.onAttach(context);
        Activity activity = context;
        try {
            someEventListener = (onWeekSliderEventListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString() + " must implement onSomeEventListener");
        }
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = super.onCreateView(inflater,container,savedInstanceState);
        ImageButton buttonNextWeek = (ImageButton) v.findViewById(R.id.btnNextWeek);
        ImageButton buttonPrevWeek = (ImageButton) v.findViewById(R.id.btnPrevWeek);
        buttonPrevWeek.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                week_first_day.add(Calendar.DAY_OF_MONTH, -7);
                someEventListener.setWeekInPlanView(week_first_day);
            }
        });
        buttonNextWeek.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                week_first_day.add(Calendar.DAY_OF_MONTH, 7);
                someEventListener.setWeekInPlanView(week_first_day);
            }
        });

        return v;
    }
}