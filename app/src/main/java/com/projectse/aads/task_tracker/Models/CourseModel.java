package com.projectse.aads.task_tracker.Models;

import android.graphics.Color;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by Andrey Zolin on 07.02.2016.
 */
public class CourseModel {

    private String name;
    private Long id;
    private int clr =0;
    private String abbreviation;

    public int getClr() {
        return clr;
    }

    public void setClr(int clr) {
        this.clr = clr;
    }

    public enum Priority{
        HIGH, LOW, MEDIUM
    }


    // by default priority has low level
    private List<TaskModel> tasksList = new ArrayList<TaskModel>();

    public CourseModel(String name) {
        this.name = name;
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

        String[] word = name.split("\\s+");
        abbreviation = "";
        String[] prepositions = {"in", "at", "of", "the", "for", "through",
                "vs", "on", "from", "as", "an", "a", "and"};

        if (word.length == 1) {
            abbreviation = name.substring(0, 1).toUpperCase() + name.substring(1);
        } else {
            for (int i = 0; i < word.length; ++i) {
                if (Arrays.asList(prepositions).contains(word[i])) {
                    continue;
                }

                if (word[i].equals(word[i].toUpperCase())) {
                    abbreviation = abbreviation + word[i];
                } else {
                    abbreviation = abbreviation + word[i].substring(0, 1).toUpperCase();
                }
            }
        }
        if (abbreviation.length() > 3) {
            abbreviation = abbreviation.substring(0, 3);
        }
    }

    public Long getId() {
        return id;
    }


    public void setId(Long id) {
        this.id = id;
    }

    public String toStringWithPriority() {
        String sb = "";
        sb = "Name:" + this.name;
        return sb;
    }

    public String toString() {
        return this.name;
    }
}
