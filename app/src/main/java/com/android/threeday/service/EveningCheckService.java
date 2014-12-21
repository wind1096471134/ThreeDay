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

import com.android.threeday.R;
import com.android.threeday.activity.checkTaskActivity.CheckTaskActivity;
import com.android.threeday.model.setting.TimeModel;
import com.android.threeday.model.threeDay.TodayModel;
import com.android.threeday.util.Util;

public class EveningCheckService extends Service {
    public EveningCheckService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Context context = getApplicationContext();
        TimeModel timeModel = new TimeModel(context);
        if(!timeModel.isDateChangeToLaterDay() && !timeModel.isTimeChangeLaterThanEveningTime()){
            TodayModel todayModel = new TodayModel(context);
            if(todayModel.getUndoneTasks().size() > 0 || todayModel.getDoneTasks().size() > 0){
                NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

                NotificationCompat.Builder builder = new NotificationCompat.Builder(context);
                builder.setAutoCancel(true);
                builder.setContentTitle(context.getResources().getString(R.string.evening_check_notification_title));
                builder.setContentText(context.getResources().getString(R.string.evening_check_notification_text));
                String remainTicker = context.getResources().getString(R.string.task_remain_notification_ticker);
                builder.setTicker(remainTicker);
                builder.setDefaults(Notification.DEFAULT_ALL);
                builder.setContentIntent(getPendingIntent(context));
                Time time = new Time();
                time.setToNow();
                builder.setWhen(time.toMillis(false));
                builder.setSmallIcon(R.drawable.ic_launcher);

                Notification notification = builder.build();
                notificationManager.notify(Util.EVENING_CHECK_NOTIFICATION_ID, notification);
            }
        }

        stopSelf(startId);
        return Service.START_NOT_STICKY;
    }

    private PendingIntent getPendingIntent(Context context){
        Intent intent = new Intent(context, CheckLockService.class);
        intent.putExtra(Util.EXTRA_KEY_LOCK_START_ACTIVITY, CheckTaskActivity.class);
        return PendingIntent.getService(context, Util.EVENING_CHECK_PENDING_INTENT_ID, intent, PendingIntent.FLAG_UPDATE_CURRENT);
    }
}
