package com.projectse.aads.task_tracker.WizardFragments;

import android.app.Activity;
import android.app.Fragment;

import com.projectse.aads.task_tracker.Interfaces.WizardManager;
import com.projectse.aads.task_tracker.WizardActivity;

/**
 * Created by smith on 4/20/16.
 */
public abstract class WizardFragment extends Fragment{

    protected WizardActivity wizardActivity;
    protected WizardManager wizardManager;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        if(activity instanceof WizardActivity){
            wizardActivity = (WizardActivity) activity;
        }
        if(activity instanceof WizardManager){
            wizardManager = (WizardManager) activity;
        }
    }

}
