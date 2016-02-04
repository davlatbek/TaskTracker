package com.projectse.aads.task_tracker;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.format.DateFormat;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TimePicker;

import com.projectse.aads.task_tracker.DBService.DatabaseHelper;
import com.projectse.aads.task_tracker.Models.TaskModel;

import java.sql.Date;
import java.sql.Time;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.TimeZone;

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

        databaseHelper = DatabaseHelper.getsInstance(this);

        Spinner dropdown = (Spinner) findViewById(R.id.spinner);
        String[] items = new String[]{"Medium", "High", "Low"};
        final ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_dropdown_item, items);
        dropdown.setAdapter(adapter);

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
        if (ValidateTaskData()) {
            AddTaskToDb();
            Intent intent = new Intent(this, PlanActivity.class);
            startActivity(intent);
        }
    }

    /**
     * Validating new task properties
     * @return True - if all required fileds are filled
     */
    public boolean ValidateTaskData() {
        EditText editName = (EditText) findViewById(R.id.txtName);
        if (editName.getText().toString().trim().equals("")) {
            editName.setError("Name is required!");
            return false;
        } else if (deadlineDateView != null) {
            if (deadlineDateView.getText().toString().equals("")) {
                deadlineDateView.setError("Deadline date is required!");
                return false;
            }
        }
        return true;
    }

    /**
     * Creating task data and adding to local database
     * @return True if all data is successfully recorded to database
     */
    public boolean AddTaskToDb(){

        //creating new task and reading to it from fields
        TaskModel task = new TaskModel();
        EditText name = (EditText) findViewById(R.id.txtName);

        EditText deadlineDate = (EditText) findViewById(R.id.txtDateDeadline);
        EditText startTimeDate = (EditText) findViewById(R.id.txtDateStartTime);
        Calendar deadLineCal = Calendar.getInstance();
        deadLineCal = getCalendarFromTxtEditViews(deadlineDate, startTimeDate);

        task.setName(name.toString());
        task.setDeadline(deadLineCal);
        databaseHelper.addTask(task);
        return true;
    }

    private static Calendar getCalendarFromTxtEditViews(EditText dateView, EditText timeView){
        Calendar cal = null;
        try {
            java.util.Date date = dateFormat.parse(String.valueOf(dateView.getText()));
            date.setTime(date.getTime() + timeFormat.parse(String.valueOf(timeView.getText())).getTime());
            cal = Calendar.getInstance();
            cal.setTime(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return cal;
    }

    @SuppressLint("ValidFragment")
    public static class TimePickerFragment extends DialogFragment
            implements TimePickerDialog.OnTimeSetListener {


        private EditText txtEdit = null;

        public TimePickerFragment(EditText t) {
            txtEdit = t;
        }

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            // Use the current time as the default values for the picker
            final Calendar c = Calendar.getInstance();
            int hour = c.get(Calendar.HOUR_OF_DAY);
            int minute = c.get(Calendar.MINUTE);

            // Create a new instance of TimePickerDialog and return it
            return new TimePickerDialog(getActivity(), this, hour, minute,
                    DateFormat.is24HourFormat(getActivity()));
        }

        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
            if (txtEdit != null) {
                txtEdit.setText((new Time((hourOfDay * 60 + minute) * 60 * 1000 - TimeZone.getDefault().getRawOffset())).toString());
            }
        }
    }

    @SuppressLint("ValidFragment")
    public static class DatePickerFragment extends DialogFragment
            implements DatePickerDialog.OnDateSetListener {

        private EditText txtEdit = null;

        public DatePickerFragment(EditText t) {
            txtEdit = t;
        }

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            // Use the current date as the default date in the picker
            final Calendar c = Calendar.getInstance();
            int year = c.get(Calendar.YEAR);
            int month = c.get(Calendar.MONTH);
            int day = c.get(Calendar.DAY_OF_MONTH);

            // Create a new instance of DatePickerDialog and return it
            return new DatePickerDialog(getActivity(), this, year, month, day);
        }

        public void onDateSet(DatePicker view, int year, int month, int day) {
            if (txtEdit != null) {
                txtEdit.setText((new Date((new GregorianCalendar(year, month, day)).getTimeInMillis()).toString()));
            }
        }
    }

    public void showTimePickerDialog(View v) {
        DialogFragment newFragment = null;
        if (v == findViewById(R.id.btnTimeStartTime))
            newFragment = new TimePickerFragment(startTimeTimeView);
        if (v == findViewById(R.id.btnTimeDeadline))
            newFragment = new TimePickerFragment(deadlineTimeView);
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
        if (v == findViewById(R.id.btnDateStartTime))
            newFragment = new DatePickerFragment(startTimeDateView);
        if (v == findViewById(R.id.btnDateDeadline))
            newFragment = new DatePickerFragment(deadlineDateView);
        if (newFragment == null)
            try {
                throw new Exception("Wrong view.");
            } catch (Exception e) {
                e.printStackTrace();
                return;
            }
        newFragment.show(getFragmentManager(), "datePicker");
    }
}
