package com.android.threeday.model;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by user on 2014/10/29.
 */
class TaskSQLiteOpenHelper extends SQLiteOpenHelper {
    public static final String DB_NAME = "ThreeDay";
    public static final String TABLE_TASK = "task";
    public static final String COLUMN_ID = "id";
    public static final String COLUMN_DAY_TYPE = "day_type";
    public static final String COLUMN_TASK_INFORMATION = "information";
    public static final String COLUMN_TASK_TO_REMAIN = "to_remain";
    public static final String COLUMN_TASK_REMAIN_TIME = "remain_time";
    public static final String COLUMN_TASK_EVALUATION = "evaluation";
    public static final String COLUMN_TASK_TIME = "time";
    public static final String COLUMN_DONE = "done";
    public static final String COLUMN_DONE_TIME = "done_time";

    private final String CREATE_TABLE_TASK = "CREATE TABLE IF NOT EXISTS " + TABLE_TASK + "(" +
            COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + COLUMN_DAY_TYPE +
            " INTEGER, " + COLUMN_TASK_INFORMATION + " TEXT, " + COLUMN_TASK_TO_REMAIN +
            " INTEGER, " + COLUMN_TASK_REMAIN_TIME + " TEXT, " + COLUMN_TASK_TIME +
            " TEXT, " + COLUMN_TASK_EVALUATION + " INTEGER, " + COLUMN_DONE + " INTEGER, "
            + COLUMN_DONE_TIME + " TEXT)";

    public static final int DB_VERSION = 1;

    private static TaskSQLiteOpenHelper mTaskSQLiteOpenHelper;

    private TaskSQLiteOpenHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    synchronized public static TaskSQLiteOpenHelper getInstance(Context context){
        if(mTaskSQLiteOpenHelper == null){
            mTaskSQLiteOpenHelper = new TaskSQLiteOpenHelper(context);
        }
        return mTaskSQLiteOpenHelper;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_TASK);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
