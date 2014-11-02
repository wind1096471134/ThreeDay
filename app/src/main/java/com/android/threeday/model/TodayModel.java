package com.android.threeday.model;

import android.content.Context;

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

}
