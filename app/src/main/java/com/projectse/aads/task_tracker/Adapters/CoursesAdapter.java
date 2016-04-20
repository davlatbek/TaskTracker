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

import com.projectse.aads.task_tracker.Models.CheckableCourseModel;
import com.projectse.aads.task_tracker.Models.CheckableTaskModel;
import com.projectse.aads.task_tracker.Models.CourseModel;
import com.projectse.aads.task_tracker.Models.TaskModel;
import com.projectse.aads.task_tracker.R;

import java.util.List;

/**
 * Class is used in WeekViewFragment for showing supertasks for each day
 */
public class CoursesAdapter extends ArrayAdapter<CheckableCourseModel> {

    List<CheckableCourseModel> objects;

    public CoursesAdapter(Context context, int resource, List<CheckableCourseModel> objects) {
        super(context, resource, objects);
        this.objects = objects;
    }

    View.OnClickListener onClickListener ;

    @Override
    public View getView(final int position, View convertView, final ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.course_list_item, null);
        }

        TextView title = (TextView) convertView.findViewById(R.id.request_name);
        TextView desc = (TextView) convertView.findViewById(R.id.request_short_desc);
        desc.setVisibility(View.INVISIBLE);
        final CheckableCourseModel checkableCourseModel = getItem(position);

        String str = checkableCourseModel.getCourseName();
        if(checkableCourseModel.getTasks() != null && !checkableCourseModel.getTasks().isEmpty())
            str += " (" + checkableCourseModel.getTasks().size() + " tasks)";
        title.setText(str);

        ImageView indicator = (ImageView) convertView.findViewById(R.id.group_indicator);
        if(checkableCourseModel.getChecked() == true)
            indicator.setImageResource(R.drawable.checked);
        else
            indicator.setImageDrawable(null);
        CourseModel course = checkableCourseModel.getCourse();
        if (course != null) {
            TextView course_label = (TextView) convertView.findViewById(R.id.abrevLbl);
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
            TextView course_label = (TextView) convertView.findViewById(R.id.abrevLbl);
            course_label.setVisibility(View.INVISIBLE);
        }

        if(onClickListener != null)
            convertView.setOnClickListener(onClickListener);
        else
            convertView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    for(CheckableCourseModel c : objects)
                        c.setChecked(false);
                    checkableCourseModel.check();
                    notifyDataSetChanged();
                }
            });
        return convertView;
    }

    public int checkedCount(){
        int counter = 0;
        for(CheckableCourseModel c: objects){
            if(c.getChecked())
                counter++;
        }
        return counter;
    }

}
