package com.android.threeday.model.threeDay;

import android.content.Context;

import com.android.threeday.util.Util;

/**
 * Created by user on 2014/10/29.
 */
public class TomorrowModel extends BaseDayModel {

    public TomorrowModel(Context context) {
        super(context);
    }

    @Override
    protected TaskDbHelper getDbHelper() {
        return new TomorrowTaskDbHelper(this.mContext);
    }

    @Override
    protected int getDayType() {
        return Util.TYPE_TOMORROW;
    }

    @Override
    protected int getBaseDayEvaluation() {
        return Util.EVALUATION_DEFAULT;
    }

}
