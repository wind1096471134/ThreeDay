package com.android.threeday.broadcastReceiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class SystemTimeChangeReceiver extends BroadcastReceiver {
    public SystemTimeChangeReceiver() {
    }

    @Override
    public void onReceive(Context context, Intent intent) {

        String action = intent.getAction();Log.e("wind", "date change" + action);
        if(action.equals("android.intent.action.TIME_SET")){
            ResendAlarmManager resendAlarmManager = new ResendAlarmManager(context);
            resendAlarmManager.resetRealTimeWhenTimeChange();
            resendAlarmManager.resendAlarms();
        }
    }
}
