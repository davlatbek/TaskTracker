package com.projectse.aads.task_tracker.Fragments;

import android.app.Activity;
import android.app.Fragment;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.projectse.aads.task_tracker.Interfaces.ActualTasksCaller;
import com.projectse.aads.task_tracker.Interfaces.AddTaskCaller;
import com.projectse.aads.task_tracker.Interfaces.DoneTasksCaller;
import com.projectse.aads.task_tracker.Interfaces.OverdueTasksCaller;
import com.projectse.aads.task_tracker.R;
import com.projectse.aads.task_tracker.Utils.ShPrefUtils;

/**
 * Created by Andrey Zolin on 20.03.2016.
 */
public class TaskCategoriesFragment extends Fragment {
    private AddTaskCaller addTaskCaller;
    private ActualTasksCaller actualTasksCaller;
    private DoneTasksCaller doneTasksCaller;
    private OverdueTasksCaller overdueTasksCaller;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        if (activity instanceof AddTaskCaller) {
            addTaskCaller = (AddTaskCaller) activity;
        }
        if (activity instanceof ActualTasksCaller) {
            actualTasksCaller = (ActualTasksCaller) activity;
        }
        if (activity instanceof DoneTasksCaller) {
            doneTasksCaller = (DoneTasksCaller) activity;
        }
        if(activity instanceof OverdueTasksCaller) {
            overdueTasksCaller = (OverdueTasksCaller) activity;
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        getActivity().setTitle("Tasks");
        View view = inflater.inflate(R.layout.fragment_tasks, container, false);

        final int MAX_STREAMS = 1;
        final SoundPool sp;
        final int sndActual,sndAddnew,sndDone,sndOverdue;

        sp = new SoundPool(MAX_STREAMS, AudioManager.STREAM_MUSIC, 0);
        sndActual = sp.load(getActivity(), R.raw.actual, 1);
        sndAddnew = sp.load(getActivity(), R.raw.addnew, 1);
        sndDone = sp.load(getActivity(), R.raw.done, 1);
        sndOverdue = sp.load(getActivity(), R.raw.overdue, 1);

        // mapping correction
        LinearLayout buttons_layout = (LinearLayout) view.findViewById(R.id.tasks_categories_buttons);
        int width = getActivity().getResources().getDisplayMetrics().widthPixels;
        buttons_layout.setLayoutParams(new LinearLayout.LayoutParams(width, width));

        final RelativeLayout addTaskButton = (RelativeLayout) view.findViewById(R.id.add_task_btn);
        addTaskButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                addTaskCaller.callAddTask(-1, null);
                if (ShPrefUtils.isPlaySounds(getActivity())) {
                    sp.play(sndAddnew, 1, 1, 0, 0, 1);
                }
                addTaskCaller.callAddTask();
            }
        });

        RelativeLayout actualTasksButton = (RelativeLayout) view.findViewById(R.id.actual_btn);
        actualTasksButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ShPrefUtils.isPlaySounds(getActivity())) {
                    sp.play(sndActual, 1, 1, 0, 0, 1);
                }
                actualTasksCaller.callActualTasks();
            }
        });

        RelativeLayout doneTasksButton = (RelativeLayout) view.findViewById(R.id.done_btn);
        doneTasksButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ShPrefUtils.isPlaySounds(getActivity())) {
                    sp.play(sndDone, 1, 1, 0, 0, 1);
                }
                doneTasksCaller.callDoneTasks();
            }
        });

        RelativeLayout overdueTasksButton = (RelativeLayout) view.findViewById(R.id.over_due_btn);
        overdueTasksButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ShPrefUtils.isPlaySounds(getActivity())) {
                    sp.play(sndOverdue, 1, 1, 0, 0, 1);
                }
                overdueTasksCaller.callOverdueTasks();
            }
        });

        return view;
    }
}
