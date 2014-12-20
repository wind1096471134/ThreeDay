package com.android.threeday.model.setting;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.format.Time;

import com.android.threeday.model.BaseModel;
import com.android.threeday.util.Util;

/**
 * Created by user on 2014/12/10.
 */
public class SettingModel implements BaseModel {
    public static final long ALARM_REPEAT_TIME = 24 * 60 * 60 * 1000;

    private Context mContext;
    private SharedPreferences mSharedPreferences;

    private int mMorningRemainTimeHour;
    private int mMorningRemainTimeMinute;
    private int mEveningCheckTimeHour;
    private int mEveningCheckTimeMinute;
    private boolean mMorningRemain;
    private boolean mEveningCheck;
    private boolean mLockSet;

    public SettingModel(Context context){
        this.mContext = context;
        initData( );
    }

    public void setMorningRemain(boolean remain){
        this.mMorningRemain = remain;
        mSharedPreferences.edit().putBoolean(Util.PREFERENCE_KEY_MORNING_REMAIN, remain).commit();
    }

    public void setEveningCheck(boolean check){
        this.mEveningCheck = check;
        mSharedPreferences.edit().putBoolean(Util.PREFERENCE_KEY_EVENING_CHECK, check).commit();
    }

    public void setMorningRemainTime(int hour, int minute){
        this.mMorningRemainTimeHour = hour;
        this.mMorningRemainTimeMinute = minute;
        this.mSharedPreferences.edit().putInt(Util.PREFERENCE_KEY_MORNING_REMAIN_TIME_HOUR, hour)
                .putInt(Util.PREFERENCE_KEY_MORNING_REMAIN_TIME_MINUTE, minute).commit();
    }

    public int getMorningRemainTimeHour( ){
        return this.mMorningRemainTimeHour;
    }

    public int getMorningRemainTimeMinute( ){
        return this.mMorningRemainTimeMinute;
    }

    public void setEveningCheckTime(int hour, int minute){
        this.mEveningCheckTimeHour = hour;
        this.mEveningCheckTimeMinute = minute;
        this.mSharedPreferences.edit().putInt(Util.PREFERENCE_KEY_EVENING_CHECK_TIME_HOUR, hour)
                .putInt(Util.PREFERENCE_KEY_EVENING_CHECK_TIME_MINUTE, minute).commit();
    }

    public int getEveningCheckTimeHour( ){
        return this.mEveningCheckTimeHour;
    }

    public int getEveningCheckTimeMinute( ){
        return this.mEveningCheckTimeMinute;
    }

    public String getMorningRemainTimeText(){
        return formatTime(this.mMorningRemainTimeHour, this.mMorningRemainTimeMinute);
    }

    public String getEveningCheckTimeText(){
        return formatTime(this.mEveningCheckTimeHour, this.mEveningCheckTimeMinute);
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

    public boolean isLockSet( ){
        return this.mLockSet;
    }

    public void setLockSet(boolean set){
        this.mLockSet = set;
        this.mSharedPreferences.edit().putBoolean(Util.PREFERENCE_KEY_LOCK_SET, set).commit();
    }

    private void initData( ){
        this.mSharedPreferences = this.mContext.getSharedPreferences(Util.PREFERENCE_NAME, Context.MODE_PRIVATE);

        this.mMorningRemain = this.mSharedPreferences.getBoolean(Util.PREFERENCE_KEY_MORNING_REMAIN, Util.DEFAULT_MORNING_REMAIN);
        this.mEveningCheck = this.mSharedPreferences.getBoolean(Util.PREFERENCE_KEY_EVENING_CHECK, Util.DEFAULT_EVENING_CHECK);

        this.mMorningRemainTimeHour = this.mSharedPreferences.getInt(Util.PREFERENCE_KEY_MORNING_REMAIN_TIME_HOUR, Util.MORNING_REMAIN_TIME_DEFAULT_HOUR);
        this.mMorningRemainTimeMinute = this.mSharedPreferences.getInt(Util.PREFERENCE_KEY_MORNING_REMAIN_TIME_MINUTE, Util.MORNING_REMAIN_TIME_DEFAULT_MINUTE);

        this.mEveningCheckTimeHour = this.mSharedPreferences.getInt(Util.PREFERENCE_KEY_EVENING_CHECK_TIME_HOUR, Util.EVENING_CHECK_TIME_DEFAULT_HOUR);
        this.mEveningCheckTimeMinute = this.mSharedPreferences.getInt(Util.PREFERENCE_KEY_EVENING_CHECK_TIME_MINUTE, Util.EVENING_CHECK_TIME_DEFAULT_MINUTE);

        this.mLockSet = this.mSharedPreferences.getBoolean(Util.PREFERENCE_KEY_LOCK_SET, false);
    }

    private String formatTime(int hour, int minute){
        String minuteString = minute < 10 ? ("0" + minute) : Integer.toString(minute);
        return hour + ":" + minuteString;
    }

    public void resetRealTime( ){
        Time now = new Time();
        now.setToNow();
        long real = this.mSharedPreferences.getLong(Util.PREFERENCE_KEY_REAL_DAY_TIME_2, 0);
        Time realTime = new Time();
        realTime.set(real);
        if(now.year == realTime.year && now.yearDay == realTime.yearDay && now.hour == realTime.hour
                && now.minute == realTime.minute){
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
