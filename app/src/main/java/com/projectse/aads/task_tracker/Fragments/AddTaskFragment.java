package com.projectse.aads.task_tracker.Fragments;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ScrollView;

import com.projectse.aads.task_tracker.DBService.DatabaseHelper;
import com.projectse.aads.task_tracker.Interfaces.ActualTasksCaller;
import com.projectse.aads.task_tracker.Models.CourseModel;
import com.projectse.aads.task_tracker.Models.TaskModel;
import com.projectse.aads.task_tracker.NotifyService.AlertReceiver;
import com.projectse.aads.task_tracker.R;
import com.projectse.aads.task_tracker.Utils.ShPrefUtils;

import java.text.ParseException;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

/**
 * Created by Davlatbek Isroilov on 3/27/2016.
 * Innopolis University
 */
public class AddTaskFragment extends TaskFragment {
    ActualTasksCaller actualTasksCaller;
    private Long parent_id = -1L;
    private Menu menu;
    long course_id, default_start_time;


    final int MAX_STREAMS = 1;
    SoundPool sp;
    int soundIdTaskDone;

    public static void hideSoftKeyboard(Activity activity) {
        InputMethodManager inputMethodManager = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(), 0);
    }

    @Override
    public void onDestroyOptionsMenu() {
        super.onDestroyOptionsMenu();
    }

    @Override
    public void onDestroy() {
        hideSoftKeyboard(getActivity());
        super.onDestroy();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        getActivity().setTitle("Add Task");
        View view = inflater.inflate(R.layout.shared_content_task_new, container, false);
        setupUI(view);
        getViews(view);
        task = new TaskModel();
        course = new CourseModel();
        setPrioritySpinner(view);
        setHasOptionsMenu(true);
        return view;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        if (activity instanceof ActualTasksCaller) {
            actualTasksCaller = (ActualTasksCaller) activity;
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        getActivity().getMenuInflater().inflate(R.menu.menu_plan_addtask, menu);
        this.menu = menu;
        menu.findItem(R.id.action_addtask).setEnabled(true);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        sp = new SoundPool(MAX_STREAMS, AudioManager.STREAM_MUSIC, 0);
        soundIdTaskDone = sp.load(getActivity(), R.raw.taskdone, 1);

        if (getArguments() != null) {
            course_id = getArguments().getLong("course_id");
            default_start_time = getArguments().getLong("default_start_time");

            timeView.setVisibility(View.INVISIBLE);
            if(default_start_time > 0) {
                Calendar startDate = Calendar.getInstance();
                startDate.setTimeInMillis(default_start_time);
                setDateTime(startTimeDateView, startDate.getTimeInMillis());
            }
            if (course_id != -1L){
                try {
                    textViewCourseLabel.setText(db.getCourse(course_id).getAbbreviation());
                    textViewCourseLabel.setBackgroundColor(db.getCourse(course_id).getClr());
                    editTextCourseName.setText(db.getCourse(course_id).getName());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @Override
    protected void getViews(View view) {
        super.getViews(view);
        textViewCourseLabel.setVisibility(View.INVISIBLE);
        timerOn.setVisibility(View.INVISIBLE);
        switchDone.setVisibility(View.INVISIBLE);
        timeView.setVisibility(View.INVISIBLE);
        spentText.setVisibility(View.INVISIBLE);

        int nonzero_count = 0;
        long sum = 0L;
        List<TaskModel> allTasks = db.getTaskModelList();

        for (TaskModel task : allTasks) {
            Long spent = task.getTimeSpentMs();
            if (spent > 0) {
                nonzero_count += 1;
                sum += spent;
            }
        }

        if (nonzero_count == 0) {
            durationView.setText(String.valueOf(60));
        }
        else {
            Long minutes = sum / nonzero_count / 1000 / 60;
            durationView.setText(String.valueOf(minutes));
        }

        editTextCourseName.setFocusable(false);
        if (getActivity().getIntent().getBooleanExtra("hide_subtasks", false)) {
            ScrollView sub_l = (ScrollView) view.findViewById(R.id.subtasksScrollView);
            sub_l.setVisibility(View.INVISIBLE);
        }
        buttonCourseSelect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickCourseList(v);
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:
                return true;
        }
        if (item.getTitle().equals("addtask")) {
            addAndSaveToDb(getView());
            for (TaskModel taskModel : super.listAllSubtasks) {
                task.addSubtask(taskModel);
                db.updateTask(task);
            }
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * On-click button function for saving created task to database
     * and then going to actual tasks
     *
     * @param v
     */
    public void addAndSaveToDb(View v) {
        if (validateTaskFields(v)) {
            long task_id = addTaskToDatabase();
            if (course_id != 0) {
                db.addCourseToTask(task_id);
                db.updateCourseToTask(task_id, course_id);
                Log.d("course id", course_id + "");
            } else {
                course_id = dialogFragmentBuilder.getCourseId();
                if (course_id != 0) {
                    db.addCourseToTask(task_id);
                    db.updateCourseToTask(task_id, course_id);
                    Log.d("course id", course_id + "");
                }
            }
            getFragmentManager().popBackStack();


            if (ShPrefUtils.isPlaySounds(getActivity())) {
                sp.play(soundIdTaskDone, 1, 1, 0, 0, 1);
            }
            hideSoftKeyboard(getActivity());
        }
    }

    /**
     * Creating task data and adding to local database
     *
     * @return True if all data is successfully recorded to database
     */
    public long addTaskToDatabase() {
        Calendar deadLineCal, startTimeCal;
        deadLineCal = getCalendarFromTxtEditViews(deadlineDateView);
        try {
            task.setPriority(task.intToPriority(spinnerPriority.getSelectedItemPosition()));
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (startTimeDateView.getText().toString().equals(""))
            startTimeCal = Calendar.getInstance(TimeZone.getTimeZone("UTC"), Locale.getDefault());
        else
            startTimeCal = getCalendarFromTxtEditViews(startTimeDateView);

        task.setName(nameView.getText().toString());
        task.setDescription(descView.getText().toString());
        task.setDeadline(deadLineCal);
        task.setStartTime(startTimeCal);
        if (!durationView.getText().toString().equals("")/* || !durationView.getText().toString().equals("0")*/)
            task.setDuration(Long.parseLong(durationView.getText().toString()));
        else task.setDuration(1L);

        if (!(parent_id < 0))
            task.setParentTaskId(parent_id);
        setAlarmNotif();
        db = DatabaseHelper.getsInstance(getActivity());
        return db.addTask(task);

    }

    public void setAlarmNotif() {
        Long time = new GregorianCalendar().getTimeInMillis() + 5 * 1000;

        Log.d("TIME_NOTIFICATIONS SET", time.toString() + "");
        Intent alertIntent = new Intent(getActivity(), AlertReceiver.class);
        AlarmManager alarmManager = (AlarmManager)
                getActivity().getSystemService(Context.ALARM_SERVICE);
        alarmManager.set(AlarmManager.RTC_WAKEUP, time,
                PendingIntent.getBroadcast(getActivity(), 1, alertIntent, PendingIntent.FLAG_UPDATE_CURRENT));
    }

    public void setupUI(View view) {
        //Set up touch listener for non-text box views to hide keyboard.
        if (view != null) {
            if (!(view instanceof EditText)) {
                view.setOnTouchListener(new View.OnTouchListener() {
                    public boolean onTouch(View v, MotionEvent event) {
                        hideSoftKeyboard(getActivity());
                        return false;
                    }
                });
            }
            //If a layout container, iterate over children and seed recursion.
            if (view instanceof ViewGroup) {
                for (int i = 0; i < ((ViewGroup) view).getChildCount(); i++) {
                    View innerView = ((ViewGroup) view).getChildAt(i);
                    setupUI(innerView);
                }
            }
        }
    }

    @Override
    public void setPrioritySpinner(View view) {
        super.setPrioritySpinner(view);
        try {
            switch (spinnerPriority.getSelectedItemPosition()) {
                case 0:
                    priorityColor.setBackgroundColor(getResources().getColor(R.color.lowPriority));
                    break;
                case 1:
                    priorityColor.setBackgroundColor(getResources().getColor(R.color.mediumPriority));
                    break;
                case 2:
                    priorityColor.setBackgroundColor(getResources().getColor(R.color.hignPriority));
                    break;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
