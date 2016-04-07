package com.projectse.aads.task_tracker.Fragments;

import android.app.Activity;
import android.app.Fragment;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TableLayout;
import android.widget.TextView;

import com.projectse.aads.task_tracker.DBService.DatabaseHelper;
import com.projectse.aads.task_tracker.Dialogs.CourseDialog;
import com.projectse.aads.task_tracker.Models.CourseModel;
import com.projectse.aads.task_tracker.Models.TaskModel;
import com.projectse.aads.task_tracker.R;

import java.util.Calendar;
import java.util.List;


/**
 * Created by Andrey Zolin on 20.03.2016.
 */
public class CoursesFragment extends Fragment {

    private onCourseClickListener courseClickEventListener;
    private View.OnClickListener requestItemListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            TextView tv = (TextView) v.findViewById(R.id.id_course);
            String st = tv.getText().toString();
            long course_id = Long.valueOf(st);

            if (courseClickEventListener != null) {
                courseClickEventListener.callCourseOverviewFragment(course_id);
            }
        }
    };
    private View.OnClickListener requestButtonListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
                CourseDialog newFragment = new CourseDialog("Add new course");
                newFragment.show(getFragmentManager(), "ecd");
        }
    };


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        getActivity().setTitle("Courses");
        View view = inflater.inflate(R.layout.fragment_course_list, container, false);

        ImageButton addRequestButton = (ImageButton) view.findViewById(R.id.create_request_fab);
      //  addRequestButton.setOnClickListener();
        addRequestButton.setOnClickListener(requestButtonListener);

        DatabaseHelper db = DatabaseHelper.getsInstance(getActivity().getApplicationContext());

        List<CourseModel> course_list = db.getCourseModelList();

        for (CourseModel c : course_list) {

            // get data from DB
            View requestListItemView = inflater.inflate(R.layout.course_list_item, null);
            TableLayout requestItemsTable = (TableLayout) view.findViewById(R.id.request_items_table);
            TextView tv = (TextView) requestListItemView.findViewById(R.id.request_name);
            TextView desc = (TextView) requestListItemView.findViewById(R.id.request_short_desc);
            TextView id = (TextView) requestListItemView.findViewById(R.id.id_course);
            tv.setText(c.getName());
            TextView abrev = (TextView) requestListItemView.findViewById(R.id.abrevLbl);
            abrev.setText(c.getAbbreviation());
            try {
                abrev.setBackgroundResource(c.getClr());

            } catch (Resources.NotFoundException e) {
                abrev.setBackgroundColor(Color.DKGRAY);
            }

//            int parsedColor = Color.parseColor(String.valueOf(c.getClr()));
//            abrev.setBackgroundColor(parsedColor);

            switch ((-1)*c.getClr()) {
                case 7617718: // int parsedColor = Color.parseColor(String.valueOf(getResources().getColor(R.color.coursecolor1)));
                    abrev.setBackgroundResource(R.color.coursecolor1);
                    break;
                case 16728876: //parsedColor = Color.parseColor(String.valueOf(getResources().getColor(R.color.coursecolor2)));
                    abrev.setBackgroundResource(R.color.coursecolor2);
                    break;
                case 5317: // parsedColor = Color.parseColor(String.valueOf(getResources().getColor(R.color.coursecolor3)));
                    abrev.setBackgroundResource(R.color.coursecolor3);
                    break;
                case 2937298: // parsedColor = Color.parseColor(String.valueOf(getResources().getColor(R.color.coursecolor4)));
                    abrev.setBackgroundResource(R.color.coursecolor4);
                    break;
                case 10011977: // parsedColor = Color.parseColor(String.valueOf(getResources().getColor(R.color.coursecolor5)));
                    abrev.setBackgroundResource(R.color.coursecolor5);
                    break;
                case 12627531:  //parsedColor = Color.parseColor(String.valueOf(getResources().getColor(R.color.coursecolor6)));
                    abrev.setBackgroundResource(R.color.coursecolor6);
                    break;
            }

            List<TaskModel> overdue_tasks = db.getOverdueTasksForCourse(c.getId(), Calendar.getInstance());
            List<TaskModel> actual_tasks = db.getActualTasksForCourse(c.getId(), Calendar.getInstance());
            List<TaskModel> done_tasks = db.getDoneTasksForCourse(c.getId());

            desc.setText(done_tasks.size() + " " + getResources().getString(R.string.done) + " | "
                        +actual_tasks.size() + " " + getResources().getString(R.string.actual) + " | "
                        +overdue_tasks.size() + " " + getResources().getString(R.string.overdue)
            );
            id.setText(c.getId().toString());
            requestListItemView.setOnClickListener(requestItemListener);
            requestItemsTable.addView(requestListItemView);
        }

        return view;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        if (activity instanceof onCourseClickListener) {
            courseClickEventListener = (onCourseClickListener) activity;
        }
    }

    public interface onCourseClickListener {
        public void callCourseOverviewFragment(long course_id);
    }
}