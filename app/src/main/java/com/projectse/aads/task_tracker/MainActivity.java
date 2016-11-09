package com.projectse.aads.task_tracker;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.media.AudioManager;
import android.media.SoundPool;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.projectse.aads.task_tracker.DBService.DatabaseHelper;
import com.projectse.aads.task_tracker.Fragments.ActualTasksFragment;
import com.projectse.aads.task_tracker.Fragments.AddTaskFragment;
import com.projectse.aads.task_tracker.Fragments.CourseOverviewFragment;
import com.projectse.aads.task_tracker.Fragments.CourseProgressFragment;
import com.projectse.aads.task_tracker.Fragments.CoursesFragment;
import com.projectse.aads.task_tracker.Fragments.DoneTasksFragment;
import com.projectse.aads.task_tracker.Fragments.EditOverviewTaskFragment;
import com.projectse.aads.task_tracker.Fragments.EditTaskFragment;
import com.projectse.aads.task_tracker.Fragments.ImportFragment;
import com.projectse.aads.task_tracker.Fragments.OverdueTasksFragment;
import com.projectse.aads.task_tracker.Fragments.PlanFragment;
import com.projectse.aads.task_tracker.Fragments.SettingsFragment;
import com.projectse.aads.task_tracker.Fragments.TaskCategoriesFragment;
import com.projectse.aads.task_tracker.Fragments.WeeklyViewFragment;
import com.projectse.aads.task_tracker.GoogleDrive.GoogleDrive;
import com.projectse.aads.task_tracker.Interfaces.ActualTasksCaller;
import com.projectse.aads.task_tracker.Interfaces.AddTaskCaller;
import com.projectse.aads.task_tracker.Interfaces.DoneTasksCaller;
import com.projectse.aads.task_tracker.Interfaces.EditTaskCaller;
import com.projectse.aads.task_tracker.Interfaces.OverdueTasksCaller;
import com.projectse.aads.task_tracker.Interfaces.TaskOverviewCaller;
import com.projectse.aads.task_tracker.Models.SettingsModel;
import com.projectse.aads.task_tracker.Models.TaskModel;
import com.projectse.aads.task_tracker.Utils.ShPrefUtils;

import net.fortuna.ical4j.data.CalendarBuilder;
import net.fortuna.ical4j.data.ParserException;
import net.fortuna.ical4j.model.Component;
import net.fortuna.ical4j.model.ComponentList;
import net.fortuna.ical4j.model.Property;
import net.fortuna.ical4j.model.PropertyList;
import net.fortuna.ical4j.model.TimeZone;
import net.fortuna.ical4j.model.component.VEvent;
import net.fortuna.ical4j.model.property.Categories;
import net.fortuna.ical4j.model.property.DtEnd;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

/**
 * Created by Andrey Zolin on 20.03.2016.
 */
/*
New features by Ivan Dmitriev.
*/

public class MainActivity
        extends AppCompatActivity
        implements WeeklyViewFragment.onWeekViewEventListener, CoursesFragment.onCourseClickListener,
        AddTaskCaller, ActualTasksCaller, DoneTasksCaller, OverdueTasksCaller, ImportFragment.TaskCategoriesCaller,
         EditTaskCaller, TaskOverviewCaller, EditOverviewTaskFragment.TaskCategoriesCaller

    {
    DatabaseHelper db;
    private DrawerLayout menuDrawer;
    private android.support.v7.widget.Toolbar toolbar;
    private NavigationView nvDrawer;
    private ActionBarDrawerToggle drawerToggle;
    private GoogleDrive drive;
    public static SettingsModel settings = null;

    final int MAX_STREAMS = 1;
    SoundPool sp;
    int soundIdSax;
    int soundIdTaskDone;

    public static Boolean DEBUG = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        sp = new SoundPool(MAX_STREAMS, AudioManager.STREAM_MUSIC, 0);
        soundIdSax = sp.load(this, R.raw.tmz, 1);
        soundIdTaskDone = sp.load(this, R.raw.taskdone, 1);

        //Set a toolbar to replace the Actionbar
        toolbar = (android.support.v7.widget.Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // Set drawer menu
        menuDrawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawerToggle = setupDrawerToggle();
        nvDrawer = (NavigationView) findViewById(R.id.nvView);

        menuDrawer.setDrawerListener(drawerToggle);
        setupDrawerContent(nvDrawer);

        final View.OnClickListener originalToolbarListener = drawerToggle.getToolbarNavigationClickListener();

        getFragmentManager().addOnBackStackChangedListener(new FragmentManager.OnBackStackChangedListener() {
            @Override
            public void onBackStackChanged() {
                if (getFragmentManager().getBackStackEntryCount() > 0) {
                    drawerToggle.setDrawerIndicatorEnabled(false);
                    drawerToggle.setToolbarNavigationClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            getFragmentManager().popBackStack();
                        }
                    });
                } else {
                    drawerToggle.setDrawerIndicatorEnabled(true);
                    drawerToggle.setToolbarNavigationClickListener(originalToolbarListener);
                }
            }
        });

        db = DatabaseHelper.getsInstance(getApplicationContext());
        // Set default locale prog-ly to English (Customer req)
        Locale.setDefault(new Locale("en"));
        setCurrentFragment(new TaskCategoriesFragment());
