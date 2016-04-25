package com.projectse.aads.task_tracker.Adapters;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.ArrayAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.projectse.aads.task_tracker.Models.TaskModel;
import com.projectse.aads.task_tracker.R;

import java.util.List;

/**
 * Class is used in WeekViewFragment for showing supertasks for each day
 */
public class TaskStackAdapter extends ArrayAdapter<TaskModel> {
    List<TaskModel> objects;

    public TaskStackAdapter(Context context, int resource, List<TaskModel> objects) {
        super(context, resource, objects);
        this.objects = objects;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.plan_list_supertask_view, null);
        }

        TextView textView = (TextView) convertView.findViewById(R.id.txtSuperTaskName);
        final TaskModel task = getItem(position);

        String label = task.toString();
        if(task.isSupertask() && task.getSubtasks_ids().size() > 0)
            label += " (" + task.getSubtasks_ids().size() + ")";
        textView.setText(label);
        textView.setTextColor(Color.BLACK);

        RelativeLayout back = (RelativeLayout) convertView.findViewById(R.id.background);
        if(position > 0)
            back.setBackgroundResource(R.color.light_green_900);
        else
            back.setBackgroundResource(R.color.light_green_400);

        if(task.isSupertask())
            setPriority(convertView.findViewById(R.id.priority), task.getPriority());

        convertView.setOnClickListener(null);

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

    public TaskModel pop() {
        if(objects == null || objects.isEmpty())
            return null;
        TaskModel task = objects.get(0);
        objects.remove(task);
        notifyDataSetChanged();
        return task;
    }
}
