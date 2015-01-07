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
import com.android.threeday.activity.mainActivity.MainActivity;
import com.android.threeday.model.setting.TimeModel;
import com.android.threeday.util.Util;

public class MorningRemainService extends Service {

    public MorningRemainService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Context context = getApplicationContext();
        TimeModel timeModel = new TimeModel(context);

        if(!timeModel.isDateChangeToLaterDay() && !timeModel.isTimeChangeLaterThanMorningTime()){
            /*because when user change the system's time later, this receive may be invoke too,
             so we should check first*/
            NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
            NotificationCompat.Builder builder = new NotificationCompat.Builder(context);
            builder.setAutoCancel(true);
            builder.setContentTitle(context.getResources().getString(R.string.morning_remain_notification_title));
            builder.setContentText(context.getResources().getString(R.string.morning_remain_notification_text));
            String remainTicker = context.getResources().getString(R.string.task_remind_notification_ticker);
            builder.setTicker(remainTicker);
            builder.setDefaults(Notification.DEFAULT_ALL);
            builder.setContentIntent(getPendingIntent(context));
            Time time = new Time();
            time.setToNow();
            builder.setWhen(time.toMillis(false));
            builder.setSmallIcon(R.drawable.ic_launcher);

            Notification notification = builder.build();
            notificationManager.notify(Util.MORNING_REMAIN_NOTIFICATION_ID, notification);

        }

        stopSelf(startId);
        return Service.START_NOT_STICKY;
    }

    private PendingIntent getPendingIntent(Context context){
        Intent intent = new Intent(context, CheckLockService.class);
        intent.putExtra(Util.EXTRA_KEY_LOCK_START_ACTIVITY, MainActivity.class);
        return PendingIntent.getService(context, Util.MORNING_REMAIN_PENDING_INTENT_ID, intent, PendingIntent.FLAG_UPDATE_CURRENT);
    }
}
