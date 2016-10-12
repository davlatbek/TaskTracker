package com.projectse.aads.task_tracker.Fragments;

import android.annotation.TargetApi;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.Fragment;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.projectse.aads.task_tracker.Adapters.SubtasksAdapter;
import com.projectse.aads.task_tracker.DBService.DatabaseHelper;
import com.projectse.aads.task_tracker.Dialogs.ListOfCourses;
import com.projectse.aads.task_tracker.Models.CourseModel;
import com.projectse.aads.task_tracker.Models.TaskModel;
import com.projectse.aads.task_tracker.R;
import com.projectse.aads.task_tracker.RequestCode;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

/**
 * Created by Davlatbek Isroilov on 4/3/2016.
 * Innopolis University
 */
public abstract class TaskFragment extends Fragment {
    // Views
    protected Button priorityColor;
    protected Spinner spinnerPriority;
    protected Switch switchDone;
    protected Switch timerOn;
    protected EditText nameView;
    protected EditText descView;
    protected TextView timeView;
    protected TextView textViewCourseLabel;
    protected ImageButton buttonCourseSelect;
    protected EditText editTextCourseName;
    protected static EditText startTimeDateView;
    protected static EditText deadlineDateView;
    protected EditText durationView;
    protected TextView courseView;

    protected Button buttonDateDeadline, buttonDateStartTime, addSubtasks;

    protected static DatabaseHelper db;
    protected ListView subtasksListView = null;
    //protected static SubtasksAdapter<TaskModel> subtasks_adapter = null;
    //Temporary
    protected static ArrayAdapter<TaskModel> subtasks_adapter = null;

    protected static java.text.DateFormat dateFormat = new SimpleDateFormat("dd-MM-yy");

    protected ListOfCourses dialogFragmentBuilder;

    // Current task
    protected static TaskModel task = null;
    protected static CourseModel course = null;

    protected List<TaskModel> listAllSubtasks= new ArrayList<>();
    protected List<TaskModel> listNewSubtasks = new ArrayList<>();

