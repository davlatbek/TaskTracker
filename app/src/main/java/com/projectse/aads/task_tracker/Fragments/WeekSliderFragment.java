package com.projectse.aads.task_tracker.Fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.view.InflateException;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.projectse.aads.task_tracker.Interfaces.ParentFragment;
import com.projectse.aads.task_tracker.R;

import java.text.SimpleDateFormat;
import java.util.Calendar;


public class WeekSliderFragment extends Fragment {
    public interface onWeekSliderEventListener {
        public void setWeek(Calendar date);
    }

    public Calendar getWeekFirstDay() {
        return (Calendar) week_first_day.clone();
    }

    private Calendar week_first_day = Calendar.getInstance();

    private static View view;
    private onWeekSliderEventListener someEventListener;
    private ImageButton buttonNextWeek;
    private ImageButton buttonPrevWeek;
    public WeekSliderFragment(){
        super();
        week_first_day.setFirstDayOfWeek(Calendar.MONDAY);
        week_first_day.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
    }

    public void setSomeEventListener(onWeekSliderEventListener someEventListener) {
        this.someEventListener = someEventListener;
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

        buttonNextWeek = (ImageButton) view.findViewById(R.id.btnNextWeek);
        buttonPrevWeek = (ImageButton) view.findViewById(R.id.btnPrevWeek);
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

        return view;
    }

    public void setWeek(Calendar week_first_day) {
        this.week_first_day = week_first_day;
        updateLabel();
    }

    public void setUnchangeble(){
        buttonNextWeek.setVisibility(View.INVISIBLE);
        buttonPrevWeek.setVisibility(View.INVISIBLE);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Fragment parent = getParentFragment();
        if(parent instanceof ParentFragment)
                ((ParentFragment) parent).onChildCreated();
    }

    public void updateLabel(){
        TextView label = (TextView) getView().findViewById(R.id.week_label);
        label.setText(weekToString());
    }

    private String weekToString(){
        SimpleDateFormat sdf = new SimpleDateFormat("dd");
        Calendar week_last_day = (Calendar) week_first_day.clone();
        week_last_day.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY);
        SimpleDateFormat sdl = new SimpleDateFormat("dd MMMM yyyy");
        if(week_last_day.get(Calendar.MONTH) != week_first_day.get(Calendar.MONTH)){
            sdf = sdl;
        }
        return sdf.format(week_first_day.getTime()) + " - " + sdl.format(week_last_day.getTime());
    }

    public static String dayToHumanReadable(Calendar date){
        SimpleDateFormat sdf = new SimpleDateFormat("dd MM yy");
        return sdf.format(date.getTime());
    }
}