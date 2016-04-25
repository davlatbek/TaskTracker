package com.projectse.aads.task_tracker.WizardFragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.TextView;

import com.projectse.aads.task_tracker.Adapters.TaskListCheckableAdapter;
import com.projectse.aads.task_tracker.DBService.DatabaseHelper;
import com.projectse.aads.task_tracker.Models.CheckableTaskModel;
import com.projectse.aads.task_tracker.Models.TaskModel;
import com.projectse.aads.task_tracker.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by smith on 4/19/16.
 */
public class TasksFragment extends WizardFragment {

    private List<CheckableTaskModel> tasks_list = new ArrayList<>();
    private DatabaseHelper db;
    private CheckBox selectAll;
    private TaskListCheckableAdapter adapter;

    private View.OnClickListener selectAllListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            for (CheckableTaskModel t : tasks_list){
                t.setChecked( selectAll.isChecked() );
            }
            adapter.notifyDataSetChanged();
        }
    };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        db = DatabaseHelper.getsInstance(getActivity().getApplicationContext());

        List<TaskModel> tasks = wizardActivity.getTasksToWeek();
        for(TaskModel t : tasks){
            if(t.isSupertask()) {
                CheckableTaskModel tt = new CheckableTaskModel( t );
                tasks_list.add( tt );
            }
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_wizzard_tasks, null);
        ListView listView = (ListView) view.findViewById(R.id.task_list);
        adapter = new TaskListCheckableAdapter(getActivity(), R.id.task_list, tasks_list);
        listView.setAdapter(adapter);
        selectAll = (CheckBox) view.findViewById(R.id.btnSelectAll);
        selectAll.setOnClickListener(selectAllListener);

        TextView tooltip = (TextView) view.findViewById(R.id.txtTooltip);
        tooltip.setText(getString(R.string.tasks_tooltip));

        Button prev = (Button) view.findViewById(R.id.btnPrev);
        prev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                wizardManager.callWeekFragment();
            }
        });
        Button next = (Button) view.findViewById(R.id.btnNext);
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                wizardActivity.selected_tasks.clear();
                for(CheckableTaskModel t : tasks_list){
                    if(t.getChecked() == true) {
                        wizardActivity.selected_tasks.add(t.getTask());
                    }
                    wizardActivity.calculateDefaultDuration();
                }
                wizardManager.callAllocateFragment();
            }
        });

        setDrawablesToImageViews(view);
        return view;
    }
}
