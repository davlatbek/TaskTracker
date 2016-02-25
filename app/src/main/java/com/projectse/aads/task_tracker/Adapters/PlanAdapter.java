package com.projectse.aads.task_tracker.Adapters;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.projectse.aads.task_tracker.Models.TaskModel;
import com.projectse.aads.task_tracker.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PlanAdapter extends BaseExpandableListAdapter {

    private ArrayList<ArrayList<String>> mGroups;
    private Context mContext;
    private Map<TaskModel,List<TaskModel>> task_hierarchy = new HashMap<>();

    public PlanAdapter(Context context, ArrayList<ArrayList<String>> groups){
        mContext = context;
        mGroups = groups;
    }

    public PlanAdapter(Context context, Map<TaskModel,List<TaskModel>> groups){
        mContext = context;
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
        for(TaskModel supT : task_hierarchy.keySet()){
            if(i++ == groupPosition)
                return supT;
        }
        return null;
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        TaskModel sup = (TaskModel)getGroup(groupPosition);
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
            LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.plan_list_supertask_view, null);
        }

        if (isExpanded){
            //Изменяем что-нибудь, если текущая Group раскрыта
        }
        else{
            //Изменяем что-нибудь, если текущая Group скрыта
        }
        TaskModel supertask = (TaskModel)getGroup(groupPosition);

        TextView textSupertaskName = (TextView) convertView.findViewById(R.id.txtSuperTaskName);
        TextView textSubs = (TextView) convertView.findViewById(R.id.txtSubsCount);
//        textSupertaskName.setText("Group " + Integer.toString(groupPosition));
//        textSubs.setText("Contains " + Integer.toString(groupPosition));
        textSupertaskName.setText(supertask.toString());
        textSubs.setText(getChildrenCount(groupPosition) + " subtasks");
        if( supertask.getIsDone() )
            textSupertaskName.setPaintFlags(textSupertaskName.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);

        return convertView;

    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild,
                             View convertView, ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.plan_list_subtask_view, null);
        }

        TaskModel subtask = (TaskModel) getChild(groupPosition, childPosition);
        TextView textChild = (TextView) convertView.findViewById(R.id.txtSubtaskName);
        textChild.setText(subtask.toString());
        if( subtask.getIsDone() )
            textChild.setPaintFlags(textChild.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);

        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }
}
