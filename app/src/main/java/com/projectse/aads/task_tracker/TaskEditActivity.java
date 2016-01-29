package com.projectse.aads.task_tracker;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.FragmentManager;
import android.app.TimePickerDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TimePicker;
import android.widget.ToggleButton;

import com.projectse.aads.task_tracker.Utils.Task;

import java.sql.Date;
import java.sql.Time;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.TimeZone;

/**
 * Shows fields for editing current Task
 */
public class TaskEditActivity extends AppCompatActivity {

    // Views
    private EditText nameView,descView,
            startTimeDateView,startTimeTimeView,
            deadlineDateView,deadlineTimeView,
            durationView
            ;
    private Switch isStartTimeNotifyView, isDeadlineNotifyView;

    private ToggleButton isDoneView;

    // Current task
    private static Task task = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_edit);
//        getActionBar().setDisplayHomeAsUpEnabled(true);

        getViews();

        task = (Task) (getIntent().getParcelableArrayExtra("task_object"))[0];

        if (task != null){
            fillData();
        }

    }

    private void fillData() {

        isDoneView.setChecked(task.getIsDone());

        if (task.getName() != null ) nameView.setText(task.getName());
        if (task.getDescription() != null ) descView.setText(task.getDescription());

        if (task.getStartTime() != null ) {
            startTimeDateView.setText((new Date(task.getStartTime().getTime())).toString());
            startTimeTimeView.setText((new Time(task.getStartTime().getTime())).toString());
        }

        isStartTimeNotifyView.setChecked(task.getIsNotifyStartTime());

        if (task.getDeadline() != null ) {
            deadlineDateView.setText((new Date(task.getDeadline().getTime())).toString());
            deadlineTimeView.setText((new Time(task.getDeadline().getTime())).toString());
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
        public TimePickerFragment(EditText t){ txtEdit = t; }

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
            if(txtEdit != null) {
                txtEdit.setText((new Time((hourOfDay * 60 + minute) * 60 * 1000 - TimeZone.getDefault().getRawOffset())).toString());
            }
        }
    }

    public static class DatePickerFragment extends DialogFragment
            implements DatePickerDialog.OnDateSetListener {

        private EditText txtEdit = null;
        public DatePickerFragment(EditText t){ txtEdit = t; }

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
            if(txtEdit != null) {
                txtEdit.setText((new Date((new GregorianCalendar(year,month,day)).getTimeInMillis()).toString()));
            }
        }
    }

    public void showTimePickerDialog(View v) {
        DialogFragment newFragment = null;
        if(v == findViewById(R.id.btnTimeStartTime))
            newFragment = new TimePickerFragment(startTimeTimeView);
        if(v == findViewById(R.id.btnTimeDeadline))
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
        if(v == findViewById(R.id.btnDateStartTime))
            newFragment = new DatePickerFragment(startTimeDateView);
        if(v == findViewById(R.id.btnDateDeadline))
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
