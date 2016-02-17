package com.projectse.aads.task_tracker.Models;

import android.graphics.Color;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Andrey Zolin on 07.02.2016.
 */
public class CourseModel {

    public Color getClr() {
        return clr;
    }

    public void setClr(Color clr) {
        this.clr = clr;
    }

    public enum Priority{
        HIGH, LOW, MEDIUM
    }

    public Priority fromIntToPriority(int priorityInt) throws Exception {
        if (priorityInt == 1) return this.priority = Priority.LOW;
        if (priorityInt == 2) return this.priority = Priority.MEDIUM;
        if (priorityInt == 3) return this.priority = Priority.HIGH;
        throw new Exception("Check Priority input value fromIntToPriority");
    }
    public int fromPriorityToInt(Priority priority)throws Exception{
        if (priority.equals(Priority.LOW)) return 1;
        if (priority.equals(Priority.MEDIUM)) return 2;
        if (priority.equals(Priority.HIGH)) return 3;
        throw new Exception("Check priority input value fromPriorityToInt");
    }

    private String name;
    private Long id;
    private Color clr =null;
    // by default priority has low level
    private Priority priority = Priority.LOW;
    private List<TaskModel> tasksList = new ArrayList<TaskModel>();

    public CourseModel(String name, Priority priority) {
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
    public Priority getPriority() {
        return priority;
    }

    public void setPriority(Priority p) {
        this.priority = p;
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
