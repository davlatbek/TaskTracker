package com.projectse.aads.task_tracker.Models;


/**
 * Created by Anastasia A. Puzankova on 07-Feb-16.
 */

/*
 * Contain data of Setting entity
 */
public class SettingsModel {
    private Long SettingsId;
    // allow notify
    private Boolean AlwaysNotifyStartTime;
    private Boolean AlwaysNotifyDeadLine;
    // the time (in minutes) between notification and StartTime/DeadLine
    private Integer NotifyStartTimeBefore;
    private Integer NotifyDeadLineBefore;
    private Integer INSSSD;
    private Integer INSTD; // if not spec. task duration hours


    public SettingsModel() {
        this.AlwaysNotifyStartTime = false;
        this.AlwaysNotifyDeadLine = false;
        this.NotifyStartTimeBefore = 1;
        this.NotifyDeadLineBefore = 1;
        this.INSSSD = 2;
        this.INSTD = 4;
        this.SettingsId = 1L;
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

    public Long getSettingsId() {
        return SettingsId;
    }

    public void setSettingsId(Long settingsId) {
        SettingsId = settingsId;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getAlwaysNotifyDeadLine()).append(getAlwaysNotifyStartTime()).append(getINSSSD()).append(getSettingsId());
        return String.valueOf(sb);
    }

    public Integer getINSTD() {
        return INSTD;
    }

    public void setINSTD(Integer INSTD) {
        this.INSTD = INSTD;
    }
}
