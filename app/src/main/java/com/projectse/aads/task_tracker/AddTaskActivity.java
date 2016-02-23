package com.projectse.aads.task_tracker;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.format.DateFormat;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Switch;
import android.widget.TimePicker;
import android.widget.Toast;

import com.projectse.aads.task_tracker.DBService.DatabaseHelper;
import com.projectse.aads.task_tracker.Models.TaskModel;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

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

    private ListView subtasks_list = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addtask);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        databaseHelper = DatabaseHelper.getsInstance(this);

        String[] items = new String[]{"Medium", "High", "Low", "Flow", "Mowq", "Tow", "Row"};


        final StableArrayAdapter<String> adapter = new StableArrayAdapter<>(this,
                android.R.layout.simple_list_item_1, Arrays.asList(items));

        subtasks_list = (ListView) findViewById(R.id.listViewSubtasks);
        subtasks_list.setAdapter(adapter);
        subtasks_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, final View view,
                                    int position, long id) {
                final String item = (String) parent.getItemAtPosition(position);
                Toast.makeText(getApplicationContext(),
                        item,
                        Toast.LENGTH_SHORT).show();
            }

        });

        //fillData();
        /*Spinner dropdown = (Spinner) findViewById(R.id.spinner);
        String[] items = new String[]{"Medium", "High", "Low"};
        final ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_dropdown_item, items);
        dropdown.setAdapter(adapter);*/
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void getViews() {
        startTimeDateView = (EditText) findViewById(R.id.txtDateStartTime);
        startTimeTimeView = (EditText) findViewById(R.id.txtTimeStartTime);
        deadlineDateView = (EditText) findViewById(R.id.txtDateDeadline);
        deadlineTimeView = (EditText) findViewById(R.id.txtTimeDeadline);
    }

    /**
     * On-click button function for saving created task to database
     * and then going back to PlanActivity
     *
     * @param v
     */
    public void addAndSaveToDb(View v) {
        if (validateTaskFields()) {
            if (addTaskToDatabase()) {
                Intent intent = new Intent(this, PlanActivity.class);
                startActivity(intent);
            }
        }
    }

    /**
     * Validating new task properties
     *
     * @return True - if all required fileds are filled
     */
    public boolean validateTaskFields() {
        Toast toast = new Toast(getApplicationContext());
        toast.setGravity(Gravity.TOP | Gravity.LEFT, 0, 0);

        Calendar dCal = getCalendarFromTxtEditViews(deadlineDateView, deadlineTimeView);
        Calendar stCal = getCalendarFromTxtEditViews(startTimeDateView, startTimeTimeView);
        EditText durationView = (EditText) findViewById(R.id.txtDuration);
        EditText editName = (EditText) findViewById(R.id.txtName);

        if (editName.getText().toString().trim().equals("")) {
            toast.makeText(getApplicationContext(), "Enter the task name!", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (isNoSimilarTasks(editName.getText().toString())) {
            toast.makeText(getApplicationContext(), "Task with this name already exists!",
                    Toast.LENGTH_SHORT).show();
            return false;
        }

        if (deadlineDateView != null) {
            if (deadlineDateView.getText().toString().equals("")) {
                toast.makeText(getApplicationContext(), "Enter the deadline!",
                        Toast.LENGTH_SHORT).show();
                return false;
            }
            if (stCal != null && stCal.after(dCal)) {
                toast.makeText(getApplicationContext(), "Start date must be before deadline!",
                        Toast.LENGTH_SHORT).show();
                return false;
            }
        }

        //in case there is start time and deadline: duration must be less than deadline - start time
        if (stCal != null && !durationView.getText().toString().equals("") &&
                dCal.getTime().getTime() - stCal.getTime().getTime()
                        < Long.parseLong(durationView.getText().toString()) * 60 * 60 * 1000) {
            toast.makeText(getApplicationContext(),
                    "Duration can't be more than time between deadline and start time!",
                    Toast.LENGTH_SHORT).show();
            return false;
        }

        //in case there is no start time: duration must be less than deadline - current time
        if (stCal == null && !durationView.getText().toString().equals("") &&
                dCal.getTime().getTime() - Calendar.getInstance().getTime().getTime()
                        < Long.parseLong(durationView.getText().toString()) * 60 * 60 * 1000) {
            toast.makeText(getApplicationContext(),
                    "Duration can't be more than time between deadline and current time!",
                    Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    /**
     * Checks for two tasks with the same name
     *
     * @param newTaskName
     * @return
     */
    public boolean isNoSimilarTasks(String newTaskName) {
        List<TaskModel> list = databaseHelper.getTaskModelList();
        for (TaskModel task : list) {
            if (task.getName().equals(newTaskName))
                return true;
        }
        return false;
    }

    /**
     * Creating task data and adding to local database
     *
     * @return True if all data is successfully recorded to database
     */
    public boolean addTaskToDatabase() {

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

        if (startDate.getText().toString().equals("") && startTime.getText().toString().equals(""))
            startTimeCal = Calendar.getInstance();
        else
            startTimeCal = getCalendarFromTxtEditViews(startDate, startTime);

        task.setName(name.getText().toString());
        task.setDeadline(deadLineCal);
        task.setDescription(description.getText().toString());
        task.setStartTime(startTimeCal);
        task.setIsNotifyStartTime(notifyStartTime.isChecked());
        task.setIsNotifyDeadline(notifyDeadLine.isChecked());
        if (!duration.getText().toString().equals(""))
            task.setDuration(Long.parseLong(duration.getText().toString()));

        databaseHelper.addTask(task);
        return true;
    }

    private static Calendar getCalendarFromTxtEditViews(EditText dateView, EditText timeView) {
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
     *
     * @param dateTxt - date view.
     * @param timeTxt - time view.
     * @param cal     - time, that will be set.
     */
    private static void setDateTime(EditText dateTxt, EditText timeTxt, Calendar cal) {
        if (dateTxt != null) {
            dateTxt.setText(dateFormat.format(cal.getTime()));
        }
        if (timeTxt != null) {
            timeTxt.setText(timeFormat.format(cal.getTime()));
        }
    }

    public void fillData() {
        setDateTime(startTimeDateView, startTimeTimeView, Calendar.getInstance());
        setDateTime(deadlineDateView, deadlineTimeView, Calendar.getInstance());
    }

    public void showTimePickerDialog(View v) {
        DialogFragment newFragment = null;
        if (v == findViewById(R.id.btnTimeStartTime)) {
            newFragment = new TimePickerFragment();
            ((TimePickerFragment) newFragment).setTxtEdit(startTimeTimeView);
        }
        if (v == findViewById(R.id.btnTimeDeadline)) {
            newFragment = new TimePickerFragment();
            ((TimePickerFragment) newFragment).setTxtEdit(deadlineTimeView);
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
        if (v == findViewById(R.id.btnDateStartTime)) {
            newFragment = new DatePickerFragment();
            ((DatePickerFragment) newFragment).setTxtEdit(startTimeDateView);
        }
        if (v == findViewById(R.id.btnDateDeadline)) {
            newFragment = new DatePickerFragment();
            ((DatePickerFragment) newFragment).setTxtEdit(deadlineDateView);
        }
        if (newFragment == null)
            try {
                throw new Exception("Wrong view.");
            } catch (Exception e) {
                e.printStackTrace();
                return;
            }
        newFragment.show(getFragmentManager(), "datePicker");

        Calendar c = Calendar.getInstance();
        //setDateTime(null, startTimeTimeView, c);
        c.set(Calendar.HOUR_OF_DAY, 23);
        c.set(Calendar.MINUTE, 59);
        c.set(Calendar.SECOND, 59);
        setDateTime(null, deadlineTimeView, c);

    }

    public static class TimePickerFragment extends DialogFragment
            implements TimePickerDialog.OnTimeSetListener {

        private EditText txtEdit = null;

        public void setTxtEdit(EditText t) {
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
                if (c != null) {
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
            if (txtEdit != null) {
                AddTaskActivity.setDateTime(null, txtEdit, c);
            }
        }
    }

    public static class DatePickerFragment extends DialogFragment
            implements DatePickerDialog.OnDateSetListener {

        private EditText txtEdit = null;

        public void setTxtEdit(EditText t) {
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
                if (c != null) {
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
            if (txtEdit != null) {
                AddTaskActivity.setDateTime(txtEdit, null, c);
            }
        }
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    private class StableArrayAdapter<T extends Object> extends ArrayAdapter<T> {

        HashMap<T, Integer> mIdMap = new HashMap<T, Integer>();

        public StableArrayAdapter(Context context, int textViewResourceId,
                                  List<T> objects) {
            super(context, textViewResourceId, objects);
            for (int i = 0; i < objects.size(); ++i) {
                mIdMap.put(objects.get(i), i);
            }
        }

        @Override
        public long getItemId(int position) {
            T item = getItem(position);
            return mIdMap.get(item);
        }

        @Override
        public boolean hasStableIds() {
            return true;
        }

    }
}