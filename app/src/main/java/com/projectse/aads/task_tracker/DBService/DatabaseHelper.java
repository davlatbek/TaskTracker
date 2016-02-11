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
public class DatabaseHelper extends SQLiteOpenHelper{

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
     * task_description TEXT, task_deadline INTEGER)
     */

    private static final String CREATE_TABLE_TASKS = "CREATE TABLE "
            + TABLE_TASKS + "("
            + TASKS_KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + TASKS_NAME + " TEXT,"
            + TASKS_DESCRIPTION + " TEXT,"
            + TASKS_IS_DONE + " BOOLEAN,"
            + TASKS_START_TIME + " INTEGER,"
            + TASKS_DEADLINE + " INTEGER,"
            + TASKS_DURATION + " INTEGER,"
            + TASKS_IS_NOTIFY_DEADLINE + " BOOLEAN,"
            + TASKS_IS_NOTIFY_START_TIME + " BOOLEAN);"
            ;

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

        try {
            db.execSQL(CREATE_TABLE_TASKS); // create tasks table
        }catch (Exception e){
            e.printStackTrace();
        }

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
    public long addTask(TaskModel task) {
        SQLiteDatabase db = this.getWritableDatabase();
        long id = 0;

        // Begin Transaction
        db.beginTransaction();
        try {
            // Creating content values
            ContentValues values = new ContentValues();
//            values.put(TASKS_NAME, task.getName());
//            values.put(TASKS_DESCRIPTION, task.getDescription());
//            values.put(TASKS_DEADLINE, task.getDeadline().getTime().getTime());
//            values.put(TASKS_START_TIME, task.getStartTime().getTime().getTime());
//            values.put(TASKS_DURATION, task.getDuration());
//            values.put(TASKS_IS_NOTIFY_START_TIME,task.getIsNotifyStartTime());
//            values.put(TASKS_IS_NOTIFY_DEADLINE,task.getIsNotifyDeadline());
//            values.put(TASKS_IS_DONE, task.getIsDone());
            fillContentByTask(values, task);
            //db.insertOrThrow(TABLE_TASKS,null,values);

            // Return id of the added task
            id = db.insertOrThrow(TABLE_TASKS,null,values);
            db.setTransactionSuccessful();
        } catch (Exception e) {
            Log.d(TAG, "Error while trying to add task to database");
        } finally {
            db.endTransaction();
        }
        return id;

    }

    /** Add UpDateEntry
     *
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
        return res;
    }

    // Delete method. Not tested yet!
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
     * @param task_id
     * @return new TaskModel object or null, if row with id doesn't exist.
     */
    public TaskModel getTask(long task_id) {
        SQLiteDatabase db = this.getReadableDatabase();

        // SELECT * FROM tasks WHERE id = ?;
        String selectQuery = "SELECT * FROM " + TABLE_TASKS + " WHERE "
                + TASKS_KEY_ID + " = " + task_id;
        Log.d(TAG, selectQuery);

        Cursor c = db.rawQuery(selectQuery, null);

        if (c != null){
            try {
                if(c.moveToFirst())
                    return createTaskByCursor(c);
            }catch (Exception e){
                return null;
            }
        }
        return null;
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
                TaskModel task = new TaskModel();
                task = createTaskByCursor(c);

                // adding to Task list
                tasksArrayList.add(task);
            } while (c.moveToNext());
        }
        return tasksArrayList;
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
        high_date.set(Calendar.MINUTE,59);
        high_date.set(Calendar.SECOND, 59);

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
            task.setDeadline(cal);
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

        return task;
    }

    private void fillContentByTask(ContentValues values, TaskModel task){
        values.put(TASKS_NAME, task.getName());
        values.put(TASKS_DESCRIPTION, task.getDescription());
        values.put(TASKS_DEADLINE, task.getDeadline().getTime().getTime());
        values.put(TASKS_START_TIME, task.getStartTime().getTime().getTime());
        values.put(TASKS_DURATION, task.getDuration());
        values.put(TASKS_IS_NOTIFY_START_TIME,task.getIsNotifyStartTime()?1:0);
        values.put(TASKS_IS_NOTIFY_DEADLINE,task.getIsNotifyDeadline()?1:0);
        values.put(TASKS_IS_DONE, task.getIsDone()?1:0);
    }

}
