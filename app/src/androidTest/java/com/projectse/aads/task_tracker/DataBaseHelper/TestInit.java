package com.projectse.aads.task_tracker.DataBaseHelper;

import android.test.AndroidTestCase;
import android.test.RenamingDelegatingContext;

import com.projectse.aads.task_tracker.DBService.DatabaseHelper;

/**
 * Created by smith on 2/7/16.
 */
class TestInit extends AndroidTestCase {
    protected DatabaseHelper db;

    @Override
    public void setUp() throws Exception {
        super.setUp();
        RenamingDelegatingContext context = new RenamingDelegatingContext(getContext(), "test_");
        context.deleteDatabase(DatabaseHelper.DATABASE_NAME);
        db = new DatabaseHelper(context);
    }

    @Override
    public void tearDown() throws Exception {
        db.close();
        super.tearDown();
    }
}
