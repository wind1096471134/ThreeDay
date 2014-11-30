package com.android.threeday.model.threeDay;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.android.threeday.model.DbHelper;
import com.android.threeday.util.Util;

import java.util.ArrayList;

/**
 * Created by user on 2014/10/29.
 */
public abstract class TaskDbHelper implements DbHelper {
    protected SQLiteOpenHelper mSQLiteOpenHelper;
    protected int mDayType;
    protected final String QUERY_SQL = "SELECT * FROM " + TaskSQLiteOpenHelper.TABLE_TASK +
            " WHERE " + TaskSQLiteOpenHelper.COLUMN_DAY_TYPE + "=?";

    public TaskDbHelper(Context context){
        initData();
        this.mSQLiteOpenHelper = TaskSQLiteOpenHelper.getInstance(context);
    }

    public ArrayList<TaskItem> getTasks( ){
        SQLiteDatabase sqLiteDatabase = this.mSQLiteOpenHelper.getReadableDatabase();
        ArrayList<TaskItem> arrayList = new ArrayList<TaskItem>(20);
        if(sqLiteDatabase.isOpen()){
            Cursor cursor = sqLiteDatabase.rawQuery(QUERY_SQL, new String[]{Integer.toString(this.mDayType)});
            fillTasks(arrayList, cursor);
        }
        return arrayList;
    }

    public long addTask(TaskItem taskItem){
        ContentValues contentValues = new ContentValues(10);
        contentValues.put(TaskSQLiteOpenHelper.COLUMN_TASK_INFORMATION, taskItem.getInformation());
        contentValues.put(TaskSQLiteOpenHelper.COLUMN_DAY_TYPE, taskItem.getDayType());
        contentValues.put(TaskSQLiteOpenHelper.COLUMN_DONE, taskItem.getDone());
        contentValues.put(TaskSQLiteOpenHelper.COLUMN_DONE_TIME, taskItem.getDoneTime());
        contentValues.put(TaskSQLiteOpenHelper.COLUMN_TASK_EVALUATION, taskItem.getEvaluation());
        contentValues.put(TaskSQLiteOpenHelper.COLUMN_TASK_TO_REMAIN, taskItem.getRemain());
        contentValues.put(TaskSQLiteOpenHelper.COLUMN_TASK_REMAIN_TIME, taskItem.getRemainTime());
        contentValues.put(TaskSQLiteOpenHelper.COLUMN_TASK_TIME, taskItem.getTime());
        SQLiteDatabase database = this.mSQLiteOpenHelper.getWritableDatabase();
        return database.insert(TaskSQLiteOpenHelper.TABLE_TASK, null, contentValues);
    }

    public int deleteTask(long id){
        return mSQLiteOpenHelper.getWritableDatabase().delete(TaskSQLiteOpenHelper.TABLE_TASK, TaskSQLiteOpenHelper.COLUMN_ID
            + "=?", new String[]{Long.toString(id)});
    }

    private int updateDatabase(long id, ContentValues contentValues){
        return mSQLiteOpenHelper.getWritableDatabase().update(TaskSQLiteOpenHelper.TABLE_TASK, contentValues, TaskSQLiteOpenHelper.COLUMN_ID
                + "=?", new String[]{Long.toString(id)});
    }

    public int updateTask(TaskItem taskItem){
        ContentValues contentValues = new ContentValues(10);
        contentValues.put(TaskSQLiteOpenHelper.COLUMN_TASK_TO_REMAIN, taskItem.getRemain());
        contentValues.put(TaskSQLiteOpenHelper.COLUMN_TASK_REMAIN_TIME, taskItem.getRemainTime());
        contentValues.put(TaskSQLiteOpenHelper.COLUMN_TASK_TIME, taskItem.getTime());
        contentValues.put(TaskSQLiteOpenHelper.COLUMN_TASK_INFORMATION, taskItem.getInformation());
        contentValues.put(TaskSQLiteOpenHelper.COLUMN_DONE, taskItem.getDone());
        contentValues.put(TaskSQLiteOpenHelper.COLUMN_DONE_TIME, taskItem.getDoneTime());
        contentValues.put(TaskSQLiteOpenHelper.COLUMN_TASK_EVALUATION, taskItem.getEvaluation());
        contentValues.put(TaskSQLiteOpenHelper.COLUMN_DAY_TYPE, taskItem.getDayType());
        return updateDatabase(taskItem.getId(), contentValues);
    }

