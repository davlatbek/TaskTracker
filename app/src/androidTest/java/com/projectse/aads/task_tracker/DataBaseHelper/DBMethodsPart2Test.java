package com.projectse.aads.task_tracker.DataBaseHelper;

import com.projectse.aads.task_tracker.Models.TaskModel;

import junit.framework.Assert;

import java.util.Calendar;

/**
 * Created by Davlatbek Isroilov on 2/18/2016.
 * Innopolis University
 */
public class DBMethodsPart2Test extends TestInit{

    public void testGetTasksForACurrentWeek() {

        Calendar dateRight1 = Calendar.getInstance();
        dateRight1.set(Calendar.HOUR_OF_DAY, 23);
        Calendar dateRight2 = Calendar.getInstance();
        dateRight2.add(Calendar.SECOND, 2);


        Calendar dateWrong1 = Calendar.getInstance();
        dateWrong1.add(Calendar.DAY_OF_MONTH, -2);

        Calendar dateWrong2 = Calendar.getInstance();
        dateWrong2.set(Calendar.HOUR_OF_DAY, 0);

        TaskModel t = new TaskModel();

        t.setStartTime(dateRight1);
        t.setId(db.addTask(t));
        t.setStartTime(dateRight2);
        t.setId(db.addTask(t));

        t.setStartTime(dateWrong1);
        t.setId(db.addTask(t));
        t.setStartTime(dateWrong2);
        t.setId(db.addTask(t));

        assertEquals(2,db.getTasksForToday().size());
    }
}