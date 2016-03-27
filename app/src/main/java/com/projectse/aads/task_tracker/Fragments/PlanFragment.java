package com.projectse.aads.task_tracker.Fragments;

import android.app.Fragment;
import android.app.FragmentManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.projectse.aads.task_tracker.R;

import java.util.Calendar;

/**
 * Created by Andrey Zolin on 20.03.2016.
 */
public class PlanFragment extends Fragment
        implements WeekSliderFragment.onWeekSliderEventListener, WeekDaysFragment.onSomeWeekDaysListener
{
    private static View view;
    private WeekDaysFragment weekDaysFragment;
    private TasksListFragment tasksListFragment;
    private WeekSliderFragment sliderFragment;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FragmentManager fm = getChildFragmentManager();

        Fragment slider = new WeekSliderFragment();
        ((WeekSliderFragment)slider).setSomeEventListener(this);
        fm.beginTransaction().replace(R.id.fragment_week_slider, slider).commit();

        Fragment list = new TasksListFragment();
        fm.beginTransaction().replace(R.id.fragment_tasks_list, list).commit();

        Fragment weekDays = new WeekDaysFragment();
        ((WeekDaysFragment)weekDays).setSomeEventListener(this);
        fm.beginTransaction().replace(R.id.fragment_week_days, weekDays).commit();

        weekDaysFragment = (WeekDaysFragment) weekDays;
        tasksListFragment = (TasksListFragment) list;
        sliderFragment = (WeekSliderFragment) slider;
        fm.executePendingTransactions();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        getActivity().setTitle("Plan");
        if (view != null) {
            ViewGroup parent = (ViewGroup) view.getParent();
            if (parent != null)
                parent.removeView(view);
        }
        view = inflater.inflate(R.layout.fragment_plan_view, null);

        return view;
    }

    /**
     *
     * @param weekDay
     */
    @Override
    public void setWeekDay(int weekDay) {
        if(weekDay >= Calendar.SUNDAY && weekDay <= Calendar.SATURDAY){
            tasksListFragment.setCurrentDayOfWeek(weekDay);
        }
    }

    @Override
    public void setWeekInPlanView(Calendar date_src) {
        Calendar date = (Calendar) date_src.clone();
        FragmentManager fm = getActivity().getFragmentManager();
        TasksListFragment fragList = (TasksListFragment) fm.findFragmentById(R.id.fragment_tasks_list);
        WeekDaysFragment fragWD = (WeekDaysFragment) fm.findFragmentById(R.id.fragment_week_days);
        WeekSliderFragment fragSlider = (WeekSliderFragment) fm.findFragmentById(R.id.fragment_week_slider);

        if(sliderFragment != null)
            sliderFragment.updateLabel();
        if(tasksListFragment != null) {
            tasksListFragment.setWeekData(date);
            if (weekDaysFragment != null)
                setWeekDay(weekDaysFragment.getCurrentDay());
        }
    }

    /**
     * Child fragments are creating async-ly.
     * To manage async views creation call this method in onViewCreated() of child fragment.
     */
    public void setDefault(){
        if(
                   weekDaysFragment.getView() != null
                && tasksListFragment.getView() != null
                && sliderFragment.getView() != null
                ) {

            Calendar week_first_day = Calendar.getInstance();
            week_first_day.setFirstDayOfWeek(Calendar.MONDAY);
            week_first_day.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
            setWeekInPlanView(week_first_day);
        }
    }
}
