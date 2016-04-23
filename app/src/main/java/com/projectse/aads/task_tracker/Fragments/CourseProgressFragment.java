package com.projectse.aads.task_tracker.Fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.Chart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
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
public class CourseProgressFragment extends Fragment {
    BarChart barChart;
    PieChart pieChart;
    private WeekSliderFragment sliderFragment;
    private WeeklyViewFragment.onWeekViewEventListener listener;
    private ImageButton buttonPreviousChart, buttonNextChart;
    DatabaseHelper db;
    List<CourseModel> courseModels;
    TextView courseLabel, totalTasks, finished, actual, overdue;
    int courseNumb;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        getActivity().setTitle("Course Progress");
        View view = inflater.inflate(R.layout.fragment_courseprogress, container, false);
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        db = DatabaseHelper.getsInstance(getActivity());
        courseModels = db.getCourseModelList();
        barChart = (BarChart) view.findViewById(R.id.barChartForCourse);
        barChart.setData(createBarChartForAllCourses(courseModels));
        barChart.animateXY(2000, 2000);
        barChart.setDescription("");
        YAxis leftAxis = barChart.getAxisLeft();
        YAxis rightAxis = barChart.getAxisRight();
        rightAxis.setEnabled(false);
        leftAxis.setAxisMinValue(0f);
        leftAxis.setAxisMaxValue(100f);
        barChart.invalidate();
        getViews(view);
        try {
            setCourseStatistics(-1);
        } catch (Exception e) {
            e.printStackTrace();
        }
        setHasOptionsMenu(true);
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
        pieChart = (PieChart) view.findViewById(R.id.pieChartForCourse);
        barChart.setVisibility(View.VISIBLE);
        pieChart.setVisibility(View.INVISIBLE);

