package com.projectse.aads.task_tracker;

import android.app.Activity;
import com.robotium.recorder.executor.Executor;

@SuppressWarnings("rawtypes")
public class MainActivityExecutor extends Executor {

	@SuppressWarnings("unchecked")
	public MainActivityExecutor() throws Exception {
		super((Class<? extends Activity>) Class.forName("com.projectse.aads.task_tracker.MainActivity"),  "com.projectse.aads.task_tracker.R.id.", new android.R.id(), false, false, "1477471807441");
	}

	public void setUp() throws Exception { 
		super.setUp();
	}
}
