package com.projectse.aads.task_tracker.DBService;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.ContactsContract;
import android.util.Log;

import com.projectse.aads.task_tracker.Models.TaskModel;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

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
    private DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
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
    private static final String TASKS_SUB_TASKS = "task_sub_task";

    // All keys used in table COURSES
    private static final String COURSES_NAME = "course_name";
    private static final String COURSES_ID = "course_id";
    private static final String COURSES_PRIORITY = "course_priority";

    // All keys used in table SETTINGS
    private static final String SETTINGS_ALWAYS_NOTIFY_START_TIME = "setting_always_notify_start_time";
    private static final String SETTINGS_ALWAYS_NOTIFY_DEADLINE = "setting_always_notify_deadline";
    private static final String SETTINGS_NOTIFY_START_TIME_BEFORE = "setting_notify_start_time_before";
    private static final String SETTINGS_NOTIFY_DEADLINE_BEFORE = "setting_notify_deadline_before";
    private static final String SETTINGS_NOTIFY_START_TIME_S_TIMES = "setting_notify_start_time_x_times";
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


    public static String TAG = "tag";


    // Tasks Table Create Query
    /**
     * CREATE TABLE tasks (task_id INTEGER PRIMARY KEY AUTOINCREMENT, task_name TEXT,
     * task_description TEXT)
     */

    private static final String CREATE_TABLE_TASKS = "CREATE TABLE "
            + TABLE_TASKS + "(" + TASKS_KEY_ID
            + " INTEGER PRIMARY KEY AUTOINCREMENT," + TASKS_NAME + " TEXT,"
            + TASKS_DESCRIPTION + " TEXT," + TASKS_DEADLINE + " INTEGER);";

