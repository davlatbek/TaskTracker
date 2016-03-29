package com.projectse.aads.task_tracker.Fragments;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.app.Fragment;
import android.app.FragmentManager;
import android.util.Log;
import android.view.InflateException;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.projectse.aads.task_tracker.Adapters.DayPlanOverviewAdapter;
import com.projectse.aads.task_tracker.Adapters.PlanAdapter;
import com.projectse.aads.task_tracker.DBService.DatabaseHelper;
import com.projectse.aads.task_tracker.Interfaces.ParentFragment;
import com.projectse.aads.task_tracker.Models.TaskModel;
import com.projectse.aads.task_tracker.R;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Andrey Zolin on 20.03.2016.
 */
public class WeeklyViewFragment extends Fragment implements WeekSliderFragment.onWeekSliderEventListener, ParentFragment {
    public interface onWeekViewEventListener{
        public void callPlanFragment(Calendar first_day, int day_of_week);
    }

    private DatabaseHelper db;

    List<DayPlanOverviewAdapter> adapters = new ArrayList<>();
    private static View view;
    private WeekSliderFragment sliderFragment;

    Map<Integer,List<TaskModel>> week_task_list = new HashMap<>();

    private onWeekViewEventListener listener;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        db = DatabaseHelper.getsInstance(getActivity().getApplicationContext());

        FragmentManager fm = getChildFragmentManager();
        sliderFragment = new WeekSliderFragment();
        sliderFragment.setSomeEventListener(this);
        fm.beginTransaction().replace(R.id.fragment_week_slider, sliderFragment).commit();
        fm.executePendingTransactions();

        week_task_list.clear();
        for(int i = Calendar.SUNDAY; i <= Calendar.SATURDAY; i++){
            week_task_list.put(i,new ArrayList<TaskModel>());
        }
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

        for(int day = Calendar.SUNDAY; day <= Calendar.SATURDAY; day++){
            int id = -1;
            int btn_id = -1;
            switch (day){
                case Calendar.MONDAY:
                    id = R.id.monday_list;
                    btn_id = R.id.btnMonday;
                    break;
                case Calendar.TUESDAY:
                    id = R.id.tuesday_list;
                    btn_id = R.id.btnTuesday;
                    break;
                case Calendar.WEDNESDAY:
                    id = R.id.wednesday_list;
                    btn_id = R.id.btnWednesday;
                    break;
                case Calendar.THURSDAY:
                    id = R.id.thursday_list;
                    btn_id = R.id.btnThursday;
                    break;
                case Calendar.FRIDAY:
                    id = R.id.friday_list;
                    btn_id = R.id.btnFriday;
                    break;
                case Calendar.SATURDAY:
                    id = R.id.saturday_list;
                    btn_id = R.id.btnSaturday;
                    break;
                case Calendar.SUNDAY:
                    id = R.id.sunday_list;
                    btn_id = R.id.btnSunday;
                    break;
            }
            ListView listView = (ListView) view.findViewById(id);
            DayPlanOverviewAdapter adapter = new DayPlanOverviewAdapter(getActivity(), id, week_task_list.get(day));
            adapters.add(adapter);
            listView.setAdapter(adapter);

            RelativeLayout day_button = (RelativeLayout) view.findViewById(btn_id);
            final int finalDay = day;

            day_button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Calendar week_first_day = sliderFragment.getWeekFirstDay();
                    if (listener != null) {
                        listener.callPlanFragment(week_first_day, finalDay);
                    }
                }
            });
        }


        return view;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        if(activity instanceof onWeekViewEventListener){
            listener = (onWeekViewEventListener) activity;
        }
    }

    @Override
    public void setWeek(Calendar week_first_date) {
        if(sliderFragment != null)
            sliderFragment.updateLabel();
        // clear all lists
        for(int i = Calendar.SUNDAY; i <= Calendar.SATURDAY; i++){
            (week_task_list.get(i)).clear();
        }
        Calendar date_cursor = (Calendar) week_first_date.clone();

        for(int i = 0; i < 7; i++){
            Log.d("WeeklyViewFragment",WeekSliderFragment.dayToHumanReadable(date_cursor));
            List<TaskModel> tasks = db.getTasksForDay(date_cursor);
            int tasks_counter = 0;
            for(TaskModel t : tasks){
                if(t.isSupertask()) {
                    (week_task_list.get(date_cursor.get(Calendar.DAY_OF_WEEK))).add(t);
                    tasks_counter++;
                }
            }
            setTaskCounter(date_cursor, tasks_counter);
            date_cursor.add(Calendar.DAY_OF_MONTH, 1);
        }
        for(DayPlanOverviewAdapter ad : adapters){
            ad.notifyDataSetChanged();
        }
    }

    private void setTaskCounter(Calendar date_cursor, int tasks_counter) {
        int day_of_week = date_cursor.get(Calendar.DAY_OF_WEEK);
        View view = null;
        switch (day_of_week){
            case Calendar.MONDAY:
                view = getView().findViewById(R.id.txtMonTasksCount);break;
            case Calendar.TUESDAY:
                view = getView().findViewById(R.id.txtTueTasksCount);break;
            case Calendar.WEDNESDAY:
                view = getView().findViewById(R.id.txtWedTasksCount);break;
            case Calendar.THURSDAY:
                view = getView().findViewById(R.id.txtThuTasksCount);break;
            case Calendar.FRIDAY:
                view = getView().findViewById(R.id.txtFriTasksCount);break;
            case Calendar.SATURDAY:
                view = getView().findViewById(R.id.txtSatTasksCount);break;
            case Calendar.SUNDAY:
                view = getView().findViewById(R.id.txtSunTasksCount);break;
        }
        if(view == null){
            return;
        }
        TextView textView = (TextView) view;
        textView.setText(String.valueOf(tasks_counter));
    }

    /**
     * Child fragments are creating async-ly.
     * To manage async views creation call this method in onViewCreated() of child fragment.
     */
    @Override
    public void onChildCreated(){
        if(sliderFragment.getView() != null) {
            Calendar week_first_day = Calendar.getInstance();
            week_first_day.setFirstDayOfWeek(Calendar.MONDAY);
            week_first_day.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
            setWeek(week_first_day);
        }
    }
}
