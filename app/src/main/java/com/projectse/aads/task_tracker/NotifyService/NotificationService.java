package com.projectse.aads.task_tracker.NotifyService;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.IBinder;
import android.support.annotation.Nullable;

import com.projectse.aads.task_tracker.TaskActivity;
import com.projectse.aads.task_tracker.TaskOverviewActivity;

import java.util.concurrent.TimeUnit;

/**
 * Created by Andrey Zolin on 07.03.2016.
 */
public class NotificationService extends Service {
    NotificationManager nm;

    @Override
    public void onCreate() {
        super.onCreate();
        nm = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
    }

    public int onStartCommand(Intent intent, int flags, int startId) {
        try {
            TimeUnit.SECONDS.sleep(5);
        } catch (InterruptedException e){
            e.printStackTrace();
        }
        sendNotif();
        return super.onStartCommand(intent,flags,startId);
    }
    void sendNotif(){
        Notification notif = new Notification(android.support.design.R.drawable.notification_template_icon_bg,"Text in status bar",System.currentTimeMillis());
        Intent intent = new Intent(this, TaskOverviewActivity.class);

    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
