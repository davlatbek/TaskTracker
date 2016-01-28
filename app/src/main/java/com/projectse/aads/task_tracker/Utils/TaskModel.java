package com.projectse.aads.task_tracker.Utils;

/**
 * Created by admin on 28.01.2016.
 */
public class TaskModel {
    public int id;
    public String name;
    public String description;


    public TaskModel(int id, String name, String description) {
        this.id = id;
        this.name = name;
        this.description = description;

    }

    public TaskModel(){

    }
}
