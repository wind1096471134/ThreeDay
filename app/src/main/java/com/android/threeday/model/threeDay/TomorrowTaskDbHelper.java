package com.android.threeday.model.threeDay;

import android.content.Context;

import com.android.threeday.util.Util;

/**
 * Created by user on 2014/10/29.
 */
public class TomorrowTaskDbHelper extends TaskDbHelper {

    public TomorrowTaskDbHelper(Context context) {
        super(context);
    }

    @Override
    protected int getDayType() {
        return Util.TYPE_TOMORROW;
    }


}
