package com.android.threeday.model;

import android.content.Context;

import com.android.threeday.util.Util;


/**
 * Created by user on 2014/10/29.
 */
public class TodayTaskDbHelper extends TaskDbHelper {

    public TodayTaskDbHelper(Context context) {
        super(context);
    }

    @Override
    protected int getDayType() {
        return Util.TYPE_TODAY;
    }

}
