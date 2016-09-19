package com.projectse.aads.task_tracker.Dialogs;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.TextView;

import com.larswerkman.lobsterpicker.sliders.LobsterShadeSlider;
import com.projectse.aads.task_tracker.DBService.DatabaseHelper;
import com.projectse.aads.task_tracker.Models.CourseModel;
import com.projectse.aads.task_tracker.R;

import java.util.List;

/**
 * Created by Andrey Zolin on 18.02.2016.
 */
public class ListOfCourses extends DialogFragment implements DialogInterface.OnClickListener {
    final String TAG = "TAG";
    final CourseModel course = new CourseModel();
    private Activity testActivity;
    private DatabaseHelper db;
    private long courseID = 0;
    AlertDialog.Builder adb;

    public ListOfCourses(Activity testActivity, DatabaseHelper db) {
        this.db = db;
        this.testActivity = testActivity;
    }

    public long getCourseId() {
        return this.courseID;
    }

    private void setCourseId(long course_id) {
        this.courseID = course_id;
    }

    public void reloadFragment() {
        adb.create();
        adb.show();
    }

    public void setCurrentFragment(Fragment fragment) {
        FragmentManager fragmentManager = getFragmentManager();
//        fragmentManager.beginTransaction().replace(R.id.flContent, fragment).commit();
        fragmentManager.beginTransaction().add(fragment, "List").commit();
        fragmentManager.executePendingTransactions();
    }


    public Dialog onCreateDialog(Bundle savedInstanceState) {


        List<CourseModel> courseModelList = null;
        try {
            courseModelList = db.getCourseModelList();
        } catch (Exception e) {
            e.printStackTrace();
        }

        ArrayAdapter<CourseModel> adapter = new ArrayAdapter<CourseModel>(getActivity(),
                android.R.layout.simple_list_item_1, courseModelList);

        final List<CourseModel> finalCourseModelList = courseModelList;
        /*final AlertDialog.Builder */adb = new AlertDialog.Builder(getActivity())
                .setTitle("Courses")
                .setSingleChoiceItems(adapter, -1, new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        EditText courseName = (EditText) testActivity.findViewById(R.id.editTextCourseName);
                        TextView textViewCourseLabel = (TextView) testActivity.findViewById(R.id.textViewCourseLabel);
                        courseName.setText(finalCourseModelList.get(which).getName());
                        textViewCourseLabel.setText(finalCourseModelList.get(which).getAbbreviation());
                        textViewCourseLabel.setBackgroundColor(finalCourseModelList.get(which).getClr());
                        int p = finalCourseModelList.get(which).getClr();
                        Log.d("COLOR", p + "");
                        switch (finalCourseModelList.get(which).getClr()) {
                            case 7617718:
                                int parsedColor = Color.parseColor(String.valueOf(getResources().getColor(R.color.coursecolor1)));
                                courseName.setBackgroundColor(parsedColor);
                                break;
                            case 16728876:
                                parsedColor = Color.parseColor(String.valueOf(getResources().getColor(R.color.coursecolor2)));
                                courseName.setBackgroundColor(parsedColor);
                                break;
                            case 5317:
                                parsedColor = Color.parseColor(String.valueOf(getResources().getColor(R.color.coursecolor3)));
                                courseName.setBackgroundColor(parsedColor);
                                break;
                            case 2937298:
                                parsedColor = Color.parseColor(String.valueOf(getResources().getColor(R.color.coursecolor4)));
                                courseName.setBackgroundColor(parsedColor);
                                break;
                            case 10011977:
                                parsedColor = Color.parseColor(String.valueOf(getResources().getColor(R.color.coursecolor5)));
                                courseName.setBackgroundColor(parsedColor);
                                break;
                            case 12627531:
                                parsedColor = Color.parseColor(String.valueOf(getResources().getColor(R.color.coursecolor6)));
                                courseName.setBackgroundColor(parsedColor);
                                break;
                        }
                        setCourseId(finalCourseModelList.get(which).getId());
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
                                        Log.d(TAG, intColor + "<-<-<-<-CHOOSED COLOR");
                                        course.setClr(intColor);
                                        long id = db.addCourse(course);
                                        Log.d(TAG, id + "");
                                        setCourseId(id);
                                        //reloadFragment();
                                        EditText t = (EditText) testActivity.findViewById(R.id.editTextCourseName);
                                        t.setText(course.getName());
                                    }

                                })
                                .setNegativeButton("Cancel",
                                        new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int id) {
                                                dialog.cancel();
                                                reloadFragment();
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