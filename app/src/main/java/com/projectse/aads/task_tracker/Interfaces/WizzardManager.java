package com.projectse.aads.task_tracker.Interfaces;

import com.projectse.aads.task_tracker.WizzardFragments.IntroFragment;

/**
 * Created by smith on 4/19/16.
 */
public interface WizzardManager {
    public void closeWizzard();
    public void callIntroFragment();
    public void callWeekFragment();
    public void callTasksFragment();
    public void callAllocateFragment();
    public void callManualAllocateFragment();
    public void callPreviewFragment();
}
