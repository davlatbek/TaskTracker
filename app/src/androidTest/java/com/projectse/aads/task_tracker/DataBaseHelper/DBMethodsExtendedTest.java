package com.projectse.aads.task_tracker.DataBaseHelper;

import com.projectse.aads.task_tracker.Models.TaskModel;

import junit.framework.Assert;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * Created by smith on 2/7/16.
 */
public class DBMethodsExtendedTest extends TestInit {

    public void testUpdateSubtasks() {
        ArrayList<Long> subts = new ArrayList<>();

        TaskModel t1 = new TaskModel();
        t1.setName("TestTask1");
        t1.setId(db.addTask(t1));
        subts.add(t1.getId());

        TaskModel t2 = new TaskModel();
        t2.setName("TestTask2");
        t2.setId(db.addTask(t2));
        subts.add(t2.getId());

        TaskModel t = new TaskModel();
        t.setName("TestTaskMaster");
        t.setId(db.addTask(t));

        List<TaskModel> list = db.getTaskModelList();
        Assert.assertTrue(db.getTask(t.getId()).getSubtasks_ids().size() == 0);

        for(Long id : subts ){
            TaskModel t_buf = db.getTask(id);
            t.addSubtask(t_buf);
        }
        t.setSubtasks_ids(subts);
        try {
            db.updateTask(t);
        } catch (Exception e) {
            e.printStackTrace();
            Assert.assertTrue(false);
        }
        TaskModel t_new = db.getTask(t.getId());
        Assert.assertTrue(t_new.getSubtasks_ids().size() == 2);
    }
}
