package com.projectse.aads.task_tracker.DataBaseHelper;

import android.database.Cursor;
import android.nfc.Tag;
import android.util.Log;

import com.projectse.aads.task_tracker.Models.CourseModel;
import com.projectse.aads.task_tracker.Models.TaskModel;

import junit.framework.Assert;

import java.util.Calendar;

/**
 * Created by Andrey Zolin on 17.02.2016.
 */
public class DBCourseToTaskMethodsTest extends TestInit {

    public static String TAG = "tag";

    public void testAddCourseToTask() throws Exception {

        TaskModel task = new TaskModel();
        task.setName("test1");
        TaskModel task2 = new TaskModel();
        task.setName("test2");

        CourseModel course = new CourseModel();
        course.setId(1L);
        course.setName("Other");
        course.setPriority(CourseModel.Priority.HIGH);
        db.addCourse(course);

        db.addCourseToTask(db.addTask(task)); // get task id after inserting in table task and put this id in table course to task with default course id = 1
        db.addCourseToTask(db.addTask(task2));
        Assert.assertTrue(db.getListOfTasks(1L).get(0).getName(), db.getListOfTasks(1L).size() >= 2);
    }

    public boolean isTableExists(String tableName) {
        Cursor cursor = db.getReadableDatabase().rawQuery("select DISTINCT tbl_name from sqlite_master where tbl_name = '" + tableName + "'", null);
        if (cursor != null) {
            if (cursor.getCount() > 0) {
                cursor.close();
                return true;
            }
            cursor.close();
        }
        return false;
    }

    public void testCreateTableCourse() throws Exception {

        Assert.assertTrue("Check if exist table courses", isTableExists("courses"));
    }

    public void testListOfAllCourses() throws Exception {

        CourseModel course = new CourseModel();
        course.setName("test3");
        course.setPriority(CourseModel.Priority.HIGH);
        db.addCourse(course);

        Assert.assertTrue("List of courses should be not empty", db.getCourseModelList().size() > 0);

    }

    public void testAddCourse() throws Exception {
        CourseModel course = new CourseModel();
        course.setName("test3");
        course.setPriority(CourseModel.Priority.HIGH);
        long id = -100;
        id = db.addCourse(course);

        Assert.assertTrue("test", id > 0);
    }

    public void testDeleteCourse() throws Exception {
        CourseModel course = new CourseModel();
        course.setName("test3");
        course.setPriority(CourseModel.Priority.HIGH);
        long id = db.addCourse(course);

        Assert.assertTrue("If we delete course method will return true", db.deleteCourse(id));
    }

    public void testUpDateCourse() throws Exception {
        CourseModel course = new CourseModel();
        course.setName("test3");
        course.setPriority(CourseModel.Priority.HIGH);
        long id = db.addCourse(course);
        course.setId(id);
        // change name and update
        course.setName("tt");
        db.updateCourse(course);

        Assert.assertTrue(db.getCourse(id).getName().equalsIgnoreCase("tt"));

    }

    public void testCreateCourseToTasks() throws Exception {
        Assert.assertTrue("Check if exist table courses_to_tasks", isTableExists("courses_to_tasks"));
    }

    public void testUpdateCourseToTask() throws Exception {
        CourseModel course_1 = new CourseModel();
        course_1.setName("Course-1");
        course_1.setPriority(CourseModel.Priority.HIGH);
        long course_id_1 = db.addCourse(course_1);

        TaskModel task = new TaskModel();
        task.setName("test1");

        long coursetotaskid = db.addCourseToTask(db.addTask(task));

        Assert.assertTrue("Number of updated rows should be == 1", db.updateCourseToTask(coursetotaskid, course_id_1) == 1);
    }

    public void testSelectListOfTasks1() throws Exception {
        CourseModel course_one = new CourseModel();
        course_one.setName("Course-1");
        course_one.setPriority(CourseModel.Priority.HIGH);
        long course_id_one = db.addCourse(course_one);

        TaskModel task_one = new TaskModel();
        task_one.setName("Task-1");
        long task_id_1 = db.addTask(task_one);

        db.addCourseToTask(db.addTask(task_one));
        db.updateCourseToTask(task_id_1, course_id_one);
        Assert.assertTrue(db.getListOfTasks(course_id_one).size() == 1);

    }

    public void testSelectListOfTasks2() throws Exception {


        CourseModel course_two = new CourseModel();
        course_two.setName("Course-2");
        course_two.setPriority(CourseModel.Priority.HIGH);
        long course_id_two = db.addCourse(course_two);

        TaskModel task_two = new TaskModel();
        task_two.setName("Task-2");
        long task_id_2 = db.addTask(task_two);

        db.addCourseToTask(db.addTask(task_two));
        db.updateCourseToTask(task_id_2, course_id_two);
        Assert.assertTrue(db.getListOfTasks(course_id_two).size() == 1);

    }
}
