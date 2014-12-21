package com.android.threeday.service;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;
import android.text.format.Time;
import android.util.Log;

import com.android.threeday.R;
import com.android.threeday.activity.remainActivity.RemainTaskActivity;
import com.android.threeday.model.setting.TimeModel;
import com.android.threeday.model.threeDay.TaskItem;
import com.android.threeday.util.Util;

/**
 * Created by user on 2014/12/8.
 */
public class RemainTaskService extends Service {

    public RemainTaskService( ) {
        super( );
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Context context = getApplicationContext();
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        TaskItem taskItem = (TaskItem) intent.getBundleExtra(Util.REMAIN_BUNDLE_KEY).getSerializable(Util.REMAIN_TASKITEM_KEY);
        if(taskItem != null && taskItem.getRemainTime() != null){
            Time time = new Time();
            time.parse(taskItem.getRemainTime());
            TimeModel timeModel = new TimeModel(context);
            if(!timeModel.isDateChangeToLaterDay() && !timeModel.isTimeChangeLater(time)){
                NotificationCompat.Builder builder = new NotificationCompat.Builder(context);
                builder.setAutoCancel(false);
                builder.setContentTitle(taskItem.getInformation());
                String remainTicker = context.getResources().getString(R.string.task_remain_notification_ticker);
                builder.setContentText(remainTicker);
                builder.setTicker(remainTicker);
                builder.setDefaults(Notification.DEFAULT_ALL);
                builder.setContentIntent(getPendingIntent(context, (int) taskItem.getId()));
                builder.setWhen(time.toMillis(false));
                builder.setSmallIcon(R.drawable.ic_launcher);

                Notification notification = builder.build();
                notification.flags |= Notification.FLAG_NO_CLEAR;
                notificationManager.notify((int) taskItem.getId(), notification);
            }
        }
        stopSelf(startId);

        return Service.START_NOT_STICKY;
    }

    private PendingIntent getPendingIntent(Context context, int id){
        Intent intent = new Intent(context, CheckLockService.class);
        intent.putExtra(Util.EXTRA_KEY_LOCK_START_ACTIVITY, RemainTaskActivity.class);
        intent.putExtra(Util.TASK_ID, id);
        //setType to make PendingIntent identify
        intent.setType(Integer.toString(id));
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        return PendingIntent.getService(context, id, intent, PendingIntent.FLAG_UPDATE_CURRENT);
    }
}
