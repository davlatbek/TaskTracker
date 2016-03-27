package com.projectse.aads.task_tracker.Adapters;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseExpandableListAdapter;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.projectse.aads.task_tracker.MainActivity;
import com.projectse.aads.task_tracker.Models.CourseModel;
import com.projectse.aads.task_tracker.Models.TaskModel;
import com.projectse.aads.task_tracker.R;
import com.projectse.aads.task_tracker.TaskActivity;
import com.projectse.aads.task_tracker.TaskOverviewActivity;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

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
