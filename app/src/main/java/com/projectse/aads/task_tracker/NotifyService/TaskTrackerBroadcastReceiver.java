package com.projectse.aads.task_tracker.NotifyService;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.projectse.aads.task_tracker.GoogleDrive.AutomaticBackup;
import com.projectse.aads.task_tracker.GoogleDrive.Constants;
import com.projectse.aads.task_tracker.GoogleDrive.GoogleDrive;
import com.projectse.aads.task_tracker.MainActivity;

/**
 * Created by Andrey Zolin on 07.03.2016.
 */
public class TaskTrackerBroadcastReceiver extends BroadcastReceiver {
    private static final String TAG = "TTReceiver";

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals("android.intent.action.BOOT_COMPLETED")) {
            AutomaticBackup.start(context, true);
            Log.d(TAG, "Started the automatic backup after reboot.");
        }else if(intent.getBooleanExtra(Constants.BACKUP_KEY, false)) {
            new GoogleDrive(context, true).backup();
        }else{
                createNotfication(context, "Times Up", "Time to start tasks!", "Alert");
        }
    }

    private void createNotfication(Context context, String s, String s1, String alert) {
        PendingIntent notificIntent = PendingIntent.getActivity(context, 0, new Intent(context, MainActivity.class), 0);
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(context)
                .setSmallIcon(android.support.design.R.drawable.notification_template_icon_bg)
                .setContentTitle(s)
                .setTicker(alert)
                .setContentText(s1);
        mBuilder.setContentIntent(notificIntent);
        mBuilder.setDefaults(Notification.DEFAULT_ALL);
        mBuilder.setAutoCancel(true);

        NotificationManager mNotificationManager =
                (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        mNotificationManager.notify(1, mBuilder.build());

    }
}
