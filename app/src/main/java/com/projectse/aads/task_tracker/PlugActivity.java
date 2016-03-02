package com.projectse.aads.task_tracker;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.projectse.aads.task_tracker.DBService.DatabaseHelper;
import com.projectse.aads.task_tracker.Models.TaskModel;

import junit.framework.Assert;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

/**
 * Created by smith on 2/29/16.
 */
public class PlugActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_plug);
        //initDebugData();
    }

    public void initDebugData(){
        DatabaseHelper db = DatabaseHelper.getsInstance(getApplicationContext());

        for ( int i = 0; i < 10; i++){
            ArrayList<Long> subts = new ArrayList<>();

            TaskModel t1 = new TaskModel();
            t1.setName("TestTask1");
            t1.setId(db.addTask(t1));
            t1.setStartTime(Calendar.getInstance(TimeZone.getTimeZone("UTC"), Locale.getDefault()));
            t1.setDeadline(Calendar.getInstance(TimeZone.getTimeZone("UTC"), Locale.getDefault()));
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
            }
        }


        Calendar dateTarget = Calendar.getInstance(TimeZone.getTimeZone("UTC"), Locale.getDefault());
        dateTarget.add(Calendar.DAY_OF_MONTH, -1);

        Calendar dateRight1 = Calendar.getInstance(TimeZone.getTimeZone("UTC"), Locale.getDefault());
        dateRight1.add(Calendar.DAY_OF_MONTH, -1);
        dateRight1.set(Calendar.HOUR_OF_DAY, 23);

        Calendar dateRight2 = Calendar.getInstance(TimeZone.getTimeZone("UTC"), Locale.getDefault());
        dateRight2.add(Calendar.DAY_OF_MONTH, -1);
        dateRight2.set(Calendar.HOUR_OF_DAY, 0);

        Calendar dateWrong1 = Calendar.getInstance(TimeZone.getTimeZone("UTC"), Locale.getDefault());
        dateWrong1.add(Calendar.DAY_OF_MONTH, 2);

        Calendar dateWrong2 = Calendar.getInstance(TimeZone.getTimeZone("UTC"), Locale.getDefault());

        TaskModel t11 = new TaskModel();

        t11.setName("Jordan");
        t11.setStartTime(dateRight1);
        t11.setId(db.addTask(t11));
        t11.setName("Fred");
        t11.setStartTime(dateRight2);
        t11.setId(db.addTask(t11));
        t11.setName("Dorian");
        t11.setStartTime(dateTarget);
        t11.setId(db.addTask(t11));

        t11.setName("Mark");
        t11.setStartTime(dateWrong1);
        t11.setId(db.addTask(t11));
        t11.setName("Frank");
        t11.setStartTime(dateWrong2);
        t11.setId(db.addTask(t11));
    }

    public void callDaylyPlan(View v){
        Intent intent = new Intent (getApplicationContext(), DaylyPlanActivity.class);
        startActivity(intent);
    }
}
