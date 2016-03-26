package com.projectse.aads.task_tracker.Fragments;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.projectse.aads.task_tracker.R;

import java.util.Calendar;


public class WeekDaysFragment extends Fragment {
    public interface onSomeWeekDaysListener {
        public void setWeekDay(int weekDay);
    }

    int current_day;

    public WeekDaysFragment(){
        Calendar cal = Calendar.getInstance();
        current_day = cal.get(Calendar.DAY_OF_WEEK);
    }

    onSomeWeekDaysListener someEventListener;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            someEventListener = (onSomeWeekDaysListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString() + " must implement onSomeEventListener");
        }
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_week_days, null);

        Button buttonMonday = (Button) v.findViewById(R.id.btnMonday);
        Button buttonTuesday = (Button) v.findViewById(R.id.btnTuesday);
        Button buttonWednesday = (Button) v.findViewById(R.id.btnWednesday);
        Button buttonThursday = (Button) v.findViewById(R.id.btnThursday);
        Button buttonFriday = (Button) v.findViewById(R.id.btnFriday);
        Button buttonSaturday = (Button) v.findViewById(R.id.btnSaturday);
        Button buttonSunday = (Button) v.findViewById(R.id.btnSunday);

        buttonMonday.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                setCurrentDay(Calendar.MONDAY);
                someEventListener.setWeekDay(Calendar.MONDAY);
            }
        });
        buttonTuesday.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                setCurrentDay(Calendar.TUESDAY);
                someEventListener.setWeekDay(Calendar.TUESDAY);
            }
        });
        buttonWednesday.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                setCurrentDay(Calendar.WEDNESDAY);
                someEventListener.setWeekDay(Calendar.WEDNESDAY);
            }
        });
        buttonThursday.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                setCurrentDay(Calendar.THURSDAY);
                someEventListener.setWeekDay(Calendar.THURSDAY);
            }
        });
        buttonFriday.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                setCurrentDay(Calendar.FRIDAY);
                someEventListener.setWeekDay(Calendar.FRIDAY);
            }
        });
        buttonSaturday.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                setCurrentDay(Calendar.SATURDAY);
                someEventListener.setWeekDay(Calendar.SATURDAY);
            }
        });
        buttonSunday.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                setCurrentDay(Calendar.SUNDAY);
                someEventListener.setWeekDay(Calendar.SUNDAY);
            }
        });


        return v;
    }

    public int getCurrentDay() {
        return current_day;
    }

    public void setCurrentDay(int current_day) {
        if(current_day >= Calendar.SUNDAY && current_day <= Calendar.SATURDAY)
            this.current_day = current_day;
    }
}