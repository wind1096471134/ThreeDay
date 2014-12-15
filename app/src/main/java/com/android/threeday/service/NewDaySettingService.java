package com.android.threeday.service;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.IBinder;
import android.util.Log;

import com.android.threeday.util.Util;

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
        SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences(Util.PREFERENCE_NAME, Context.MODE_PRIVATE);
        sharedPreferences.edit().putBoolean(Util.PREFERENCE_KEY_NEW_DAY_CHECK, false).commit();
        stopSelf(startId);
        return Service.START_NOT_STICKY;
    }
}
