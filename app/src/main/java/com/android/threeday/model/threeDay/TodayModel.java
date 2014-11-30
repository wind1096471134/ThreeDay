package com.android.threeday.model.threeDay;

import android.content.Context;
import android.content.SharedPreferences;

import com.android.threeday.util.Util;

/**
 * Created by user on 2014/10/29.
 */
public class TodayModel extends BaseDayModel {

    public TodayModel(Context context) {
        super(context);
    }

    @Override
    protected TaskDbHelper getDbHelper() {
        return new TodayTaskDbHelper(this.mContext);
    }

    @Override
    protected int getDayType() {
        return Util.TYPE_TODAY;
    }

    @Override
    public boolean setDayEvaluation(int evaluation) {
        SharedPreferences sharedPreferences = this.mContext.getSharedPreferences(Util.PREFERENCE_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.edit().putInt(Util.PREFERENCE_KEY_DAY_EVALUATION, evaluation).commit();
    }

}