//    public DatabaseHelper(Context context) {
//        super(context,DATABASE_NAME,null,DATABASE_VERSION);
//    }

    /**
     * This method is called by system if the database is accessed but not yet
     * created.
     *
     * @param db
     */

    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL(CREATE_TABLE_TASKS); // create tasks table

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

            db.execSQL("DROP TABLE IF EXISTS " + CREATE_TABLE_TASKS); // drop table if exists
            onCreate(db);
        }


    }

    /**
     * This method is used to add task in task table
     *
     * @param task
     * @return
     */
    public void addTask(TaskModel task) {
        SQLiteDatabase db = this.getWritableDatabase();


        // Begin Transaction
        db.beginTransaction();
        try {
            // Creating content values
            ContentValues values = new ContentValues();
            values.put(TASKS_NAME, task.getName());
            values.put(TASKS_DEADLINE, task.getStartTime().getTime().getTime());
            //values.put(TASKS_DESCRIPTION, task.description);
            db.insertOrThrow(TABLE_TASKS, null, values);
            db.setTransactionSuccessful();
        } catch (Exception e) {
            Log.d(TAG, "Error while trying to add task to database");
        } finally {
            db.endTransaction();
        }

    }

    // Add UpDateEntry

    public int updateEntry(TaskModel task) {
        SQLiteDatabase db = this.getWritableDatabase();
        // update row in task table base on task.is value
        ContentValues values = new ContentValues();
        values.put(TASKS_NAME, task.getName());
        values.put(TASKS_DEADLINE, task.getDeadline().getTime().getTime());

        return db.update(TABLE_TASKS, values, TASKS_KEY_ID + " = ?",
                new String[]{String.valueOf(task.getId())});
    }

    // Add Delete method

    public void deleteEntry(long id) {
        // delete row in task table based on id
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_TASKS, TASKS_KEY_ID + " = ?", new String[]{String.valueOf(id)});

    }

    // Get task object by id
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
        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(c.getLong(c.getColumnIndex(TASKS_DEADLINE)));
        tasks.setDeadline(cal);

        return tasks;
    }

    // RECEIVE LIST OF TASKS
    public List<TaskModel> getTaskModelList() {
        List<TaskModel> tasksArrayList = new ArrayList<TaskModel>();

        String selectQuery = "SELECT * FROM " + TABLE_TASKS;
        Log.d(TAG, selectQuery);

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (c.moveToFirst()) {
            do {
                TaskModel tasks = new TaskModel();
                tasks.setId(c.getLong(c.getColumnIndex(TASKS_KEY_ID)));
                tasks.setName(c.getString(c.getColumnIndex(TASKS_NAME)));
                Calendar cal = Calendar.getInstance();
                cal.setTimeInMillis(c.getLong(c.getColumnIndex(TASKS_DEADLINE)));
                tasks.setDeadline(cal);

                // adding to Task list
                tasksArrayList.add(tasks);
            } while (c.moveToNext());
        }
        return tasksArrayList;
    }

    /**
     * Gets a day as an input and returns list of tasks for a randomly chosen week
     *
     * @param day Choose day starting from which tasks will be returned for a week
     * @return List of random week tasks
     */
    public List<TaskModel> getTasksForAChosenWeek(Calendar day) {
        List<TaskModel> tasks = new ArrayList<>();

        //setting starting day time
        day.set(Calendar.HOUR_OF_DAY, 00);
        day.set(Calendar.MINUTE, 00);
        day.set(Calendar.SECOND, 01);

        //setting last day of random week
        Calendar lastDay = day;
        lastDay.add(Calendar.DATE, 7);
        lastDay.set(Calendar.HOUR_OF_DAY, 23);
        lastDay.set(Calendar.MINUTE, 59);
        lastDay.set(Calendar.SECOND, 59);

        Calendar cal = Calendar.getInstance();

        /*String selectQuery = "SELECT * FROM " + TABLE_TASKS + " WHERE "
                + TASKS_START_TIME + " > " + day.getTime().getTime() +
                " AND " + TASKS_DEADLINE + " < " + lastDay.getTime().getTime();*/

        String selectQuery = "SELECT * FROM " + TABLE_TASKS;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(selectQuery, null);

        if (c.moveToFirst()) {
            do {
                TaskModel task = new TaskModel();
                cal.setTimeInMillis(c.getLong(c.getColumnIndex(TASKS_DEADLINE)));
                if (cal.getTime().getTime() >= day.getTime().getTime() &&
                        cal.getTime().getTime() <= lastDay.getTime().getTime()) {
                    task.setId(c.getLong(c.getColumnIndex(TASKS_KEY_ID)));
                    task.setName(c.getString(c.getColumnIndex(TASKS_NAME)));
                    task.setDeadline(cal);
                    tasks.add(task);
                }
            } while (c.moveToNext());
        }
        return tasks;
    }

    /**
     * Returns list of tasks for current week
     *
     * @return List of current week tasks
     */
    public List<TaskModel> getTasksForACurrentWeek() {
        List<TaskModel> tasks = new ArrayList<>();

        //setting last day of current week
        Calendar lastDayOfCurrentWeek = Calendar.getInstance();
        lastDayOfCurrentWeek.add(Calendar.DATE, 7);
        lastDayOfCurrentWeek.set(Calendar.HOUR_OF_DAY, 23);
        lastDayOfCurrentWeek.set(Calendar.MINUTE, 59);
        lastDayOfCurrentWeek.set(Calendar.SECOND, 59);

        Calendar cal = Calendar.getInstance();
        String selectQuery = "SELECT * FROM " + TABLE_TASKS;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(selectQuery, null);

        if (c.moveToFirst()) {
            do {
                TaskModel task = new TaskModel();
                cal.setTimeInMillis(c.getLong(c.getColumnIndex(TASKS_DEADLINE)));
                if (cal.getTime().getTime() <= lastDayOfCurrentWeek.getTime().getTime()) {
                    task.setId(c.getLong(c.getColumnIndex(TASKS_KEY_ID)));
                    task.setName(c.getString(c.getColumnIndex(TASKS_NAME)));
                    task.setDeadline(cal);
                    tasks.add(task);
                }
            } while (c.moveToNext());
        }
        return tasks;
    }

    /**
     *
     * @param parentID The identificator of parent task
     * @param childID The identificator of child task
     * @return
     */
    public boolean AddSubTask(long parentID, long childID) {
        return true;
    }
}