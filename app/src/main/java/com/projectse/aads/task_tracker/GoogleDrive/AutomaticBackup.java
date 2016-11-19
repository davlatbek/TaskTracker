package com.projectse.aads.task_tracker.GoogleDrive;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.preference.PreferenceManager;

import com.projectse.aads.task_tracker.NotifyService.TaskTrackerBroadcastReceiver;

import java.util.Calendar;

/**
 * Created by Liza on 17.11.2016 г..
 */
public class AutomaticBackup {

    public static void start(Context context, boolean fromReboot){
        AlarmManager alarmMgr = (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
        PendingIntent alarmIntent = getPendoingIntent(context);

        int interval = PreferenceManager.getDefaultSharedPreferences(context.getApplicationContext())
                .getInt(Constants.BACKUP_INTERVAL_KEY, -1);
        if(interval > -1) {
            Calendar calendar = Calendar.getInstance();
            if(!fromReboot){
                calendar.add(Calendar.HOUR_OF_DAY, interval);
            }

            alarmMgr.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(),
                    1000 * 60 * 60 * interval, alarmIntent);
                    //1000 * 60  * interval, alarmIntent); //for testing - this is in minutes
        }
    }

    public static void stop(Context context){
        AlarmManager alarmMgr = (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
        PendingIntent alarmIntent = getPendoingIntent(context);
        alarmMgr.cancel(alarmIntent);
        PreferenceManager.getDefaultSharedPreferences(context.getApplicationContext())
                .edit().putInt(Constants.BACKUP_INTERVAL_KEY, -1).commit();
    }

    private static PendingIntent getPendoingIntent(Context context){
        Intent intent = new Intent(context, TaskTrackerBroadcastReceiver.class);
        intent.putExtra(Constants.BACKUP_KEY, true);
        PendingIntent alarmIntent = PendingIntent.getBroadcast(context, 0, intent, 0);
        return alarmIntent;
    }
}
