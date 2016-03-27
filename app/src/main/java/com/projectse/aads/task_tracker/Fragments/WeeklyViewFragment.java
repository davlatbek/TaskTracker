package com.projectse.aads.task_tracker.Fragments;

import android.os.Bundle;
import android.app.Fragment;
import android.app.FragmentManager;
import android.view.InflateException;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.projectse.aads.task_tracker.R;

import java.util.Calendar;

/**
 * Created by Andrey Zolin on 20.03.2016.
 */
public class WeeklyViewFragment extends Fragment implements WeekSliderFragment.onWeekSliderEventListener {
    private static View view;
    private WeekSliderFragment sliderFragment;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FragmentManager fm = getChildFragmentManager();
        sliderFragment = new WeekSliderFragment();
        sliderFragment.setSomeEventListener(this);
        fm.beginTransaction().replace(R.id.fragment_week_slider, sliderFragment).commit();
        fm.executePendingTransactions();
    }

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

        } catch (InflateException e) {
        /* map is already there, just return view as it is */
        }
        return view;
    }

    @Override
    public void setWeek(Calendar date) {
        if(sliderFragment != null)
            sliderFragment.updateLabel();
    }

    /**
     * Child fragments are creating async-ly.
     * To manage async views creation call this method in onViewCreated() of child fragment.
     */
    public void setDefault(){
        if(sliderFragment.getView() != null) {
            Calendar week_first_day = Calendar.getInstance();
            week_first_day.setFirstDayOfWeek(Calendar.MONDAY);
            week_first_day.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
            setWeek(week_first_day);
        }
    }
}