    public int setTaskRemain(long id, String remainTime){
        ContentValues contentValues = new ContentValues(2);
        contentValues.put(TaskSQLiteOpenHelper.COLUMN_TASK_TO_REMAIN, true);
        contentValues.put(TaskSQLiteOpenHelper.COLUMN_TASK_REMAIN_TIME, remainTime);
        return updateDatabase(id, contentValues);
    }

    public int cancelTaskRemain(long id){
        ContentValues contentValues = new ContentValues(2);
        contentValues.put(TaskSQLiteOpenHelper.COLUMN_TASK_TO_REMAIN, false);
        contentValues.putNull(TaskSQLiteOpenHelper.COLUMN_TASK_REMAIN_TIME);
        return updateDatabase(id, contentValues);
    }

    public int changeTaskRemainTime(long id, String remainTime){
        ContentValues contentValues = new ContentValues(1);
        contentValues.put(TaskSQLiteOpenHelper.COLUMN_TASK_REMAIN_TIME, remainTime);
        return updateDatabase(id, contentValues);
    }

    public int setTaskDone(long id, boolean done){
        ContentValues contentValues = new ContentValues(1);
        contentValues.put(TaskSQLiteOpenHelper.COLUMN_DONE, done);
        return updateDatabase(id, contentValues);
    }

    public int setTaskDoneTime(long id, String time){
        ContentValues contentValues = new ContentValues(1);
        contentValues.put(TaskSQLiteOpenHelper.COLUMN_DONE_TIME, time);
        return updateDatabase(id, contentValues);
    }

    public int setTaskEvaluation(long id, int evaluation){
        ContentValues contentValues = new ContentValues(1);
        contentValues.put(TaskSQLiteOpenHelper.COLUMN_TASK_EVALUATION, evaluation);
        return updateDatabase(id, contentValues);
    }

    private void initData( ){
        this.mDayType = getDayType();
    }

    protected void fillTasks(ArrayList<TaskItem> arrayList, Cursor cursor){
        if(cursor != null){
            if(cursor.moveToFirst()){
                TaskItem taskItem = null;
                int index = 0;
                do{
                    int id = cursor.getInt(cursor.getColumnIndex(TaskSQLiteOpenHelper.COLUMN_ID));
                    index = cursor.getColumnIndex(TaskSQLiteOpenHelper.COLUMN_TASK_TIME);
                    String time = cursor.isNull(index) ? null : cursor.getString(index);
                    taskItem = new TaskItem(id, this.mDayType, time);
                    index = cursor.getColumnIndex(TaskSQLiteOpenHelper.COLUMN_DONE);
                    taskItem.setDone(!cursor.isNull(index) && (cursor.getInt(index) == Util.DONE));
                    index = cursor.getColumnIndex(TaskSQLiteOpenHelper.COLUMN_DONE_TIME);
                    taskItem.setDoneTime(cursor.isNull(index) ? null : cursor.getString(index));
                    index = cursor.getColumnIndex(TaskSQLiteOpenHelper.COLUMN_TASK_EVALUATION);
                    taskItem.setEvaluation(cursor.isNull(index) ? Util.EVALUATION_DEFAULT : cursor.getInt(index));
                    index = cursor.getColumnIndex(TaskSQLiteOpenHelper.COLUMN_TASK_INFORMATION);
                    taskItem.setInformation(cursor.isNull(index) ? null : cursor.getString(index));
                    index = cursor.getColumnIndex(TaskSQLiteOpenHelper.COLUMN_TASK_TO_REMAIN);
                    taskItem.setRemain(!cursor.isNull(index) && (cursor.getInt(index) == Util.REMAIN));
                    index = cursor.getColumnIndex(TaskSQLiteOpenHelper.COLUMN_TASK_REMAIN_TIME);
                    taskItem.setRemainTime(cursor.isNull(index) ? null : cursor.getString(index));
                    arrayList.add(taskItem);
                }while (cursor.moveToNext());
            }
        }
    }

    protected abstract int getDayType( );

}
