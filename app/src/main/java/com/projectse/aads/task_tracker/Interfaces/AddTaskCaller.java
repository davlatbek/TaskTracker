package com.projectse.aads.task_tracker.Interfaces;

import java.util.Calendar;

/**
 * Created by Andrey Zolin on 28.03.2016.
 */
public interface AddTaskCaller {
    public void callAddTask(long defaultCourseId, Calendar defaultStartTime);
    public void callAddTask();
}
