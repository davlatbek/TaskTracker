package com.projectse.aads.task_tracker.Fragments;

import android.annotation.TargetApi;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.Fragment;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
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
import com.projectse.aads.task_tracker.MainActivity;
import com.projectse.aads.task_tracker.Models.CourseModel;
import com.projectse.aads.task_tracker.Models.TaskModel;
import com.projectse.aads.task_tracker.R;
import com.projectse.aads.task_tracker.RequestCode;
import com.projectse.aads.task_tracker.TaskActivity;

import java.lang.reflect.Array;
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
    protected EditText nameView;
    protected EditText descView;
    protected TextView textViewCourseLabel;
    protected ImageButton buttonCourseSelect;
    protected EditText editTextCourseName;
    protected static EditText startTimeDateView;
    protected static EditText deadlineDateView;
    protected EditText durationView;
    protected TextView courseView;

    protected Button buttonDateDeadline, buttonDateStartTime, addSubtasks, clearSubtasks;

    protected static DatabaseHelper db;
    protected ListView subtasksListView = null;
    //protected static SubtasksAdapter<TaskModel> subtasks_adapter = null;
    //Temporary
    protected static ArrayAdapter<TaskModel> subtasks_adapter = null;

    private static java.text.DateFormat dateFormat = new SimpleDateFormat("dd-MM-yy");

    protected ListOfCourses dialogFragmentBuilder;

    // Current task
    protected static TaskModel task = null;
    protected static CourseModel course = null;

    protected List<TaskModel> listAllSubtasks= new ArrayList<>();
    protected List<TaskModel> listNewSubtasks = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.shared_content_task_new, container, false);
        db = DatabaseHelper.getsInstance(getActivity().getApplicationContext());
        TimeZone timeZone = TimeZone.getTimeZone("UTC");
        dateFormat.setTimeZone(timeZone);
        return view;
    }

    protected void getViews(View view) {
        priorityColor = (Button) view.findViewById(R.id.btnPriorityColor);
        spinnerPriority = (Spinner) view.findViewById(R.id.spinnerPriority);
        setPrioritySpinner(view);
        switchDone = (Switch) view.findViewById(R.id.doneSwitch);

        nameView = (EditText) view.findViewById(R.id.txtName);
        descView = (EditText) view.findViewById(R.id.txtDescription);
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
        clearSubtasks = (Button) view.findViewById(R.id.btnClearSubtasks);
        subtasksListView = (ListView) view.findViewById(R.id.listViewSubtasks);
        subtasks_adapter = new ArrayAdapter<TaskModel>(getActivity(),
                android.R.layout.simple_list_item_1, listAllSubtasks);
        subtasksListView.setAdapter(subtasks_adapter);

        courseView = (TextView) view.findViewById(R.id.coursename);
        //dialogFragmentBuilder = new ListOfCourses(getActivity(), db);
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
        if (task.getName() != null) nameView.setText(task.getName());
        if (task.getDescription() != null) descView.setText(task.getDescription());
        textViewCourseLabel.setText(db.getCourse(course_id).getAbbreviation());
        textViewCourseLabel.setBackgroundColor(db.getCourse(course_id).getClr());
        editTextCourseName.setText(db.getCourse(course_id).getName());
        if (task.getStartTime() != null) {
            setDateTime(startTimeDateView, task.getStartTime().getTimeInMillis());
        }
        if (task.getDeadline() != null) {
            setDateTime(deadlineDateView, task.getDeadline().getTimeInMillis());
        }
        if (task.getDuration() != null)
            durationView.setText(task.getDuration().toString());
        fillSubtasks();
    }

    /**
     * Checks for two tasks with the same name
     *
     * @param newTaskName
     * @return
     */
    public boolean isNoSimilarTasks(String newTaskName, CourseModel course) {
        List<TaskModel> list = db.getListOfTasks(course.getId());
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
        Toast toast = new Toast(getActivity().getApplicationContext());
        toast.setGravity(Gravity.TOP | Gravity.LEFT, 0, 0);

        Calendar dCal = getCalendarFromTxtEditViews(deadlineDateView);
        Calendar stCal = getCalendarFromTxtEditViews(startTimeDateView);
        EditText durationView = (EditText) view.findViewById(R.id.txtDuration);
        EditText editName = (EditText) view.findViewById(R.id.txtName);

        if (editName.getText().toString().trim().equals("")) {
            toast.makeText(getActivity().getApplicationContext(), "Enter the task name!", Toast.LENGTH_SHORT).show();
            return false;
        }

        //  !NEED a getCourseIdByCourseName(String courseName) METHOD HERE
        /*if (!isNoSimilarTasks(editName.getText().toString(), getCourseIdByCourseName(editTextCourseName.getText().toString()))) {
            toast.makeText(getActivity().getApplicationContext(), "Task with this name already exists!",
                    Toast.LENGTH_SHORT).show();
            return false;
        }*/

        if (deadlineDateView != null) {
            if (deadlineDateView.getText().toString().equals("")) {
                toast.makeText(getActivity().getApplicationContext(), "Enter the deadline!",
                        Toast.LENGTH_SHORT).show();
                return false;
            }
            if (stCal != null && stCal.after(dCal)) {
                toast.makeText(getActivity().getApplicationContext(), "Start date must be before deadline!",
                        Toast.LENGTH_SHORT).show();
                return false;
            }
        }

        //in case there is start time and deadline: duration must be less than deadline - start time
        if (stCal != null && !durationView.getText().toString().equals("") &&
                dCal.getTime().getTime() - stCal.getTime().getTime()
                        < Long.parseLong(durationView.getText().toString()) * 60 * 60 * 1000) {
            toast.makeText(getActivity().getApplicationContext(),
                    "Duration can't be more than time between deadline and start time!",
                    Toast.LENGTH_SHORT).show();
            return false;
        }

        //in case there is no start time: duration must be less than deadline - current time
        if (stCal == null && !durationView.getText().toString().equals("") &&
                dCal.getTime().getTime() - Calendar.getInstance(TimeZone.getTimeZone("UTC"), Locale.getDefault()).getTime().getTime()
                        < Long.parseLong(durationView.getText().toString()) * 60 * 60 * 1000) {
            toast.makeText(getActivity().getApplicationContext(),
                    "Duration can't be more than time between deadline and current time!",
                    Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    public void setPrioritySpinner(View view) {
        Spinner spinnerPriority = (Spinner) view.findViewById(R.id.spinnerPriority);
        final String[] priorities = new String[]{"Low", "Medium", "High"};
        ArrayAdapter<String> adapterPrioritySpinner =
                new ArrayAdapter<String>(getActivity().getApplicationContext(),
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

    private  void fillSubtasksList() {
        if(db == null)
            return;
        listAllSubtasks.clear();
        for(Long id : task.getSubtasks_ids()){
            TaskModel subtask = db.getTask(id);
            if(subtask == null)
                return;
            listAllSubtasks.add(subtask);
        }
    }

    private boolean isEmptyListSet = false;

    public void fillSubtasks() {
        //First, get all subtasks for current task
        List<Long> subtasks_ids = task.getSubtasks_ids();
        for(Long id : subtasks_ids) {
            db = DatabaseHelper.getsInstance(getActivity());
            TaskModel subtask = db.getTask(id);
            if(subtask == null)
                return;
            listAllSubtasks.add(subtask);
        }
        subtasks_adapter.notifyDataSetChanged();

        //Second, get and add new subtasks to the AllSubtasksList to show

        /*if(!isEmptyListSet){
            TextView emptyList = new TextView(getActivity());
            emptyList.setText("The list of subtasks is empty");
            subtasksListView.setEmptyView(emptyList);
            ((ViewGroup) subtasksListView.getParent()).addView(emptyList);
            emptyList.setTextSize(25);
            emptyList.setGravity(Gravity.CENTER);
            emptyList.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.MATCH_PARENT));
            isEmptyListSet = true;
        }*/

        /*subtasksListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, final View view,
                                    int position, long id) {
                final TaskModel item = (TaskModel) parent.getItemAtPosition(position);
                //callTaskOverviewActivity(item);
            }
        });*/
    }
}