package com.projectse.aads.task_tracker.Models;

import com.projectse.aads.task_tracker.MainActivity;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

/**
 * Created by smith on 1/27/16.
 * <p/>
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
    private List<Long> subtasks_ids = new ArrayList<>();
    private Long parentTaskId = -1L;
    private CourseModel course;
    private boolean isStartTimeSet = false;

    private Priority priority = Priority.LOW;

    public CourseModel getCourse() {
        return course;
    }

    public void setCourse(CourseModel course) {
        this.course = course;
    }

    public enum Priority {
        HIGH,
        MEDIUM,
        LOW
    }

    public Priority intToPriority(int priorityInt) throws Exception {
        switch (priorityInt) {
            case 0:
                return this.priority = Priority.LOW;
            case 1:
                return this.priority = Priority.MEDIUM;
            case 2:
                return this.priority = Priority.HIGH;
        }
        throw new Exception("Check priority input value in intToPriority()");
    }

    public int priorityToInt(Priority priority) throws Exception {
        switch (priority) {
            case LOW:
                return 0;
            case MEDIUM:
                return 1;
            case HIGH:
                return 2;
        }
        throw new Exception("Check priority input value in priorityToInt()");
    }

    public TaskModel() {

        //set last second for a current day as default for startTime and deadline.
        startTime.set(Calendar.HOUR_OF_DAY, 23);
        startTime.set(Calendar.MINUTE, 59);
        startTime.set(Calendar.SECOND, 58);

        deadline.set(Calendar.HOUR_OF_DAY, 23);
        deadline.set(Calendar.MINUTE, 59);
        deadline.set(Calendar.SECOND, 58);

        duration = 1L;

    }

    public TaskModel(Long id, String name) {
        this();
        this.id = id;
        this.name = name;
    }

    public void setSubtasks_ids(List<Long> subtasks_ids) {
        this.subtasks_ids.clear();
        for (Long id : subtasks_ids)
            this.subtasks_ids.add(id);
    }

    public Long getParentTaskId() {
        return parentTaskId;
    }

    public void setParentTaskId(Long parentTaskId) {
        this.parentTaskId = parentTaskId;
    }

    public Priority getPriority() {
        return priority;
    }

    public void setPriority(Priority priority) {
        this.priority = priority;
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
        if(startTime.get(Calendar.DAY_OF_YEAR)>deadline.get(Calendar.DAY_OF_YEAR))
            throw new IllegalArgumentException("Start time cannot be after deadline");
        this.startTime = startTime;
        isStartTimeSet = true;
    }

    public Calendar getDeadline() {
        return deadline;
    }

    public void setDeadline(Calendar deadline) {
        this.deadline = deadline;
        if(!isStartTimeSet) {
            startTime = (Calendar) deadline.clone();
            Integer diff = 1;
            if(MainActivity.settings != null)
                diff = MainActivity.settings.getINSSSD();
            startTime.add(Calendar.DAY_OF_WEEK, -1 * diff);
        }
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

    public void addSubtask(TaskModel task) {
        this.subtasks_ids.add(task.getId());
        if (this.id != null) task.setParentTaskId(this.id);
    }

    public List<Long> getSubtasks_ids() {
        return subtasks_ids;
    }

    public boolean deleteSubtask(Long subtask_id) {
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