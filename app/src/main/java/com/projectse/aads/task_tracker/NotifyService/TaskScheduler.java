package com.projectse.aads.task_tracker.NotifyService;

import android.app.job.JobInfo;
import android.app.job.JobScheduler;

import java.util.List;

/**
 * Created by Andrey Zolin on 17.04.2016.
 */
public class TaskScheduler extends JobScheduler {
    @Override
    public int schedule(JobInfo job) {
        return 0;
    }

    @Override
    public void cancel(int jobId) {

    }

    @Override
    public void cancelAll() {

    }

    @Override
    public List<JobInfo> getAllPendingJobs() {
        return null;
    }
}
