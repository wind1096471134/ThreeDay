package com.android.threeday.model.setting;

import android.content.Context;
import android.content.SharedPreferences;

import com.android.threeday.model.BaseModel;
import com.android.threeday.util.Util;

/**
 * Created by user on 2014/12/10.
 */
public class SettingModel implements BaseModel {
    public static final long ALARM_REPEAT_TIME = 24 * 60 * 60 * 1000;

    private Context mContext;
    private SharedPreferences mSharedPreferences;

    private String mMorningRemainTime;
    private String mEveningCheckTime;
    private int mMorningRemainTimeHour;
    private int mMorningRemainTimeMinute;
    private int mEveningCheckTimeHour;
    private int mEveningCheckTimeMinute;
    private boolean mMorningRemain;
    private boolean mEveningCheck;

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
        this.mMorningRemainTime = formatTime(hour, minute);
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
        this.mEveningCheckTime = formatTime(hour, minute);
        this.mSharedPreferences.edit().putInt(Util.PREFERENCE_KEY_EVENING_CHECK_TIME_HOUR, hour)
                .putInt(Util.PREFERENCE_KEY_EVENING_CHECK_TIME_MINUTE, minute).commit();
    }

    public int getEveningCheckTimeHour( ){
        return this.mEveningCheckTimeHour;
    }

    public int getEveningCheckTimeMinute( ){
        return this.mEveningCheckTimeMinute;
    }

    public String getMorningRemainTime( ){
        return this.mMorningRemainTime;
    }

    public String getEveningCheckTime( ){
        return this.mEveningCheckTime;
    }

    public boolean isMorningRemain( ){
        return this.mMorningRemain;
    }

    public boolean isEveningCheck( ){
        return this.mEveningCheck;
    }

    private void initData( ){
        mSharedPreferences = this.mContext.getSharedPreferences(Util.PREFERENCE_NAME, Context.MODE_PRIVATE);

        this.mMorningRemain = this.mSharedPreferences.getBoolean(Util.PREFERENCE_KEY_MORNING_REMAIN, Util.DEFAULT_MORNING_REMAIN);
        this.mEveningCheck = this.mSharedPreferences.getBoolean(Util.PREFERENCE_KEY_EVENING_CHECK, Util.DEFAULT_EVENING_CHECK);

        this.mMorningRemainTimeHour = mSharedPreferences.getInt(Util.PREFERENCE_KEY_MORNING_REMAIN_TIME_HOUR, Util.MORNING_REMAIN_TIME_DEFAULT_HOUR);
        this.mMorningRemainTimeMinute = mSharedPreferences.getInt(Util.PREFERENCE_KEY_MORNING_REMAIN_TIME_MINUTE, Util.MORNING_REMAIN_TIME_DEFAULT_MINUTE);
        this.mMorningRemainTime = formatTime(this.mMorningRemainTimeHour, this.mMorningRemainTimeMinute);

        this.mEveningCheckTimeHour = mSharedPreferences.getInt(Util.PREFERENCE_KEY_EVENING_CHECK_TIME_HOUR, Util.EVENING_CHECK_TIME_DEFAULT_HOUR);
        this.mEveningCheckTimeMinute = mSharedPreferences.getInt(Util.PREFERENCE_KEY_EVENING_CHECK_TIME_MINUTE, Util.EVENING_CHECK_TIME_DEFAULT_MINUTE);
        this.mEveningCheckTime = formatTime(this.mEveningCheckTimeHour, this.mEveningCheckTimeMinute);
    }

    private String formatTime(int hour, int minute){
        String minuteString = minute < 10 ? ("0" + minute) : Integer.toString(minute);
        return hour + ":" + minuteString;
    }
}
