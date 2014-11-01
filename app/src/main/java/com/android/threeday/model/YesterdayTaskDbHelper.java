package com.android.threeday.model;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.android.threeday.util.Util;

import java.util.ArrayList;

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
