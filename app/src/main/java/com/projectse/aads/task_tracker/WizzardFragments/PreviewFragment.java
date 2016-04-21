package com.projectse.aads.task_tracker.WizzardFragments;

import android.app.Activity;
import android.app.FragmentManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.projectse.aads.task_tracker.Adapters.DayPlanOverviewAdapter;
import com.projectse.aads.task_tracker.DBService.DatabaseHelper;
import com.projectse.aads.task_tracker.Fragments.WeekSliderFragment;
import com.projectse.aads.task_tracker.Fragments.WeeklyViewFragment;
import com.projectse.aads.task_tracker.Interfaces.WizzardManager;
import com.projectse.aads.task_tracker.Models.TaskModel;
import com.projectse.aads.task_tracker.R;
import com.projectse.aads.task_tracker.WizzardActivity;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class PreviewFragment extends WeeklyViewFragment {
    private WizzardManager wizzardManager;
    private WizzardActivity wizzardActivity;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        if(activity instanceof WizzardManager){
            wizzardManager = (WizzardManager) activity;
        }
        if(activity instanceof WizzardActivity){
            wizzardActivity = (WizzardActivity) activity;
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = super.onCreateView(inflater, container, savedInstanceState);
        getActivity().setTitle("Preview");

        view = inflater.inflate(R.layout.fragment_wizzard_preview, null);

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

            Button prev = (Button) view.findViewById(R.id.btnPrev);
            prev.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    wizzardManager.callAllocateFragment();
                }
            });

            RelativeLayout day_button = (RelativeLayout) view.findViewById(btn_id);
            day_button.setOnClickListener(null);
        }
        return view;
    }

    @Override
    public void setWeek(Calendar week_first_date) {
        week_first_date = (Calendar) week_first_date.clone();
        sliderFragment.setWeek(week_first_date);
        for(int day = Calendar.SUNDAY; day <= Calendar.SATURDAY; day++){

            List<TaskModel> list =  week_task_list.get(day);
            list.clear();
            list.addAll((wizzardActivity.loadByDay.get(day)).getTasks());
            setTaskCounter(day,list.size());
        }
        for(DayPlanOverviewAdapter adapter : adapters)
            adapter.notifyDataSetChanged();
    }

    @Override
    public void onChildCreated() {
        super.onChildCreated();
        sliderFragment.setUnchangeble();
    }
}
