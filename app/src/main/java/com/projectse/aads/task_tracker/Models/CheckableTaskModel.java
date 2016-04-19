package com.projectse.aads.task_tracker.Models;

public class CheckableTaskModel{
    private Boolean isChecked = false;
    private TaskModel task;

    public void setChecked(Boolean checked) {
        isChecked = checked;
    }

    public Boolean getChecked() {
        return isChecked;
    }

    public TaskModel getTask() {
        return task;
    }

    public CheckableTaskModel(TaskModel task){
        this.task = task;
    }

    public void check() {
        if (isChecked)
            isChecked = false;
        else
            isChecked = true;
    }
}