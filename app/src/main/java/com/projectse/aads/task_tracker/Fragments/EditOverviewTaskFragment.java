package com.projectse.aads.task_tracker.Fragments;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.media.AudioManager;
import android.media.SoundPool;
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
import android.widget.AdapterView;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Toast;

import com.projectse.aads.task_tracker.Interfaces.ActualTasksCaller;
import com.projectse.aads.task_tracker.Models.CourseModel;
import com.projectse.aads.task_tracker.Models.TaskModel;
import com.projectse.aads.task_tracker.R;
import com.projectse.aads.task_tracker.Utils.ShPrefUtils;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;
import java.util.TimeZone;

/**
 * Created by Davlatbek Isroilov on 4/4/2016.
 * Innopolis University
 */
public class EditOverviewTaskFragment extends TaskFragment{
    private ActualTasksCaller actualTasksCaller;
    private TaskCategoriesCaller categoriesCaller;
    private boolean areEditListenersRegistered = false;

    private Menu menu;

    final int MAX_STREAMS = 1;
    SoundPool sp;
    int soundIdFinishTask;

    public interface TaskCategoriesCaller{
        void callTasksCategory();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        onDestroyOptionsMenu();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        sp = new SoundPool(MAX_STREAMS, AudioManager.STREAM_MUSIC, 0);
        soundIdFinishTask = sp.load(getActivity(), R.raw.finishtask, 1);

        getActivity().setTitle("Task Overview");
        View view = inflater.inflate(R.layout.shared_content_task_new, container, false);
        setupUI(view);
        getViews(view);
        setHasOptionsMenu(true);
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Long task_id = getArguments().getLong("task_id");
        task = db.getTask(task_id);
        Long course_id = db.getCourseIdByTaskId(task_id);
        course = new CourseModel();
        try {
            super.fillData(course_id);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /*@Override
    public void onStart() {
        super.onStart();
        Long task_id = getArguments().getLong("task_id");
        task = db.getTask(task_id);
        Long course_id = db.getCourseIdByTaskId(task_id);
    }*/

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        if (activity instanceof ActualTasksCaller) {
            actualTasksCaller = (ActualTasksCaller) activity;
        }
        if(activity instanceof TaskCategoriesCaller)
            categoriesCaller = (TaskCategoriesCaller) activity;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        getActivity().getMenuInflater().inflate(R.menu.menu_plan_edit_overview, menu);
        menu.findItem(R.id.action_deletetask).setEnabled(true);
        menu.findItem(R.id.action_edittask).setEnabled(true);
        this.menu = menu;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getTitle().equals("edittask")) {
            //TODO solve it
//            Intent intent = new Intent(getActivity(), TaskEditActivity.class);
//            intent.putExtra("task_id", task.getId());
            switchToEditMode();
            menu.findItem(R.id.action_deletetask).setEnabled(false).setVisible(false);
            menu.findItem(R.id.action_edittask).setIcon(R.drawable.ic_save_24dp).setTitle("savetask");
        }
        else if (item.getTitle().equals("deletetask")) {
            createDeleteDialog();
        }
        else if (item.getTitle().equals("savetask")) {
            for (TaskModel taskModel : super.listNewSubtasks){
                long sid = db.addTask(taskModel);
                taskModel.setId(sid);
                task.addSubtask(taskModel);
            }
            super.listNewSubtasks.clear();
            db.updateTask(task);
            db.updateCourseToTask(task.getId(), dialogFragmentBuilder.getCourseId());

            super.listAllSubtasks.clear();
            subtasks_adapter.notifyDataSetChanged();
            switchToOverviewMode();
//            menu.findItem(R.id.action_savetask).setEnabled(false);
            menu.findItem(R.id.action_deletetask).setEnabled(false).setVisible(true);
            menu.findItem(R.id.action_edittask).setEnabled(true).setIcon(R.drawable.edit_material).setTitle("edittask");
            try {
                fillData(db.getTask(task.getId()).getCourse().getId());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void getViews(View view) {
        super.getViews(view);
        setDefaultOverviewView();
        switchDone.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                task.setIsDone(isChecked);

                if (isChecked && ShPrefUtils.isPlaySounds(getActivity())) {
                    sp.play(soundIdFinishTask, 1, 1, 0, 0, 1);
                }

                if (isChecked) {
                    if (timerOn.isChecked()) {
                        timerOn.setChecked(false);
                    }
                    
                    timerOn.setVisibility(View.INVISIBLE);
                } else {
                    timerOn.setVisibility(View.VISIBLE);
                }
            }
        });
    }

