package com.projectse.aads.task_tracker.WizardFragments;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.projectse.aads.task_tracker.DBService.DatabaseHelper;
import com.projectse.aads.task_tracker.Interfaces.WizardManager;
import com.projectse.aads.task_tracker.R;

public class IntroFragment extends WizardFragment {
    DatabaseHelper db;
    private WizardManager wizardManager;

    private View.OnClickListener closeWizardListener
            = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            wizardManager.closeWizard();
        }
    };
    private View.OnClickListener createListener
            = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            wizardManager.callWeekFragment();
        }
    };

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        if (activity instanceof WizardManager) {
            wizardManager = (WizardManager) activity;
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        db = DatabaseHelper.getsInstance(getActivity().getApplicationContext());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        getActivity().setTitle(R.string.wizard_title);
        View view = inflater.inflate(R.layout.fragment_wizzard_intro, container, false);

        Button cancelBtn = (Button) view.findViewById(R.id.btnCancel);
        cancelBtn.setOnClickListener(closeWizardListener);
        Button createBtn = (Button) view.findViewById(R.id.btnCreate);
        createBtn.setOnClickListener(createListener);

        setDrawablesToImageViews(view);
        return view;
    }

}
