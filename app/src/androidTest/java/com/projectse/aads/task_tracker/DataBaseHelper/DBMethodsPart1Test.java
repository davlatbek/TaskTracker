package com.projectse.aads.task_tracker.DataBaseHelper;

import com.projectse.aads.task_tracker.Models.TaskModel;

import junit.framework.Assert;

import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

/**
 * Created by smith on 2/7/16.
 */
public class DBMethodsPart1Test extends TestInit {

    public void testMarkTaskIsDone() {
        TaskModel t = new TaskModel();
        t.setName("TestTask3");
        t.setId(db.addTask(t));
        long t_id = t.getId();
        try {
            db.markTaskAsDone(t.getId());
        } catch (Exception e) {
            e.printStackTrace();
            Assert.assertTrue(false);
        }
        Assert.assertTrue(db.getTask(t_id).getIsDone());
    }

    public void testUpdateTask(){
//        List<TaskModel> list = db.getTaskModelList();
        TaskModel t = new TaskModel();
        t.setId(db.addTask(t));
        t.setName("TestTask");
        long t_id = t.getId();

        String new_name = "TestTask1";
        t.setName(new_name);
        Calendar dateSt = Calendar.getInstance(TimeZone.getTimeZone("UTC"), Locale.getDefault());
        dateSt.set(Calendar.HOUR_OF_DAY, 20);
        t.setStartTime(dateSt);

        Calendar dateD = Calendar.getInstance(TimeZone.getTimeZone("UTC"), Locale.getDefault());
        dateD.set(Calendar.HOUR_OF_DAY, 23);
        t.setDeadline(dateD);

        db.updateTask(t);

        TaskModel t1 = db.getTask(t_id);
        assertEquals(t.getId(), t1.getId());
        assertEquals(new_name, t1.getName());
        assertEquals(0, t1.getStartTime().compareTo(dateSt));
        assertEquals(0, t1.getDeadline().compareTo(dateD));

    }

    public void testDeleteTask(){
        TaskModel t = new TaskModel();
        t.setName("TestTask");

        List<TaskModel> tasks = db.getTaskModelList();

        t.setId(db.addTask(t));
        long t_id = t.getId();

        tasks = db.getTaskModelList();

        db.deleteTask(t_id);
        tasks = db.getTaskModelList();
        TaskModel t_new = db.getTask(t_id);
        assertEquals(null, t_new); // instance has been deleted and doesn't exist in DB
    }

    public void testGetTaskForDay(){
        //init dates
        Calendar dateTarget = Calendar.getInstance(TimeZone.getTimeZone("UTC"), Locale.getDefault());
        dateTarget.add(Calendar.DAY_OF_MONTH, -1);

        Calendar dateRight1 = Calendar.getInstance(TimeZone.getTimeZone("UTC"), Locale.getDefault());
        dateRight1.add(Calendar.DAY_OF_MONTH, -1);
        dateRight1.set(Calendar.HOUR_OF_DAY, 23);

        Calendar dateRight2 = Calendar.getInstance(TimeZone.getTimeZone("UTC"), Locale.getDefault());
        dateRight2.add(Calendar.DAY_OF_MONTH, -1);
        dateRight2.set(Calendar.HOUR_OF_DAY, 0);

        Calendar dateWrong1 = Calendar.getInstance(TimeZone.getTimeZone("UTC"), Locale.getDefault());
        dateWrong1.add(Calendar.DAY_OF_MONTH, -2);

        Calendar dateWrong2 = Calendar.getInstance(TimeZone.getTimeZone("UTC"), Locale.getDefault());

        TaskModel t = new TaskModel();

        t.setStartTime(dateRight1);
        t.setId(db.addTask(t));
        t.setStartTime(dateRight2);
        t.setId(db.addTask(t));
        t.setStartTime(dateTarget);
        t.setId(db.addTask(t));

        t.setStartTime(dateWrong1);
        t.setId(db.addTask(t));
        t.setStartTime(dateWrong2);
        t.setId(db.addTask(t));

        assertEquals(3,db.getTasksForDay(dateTarget).size());
    }

    public void testGetTaskForToday(){
        //init dates

        Calendar dateRight1 = Calendar.getInstance(TimeZone.getTimeZone("UTC"), Locale.getDefault());
        dateRight1.set(Calendar.HOUR_OF_DAY, 23);
        Calendar dateRight2 = Calendar.getInstance(TimeZone.getTimeZone("UTC"), Locale.getDefault());
        dateRight2.add(Calendar.SECOND, 2);


        Calendar dateWrong1 = Calendar.getInstance(TimeZone.getTimeZone("UTC"), Locale.getDefault());
        dateWrong1.add(Calendar.DAY_OF_MONTH, -2);

        Calendar dateWrong2 = Calendar.getInstance(TimeZone.getTimeZone("UTC"), Locale.getDefault());
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
