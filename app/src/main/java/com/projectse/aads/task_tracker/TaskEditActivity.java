package com.projectse.aads.task_tracker;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.format.DateFormat;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TimePicker;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.projectse.aads.task_tracker.DBService.DatabaseHelper;
import com.projectse.aads.task_tracker.Models.TaskModel;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * Shows fields for editing current Task
 */

public class TaskEditActivity extends AppCompatActivity {

    // Views
    private EditText nameView;
    private EditText descView;
    private static EditText startTimeDateView;
    private static EditText startTimeTimeView;
    private static EditText deadlineDateView;
    private static EditText deadlineTimeView;
    private EditText durationView;
    private Switch isStartTimeNotifyView, isDeadlineNotifyView;

    private Button deleteButton;

    private ToggleButton isDoneView;

    // Current task
    private static TaskModel task = null;

    private static java.text.DateFormat dateFormat = new SimpleDateFormat("dd-MM-yy");
    private static java.text.DateFormat timeFormat = new SimpleDateFormat("HH:mm");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_edit);
//        getActionBar().setDisplayHomeAsUpEnabled(true);

        getViews();

        Long task_id = getIntent().getLongExtra("task_id",-1);

        DatabaseHelper db = DatabaseHelper.getsInstance(getApplicationContext());
        task = db.getTask(task_id);

        if (task != null) fillData();


        nameView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}

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
                if((s.toString()).matches("\\d+")) {
                    task.setDuration(Long.parseLong(s.toString()));
                }else{
                    Toast.makeText(getApplicationContext(), "Duration is number only" ,Toast.LENGTH_SHORT).show();
                }
            }
        });
        startTimeTimeView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
//                if(correctTime())
                correctTime();
                task.setStartTime(getCalendarFromTxtEditViews(startTimeDateView, startTimeTimeView));
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
//                if(correctTime())
                correctTime();
                    task.setStartTime(getCalendarFromTxtEditViews(startTimeDateView, startTimeTimeView));
            }
        });
        deadlineTimeView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
//                if(correctTime())
                correctTime();
                    task.setDeadline(getCalendarFromTxtEditViews(deadlineDateView, deadlineTimeView));
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
//                if(correctTime())
                correctTime();
                    task.setDeadline(getCalendarFromTxtEditViews(deadlineDateView, deadlineTimeView));
            }
        });

        isDoneView.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                task.setIsDone(isChecked);
            }
        });
        isStartTimeNotifyView.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                task.setIsNotifyStartTime(isChecked);
            }
        });
        isDeadlineNotifyView.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                task.setIsNotifyDeadline(isChecked);
            }
        });

        deleteButton = (Button) findViewById(R.id.deleteButton);
        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createDialog();
            }
        });
    }

    // flag for recursion exit. Use in correctTime only.
    private boolean flag = false;
    // return false, if all was correct
    public boolean correctTime(){
        Calendar dCal = getCalendarFromTxtEditViews(deadlineDateView,deadlineTimeView);
        Calendar stCal = getCalendarFromTxtEditViews(startTimeDateView,startTimeTimeView);
        boolean isCorrected = false;
        if(flag == true){flag = false; return false;}

        if(dCal == null && stCal == null)
            return false;
        // Case: deadline cannot be earlier than now.
        // Set default.
        if(Calendar.getInstance().after(dCal)){
            flag = true;
            dCal = Calendar.getInstance();
            dCal.set(Calendar.HOUR_OF_DAY,23);
            dCal.set(Calendar.MINUTE,59);
            dCal.set(Calendar.SECOND,59);
            setDateTime(deadlineDateView, null, dCal);
            setDateTime(null, deadlineTimeView, dCal);
            isCorrected = true;
        }
        // Case: if duration more than difference of deadline and startTime.
        // Don't allow this. Set duration 0.
        if(dCal.getTime().getTime() - stCal.getTime().getTime() < Long.parseLong(durationView.getText().toString())*60*60*1000){
            flag = true;
            Toast.makeText(getApplicationContext(), "Duration cannot be more than defference between start time and deadline.", Toast.LENGTH_SHORT).show();
            durationView.setText(String.valueOf(0));
            isCorrected = true;
        }
        // Case: start time after deadline.
        // Set starttime = deadline.
        if(stCal.after(dCal)){
            flag = true;
            setDateTime(startTimeDateView, null, dCal);
            setDateTime(null, startTimeTimeView, dCal);
            isCorrected = true;
        }
        return isCorrected;
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

    private void fillData() {

        isDoneView.setChecked(task.getIsDone());

        if (task.getName() != null ) nameView.setText(task.getName());
        if (task.getDescription() != null ) descView.setText(task.getDescription());

        if (task.getStartTime() != null ) {
            // TODO obsolete
//            startTimeDateView.setText((new Date(task.getStartTime().getTime())).toString());
//            startTimeTimeView.setText((new Time(task.getStartTime().getTime())).toString());

            setDateTime(startTimeDateView, startTimeTimeView, task.getStartTime());
        }

        isStartTimeNotifyView.setChecked(task.getIsNotifyStartTime());

        if (task.getDeadline() != null ) {
            // TODO obsolete
//            deadlineDateView.setText((new Date(task.getDeadline().getTime())).toString());
//            deadlineTimeView.setText((new Time(task.getDeadline().getTime())).toString());
            setDateTime(deadlineDateView,deadlineTimeView,task.getStartTime());
        }

        isDeadlineNotifyView.setChecked(task.getIsNotifyDeadline());

        if (task.getDuration() != null ) durationView.setText(task.getDuration().toString());
    }

    private void getViews(){
        nameView = (EditText) findViewById(R.id.txtName);
        descView = (EditText) findViewById(R.id.txtDescription);

        startTimeDateView = (EditText) findViewById(R.id.txtDateStartTime);
        startTimeTimeView = (EditText) findViewById(R.id.txtTimeStartTime);

        deadlineDateView = (EditText) findViewById(R.id.txtDateDeadline);
        deadlineTimeView = (EditText) findViewById(R.id.txtTimeDeadline);

        durationView = (EditText) findViewById(R.id.txtDuration);

        isStartTimeNotifyView = (Switch) findViewById(R.id.swtStartTimeNotification);
        isDeadlineNotifyView = (Switch) findViewById(R.id.swtDeadlineNotification);

        isDoneView = (ToggleButton) findViewById(R.id.btnIsDone);
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
                TaskEditActivity.setDateTime(null, txtEdit,c);
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
                TaskEditActivity.setDateTime(txtEdit,null,c);
        }
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

    @Override
    protected void onDestroy(){
        super.onDestroy();
        setResult(0);
        // write changes to base
        finish();
    }


    private void createDialog() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
        alertDialog.setMessage("Hi");

        alertDialog.setCancelable(false);
        alertDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //
            }
        }); {

        }
    }
}
