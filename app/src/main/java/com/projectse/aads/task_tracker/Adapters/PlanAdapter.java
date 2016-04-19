package com.projectse.aads.task_tracker.Adapters;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.projectse.aads.task_tracker.MainActivity;
import com.projectse.aads.task_tracker.Models.CourseModel;
import com.projectse.aads.task_tracker.Models.TaskModel;
import com.projectse.aads.task_tracker.R;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PlanAdapter extends BaseExpandableListAdapter {

    private Context context;
    private Map<TaskModel, List<TaskModel>> task_hierarchy = new HashMap<>();

    public PlanAdapter(Context context, Map<TaskModel, List<TaskModel>> groups) {
        this.context = context;
        task_hierarchy = groups;
    }

    @Override
    public int getGroupCount() {
        return task_hierarchy.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return task_hierarchy.get(getGroup(groupPosition)).size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        int i = 0;
        for (TaskModel supT : task_hierarchy.keySet()) {
            if (i++ == groupPosition)
                return supT;
        }
        return null;
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        TaskModel sup = (TaskModel) getGroup(groupPosition);
        return task_hierarchy.get(sup).get(childPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView,
                             ViewGroup parent) {

        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.supertask_listitem_view, null);
        }

        ImageView indicator = (ImageView) convertView.findViewById(R.id.group_indicator);
        if (isExpanded){
            indicator.setImageResource(R.drawable.up_arrow);
        }
        else{
            indicator.setImageResource(R.drawable.down_arrow);
        }
        final TaskModel supertask = (TaskModel)getGroup(groupPosition);

        TextView textSupertaskName = (TextView) convertView.findViewById(R.id.txtSuperTaskName);
        LinearLayout super_task_block = (LinearLayout) convertView.findViewById(R.id.supertask_info);
        super_task_block.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((MainActivity) context).callTaskOverviewActivity(supertask);
            }
        });

        setPriority(convertView.findViewById(R.id.priority), supertask.getPriority());

        CourseModel course = supertask.getCourse();
        if (course != null) {
            TextView course_label = (TextView) convertView.findViewById(R.id.lblCourse);
            course_label.setText(course.getAbbreviation());
            switch ((-1) * course.getClr()) {
                case 7617718:
                    course_label.setBackgroundResource(R.color.coursecolor1);
                    break;
                case 16728876:
                    course_label.setBackgroundResource(R.color.coursecolor2);
                    break;
                case 5317:
                    course_label.setBackgroundResource(R.color.coursecolor3);
                    break;
                case 2937298:
                    course_label.setBackgroundResource(R.color.coursecolor4);
                    break;
                case 10011977:
                    course_label.setBackgroundResource(R.color.coursecolor5);
                    break;
                case 12627531:
                    course_label.setBackgroundResource(R.color.coursecolor6);
                    break;
                default:
                    try {
                        course_label.setBackgroundResource(course.getClr());
                    }catch (Exception e){
                        e.printStackTrace();
                    }
            }
        } else {
            TextView course_label = (TextView) convertView.findViewById(R.id.lblCourse);
            course_label.setText("NaN");
            course_label.setBackgroundColor(Color.DKGRAY);
        }

        TextView textSubs = (TextView) convertView.findViewById(R.id.txtSubsCount);
        int children_count = getChildrenCount(groupPosition);
        if (children_count > 0) {
            textSupertaskName.setText(supertask.toString() + " (" + children_count + " subtasks)");
            textSubs.setText(children_count + " subtasks");
            indicator.setVisibility(View.VISIBLE);
        }else{
            textSupertaskName.setText(supertask.toString());
            indicator.setVisibility(View.INVISIBLE);
        }

        if (supertask.getIsDone())
            textSupertaskName.setPaintFlags(textSupertaskName.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);

        return convertView;

    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild,
                             View convertView, ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.plan_list_subtask_view, null);
        }

        final TaskModel subtask = (TaskModel) getChild(groupPosition, childPosition);
        TextView textSubtaskName = (TextView) convertView.findViewById(R.id.txtSubtaskName);

        textSubtaskName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((MainActivity) context).callTaskOverviewActivity(subtask);
            }
        });
        textSubtaskName.setText(subtask.toString());
        if (subtask.getIsDone())
            textSubtaskName.setPaintFlags(textSubtaskName.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);

        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }

    void setPriority(View viewById, TaskModel.Priority priority) {
        switch (priority) {
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
