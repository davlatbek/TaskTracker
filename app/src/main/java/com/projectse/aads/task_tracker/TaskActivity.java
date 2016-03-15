package com.projectse.aads.task_tracker;

import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.format.DateFormat;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.TimePicker;

import com.projectse.aads.task_tracker.Adapters.SubtasksAdapter;
import com.projectse.aads.task_tracker.DBService.DatabaseHelper;
import com.projectse.aads.task_tracker.Dialogs.AddSubtaskDialog;
import com.projectse.aads.task_tracker.Dialogs.ListOfCourses;
import com.projectse.aads.task_tracker.Models.CourseModel;
import com.projectse.aads.task_tracker.Models.TaskModel;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

/**
 * Created by smith on 2/23/16.
 */
public abstract class TaskActivity extends AppCompatActivity implements AddSubtaskDialog.NoticeDialogListener {

    // Views
    protected EditText nameView;
    protected EditText descView;
    protected TextView courseView;
    protected static EditText startTimeDateView;
    protected static EditText startTimeTimeView;
    protected static EditText deadlineDateView;
    protected static EditText deadlineTimeView;
    protected EditText durationView;
    protected Switch isStartTimeNotifyView, isDeadlineNotifyView;
    protected Spinner spinnerPriority;

    protected static DatabaseHelper db = null;
    protected ListView subtasksListView = null;
    protected static List<TaskModel> subtasks_list = new ArrayList<>();
    protected static SubtasksAdapter<TaskModel> subtasks_adapter = null;


    private static java.text.DateFormat dateFormat = new SimpleDateFormat("dd-MM-yy");
    private static java.text.DateFormat timeFormat = new SimpleDateFormat("HH:mm");

    protected ListOfCourses dialogFragmentBuilder;

    // Current task
    public static TaskModel task = null;
    public static CourseModel course = null;

    public TaskActivity(){
        super();
        TimeZone timeZone = TimeZone.getTimeZone("UTC");
        dateFormat.setTimeZone(timeZone);
        timeFormat.setTimeZone(timeZone);
    }

    private boolean isEmptyListSet = false;

    protected void getViews(){
        nameView = (EditText) findViewById(R.id.txtName);
        descView = (EditText) findViewById(R.id.txtDescription);
        courseView = (TextView) findViewById(R.id.coursename);

        startTimeDateView = (EditText) findViewById(R.id.txtDateStartTime);
        startTimeTimeView = (EditText) findViewById(R.id.txtTimeStartTime);

        deadlineDateView = (EditText) findViewById(R.id.txtDateDeadline);
        deadlineTimeView = (EditText) findViewById(R.id.txtTimeDeadline);

        durationView = (EditText) findViewById(R.id.txtDuration);

        isStartTimeNotifyView = (Switch) findViewById(R.id.swtStartTimeNotification);
        isDeadlineNotifyView = (Switch) findViewById(R.id.swtDeadlineNotification);

        spinnerPriority = (Spinner) findViewById(R.id.spinnerPriority);
        setPrioritySpinner();

        dialogFragmentBuilder = new ListOfCourses(this, new DatabaseHelper(this));
    }

    protected void fillData() {
        db = DatabaseHelper.getsInstance(getApplicationContext());
        db.getCourseIdByTaskId(task.getId());
        try {
            course = db.getCourse(task.getId());
        } catch (Exception e) {
            e.printStackTrace();
        }
        courseView.setText(course.getName());
        if (task.getName() != null ) nameView.setText(task.getName());
        if (task.getDescription() != null ) descView.setText(task.getDescription());
        if (task.getStartTime() != null ) {
            // TODO obsolete
//            startTimeDateView.setText((new Date(task.getStartTime().getTime())).toString());
//            startTimeTimeView.setText((new Time(task.getStartTime().getTime())).toString());

            setDateTime(startTimeDateView, startTimeTimeView, task.getStartTime().getTimeInMillis());
        }

        isStartTimeNotifyView.setChecked(task.getIsNotifyStartTime());

        if (task.getDeadline() != null ) {
            // TODO obsolete
//            deadlineDateView.setText((new Date(task.getDeadline().getTime())).toString());
//            deadlineTimeView.setText((new Time(task.getDeadline().getTime())).toString());
            setDateTime(deadlineDateView, deadlineTimeView, task.getDeadline().getTimeInMillis());
        }

        isDeadlineNotifyView.setChecked(task.getIsNotifyDeadline());

        /*if (task.getDuration() != null ) durationView.setText(task.getDuration().toString());
        switch (task.getPriority()){
            case LOW: spinnerPriority.setId(0);
                break;
            case MEDIUM: spinnerPriority.;
        }*/
        /*try {
            spinnerPriority.setSelection(task.priorityToInt(task.getPriority()));
        } catch (Exception e) {
            e.printStackTrace();
        }*/
        fillSubtasks();
    }

