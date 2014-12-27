package com.android.threeday.model.setting;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.format.Time;

import com.android.threeday.model.BaseModel;
import com.android.threeday.util.Util;

/**
 * Created by user on 2014/12/21.
 */
public class TimeModel implements BaseModel {
    private static final int SAME_MINUTE_EDGE = 1;
    private SharedPreferences mSharedPreferences;

    private int mMorningRemainTimeMinute;
    private int mMorningRemainTimeHour;
    private int mEveningCheckTimeHour;
    private int mEveningCheckTimeMinute;
    private boolean mMorningRemain;
    private boolean mEveningCheck;
    private boolean mTodayTasksCheck;

    public TimeModel(Context context){
        mSharedPreferences = context.getSharedPreferences(Util.PREFERENCE_NAME, Context.MODE_PRIVATE);
        initData( );
    }

    private void initData( ){
        this.mMorningRemainTimeHour = this.mSharedPreferences.getInt(Util.PREFERENCE_KEY_MORNING_REMAIN_TIME_HOUR, Util.MORNING_REMAIN_TIME_DEFAULT_HOUR);
        this.mMorningRemainTimeMinute = this.mSharedPreferences.getInt(Util.PREFERENCE_KEY_MORNING_REMAIN_TIME_MINUTE, Util.MORNING_REMAIN_TIME_DEFAULT_MINUTE);
        this.mEveningCheckTimeHour = this.mSharedPreferences.getInt(Util.PREFERENCE_KEY_EVENING_CHECK_TIME_HOUR, Util.EVENING_CHECK_TIME_DEFAULT_HOUR);
        this.mEveningCheckTimeMinute = this.mSharedPreferences.getInt(Util.PREFERENCE_KEY_EVENING_CHECK_TIME_MINUTE, Util.EVENING_CHECK_TIME_DEFAULT_MINUTE);
        this.mMorningRemain = this.mSharedPreferences.getBoolean(Util.PREFERENCE_KEY_MORNING_REMAIN, Util.DEFAULT_MORNING_REMAIN);
        this.mEveningCheck = this.mSharedPreferences.getBoolean(Util.PREFERENCE_KEY_EVENING_CHECK, Util.DEFAULT_EVENING_CHECK);
        this.mTodayTasksCheck = this.mSharedPreferences.getBoolean(Util.PREFERENCE_KEY_TODAY_TASKS_CHECK, false);
    }

    public boolean isTodayTasksCheck( ){
        return this.mTodayTasksCheck;
    }

    public boolean setMorningRemain(boolean remain){
        this.mMorningRemain = remain;
        return mSharedPreferences.edit().putBoolean(Util.PREFERENCE_KEY_MORNING_REMAIN, remain).commit();
    }

    public boolean setEveningCheck(boolean check){
        this.mEveningCheck = check;
        return mSharedPreferences.edit().putBoolean(Util.PREFERENCE_KEY_EVENING_CHECK, check).commit();
    }

    public boolean setMorningRemainTime(int hour, int minute){
        this.mMorningRemainTimeHour = hour;
        this.mMorningRemainTimeMinute = minute;
        return this.mSharedPreferences.edit().putInt(Util.PREFERENCE_KEY_MORNING_REMAIN_TIME_HOUR, hour)
                .putInt(Util.PREFERENCE_KEY_MORNING_REMAIN_TIME_MINUTE, minute).commit();
    }

    public int getMorningRemainTimeHour( ){
        return this.mMorningRemainTimeHour;
    }

    public int getMorningRemainTimeMinute( ){
        return this.mMorningRemainTimeMinute;
    }

    public boolean setEveningCheckTime(int hour, int minute){
        this.mEveningCheckTimeHour = hour;
        this.mEveningCheckTimeMinute = minute;
        return this.mSharedPreferences.edit().putInt(Util.PREFERENCE_KEY_EVENING_CHECK_TIME_HOUR, hour)
                .putInt(Util.PREFERENCE_KEY_EVENING_CHECK_TIME_MINUTE, minute).commit();
    }

    public int getEveningCheckTimeHour( ){
        return this.mEveningCheckTimeHour;
    }

    public int getEveningCheckTimeMinute( ){
        return this.mEveningCheckTimeMinute;
    }

    public long getMorningRemainTimeMills(){
        Time time = new Time();
        time.setToNow();
        time.hour = this.mMorningRemainTimeHour;
        time.minute = this.mMorningRemainTimeMinute;
        return time.toMillis(false);
    }

    public long getEveningCheckTimeMills(){
        Time time = new Time();
        time.setToNow();
        time.hour = this.mEveningCheckTimeHour;
        time.minute = this.mEveningCheckTimeMinute;
        return time.toMillis(false);
    }

    public boolean isMorningRemain( ){
        return this.mMorningRemain;
    }

    public boolean isEveningCheck( ){
        return this.mEveningCheck;
    }

