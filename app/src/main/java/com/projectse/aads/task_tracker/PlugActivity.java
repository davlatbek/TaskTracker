package com.projectse.aads.task_tracker;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.projectse.aads.task_tracker.DBService.DatabaseHelper;
import com.projectse.aads.task_tracker.Models.CourseModel;
import com.projectse.aads.task_tracker.Models.TaskModel;

import junit.framework.Assert;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

/**
 * Created by smith on 2/29/16.
 */
public class PlugActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_plug);
        //initDebugData();
    }

    public static void initDebugData(DatabaseHelper db){

        CourseModel cMPP = new CourseModel("Modern programming paradigms");
        cMPP.setClr(R.color.coursecolor2);
        cMPP.setId(db.addCourse(cMPP));

        CourseModel cOOP = new CourseModel("Object Oriented programming");
        cOOP.setClr(R.color.coursecolor3);
        cOOP.setId(db.addCourse(cOOP));

        CourseModel cUXUI = new CourseModel("User Exprerience and User Interfaces");
        cUXUI.setClr(R.color.coursecolor4);
        cUXUI.setId(db.addCourse(cUXUI));

        TaskModel t_O_MPP_0 = new TaskModel();
        t_O_MPP_0.setName("Iterators hw");
        Calendar st = Calendar.getInstance(TimeZone.getTimeZone("UTC"), Locale.getDefault());
        Calendar dd = Calendar.getInstance(TimeZone.getTimeZone("UTC"), Locale.getDefault());
        st.add(Calendar.DAY_OF_MONTH, -7);
        dd.add(Calendar.DAY_OF_MONTH, -4);
        t_O_MPP_0.setStartTime(st);
        t_O_MPP_0.setDeadline(dd);
        t_O_MPP_0.setId(db.addTask(t_O_MPP_0));

        TaskModel t_O_MPP_1 = new TaskModel();
        t_O_MPP_1.setName("Scala overview");
        st = Calendar.getInstance(TimeZone.getTimeZone("UTC"), Locale.getDefault());
        dd = Calendar.getInstance(TimeZone.getTimeZone("UTC"), Locale.getDefault());
        st.add(Calendar.DAY_OF_MONTH, -4);
        dd.add(Calendar.DAY_OF_MONTH, -1);
        t_O_MPP_1.setStartTime(st);
        t_O_MPP_1.setDeadline(dd);
        t_O_MPP_1.setId(db.addTask(t_O_MPP_1));

        TaskModel t_D_MPP_0 = new TaskModel();
        t_D_MPP_0.setName("Calculator app");
        st = Calendar.getInstance(TimeZone.getTimeZone("UTC"), Locale.getDefault());
        dd = Calendar.getInstance(TimeZone.getTimeZone("UTC"), Locale.getDefault());
        st.add(Calendar.DAY_OF_MONTH, -4);
        dd.add(Calendar.DAY_OF_MONTH, -1);
        t_D_MPP_0.setStartTime(st);
        t_D_MPP_0.setDeadline(dd);
        t_D_MPP_0.setPriority(TaskModel.Priority.MEDIUM);
        t_D_MPP_0.setIsDone(true);
        t_D_MPP_0.setId(db.addTask(t_D_MPP_0));

        TaskModel t_D_MPP_1 = new TaskModel();
        t_D_MPP_1.setName("Fibonacci Template");
        st = Calendar.getInstance(TimeZone.getTimeZone("UTC"), Locale.getDefault());
        dd = Calendar.getInstance(TimeZone.getTimeZone("UTC"), Locale.getDefault());
        st.add(Calendar.DAY_OF_MONTH, -4);
        dd.add(Calendar.DAY_OF_MONTH, -1);
        t_D_MPP_1.setStartTime(st);
        t_D_MPP_1.setDeadline(dd);
        t_D_MPP_1.setIsDone(true);
        t_D_MPP_1.setId(db.addTask(t_D_MPP_1));

        TaskModel t_A_MPP_0 = new TaskModel();
        t_A_MPP_0.setName("Prepare to Exam");
        st = Calendar.getInstance(TimeZone.getTimeZone("UTC"), Locale.getDefault());
        dd = Calendar.getInstance(TimeZone.getTimeZone("UTC"), Locale.getDefault());
        t_A_MPP_0.setStartTime(st);
        t_A_MPP_0.setDeadline(dd);
        t_A_MPP_0.setPriority(TaskModel.Priority.HIGH);
        t_A_MPP_0.setId(db.addTask(t_A_MPP_0));

        try {
            db.addCourseToTask(t_O_MPP_0.getId());
            db.updateCourseToTask(t_O_MPP_0.getId(), cMPP.getId());
            db.addCourseToTask(t_O_MPP_1.getId());
            db.updateCourseToTask(t_O_MPP_1.getId(), cMPP.getId());
            db.addCourseToTask(t_D_MPP_0.getId());
            db.updateCourseToTask(t_D_MPP_0.getId(), cMPP.getId());
            db.addCourseToTask(t_D_MPP_1.getId());
            db.updateCourseToTask(t_D_MPP_1.getId(), cMPP.getId());
            db.addCourseToTask(t_A_MPP_0.getId());
            db.updateCourseToTask(t_A_MPP_0.getId(), cMPP.getId());
        } catch (Exception e) {
            e.printStackTrace();
        }

        TaskModel t_D_UX_0 = new TaskModel();
        t_D_UX_0.setName("Create ptototype");
        st = Calendar.getInstance(TimeZone.getTimeZone("UTC"), Locale.getDefault());
        dd = Calendar.getInstance(TimeZone.getTimeZone("UTC"), Locale.getDefault());
        st.add(Calendar.DAY_OF_MONTH, -6);
        dd.add(Calendar.DAY_OF_MONTH, -7);
        t_D_UX_0.setStartTime(st);
        t_D_UX_0.setDeadline(dd);
        t_D_UX_0.setPriority(TaskModel.Priority.MEDIUM);
        t_D_UX_0.setIsDone(true);
        t_D_UX_0.setId(db.addTask(t_D_UX_0));

        try {
            db.addCourseToTask(t_D_UX_0.getId());
            db.updateCourseToTask(t_D_UX_0.getId(), cUXUI.getId());
        } catch (Exception e) {
            e.printStackTrace();
        }

        TaskModel t_D_OOP_0 = new TaskModel();
        t_D_OOP_0.setName("Prepare to midterm");
        st = Calendar.getInstance(TimeZone.getTimeZone("UTC"), Locale.getDefault());
        dd = Calendar.getInstance(TimeZone.getTimeZone("UTC"), Locale.getDefault());
        st.add(Calendar.DAY_OF_MONTH, -9);
        dd.add(Calendar.DAY_OF_MONTH, -3);
        t_D_OOP_0.setStartTime(st);
        t_D_OOP_0.setDeadline(dd);
        t_D_OOP_0.setPriority(TaskModel.Priority.HIGH);
        t_D_OOP_0.setIsDone(true);
        t_D_OOP_0.setId(db.addTask(t_D_OOP_0));

        TaskModel t_A_OOP_0 = new TaskModel();
        t_A_OOP_0.setName("Read Touch of Class");
        st = Calendar.getInstance(TimeZone.getTimeZone("UTC"), Locale.getDefault());
        dd = Calendar.getInstance(TimeZone.getTimeZone("UTC"), Locale.getDefault());
        st.add(Calendar.DAY_OF_MONTH, -3);
        dd.add(Calendar.DAY_OF_MONTH, 5);
        t_A_OOP_0.setStartTime(st);
        t_A_OOP_0.setDeadline(dd);
        t_A_OOP_0.setPriority(TaskModel.Priority.MEDIUM);
        t_A_OOP_0.setId(db.addTask(t_A_OOP_0));

        TaskModel t_A_OOP_1 = new TaskModel();
        t_A_OOP_1.setName("HW#6 Query call");
        st = Calendar.getInstance(TimeZone.getTimeZone("UTC"), Locale.getDefault());
        dd = Calendar.getInstance(TimeZone.getTimeZone("UTC"), Locale.getDefault());
        st.add(Calendar.DAY_OF_MONTH, 3);
        dd.add(Calendar.DAY_OF_MONTH, 5);
        t_A_OOP_1.setStartTime(st);
        t_A_OOP_1.setDeadline(dd);
        t_A_OOP_1.setId(db.addTask(t_A_OOP_1));

        try {
            db.addCourseToTask(t_D_OOP_0.getId());
            db.updateCourseToTask(t_D_OOP_0.getId(), cOOP.getId());
            db.addCourseToTask(t_A_OOP_0.getId());
            db.updateCourseToTask(t_A_OOP_0.getId(), cOOP.getId());
            db.addCourseToTask(t_A_OOP_1.getId());
            db.updateCourseToTask(t_A_OOP_1.getId(), cOOP.getId());
        } catch (Exception e) {
            e.printStackTrace();
        }



//        for ( int i = 0; i < 20; i++){
//            ArrayList<Long> subts = new ArrayList<>();
//
//            TaskModel t1 = new TaskModel();
//            t1.setName("TestTask1");
//            t1.setId(db.addTask(t1));
//            t1.setStartTime(Calendar.getInstance(TimeZone.getTimeZone("UTC"), Locale.getDefault()));
//            t1.setDeadline(Calendar.getInstance(TimeZone.getTimeZone("UTC"), Locale.getDefault()));
//            subts.add(t1.getId());
//
//            TaskModel t2 = new TaskModel();
//            t2.setName("TestTask2");
//            t2.setId(db.addTask(t2));
//            subts.add(t2.getId());
//
//            TaskModel t = new TaskModel();
//            t.setName("TestTaskMaster");
//            t.setId(db.addTask(t));
//
//            try {
//                db.addCourseToTask(t.getId());
//                db.updateCourseToTask(t.getId(), cMPP.getId());
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//
//            List<TaskModel> list = db.getTaskModelList();
//            Assert.assertTrue(db.getTask(t.getId()).getSubtasks_ids().size() == 0);
//
//            for(Long id : subts ){
//                TaskModel t_buf = db.getTask(id);
//                t.addSubtask(t_buf);
//            }
//            t.setSubtasks_ids(subts);
//            try {
//                db.updateTask(t);
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//        }
//
//
//        Calendar dateTarget = Calendar.getInstance(TimeZone.getTimeZone("UTC"), Locale.getDefault());
//        dateTarget.add(Calendar.DAY_OF_MONTH, -1);
//
//        Calendar dateRight1 = Calendar.getInstance(TimeZone.getTimeZone("UTC"), Locale.getDefault());
//        dateRight1.add(Calendar.DAY_OF_MONTH, -1);
//        dateRight1.set(Calendar.HOUR_OF_DAY, 23);
//
//        Calendar dateRight2 = Calendar.getInstance(TimeZone.getTimeZone("UTC"), Locale.getDefault());
//        dateRight2.add(Calendar.DAY_OF_MONTH, -1);
//        dateRight2.set(Calendar.HOUR_OF_DAY, 0);
//
//        Calendar dateWrong1 = Calendar.getInstance(TimeZone.getTimeZone("UTC"), Locale.getDefault());
//        dateWrong1.add(Calendar.DAY_OF_MONTH, 2);
//
//        Calendar dateWrong2 = Calendar.getInstance(TimeZone.getTimeZone("UTC"), Locale.getDefault());
//
//        TaskModel t11 = new TaskModel();
//
//        t11.setName("Jordan");
//        t11.setStartTime(dateRight1);
//        t11.setId(db.addTask(t11));
//        t11.setName("Fred");
//        t11.setStartTime(dateRight2);
//        t11.setId(db.addTask(t11));
//        t11.setName("Dorian");
//        t11.setStartTime(dateTarget);
//        t11.setId(db.addTask(t11));
//
//        t11.setName("Mark");
//        t11.setStartTime(dateWrong1);
//        t11.setId(db.addTask(t11));
//        t11.setName("Frank");
//        t11.setStartTime(dateWrong2);
//        t11.setId(db.addTask(t11));
    }

    public void callDaylyPlan(View v){
        Intent intent = new Intent (getApplicationContext(), DailyPlanActivity.class);
        startActivity(intent);
    }
}
