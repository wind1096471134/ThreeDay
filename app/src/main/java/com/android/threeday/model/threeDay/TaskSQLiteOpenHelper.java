package com.android.threeday.model.threeDay;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.android.threeday.model.BaseSQLiteOpenHelper;

/**
 * Created by user on 2014/10/29.
 */
class TaskSQLiteOpenHelper extends BaseSQLiteOpenHelper {
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
    public static final String RESET_DATABASE_ID = "UPDATE SQLITE_SEQUENCE SET SEQ = 0 WHERE NAME = '"
            + TABLE_TASK + "'";

    private final String CREATE_TABLE_TASK = "CREATE TABLE IF NOT EXISTS " + TABLE_TASK + "(" +
            COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + COLUMN_DAY_TYPE +
            " INTEGER, " + COLUMN_TASK_INFORMATION + " TEXT, " + COLUMN_TASK_TO_REMAIN +
            " INTEGER, " + COLUMN_TASK_REMAIN_TIME + " TEXT, " + COLUMN_TASK_TIME +
            " TEXT, " + COLUMN_TASK_EVALUATION + " INTEGER, " + COLUMN_DONE + " INTEGER, "
            + COLUMN_DONE_TIME + " TEXT)";

    private static TaskSQLiteOpenHelper mTaskSQLiteOpenHelper;

    private TaskSQLiteOpenHelper(Context context) {
        super(context);
    }

    synchronized public static TaskSQLiteOpenHelper getInstance(Context context){
        if(mTaskSQLiteOpenHelper == null){
            mTaskSQLiteOpenHelper = new TaskSQLiteOpenHelper(context);
        }
        return mTaskSQLiteOpenHelper;
    }

    @Override
    protected void createTable(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_TASK);
    }
}
