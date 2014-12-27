package com.android.threeday.broadcastReceiver;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.format.Time;

import com.android.threeday.model.setting.TimeModel;
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
public class ResendAlarmManager {
    private Context mContext;
    private AlarmManager mAlarmManager;
    private TodayModel mTodayModel;
    private TomorrowModel mTomorrowModel;
    private TimeModel mTimeModel;

    public ResendAlarmManager(Context context){
        this.mContext = context;
        initData( );
    }

    private void initData( ){
        this.mTodayModel = new TodayModel(this.mContext);
        this.mTomorrowModel = new TomorrowModel(this.mContext);
        this.mTimeModel = new TimeModel(this.mContext);
        this.mAlarmManager = (AlarmManager) this.mContext.getSystemService(Context.ALARM_SERVICE);
    }

    public void resetRealTimeWhenTimeChange(){
        this.mTimeModel.resetRealTime();
    }

    public void resetRealTimeWhenBoot( ){
        Time time = new Time();
        time.setToNow();
        this.mTimeModel.resetRealTime(time.toMillis(false));
    }

    public void resendAlarms( ){
        int day = this.mTimeModel.compareLastInAndRealDay();
        Time time = new Time();
        time.setToNow();
        long nowTimeMills = time.toMillis(false);
        if(day <= 0){
            /*user enter the app today, so we should resend all alarm*/
            ArrayList<TaskItem> taskItems = this.mTodayModel.getUndoneTasks();
            resendTaskRemainAlarms(taskItems);
            taskItems = this.mTomorrowModel.getUndoneTasks();
            resendTaskRemainAlarms(taskItems);
            this.mTimeModel.resetLastInTime(nowTimeMills);
        }else if(day == 1){
            /*user not enter the app today but yesterday, so when the system time has change
            we should also resend the today's remain alarm */
            this.mTimeModel.resetLastInTime(nowTimeMills - Util.A_DAY_IN_MILLIS);
        }else{
             /*user not enter the app before yesterday so there are not remain alarm we
             should resend, so we delete all data*/
            this.mTimeModel.resetLastInTime(nowTimeMills - Util.A_DAY_IN_MILLIS * 2);
        }
        resendMorningOrEveningAlarm();
        resendNewDayAlarm();
        this.mTimeModel.resetRealTime(nowTimeMills);
    }

    private void resendMorningOrEveningAlarm( ){
        if(this.mTimeModel.isMorningRemain()){
            long remainTime = this.mTimeModel.getMorningRemainTimeMills();
            resendMorningRemainOrEveningCheckAlarm(remainTime, getMorningRemainPendingIntent());
        }
        if(this.mTimeModel.isEveningCheck()){
            long checkTime = this.mTimeModel.getEveningCheckTimeMills();
            resendMorningRemainOrEveningCheckAlarm(checkTime, getEveningCheckPendingIntent());
        }
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

    private void resendMorningRemainOrEveningCheckAlarm(long timeMills, PendingIntent pendingIntent){
        Time time = new Time();
        time.set(timeMills);
        Time now = new Time();
        now.setToNow();
        long startTime = time.after(now) ? time.toMillis(false) : (time.toMillis(false) + Util.A_DAY_IN_MILLIS);
        this.mAlarmManager.cancel(pendingIntent);
        this.mAlarmManager.setRepeating(AlarmManager.RTC, startTime, Util.A_DAY_IN_MILLIS, pendingIntent);
    }

    private PendingIntent getMorningRemainPendingIntent( ){
        Intent intent = new Intent(this.mContext, MorningRemainService.class);
        return PendingIntent.getService(this.mContext, Util.MORNING_REMAIN_PENDING_INTENT_ID, intent, PendingIntent.FLAG_UPDATE_CURRENT);
    }

    private PendingIntent getEveningCheckPendingIntent( ){
        Intent intent = new Intent(this.mContext, EveningCheckService.class);
        return PendingIntent.getService(this.mContext, Util.EVENING_CHECK_PENDING_INTENT_ID, intent, PendingIntent.FLAG_UPDATE_CURRENT);
    }

    /*we send all alarms when user enter the app because alarms may be killed by system, so we should make sure again*/
    public void sendTaskRemainAlarmsAgain( ){
        resendMorningOrEveningAlarm();
        resendNewDayAlarm();

        resendTaskRemainAlarms(this.mTodayModel.getUndoneTasks());
        resendTaskRemainAlarms(this.mTomorrowModel.getUndoneTasks());
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
                resetTime.second = remainTime.second;
                if(resetTime.before(now)){
                    cancelAlarm(getRemainAlarmPendingIntent(taskItem));
                    this.mTodayModel.cancelUndoneTaskRemain(taskItems.indexOf(taskItem));
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