//        MainActivity.settings = db.getSettings();

//        if(DEBUG && db.getCourseModelList().size() == 0)
//            PlugDebug.initDebugData(db);

        //TEST google api
        //----------------------------------------
//        mGoogleApiClient = new GoogleApiClient.Builder(this)
//                .addApi(Drive.API)
//                .addScope(Drive.SCOPE_FILE)
//                .addConnectionCallbacks(this)
//                .addOnConnectionFailedListener(this)
//                .build();
    }
//        private GoogleApiClient mGoogleApiClient;
//        protected static final int REQUEST_CODE_RESOLUTION = 1;
//
//        @Override
//        protected void onStart() {
//            super.onStart();
//            mGoogleApiClient.connect();
//        }
//
//        @Override
//        public void onConnectionFailed(ConnectionResult connectionResult) {
//            if (connectionResult.hasResolution()) {
//                try {
//                    connectionResult.startResolutionForResult(this, RESOLVE_CONNECTION_REQUEST_CODE);
//                } catch (IntentSender.SendIntentException e) {
//                    // Unable to resolve, message user appropriately
//                }
//            } else {
//                GooglePlayServicesUtil.getErrorDialog(connectionResult.getErrorCode(), this, 0).show();
//            }
//        }
//
//        @Override
//        protected void onActivityResult(int requestCode, int resultCode,
//                                        Intent data) {
//            super.onActivityResult(requestCode, resultCode, data);
//            if (requestCode == REQUEST_CODE_RESOLUTION && resultCode == RESULT_OK) {
//                mGoogleApiClient.connect();
//            }
//        }
        //-------------------------------------
    public void butTestSound_Click(View v){
        Toast toast;
        String ss;
        if (ShPrefUtils.isPlaySounds(this)) {
            ss = "Sounds are enabled!";
            sp.play(soundIdSax, 1, 1, 0, 0, 1);
        } else {
            ss = "Sounds are disabled";
        }
        toast = Toast.makeText(getApplicationContext(), ss, Toast.LENGTH_SHORT);
        toast.show();

    }

    public void btnBackup_Click(View v){
        drive = new GoogleDrive(this);
        drive.backup();
    }

    public void btnRestore_Click(View v){
        drive = new GoogleDrive(this);
        drive.restore();
    }

    private ActionBarDrawerToggle setupDrawerToggle() {
        return new ActionBarDrawerToggle(this, menuDrawer, toolbar, R.string.drawer_open, R.string.drawer_close);
    }

    private void setupDrawerContent(NavigationView navigationView) {
        navigationView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(MenuItem item) {
                        selectDrawerItem(item);
                        return true;
                    }
                }
        );
    }

    public void selectDrawerItem(MenuItem item) {
        Fragment fragment = null;

        Class fragmentClass;

        switch (item.getItemId()) {
            case R.id.nav_tasks_fragment:
                fragmentClass = TaskCategoriesFragment.class;
                break;
            case R.id.nav_plan_fragment:
                fragmentClass = PlanFragment.class;
                break;
            case R.id.nav_weekly_view_fragment:
                fragmentClass = WeeklyViewFragment.class;
                break;
            case R.id.nav_courses_fragment:
                fragmentClass = CoursesFragment.class;
                break;
            case R.id.nav_progress_fragment:
                /*fragmentClass = ProgressFragment.class;*/
                fragmentClass = CourseProgressFragment.class;
                break;
            case R.id.nav_settings_fragment:
                fragmentClass = SettingsFragment.class;
                break;
            case R.id.nav_import_fragment:
                callOpenFileForImport();
                return;

            default:
                fragmentClass = TaskCategoriesFragment.class;
        }

        try {
            fragment = (Fragment) fragmentClass.newInstance();
        } catch (Exception e) {
            e.printStackTrace();
        }

        setCurrentFragment(fragment);

        setTitle(item.getTitle());
        menuDrawer.closeDrawers();
    }

    public void setCurrentFragment(Fragment fragment) {
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        for(int i = 0; i < fragmentManager.getBackStackEntryCount(); ++i) {
            fragmentManager.popBackStack();
        }
        transaction.replace(R.id.flContent, fragment).commit();
        fragmentManager.executePendingTransactions();
    }

    public void setCurrentFragmentAddBackStack(Fragment fragment) {
        FragmentManager fragmentManager = getFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.flContent, fragment).addToBackStack(null).commit();
        fragmentManager.executePendingTransactions();
    }

    @Override
    public void onBackPressed() {
        if (getFragmentManager().getBackStackEntryCount() > 0) {
            getFragmentManager().popBackStack();
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (drawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        switch (item.getItemId()) {
            case android.R.id.home:
                menuDrawer.openDrawer(GravityCompat.START);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        drawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        drawerToggle.onConfigurationChanged(newConfig);
    }

    @Override
    public void callTasksCategory(){
        TaskCategoriesFragment fr = new TaskCategoriesFragment();
        setCurrentFragment(fr);
    }

    /**************************************
     * TASK ACTIVITY
     ************************************************/

    public void callAddTaskActivity() {
        AddTaskFragment addTaskFragment = new AddTaskFragment();
        setCurrentFragmentAddBackStack(addTaskFragment);
    }

    public void callTaskOverviewActivity(TaskModel taskModel) {
        /*Intent intent = new Intent(getApplicationContext(), TaskOverviewActivity.class);
        intent.putExtra("task_id", taskModel.getId());
        startActivityForResult(intent, 0);*/
        callTaskOverview(taskModel);
    }

    /************************************
     * WEEK VIEW FRAGMENT
     **********************************************/

    @Override
    public void callPlanFragment(Calendar first_day, int day_of_week) {
        PlanFragment fragment = new PlanFragment();
        setCurrentFragmentAddBackStack(fragment);
        int i = 5;
        while (i-- > 0) {
            try {
                fragment.setWeek(first_day);
                fragment.setWeekDay(day_of_week);
                break;
            } catch (Exception e) {
            }
        }
    }

    @Override
    public void callCourseOverviewFragment(long course_id) {
        CourseOverviewFragment fragment = new CourseOverviewFragment();
        fragment.setCourseID(course_id);
        setCurrentFragmentAddBackStack(fragment);
    }

    @Override
    public void callAddTask(long defaultCourseId, Calendar defaultStartTime) {
        AddTaskFragment addTaskFragment = new AddTaskFragment();
        Bundle bundle = new Bundle();
        bundle.putLong("course_id", defaultCourseId);
        if (defaultStartTime != null){
            bundle.putLong("default_start_time", defaultStartTime.getTimeInMillis());
        }
        addTaskFragment.setArguments(bundle);
        setCurrentFragmentAddBackStack(addTaskFragment);
    }

    @Override
    public void callAddTask() {
        AddTaskFragment addTaskFragment = new AddTaskFragment();
        setCurrentFragmentAddBackStack(addTaskFragment);
    }

    @Override
    public void callEditTask() {
        EditTaskFragment editTaskFragment = new EditTaskFragment();
        setCurrentFragment(editTaskFragment);
    }

    @Override
    public void callTaskOverview(TaskModel taskModel) {
        EditOverviewTaskFragment taskOverviewFragment = new EditOverviewTaskFragment();
        Bundle bundle = new Bundle();
        bundle.putLong("task_id", taskModel.getId());
        taskOverviewFragment.setArguments(bundle);
        setCurrentFragmentAddBackStack(taskOverviewFragment);
    }

    @Override
    public void callActualTasks() {
        ActualTasksFragment fragment = new ActualTasksFragment();
        setCurrentFragmentAddBackStack(fragment);
    }

    @Override
    public void callDoneTasks() {
        DoneTasksFragment fragment = new DoneTasksFragment();
        setCurrentFragmentAddBackStack(fragment);
    }

    @Override
    public void callOverdueTasks() {
        OverdueTasksFragment fragment = new OverdueTasksFragment();
        setCurrentFragmentAddBackStack(fragment);
    }



    Intent intent;

    public void callOpenFileForImport() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(getString(R.string.import_hint))
                .setTitle(getString(R.string.import_title));
        builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("text/*.ics");
                startActivityForResult(intent, RequestCode.REQ_CODE_OPENFILE);
            }
        });
        builder.setNegativeButton(R.string.cancel, null);
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private Map<String,List<TaskModel>> parse(String path) throws IOException, ParserException {
        FileInputStream fin = new FileInputStream(path);
        CalendarBuilder builder = new CalendarBuilder();
        net.fortuna.ical4j.model.Calendar calendar = builder.build(fin);

        ComponentList listEvent = calendar.getComponents(Component.VEVENT);
        Map<String,List<TaskModel>> map = new HashMap<>();

        for (Object elem : listEvent) {
            VEvent event = (VEvent) elem;
            String cousre_name = null;

            String title = event.getSummary().getValue();
            String description = event.getDescription().getValue();

            PropertyList categories = event.getProperties(Property.CATEGORIES);
            for(Property c : categories){
                if(c instanceof Categories){
                    cousre_name = c.getValue();
                    if(!map.containsKey(cousre_name)) {
                        map.put(cousre_name,new ArrayList<TaskModel>());
                    }
                }
            }


            TaskModel task = new TaskModel();
            task.setName(title);
            task.setDescription(description);
            DtEnd date = event.getEndDate();
            if(date != null){
                TimeZone zone = date.getTimeZone();
                Calendar deadline;
                if(zone != null)
                    deadline = Calendar.getInstance(zone);
                else
                    deadline = Calendar.getInstance();
                deadline.setTime(date.getDate());
                task.setDeadline( deadline );
            }
            if(cousre_name != null)
                (map.get(cousre_name)).add(task);
            else {
                if(!map.containsKey("UNSET")) {
                    map.put("UNSET", new ArrayList<TaskModel>());
                }
                (map.get("UNSET")).add(task);
            }
            Log.d("OPENED",title + " : " + description);
        }
        return map;
    }

    public void ICalToData(Uri currFileURI){
        Map<String,List<TaskModel>> map = null;
        try {
//            List<String> text = readFile(currFileURI.getPath());
            map = parse(currFileURI.getPath());
        } catch (IOException e) {
            Toast.makeText(this,"Cannot read from file: "+e.getMessage(),Toast.LENGTH_LONG);
            e.printStackTrace();
        } catch (ParserException e) {
            Toast.makeText(this,"Cannot parse iCal: "+e.getMessage(),Toast.LENGTH_LONG);
            e.printStackTrace();
        }
        if (map == null)
            return;
        ImportFragment fr = new ImportFragment();
        setCurrentFragment(fr);
        fr.setData(map);
        menuDrawer.closeDrawers();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case RequestCode.REQ_CODE_WIZZARD:
                    // do something
                    break;
                case RequestCode.REQ_CODE_OPENFILE:
                    Uri currFileURI = data.getData();
                    Log.d("FILE",currFileURI.getPath());
                    ICalToData(currFileURI);
                    break;
                case RequestCode.REQUEST_CODE_RESOLUTION:
                    if(GoogleDrive.isBackup) {
                        drive.backup();
                    }else{
                        drive.restore();
                    }
            }
        }
    }

}
