package com.projectse.aads.task_tracker.Models;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by Andrey Zolin on 28.01.2016.
 */
public class TaskModel {
    public Long id;
    public String name;
    public String description;
    private Date deadline;
    private Date startTime;
    private Long duration;
    private Boolean isNotifyDeadline = Boolean.FALSE;
    private Boolean isNotifyStartTime = Boolean.FALSE;
    private Boolean isDone = Boolean.FALSE;

    // not supported yet
    private Long parentTaskId;
    private List<TaskModel> subtasks = new ArrayList<>();
    private Integer priority = 0;


    public TaskModel(Long id, String name) {
        this.id = id;
        this.name = name;
        //this.description = description;

    }

    public TaskModel(){

    }
    public String getName() {
        return name;
    }

    public String toString() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Date getDeadline() {
        return deadline;
    }

    public void setDeadline(Date deadline) {
        this.deadline = deadline;
    }

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public Long getDuration() {
        return duration;
    }

    public void setDuration(Long duration) {
        this.duration = duration;
    }

    public Boolean getIsNotifyDeadline() {
        return isNotifyDeadline;
    }

    public void setIsNotifyDeadline(Boolean flag) {
        this.isNotifyDeadline = flag;
    }

    public Boolean getIsNotifyStartTime() {
        return isNotifyStartTime;
    }

    public void setIsNotifyStartTime(Boolean isNotifyStartTime) {
        this.isNotifyStartTime = isNotifyStartTime;
    }

    public Boolean getIsDone() {
        return isDone;
    }

    public void setIsDone(Boolean isDone) {
        this.isDone = isDone;
    }


}
