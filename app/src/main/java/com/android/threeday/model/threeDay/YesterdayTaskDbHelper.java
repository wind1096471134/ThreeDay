package com.android.threeday.model.threeDay;

import android.content.Context;

import com.android.threeday.util.Util;

/**
 * Created by user on 2014/10/29.
 */
public class YesterdayTaskDbHelper extends TaskDbHelper {

    public YesterdayTaskDbHelper(Context context) {
        super(context);
    }

    @Override
    protected int getDayType() {
        return Util.TYPE_YESTERDAY;
    }


}
