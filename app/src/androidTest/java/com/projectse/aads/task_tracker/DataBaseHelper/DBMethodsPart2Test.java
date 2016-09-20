package com.projectse.aads.task_tracker.DataBaseHelper;

import android.util.Log;

import com.projectse.aads.task_tracker.Models.TaskModel;

import junit.framework.Assert;

import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

/**
 * Created by Davlatbek Isroilov on 2/18/2016.
 * Innopolis University
 */
public class DBMethodsPart2Test extends TestInit {
    public static String TAG = "tag";

    public void testAddTask() {
        TaskModel task = new TaskModel(2L, "Task 1");
        Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("UTC"), Locale.getDefault());
        cal.set(Calendar.DATE, 1);
        task.setDeadline(cal);
        long id = db.addTask(task);
        assertEquals(task.getName(), db.getTask(id).getName());
        assertEquals(task.getDeadline(), db.getTask(id).getDeadline());
    }

    public void testGetTask() {
        TaskModel task = new TaskModel(1L, "Task 1");
        long addedTaskId = db.addTask(task);
        Log.d(TAG, "================ 1 == " + db.getTask(1).getId());
        assertEquals(1L, (long) db.getTask(1).getId());
        assertEquals("Task 1", db.getTask(1).getName());
    }

    public void testGetTasksForACurrentWeek() {

        Calendar beforeCurrentDate = Calendar.getInstance(TimeZone.getTimeZone("UTC"), Locale.getDefault());
        beforeCurrentDate.add(Calendar.DATE, -1);
        beforeCurrentDate.set(Calendar.HOUR_OF_DAY, 23);
        beforeCurrentDate.set(Calendar.MINUTE, 59);
        beforeCurrentDate.set(Calendar.SECOND, 59);

        Calendar currentDate = Calendar.getInstance(TimeZone.getTimeZone("UTC"), Locale.getDefault());
        currentDate.set(Calendar.HOUR_OF_DAY, 23);
        currentDate.set(Calendar.MINUTE, 59);
        currentDate.set(Calendar.SECOND, 59);

        Calendar afterCurrentDate = Calendar.getInstance(TimeZone.getTimeZone("UTC"), Locale.getDefault());
        afterCurrentDate.add(Calendar.DATE, 8);
        afterCurrentDate.set(Calendar.HOUR_OF_DAY, 0);
        afterCurrentDate.set(Calendar.MINUTE, 0);
        afterCurrentDate.set(Calendar.SECOND, 0);

        TaskModel task = new TaskModel();
        task.setStartTime(beforeCurrentDate);
        task.setId(db.addTask(task));

        task.setStartTime(currentDate);
        task.setId(db.addTask(task));

        task.setStartTime(afterCurrentDate);
        task.setId(db.addTask(task));
        //Log.d(TAG, "1 == " + db.getTasksForACurrentWeek().size());
        //assertEquals(1, db.getTasksForACurrentWeek().size());
    }

    public void testGetTasksForAChosenWeek() {

        Calendar startDate = Calendar.getInstance(TimeZone.getTimeZone("UTC"), Locale.getDefault());

        Calendar currentDate = Calendar.getInstance(TimeZone.getTimeZone("UTC"), Locale.getDefault());
        currentDate.set(Calendar.HOUR_OF_DAY, 00);
        currentDate.set(Calendar.MINUTE, 00);
        currentDate.set(Calendar.SECOND, 02);

        Calendar currentDate2 = Calendar.getInstance(TimeZone.getTimeZone("UTC"), Locale.getDefault());
        currentDate2.add(Calendar.DATE, 3);
        currentDate2.set(Calendar.HOUR_OF_DAY, 02);
        currentDate2.set(Calendar.MINUTE, 02);
        currentDate2.set(Calendar.SECOND, 02);

        Calendar beforeCurrentDate = Calendar.getInstance(TimeZone.getTimeZone("UTC"), Locale.getDefault());
        beforeCurrentDate.set(Calendar.DATE, -1);
        beforeCurrentDate.set(Calendar.HOUR_OF_DAY, 23);
        beforeCurrentDate.set(Calendar.MINUTE, 59);
        beforeCurrentDate.set(Calendar.SECOND, 59);

        Calendar afterCurrentDate = Calendar.getInstance(TimeZone.getTimeZone("UTC"), Locale.getDefault());
        afterCurrentDate.add(Calendar.DATE, 8);
        afterCurrentDate.set(Calendar.HOUR_OF_DAY, 00);
        afterCurrentDate.set(Calendar.MINUTE, 00);
        afterCurrentDate.set(Calendar.SECOND, 01);

        TaskModel task = new TaskModel();
        task.setName("Task 1");
        task.setStartTime(beforeCurrentDate);
        task.setId(db.addTask(task));
        Log.d(TAG, "task 1 " + task.getStartTime().getTime().toString() + "\n");

        task.setName("Task 2");
        task.setStartTime(currentDate);
        task.setId(db.addTask(task));
        Log.d(TAG, "task 2 " + task.getStartTime().getTime().toString() + "\n");

        task.setName("Task 3");
        task.setStartTime(afterCurrentDate);
        task.setId(db.addTask(task));
        Log.d(TAG, "task 3 " + task.getStartTime().getTime().toString() + "\n");

        task.setName("Task 4");
        task.setStartTime(currentDate2);
        task.setId(db.addTask(task));
        Log.d(TAG, "task 4 " + task.getStartTime().getTime().toString() + "\n");

        /*
        for (TaskModel t : db.getTasksForAChosenWeek(startDate)){
            Log.d("cw", "==============================================" +
                    "Name: " + t.getName() + "\n" +
                    "Starttime: " + t.getStartTime().getTime().toString() + "\n" +
                    "Deadline: " + "\n");
        }

        assertEquals(2, (db.getTasksForAChosenWeek(startDate)).size());
        */
    }


}