        buttonPreviousChart.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                barChart.setVisibility(View.INVISIBLE);
                pieChart.setVisibility(View.VISIBLE);
                pieChart.setDescriptionTextSize(40f);
                try {
                    pieChart.setData(createPieChartByCourse(1));
                } catch (Exception e) {
                    e.printStackTrace();
                }
                pieChart.animateXY(2000, 2000);
                pieChart.invalidate();
                try {
                    setCourseStatistics(0);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        buttonNextChart.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                barChart.setVisibility(View.VISIBLE);
                pieChart.setVisibility(View.INVISIBLE);
                courseNumb++;
                if (courseNumb > courseList.size() - 1){
                    courseNumb = -1;
                }
                if (courseNumb == -1) {
                    courseLabel.setText("All Courses");
                    barChart.setDrawValueAboveBar(false);
                    YAxis leftAxis = barChart.getAxisLeft();
                    leftAxis.setAxisMinValue(0f);
                    barChart.setData(createBarChartForAllCourses(courseModels));
                    barChart.animateXY(500, 500);
                    pieChart.setDescription("");
                    barChart.invalidate();
                    try {
                        setCourseStatistics(-1);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {
                    try {
                        switchChart(courseList.get(courseNumb));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        });

        totalTasks = (TextView) view.findViewById(R.id.total);
        finished = (TextView) view.findViewById(R.id.finished);
        actual = (TextView) view.findViewById(R.id.actual);
        overdue = (TextView) view.findViewById(R.id.overdue);
    }

    public void switchChart(long course_id) throws Exception {
        courseLabel.setText(db.getCourse(course_id).getName());
        barChart.setVisibility(View.INVISIBLE);
        pieChart.setVisibility(View.VISIBLE);
        pieChart.setDescription("");
        pieChart.setDescriptionTextSize(40f);
        pieChart.setData(createPieChartByCourse(course_id));
        pieChart.animateXY(2000, 2000);
        pieChart.invalidate();
        setCourseStatistics(course_id);
    }

    public PieData createPieChartByCourse(long courseId) throws Exception {
        int actualNumber = 0, finishedNumber = 0, overDueNumber = 0;
        List<TaskModel> taskModels = db.getActualTasks(Calendar.getInstance());
        for (TaskModel task : taskModels){
           if (task.getCourse().getId() == courseId)
               actualNumber++;
        }
        taskModels = db.getDoneTasks();
        for (TaskModel task : taskModels){
            if (task.getCourse().getId() == courseId)
                finishedNumber++;
        }
        taskModels = db.getOverdueTasks(Calendar.getInstance());
        for (TaskModel task : taskModels){
            if (task.getCourse().getId() == courseId)
                overDueNumber++;
        }
        ArrayList<Entry> entries = new ArrayList<>();
        entries.add(new Entry(actualNumber, 0));
        entries.add(new Entry(finishedNumber, 1));
        entries.add(new Entry(overDueNumber, 2));

        PieDataSet pieDataSet = new PieDataSet(entries, "# of tasks in a course");
        pieDataSet.setColors(ColorTemplate.COLORFUL_COLORS);

        ArrayList<String> labels = new ArrayList<String>();
        labels.add("Actual");
        labels.add("Finished");
        labels.add("Overdue");

        return new PieData(labels, pieDataSet);
    }

    public BarData createBarChartForAllCourses(List<CourseModel> coursesList) {
        List<TaskModel> doneTasks = db.getDoneTasks();
        List<TaskModel> allTasks = db.getTaskModelList();
        int finishedNumber = 0, allNumber = 0, courseNumber = 0;

        ArrayList<BarEntry> entries = new ArrayList<>();
        for (CourseModel courseModel : courseModels){
            finishedNumber = 0;
            allNumber = 0;
            for (TaskModel task : doneTasks){
                if (task.getCourse().getId() == courseModel.getId())
                    finishedNumber++;
            }
            for (TaskModel task : allTasks){
                if(task.getCourse()== null || courseModel == null)
                    continue;
                if (task.getCourse().getId() == courseModel.getId())
                    allNumber++;
            }
            entries.add(new BarEntry(((float)finishedNumber/(float)allNumber)*100, courseNumber));
            courseNumber++;
        }
        BarDataSet barDataSet = new BarDataSet(entries, "% of done tasks");
        barDataSet.setColors(ColorTemplate.COLORFUL_COLORS);

        String[] courseLabels = new String[courseModels.size()];
        int i = 0;
        ArrayList<String> labels = new ArrayList<String>();
        for (CourseModel courseModel : coursesList){
            labels.add(courseModel.getName());
            courseLabels[i] = courseModel.getAbbreviation();
            i++;
        }
        barDataSet.setStackLabels(courseLabels);

        return new BarData(labels, barDataSet);
    }

    public void setCourseStatistics(long course_id) throws Exception {
        int totalNumber = 0, actualNumber = 0, finishedNumber = 0, overDueNumber = 0;
        if (course_id < 0){
            totalTasks.setText("Total tasks: " + String.valueOf(db.getTaskModelList().size()));
            finished.setText("Finished: " + String.valueOf(db.getDoneTasks().size()));
            actual.setText("Actual: " + String.valueOf(db.getActualTasks(Calendar.getInstance()).size()));
            overdue.setText("Overdue: " + String.valueOf(db.getOverdueTasks(Calendar.getInstance()).size()));
        } else {
            List<TaskModel> taskModels = db.getTaskModelList();
            for (TaskModel task : taskModels){
                if (task.getCourse().getId() == course_id)
                    totalNumber++;
            }
            taskModels = db.getActualTasks(Calendar.getInstance());
            for (TaskModel task : taskModels){
                if (task.getCourse().getId() == course_id)
                    actualNumber++;
            }
            taskModels = db.getDoneTasks();
            for (TaskModel task : taskModels){
                if (task.getCourse().getId() == course_id)
                    finishedNumber++;
            }
            taskModels = db.getOverdueTasks(Calendar.getInstance());
            for (TaskModel task : taskModels){
                if (task.getCourse().getId() == course_id)
                    overDueNumber++;
            }
            totalTasks.setText("Total tasks: " + totalNumber);
            finished.setText("Finished: " + finishedNumber);
            actual.setText("Actual: " + actualNumber);
            overdue.setText("Overdue: " + overDueNumber);
        }
    }
}