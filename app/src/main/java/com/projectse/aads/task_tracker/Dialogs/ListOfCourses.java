package com.projectse.aads.task_tracker.Dialogs;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.larswerkman.lobsterpicker.sliders.LobsterShadeSlider;
import com.projectse.aads.task_tracker.AddTaskActivity;
import com.projectse.aads.task_tracker.DBService.DatabaseHelper;
import com.projectse.aads.task_tracker.Models.CourseModel;
import com.projectse.aads.task_tracker.R;

import java.util.List;

/**
 * Created by Andrey Zolin on 18.02.2016.
 */
public class ListOfCourses extends DialogFragment implements DialogInterface.OnClickListener {
    final String TAG = "TAG";
    private AddTaskActivity testActivity;
    final CourseModel course = new CourseModel();
    private DatabaseHelper db;


    public ListOfCourses(AddTaskActivity testActivity, DatabaseHelper db) {
        this.db = db;
        this.testActivity = testActivity;
    }

    public Dialog onCreateDialog(Bundle savedInstanceState) {


        List<CourseModel> courseModelList = null;
        try {
            courseModelList = db.getCourseModelList();
        } catch (Exception e) {
            e.printStackTrace();
        }

        ArrayAdapter<CourseModel> adapter = new ArrayAdapter<CourseModel>(getActivity(), android.R.layout.simple_list_item_1, courseModelList);

        final List<CourseModel> finalCourseModelList = courseModelList;
        final AlertDialog.Builder adb = new AlertDialog.Builder(getActivity())
                .setTitle("Courses")
                .setSingleChoiceItems(adapter, -1, new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        TextView t = (TextView) testActivity.findViewById(R.id.textSelectedCourse);


                        Toast.makeText(
                                testActivity,
                                "Selected course: "
                                        + finalCourseModelList.get(which),
                                Toast.LENGTH_SHORT).show();
                        t.setText("Selected course: "
                                + finalCourseModelList.get(which));

                        dismiss();
                    }
                })
                .setPositiveButton("Add new course", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        LayoutInflater inflater = getActivity().getLayoutInflater();
                        View inflate = inflater.inflate(R.layout.add_new_course_form, null);
                        final EditText courseName = (EditText) inflate.findViewById(R.id.coursename);


                        final LobsterShadeSlider shadeSlider = (LobsterShadeSlider) inflate.findViewById(R.id.shadeslider);

                        AlertDialog.Builder addnewcourse = new AlertDialog.Builder(getActivity())
                                .setTitle("Add new course")
                                .setView(inflate)
                                .setPositiveButton("Create", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        // Get name from field
                                        course.setName(courseName.getText().toString());
                                        // Get color from slider
                                        Integer intColor = shadeSlider.getColor();
                                        String hexColor = "#" + Integer.toHexString(intColor).substring(2);
                                        int color = Integer.parseInt(hexColor.replaceFirst("^#",""), 16);
                                        Log.d(TAG,color+"<-<-<-<-CHOOSED COLOR");
                                        course.setClr(color);
                                        long id = db.addCourse(course);
                                        Log.d(TAG, id + "");
                                    }

                                })
                                .setNegativeButton("Cancel",
                                        new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int id) {
                                                dialog.cancel();
                                            }
                                        });
                        addnewcourse.show();

                    }

                });
        return adb.create();
    }


    @Override
    public void onClick(DialogInterface dialog, int which) {

    }

    public void onDismiss(DialogInterface dialog) {
        super.onDismiss(dialog);
        Log.d(TAG, "onDissmiss");
    }

    public void onCancel(DialogInterface dialog) {
        super.onCancel(dialog);
        Log.d(TAG, "onCancel");
    }
}