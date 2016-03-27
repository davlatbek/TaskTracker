package com.projectse.aads.task_tracker.Fragments;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DialogFragment;
import android.app.ListFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.larswerkman.lobsterpicker.sliders.LobsterShadeSlider;
import com.projectse.aads.task_tracker.DBService.DatabaseHelper;
import com.projectse.aads.task_tracker.Dialogs.ListOfCourses;
import com.projectse.aads.task_tracker.MainActivity;
import com.projectse.aads.task_tracker.Models.CourseModel;
import com.projectse.aads.task_tracker.R;
import com.projectse.aads.task_tracker.TaskActivity;
import com.projectse.aads.task_tracker.TaskAddActivity;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by Andrey Zolin on 20.03.2016.
 */
public class CoursesFragment extends Fragment {

    public interface onCourseClickListener{
        public void callCourseOverviewFragment(long course_id);
    }

    public interface onCreateCpourseListener {
        public void callCourseCreateFragment();
    }

    private onCourseClickListener courseClickEventListener;
    private onCreateCpourseListener courseCreateClickListener;

    public void setSomeEventListener(onCourseClickListener someEventListener) {
        this.courseClickEventListener = someEventListener;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        getActivity().setTitle("Courses");
        View view = inflater.inflate(R.layout.fragment_course_list, container, false);

        ImageButton addRequestButton = (ImageButton) view.findViewById(R.id.create_request_fab);
        addRequestButton.setOnClickListener(requestButtonListener);

        DatabaseHelper db = new DatabaseHelper(getActivity());

//        CourseModel course = new CourseModel();
//        course.setName("54545");
//        course.setClr(32);
//        course.setId(3l);
//        db.addCourse(course);

        List<CourseModel> course_list = new ArrayList<>();
        try {
             course_list = db.getCourseModelList();
        } catch (Exception e) {
            e.printStackTrace();
        }

        for(CourseModel c : course_list) {

            // get data from DB
            View requestListItemView = inflater.inflate(R.layout.course_list_item, null);
            TableLayout requestItemsTable = (TableLayout) view.findViewById(R.id.request_items_table);
            TextView tv = (TextView)requestListItemView.findViewById(R.id.request_name);
            TextView desc = (TextView)requestListItemView.findViewById(R.id.request_short_desc);
            tv.setText(c.getName());
            desc.setText("www");
            requestListItemView.setOnClickListener(requestItemListener);
            requestItemsTable.addView(requestListItemView);
        }

        return view;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        if(activity instanceof onCourseClickListener){
            courseClickEventListener = (onCourseClickListener) activity;
        }
    }

    private View.OnClickListener requestItemListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            long course_id = Integer.getInteger(((TextView) v.findViewById(R.id.id_course)).getText().toString());
            if(courseClickEventListener != null){
                courseClickEventListener.callCourseOverviewFragment(course_id);
            }

          //  someEventListener

            // call course
            Toast.makeText(getActivity(), ((TextView) v.findViewById(R.id.request_name)).getText() + " item selected", Toast.LENGTH_SHORT).show();
        }
    };

    private View.OnClickListener requestButtonListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            final CourseModel course = new CourseModel();
            final DatabaseHelper db = new DatabaseHelper(getActivity());
            final LayoutInflater inflater = getActivity().getLayoutInflater();
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
                            course.setClr(intColor);
                            long id = db.addCourse(course);
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
    };
}
