package com.projectse.aads.task_tracker;

import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

import com.projectse.aads.task_tracker.DBService.DatabaseHelper;
import com.projectse.aads.task_tracker.Interfaces.WizzardManager;
import com.projectse.aads.task_tracker.WizzardFragments.IntroFragment;

/**
 * Created by smith on 4/19/16.
 */
public class WizzardActivity extends AppCompatActivity implements WizzardManager {
    private android.support.v7.widget.Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wizzard);

        //Set a toolbar to replace the Actionbar
        toolbar = (android.support.v7.widget.Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setCurrentFragment(new IntroFragment());
    }

    public void setCurrentFragment(Fragment fragment) {
        FragmentManager fragmentManager = getFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.flContent, fragment).commit();
        fragmentManager.executePendingTransactions();
    }

    @Override
    public void closeWizzard() {
        Intent intent = new Intent();
//        intent.putExtra() if it's needed
        setResult(RESULT_OK, intent);
        finish();
    }

    @Override
    public void callWeekFragment() {
        throw new InternalError("Implement this");
    }

    @Override
    public void callTasksFragment() {
        throw new InternalError("Implement this");
    }

    @Override
    public void callAllocateFragment() {
        throw new InternalError("Implement this");
    }

    @Override
    public void callManualAllocateFragment() {
        throw new InternalError("Implement this");
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                closeWizzard();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
