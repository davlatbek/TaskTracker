package com.projectse.aads.task_tracker.WizardFragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.projectse.aads.task_tracker.Adapters.DayPlanOverviewAdapter;
import com.projectse.aads.task_tracker.Adapters.TaskStackAdapter;
import com.projectse.aads.task_tracker.Models.TaskModel;
import com.projectse.aads.task_tracker.R;
import com.projectse.aads.task_tracker.WizardActivity;

import java.util.Calendar;
import java.util.List;

/**
 * Created by smith on 4/24/16.
 */
public class ManualAllocationFragment extends WizardFragment{
    private List<TaskModel> selected_tasks;
    public ManualAllocationFragment(List<TaskModel> selected_tasks) {
        super();
        this.selected_tasks = selected_tasks;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = super.onCreateView(inflater, container, savedInstanceState);
        getActivity().setTitle("Manual Allocation");
        view = inflater.inflate(R.layout.fragment_wizzard_manual, null);

        ListView listView = (ListView) view.findViewById(R.id.task_list);
        final TaskStackAdapter adapter = new TaskStackAdapter(getActivity(),R.id.task_list,selected_tasks);
        listView.setAdapter(adapter);

        for(int day = Calendar.SUNDAY; day <= Calendar.SATURDAY; day++){
            int counter_id = -1;
            int btn_id = -1;
            switch (day){
                case Calendar.MONDAY:
                    counter_id = R.id.countMon;
                    btn_id = R.id.btnMonday;
                    break;
                case Calendar.TUESDAY:
                    counter_id = R.id.countTue;
                    btn_id = R.id.btnTuesday;
                    break;
                case Calendar.WEDNESDAY:
                    counter_id = R.id.countWed;
                    btn_id = R.id.btnWednesday;
                    break;
                case Calendar.THURSDAY:
                    counter_id = R.id.countThu;
                    btn_id = R.id.btnThursday;
                    break;
                case Calendar.FRIDAY:
                    counter_id = R.id.countFri;
                    btn_id = R.id.btnFriday;
                    break;
                case Calendar.SATURDAY:
                    counter_id = R.id.countSat;
                    btn_id = R.id.btnSaturday;
                    break;
                case Calendar.SUNDAY:
                    counter_id = R.id.countSun;
                    btn_id = R.id.btnSunday;
                    break;
            }
            TextView counterView = (TextView) view.findViewById(counter_id);
            counterView.setText( String.valueOf(wizardActivity.loadByDay.get(day).getLeftScore()) );

            Button day_button = (Button) view.findViewById(btn_id);
            day_button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    TaskModel task = adapter.pop();
                    if(task != null){
                        //TODO do smth
                    }else{
                        Toast.makeText(getActivity(),"List is empty",Toast.LENGTH_SHORT);
                    }
                }
            });
        }

        Button prev = (Button) view.findViewById(R.id.btnPrev);
        prev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                wizardManager.callAllocateFragment();
            }
        });
        return view;
    }
}
