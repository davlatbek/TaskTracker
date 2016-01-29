package com.projectse.aads.task_tracker.Utils;

import android.os.Parcel;
import android.os.Parcelable;

import java.sql.Time;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by smith on 1/27/16.
 *
 * Contain data of task entity.
 */
public class Task implements Parcelable {

    private String name;
    private Long id;
    private String description;
    private Time startTime = new Time(System.currentTimeMillis());
    private Time deadline = new Time( startTime.getTime() + 7*24*60*60*1000 );
    private Long duration = new Long( 8*7*24 );
    private Boolean isNotifyDeadline = Boolean.FALSE;
    private Boolean isNotifyStartTime = Boolean.FALSE;
    private Boolean isDone = Boolean.FALSE;

    // not supported yet
    private Long parentTaskId;
    private List<Task> subtasks = new ArrayList<>();
    private Integer priority = 0;


    public Task(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    /**
     * Helps in intent creation.
     * @param in - Parcel object, that contain's data for transmition
     */
    protected Task(Parcel in) {
        id = in.readLong();
        name = in.readString();
        description = in.readString();
        deadline = (Time) in.readSerializable();
        startTime = (Time) in.readSerializable();

        boolean [] b_arr = new boolean[3];
        in.readBooleanArray(b_arr);
        isNotifyDeadline = b_arr[0];
        isNotifyStartTime = b_arr[1];
        isDone = b_arr[2];

        subtasks = in.createTypedArrayList(Task.CREATOR);
//        parentTaskId = in.readLong();
//        priority = in.readInt();
    }

    public static final Creator<Task> CREATOR = new Creator<Task>() {
        @Override
        public Task createFromParcel(Parcel in) {
            return new Task(in);
        }

        @Override
        public Task[] newArray(int size) {
            return new Task[size];
        }
    };

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

    public Time getDeadline() {
        return deadline;
    }

    public void setDeadline(Time deadline) {
        this.deadline = deadline;
    }

    public Time getStartTime() {
        return startTime;
    }

    public void setStartTime(Time startTime) {
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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(id);
        dest.writeString(name);
        dest.writeString(description);
        dest.writeSerializable(deadline);
        dest.writeSerializable(startTime);
        dest.writeBooleanArray(new boolean[]{isNotifyDeadline, isNotifyStartTime, isDone});
        dest.writeTypedList(subtasks);
//        dest.writeLong(parentTaskId==null?parentTaskId:0);
//        dest.writeInt(priority);
    }
}
