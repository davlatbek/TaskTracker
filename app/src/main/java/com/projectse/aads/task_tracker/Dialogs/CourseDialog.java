package com.projectse.aads.task_tracker.Dialogs;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.larswerkman.lobsterpicker.sliders.LobsterShadeSlider;
import com.projectse.aads.task_tracker.DBService.DatabaseHelper;
import com.projectse.aads.task_tracker.Models.CourseModel;
import com.projectse.aads.task_tracker.R;

/**
 * Created by Anastasia A. Puzankova on 05-Apr-16.
 */
public class CourseDialog extends DialogFragment {
    String dialogName = null;
    String dialogCourseName = null;
    int color = 0;
    Boolean isEdit = false;


    public CourseDialog(String name) {
        this.dialogName = name;
    }

    public CourseDialog(String name, String courseName, Integer color) {
        this.dialogName = name;
        this.dialogCourseName = courseName;
        this.color = color;
        isEdit = true;
    }

    public void reloadFragment() {
    }

    public void setCurrentFragment(Fragment fragment) {
        FragmentManager fragmentManager = getFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.flContent, fragment).commit();
        fragmentManager.executePendingTransactions();
    }


    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        //   AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        final CourseModel course = new CourseModel();
        final Context context = getActivity();
        final DatabaseHelper db = DatabaseHelper.getsInstance(getActivity().getApplicationContext());
        final LayoutInflater inflater = getActivity().getLayoutInflater();
        View inflate = inflater.inflate(R.layout.add_new_course_form, null);
        final EditText courseName = (EditText) inflate.findViewById(R.id.coursename);
        if (isEdit == true) {
            courseName.setText(dialogCourseName);
        }
        final LobsterShadeSlider shadeSlider = (LobsterShadeSlider) inflate.findViewById(R.id.shadeslider);
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity())
                .setTitle(dialogName)
                .setView(inflate)
                .setPositiveButton("Create", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // Get name from field
                        String name = courseName.getText().toString();
                        if(name == null || name.equals("")){
                            Toast.makeText(context, "Course without name not created!", Toast.LENGTH_LONG).show();
                        } else {
                            course.setName(name.toString());
                            // Get color from slider
                            Integer intColor = shadeSlider.getColor();
                            course.setClr(intColor);
                            if (isEdit == true) {
                                db.updateCourse(course);
                            } else {
                                long id = db.addCourse(course);
                            }
                            reloadFragment();
                        }
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

        return builder.create();

    }
}
