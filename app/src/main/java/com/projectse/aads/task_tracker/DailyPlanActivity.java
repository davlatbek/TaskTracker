package com.projectse.aads.task_tracker;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.projectse.aads.task_tracker.DBService.DatabaseHelper;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

/**
 * Shows list of tasks
 */
public class DailyPlanActivity extends PlanActivity {
    Calendar day = Calendar.getInstance(TimeZone.getTimeZone("UTC"), Locale.getDefault());
    private static java.text.DateFormat dateFormat = new SimpleDateFormat("dd-MM-yy");

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        
        Long time = getIntent().getLongExtra("day", -1);
        if(time > 0)
            day.setTime(new Date(time));
        setTime();
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                callAddTaskActivity();
            }
        });

        ImageView nextBtn = (ImageView) findViewById(R.id.imgRight);
        nextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onDayForward();
            }
        });
        ImageView backBtn = (ImageView) findViewById(R.id.imgLeft);
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onDayBack();
            }
        });
    }

    @Override
    protected void onResume() {
        final DatabaseHelper db = DatabaseHelper.getsInstance(this);
        taskList.clear();
        taskList = db.getTasksForDay(day);
        super.onResume();
    }

    private void setTime(){
        TextView txt = (TextView) findViewById(R.id.txtDay);
        txt.setText(dateFormat.format(day.getTime()));
    }

    public void onDayForward(){
        day.add(Calendar.DAY_OF_MONTH,1);
        callDaylyPlan();
    }

    public void onDayBack(){
        day.add(Calendar.DAY_OF_MONTH,-1);
        callDaylyPlan();
    }

    public void callDaylyPlan(){
        Intent intent = new Intent (getApplicationContext(), DailyPlanActivity.class);
        intent.putExtra("day", day.getTimeInMillis());
        startActivity(intent);
        //should call finish() ?
    }
}