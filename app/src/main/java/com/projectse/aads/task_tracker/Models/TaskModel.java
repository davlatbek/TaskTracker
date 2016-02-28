package com.projectse.aads.task_tracker.Models;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.TimeZone;

/**
 * Created by smith on 1/27/16.
 *
 * Contain data of task entity.
 */
public class TaskModel {

    private String name = "";
    private Long id;
    private String description = "";

    private Calendar startTime = Calendar.getInstance(TimeZone.getTimeZone("UTC"), Locale.getDefault());

    private Calendar deadline = Calendar.getInstance(TimeZone.getTimeZone("UTC"), Locale.getDefault());
    private Long duration;
    private Boolean isNotifyDeadline = Boolean.FALSE;
    private Boolean isNotifyStartTime = Boolean.FALSE;
    private Boolean isDone = Boolean.FALSE;

    public void setSubtasks_ids(List<Long> subtasks_ids) {
        this.subtasks_ids.clear();
        for(Long id : subtasks_ids)
            this.subtasks_ids.add(id);
    }

    private List<Long> subtasks_ids = new ArrayList<>();

    public Long getParentTaskId() {
        return parentTaskId;
    }

    public void setParentTaskId(Long parentTaskId) {
        this.parentTaskId = parentTaskId;
    }

    // not supported yet
    private Long parentTaskId = -1L;

    private Integer priority = 0;
    public TaskModel(){

        //set last second for a current day as default for startTime and deadline.
        startTime.set(Calendar.HOUR_OF_DAY, 23);
        startTime.set(Calendar.MINUTE, 59);
        startTime.set(Calendar.SECOND, 59);

        deadline.set(Calendar.HOUR_OF_DAY, 23);
        deadline.set(Calendar.MINUTE, 59);
        deadline.set(Calendar.SECOND, 59);

        duration = Long.valueOf(0);

    }

    public TaskModel(Long id, String name) {
        this();
        this.id = id;
        this.name = name;
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

    public Calendar getStartTime() {
        return startTime;
    }

    public void setStartTime(Calendar startTime) {
        this.startTime = startTime;
    }

    public Calendar getDeadline() {
        return deadline;
    }

    public void setDeadline(Calendar deadline) {
        this.deadline = deadline;
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

    public void addSubtask(TaskModel task){
        this.subtasks_ids.add(task.getId());
        if(this.id != null)task.setParentTaskId(this.id);
    }

    public List<Long> getSubtasks_ids() {
        return subtasks_ids;
    }

    public boolean deleteSubtask(Long subtask_id){
            return subtasks_ids.remove(subtask_id);
    }

    public void clearSubtasks() {
        subtasks_ids.clear();
    }

    public boolean isSubtask() {
        return parentTaskId!=null && parentTaskId > 0;
    }

    public boolean isSupertask() {
        return !isSubtask();
    }
}