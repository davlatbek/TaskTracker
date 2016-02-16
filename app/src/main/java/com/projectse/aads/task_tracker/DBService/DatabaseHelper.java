package com.projectse.aads.task_tracker.DBService;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.projectse.aads.task_tracker.Models.CourseModel;
import com.projectse.aads.task_tracker.Models.SettingsModel;
import com.projectse.aads.task_tracker.Models.TaskModel;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Andrey Zolin on 28.01.2016.
 */
public class DatabaseHelper extends SQLiteOpenHelper {

    // Create new instance of our database
    // In any activity just pass the context and use the singleton method
    // DatabaseHelper helper = DatabaseHelper.getInstance(this);
    private static DatabaseHelper sInstance;

    // Using just only one instance to connect
    public static synchronized DatabaseHelper getsInstance(Context context) {
        if (sInstance == null) {
            sInstance = new DatabaseHelper(context.getApplicationContext());
        }
        return sInstance;
    }

    // Constructor
    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        context.deleteDatabase(DATABASE_NAME);

    }

    // Called when the database connection is being configured.
    // Configure database settings for things like foreign key support

    @Override
    public void onConfigure(SQLiteDatabase db) {
        super.onConfigure(db);
        db.setForeignKeyConstraintsEnabled(true);
    }

    // Database Name
    public static String DATABASE_NAME = "task_tracker_database";

    // Current version of database
    private static final int DATABASE_VERSION = 1;

    // Name of tables
    private static final String TABLE_TASKS = "tasks";
    private static final String TABLE_SUBTASKS = "tasks_to_subtasks";
    private static final String TABLE_COURSES = "courses";
    private static final String TABLE_SETTINGS = "settings";
    private static final String TABLE_PLANS = "plans";
    private static final String TABLE_PLANS_TO_TASKS = "plans_to_tasks";
    private static final String TABLE_COURSES_TO_TASKS = "courses_to_tasks";

    // All Keys used in table TASKS
    private static final String TASKS_KEY_ID = "task_id";
    private static final String TASKS_NAME = "task_name";
    private static final String TASKS_DESCRIPTION = "task_description";
    private static final String TASKS_DEADLINE = "task_deadline";
    private static final String TASKS_START_TIME = "task_start_time";
    private static final String TASKS_DURATION = "task_duration";
    private static final String TASKS_IS_NOTIFY_DEADLINE = "task_is_notify_deadline";
    private static final String TASKS_IS_NOTIFY_START_TIME = "task_is_notify_start_time";
    private static final String TASKS_PARENT_TASK = "task_parent_task";
    private static final String TASKS_PRIORITY = "task_priority";
    private static final String TASKS_IS_DONE = "task_is_done";

    // All keys used in table COURSES
    private static final String COURSE_ID = "course_id";
    private static final String COURSE_NAME = "course_name";
    private static final String COURSE_PRIORITY = "course_priority";

    // All keys used in table SETTINGS
    private static final String SETTINGS_ALWAYS_NOTIFY_START_TIME = "setting_always_notify_start_time";
    private static final String SETTINGS_ALWAYS_NOTIFY_DEADLINE = "setting_always_notify_deadline";
    private static final String SETTINGS_NOTIFY_START_TIME_BEFORE = "setting_notify_start_time_before";
    private static final String SETTINGS_NOTIFY_DEADLINE_BEFORE = "setting_notify_deadline_before";
    private static final String SETTINGS_NOTIFY_START_TIME_X_TIMES = "setting_notify_start_time_x_times";
    private static final String SETTINGS_NOTIFY_DEADLINE_X_TIMES = "setting_notify_deadline_x_times";

    // All keys used in table PLANS
    private static final String PLANS_ID = "plan_id";
    private static final String PLANS_TYPE = "plan_type";
    private static final String PLANS_DAY = "plan_day";

    // All keys used in table PLANS_TO_TASKS
    private static final String PLANS_TO_TASKS_PLAN_ID = "plan_to_task_plan_id";
    private static final String PLANS_TO_TASKS_TASK_ID = "plan_to_task_task_id";

    // All keys used in table COURSES_TO_TASKS
    private static final String COURSES_TO_TASKS_CURSE_ID = "course_to_task_curse_id";
    private static final String COURSES_TO_TASKS_TASK_ID = "course_to_task_task_id";
    private static final String COURSES_TO_TASK_ID = "course_to_task_id";


    public static String TAG = "tag";


    // Tasks Table Create Query
    /**
     * CREATE TABLE tasks (task_id INTEGER PRIMARY KEY AUTOINCREMENT, task_name TEXT,
     * task_description TEXT, task_deadline INTEGER)
     */
    private static final String CREATE_TABLE_TASKS = "CREATE TABLE "
            + TABLE_TASKS + "("
            + TASKS_KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + TASKS_NAME + " TEXT,"
            + TASKS_DESCRIPTION + " TEXT,"
            + TASKS_PARENT_TASK + " INTEGER, "
            + TASKS_IS_DONE + " INTEGER,"
            + TASKS_START_TIME + " INTEGER,"
            + TASKS_DEADLINE + " INTEGER,"
            + TASKS_DURATION + " INTEGER,"
            + TASKS_IS_NOTIFY_DEADLINE + " INTEGER,"
            + TASKS_IS_NOTIFY_START_TIME + " INTEGER,"
            + "FOREIGN KEY(" + TASKS_PARENT_TASK + ") REFERENCES " + TABLE_TASKS + "(" + TASKS_KEY_ID + ")"
            + ");"
            ;

