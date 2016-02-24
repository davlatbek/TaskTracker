package com.projectse.aads.task_tracker.Adapters;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.projectse.aads.task_tracker.R;

import java.util.HashMap;
import java.util.List;

/**
 * Created by smith on 2/23/16.
 */
public class SubtasksAdapter<T extends Object> extends ArrayAdapter<T> {

    HashMap<T, Integer> mIdMap = new HashMap<T, Integer>();

    public SubtasksAdapter(Context context, int textViewResourceId,
                           List<T> objects) {
        super(context, textViewResourceId, objects);
        for (int i = 0; i < objects.size(); ++i) {
            mIdMap.put(objects.get(i), i);
        }
    }

    @Override
    public long getItemId(int position) {
        T item = getItem(position);
        return mIdMap.get(item);
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.subtasks_list_item, null);
        }

        TextView textChild = (TextView) convertView.findViewById(R.id.txtSubtaskName);
        textChild.setText(getItem(position).toString());
        textChild.setTextColor(Color.BLACK);
        textChild.setPaintFlags(textChild.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);

        Button button = (Button)convertView.findViewById(R.id.buttonChild);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getContext(), "button is pressed", Toast.LENGTH_SHORT).show();
            }
        });
        return convertView;
    }
}
