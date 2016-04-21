package com.projectse.aads.task_tracker.WizzardFragments;

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
import com.projectse.aads.task_tracker.Interfaces.WizzardManager;
import com.projectse.aads.task_tracker.Models.CheckableTaskModel;
import com.projectse.aads.task_tracker.Models.TaskModel;
import com.projectse.aads.task_tracker.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by smith on 4/19/16.
 */
public class AllocateFragment extends WizzardFragment {

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

        //TODO implement method get tasks for plan
        List<TaskModel> tasks = db.getTaskModelList();
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
        View view = inflater.inflate(R.layout.fragment_wizzard_allocate, null);

        View allocate_start = view.findViewById(R.id.allocate_to_start);
        View allocate_evenly = view.findViewById(R.id.allocate_evenly);
        View allocate_to_end = view.findViewById(R.id.allocate_to_end);
        View allocate_manually = view.findViewById(R.id.allocate_manually);

        allocate_start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                wizzardActivity.allocateToStart();
            }
        });

        allocate_evenly.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                wizzardActivity.allocateEvenly();
            }
        });

        allocate_to_end.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                wizzardActivity.allocateToEnd();
            }
        });

        allocate_manually.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                wizzardActivity.allocateManually();
            }
        });

        TextView tooltip = (TextView) view.findViewById(R.id.txtTooltip);
        tooltip.setText(getString(R.string.allocate_tooltip));

        Button prev = (Button) view.findViewById(R.id.btnPrev);
        prev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                wizzardManager.callTasksFragment();
            }
        });
        Button next = (Button) view.findViewById(R.id.btnNext);
        next.setVisibility(View.INVISIBLE);

        return view;
    }
}