    protected Handler timerHandler = new Handler();
    protected Runnable timerRunnable = new Runnable() {

        @Override
        public void run() {
            setTimeView();
            timerHandler.postDelayed(this, 1000);
        }
    };

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.shared_content_task_new, container, false);
        db = DatabaseHelper.getsInstance(getActivity());
        TimeZone timeZone = TimeZone.getTimeZone("UTC");
        dateFormat.setTimeZone(timeZone);
        return view;
    }

    @Override
    public void onPause() {
        super.onPause();
        timerHandler.removeCallbacks(timerRunnable);
        if(task.getRunning()) {
            updateTimer();
            task.setLastSessionStart(Calendar.getInstance().getTimeInMillis());
        }

        db.updateTask(task);
    }

    @Override
    public void onResume() {
        super.onResume();
        if(task.getRunning()) {
            timerHandler.postDelayed(timerRunnable, 0);
        }
    }

    //this method called by fragment onCreateView
    protected void getViews(View view) {
        priorityColor = (Button) view.findViewById(R.id.btnPriorityColor);
        spinnerPriority = (Spinner) view.findViewById(R.id.spinnerPriority);
        setPrioritySpinner(view);
        switchDone = (Switch) view.findViewById(R.id.doneSwitch);
        timerOn = (Switch) view.findViewById(R.id.timer);

        nameView = (EditText) view.findViewById(R.id.txtName);
        descView = (EditText) view.findViewById(R.id.txtDescription);
        timeView = (TextView) view.findViewById(R.id.timeSpent);
        textViewCourseLabel = (TextView) view.findViewById(R.id.textViewCourseLabel);
        buttonCourseSelect = (ImageButton) view.findViewById(R.id.buttonCourseSelect);
        editTextCourseName = (EditText) view.findViewById(R.id.editTextCourseName);

        startTimeDateView = (EditText) view.findViewById(R.id.txtDateStartTime);
        buttonDateStartTime = (Button) view.findViewById(R.id.btnDateStartTime);
        buttonDateStartTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePickerDialog(v);
            }
        });
        deadlineDateView = (EditText) view.findViewById(R.id.txtDateDeadline);
        buttonDateDeadline = (Button) view.findViewById(R.id.btnDateDeadline);
        buttonDateDeadline.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePickerDialog(v);
            }
        });
        durationView = (EditText) view.findViewById(R.id.txtDuration);

        addSubtasks = (Button) view.findViewById(R.id.btnAddSubtask);
        addSubtasks.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callAddSubtaskDialog(v);
            }
        });
        subtasksListView = (ListView) view.findViewById(R.id.listViewSubtasks);
        subtasks_adapter = new ArrayAdapter<TaskModel>(getActivity(),
                android.R.layout.simple_list_item_1, listAllSubtasks);
        subtasksListView.setAdapter(subtasks_adapter);

        courseView = (TextView) view.findViewById(R.id.coursename);
        db = DatabaseHelper.getsInstance(getActivity());
        dialogFragmentBuilder = new ListOfCourses(getActivity(), db);
        setTimerOnChechChangedListener();
    }

    private void setTimerOnChechChangedListener(){
        timerOn.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                task.setRunning(isChecked);
                if (isChecked) {
                    task.setLastSessionStart(Calendar.getInstance().getTimeInMillis());
                    timerHandler.postDelayed(timerRunnable, 0);
                }

                if(!isChecked && task.getLastSessionStart() != 0) {
                    timerHandler.removeCallbacks(timerRunnable);
                    updateTimer();
                }
            }
        });
    }

    protected void fillData(long course_id) throws Exception {
        try {
            spinnerPriority.setSelection(task.priorityToInt(task.getPriority()));
            switch (task.getPriority()){
                case LOW:
                    priorityColor.setBackgroundColor(getResources().getColor(R.color.lowPriority));
                    break;
                case MEDIUM:
                    priorityColor.setBackgroundColor(getResources().getColor(R.color.mediumPriority));
                    break;
                case HIGH:
                    priorityColor.setBackgroundColor(getResources().getColor(R.color.hignPriority));
                    break;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        switchDone.setChecked(task.getIsDone());
        //removing the listener and then adding it back because when setting the value here
        //we don't want the code from the listener to be executed because the switch will
        //not be triggered by the user but by us
        timerOn.setOnCheckedChangeListener(null);
        timerOn.setChecked(task.getRunning());
        setTimerOnChechChangedListener();
        if(task.getRunning()){
            timerHandler.postDelayed(timerRunnable, 0);
        }

        if (task.getName() != null) nameView.setText(task.getName());
        if (task.getDescription() != null) descView.setText(task.getDescription());
        if (course_id != 0){
            textViewCourseLabel.setText(db.getCourse(course_id).getAbbreviation());
            textViewCourseLabel.setBackgroundColor(db.getCourse(course_id).getClr());
            editTextCourseName.setText(db.getCourse(course_id).getName());
        }
        if (task.getStartTime() != null) {
            setDateTime(startTimeDateView, task.getStartTime().getTimeInMillis());
        }
        if (task.getDeadline() != null) {
            setDateTime(deadlineDateView, task.getDeadline().getTimeInMillis());
        }
        if (task.getDuration() != null)
            durationView.setText(task.getDuration().toString());

        fillSubtasks();
        setTimeView();
    }

    protected void setTimeView(){
        String time;
        long milis = task.getTimeSpentMs();
        if(task.getRunning()){
            long timeTrackedCurrentlyInMs = Calendar.getInstance().getTimeInMillis() - task.getLastSessionStart();
            milis += timeTrackedCurrentlyInMs;
        }

        Long seconds = milis / 1000;
        Long minutes = seconds / 60;
        Long hours = minutes / 60;
        Long days = hours / 24;
        String s = (seconds %= 60).toString();
        if (s.length() == 1) {
            s = "0" + s;
        }
        String m = (minutes %= 60).toString();
        if (m.length() == 1) {
            m = "0" + m;
        }
        String h = (hours %= 24).toString();
        if (days > 0) {
            time = days + "d " + hours + "h";
        } else {
            time = h + ":" + m + ":" + s;
        }

        timeView.setText(time);
    }

    protected void updateTimer(){
        Long timeSpent = task.getTimeSpentMs();
        Long tStart = task.getLastSessionStart();
        Long tFinish = Calendar.getInstance().getTimeInMillis();
        timeSpent += tFinish - tStart;
        task.setTimeSpentMs(timeSpent);
    }

    /**
     * Checks for two tasks with the same name
     *
     * @param newTaskName
     * @return
     */
    public boolean isNoSimilarTasks(String newTaskName, long course_id) {
        List<TaskModel> list = db.getListOfTasks(course_id);
        for (TaskModel task : list) {
            if (task.getName().equals(newTaskName))
                return false;
        }
        return true;
    }

    /**
     * Validating new task properties
     *
     * @return True - if all required fileds are filled
     */
    public boolean validateTaskFields(View view) {
        Toast toast = new Toast(getActivity());
        toast.setGravity(Gravity.TOP | Gravity.LEFT, 0, 0);

        Calendar dCal = getCalendarFromTxtEditViews(deadlineDateView);
        Calendar stCal = getCalendarFromTxtEditViews(startTimeDateView);
        EditText durationView = (EditText) view.findViewById(R.id.txtDuration);
        EditText editName = (EditText) view.findViewById(R.id.txtName);

        if (editName.getText().toString().trim().equals("")) {
            Toast.makeText(getActivity(), "Enter the task name!", Toast.LENGTH_SHORT).show();
            return false;
        }

        //course id by course name method needed
        /*if (!isNoSimilarTasks(editName.getText().toString(), db.getCourseIdByTaskId(task.getId()))) {
            Toast.makeText(getActivity(), "Task with this name already exists!",
                    Toast.LENGTH_SHORT).show();
            return false;
        }*/

        if (deadlineDateView != null) {
            if (deadlineDateView.getText().toString().equals("")) {
                Toast.makeText(getActivity(), "Enter the deadline!",
                        Toast.LENGTH_SHORT).show();
                return false;
            }
            if (stCal != null && stCal.after(dCal)) {
                Toast.makeText(getActivity(), "Start date must be before deadline!",
                        Toast.LENGTH_SHORT).show();
                return false;
            }
        }

        //in case there is start time and deadline: duration must be less than deadline - start time
        if (stCal != null && !durationView.getText().toString().equals("") &&
                dCal.getTime().getTime() - stCal.getTime().getTime()
                        < Long.parseLong(durationView.getText().toString()) * 60 * 60 * 1000) {
            Toast.makeText(getActivity(),
                    "Duration can't be more than time between deadline and start time!",
                    Toast.LENGTH_SHORT).show();
            return false;
        }

        //in case there is no start time: duration must be less than deadline - current time
        if (stCal == null && !durationView.getText().toString().equals("") &&
                dCal.getTime().getTime() - Calendar.getInstance(TimeZone.getTimeZone("UTC"),
                        Locale.getDefault()).getTime().getTime() <
                        Long.parseLong(durationView.getText().toString()) * 60 * 60 * 1000) {
            Toast.makeText(getActivity().getApplicationContext(),
                    "Duration can't be more than time between deadline and current time!",
                    Toast.LENGTH_SHORT).show();
            return false;
        }

        //if startDate is not set, then startDate = deadlineDate - Settings.ISSSTD
        if (stCal == null){
            int startDateDefaultDays = db.getSettings().getINSSSD();
            Calendar startDateDefault = dCal;
            startDateDefault.add(Calendar.DAY_OF_YEAR, -startDateDefaultDays);
            stCal = startDateDefault;
            setDateTime(startTimeDateView, stCal.getTimeInMillis());
        }

        return true;
    }

    public void setPrioritySpinner(View view) {
        Spinner spinnerPriority = (Spinner) view.findViewById(R.id.spinnerPriority);
        final String[] priorities = new String[]{"Low", "Medium", "High"};
        ArrayAdapter<String> adapterPrioritySpinner =
                new ArrayAdapter<String>(getActivity(),
                        android.R.layout.simple_spinner_dropdown_item, priorities);
        spinnerPriority.setAdapter(adapterPrioritySpinner);

        spinnerPriority.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @TargetApi(Build.VERSION_CODES.M)
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Log.d("PRIORITY", "Priority: " + position);
                switch (position) {
                    case 0:
                        task.setPriority(TaskModel.Priority.LOW);
                        //priorityColor.setForeground(new ColorDrawable(0x4daf51));
                        priorityColor.setBackgroundColor(getResources().getColor(R.color.lowPriority));
                        break;
                    case 1:
                        task.setPriority(TaskModel.Priority.MEDIUM);
                        //priorityColor.setForeground(new ColorDrawable(Color.YELLOW));
                        priorityColor.setBackgroundColor(getResources().getColor(R.color.mediumPriority));
                        break;
                    case 2:
                        task.setPriority(TaskModel.Priority.HIGH);
                        //priorityColor.setForeground(new ColorDrawable(Color.RED));
                        priorityColor.setBackgroundColor(getResources().getColor(R.color.hignPriority));
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }

    protected static Calendar getCalendarFromTxtEditViews(EditText dateView) {
        Calendar cal = null;
        try {
            java.util.Date date = dateFormat.parse(String.valueOf(dateView.getText()));
            cal = Calendar.getInstance(TimeZone.getTimeZone("UTC"), Locale.getDefault());
            cal.setTime(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return cal;
    }

    public void showDatePickerDialog(View v) {
        DialogFragment newFragment = null;
        if (v == getActivity().findViewById(R.id.btnDateStartTime)) {
            newFragment = new DatePickerFragment();
            ((DatePickerFragment) newFragment).setTxtEdit(startTimeDateView);
        }
        if (v == getActivity().findViewById(R.id.btnDateDeadline)) {
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
    }

    /**
     * Set Calendar date to views.
     *
     * @param dateTxt     - date view.
     * @param calInMillis - time, that will be set.
     */
    protected static void setDateTime(EditText dateTxt, long calInMillis) {
        Calendar time = Calendar.getInstance(TimeZone.getTimeZone("UTC"), Locale.getDefault());
        time.setTimeInMillis(calInMillis);
        if (dateTxt != null) {
            dateTxt.setText(dateFormat.format(time.getTime()));
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
            Calendar c = Calendar.getInstance(TimeZone.getTimeZone("UTC"), Locale.getDefault());
            c.set(Calendar.YEAR, year);
            c.set(Calendar.MONTH, month);
            c.set(Calendar.DAY_OF_MONTH, day);
            if (txtEdit != null)
                setDateTime(txtEdit, c.getTimeInMillis());
        }
    }

    /****************
     *SUBTASKS PART*
     ****************/

    public void callAddSubtaskDialog(View view){
        AddSubtaskDialogFragment dialogFragment = new AddSubtaskDialogFragment();
        dialogFragment.setTargetFragment(this, RequestCode.REQ_CODE_ADDTASK);
        dialogFragment.show(getFragmentManager(), "sas");
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RequestCode.REQ_CODE_ADDTASK){
            String subtaskDetailsString = data.getStringExtra("subtask_details");
            TaskModel taskModel = new TaskModel();
            taskModel.setName(subtaskDetailsString);
            taskModel.setDeadline(Calendar.getInstance());
            listNewSubtasks.add(taskModel);
            listAllSubtasks.add(taskModel);
            subtasks_adapter.notifyDataSetChanged();
        }
        else if (requestCode == RequestCode.REQ_CODE_VIEWTASK) {
            Long subtaskId = data.getLongExtra("subtask_id", -1);
            String subtaskName= data.getStringExtra("subtask_name");
            Boolean subtaskIsDone = data.getBooleanExtra("subtask_isdone", true);
            TaskModel taskModel = db.getTask(subtaskId);
            taskModel.setName(subtaskName);
            taskModel.setIsDone(subtaskIsDone);
            db.updateTask(taskModel);
            fillSubtasks();
        }
    }

    public void addSubtask(TaskModel subtask){
        SubtasksAdapter<TaskModel> adapter = (SubtasksAdapter<TaskModel>) subtasksListView.getAdapter();
        if( task == null || adapter == null)
            return;
        task.addSubtask(subtask);
        db.updateTask(task);
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

    private boolean isEmptyListSet = false;

    public void fillSubtasks() {
        //First, get all subtasks for current task
        listAllSubtasks.clear();
        List<Long> subtasks_ids = task.getSubtasks_ids();
        for(Long id : subtasks_ids) {
            db = DatabaseHelper.getsInstance(getActivity());
            TaskModel subtask = db.getTask(id);
            if(subtask == null)
                return;
            listAllSubtasks.add(subtask);
        }
        subtasks_adapter.notifyDataSetChanged();

        subtasksListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, final View view,
                                    int position, long id) {
                final TaskModel task = (TaskModel) parent.getItemAtPosition(position);
                Bundle args = new Bundle();
                args.putLong("subtask_id", task.getId());

                AddSubtaskDialogFragment dialogFragment = new AddSubtaskDialogFragment();
                dialogFragment.setArguments(args);
                dialogFragment.setTargetFragment(TaskFragment.this, RequestCode.REQ_CODE_VIEWTASK);
                dialogFragment.show(getFragmentManager(), "sas");
            }
        });
    }

    public void onClickCourseList(View view) {
        dialogFragmentBuilder.show(getFragmentManager(), "selectcourse");
    }
}