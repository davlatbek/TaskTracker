package com.projectse.aads.task_tracker.Adapters;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.ArrayAdapter;
import android.widget.BaseExpandableListAdapter;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.projectse.aads.task_tracker.Models.TaskModel;
import com.projectse.aads.task_tracker.R;

import java.util.List;

/**
 * Class is used in WeekViewFragment for showing supertasks for each day
 */
public class DayPlanOverviewAdapter extends ArrayAdapter<TaskModel> {

    public DayPlanOverviewAdapter(Context context, int resource, List<TaskModel> objects) {
        super(context, resource, objects);
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.list_item_small_task, null);
        }

        TextView textView = (TextView) convertView.findViewById(R.id.txtSuperTaskName);
        final TaskModel task = (TaskModel)getItem(position);

        String label = task.toString();
        if(task.isSupertask() && task.getSubtasks_ids().size() > 0)
            label += " (" + task.getSubtasks_ids().size() + ")";
        textView.setText(label);
        textView.setTextColor(Color.BLACK);

        if(task.isSupertask())
            setPriority(convertView.findViewById(R.id.priority), task.getPriority());

        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(v.getParent() != null) {
                    ViewParent parent = v.getParent().getParent();
                    if (parent instanceof RelativeLayout && ((RelativeLayout) parent).hasOnClickListeners()) {
                        ((RelativeLayout) parent).callOnClick();
                    }
                }
            }
        });

        return convertView;
    }

    void setPriority(View viewById, TaskModel.Priority priority){
        switch (priority){
            case HIGH:
                viewById.setBackgroundResource(R.color.hignPriority);
                break;
            case MEDIUM:
                viewById.setBackgroundResource(R.color.mediumPriority);
                break;
            case LOW:
                viewById.setBackgroundResource(R.color.lowPriority);
                break;
        }

    }
}
