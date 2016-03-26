package com.projectse.aads.task_tracker.Fragments;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.projectse.aads.task_tracker.R;

import java.text.SimpleDateFormat;
import java.util.Calendar;


public class WeekViewWeekSliderFragment extends Fragment {
    Calendar week_first_day = Calendar.getInstance();

    public WeekViewWeekSliderFragment(){
        week_first_day.setFirstDayOfWeek(Calendar.MONDAY);
        week_first_day.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
    }

    public interface onWeekViewWeekSliderEventListener {
        public void setWeek(Calendar date);
    }

    onWeekViewWeekSliderEventListener someEventListener;

    @Override
    public void onAttach(Activity context) {
        super.onAttach(context);
        Activity activity = context;
        try {
            someEventListener = (onWeekViewWeekSliderEventListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString() + " must implement onSomeEventListener");
        }
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_week_slider, null);
        ImageButton buttonNextWeek = (ImageButton) v.findViewById(R.id.btnNextWeek);
        ImageButton buttonPrevWeek = (ImageButton) v.findViewById(R.id.btnPrevWeek);
        buttonPrevWeek.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                week_first_day.add(Calendar.DAY_OF_MONTH, -7);
                someEventListener.setWeek(week_first_day);
            }
        });
        buttonNextWeek.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                week_first_day.add(Calendar.DAY_OF_MONTH, 7);
                someEventListener.setWeek(week_first_day);
            }
        });

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