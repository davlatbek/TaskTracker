package com.projectse.aads.task_tracker;

import android.test.AndroidTestCase;
import android.test.RenamingDelegatingContext;

import com.projectse.aads.task_tracker.DBService.DatabaseHelper;
import com.projectse.aads.task_tracker.Models.TaskModel;

import org.junit.Test;

/**
 * Created by smith on 2/7/16.
 */
public class DataBaseHelperTest extends AndroidTestCase {
    private DatabaseHelper db;

    @Override
    public void setUp() throws Exception {
        super.setUp();
        RenamingDelegatingContext context = new RenamingDelegatingContext(getContext(), "test_");
        db = new DatabaseHelper(context);
    }

    @Override
    public void tearDown() throws Exception {
        db.close();
        super.tearDown();
    }

    @Test
    public void markTaskIsDone() {
        DatabaseHelper db = DatabaseHelper.getsInstance(getContext());

        TaskModel t = new TaskModel();
        t.setId(db.addTask(t));
        t.setName("TestTask");
        long t_id = t.getId();
        try {
            db.markTaskAsDone(t.getId());
        } catch (Exception e) {
            e.printStackTrace();
            assert (false);
        }
        assert(db.getTask(t_id).getIsDone());
    }

    @Test
    public void deleteTask(){
        DatabaseHelper db = DatabaseHelper.getsInstance(getContext());
        TaskModel t = new TaskModel();
        t.setId(db.addTask(t));
        t.setName("TestTask");
        long t_id = t.getId();
        db.deleteTask(t_id);
        assertEquals(null, db.getTask(t_id)); // instance has been deleted and doesn't exist in DB
    }

}
