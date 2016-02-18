package com.projectse.aads.task_tracker.Models;


/**
 * Created by Anastasia A. Puzankova on 07-Feb-16.
 */

/*
 * Contain data of Setting entity
 */
public class SettingsModel {
    // allow notify
    private Boolean AlwaysNotifyStartTime;
    private Boolean AlwaysNotifyDeadLine;
    // the time (in minutes) between notification and StartTime/DeadLine
    private Integer NotifyStartTimeBefore;
    private Integer NotifyDeadLineBefore;
    // number of notifications
    private Integer NotifyStartTimeXTimes;
    private Integer NotifyDeadLineXTimes;

    public SettingsModel() {
        AlwaysNotifyStartTime = true;
        AlwaysNotifyDeadLine = true;
        NotifyStartTimeBefore = 0;
        NotifyDeadLineBefore = 0;
        NotifyStartTimeXTimes = 1;
        NotifyDeadLineXTimes = 1;
    }

    public Boolean getAlwaysNotifyStartTime() {
        return AlwaysNotifyStartTime;
    }

    public Boolean getAlwaysNotifyDeadLine() {
        return AlwaysNotifyDeadLine;
    }

    public Integer getNotifyStartTimeBefore() {
        return NotifyStartTimeBefore;
    }

    public Integer getNotifyDeadLineBefore() {
        return NotifyDeadLineBefore;
    }

    public Integer getNotifyStartTimeXTimes() {
        return NotifyStartTimeXTimes;
    }

    public Integer getNotifyDeadLineXTimes() {
        return NotifyDeadLineXTimes;
    }

    public void setAlwaysNotifyStartTime(Boolean alwaysNotifyStartTime) {
        AlwaysNotifyStartTime = alwaysNotifyStartTime;
    }

    public void setAlwaysNotifyDeadLine(Boolean alwaysNotifyDeadLine) {
        AlwaysNotifyDeadLine = alwaysNotifyDeadLine;
    }

    public void setNotifyStartTimeBefore(Integer notifyStartTimeBefore) {
        NotifyStartTimeBefore = notifyStartTimeBefore;
    }

    public void setNotifyDeadLineBefore(Integer notifyDeadLineBefore) {
        NotifyDeadLineBefore = notifyDeadLineBefore;
    }

    public void setNotifyStartTimeXTimes(Integer notifyStartTimeXTimes) {
        NotifyStartTimeXTimes = notifyStartTimeXTimes;
    }

    public void setNotifyDeadLineXTimes(Integer notifyDeadLineXTimes) {
        NotifyDeadLineXTimes = notifyDeadLineXTimes;
    }
}
