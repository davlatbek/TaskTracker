package com.projectse.aads.task_tracker.NotifyService;

import android.app.job.JobParameters;
import android.app.job.JobService;
import android.util.Log;

import com.projectse.aads.task_tracker.MainActivity;

import java.util.LinkedList;

/**
 * Created by Andrey Zolin on 21.04.2016.
 */
public class TaskSchedulerService extends JobService {
    private static final String TAG = "SyncService";
    @Override
    public boolean onStartJob(JobParameters params) {
        Log.i(TAG, "on start job: " + params.getJobId());
        return true;
    }

    @Override
    public boolean onStopJob(JobParameters params) {
        Log.i(TAG, "on stop job: " + params.getJobId());
        return true;
    }

    MainActivity mActivity;
    private final LinkedList<JobParameters> jobParamsMap = new LinkedList<JobParameters>();
    public void setUiCallback(MainActivity activity) {
        mActivity = activity;
    }
    
}
