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
    // the time between notification and StartTime/DeadLine
    private String NotifyStartTimeBefore;
    private String NotifyDeadLineBefore;
    // number of notifications
    private Integer NotifyStartTimeXTimes;

    private Integer NotifyDeadLineXTimes;

    public SettingsModel() {
        AlwaysNotifyStartTime = true;
        AlwaysNotifyDeadLine = true;
        NotifyStartTimeBefore = "";
        NotifyDeadLineBefore = "";
        NotifyStartTimeXTimes = 1;
        NotifyDeadLineXTimes = 1;
    }

    public Boolean getAlwaysNotifyStartTime() {
        return AlwaysNotifyStartTime;
    }

    public Boolean getAlwaysNotifyDeadLine() {
        return AlwaysNotifyDeadLine;
    }

    public String getNotifyStartTimeBefore() {
        return NotifyStartTimeBefore;
    }

    public String getNotifyDeadLineBefore() {
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

    public void setNotifyStartTimeBefore(String notifyStartTimeBefore) {
        NotifyStartTimeBefore = notifyStartTimeBefore;
    }

    public void setNotifyDeadLineBefore(String notifyDeadLineBefore) {
        NotifyDeadLineBefore = notifyDeadLineBefore;
    }

    public void setNotifyStartTimeXTimes(Integer notifyStartTimeXTimes) {
        NotifyStartTimeXTimes = notifyStartTimeXTimes;
    }

    public void setNotifyDeadLineXTimes(Integer notifyDeadLineXTimes) {
        NotifyDeadLineXTimes = notifyDeadLineXTimes;
    }
}
