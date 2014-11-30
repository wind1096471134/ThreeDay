package com.android.threeday.model.threeDay;

import android.content.Context;

import com.android.threeday.util.Util;

/**
 * Created by user on 2014/10/29.
 */
public class YesterdayModel extends BaseDayModel {

    public YesterdayModel(Context context) {
        super(context);
    }

    @Override
    protected TaskDbHelper getDbHelper() {
        return new YesterdayTaskDbHelper(this.mContext);
    }

    @Override
    protected int getDayType() {
        return Util.TYPE_YESTERDAY;
    }

}
