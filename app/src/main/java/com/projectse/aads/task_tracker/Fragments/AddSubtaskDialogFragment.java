package com.projectse.aads.task_tracker.Fragments;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Switch;

import com.projectse.aads.task_tracker.DBService.DatabaseHelper;
import com.projectse.aads.task_tracker.Models.TaskModel;
import com.projectse.aads.task_tracker.R;
import com.projectse.aads.task_tracker.RequestCode;
import com.projectse.aads.task_tracker.TaskAddActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Davlatbek Isroilov on 4/7/2016.
 * Innopolis University
 */
public class AddSubtaskDialogFragment extends DialogFragment{
    DatabaseHelper db;
    Button cancelButton, saveButton;
    Switch switchSubtaskdone;
    EditText editTextSubtaskDetails;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_add_subtask_dialog, container, false);
        //getDialog().setTitle("Add Subtask Dialog");
        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        db = DatabaseHelper.getsInstance(getActivity());
        getView(rootView);

        Bundle mArgs = getArguments();
        if (mArgs != null) {
            final Long subtask_id = mArgs.getLong("subtask_id");
            TaskModel taskModel = db.getTask(subtask_id);
            editTextSubtaskDetails.setText(taskModel.getName());
            switchSubtaskdone.setChecked(taskModel.getIsDone());
            saveButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent ();
                    intent.putExtra("subtask_id", subtask_id);
                    intent.putExtra("subtask_name", editTextSubtaskDetails.getText().toString());
                    intent.putExtra("subtask_isdone", switchSubtaskdone.isChecked());
                    getTargetFragment().onActivityResult(getTargetRequestCode(), RequestCode.REQ_CODE_VIEWTASK, intent);
                    dismiss();
                }
            });
        }

        return rootView;
    }

    public void getView(View rootView){
        editTextSubtaskDetails = (EditText) rootView.findViewById(R.id.editTextSubtaskDetails);
        switchSubtaskdone = (Switch) rootView.findViewById(R.id.switchDoneSubtask);
        cancelButton = (Button) rootView.findViewById(R.id.buttonCancelSubtask);
        saveButton = (Button) rootView.findViewById(R.id.buttonSaveSubtask);
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent ();
                intent.putExtra("subtask_details", editTextSubtaskDetails.getText().toString());
                getTargetFragment().onActivityResult(getTargetRequestCode(), RequestCode.REQ_CODE_ADDTASK, intent);
                dismiss();
            }
        });
    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        super.onDismiss(dialog);
    }
}