//    public DatabaseHelper(Context context) {
//        super(context,DATABASE_NAME,null,DATABASE_VERSION);
//    }


    // Course Table Create Query
    /**
     * CREATE TABLE courses (course_id INTEGER PRIMARY KEY AUTOINCREMENT, course_name TEXT,
     * course_priority INTEGER)
     */

    private static final String CREATE_TABLE_COURSES = "CREATE TABLE "
            + TABLE_COURSES + "(" + COURSE_ID
            + " INTEGER PRIMARY KEY AUTOINCREMENT," + COURSE_NAME + " TEXT,"
            + COURSE_PRIORITY + " INTEGER);";


    /**
     * CREATE TABLE TABLE_COURSES_TO_TASKS (course_to_task_id INTEGER PRIMARY KEY AUTOINCREMENT,
     * course_to_task_task_id INTEGER PRIMARY KEY, course_to_task_curse_id INTEGER, FOREIGN KEY(course_to_task_curse_id) REFERENCES courses(course_id))
     */

    private static final String CREATE_TABLE_COURSES_TO_TASK = "CREATE TABLE "
            + TABLE_COURSES_TO_TASKS + " (" + COURSES_TO_TASK_ID
            + " INTEGER PRIMARY KEY AUTOINCREMENT," + COURSES_TO_TASKS_TASK_ID + " INTEGER," + COURSES_TO_TASKS_CURSE_ID + " INTEGER,"
            + "FOREIGN KEY(" + COURSES_TO_TASKS_CURSE_ID + ") REFERENCES " + TABLE_COURSES + "(" + COURSE_ID + "));";


    /**
     * This method is called by system if the database is accessed but not yet
     * created.
     *
     * @param db
     */

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_TASKS); // create tasks table
        //db.execSQL(CREATE_TABLE_COURSES); // create course table
        db.execSQL(CREATE_TABLE_COURSES_TO_TASK); // create course to task table
    }

    /**
     * This method is called when any modifications in database are done like
     * version is updated or database is changed
     *
     * @param db
     * @param oldVersion
     * @param newVersion
     */
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (oldVersion != newVersion) {

            db.execSQL("DROP TABLE IF EXISTS " + TABLE_TASKS); // drop table if exists
            onCreate(db);
        }


    }


    /**
     * Drop and recreate task table
     * @param db
     */
    public void updateTaskTable(SQLiteDatabase db){
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_TASKS + ";");
        db.execSQL(CREATE_TABLE_TASKS); // create course table
    }

    /**
     * This method is used to add course in to course table
     *
     * @param course
     * @return
     */
    public long addCourse(CourseModel course) {
        SQLiteDatabase db = this.getWritableDatabase();
        long id = 0;

        db.beginTransaction();
        try {
            ContentValues values = new ContentValues();
            values.put(COURSE_NAME, course.getName());
            values.put(COURSE_PRIORITY,course.fromPriorityToInt(course.getPriority()));
            Log.d("TAG", "add prioritiy in int value in db " + String.valueOf(course.fromPriorityToInt(course.getPriority())));
            id = db.insertOrThrow(TABLE_COURSES, null, values);
            db.setTransactionSuccessful();

        } catch (Exception e) {
            Log.d(TAG, "Error while trying to add course to database");
        } finally {
            db.endTransaction();
        }

        return id;
    }

    // get course by id

    public CourseModel getCourse(long id) throws Exception {
        SQLiteDatabase db = this.getReadableDatabase();

        // SELECT * FROM tasks WHERE id = ?;
        String selectQuery = "SELECT * FROM " + TABLE_COURSES + " WHERE "
                + COURSE_ID + " = " + id;
        Log.d(TAG, selectQuery);

        Cursor c = db.rawQuery(selectQuery, null);

        if (c != null)
            c.moveToFirst();

        CourseModel course = new CourseModel();

        course.setId(c.getLong(c.getColumnIndex(COURSE_ID)));
        course.setName(c.getString(c.getColumnIndex(COURSE_NAME)));
        course.fromIntToPriority(c.getInt(c.getColumnIndex(COURSE_PRIORITY)));
        Log.d("Tag", "try to convert int into priority --->>>>>" + c.getInt(c.getColumnIndex(COURSE_PRIORITY)));
        return course;
    }


    /**
     * This method is used to delete course from course table
     */

    public void deleteCourse(long id) {
        // delete row in course table based on id

        SQLiteDatabase db = this.getWritableDatabase();
        db.beginTransaction();
        String where = COURSE_ID + " = ?" + id;
        try {
            db.delete(TABLE_COURSES, where, null);
            db.setTransactionSuccessful();
        } catch (Exception e) {
            Log.d(TAG, "Error while trying to delete course");
        } finally {
            db.endTransaction();
        }
    }

    /**
     * This method is used to update course by id
     */
    public int updateCourse(CourseModel course) {
        SQLiteDatabase db = this.getWritableDatabase();
        int id = 0;
        db.beginTransaction();
        try {
            ContentValues values = new ContentValues();
            values.put(COURSE_NAME, course.getName());
            values.put(COURSE_PRIORITY, String.valueOf(course.getPriority()));
            id = db.update(TABLE_COURSES, values, COURSE_ID + " = ?",
                    new String[]{String.valueOf(course.getId())});
            db.setTransactionSuccessful();
        } catch (Exception e) {
            Log.d(TAG, "Error while trying to update course");
        } finally {
            db.endTransaction();
        }
        return id;
    }

    /**
     * This method is used to add task in task table
     *
     * @param task
     * @return id, auto-generized by db
     */
    public long addTask(TaskModel task) {
        SQLiteDatabase db = this.getWritableDatabase();
        long id = 0;

        // Begin Transaction
        db.beginTransaction();
        try {
            // Creating content values
            ContentValues values = new ContentValues();
            values.put(TASKS_NAME, task.getName());
            values.put(TASKS_DESCRIPTION, task.getDescription());

            //writing 1 or 0 to boolean types
            int bool = task.getIsNotifyDeadline() ? 1 : 0;
            values.put(TASKS_IS_NOTIFY_DEADLINE, bool);
            bool = task.getIsNotifyStartTime() ? 1 : 0;
            values.put(TASKS_IS_NOTIFY_START_TIME, bool);
            bool = task.getIsDone() ? 1 : 0;
            values.put(TASKS_IS_DONE, bool);

            values.put(TASKS_DURATION, task.getDuration());
            if (task.getStartTime() != null)
                values.put(TASKS_START_TIME, task.getStartTime().getTimeInMillis());
            if (task.getDeadline() != null)
                values.put(TASKS_DEADLINE, task.getDeadline().getTime().getTime());

            // Return id of the added task
            id = db.insertOrThrow(TABLE_TASKS, null, values);
            db.setTransactionSuccessful();
        } catch (Exception e) {
            Log.d(TAG, "Error while trying to add task to database " + e.toString());
            e.printStackTrace();
        } finally {
            db.endTransaction();
        }
        updateSubtasks(task);
        return id;
    }

    /**
     * write changes to db for task
     * @param task
     * @return the number of rows affected
     */

    public int updateTask(TaskModel task) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.beginTransaction();
        int res = 0;
        try {
            ContentValues values = new ContentValues();
            fillContentByTask(values, task);

            res = db.update(TABLE_TASKS, values, TASKS_KEY_ID + " = ?",
                    new String[]{String.valueOf(task.getId())});
            db.setTransactionSuccessful();
        } catch (Exception e) {
            Log.d(TAG, "Error while trying to update task to database");
        } finally {
            db.endTransaction();
        }
        updateSubtasks(task);
        return res;
    }

    // Delete method.
    public void deleteTask(long id) {
        // delete row in task table based on id

        SQLiteDatabase db = getWritableDatabase();
        db.beginTransaction();
        String where = TASKS_KEY_ID + " = " + id;
        try {
            db.delete(TABLE_TASKS, where, null);
            db.setTransactionSuccessful();
        } catch (Exception e) {
            Log.d(TAG, "Error while trying to delete all posts and users");
        } finally {
            db.endTransaction();
        }
    }

    /** Get task object by id
     *
     * @param id
     * @return
     */
    public TaskModel getTask(long id) {
        SQLiteDatabase db = this.getReadableDatabase();

        // SELECT * FROM tasks WHERE id = ?;
        String selectQuery = "SELECT * FROM " + TABLE_TASKS + " WHERE "
                + TASKS_KEY_ID + " = " + id;
        Log.d(TAG, selectQuery);

        Cursor c = db.rawQuery(selectQuery, null);

        if (c != null)
            c.moveToFirst();

        TaskModel tasks = new TaskModel();
        tasks.setId(c.getLong(c.getColumnIndex(TASKS_KEY_ID)));
        tasks.setName(c.getString(c.getColumnIndex(TASKS_NAME)));
        Calendar calStart = Calendar.getInstance();
        calStart.setTimeInMillis(c.getLong(c.getColumnIndex(TASKS_START_TIME)));
        tasks.setStartTime(calStart);
        tasks.setDuration((long) c.getInt(c.getColumnIndex(TASKS_DURATION)));

        int boolInt = c.getInt(c.getColumnIndex(TASKS_IS_DONE));
        Boolean boolString = boolInt == 1 ? true : false;
        tasks.setIsDone(boolString);

        boolInt = c.getInt(c.getColumnIndex(TASKS_IS_NOTIFY_DEADLINE));
        boolString = boolInt == 1;
        tasks.setIsNotifyDeadline(boolString);

        boolInt = c.getInt(c.getColumnIndex(TASKS_IS_NOTIFY_START_TIME));
        boolString = boolInt == 1;
        tasks.setIsNotifyStartTime(boolString);
        tasks.setDescription(c.getString(c.getColumnIndex(TASKS_DESCRIPTION)));

        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(c.getLong(c.getColumnIndex(TASKS_DEADLINE)));
        tasks.setDeadline(cal);

        getSubtasks(tasks);
        return tasks;
    }


    /** RECEIVE LIST OF TASKS
     *
     * @return
     */

    public List<TaskModel>
    getTaskModelList() {
        List<TaskModel> tasksArrayList = new ArrayList<TaskModel>();

        String selectQuery = "SELECT * FROM " + TABLE_TASKS;
        Log.d(TAG, selectQuery);

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (c.moveToFirst()) {
            do {
                TaskModel task = new TaskModel();
                task = createTaskByCursor(c);

                // adding to Task list
                tasksArrayList.add(task);
            } while (c.moveToNext());
        }
        return tasksArrayList;
    }

    // RECEIVE LIST OF COURSES

    public List<CourseModel> getCourseModelList() throws Exception {
        List<CourseModel> courseArrayList = new ArrayList<CourseModel>();
        String selectQuery = "SELECT * FROM " + TABLE_COURSES;
        Log.d(TAG, selectQuery);

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(selectQuery, null);

        if (c.moveToFirst()) {
            do {
                CourseModel course = new CourseModel();
                course.setId(c.getLong(c.getColumnIndex(COURSE_ID)));
                course.setName(c.getString(c.getColumnIndex(COURSE_NAME)));
                course.fromIntToPriority(c.getInt(c.getColumnIndex(COURSE_PRIORITY)));
                courseArrayList.add(course);
            } while (c.moveToNext());
        }
        return courseArrayList;
    }


    // SETTING METHODS

    // return object of class SettingModel with all settings
    public SettingsModel getAllSettings() {
        SQLiteDatabase db = this.getReadableDatabase();
        SettingsModel settings = new SettingsModel();



        String selectQuery = "SELECT * FROM" + TABLE_SETTINGS;
        Log.d(TAG, selectQuery);

        Cursor c = db.rawQuery(selectQuery, null);
        try {
            if (c.moveToFirst()) {
                settings.setAlwaysNotifyDeadLine(Boolean.getBoolean(c.getString(c.getColumnIndex(SETTINGS_ALWAYS_NOTIFY_START_TIME))));
                settings.setAlwaysNotifyDeadLine(Boolean.getBoolean(c.getString(c.getColumnIndex(SETTINGS_ALWAYS_NOTIFY_DEADLINE))));
                settings.setNotifyStartTimeXTimes(Integer.getInteger(c.getString(c.getColumnIndex(SETTINGS_NOTIFY_START_TIME_X_TIMES))));
                settings.setNotifyDeadLineXTimes(Integer.getInteger(c.getString(c.getColumnIndex(SETTINGS_NOTIFY_DEADLINE_X_TIMES))));
                settings.setNotifyDeadLineBefore(Integer.getInteger(c.getString(c.getColumnIndex(SETTINGS_NOTIFY_DEADLINE_BEFORE))));
                settings.setNotifyStartTimeBefore(Integer.getInteger(c.getString(c.getColumnIndex(SETTINGS_NOTIFY_START_TIME_BEFORE))));

            }
        } catch (Exception e) {
            Log.d(TAG, "Error while trying to get settings from database");
        } finally {
            if (c != null && !c.isClosed()) {
                c.close();
            }
        }

        return settings;
    }

    // return string value of particular setting from DB
    public String getSetting(String settingName) {
        SQLiteDatabase db = this.getReadableDatabase();
        String result = "";

        String selectQuery = "SELECT " + settingName + "FROM " + TABLE_SETTINGS;
        Log.d(TAG, selectQuery);

        Cursor c = db.rawQuery(selectQuery, null);
        try {
            if (c.moveToFirst()) {
                result = c.getString(c.getColumnIndex(settingName));
            }
        } catch (Exception e) {
            Log.d(TAG, "Error while trying to get settings from database");
        } finally {
            if (c != null && !c.isClosed()) {
                c.close();
            }
        }

        return result;
    }

    /**
     * Set isDone true for task with id.
     * @param task_id
     * @throws Exception - if more than one instance has been affected
     */
    public void markTaskAsDone(long task_id) throws Exception {
        SQLiteDatabase db = this.getWritableDatabase();
        db.beginTransaction();
        try {
            ContentValues values = new ContentValues();
            values.put(TASKS_IS_DONE, 1);
            if (db.update(TABLE_TASKS, values, TASKS_KEY_ID + " = ?", new String[]{String.valueOf(task_id)}) != 1) {
                throw new Exception("No or more than one row was affected");
            }else{
                db.setTransactionSuccessful();
            }
        } catch (Exception e) {
            Log.d(TAG, "Error while trying to update task to database");
        } finally {
            db.endTransaction();
        }
    }

    /**
     * Create instance of TaskModel and fill by data stored in cursor.
     * @param c - cursor with current position. (Warn! Method doesn't move cursor)
     * @return new TaskModel instance.
     */
    private TaskModel createTaskByCursor(Cursor c){
        if (c == null)
            return null;
        TaskModel task = new TaskModel();

        task.setId(c.getLong(c.getColumnIndex(TASKS_KEY_ID)));
        task.setName(c.getString(c.getColumnIndex(TASKS_NAME)));
        task.setDescription(c.getString(c.getColumnIndex(TASKS_DESCRIPTION)));

        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(c.getLong(c.getColumnIndex(TASKS_DEADLINE)));
        task.setDeadline(cal);
        if(!c.isNull(c.getColumnIndex(TASKS_START_TIME))){
            cal.setTimeInMillis(c.getLong(c.getColumnIndex(TASKS_START_TIME)));
            task.setStartTime(cal);
        }

        if(!c.isNull(c.getColumnIndex(TASKS_DURATION))) {
            task.setDuration(c.getLong(c.getColumnIndex(TASKS_DURATION)));
        }
        if(!c.isNull(c.getColumnIndex(TASKS_IS_NOTIFY_DEADLINE))) {
            task.setIsNotifyDeadline(c.getInt(c.getColumnIndex(TASKS_IS_NOTIFY_DEADLINE)) > 0);
        }
        if(!c.isNull(c.getColumnIndex(TASKS_IS_NOTIFY_START_TIME))) {
            task.setIsNotifyStartTime(c.getInt(c.getColumnIndex(TASKS_IS_NOTIFY_START_TIME)) > 0);
        }
        if(!c.isNull(c.getColumnIndex(TASKS_IS_DONE))) {
            task.setIsDone(c.getInt(c.getColumnIndex(TASKS_IS_DONE)) > 0);
        }

        getSubtasks(task);
        return task;
    }

    /**
     *
     * @param task - master task.
     */
    private void getSubtasks(TaskModel task) {
        List<TaskModel> subtasksArrayList = new ArrayList<TaskModel>();
        List<Long> subtasksIdsArrayList = new ArrayList<>();
        Map<Long, String> subtasks_map = new HashMap<>();

        String selectQuery = "SELECT * FROM " + TABLE_TASKS
                + " WHERE " + TASKS_PARENT_TASK  + " = " + task.getId()
                ;
        Log.d(TAG, selectQuery);

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (c.moveToFirst()) {
            do {
                Log.d(TAG, c.getLong(c.getColumnIndex(TASKS_PARENT_TASK)) + " " + c.getLong(c.getColumnIndex(TASKS_KEY_ID)));
                subtasksIdsArrayList.add(c.getLong(c.getColumnIndex(TASKS_KEY_ID)));
            } while (c.moveToNext());
        }
        task.setSubtasks_ids(subtasksIdsArrayList);
    }

    private void fillContentByTask(ContentValues values, TaskModel task) {
        values.put(TASKS_NAME, task.getName());
        values.put(TASKS_DESCRIPTION, task.getDescription());
        values.put(TASKS_DEADLINE, task.getDeadline().getTime().getTime());
        values.put(TASKS_START_TIME, task.getStartTime().getTime().getTime());
        values.put(TASKS_DURATION, task.getDuration());
        values.put(TASKS_IS_NOTIFY_START_TIME,task.getIsNotifyStartTime()?1:0);
        values.put(TASKS_IS_NOTIFY_DEADLINE,task.getIsNotifyDeadline() ? 1 : 0);
        values.put(TASKS_IS_DONE, task.getIsDone()?1:0);
    }

    /**
     * Get tasks by SELECT WHERE startTime between low_date AND high_date
     * @param low_date
     * @param high_date
     * @return list of tasks or null
     */
    private List<TaskModel> getTasksBetweenDates(Calendar low_date, Calendar high_date){
        List<TaskModel> tasksArrayList = new ArrayList<TaskModel>();

        String selectQuery = "SELECT * FROM " + TABLE_TASKS + " WHERE " + TASKS_START_TIME +
                " BETWEEN " + low_date.getTime().getTime() + " AND " + high_date.getTime().getTime();
        Log.d(TAG, selectQuery);

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(selectQuery, null);

        if(c != null) {
            // looping through all rows and adding to list
            if (c.moveToFirst()) {
                do {
                    TaskModel task = new TaskModel();
                    task = createTaskByCursor(c);

                    // adding to Task list
                    tasksArrayList.add(task);
                } while (c.moveToNext());
            }
            return tasksArrayList;
        }else
            return null;
    }

    /**
     * Get all tasks that's starttime in cur_time < x < 23:59:59 of given date.
     * @return
     */
    public List<TaskModel> getTasksForToday(){

        Calendar low_date = Calendar.getInstance();
        Calendar high_date = Calendar.getInstance();

        high_date.set(Calendar.HOUR_OF_DAY, 23);
        high_date.set(Calendar.MINUTE, 59);
        high_date.set(Calendar.SECOND, 59);

        return getTasksBetweenDates(low_date,high_date);
    }

    /**
     * Get all tasks that's starttime in 00:00:00 < cur_time < 23:59:59 of given date.
     * @param date
     * @return
     */
    public List<TaskModel> getTasksForDay(Calendar date){

        Calendar low_date = Calendar.getInstance();
        Calendar high_date = Calendar.getInstance();

        low_date.setTime(date.getTime());
        low_date.set(Calendar.HOUR_OF_DAY, 0);
        low_date.set(Calendar.MINUTE,0);
        low_date.set(Calendar.SECOND, 0);

        high_date.setTime(date.getTime());
        high_date.set(Calendar.HOUR_OF_DAY, 23);
        high_date.set(Calendar.MINUTE, 59);
        high_date.set(Calendar.SECOND, 59);

        return getTasksBetweenDates(low_date,high_date);
    }

    /**
     * @param t
     */
    private void updateSubtasks(TaskModel t){
        // UPDATE PARENT_ID = NULL
        SQLiteDatabase db = getWritableDatabase();
        db.beginTransaction();
        String where = TASKS_PARENT_TASK + " = " + t.getId();
        try {
            ContentValues values = new ContentValues();
            values.putNull(TASKS_PARENT_TASK);
            db.update(TABLE_TASKS, values, where, null);
            db.setTransactionSuccessful();
        } catch (Exception e) {
            Log.d(TAG, "Error while trying to set null for tasks with parent_id = " + t.getId() + ". "+ e.toString());
        } finally {
            db.endTransaction();
        }

        // UPDATE PARENT_ID
        // Begin Transaction
        db.beginTransaction();
        try {
            // Creating content values
            ContentValues values = new ContentValues();
            values.put(TASKS_PARENT_TASK, t.getId());
            for(Long slave_id : t.getSubtasks_ids()){
                String wheres = TASKS_KEY_ID + " = " + slave_id;
                db.update(TABLE_TASKS, values, wheres, null);
            }
            db.setTransactionSuccessful();
        } catch (Exception e) {
            Log.d(TAG, "Error while trying to set parent task " + e.toString());
            e.printStackTrace();
        } finally {
            db.endTransaction();
        }
    }
}

