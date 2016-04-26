package com.projectse.aads.task_tracker.Fragments;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextWatcher;
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
import android.widget.TextView;
import android.widget.Toast;

import com.projectse.aads.task_tracker.DBService.DatabaseHelper;
import com.projectse.aads.task_tracker.Models.CourseModel;
import com.projectse.aads.task_tracker.Models.TaskModel;
import com.projectse.aads.task_tracker.R;

import java.util.Calendar;
import java.util.Locale;
import java.util.TimeZone;

/**
 * Created by Davlatbek Isroilov on 4/3/2016.
 * Innopolis University
 */
public class EditTaskFragment extends TaskFragment {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        getActivity().setTitle("Edit Task");
        View view = inflater.inflate(R.layout.shared_content_task_new, container, false);
        //setupUI(view.findViewById(R.id.parentId));
        getViews(view);
        setListeners();
        switchDone.setVisibility(View.INVISIBLE);
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
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        getActivity().getMenuInflater().inflate(R.menu.menu_plan_edittask, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:
                DatabaseHelper db = DatabaseHelper.getsInstance(getActivity().getApplicationContext());
                db.updateTask(task);
                Intent intent = new Intent();
                intent.putExtra("task_id", task.getId());
                //setResult(RESULT_OK, intent);
                //finish();
                return true;
        }
        if (item.getTitle().equals("savetask")) {
            // write changes to base
            db.updateTask(task);
            long courseID = db.updateCourseToTask(task.getId(), dialogFragmentBuilder.getCourseId());
            Log.d("UPDATE COURSE", courseID + "");
        }
        return super.onOptionsItemSelected(item);
    }

    /*@Override
    public void onResume() {
        super.onResume();
        TextView t = (TextView) getActivity().findViewById(R.id.textSelectedCourse);
        Long task_id = getIntent().getLongExtra("task_id", -1);
        task = db.getTask(task_id);
        long course_id = db.getCourseIdByTaskId(task_id);
        checkCourse(course_id);
        ScrollView sub_l = (ScrollView) getActivity().findViewById(R.id.subtasksScrollView);
        sub_l.setVisibility(View.VISIBLE);
        if (task != null && task.getParentTaskId() != null && task.getParentTaskId() > 0) {
            sub_l.setVisibility(View.INVISIBLE);
        }
        if (task != null) try {
            fillData(task.getCourse().getId());
        } catch (Exception e) {
            e.printStackTrace();
        }
        setListeners();
    }*/

    @Override
    public void onPause() {
        DatabaseHelper db = DatabaseHelper.getsInstance(getActivity().getApplicationContext());
        db.updateTask(task);
        db.addCourseToTask(task.getId());
        long courseID = db.updateCourseToTask(task.getId(), dialogFragmentBuilder.getCourseId());
        Log.d("UPDATE COURSE", courseID + "");
        hideSoftKeyboard(getActivity());
        super.onPause();
    }

    @Override
    public void onDestroy() {
        // write changes to base
        DatabaseHelper db = DatabaseHelper.getsInstance(getActivity().getApplicationContext());
        db.updateTask(task);
        long courseID = db.updateCourseToTask(task.getId(), dialogFragmentBuilder.getCourseId());
        Log.d("UPDATE COURSE", courseID + "");
        //setResult(RESULT_OK);
        hideSoftKeyboard(getActivity());
        super.onDestroy();
        //finish();
    }

    public void checkCourse(long course_id) {
        TextView t = (TextView) getActivity().findViewById(R.id.textSelectedCourse);
        try {
            course = db.getCourse(course_id);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (course_id > 0) {
            t.setText("Course: "
                    + course.getName());
            t.setBackgroundColor(course.getClr());
        } else {
            t.setText("Course is not selected");
        }
    }

    private void setListeners() {

        nameView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                task.setName(s.toString());
            }
        });
        descView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                task.setDescription(s.toString());
            }
        });
        durationView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if ((s.toString()).matches("\\d+")) {
                    task.setDuration(Long.parseLong(s.toString()));
                } else {
                    Toast.makeText(getActivity().getApplicationContext(), "Duration is number only", Toast.LENGTH_SHORT).show();
                }
            }
        });
        startTimeDateView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                correctTime();
                task.setStartTime(getCalendarFromTxtEditViews(startTimeDateView));
            }
        });
        deadlineDateView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                correctTime();
                task.setDeadline(getCalendarFromTxtEditViews(deadlineDateView));
            }
        });
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

    // flag for recursion exit. Use in correctTime only.
    private boolean flag = false;

    // return false, if all was correct
    public boolean correctTime() {

        Calendar dCal = getCalendarFromTxtEditViews(deadlineDateView);
        Calendar stCal = getCalendarFromTxtEditViews(startTimeDateView);
        boolean isCorrected = false;
        if (flag) {
            flag = false;
            return false;
        }

        if (dCal == null && stCal == null)
            return false;
        // Case: deadline cannot be earlier than now.
        // Set default.
        if (Calendar.getInstance(TimeZone.getTimeZone("UTC"), Locale.getDefault()).after(dCal)) {
            flag = true;
            dCal = Calendar.getInstance(TimeZone.getTimeZone("UTC"), Locale.getDefault());
            setDateTime(deadlineDateView, dCal.getTimeInMillis());
            isCorrected = true;
        }

        // Case: if duration more than difference of deadline and startTime.
        // Don't allow this. Set duration 0.
        if (!durationView.getText().toString().equals("")) {
            if (dCal.getTime().getTime() - stCal.getTime().getTime() < Long.parseLong(durationView.getText().toString()) * 60 * 60 * 1000) {
                flag = true;
                Toast.makeText(getActivity().getApplicationContext(),
                        "Duration cannot be more than defference between start time and deadline.", Toast.LENGTH_SHORT).show();
                durationView.setText(String.valueOf(0));
                isCorrected = true;
            }
        }
        // Case: start time after deadline.
        // Set starttime = deadline.
        if (stCal.after(dCal)) {
            flag = true;
            setDateTime(startTimeDateView, dCal.getTimeInMillis());
            isCorrected = true;
        }
        return isCorrected;
    }
}
