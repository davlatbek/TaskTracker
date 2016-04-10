package com.projectse.aads.task_tracker.Models;


/**
 * Created by Anastasia A. Puzankova on 07-Feb-16.
 */

/*
 * Contain data of Setting entity
 */
public class SettingsModel {
    private String SettingsName;
    // allow notify
    private Boolean AlwaysNotifyStartTime;
    private Boolean AlwaysNotifyDeadLine;
    // the time between notification and StartTime/DeadLine
    private Integer NotifyStartTimeBefore;
    private Integer NotifyDeadLineBefore;
    private Integer INSSSD;


    public SettingsModel() {
        this.AlwaysNotifyStartTime = true;
        this.AlwaysNotifyDeadLine = false;
        this.NotifyStartTimeBefore = 1;
        this.NotifyDeadLineBefore = 1;
        this.INSSSD = 5;
    }


    public Integer getINSSSD() {
        return INSSSD;
    }

    public void setINSSSD(Integer INSSSD) {
        this.INSSSD = INSSSD;
    }

    public Boolean getAlwaysNotifyStartTime() {
        return AlwaysNotifyStartTime;
    }

    public void setAlwaysNotifyStartTime(Boolean alwaysNotifyStartTime) {
        AlwaysNotifyStartTime = alwaysNotifyStartTime;
    }

    public Boolean getAlwaysNotifyDeadLine() {
        return AlwaysNotifyDeadLine;
    }

    public void setAlwaysNotifyDeadLine(Boolean alwaysNotifyDeadLine) {
        AlwaysNotifyDeadLine = alwaysNotifyDeadLine;
    }

    public Integer getNotifyStartTimeBefore() {
        return NotifyStartTimeBefore;
    }

    public void setNotifyStartTimeBefore(Integer notifyStartTimeBefore) {
        NotifyStartTimeBefore = notifyStartTimeBefore;
    }

    public Integer getNotifyDeadLineBefore() {
        return NotifyDeadLineBefore;
    }

    public void setNotifyDeadLineBefore(Integer notifyDeadLineBefore) {
        NotifyDeadLineBefore = notifyDeadLineBefore;
    }

    public String getSettingsName() {
        return SettingsName;
    }

    public void setSettingsName(String settingsName) {
        SettingsName = settingsName;
    }
}
