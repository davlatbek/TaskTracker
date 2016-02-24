package com.projectse.aads.task_tracker.Adapters;

import android.content.Context;
import android.widget.ArrayAdapter;

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

}
