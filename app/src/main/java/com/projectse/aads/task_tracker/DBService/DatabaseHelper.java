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
        //context.deleteDatabase(DATABASE_NAME);

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
    private static final int DATABASE_VERSION = 2;

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
    private static final String TASKS_SUB_TASKS = "task_sub_task";

    //All keys used in table SUBTASKS
    private static final String SUBTASKS_MASTER_ID = "master_id";
    private static final String SUBTASKS_SLAVE_ID = "slave_id";

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
    private static final String COURSES_TO_TASKS_CURSE_ID = "courses_to_tasks_curse_id";
    private static final String COURSES_TO_TASKS_TASK_ID = "courses_to_tasks_task_id";


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
            + TASKS_IS_DONE + " INTEGER,"
            + TASKS_START_TIME + " INTEGER,"
            + TASKS_DEADLINE + " INTEGER,"
            + TASKS_DURATION + " INTEGER,"
            + TASKS_IS_NOTIFY_DEADLINE + " INTEGER,"
            + TASKS_IS_NOTIFY_START_TIME + " INTEGER);";

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
     * CREATE TABLE TABLE_COURSES_TO_TASKS
     */

    private static final String CREATE_TABLE_COURSES_TO_TASK = "CREATE TABLE "
            + TABLE_COURSES_TO_TASKS + " (" + COURSES_TO_TASKS_TASK_ID + " INTEGER,"
            + COURSES_TO_TASKS_CURSE_ID + " INTEGER DEFAULT 1, "
            + "PRIMARY KEY (" + COURSES_TO_TASKS_TASK_ID + ", " + COURSES_TO_TASKS_CURSE_ID + "), "
            + "FOREIGN KEY(" + COURSES_TO_TASKS_TASK_ID + ") REFERENCES " + TABLE_TASKS + "(" + TASKS_KEY_ID + ") ON UPDATE CASCADE,"
            + "FOREIGN KEY(" + COURSES_TO_TASKS_CURSE_ID + ") REFERENCES " + TABLE_COURSES + "(" + COURSE_ID + ") ON UPDATE CASCADE);";

    /**
     * CREATE TABLE SUBTASKS
     */

    private static final String CREATE_TABLE_SUBTASKS = "CREATE TABLE "
            + TABLE_SUBTASKS + " (" + SUBTASKS_MASTER_ID
            + " INTEGER," + SUBTASKS_SLAVE_ID + " INTEGER,"
            + "FOREIGN KEY(" + SUBTASKS_MASTER_ID + ") REFERENCES " + TABLE_TASKS + "(" + TASKS_KEY_ID + "),"
            + "FOREIGN KEY(" + SUBTASKS_SLAVE_ID + ") REFERENCES " + TABLE_TASKS + "(" + TASKS_KEY_ID + ")"
            + ");";

    private static final String CREATE_TABLE_SETTINGS = "CREATE TABLE" +
            TABLE_SETTINGS + " (" +
            SETTINGS_ALWAYS_NOTIFY_START_TIME + " TEXT," +
            SETTINGS_ALWAYS_NOTIFY_DEADLINE  + " TEXT," +
            SETTINGS_NOTIFY_START_TIME_BEFORE  + " INTEGER," +
            SETTINGS_NOTIFY_DEADLINE_BEFORE  + " INTEGER," +
            SETTINGS_NOTIFY_START_TIME_X_TIMES + " INTEGER," +
            SETTINGS_NOTIFY_DEADLINE_X_TIMES + " INTEGER);";


    /**
     * This method is called by system if the database is accessed but not yet
     * created.
     *
     * @param db
     */

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("PRAGMA foreign_keys=ON"); // Set foreign keys on
        db.execSQL(CREATE_TABLE_TASKS); // create tasks table
        db.execSQL(CREATE_TABLE_COURSES); // create course table
        db.execSQL(CREATE_TABLE_COURSES_TO_TASK); // create course to task table
        db.execSQL(CREATE_TABLE_SUBTASKS); // create subtasks table
        db.execSQL(CREATE_TABLE_SETTINGS); // create settings table
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

    public void deleteTaskTable(SQLiteDatabase db) {
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
            values.put(COURSE_PRIORITY, course.fromPriorityToInt(course.getPriority()));
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

    public boolean deleteCourse(long id) {
        // delete row in course table based on id

        SQLiteDatabase db = this.getWritableDatabase();
        db.beginTransaction();
        String where = COURSE_ID + " = " + id;
        try {
            db.delete(TABLE_COURSES, where, null);
            db.setTransactionSuccessful();
            return true;
        } catch (Exception e) {
            Log.d(TAG, "Error while trying to delete course");
            return false;
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
            values.put(COURSE_PRIORITY, String.valueOf(course.fromPriorityToInt(course.getPriority())));
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

    /**
     * Get task object by id
     *
     * @param id
     * @return TODO check add support of subtasks
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


    /**
     * RECEIVE LIST OF TASKS
     *
     * @return TODO add support of subtasks
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

    // WORK WITH TABLE COURSES TO TASKS

    /**
     * This method used to add relation between task and course
     */

    public long addCourseToTask(long task_id) {
        SQLiteDatabase db = this.getWritableDatabase();
        long id = 0;
        db.beginTransaction();
        try {
            ContentValues values = new ContentValues();
            values.put(COURSES_TO_TASKS_TASK_ID, task_id);
            id = db.insertOrThrow(TABLE_COURSES_TO_TASKS, null, values);
            db.setTransactionSuccessful();
        } catch (Exception e) {
            Log.d(TAG, "Error while adding row in to CourseToTask table");
        } finally {
            db.endTransaction();
        }
        Log.d(TAG, id + " <<<---- Id of coursetotasktable");
        return id;
    }

    /**
     * Update course to task
     */
    public long updateCourseToTask(long task_id, long course_id) {
        SQLiteDatabase db = this.getWritableDatabase();
        int id = 0;
        db.beginTransaction();
        try {
            ContentValues values = new ContentValues();
            values.put(COURSES_TO_TASKS_CURSE_ID, course_id);
            id = db.update(TABLE_COURSES_TO_TASKS, values, COURSES_TO_TASKS_TASK_ID + " = ?",
                    new String[]{String.valueOf(task_id)});
            db.setTransactionSuccessful();
        } catch (Exception e) {
            Log.d(TAG, "Error while trying to update courseToTask update id of Course");
        } finally {
            db.endTransaction();
        }
        Log.d(TAG, id + "<<<<----- update courseToTask update id of Course");
        return id;
    }

    /**
     * Delete row from table course to task by task_id
     *
     * @return
     */

    public void deleteEntryFromCourseToTaskTable(long task_id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.beginTransaction();
        String sq = COURSES_TO_TASKS_TASK_ID + " = " + task_id;
        try {
            db.delete(TABLE_COURSES_TO_TASKS, sq, null);
            db.setTransactionSuccessful();
        } catch (Exception e) {
            Log.d(TAG, "Error while trying to delete row from course to task by task_id");
        } finally {
            db.endTransaction();
        }

    }

    /**
     * Select list of tasks by course id
     *
     * @return List Task
     */

    public List<TaskModel> getListOfTasks(long coures_id) {
        List<TaskModel> taskList = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();
        String sq = "SELECT " + TASKS_KEY_ID + " FROM " + TABLE_COURSES + ", " + TABLE_TASKS + ", "
                + TABLE_COURSES_TO_TASKS + " WHERE " + TABLE_TASKS + "." + TASKS_KEY_ID + " = " + TABLE_COURSES_TO_TASKS +
                "." + COURSES_TO_TASKS_TASK_ID + " AND " + TABLE_COURSES + "." + COURSE_ID + " = " + TABLE_COURSES_TO_TASKS + "." + COURSES_TO_TASKS_CURSE_ID
                + " AND " + COURSES_TO_TASKS_CURSE_ID + " = " + coures_id;

        Log.d(TAG, sq);
        Cursor c = db.rawQuery(sq, null);

        db.beginTransaction();
        try {
            if (c.moveToFirst()) {
                do {
                    taskList.add(getTask(c.getLong(c.getColumnIndex(TASKS_KEY_ID))));
                    Log.d(TAG, "add to list");

                } while (c.moveToNext());
            }
        } catch (Exception e) {
            Log.d(TAG, "Error while trying select tasks by course_id");
        } finally {
            db.endTransaction();
        }

        return taskList;


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

    // update particular setting in database by name and new value
    public int updateSetting(String settingName, String value) {
        int res = 0;
        SQLiteDatabase db = this.getWritableDatabase();
        db.beginTransaction();
        try {
            ContentValues values = new ContentValues();
            values.put(settingName, value);
            res = db.update(TABLE_SETTINGS, values, settingName + "!= ",  new String[]{value});

        } catch (Exception e) {
            Log.d(TAG, "Error while trying to update settings " + settingName + " in database");
        } finally {
            db.endTransaction();
        }

        return res;
    }

    /**
     * Set isDone true for task with id.
     *
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
            } else {
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
     *
     * @param c - cursor with current position. (Warn! Method doesn't move cursor)
     * @return new TaskModel instance.
     */
    private TaskModel createTaskByCursor(Cursor c) {
        if (c == null)
            return null;
        TaskModel task = new TaskModel();

        task.setId(c.getLong(c.getColumnIndex(TASKS_KEY_ID)));
        task.setName(c.getString(c.getColumnIndex(TASKS_NAME)));
        task.setDescription(c.getString(c.getColumnIndex(TASKS_DESCRIPTION)));

        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(c.getLong(c.getColumnIndex(TASKS_DEADLINE)));
        task.setDeadline(cal);
        if (!c.isNull(c.getColumnIndex(TASKS_START_TIME))) {
            cal.setTimeInMillis(c.getLong(c.getColumnIndex(TASKS_START_TIME)));
            task.setStartTime(cal);
        }

        if (!c.isNull(c.getColumnIndex(TASKS_DURATION))) {
            task.setDuration(c.getLong(c.getColumnIndex(TASKS_DURATION)));
        }
        if (!c.isNull(c.getColumnIndex(TASKS_IS_NOTIFY_DEADLINE))) {
            task.setIsNotifyDeadline(c.getInt(c.getColumnIndex(TASKS_IS_NOTIFY_DEADLINE)) > 0);
        }
        if (!c.isNull(c.getColumnIndex(TASKS_IS_NOTIFY_START_TIME))) {
            task.setIsNotifyStartTime(c.getInt(c.getColumnIndex(TASKS_IS_NOTIFY_START_TIME)) > 0);
        }
        if (!c.isNull(c.getColumnIndex(TASKS_IS_DONE))) {
            task.setIsDone(c.getInt(c.getColumnIndex(TASKS_IS_DONE)) > 0);
        }

        getSubtasks(task);
        return task;
    }

    /**
     * TODO test
     *
     * @param task - master task.
     */
    private void getSubtasks(TaskModel task) {
        List<TaskModel> subtasksArrayList = new ArrayList<TaskModel>();
        List<Long> subtasksIdsArrayList = new ArrayList<>();
        Map<Long, String> subtasks_map = new HashMap<>();

        String selectQuery = "SELECT * FROM " + TABLE_SUBTASKS
                + " WHERE " + SUBTASKS_MASTER_ID + " = " + task.getId();
        Log.d(TAG, selectQuery);

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (c.moveToFirst()) {
            do {
                Log.d(TAG, c.getLong(c.getColumnIndex(SUBTASKS_MASTER_ID)) + " " + c.getLong(c.getColumnIndex(SUBTASKS_SLAVE_ID)));
                subtasksIdsArrayList.add(c.getLong(c.getColumnIndex(SUBTASKS_SLAVE_ID)));
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
        values.put(TASKS_IS_NOTIFY_START_TIME, task.getIsNotifyStartTime() ? 1 : 0);
        values.put(TASKS_IS_NOTIFY_DEADLINE, task.getIsNotifyDeadline() ? 1 : 0);
        values.put(TASKS_IS_DONE, task.getIsDone() ? 1 : 0);
    }

    /**
     * Get tasks by SELECT WHERE startTime between low_date AND high_date
     *
     * @param low_date
     * @param high_date
     * @return list of tasks or null
     */
    private List<TaskModel> getTasksBetweenDates(Calendar low_date, Calendar high_date) {
        List<TaskModel> tasksArrayList = new ArrayList<TaskModel>();

        String selectQuery = "SELECT * FROM " + TABLE_TASKS + " WHERE " + TASKS_START_TIME +
                " BETWEEN " + low_date.getTime().getTime() + " AND " + high_date.getTime().getTime();
        Log.d(TAG, selectQuery);

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(selectQuery, null);

        if (c != null) {
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
        } else
            return null;
    }

    /**
     * Get all tasks that's starttime in cur_time < x < 23:59:59 of given date.
     *
     * @return
     */
    public List<TaskModel> getTasksForToday() {

        Calendar low_date = Calendar.getInstance();
        Calendar high_date = Calendar.getInstance();

        high_date.set(Calendar.HOUR_OF_DAY, 23);
        high_date.set(Calendar.MINUTE, 59);
        high_date.set(Calendar.SECOND, 59);

        return getTasksBetweenDates(low_date, high_date);
    }

    /**
     * Get all tasks that's starttime in 00:00:00 < cur_time < 23:59:59 of given date.
     *
     * @param date
     * @return
     */
    public List<TaskModel> getTasksForDay(Calendar date) {

        Calendar low_date = Calendar.getInstance();
        Calendar high_date = Calendar.getInstance();

        low_date.setTime(date.getTime());
        low_date.set(Calendar.HOUR_OF_DAY, 0);
        low_date.set(Calendar.MINUTE, 0);
        low_date.set(Calendar.SECOND, 0);

        high_date.setTime(date.getTime());
        high_date.set(Calendar.HOUR_OF_DAY, 23);
        high_date.set(Calendar.MINUTE, 59);
        high_date.set(Calendar.SECOND, 59);

        return getTasksBetweenDates(low_date, high_date);
    }

    /**
     * TODO test
     *
     * @param t
     */
    private void updateSubtasks(TaskModel t) {
        //DELETE where master_id = t.id
        SQLiteDatabase db = getWritableDatabase();
        db.beginTransaction();
        String where = SUBTASKS_MASTER_ID + " = " + t.getId();
        try {
            db.delete(TABLE_SUBTASKS, where, null);
            db.setTransactionSuccessful();
        } catch (Exception e) {
            Log.d(TAG, "Error while trying to delete subtasks for master.id = " + t.getId() + ".");
        } finally {
            db.endTransaction();
        }

        //INSERT master_id, subt.id
        // Begin Transaction
        db.beginTransaction();
        try {
            for (Long slave_id : t.getSubtasks_ids()) {
                // Creating content values
                ContentValues values = new ContentValues();
                values.put(SUBTASKS_MASTER_ID, t.getId());
                values.put(SUBTASKS_SLAVE_ID, slave_id);
                db.insert(TABLE_SUBTASKS, null, values);
            }
            db.setTransactionSuccessful();
        } catch (Exception e) {
            Log.d(TAG, "Error while trying to add task to database " + e.toString());
            e.printStackTrace();
        } finally {
            db.endTransaction();
        }
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
}

