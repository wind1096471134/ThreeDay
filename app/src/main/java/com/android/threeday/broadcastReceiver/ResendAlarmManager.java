package com.android.threeday.broadcastReceiver;

import android.app.AlarmManager;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.format.Time;
import android.util.Log;

import com.android.threeday.model.setting.SettingModel;
import com.android.threeday.model.threeDay.TaskItem;
import com.android.threeday.model.threeDay.TodayModel;
import com.android.threeday.model.threeDay.TomorrowModel;
import com.android.threeday.service.EveningCheckService;
import com.android.threeday.service.MorningRemainService;
import com.android.threeday.service.NewDaySettingService;
import com.android.threeday.service.RemainTaskService;
import com.android.threeday.util.Util;

import java.util.ArrayList;

/**
 * Created by user on 2014/12/14.
 */
class ResendAlarmManager {
    private Context mContext;
    private AlarmManager mAlarmManager;
    private NotificationManager mNotificationManager;
    private TodayModel mTodayModel;
    private TomorrowModel mTomorrowModel;
    private SettingModel mSettingModel;

    public ResendAlarmManager(Context context){
        this.mContext = context;
        initData( );
    }

    private void initData( ){
        this.mTodayModel = new TodayModel(this.mContext);
        this.mTomorrowModel = new TomorrowModel(this.mContext);
        this.mSettingModel = new SettingModel(this.mContext);
        this.mAlarmManager = (AlarmManager) this.mContext.getSystemService(Context.ALARM_SERVICE);
        this.mNotificationManager = (NotificationManager) this.mContext.getSystemService(Context.NOTIFICATION_SERVICE);
    }

    public void resendAlarms( ){
        ArrayList<TaskItem> taskItems = this.mTodayModel.getUndoneTasks();
        resendTaskRemainAlarms(taskItems);
        taskItems = this.mTomorrowModel.getUndoneTasks();
        resendTaskRemainAlarms(taskItems);

        if(this.mSettingModel.isMorningRemain()){
            String remainTime = this.mSettingModel.getMorningRemainTime();
            resendMorningRemainOrEveningCheckAlarm(remainTime, getMorningRemainPendingIntent());
        }
        if(this.mSettingModel.isEveningCheck()){
            String checkTime = this.mSettingModel.getEveningCheckTime();
            resendMorningRemainOrEveningCheckAlarm(checkTime, getEveningCheckPendingIntent());
        }

        resendNewDayAlarm();
    }

    private void resendNewDayAlarm(){
        Intent intent = new Intent(this.mContext, NewDaySettingService.class);
        PendingIntent pendingIntent = PendingIntent.getService(this.mContext, Util.UPDATE_DATA_AT_NEW_DAY_ALARM_ID, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        Time time = new Time();
        time.setToNow();
        time.hour = Util.NEW_DAY_ALARM_HOUR;
        time.minute = Util.NEW_DAY_ALARM_MINUTE;
        this.mAlarmManager.cancel(pendingIntent);
        this.mAlarmManager.setRepeating(AlarmManager.RTC_WAKEUP, time.toMillis(false) + Util.A_DAY_IN_MILLIS, Util.A_DAY_IN_MILLIS, pendingIntent);
    }

    private void resendMorningRemainOrEveningCheckAlarm(String timeString, PendingIntent pendingIntent){
        Time time = new Time();
        time.parse(timeString);
        Time now = new Time();
        now.setToNow();
        long startTime = time.before(now) ? (time.toMillis(false) + SettingModel.ALARM_REPEAT_TIME) : time.toMillis(false);
        this.mAlarmManager.cancel(pendingIntent);
        this.mAlarmManager.setRepeating(AlarmManager.RTC, startTime, SettingModel.ALARM_REPEAT_TIME, pendingIntent);
    }

    private PendingIntent getMorningRemainPendingIntent( ){
        Intent intent = new Intent(this.mContext, MorningRemainService.class);
        return PendingIntent.getService(this.mContext, Util.MORNING_REMAIN_ALARM_ID, intent, PendingIntent.FLAG_UPDATE_CURRENT);
    }

    private PendingIntent getEveningCheckPendingIntent( ){
        Intent intent = new Intent(this.mContext, EveningCheckService.class);
        return PendingIntent.getService(this.mContext, Util.EVENING_CHECK_ALARM_ID, intent, PendingIntent.FLAG_UPDATE_CURRENT);
    }

    private void resendTaskRemainAlarms(ArrayList<TaskItem> taskItems){
        Time remainTime = new Time();
        Time resetTime = new Time();
        resetTime.setToNow();
        Time now = new Time();
        now.setToNow();
        for(TaskItem taskItem : taskItems){
            if(taskItem.getRemain()){
                remainTime.parse(taskItem.getRemainTime());
                resetTime.hour = remainTime.hour;
                resetTime.minute = remainTime.minute;
                if(resetTime.before(now)){
                    Log.e("wind", "cancel " + taskItem.getId());
                    this.mNotificationManager.cancel((int) taskItem.getId());
                    this.mTodayModel.cancelUndoneTaskRemain(taskItems.indexOf(taskItem));
                    cancelAlarm(getRemainAlarmPendingIntent(taskItem));
                }else if(resetTime.after(now)){
                    this.mTodayModel.setUndoneTaskRemain(taskItems.indexOf(taskItem), resetTime.format2445());
                    resendAlarm(resetTime.toMillis(false), getRemainAlarmPendingIntent(taskItem));
                }
            }
        }
    }

    private void cancelAlarm(PendingIntent pendingIntent){
        this.mAlarmManager.cancel(pendingIntent);
    }

    private void resendAlarm(long time, PendingIntent pendingIntent){
        cancelAlarm(pendingIntent);
        this.mAlarmManager.set(AlarmManager.RTC, time, pendingIntent);
    }

    private PendingIntent getRemainAlarmPendingIntent(TaskItem taskItem){
        Intent intent = new Intent(this.mContext, RemainTaskService.class);
        Bundle bundle = new Bundle(1);
        bundle.putSerializable(Util.REMAIN_TASKITEM_KEY, taskItem);
        intent.putExtra(Util.REMAIN_BUNDLE_KEY, bundle);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        //set type to make every intent different
        intent.setType(Long.toString(taskItem.getId()));
        return PendingIntent.getService(this.mContext, (int) taskItem.getId(), intent, PendingIntent.FLAG_UPDATE_CURRENT);
    }
}
