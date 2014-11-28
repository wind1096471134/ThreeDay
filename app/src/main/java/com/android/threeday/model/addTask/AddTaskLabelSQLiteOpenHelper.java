package com.android.threeday.model.addTask;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.android.threeday.model.BaseSQLiteOpenHelper;

/**
 * Created by user on 2014/11/27.
 */
class AddTaskLabelSQLiteOpenHelper extends BaseSQLiteOpenHelper {
    public static final String TABLE_NAME = "add_task_labels";
    public static final String COLUMN_ID = "id";
    public static final String COLUMN_LABEL_NAME = "label_name";

    public static final String CRATE_TABLE = "CREATE TABLE IF NOT EXISTS " + TABLE_NAME
            + "(" + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + COLUMN_LABEL_NAME
            + " TEXT)";

    private static AddTaskLabelSQLiteOpenHelper mAddTaskLabelSQLiteOpenHelper;

    synchronized public static AddTaskLabelSQLiteOpenHelper getInstance(Context context){
        if(mAddTaskLabelSQLiteOpenHelper == null){
            mAddTaskLabelSQLiteOpenHelper = new AddTaskLabelSQLiteOpenHelper(context);
        }
        return mAddTaskLabelSQLiteOpenHelper;
    }

    private AddTaskLabelSQLiteOpenHelper(Context context) {
        super(context);
    }

    @Override
    protected void createTable(SQLiteDatabase db) {
        db.execSQL(CRATE_TABLE);
    }
}
