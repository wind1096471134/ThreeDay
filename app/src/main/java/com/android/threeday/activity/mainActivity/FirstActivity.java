package com.android.threeday.activity.mainActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.android.threeday.activity.lockActivity.LockActivity;
import com.android.threeday.model.setting.LockModel;
import com.android.threeday.util.Util;

/**
 * Created by user on 2014/12/18.
 */
public class FirstActivity extends Activity {
    private LockModel mLockModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.mLockModel = new LockModel(this);
        Intent intent;
        if(this.mLockModel.isLockSet()){
            intent = new Intent(this, LockActivity.class);
            intent.putExtra(Util.EXTRA_KEY_LOCK_START_ACTIVITY, MainActivity.class);
            intent.putExtra(Util.EXTRA_KEY_LOCK_ACTIVITY_STATE, LockActivity.STATE_LOCK_IN);
        }else{
            intent = new Intent(this, MainActivity.class);
        }
        startActivity(intent);
        finish();
    }

}
