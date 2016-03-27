package com.projectse.aads.task_tracker.Fragments;

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

import com.projectse.aads.task_tracker.Adapters.DayPlanOverviewAdapter;
import com.projectse.aads.task_tracker.Adapters.PlanAdapter;
import com.projectse.aads.task_tracker.DBService.DatabaseHelper;
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
public class WeeklyViewFragment extends Fragment implements WeekSliderFragment.onWeekSliderEventListener {
    DatabaseHelper db;

    List<DayPlanOverviewAdapter> adapters = new ArrayList<>();
    private static View view;
    private WeekSliderFragment sliderFragment;

    Map<Integer,List<TaskModel>> week_task_list = new HashMap<>();

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
            switch (day){
                case Calendar.MONDAY:
                    id = R.id.monday_list;
                    break;
                case Calendar.TUESDAY:
                    id = R.id.tuesday_list;
                    break;
                case Calendar.WEDNESDAY:
                    id = R.id.wednesday_list;
                    break;
                case Calendar.THURSDAY:
                    id = R.id.thursday_list;
                    break;
                case Calendar.FRIDAY:
                    id = R.id.friday_list;
                    break;
                case Calendar.SATURDAY:
                    id = R.id.saturday_list;
                    break;
                case Calendar.SUNDAY:
                    id = R.id.sunday_list;
                    break;
            }
            ListView listView = (ListView) view.findViewById(id);
            DayPlanOverviewAdapter adapter = new DayPlanOverviewAdapter(getActivity(), id, week_task_list.get(day));
            adapters.add(adapter);
            listView.setAdapter(adapter);
        }
//        ListView listView = (ListView) view.findViewById(R.id.monday_list);
//        new DayPlanOverviewAdapter(getActivity(), R.id.monday_list, week_task_list.get(Calendar.MONDAY));
//        listView.setAdapter(mon_adapter);

        return view;
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
            Log.d("WeeklyViewFraagment",WeekSliderFragment.dayToHumanReadable(date_cursor));
            List<TaskModel> tasks = db.getTasksForDay(date_cursor);
            for(TaskModel t : tasks){
                if(t.isSupertask())
                    (week_task_list.get(date_cursor.get(Calendar.DAY_OF_WEEK))).add(t);
            }
            date_cursor.add(Calendar.DAY_OF_MONTH, 1);
        }
        for(DayPlanOverviewAdapter ad : adapters){
            ad.notifyDataSetChanged();
        }
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
