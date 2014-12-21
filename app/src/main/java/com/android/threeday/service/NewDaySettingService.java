package com.android.threeday.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import com.android.threeday.model.setting.TimeModel;

/**
 * Created by user on 2014/12/13.
 */
public class NewDaySettingService extends Service {
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        TimeModel timeModel = new TimeModel(getApplicationContext());
        if(!timeModel.isRealTimeResetToNow()){
            timeModel.setRealTime( );
        }
        stopSelf(startId);
        return Service.START_NOT_STICKY;
    }
}
