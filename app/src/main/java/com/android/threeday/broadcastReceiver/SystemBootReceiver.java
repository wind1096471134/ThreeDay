package com.android.threeday.broadcastReceiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class SystemBootReceiver extends BroadcastReceiver {
    public SystemBootReceiver() {
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        if(intent.getAction().equals("android.intent.action.BOOT_COMPLETED")){
            ResendAlarmManager resendAlarmManager = new ResendAlarmManager(context);
            resendAlarmManager.resetRealTimeWhenBoot();
            resendAlarmManager.resendAlarms();
        }
    }
}
