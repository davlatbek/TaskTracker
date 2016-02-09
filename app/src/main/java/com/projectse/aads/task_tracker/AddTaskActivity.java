package com.projectse.aads.task_tracker;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.format.DateFormat;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TimePicker;

import com.projectse.aads.task_tracker.DBService.DatabaseHelper;
import com.projectse.aads.task_tracker.Models.TaskModel;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * Created by Davlatbek Isroilov on 1/31/2016.
 * Innopolis University
 */
public class AddTaskActivity extends AppCompatActivity {

    //Text views
    private EditText startTimeTimeView, deadlineTimeView,
            startTimeDateView, deadlineDateView;
    public DatabaseHelper databaseHelper;
    private static java.text.DateFormat dateFormat = new SimpleDateFormat("dd-MM-yy");
    private static java.text.DateFormat timeFormat = new SimpleDateFormat("HH:mm");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addtask);
        getViews();
        databaseHelper = DatabaseHelper.getsInstance(this);
        //fillData();
        /*Spinner dropdown = (Spinner) findViewById(R.id.spinner);
        String[] items = new String[]{"Medium", "High", "Low"};
        final ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_dropdown_item, items);
        dropdown.setAdapter(adapter);*/

        //Validation of fields
        /*final EditText nameEditText = (EditText) findViewById(R.id.txtName);
        findViewById(R.id.addTaskButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                final String name = nameEditText.getText().toString();
                if (name.length() == 0) {
                    nameEditText.setError("Invalid Name");
                }
            }
        });*/
    }

    public void getViews() {
        startTimeDateView = (EditText) findViewById(R.id.txtDateStartTime);
        startTimeTimeView = (EditText) findViewById(R.id.txtTimeStartTime);

        deadlineDateView = (EditText) findViewById(R.id.txtDateDeadline);
        deadlineTimeView = (EditText) findViewById(R.id.txtTimeDeadline);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    /**
     * On-click button function for saving created task to database
     * and then going back to PlanActivity
     *
     * @param v
     */
    public void AddAndSaveToDb(View v) {
        if (ValidateTaskFields()) {
            if (AddTaskToDatabase()) {
                Intent intent = new Intent(this, PlanActivity.class);
                startActivity(intent);
            }
        }
    }

    /**
     * Validating new task properties
     * @return True - if all required fileds are filled
     */
    public boolean ValidateTaskFields() {
        EditText editName = (EditText) findViewById(R.id.txtName);
        if (editName.getText().toString().trim().equals("")) {
            editName.setError("Enter the task name!");
            return false;
        } else if (deadlineDateView != null) {
            if (deadlineDateView.getText().toString().equals("")) {
                deadlineDateView.setError("Enter the deadline!");
                return false;
            }
        }
        return true;
    }

    /**
     * Creating task data and adding to local database
     * @return True if all data is successfully recorded to database
     */
    public boolean AddTaskToDatabase(){

        //creating new task and reading to it from fields
        TaskModel task = new TaskModel();

        EditText name = (EditText) findViewById(R.id.txtName);
        EditText description = (EditText) findViewById(R.id.txtDescription);
        EditText deadlineDate = (EditText) findViewById(R.id.txtDateDeadline);
        EditText deadlineTime = (EditText) findViewById(R.id.txtTimeDeadline);
        EditText startDate = (EditText) findViewById(R.id.txtDateStartTime);
        EditText startTime = (EditText) findViewById(R.id.txtTimeStartTime);
        Switch notifyStartTime = (Switch) findViewById(R.id.swtStartTimeNotification);
        Switch notifyDeadLine = (Switch) findViewById(R.id.swtDeadlineNotification);
        EditText duration = (EditText) findViewById(R.id.txtDuration);
        Calendar deadLineCal, startTimeCal;
        deadLineCal = getCalendarFromTxtEditViews(deadlineDate, deadlineTime);
        startTimeCal = getCalendarFromTxtEditViews(startDate, startTime);

        //compute duration in hours automatically
        /*long durationInHours = ( deadLineCal.getTimeInMillis() - startTimeCal.getTimeInMillis() ) / (1000*60*60);
        duration.setText((int) durationInHours);*/

        task.setName(name.getText().toString());
        task.setDeadline(deadLineCal);
        task.setDescription(description.getText().toString());
        task.setStartTime(startTimeCal);
        task.setIsNotifyStartTime(notifyStartTime.isChecked());
        task.setIsNotifyDeadline(notifyDeadLine.isChecked());
        if (duration.getText().toString().equals("")){
            task.setDuration(0L);
        } else
            task.setDuration(Long.parseLong(duration.getText().toString()));

        databaseHelper.addTask(task);
        return true;
    }

    private static Calendar getCalendarFromTxtEditViews(EditText dateView, EditText timeView){
        Calendar cal = null;
        try {
            java.util.Date date = dateFormat.parse(String.valueOf(dateView.getText().toString()));
            date.setTime(date.getTime() + timeFormat.parse(String.valueOf(timeView.getText())).getTime());
            cal = Calendar.getInstance();
            cal.setTime(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return cal;
    }

    /**
     * Set Calendar date to views.
     * @param dateTxt - date view.
     * @param timeTxt - time view.
     * @param cal - time, that will be set.
     */
    private static void setDateTime(EditText dateTxt, EditText timeTxt,Calendar cal){
        if(dateTxt != null) {
            dateTxt.setText(dateFormat.format(cal.getTime()));
        }
        if(timeTxt != null) {
            timeTxt.setText(timeFormat.format(cal.getTime()));
        }
    }

    public void fillData(){
        setDateTime(startTimeDateView, startTimeTimeView, Calendar.getInstance());
        setDateTime(deadlineDateView, deadlineTimeView, Calendar.getInstance());
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

    public static class TimePickerFragment extends DialogFragment
            implements TimePickerDialog.OnTimeSetListener {

        private EditText txtEdit = null;
        public void setTxtEdit(EditText t){
            txtEdit = t;
        }

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {

            int hour, minute;
            final Calendar curr = Calendar.getInstance();
            hour = curr.get(Calendar.HOUR_OF_DAY);
            minute = curr.get(Calendar.MINUTE);
            if (txtEdit != null) {
                Calendar c = null;
                try {
                    c = Calendar.getInstance();
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
            Calendar c = Calendar.getInstance();
            c.set(Calendar.HOUR_OF_DAY, hourOfDay);
            c.set(Calendar.MINUTE, minute);
            if(txtEdit != null) {
                AddTaskActivity.setDateTime(null, txtEdit, c);
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
            final Calendar curr = Calendar.getInstance();
            year = curr.get(Calendar.YEAR);
            month = curr.get(Calendar.MONTH);
            day = curr.get(Calendar.DAY_OF_MONTH);
            if (txtEdit != null) {
                Calendar c = null;
                try {
                    c = Calendar.getInstance();
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
            Calendar c = Calendar.getInstance();
            c.set(Calendar.YEAR, year);
            c.set(Calendar.MONTH, month);
            c.set(Calendar.DAY_OF_MONTH, day);
            if (txtEdit != null)
                AddTaskActivity.setDateTime(txtEdit,null,c);
        }
    }
}