    private static void fillSubtasksList(){
        subtasks_list.clear();
        for(Long id : task.getSubtasks_ids()){
            subtasks_list.add(db.getTask(id));
        }
    }

    public void fillSubtasks(){
        fillSubtasksList();
        subtasks_adapter = new SubtasksAdapter<>(this,
                android.R.layout.simple_list_item_1, subtasks_list);

        subtasksListView = (ListView) findViewById(R.id.listViewSubtasks);
        subtasksListView.setAdapter(subtasks_adapter);

        if(!isEmptyListSet){
            TextView emptyList = new TextView(this);
            emptyList.setText("The list of subtasks is empty");
            subtasksListView.setEmptyView(emptyList);
            ((ViewGroup) subtasksListView.getParent()).addView(emptyList);
            emptyList.setTextSize(25);
            emptyList.setGravity(Gravity.CENTER);
            emptyList.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.MATCH_PARENT));
            isEmptyListSet = true;
        }

        subtasksListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, final View view,
                                    int position, long id) {
                final TaskModel item = (TaskModel) parent.getItemAtPosition(position);
                callTaskOverviewActivity(item);
            }

        });
    }

    /**
     * Set Calendar date to views.
     * @param dateTxt - date view.
     * @param timeTxt - time view.
     * @param calInMillis - time, that will be set.
     */
    protected static void setDateTime(EditText dateTxt, EditText timeTxt,long calInMillis){
        Calendar time = Calendar.getInstance(TimeZone.getTimeZone("UTC"), Locale.getDefault());
        time.setTimeInMillis(calInMillis);
        if(dateTxt != null) {
            dateTxt.setText(dateFormat.format(time.getTime()));
        }
        if(timeTxt != null) {
            timeTxt.setText(timeFormat.format(time.getTime()));
        }
    }

    protected static Calendar getCalendarFromTxtEditViews(EditText dateView, EditText timeView){
        Calendar cal = null;
        try {
            java.util.Date date = dateFormat.parse(String.valueOf(dateView.getText()));
            date.setTime(date.getTime() + timeFormat.parse(String.valueOf(timeView.getText())).getTime());
            cal = Calendar.getInstance(TimeZone.getTimeZone("UTC"), Locale.getDefault());
            cal.setTime(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return cal;
    }

    public void addSubtask(TaskModel subtask){
        SubtasksAdapter<TaskModel> adapter = (SubtasksAdapter<TaskModel>) subtasksListView.getAdapter();
        if( task == null || adapter == null)
            return;
        task.addSubtask(subtask);
        db.updateTask(task);
    }

    public void showTimePickerDialog(View v) {
        DialogFragment newFragment = null;
        if(v == findViewById(R.id.btnTimeStartTime)) {
            newFragment = new TimePickerFragment();
            ((TimePickerFragment)newFragment).setTxtEdit(startTimeTimeView);
        }
        if(v == findViewById(R.id.btnTimeDeadline)) {
            newFragment = new TimePickerFragment();
            ((TimePickerFragment)newFragment).setTxtEdit(deadlineTimeView);
        }
        if (newFragment == null)
            try {
                throw new Exception("Wrong view.");
            } catch (Exception e) {
                e.printStackTrace();
                return;
            }
        newFragment.show(getFragmentManager(), "timePicker");
    }

    public void showDatePickerDialog(View v) {
        DialogFragment newFragment = null;
        if(v == findViewById(R.id.btnDateStartTime)){
            newFragment = new DatePickerFragment();
            ((DatePickerFragment)newFragment).setTxtEdit(startTimeDateView);
        }
        if(v == findViewById(R.id.btnDateDeadline)){
            newFragment = new DatePickerFragment();
            ((DatePickerFragment)newFragment).setTxtEdit(deadlineDateView);
        }
        if (newFragment == null)
            try {
                throw new Exception("Wrong view.");
            } catch (Exception e) {
                e.printStackTrace();
                return;
            }
        newFragment.show(getFragmentManager(), "datePicker");
    }

    public void callAddSubtaskDialog(View view){
        AddSubtaskDialog newFragment = new AddSubtaskDialog();
        newFragment.parent = this;
        newFragment.show(getFragmentManager(), "sas");
//        runOnUiThread(new Runnable() {
//            @Override
//            public void run() {
//                subtasks_adapter.notifyDataSetChanged();
//            }
//        });
    }

    public static class TimePickerFragment extends DialogFragment
            implements TimePickerDialog.OnTimeSetListener {


        private EditText txtEdit = null;
        public void setTxtEdit(EditText t){
            txtEdit = t;
        }

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {

            int hour, minute;
            final Calendar curr = Calendar.getInstance(TimeZone.getTimeZone("UTC"), Locale.getDefault());
            hour = curr.get(Calendar.HOUR_OF_DAY);
            minute = curr.get(Calendar.MINUTE);
            if (txtEdit != null) {
                Calendar c = null;
                try {
                    c = Calendar.getInstance(TimeZone.getTimeZone("UTC"), Locale.getDefault());
                    c.setTime(timeFormat.parse(String.valueOf(txtEdit.getText())));
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                if(c != null){
                    hour = c.get(Calendar.HOUR_OF_DAY);
                    minute = c.get(Calendar.MINUTE);
                }
            }

            // Create a new instance of TimePickerDialog and return it
            return new TimePickerDialog(getActivity(), this, hour, minute,
                    DateFormat.is24HourFormat(getActivity()));
        }

        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
            Calendar c = Calendar.getInstance(TimeZone.getTimeZone("UTC"), Locale.getDefault());
            c.set(Calendar.HOUR_OF_DAY, hourOfDay);
            c.set(Calendar.MINUTE, minute);
            if(txtEdit != null) {
                TaskEditActivity.setDateTime(null, txtEdit,c.getTimeInMillis());
            }
        }
    }

    public static class DatePickerFragment extends DialogFragment
            implements DatePickerDialog.OnDateSetListener {

        private EditText txtEdit = null;

        public void setTxtEdit(EditText t){
            txtEdit = t;
        }

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            int year, month, day;
            final Calendar curr = Calendar.getInstance(TimeZone.getTimeZone("UTC"), Locale.getDefault());
            year = curr.get(Calendar.YEAR);
            month = curr.get(Calendar.MONTH);
            day = curr.get(Calendar.DAY_OF_MONTH);
            if (txtEdit != null) {
                Calendar c = null;
                try {
                    c = Calendar.getInstance(TimeZone.getTimeZone("UTC"), Locale.getDefault());
                    c.setTime(dateFormat.parse(String.valueOf(txtEdit.getText())));
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                if(c != null){
                    year = c.get(Calendar.YEAR);
                    month = c.get(Calendar.MONTH);
                    day = c.get(Calendar.DAY_OF_MONTH);
                }
            }

            // Create a new instance of DatePickerDialog and return it
            return new DatePickerDialog(getActivity(), this, year, month, day);
        }

        public void onDateSet(DatePicker view, int year, int month, int day) {
            Calendar c = Calendar.getInstance(TimeZone.getTimeZone("UTC"), Locale.getDefault());
            c.set(Calendar.YEAR, year);
            c.set(Calendar.MONTH, month);
            c.set(Calendar.DAY_OF_MONTH, day);
            if (txtEdit != null)
                TaskEditActivity.setDateTime(txtEdit,null,c.getTimeInMillis());
        }
    }



    public void callTaskOverviewActivity(TaskModel taskModel){
        Intent intent = new Intent (getApplicationContext(), TaskOverviewActivity.class);
        intent.putExtra("task_id", taskModel.getId());
        startActivityForResult(intent, RequestCode.REQ_CODE_VIEWTASK);

    }

    @Override
    protected synchronized void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(resultCode == RESULT_OK){
            fillSubtasksList();
            subtasks_adapter.notifyDataSetChanged();
        }
    }


    public void onClickCourseList(View view) {
        switch (view.getId()) {
            case R.id.selectCourse:
                dialogFragmentBuilder.show(getFragmentManager(), "selectcourse");
                break;
            default:
                break;
        }
    }

    public void setPrioritySpinner(){
        Spinner spinnerPriority = (Spinner) findViewById(R.id.spinnerPriority);
        final String[] priorities = new String[] {"Low", "Medium", "High"};
        ArrayAdapter<String> adapterPrioritySpinner = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_dropdown_item, priorities);
        spinnerPriority.setAdapter(adapterPrioritySpinner);
        spinnerPriority.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                try {
                    task.setPriority(task.intToPriority(position));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }

    public synchronized void deleteSubtask(final Long subtask_id) {
        task.deleteSubtask(subtask_id);
        db.updateTask(task);
        onResume();
    }

    public synchronized void OnClearSubtasks(View v) {
        task.clearSubtasks();
        db.updateTask(task);
        onResume();
    }

    @Override
    public synchronized void onDialogDismiss(DialogFragment dialog, TaskModel item){
        task = db.getTask(getIntent().getLongExtra("task_id",-1L));
        addSubtask(item);
        onResume();
//        subtasks_adapter.notifyDataSetChanged();
    }

}
