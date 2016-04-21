package com.projectse.aads.task_tracker.WizzardFragments;

import android.app.Activity;
import android.app.Fragment;

import com.projectse.aads.task_tracker.Interfaces.WizzardManager;
import com.projectse.aads.task_tracker.WizzardActivity;

/**
 * Created by smith on 4/20/16.
 */
public abstract class WizzardFragment extends Fragment{

    protected WizzardActivity wizzardActivity;
    protected WizzardManager wizzardManager;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        if(activity instanceof WizzardActivity){
            wizzardActivity = (WizzardActivity) activity;
        }
        if(activity instanceof WizzardManager){
            wizzardManager = (WizzardManager) activity;
        }
    }

}
