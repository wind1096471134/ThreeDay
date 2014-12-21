package com.android.threeday.model.setting;

import android.content.Context;

import com.android.threeday.model.BaseModel;

/**
 * Created by user on 2014/12/10.
 */
public class SettingModel implements BaseModel {
    private Context mContext;
    private TimeModel mTimeModel;
    private LockModel mLockModel;

    public SettingModel(Context context){
        this.mContext = context;
        initData( );
    }

    public void setMorningRemain(boolean remain){
        this.mTimeModel.setMorningRemain(remain);
    }

    public void setEveningCheck(boolean check){
        this.mTimeModel.setEveningCheck(check);
    }

    public void setMorningRemainTime(int hour, int minute){
        this.mTimeModel.setMorningRemainTime(hour, minute);
    }

    public int getMorningRemainTimeHour( ){
        return this.mTimeModel.getMorningRemainTimeHour();
    }

    public int getMorningRemainTimeMinute( ){
        return this.mTimeModel.getMorningRemainTimeMinute();
    }

    public void setEveningCheckTime(int hour, int minute){
        this.mTimeModel.setEveningCheckTime(hour, minute);
    }

    public int getEveningCheckTimeHour( ){
        return this.mTimeModel.getEveningCheckTimeHour( );
    }

    public int getEveningCheckTimeMinute( ){
        return this.mTimeModel.getEveningCheckTimeMinute( );
    }

    public String getMorningRemainTimeText(){
        return formatTime(this.mTimeModel.getMorningRemainTimeHour(), this.mTimeModel.getMorningRemainTimeMinute());
    }

    public String getEveningCheckTimeText(){
        return formatTime(this.mTimeModel.getEveningCheckTimeHour(), this.mTimeModel.getEveningCheckTimeMinute());
    }

    public boolean isMorningRemain( ){
        return this.mTimeModel.isMorningRemain( );
    }

    public boolean isEveningCheck( ){
        return this.mTimeModel.isEveningCheck( );
    }

    public boolean isLockSet( ){
        return this.mLockModel.isLockSet();
    }

    public void setLockSet(boolean set){
        this.mLockModel.setLock(set);
    }

    private void initData( ){
        this.mTimeModel = new TimeModel(this.mContext);
        this.mLockModel = new LockModel(this.mContext);
    }

    private String formatTime(int hour, int minute){
        String minuteString = minute < 10 ? ("0" + minute) : Integer.toString(minute);
        return hour + ":" + minuteString;
    }

}
