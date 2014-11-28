package com.android.threeday.model;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by user on 2014/11/27.
 */
public abstract class BaseSQLiteOpenHelper extends SQLiteOpenHelper {
    public static final String DB_NAME = "ThreeDay";
    public static final int DB_VERSION = 1;

    public BaseSQLiteOpenHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
        createTable(getWritableDatabase());
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    protected abstract void createTable(SQLiteDatabase db);
}
