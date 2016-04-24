package com.projectse.aads.task_tracker.WizardFragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.projectse.aads.task_tracker.Adapters.TaskStackAdapter;
import com.projectse.aads.task_tracker.Models.TaskModel;
import com.projectse.aads.task_tracker.R;
import com.projectse.aads.task_tracker.WizardActivity;

import org.apache.commons.lang3.time.DateUtils;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

import static com.projectse.aads.task_tracker.Models.TaskModel.roundByDay;

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

        TextView tooltip = (TextView) view.findViewById(R.id.txtTooltip);
        tooltip.setText(getString(R.string.manual_alloc_tooltip));

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
            final int finalCounter_id = counter_id;
            final int finalDay = day;
            day_button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(adapter.isEmpty()){
                        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                        builder.setMessage(getString(R.string.no_more_tasks))
                                .setTitle(getString(R.string.list_is_empty));
                        builder.setPositiveButton(R.string.ok, null);
                        AlertDialog dialog = builder.create();
                        dialog.show();
                        return;
                    }

                    TaskModel task = adapter.getItem(0);

                    WizardActivity.Load load = wizardActivity.loadByDay.get(finalDay);
                    if( load.getLeftScore() <= 0 || (load.getLeftScore() + 1) < task.getDuration()){
                        StringBuilder stringBuilder = new StringBuilder();
                        stringBuilder.append("Left free hours: ");
                        stringBuilder.append(load.getLeftScore());
                        stringBuilder.append("\nTask's duration: ");
                        stringBuilder.append(task.getDuration());
                        stringBuilder.append("\n");

                        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                        builder.setMessage(stringBuilder)
                                .setTitle(getString(R.string.free_hourse_constrained_failed));
                        builder.setPositiveButton(R.string.ok, null);
                        AlertDialog dialog = builder.create();
                        dialog.show();
                        return;
                    }

                    Calendar date = wizardActivity.getFirstDayOfWeek();
                    date.set(Calendar.DAY_OF_WEEK,finalDay);

                    Calendar deadline = roundByDay(task.getDeadline());
                    date = roundByDay(date);
                    date.setTimeZone(deadline.getTimeZone());

                    if( date.compareTo(deadline) > 0){
                        SimpleDateFormat dateFormat = new SimpleDateFormat("dd MM yyyy hh:mm:ss");
                        StringBuilder stringBuilder = new StringBuilder();
                        stringBuilder.append("Try to set: ");
                        stringBuilder.append(dateFormat.format(date.getTime()));
                        stringBuilder.append("\nTask's deadline: ");
                        stringBuilder.append(dateFormat.format(deadline.getTime()));
                        stringBuilder.append("\n");
                        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                        builder.setMessage(stringBuilder)
                                .setTitle(getString(R.string.deadline_constrained_failed));
                        builder.setPositiveButton(R.string.ok, null);
                        AlertDialog dialog = builder.create();
                        dialog.show();
                        return;
                    }
                    task = adapter.pop();
                    if(task != null){
                        addTask(load,task, finalCounter_id);
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

        Button next = (Button) view.findViewById(R.id.btnNext);
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                wizardActivity.commitChanges();
            }
        });
        return view;
    }

    private void addTask(WizardActivity.Load load, TaskModel task, int counter_id){
        load.addTask(task);
        if(getView() != null){
            try {
                TextView counterView = (TextView) getView().findViewById(counter_id);
                counterView.setText(String.valueOf(load.getLeftScore()));
            }catch (Exception e){
                Log.d("CaughtError",e.getMessage());
            }
        }
    }
}
