package com.projectse.aads.task_tracker.NotifyService;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;

import com.projectse.aads.task_tracker.DailyPlanActivity;
import com.projectse.aads.task_tracker.PlanActivity;
import com.projectse.aads.task_tracker.TaskActivity;
import com.projectse.aads.task_tracker.TaskAddActivity;
import com.projectse.aads.task_tracker.TaskOverviewActivity;

import java.util.concurrent.TimeUnit;

/**
 * Created by Andrey Zolin on 07.03.2016.
 */
public class AlertReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        createNotfication(context, "Times Up", "5 sec has passed", "Alert");
    }

    private void createNotfication(Context context, String s, String s1, String alert) {
        PendingIntent notificIntent = PendingIntent.getActivity(context, 0, new Intent(context, DailyPlanActivity.class), 0);
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
