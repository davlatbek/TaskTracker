package com.projectse.aads.task_tracker.DataBaseHelper;

import com.projectse.aads.task_tracker.Models.TaskModel;

import junit.framework.Assert;

import java.util.Calendar;

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

        db.updateTask(t);
        TaskModel t1 = db.getTask(t_id);
        assertEquals(t1.getId(), t.getId());
        assertEquals(t1.getName(), t.getName());
    }

    public void testDeleteTask(){
        TaskModel t = new TaskModel();
        t.setName("TestTask");
        t.setId(db.addTask(t));
        long t_id = t.getId();

        db.deleteTask(t_id);
        assertEquals(null, db.getTask(t_id)); // instance has been deleted and doesn't exist in DB
    }

    public void testGetTaskForDay(){
        //init dates
        Calendar dateTarget = Calendar.getInstance();
        dateTarget.add(Calendar.DAY_OF_MONTH, -1);

        Calendar dateRight1 = Calendar.getInstance();
        dateRight1.add(Calendar.DAY_OF_MONTH, -1);
        dateRight1.set(Calendar.HOUR_OF_DAY, 23);

        Calendar dateRight2 = Calendar.getInstance();
        dateRight2.add(Calendar.DAY_OF_MONTH, -1);
        dateRight2.set(Calendar.HOUR_OF_DAY, 0);

        Calendar dateWrong1 = Calendar.getInstance();
        dateWrong1.add(Calendar.DAY_OF_MONTH, -2);

        Calendar dateWrong2 = Calendar.getInstance();

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