    private void setDefaultOverviewView() {
        switchDone.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                task.setIsDone(isChecked);
            }
        });

        switchDone.setEnabled(true);
        switchDone.setAlpha(1f);
        spinnerPriority.setEnabled(false);
        buttonCourseSelect.setEnabled(false);
        buttonCourseSelect.setAlpha(.4f);
        buttonCourseSelect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        buttonDateStartTime.setEnabled(false);
        buttonDateStartTime.setAlpha(.4f);
        buttonDateDeadline.setEnabled(false);
        buttonDateDeadline.setAlpha(.4f);
        addSubtasks.setEnabled(false);
        addSubtasks.setAlpha(.4f);
        /*clearSubtasks.setEnabled(false);
        clearSubtasks.setAlpha(.4f);*/
        editTextCourseName.setFocusable(false);

        /*nameView.setFocusable(false);
        descView.setFocusable(false);
        durationView.setFocusable(false);*/
    }

    private void createDeleteDialog() {
        final AlertDialog.Builder alertDialog = new AlertDialog.Builder(getActivity());
        alertDialog.setMessage("Are you sure you want to delete this task?");

        alertDialog.setCancelable(false);
        alertDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (task.getSubtasks_ids().size() > 0) {
                    AlertDialog.Builder alertDialog1 = new AlertDialog.Builder(alertDialog.getContext());
                    alertDialog1.setMessage("This task contains subtasks!\n They will be deleted too. Are you sure?");

                    alertDialog1.setCancelable(false);
                    alertDialog1.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            for (Long sub_id : task.getSubtasks_ids())
                                db.deleteTask(sub_id);
                            db.deleteTask(task.getId());
                            getFragmentManager().popBackStack();
                        }
                    });

                    alertDialog1.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    });

                    alertDialog1.create().show();
                } else {
                    db.deleteTask(task.getId());
                    getFragmentManager().popBackStack();
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
        if (view != null){
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

    public void switchToEditMode(){
        getActivity().setTitle("Edit Task");
        super.listNewSubtasks = new ArrayList<>();
        if(!areEditListenersRegistered) {
            setListeners();
            areEditListenersRegistered = true;
        }

        //timerOn.setEnabled(true);
        switchDone.setEnabled(false);
        switchDone.setAlpha(.4f);
        spinnerPriority.setEnabled(true);
        buttonCourseSelect.setEnabled(true);
        buttonCourseSelect.setAlpha(1f);
        buttonCourseSelect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickCourseList(v);
            }
        });
        buttonDateStartTime.setEnabled(true);
        buttonDateStartTime.setAlpha(1f);
        buttonDateDeadline.setEnabled(true);
        buttonDateDeadline.setAlpha(1f);
        addSubtasks.setEnabled(true);
        addSubtasks.setAlpha(1f);
        /*clearSubtasks.setEnabled(true);
        clearSubtasks.setAlpha(1f);*/

        /*nameView.setFocusable(true);
        descView.setFocusable(true);
        durationView.setFocusable(true);*/
    }

    /****************
     *EDIT TASK PART*
     ****************/

    public void switchToOverviewMode(){
        getActivity().setTitle("Task Overview");
        setDefaultOverviewView();
        hideSoftKeyboard(getActivity());
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

    /*@Override
    public void onPause() {
        DatabaseHelper db = DatabaseHelper.getsInstance(getActivity().getApplicationContext());
        db.updateTask(task);
        db.addCourseToTask(task.getId());
        long courseID = db.updateCourseToTask(task.getId(), dialogFragmentBuilder.getCourseId());
        Log.d("UPDATE COURSE", courseID + "");
        super.onPause();
    }*/

    @Override
    public void onDestroy() {
        // write changes to base
        db.updateTask(task);
        db.updateCourseToTask(task.getId(), dialogFragmentBuilder.getCourseId());
        //Log.d("UPDATE COURSE", courseID + "");
        //db.updateTask(task);
        //long courseID = db.updateCourseToTask(task.getId(), dialogFragmentBuilder.getCourseId());
        hideSoftKeyboard(getActivity());
        super.onDestroy();
    }

    private void setListeners() {
        spinnerPriority.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Log.d("PRIORITY", "Priority: " + position);
                switch (position) {
                    case 0:
                        task.setPriority(TaskModel.Priority.LOW);
                        priorityColor.setBackgroundColor(getResources().getColor(R.color.lowPriority));
                        //priorityColor.setForeground(new ColorDrawable(getResources().getColor(R.color.lowPriority)));
                        break;
                    case 1:
                        task.setPriority(TaskModel.Priority.MEDIUM);
                        priorityColor.setBackgroundColor(getResources().getColor(R.color.mediumPriority));
                        //priorityColor.setForeground(new ColorDrawable(getResources().getColor(R.color.mediumPriority)));
                        break;
                    case 2:
                        task.setPriority(TaskModel.Priority.HIGH);
                        priorityColor.setBackgroundColor(getResources().getColor(R.color.hignPriority));
                        //priorityColor.setForeground(new ColorDrawable(getResources().getColor(R.color.hignPriority)));
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
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
                    Toast.makeText(getActivity().getApplicationContext(), "Duration is numbers only", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    // flag for recursion exit
    private boolean flag = false;

    // return false, if everything is correct
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
            assert dCal != null;
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
            assert dCal != null;
            setDateTime(startTimeDateView, dCal.getTimeInMillis());
            isCorrected = true;
        }
        return isCorrected;
    }

}
