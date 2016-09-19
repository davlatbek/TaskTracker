package com.projectse.aads.task_tracker.WizardFragments;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.projectse.aads.task_tracker.Adapters.DayPlanOverviewAdapter;
import com.projectse.aads.task_tracker.Fragments.WeeklyViewFragment;
import com.projectse.aads.task_tracker.Interfaces.WizardManager;
import com.projectse.aads.task_tracker.Models.TaskModel;
import com.projectse.aads.task_tracker.R;
import com.projectse.aads.task_tracker.WizardActivity;

import java.util.Calendar;
import java.util.List;

public class PreviewFragment extends WeeklyViewFragment {
    private WizardManager wizardManager;
    private WizardActivity wizardActivity;
    private View.OnClickListener commitChangesListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            wizardActivity.commitChanges();
        }
    };

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        if(activity instanceof WizardManager){
            wizardManager = (WizardManager) activity;
        }
        if(activity instanceof WizardActivity){
            wizardActivity = (WizardActivity) activity;
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


            RelativeLayout day_button = (RelativeLayout) view.findViewById(btn_id);
            day_button.setOnClickListener(null);
        }

//        view.findViewById(R.id.add_task_btn).setVisibility(View.INVISIBLE);
        ((TextView) view.findViewById(R.id.add_task_lbl)).setText(R.string.confirm);
        Drawable drawable = getActivity().getDrawable(R.drawable.ic_check_circle_24dp);
        drawable.mutate().setColorFilter( 0xfffdfdfe, PorterDuff.Mode.XOR);
        ((ImageView)view.findViewById(R.id.add_task_icon)).setImageDrawable(drawable);
        view.findViewById(R.id.add_task_btn).setOnClickListener(commitChangesListener);

        Button prev = (Button) view.findViewById(R.id.btnPrev);
        prev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                wizardManager.callAllocateFragment();
            }
        });

        Button next = (Button) view.findViewById(R.id.btnNext);
        next.setVisibility(View.INVISIBLE);
//        next.setOnClickListener(commitChangesListener);
        return view;
    }

    @Override
    public void setWeek(Calendar week_first_date) {
        week_first_date = (Calendar) week_first_date.clone();
        sliderFragment.setWeek(week_first_date);
        for(int day = Calendar.SUNDAY; day <= Calendar.SATURDAY; day++){

            List<TaskModel> list =  week_task_list.get(day);
            list.clear();
            list.addAll((wizardActivity.loadByDay.get(day)).getTasks());
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
