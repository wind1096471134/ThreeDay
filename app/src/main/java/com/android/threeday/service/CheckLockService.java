package com.android.threeday.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import com.android.threeday.activity.lockActivity.LockActivity;
import com.android.threeday.model.setting.LockModel;
import com.android.threeday.util.Util;

public class CheckLockService extends Service {
    public CheckLockService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        LockModel lockModel = new LockModel(getApplicationContext());
        Class activityClass = (Class) intent.getSerializableExtra(Util.EXTRA_KEY_LOCK_START_ACTIVITY);
        if(lockModel.isLockSet()){
            intent.setClass(getApplicationContext(), LockActivity.class);
            intent.putExtra(Util.EXTRA_KEY_LOCK_START_ACTIVITY, activityClass);
            intent.putExtra(Util.EXTRA_KEY_LOCK_ACTIVITY_STATE, LockActivity.STATE_LOCK_IN);
        }else{
            intent.setClass(getApplicationContext(), activityClass);
        }
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        stopSelf();
        return Service.START_NOT_STICKY;
    }
}
