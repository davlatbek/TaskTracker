package com.projectse.aads.task_tracker.Models;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by smith on 4/20/16.
 */
public class CheckableCourseModel {
    private Boolean isChecked = false;
    private String course_name;
    private CourseModel course;
    private List<TaskModel> tasks = new ArrayList<>();

    public CheckableCourseModel(String course_name){
        this.course_name = course_name;
    }

    public CheckableCourseModel(CourseModel course){
        this.course = course;
        this.course_name = course.getName();
    }

    public List<TaskModel> getTasks() {
        return tasks;
    }

    public void setTasks(List<TaskModel> tasks) {
        this.tasks = tasks;
    }

    public Boolean getChecked() {
        return isChecked;
    }

    public void setChecked(Boolean checked) {
        isChecked = checked;
    }

    public CourseModel getCourse() {
        return course;
    }

    public void check() {
        if (isChecked)
            isChecked = false;
        else
            isChecked = true;
    }

    public String getCourseName() {
        return course_name;
    }
}
