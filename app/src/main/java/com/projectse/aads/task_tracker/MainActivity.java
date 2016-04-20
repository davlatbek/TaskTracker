package com.projectse.aads.task_tracker;

import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Intent;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.projectse.aads.task_tracker.DBService.DatabaseHelper;
import com.projectse.aads.task_tracker.Fragments.ActualTasksFragment;
import com.projectse.aads.task_tracker.Fragments.CourseOverviewFragment;
import com.projectse.aads.task_tracker.Fragments.CoursesFragment;
import com.projectse.aads.task_tracker.Fragments.DoneTasksFragment;
import com.projectse.aads.task_tracker.Fragments.ImportFragment;
import com.projectse.aads.task_tracker.Fragments.OverdueTasksFragment;
import com.projectse.aads.task_tracker.Fragments.PlanFragment;
import com.projectse.aads.task_tracker.Fragments.ProgressFragment;
import com.projectse.aads.task_tracker.Fragments.SettingsFragment;
import com.projectse.aads.task_tracker.Fragments.TaskCategoriesFragment;
import com.projectse.aads.task_tracker.Fragments.WeeklyViewFragment;
import com.projectse.aads.task_tracker.Interfaces.ActualTasksCaller;
import com.projectse.aads.task_tracker.Interfaces.AddTaskCaller;
import com.projectse.aads.task_tracker.Interfaces.DoneTasksCaller;
import com.projectse.aads.task_tracker.Interfaces.OverdueTasksCaller;
import com.projectse.aads.task_tracker.Interfaces.WizzardCaller;
import com.projectse.aads.task_tracker.Models.TaskModel;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

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

/**
 * Created by Andrey Zolin on 20.03.2016.
 */
public class MainActivity
        extends AppCompatActivity
        implements WeeklyViewFragment.onWeekViewEventListener, CoursesFragment.onCourseClickListener,
        AddTaskCaller, ActualTasksCaller, DoneTasksCaller, OverdueTasksCaller, ImportFragment.TaskCategoriesCaller,
        WizzardCaller
{
    DatabaseHelper db;
    private DrawerLayout menuDrawer;
    private android.support.v7.widget.Toolbar toolbar;
    private NavigationView nvDrawer;
    private ActionBarDrawerToggle drawerToggle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

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

//        PlugActivity.initDebugData(db);
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
            case R.id.nav_import_fragment:
                callOpenFileForImport();
                return;
            case R.id.nav_wizzard_fragment:
                callWizzard();
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
        fragmentManager.beginTransaction().replace(R.id.flContent, fragment).commit();
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
        Intent intent = new Intent(getApplicationContext(), TaskAddActivity.class);
        startActivity(intent);
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

    public void callImportFragment(){

    }

    @Override
    public void callWizzard() {
        Intent intent = new Intent(getApplicationContext(), WizzardActivity.class);
        startActivityForResult(intent, RequestCode.REQ_CODE_WIZZARD);
    }

    Intent intent;

    public void callOpenFileForImport() {
        intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("text/*.ics");
        startActivityForResult(intent, RequestCode.REQ_CODE_OPENFILE);
    }

    private List<String> readFile(String path) throws IOException {
        File file = new File(path);
        List<String> textt = new ArrayList<>();
        if(file.exists()){
            StringBuilder text = new StringBuilder();

            try {
                BufferedReader br = new BufferedReader(new FileReader(file));
                String line;

                while ((line = br.readLine()) != null) {
                    textt.add(line);
                    text.append(line);
                    text.append('\n');
                }
                br.close();
            }
            catch (IOException e) {
                throw e;
            }
            Log.d("FILEOPENED",text.toString());
            return textt;
        }else
            throw new IOException("File is not exist.");
    }

    private void parse(List<String> text){

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
            }
        }
    }
}
