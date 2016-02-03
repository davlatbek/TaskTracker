package com.projectse.aads.task_tracker.Utils;

import android.widget.ListView;

import com.projectse.aads.task_tracker.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by smith on 1/30/16.
 * Store tasks in hash map statically.
 */
public class TaskModel {
    final private static Map<Long,Task> tasks = new HashMap<>();

    /**
     * Create debug data
     */
    /** TODO
     * avoid this method usage and delete finally.
     */
    public static void initData(){
        String[] values = new String[] { "Android", "iPhone", "WindowsMobile",
                "Blackberry", "WebOS", "Ubuntu", "Windows7", "Max OS X",
                "Linux", "OS/2", "Ubuntu", "Windows7", "Max OS X", "Linux",
                "OS/2", "Ubuntu", "Windows7", "Max OS X", "Linux", "OS/2",
                "Android", "iPhone", "WindowsMobile" };

        for (int i = 0; i < values.length; ++i) {
            tasks.put( (long)i, new Task((long) i,values[i]));
        }
    }

    public static Task getTaskById(long id){
        return tasks.get(id);
    }

    public static List<Task> toList(){
        List<Task> list = new ArrayList<>();
        list.addAll(tasks.values());
        return list;
    }
}
