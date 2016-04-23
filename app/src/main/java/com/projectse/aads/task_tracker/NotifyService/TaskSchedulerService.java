package com.projectse.aads.task_tracker.NotifyService;

import android.app.job.JobParameters;
import android.app.job.JobService;

/**
 * Created by Andrey Zolin on 21.04.2016.
 */
public class TaskSchedulerService extends JobService {
    @Override
    public boolean onStartJob(JobParameters params) {
        return false;
    }

    @Override
    public boolean onStopJob(JobParameters params) {
        return false;
    }
}
