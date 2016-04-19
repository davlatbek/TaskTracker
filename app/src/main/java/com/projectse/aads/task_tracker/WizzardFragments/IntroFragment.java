package com.projectse.aads.task_tracker.WizzardFragments;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.projectse.aads.task_tracker.DBService.DatabaseHelper;
import com.projectse.aads.task_tracker.Fragments.TasksListFragment;
import com.projectse.aads.task_tracker.Interfaces.WizzardManager;
import com.projectse.aads.task_tracker.R;

public class IntroFragment extends Fragment {
    DatabaseHelper db;
    private WizzardManager wizzardManager;

    private View.OnClickListener closeWizzardListener
            = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            wizzardManager.closeWizzard();
        }
    };
    private View.OnClickListener createListener
            = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            wizzardManager.callWeekFragment();
        }
    };

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        if (activity instanceof WizzardManager) {
            wizzardManager = (WizzardManager) activity;
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        db = DatabaseHelper.getsInstance(getActivity().getApplicationContext());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        getActivity().setTitle(R.string.wizzard_title);
        View view = inflater.inflate(R.layout.fragment_wizzard_intro, container, false);

        Button cancelBtn = (Button) view.findViewById(R.id.btnCancel);
        cancelBtn.setOnClickListener(closeWizzardListener);
        Button createBtn = (Button) view.findViewById(R.id.btnCreate);
        createBtn.setOnClickListener(createListener);
        return view;
    }

}
