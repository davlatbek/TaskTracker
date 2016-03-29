package com.projectse.aads.task_tracker;

import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

import com.projectse.aads.task_tracker.DBService.DatabaseHelper;
import com.projectse.aads.task_tracker.Fragments.ActualTasksFragment;
import com.projectse.aads.task_tracker.Fragments.CourseOverviewFragment;
import com.projectse.aads.task_tracker.Fragments.CoursesFragment;
import com.projectse.aads.task_tracker.Fragments.PlanFragment;
import com.projectse.aads.task_tracker.Fragments.ProgressFragment;
import com.projectse.aads.task_tracker.Fragments.SettingsFragment;
import com.projectse.aads.task_tracker.Fragments.TaskCategoriesFragment;
import com.projectse.aads.task_tracker.Fragments.WeeklyViewFragment;
import com.projectse.aads.task_tracker.Interfaces.ActualTasksCaller;
import com.projectse.aads.task_tracker.Interfaces.AddTaskCaller;
import com.projectse.aads.task_tracker.Models.TaskModel;

import java.util.Calendar;

/**
 * Created by Andrey Zolin on 20.03.2016.
 */
public class MainActivity
        extends AppCompatActivity
        implements WeeklyViewFragment.onWeekViewEventListener, CoursesFragment.onCourseClickListener,
        AddTaskCaller, ActualTasksCaller {
    DatabaseHelper db;
    private DrawerLayout menuDrawer;
    private android.support.v7.widget.Toolbar toolbar;
    private NavigationView nvDrawer;
    // public DatabaseHelper db = new DatabaseHelper(this); // need for fragments
    private ActionBarDrawerToggle drawerToggle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Set a toolbar to replace the Actionbar
        toolbar = (android.support.v7.widget.Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Set drawer menu
        menuDrawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawerToggle = setupDrawerToggle();
        nvDrawer = (NavigationView) findViewById(R.id.nvView);

        menuDrawer.setDrawerListener(drawerToggle);
        setupDrawerContent(nvDrawer);

        db = DatabaseHelper.getsInstance(getApplicationContext());
        PlugActivity.initDebugData(db);
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
                fragmentClass = ProgressFragment.class;
                break;
            case R.id.nav_settings_fragment:
                fragmentClass = SettingsFragment.class;
                break;
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
        fragmentManager.beginTransaction().replace(R.id.flContent, fragment).commit();
        fragmentManager.executePendingTransactions();
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

    /**************************************
     * TASK ACTIVITY
     ************************************************/

    public void callAddTaskActivity() {
        Intent intent = new Intent(getApplicationContext(), TaskAddActivity.class);
        startActivity(intent);
    }

    public void callTaskOverviewActivity(TaskModel taskModel) {
        Intent intent = new Intent(getApplicationContext(), TaskOverviewActivity.class);
        intent.putExtra("task_id", taskModel.getId());
        startActivityForResult(intent, 0);
    }

    /************************************
     * WEEK VIEW FRAGMENT
     **********************************************/

    @Override
    public void callPlanFragment(Calendar first_day, int day_of_week) {
        PlanFragment fragment = new PlanFragment();
        setCurrentFragment(fragment);
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
        setCurrentFragment(fragment);
    }

    @Override
    public void callAddTask(long defaultCourseId, Calendar defaultStartTime) {
        Intent intent = new Intent(getApplicationContext(), TaskAddActivity.class);
        startActivity(intent);
    }

    @Override
    public void callActualTasks() {
        ActualTasksFragment fragment = new ActualTasksFragment();
        setCurrentFragment(fragment);
    }
}