    public boolean isRealTimeResetToNow( ){
        Time time = new Time();
        time.set(this.mSharedPreferences.getLong(Util.PREFERENCE_KEY_REAL_DAY_TIME_2, 0));
        Time now = new Time();
        now.setToNow();
        return time.year == now.year && time.yearDay == now.yearDay && time.hour == now.hour
                && time.minute == now.minute;
    }

    public boolean setRealTime( ){
        Time time = new Time();
        time.setToNow();
        return this.mSharedPreferences.edit().
                putLong(Util.PREFERENCE_KEY_REAL_DAY_TIME_1, this.mSharedPreferences.getLong(Util.PREFERENCE_KEY_REAL_DAY_TIME_2, 0))
                .putLong(Util.PREFERENCE_KEY_REAL_DAY_TIME_2, time.toMillis(false)).commit();
    }

    public boolean isDateChangeToLaterDay( ){
        Time time = new Time();
        time.set(this.mSharedPreferences.getLong(Util.PREFERENCE_KEY_REAL_DAY_TIME_2, 0));
        Time now = new Time();
        now.setToNow();
        if(time.year == now.year && time.yearDay == now.yearDay && time.hour == now.hour
                && time.minute == now.minute){
            /*In this situation, time2 equals now because the SystemTimeChangeReceiver change it before
             this call, so time1 is the real datetime */
            time.set(this.mSharedPreferences.getLong(Util.PREFERENCE_KEY_REAL_DAY_TIME_1, 0));
        }
        if(now.year > time.year){
            return true;
        }else if(now.year == time.year){
            if(now.yearDay > time.yearDay){
                return true;
            }
        }
        return false;
    }

    private boolean isTimeLater(int hour, int minute){
        Time now = new Time();
        now.setToNow();
        if(now.hour == hour){
            /*because alarm may reach later than exact time, so we think within this edge is the same time*/
            if(now.minute - SAME_MINUTE_EDGE > minute){
                return true;
            }
        }else if(now.hour > hour){
            return true;
        }
        return false;
    }

    public boolean isTimeChangeLater(Time time){
        return isTimeLater(time.hour, time.minute);
    }

    public boolean isTimeChangeLaterThanMorningTime( ){
        int hour = this.mSharedPreferences.getInt(Util.PREFERENCE_KEY_MORNING_REMAIN_TIME_HOUR, 0);
        int minute = this.mSharedPreferences.getInt(Util.PREFERENCE_KEY_MORNING_REMAIN_TIME_MINUTE, 0);
        return isTimeLater(hour, minute);
    }

    public boolean isTimeChangeLaterThanEveningTime( ){
        int hour = this.mSharedPreferences.getInt(Util.PREFERENCE_KEY_EVENING_CHECK_TIME_HOUR, 0);
        int minute = this.mSharedPreferences.getInt(Util.PREFERENCE_KEY_EVENING_CHECK_TIME_MINUTE, 0);
        return isTimeLater(hour, minute);
    }

    public void resetRealTime( ){
        if(isRealTimeResetToNow()){
                        /*we assume that in these situation this method invoke after NewDayAlarm immediately,
             because NewDayAlarm changes time2 to now, so the real time is time1, we set it back*/
            this.mSharedPreferences.edit().putLong(Util.PREFERENCE_KEY_REAL_DAY_TIME_2,
                    this.mSharedPreferences.getLong(Util.PREFERENCE_KEY_REAL_DAY_TIME_1, 0)).commit();

        }
    }

    public int compareLastInAndRealDay( ){
        long lastIn = this.mSharedPreferences.getLong(Util.PREFERENCE_KEY_LAST_IN_DAY_TIME, -1);
        long real = this.mSharedPreferences.getLong(Util.PREFERENCE_KEY_REAL_DAY_TIME_2, -1);
        if(lastIn != -1 && real != -1){
            Time lastInTime = new Time();
            lastInTime.set(lastIn);
            Time realTime = new Time();
            realTime.set(real);
            if(lastInTime.year == realTime.year){
                return realTime.yearDay - lastInTime.yearDay;
            }else if(realTime.year > lastInTime.year){
                if(lastInTime.year % 4 == 0){// 366 day
                    return 365 - lastInTime.yearDay + realTime.yearDay + 1;
                }else{// 365 day
                    return 364 - lastInTime.year + realTime.yearDay + 1;
                }
            }
        }

        return 0;
    }

    public void resetLastInTime(long time){
        this.mSharedPreferences.edit().putLong(Util.PREFERENCE_KEY_LAST_IN_DAY_TIME, time).commit();
    }

    public void resetRealTime(long time){
        this.mSharedPreferences.edit().
                putLong(Util.PREFERENCE_KEY_REAL_DAY_TIME_1, this.mSharedPreferences.getLong(Util.PREFERENCE_KEY_REAL_DAY_TIME_2, 0))
                .putLong(Util.PREFERENCE_KEY_REAL_DAY_TIME_2, time).commit();
    }
}
