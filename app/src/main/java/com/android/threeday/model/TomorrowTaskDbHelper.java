package com.android.threeday.model;

import android.content.Context;

import com.android.threeday.util.Util;

import java.util.ArrayList;

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
