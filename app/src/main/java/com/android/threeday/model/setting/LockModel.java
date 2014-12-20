package com.android.threeday.model.setting;

import android.content.Context;
import android.content.SharedPreferences;

import com.android.threeday.model.BaseModel;
import com.android.threeday.util.Util;

/**
 * Created by user on 2014/12/18.
 */
public class LockModel implements BaseModel {
    private SharedPreferences mSharedPreferences;

    public LockModel(Context context){
        this.mSharedPreferences = context.getSharedPreferences(Util.PREFERENCE_NAME, Context.MODE_PRIVATE);
    }

    public boolean isLockSet( ){
        return this.mSharedPreferences.getBoolean(Util.PREFERENCE_KEY_LOCK_SET, false);
    }

    public String getLockPassword( ){
        return this.mSharedPreferences.getString(Util.PREFERENCE_KEY_LOCK_PASSWORD, null);
    }

    public boolean setLock(boolean set){
        return this.mSharedPreferences.edit().putBoolean(Util.PREFERENCE_KEY_LOCK_SET, set).commit();
    }

    public boolean setLockPassword(String password){
        return this.mSharedPreferences.edit().putString(Util.PREFERENCE_KEY_LOCK_PASSWORD, password).commit();
    }
}
