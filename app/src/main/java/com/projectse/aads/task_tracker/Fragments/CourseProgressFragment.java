package com.projectse.aads.task_tracker.Fragments;

import android.app.Fragment;
import android.app.FragmentManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.github.mikephil.charting.*;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.projectse.aads.task_tracker.DBService.DatabaseHelper;
import com.projectse.aads.task_tracker.Models.CourseModel;
import com.projectse.aads.task_tracker.Models.TaskModel;
import com.projectse.aads.task_tracker.R;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * Created by Davlatbek Isroilov on 4/7/2016.
 * Innopolis University
 */
public class CourseProgressFragment extends Fragment implements WeekSliderFragment.onWeekSliderEventListener {
    BarChart chart;
    private WeekSliderFragment sliderFragment;
    private WeeklyViewFragment.onWeekViewEventListener listener;
    private ImageButton buttonPreviousChart, buttonNextChart;
    DatabaseHelper db;
    List<CourseModel> courseModels;
    TextView courseLabel, totalTasks, finished, actual, overdue, postponed, deleted;
    int courseNumb;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        getActivity().setTitle("Course Progress");
        View view = inflater.inflate(R.layout.fragment_courseprogress, container, false);

        db = DatabaseHelper.getsInstance(getActivity());
        List<CourseModel> courses = db.getCourseModelList();
        int coursesNumber = courses.size();

        chart = (BarChart) view.findViewById(R.id.barChartForCourse);
        chart.setData(createBarChartForAllCourses(courses));
        chart.setDescription("Progress chart");
        chart.animateXY(2000, 2000);
        chart.invalidate();
        setHasOptionsMenu(true);

        /*FragmentManager fm = getChildFragmentManager();
        sliderFragment = new WeekSliderFragment();
        sliderFragment.setSomeEventListener(this);
        fm.beginTransaction().replace(R.id.fragment_week_slider, sliderFragment).commit();
        fm.executePendingTransactions();*/

        getViews(view);

        return view;
    }

    public void getViews(View view) {
        courseNumb = 0;
        courseLabel = (TextView) view.findViewById(R.id.week_label);
        courseLabel.setText("All Courses");

        courseModels = db.getCourseModelList();
        final List<Long> courseList = new ArrayList<>();
        for (CourseModel courseModel : courseModels){
            courseList.add(courseModel.getId());
        }

        buttonPreviousChart = (ImageButton) view.findViewById(R.id.btnPrevWeek);
        buttonNextChart = (ImageButton) view.findViewById(R.id.btnNextWeek);
        buttonPreviousChart.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

            }
        });
        buttonNextChart.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                courseNumb++;
                if (courseNumb > courseList.size() - 1){
                    courseNumb = -1;
                }
                try {
                    switchChart(courseList.get(courseNumb));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        totalTasks = (TextView) view.findViewById(R.id.total);
        finished = (TextView) view.findViewById(R.id.finished);
        actual = (TextView) view.findViewById(R.id.total);
        overdue = (TextView) view.findViewById(R.id.finished);
        postponed = (TextView) view.findViewById(R.id.total);
        deleted = (TextView) view.findViewById(R.id.finished);
    }

    public void switchChart(long course_id) throws Exception {
        if (course_id < 0){
            chart.setData(createBarChartForAllCourses(courseModels));
            chart.setDescription("Progress chart");
            chart.animateXY(1000, 1000);
            chart.invalidate();
        }
        else {
            courseLabel.setText(db.getCourse(course_id).getName());
            chart.setData(createBarChartByCourse(course_id));
            chart.animateXY(1000, 1000);
            chart.invalidate();
        }
    }

    public BarData createBarChartByCourse(long courseId) {
        ArrayList<BarEntry> entries = new ArrayList<>();
        entries.add(new BarEntry(1, 0));
        entries.add(new BarEntry(0, 1));
        entries.add(new BarEntry(1, 2));
        entries.add(new BarEntry(4, 3));
        BarDataSet barDataSet = new BarDataSet(entries, "# of tasks in a course");
        barDataSet.setColors(ColorTemplate.COLORFUL_COLORS);
        barDataSet.setStackLabels(new String[]{"1", "2", "3", "4"});

        ArrayList<String> labels = new ArrayList<String>();
        labels.add("aa");
        labels.add("bb");
        labels.add("cc");
        labels.add("dd");

        return new BarData(labels, barDataSet);
    }

    public BarData createBarChartForAllCourses(List<CourseModel> coursesList) {

        ArrayList<BarEntry> entries = new ArrayList<>();
        entries.add(new BarEntry(6, 0));
        entries.add(new BarEntry(12, 1));
        entries.add(new BarEntry(9, 2));
        entries.add(new BarEntry(4, 3));
        BarDataSet barDataSet = new BarDataSet(entries, "# of tasks in a course");
        barDataSet.setColors(ColorTemplate.COLORFUL_COLORS);
        barDataSet.setStackLabels(new String[]{"OSN", "DM", "UX", "MPP"});

        ArrayList<String> labels = new ArrayList<String>();
        labels.add("OSN");
        labels.add("DM");
        labels.add("UX");
        labels.add("MPP");

        return new BarData(labels, barDataSet);
    }

    public void setCourseStatistics(long course_id) throws Exception {
        CourseModel courseModel = db.getCourse(course_id);

    }

    @Override
    public void setWeek(Calendar date) {

    }
}