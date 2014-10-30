package com.android.threeday.model;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by user on 2014/10/29.
 */
class TaskSQLiteOpenHelper extends SQLiteOpenHelper {
    private static TaskSQLiteOpenHelper taskSQLiteOpenHelper;

    public static final String DB_NAME = "ThreeDay";
    public static final int DB_VERSION = 1;

    synchronized public static TaskSQLiteOpenHelper getInstance(Context context){
        if(taskSQLiteOpenHelper == null){
            taskSQLiteOpenHelper = new TaskSQLiteOpenHelper(context);
        }
        return taskSQLiteOpenHelper;
    }

    private TaskSQLiteOpenHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table task(id integer primary key,name text)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
