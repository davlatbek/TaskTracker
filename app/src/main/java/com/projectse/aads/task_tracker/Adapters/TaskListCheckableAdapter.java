package com.projectse.aads.task_tracker.Adapters;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.projectse.aads.task_tracker.Models.CheckableTaskModel;
import com.projectse.aads.task_tracker.Models.CourseModel;
import com.projectse.aads.task_tracker.Models.TaskModel;
import com.projectse.aads.task_tracker.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Class is used in WeekViewFragment for showing supertasks for each day
 */
public class TaskListCheckableAdapter extends ArrayAdapter<CheckableTaskModel> {

    private List<CheckableTaskModel> objs = new ArrayList<>();

    public TaskListCheckableAdapter(Context context, int resource, List<CheckableTaskModel> objects) {
        super(context, resource, objects);
        objs = objects;
    }

    @Override
    public View getView(final int position, View convertView, final ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.supertask_listitem_view, null);
        }


        TextView textView = (TextView) convertView.findViewById(R.id.txtSuperTaskName);
        final CheckableTaskModel item = getItem(position);
        final TaskModel task = item.getTask();

        String label = task.toString();
        if(task.isSupertask() && task.getSubtasks_ids().size() > 0) {
            label += " (" + task.getSubtasks_ids().size() + ")";
        }
        textView.setText(label);
        textView.setTextColor(Color.BLACK);

        ImageView indicator = (ImageView) convertView.findViewById(R.id.group_indicator);
        if(item.getChecked() == true)
            indicator.setImageResource(R.drawable.checked);
        else
            indicator.setImageDrawable(null);

        if(task.isSupertask()) {
            setPriority(convertView.findViewById(R.id.priority), task.getPriority());
        }
        CourseModel course = task.getCourse();
        if (course != null) {
            TextView course_label = (TextView) convertView.findViewById(R.id.lblCourse);
            course_label.setText(course.getAbbreviation());
            //TODO BLYUA, eto zhest'!!! REMOVE THIS hardcode part. [smith]
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

        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                item.check();
                getView(position,v,parent);
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

    public void setAllChecked(){
        for (CheckableTaskModel t : objs){
            t.setChecked( true );
        }
        notifyDataSetChanged();
    }
    public void setAllUnchecked(){
        for (CheckableTaskModel t : objs){
            t.setChecked( false );
        }
        notifyDataSetChanged();
    }
}
