package com.projectse.aads.task_tracker.Dialogs;

import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.TextView;

import com.projectse.aads.task_tracker.AddTaskActivity;
import com.projectse.aads.task_tracker.DBService.DatabaseHelper;
import com.projectse.aads.task_tracker.Models.TaskModel;
import com.projectse.aads.task_tracker.RequestCode;
import com.projectse.aads.task_tracker.TaskActivity;
import com.projectse.aads.task_tracker.TaskEditActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by smith on 2/25/16.
 */
public class AddSubtaskDialog extends DialogFragment {

    public Activity parent = null;
    List<TaskModel> candidates = new ArrayList<>();
    ListAdapter adapter_candidates = null;
    TaskModel stored_task = null;
    DatabaseHelper db = null;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        db = DatabaseHelper.getsInstance(parent);
        stored_task = ((TaskActivity)parent).task;

//			ListView listview = (ListView) findViewById(R.id.listview);

        final Button btn = new Button(parent.getApplicationContext());

        btn.setText("Create new task");

        // Set click listener for button
        btn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                callAddTaskActivity();
            }
        });

        LinearLayout l = new LinearLayout(parent.getApplicationContext());
        l.setOrientation(LinearLayout.VERTICAL);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                ActionBar.LayoutParams.MATCH_PARENT, ActionBar.LayoutParams.MATCH_PARENT);

        TextView t = new TextView(parent.getApplicationContext());
        t.setText("Add subtask.");
        t.setTextSize(25);
        t.setTextColor(Color.BLACK);
        t.setLayoutParams(params);
        btn.setLayoutParams(params);
        l.addView(t);
        l.addView(btn);
        // Inflate and set the layout for the dialog
        List<Long> candidates_ids = db.getSubtasksCandidates(stored_task.getId());
        candidates.clear();
        for(Long id : candidates_ids){
            candidates.add(db.getTask(id));
        }

        adapter_candidates = new ArrayAdapter<TaskModel>(getActivity(),android.R.layout.simple_list_item_1, candidates);

        // Pass null as the parent view because its going in the dialog layout
        builder
//					.setView(inflater.inflate(R.layout.dialog, null))
//					.setView(btn)
                .setCustomTitle(l)
//					.setTitle("Add Subtask")
                .setAdapter(adapter_candidates, new DialogInterface.OnClickListener() {

                    public synchronized void onClick(DialogInterface dialog, int which) {
                        TaskModel item = (TaskModel) adapter_candidates.getItem(which);
                        mListener.onDialogDismiss(AddSubtaskDialog.this, item);
//                        ((TaskEditActivity)parent).onResume();
//                            dismiss();
                    }

                });
        return builder.create();
    }

    public interface NoticeDialogListener {
        public void onDialogDismiss(DialogFragment dialog, TaskModel item);
    }


    NoticeDialogListener mListener;

    // Override the Fragment.onAttach() method to instantiate the NoticeDialogListener
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        // Verify that the host activity implements the callback interface
        try {
            // Instantiate the NoticeDialogListener so we can send events to the host
            mListener = (NoticeDialogListener) activity;
        } catch (ClassCastException e) {
            // The activity doesn't implement the interface, throw exception
            throw new ClassCastException(activity.toString()
                    + " must implement NoticeDialogListener");
        }
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(resultCode == parent.RESULT_OK){
            switch (requestCode){
                case RequestCode.REQ_CODE_ADDTASK:
                    TaskModel item = db.getTask(data.getLongExtra("task_id",-1));
                    mListener.onDialogDismiss(AddSubtaskDialog.this, item);
//                        ((TaskEditActivity)parent).onResume();
                    dismiss();
                    break;
            }
        }
    }

    public synchronized void callAddTaskActivity(){
        Intent intent = new Intent (parent.getApplicationContext(), AddTaskActivity.class);
        intent.putExtra("parent_id",stored_task.getId());
        intent.putExtra("hide_subtasks",true);
        startActivityForResult(intent, RequestCode.REQ_CODE_ADDTASK);
    }

}