package com.projectse.aads.task_tracker.Fragments;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.Fragment;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.projectse.aads.task_tracker.Adapters.SubtasksAdapter;
import com.projectse.aads.task_tracker.DBService.DatabaseHelper;
import com.projectse.aads.task_tracker.Dialogs.ListOfCourses;
import com.projectse.aads.task_tracker.Interfaces.ActualTasksCaller;
import com.projectse.aads.task_tracker.Interfaces.EditTaskCaller;
import com.projectse.aads.task_tracker.Interfaces.TaskOverviewCaller;
import com.projectse.aads.task_tracker.Models.CourseModel;
import com.projectse.aads.task_tracker.Models.TaskModel;
import com.projectse.aads.task_tracker.NotifyService.AlertReceiver;
import com.projectse.aads.task_tracker.R;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Locale;
import java.util.TimeZone;

/**
 * Created by Davlatbek Isroilov on 3/27/2016.
 * Innopolis University
 */
public class TaskOverviewFragment extends TaskFragment {
    private Long parent_id = -1L;
    EditTaskCaller editTaskCaller;

    final int MAX_STREAMS = 1;
    SoundPool sp;
    int soundIdFinishTask;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        getActivity().setTitle("Task Overview");

        sp = new SoundPool(MAX_STREAMS, AudioManager.STREAM_MUSIC, 0);
        soundIdFinishTask = sp.load(getActivity(), R.raw.finishtask, 1);

        View view = inflater.inflate(R.layout.shared_content_task_new, container, false);
        //setupUI(view.findViewById(R.id.parentId));
        getViews(view);
        task = new TaskModel();
        course = new CourseModel();
        /*try {
            super.fillData(course.getId());
        } catch (Exception e) {
            e.printStackTrace();
        }*/
        setHasOptionsMenu(true);
        return view;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        if (activity instanceof EditTaskCaller) {
            editTaskCaller = (EditTaskCaller) activity;
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        getActivity().getMenuInflater().inflate(R.menu.menu_plan_overview, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:
                //finish();
                return true;
            /*case android.R.id.action_edittask:
                return true;*/
        }
        if (item.getTitle().equals("edittask")) {
            //TODO solve it
//            Intent intent = new Intent(getActivity(), TaskEditActivity.class);
//            intent.putExtra("task_id", task.getId());
            editTaskCaller.callEditTask();
        }
        else if (item.getTitle().equals("deletetask"))
            createDeleteDialog();
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void getViews(View view) {
        super.getViews(view);
        spinnerPriority.setEnabled(false);
        buttonCourseSelect.setVisibility(View.INVISIBLE);
        buttonDateStartTime.setVisibility(View.INVISIBLE);
        buttonDateDeadline.setVisibility(View.INVISIBLE);
        addSubtasks.setVisibility(View.INVISIBLE);
    }

    private void createDeleteDialog() {
        final AlertDialog.Builder alertDialog = new AlertDialog.Builder(getActivity().getApplicationContext());
        alertDialog.setMessage("Are you sure you want to delete this task?");

        alertDialog.setCancelable(false);
        alertDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (task.getSubtasks_ids().size() > 0) {
                    AlertDialog.Builder alertDialog1 = new AlertDialog.Builder(alertDialog.getContext());
                    alertDialog1.setMessage("Task contains subtasks.\n They also will be deleted. Are you sure?");

                    alertDialog1.setCancelable(false);
                    alertDialog1.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            /*Long task_id = getIntent().getLongExtra("task_id", -1);
                            for (Long sub_id : task.getSubtasks_ids())
                                db.deleteTask(sub_id);
                            db.deleteTask(task_id);
                            Intent intent = new Intent();
                            intent.putExtra("deleted_task_id", task_id);
                            setResult(RESULT_OK, intent);
                            finish();*/
                        }
                    });

                    alertDialog1.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    });

                    alertDialog1.create().show();
                } else {
                    /*Long task_id = getIntent().getLongExtra("task_id", -1);
                    db.deleteTask(task_id);
                    Intent intent = new Intent();
                    intent.putExtra("deleted_task_id", task_id);
                    setResult(RESULT_OK, intent);
                    finish();*/
                }
            }
        });

        alertDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });

        alertDialog.create().show();
    }

    public static void hideSoftKeyboard(Activity activity) {
        InputMethodManager inputMethodManager = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(), 0);
    }

    public void setupUI(View view) {

        //Set up touch listener for non-text box views to hide keyboard.
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
