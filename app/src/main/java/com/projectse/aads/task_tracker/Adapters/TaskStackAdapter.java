package com.projectse.aads.task_tracker.Adapters;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.projectse.aads.task_tracker.Models.CourseModel;
import com.projectse.aads.task_tracker.Models.TaskModel;
import com.projectse.aads.task_tracker.R;

import java.text.SimpleDateFormat;
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
            convertView = inflater.inflate(R.layout.supertask_listitem_view, null);
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

        if(task.isSupertask()) {
            setPriority(convertView.findViewById(R.id.priority), task.getPriority());
            TextView course_label = (TextView) convertView.findViewById(R.id.lblCourse);
            setCourse(course_label, task);
            TextView more = (TextView) convertView.findViewById(R.id.txtSubsCount);
            setMore(more,task);
        }

        convertView.setOnClickListener(null);

        return convertView;
    }

    private void setMore(TextView view, TaskModel task) {
        if(view == null || task == null)
            return;
        else{
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("Duration: ");
            stringBuilder.append(task.getDuration());
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd MM yyyy");
            stringBuilder.append("; Deadline: ");
            stringBuilder.append( dateFormat.format(task.getDeadline().getTime()) );
            view.setText(stringBuilder);
        }
    }

    private void setPriority(View viewById, TaskModel.Priority priority){
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

    private void setCourse(TextView viewById, TaskModel supertask){
        if(viewById == null || supertask == null)
            return;
        CourseModel course = supertask.getCourse();
        if (course != null) {
            viewById.setText(course.getAbbreviation());
            //TODO BLYUA, eto zhest'!!! REMOVE THIS hardcode part. [smith]
            switch ((-1) * course.getClr()) {
                case 7617718:
                    viewById.setBackgroundResource(R.color.coursecolor1);
                    break;
                case 16728876:
                    viewById.setBackgroundResource(R.color.coursecolor2);
                    break;
                case 5317:
                    viewById.setBackgroundResource(R.color.coursecolor3);
                    break;
                case 2937298:
                    viewById.setBackgroundResource(R.color.coursecolor4);
                    break;
                case 10011977:
                    viewById.setBackgroundResource(R.color.coursecolor5);
                    break;
                case 12627531:
                    viewById.setBackgroundResource(R.color.coursecolor6);
                    break;
                default:
                    try {
                        viewById.setBackgroundResource(course.getClr());
                    }catch (Exception e){
                        e.printStackTrace();
                    }
            }
        } else {
            viewById.setText("NaN");
            viewById.setBackgroundColor(Color.DKGRAY);
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
