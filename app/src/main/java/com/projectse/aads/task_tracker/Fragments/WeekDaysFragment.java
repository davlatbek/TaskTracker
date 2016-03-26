package com.projectse.aads.task_tracker.Fragments;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.projectse.aads.task_tracker.R;

import java.util.ArrayList;
import java.util.Calendar;


public class WeekDaysFragment extends XMLFragment {
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

        final Button buttonMonday = (Button) v.findViewById(R.id.btnMonday);
        final Button buttonTuesday = (Button) v.findViewById(R.id.btnTuesday);
        final Button buttonWednesday = (Button) v.findViewById(R.id.btnWednesday);
        final Button buttonThursday = (Button) v.findViewById(R.id.btnThursday);
        final Button buttonFriday = (Button) v.findViewById(R.id.btnFriday);
        final Button buttonSaturday = (Button) v.findViewById(R.id.btnSaturday);
        final Button buttonSunday = (Button) v.findViewById(R.id.btnSunday);

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

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setCurrentDay(current_day);
    }

    public int getCurrentDay() {
        return current_day;
    }

    public void setCurrentDay(int current_day) {
        if(current_day >= Calendar.SUNDAY && current_day <= Calendar.SATURDAY)
            this.current_day = current_day;
        ArrayList<Button> buttons = new ArrayList<>();
        buttons.add((Button) getView().findViewById(R.id.btnMonday));
        buttons.add((Button) getView().findViewById(R.id.btnTuesday));
        buttons.add((Button) getView().findViewById(R.id.btnWednesday));
        buttons.add((Button) getView().findViewById(R.id.btnThursday));
        buttons.add((Button) getView().findViewById(R.id.btnFriday));
        buttons.add((Button) getView().findViewById(R.id.btnSaturday));
        buttons.add((Button) getView().findViewById(R.id.btnSunday));
        for(Button btn : buttons){
            btn.setBackgroundColor(getResources().getColor(R.color.light_green_300));
            btn.setTextColor(Color.BLACK);
        }
        switch (current_day){
            case Calendar.MONDAY:
                toggleButton(buttons.get(0));
                break;
            case Calendar.TUESDAY:
                toggleButton(buttons.get(1));
                break;
            case Calendar.WEDNESDAY:
                toggleButton(buttons.get(2));
                break;
            case Calendar.THURSDAY:
                toggleButton(buttons.get(3));
                break;
            case Calendar.FRIDAY:
                toggleButton(buttons.get(4));
                break;
            case Calendar.SATURDAY:
                toggleButton(buttons.get(5));
                break;
            case Calendar.SUNDAY:
                toggleButton(buttons.get(6));
                break;
        }
    }

    public void unsetToggle(){
        ArrayList<Button> buttons = new ArrayList<>();
        buttons.add((Button) getView().findViewById(R.id.btnMonday));
        buttons.add((Button) getView().findViewById(R.id.btnTuesday));
        buttons.add((Button) getView().findViewById(R.id.btnWednesday));
        buttons.add((Button) getView().findViewById(R.id.btnThursday));
        buttons.add((Button) getView().findViewById(R.id.btnFriday));
        buttons.add((Button) getView().findViewById(R.id.btnSaturday));
        buttons.add((Button) getView().findViewById(R.id.btnSunday));
        for(Button btn : buttons){
            btn.setBackgroundColor(getResources().getColor(R.color.light_green_300));
            btn.setTextColor(Color.BLACK);
        }
    }

    public void toggleButton(Button btn){
        btn.setBackgroundColor(getResources().getColor(R.color.light_green_800));
        btn.setTextColor(Color.WHITE);
    }
}