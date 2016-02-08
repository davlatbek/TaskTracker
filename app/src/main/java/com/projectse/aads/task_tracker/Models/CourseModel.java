package com.projectse.aads.task_tracker.Models;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Andrey Zolin on 07.02.2016.
 */
public class CourseModel {
    private String name;
    private Long id;
    private int priority = 0;
    private List<TaskModel> tasksList = new ArrayList<TaskModel>();

    public CourseModel(String name, int priority) {
        this.name = name;
        this.priority = priority;
    }

    public CourseModel() {

    }

    public void addTaskToCourse(TaskModel task){
        this.tasksList.add(task);
    }

    public void removeTaskFromCourse(TaskModel task) {
        this.tasksList.remove(task);
    }

    public List<TaskModel> listOfTasks() {

        return tasksList;
    }

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public int getPriority() {
        return priority;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String toString() {
        String sb = "";
        sb = "Name:" + this.name + ", priority:" + this.priority;

        return sb;
    }
}
