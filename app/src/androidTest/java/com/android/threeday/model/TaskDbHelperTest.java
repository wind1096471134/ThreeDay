package com.android.threeday.model;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import android.text.format.Time;

import com.android.threeday.model.testInterface.TaskDbInterface;
import com.android.threeday.util.Util;

import java.util.ArrayList;
import java.util.Random;

import static junit.framework.Assert.*;

/**
 * Created by user on 2014/10/31.
 */
public class TaskDbHelperTest implements TaskDbInterface {
    private TaskDbHelper mTaskDbHelper;
    private Context mContext;

    public TaskDbHelperTest(Context context, TaskDbHelper taskDbHelper){
        this.mContext = context;
        this.mTaskDbHelper = taskDbHelper;
    }

    private void deleteTestData( ){
        TaskSQLiteOpenHelper taskSQLiteOpenHelper = TaskSQLiteOpenHelper.getInstance(mContext);
        taskSQLiteOpenHelper.getWritableDatabase().delete(TaskSQLiteOpenHelper.TABLE_TASK, null, null);
    }

    private Cursor putTestData( ){
        deleteTestData();
        int num = new Random().nextInt(3);
        ContentValues contentValues = new ContentValues();
        contentValues.put(TaskSQLiteOpenHelper.COLUMN_TASK_INFORMATION, "");
        contentValues.put(TaskSQLiteOpenHelper.COLUMN_DAY_TYPE, mTaskDbHelper.getDayType());
        SQLiteDatabase database = TaskSQLiteOpenHelper.getInstance(mContext).getWritableDatabase();
        for(int i = 0; i < num; i++){
            database.insert(TaskSQLiteOpenHelper.TABLE_TASK, null, contentValues);
        }
        Cursor cursor = database.rawQuery("select * from " + TaskSQLiteOpenHelper.TABLE_TASK
                + " where " + TaskSQLiteOpenHelper.COLUMN_DAY_TYPE + "=" + mTaskDbHelper.getDayType(), null);
        return cursor;
    }

    @Override
    public void testFillTasks( ) throws Exception{
        Cursor cursor = putTestData();
        ArrayList<TaskItem> arrayList = new ArrayList<TaskItem>();
        this.mTaskDbHelper.fillTasks(arrayList, cursor);
        assertEquals(cursor.getCount(), arrayList.size());

        if(arrayList.size() > 0){
            TaskItem taskItem = arrayList.get(0);
            int index = 0;
            cursor.moveToFirst();
            assertEquals(cursor.getInt(cursor.getColumnIndex(TaskSQLiteOpenHelper.COLUMN_ID)), taskItem.getId());
            index = cursor.getColumnIndex(TaskSQLiteOpenHelper.COLUMN_TASK_TIME);
            assertEquals(cursor.isNull(index) ? null : cursor.getString(index), taskItem.getTime());
            index = cursor.getColumnIndex(TaskSQLiteOpenHelper.COLUMN_TASK_EVALUATION);
            assertEquals(cursor.isNull(index) ? Util.EVALUATION_DEFAULT : cursor.getInt(index), taskItem.getEvaluation());
            index = cursor.getColumnIndex(TaskSQLiteOpenHelper.COLUMN_DAY_TYPE);
            assertEquals(cursor.isNull(index) ? 0 : cursor.getInt(index), taskItem.getDayType());
            index = cursor.getColumnIndex(TaskSQLiteOpenHelper.COLUMN_TASK_INFORMATION);
            assertEquals(cursor.isNull(index) ? null : cursor.getString(index), taskItem.getInformation());
            index = cursor.getColumnIndex(TaskSQLiteOpenHelper.COLUMN_DONE);
            assertEquals(cursor.isNull(index) ? false : (cursor.getInt(index) == Util.DONE), taskItem.getDone());
            index = cursor.getColumnIndex(TaskSQLiteOpenHelper.COLUMN_DONE_TIME);
            assertEquals(cursor.isNull(index) ? null : cursor.getString(index), taskItem.getDoneTime());
            index = cursor.getColumnIndex(TaskSQLiteOpenHelper.COLUMN_TASK_TO_REMAIN);
            assertEquals(cursor.isNull(index) ? false : (cursor.getInt(index) == Util.REMAIN), taskItem.getRemain());
            index = cursor.getColumnIndex(TaskSQLiteOpenHelper.COLUMN_TASK_REMAIN_TIME);
            assertEquals(cursor.isNull(index) ? null : cursor.getString(index), taskItem.getRemainTime());
        }
    }

    @Override
    public void testGetTask( ) throws Exception{
        Cursor cursor = putTestData();
        ArrayList<TaskItem> arrayList = mTaskDbHelper.getTasks();
        assertNotNull(arrayList);
        assertEquals(cursor.getCount(), arrayList.size());
    }

    @Override
    public void testAddTask( ) throws Exception{
        Time time = new Time();
        time.setToNow();
        time.format("%Y%m%dT %H%M%S");
        TaskItem taskItem = new TaskItem(0, mTaskDbHelper.getDayType(), time.toString());
        taskItem.setDone(true);
        taskItem.setEvaluation(Util.EVALUATION_BAD);
        long id = mTaskDbHelper.addTask(taskItem);
        assertNotSame(-1, id);

        TaskSQLiteOpenHelper taskSQLiteOpenHelper = TaskSQLiteOpenHelper.getInstance(mContext);
        Cursor cursor = taskSQLiteOpenHelper.getReadableDatabase().rawQuery("select * from " + TaskSQLiteOpenHelper.TABLE_TASK
                + " where " + TaskSQLiteOpenHelper.COLUMN_DAY_TYPE + "=" + mTaskDbHelper.getDayType() + " and "
                + TaskSQLiteOpenHelper.COLUMN_ID + "=" + id, null);
        cursor.moveToFirst();
        assertEquals(taskItem.getDayType(), cursor.getInt(cursor.getColumnIndex(TaskSQLiteOpenHelper.COLUMN_DAY_TYPE)));
        assertEquals(taskItem.getDone(), cursor.getInt(cursor.getColumnIndex(TaskSQLiteOpenHelper.COLUMN_DONE)) == Util.DONE);
    }

    @Override
    public void testDeleteTask( ) throws Exception{
        deleteTestData();
        TaskItem taskItem = new TaskItem(0, mTaskDbHelper.getDayType(), null);
        long id = mTaskDbHelper.addTask(taskItem);
        int rows = mTaskDbHelper.deleteTask(id);
        assertEquals(1, rows);
    }

    @Override
    public void testSetRemain( ) throws Exception{
        deleteTestData();
        TaskItem taskItem = new TaskItem(0, mTaskDbHelper.getDayType(), null);
        taskItem.setRemain(false);
        long id = mTaskDbHelper.addTask(taskItem);
        int rows = mTaskDbHelper.setTaskRemain(id, "00");
        assertEquals(1, rows);

        SQLiteDatabase sqLiteDatabase = TaskSQLiteOpenHelper.getInstance(mContext).getWritableDatabase();
        Cursor cursor = sqLiteDatabase.rawQuery("select " + TaskSQLiteOpenHelper.COLUMN_TASK_TO_REMAIN  +
                "," + TaskSQLiteOpenHelper.COLUMN_TASK_REMAIN_TIME + " from " + TaskSQLiteOpenHelper.TABLE_TASK  + " where " + TaskSQLiteOpenHelper.COLUMN_ID
                + "=" + id, null);
        cursor.moveToFirst();
        assertTrue(cursor.getInt(0) == Util.REMAIN);
        assertEquals("00", cursor.getString(1));
    }

    @Override
    public void testCancelRemain( ) throws Exception{
        deleteTestData();
        TaskItem taskItem = new TaskItem(0, mTaskDbHelper.getDayType(), null);
        taskItem.setRemain(true);
        taskItem.setRemainTime("00");
        long id = mTaskDbHelper.addTask(taskItem);
        int rows = mTaskDbHelper.cancelTaskRemain(id);
        assertEquals(1, rows);

        SQLiteDatabase sqLiteDatabase = TaskSQLiteOpenHelper.getInstance(mContext).getWritableDatabase();
        Cursor cursor = sqLiteDatabase.rawQuery("select " + TaskSQLiteOpenHelper.COLUMN_TASK_TO_REMAIN  +
                "," + TaskSQLiteOpenHelper.COLUMN_TASK_REMAIN_TIME + " from " + TaskSQLiteOpenHelper.TABLE_TASK  + " where " + TaskSQLiteOpenHelper.COLUMN_ID
                + "=" + id, null);
        cursor.moveToFirst();
        assertTrue(cursor.getInt(0) == Util.UN_REMAIN);
        assertEquals(null, cursor.getString(1));
    }

    @Override
    public void testChangeRemainTime( ) throws Exception{
        deleteTestData();
        TaskItem taskItem = new TaskItem(0, mTaskDbHelper.getDayType(), null);
        taskItem.setRemain(true);
        taskItem.setRemainTime("1");
        long id = mTaskDbHelper.addTask(taskItem);
        int rows = mTaskDbHelper.changeTaskRemainTime(id, "2");
        assertEquals(1, rows);

        SQLiteDatabase sqLiteDatabase = TaskSQLiteOpenHelper.getInstance(mContext).getWritableDatabase();
        Cursor cursor = sqLiteDatabase.rawQuery("select " + TaskSQLiteOpenHelper.COLUMN_TASK_REMAIN_TIME  +
                " from " + TaskSQLiteOpenHelper.TABLE_TASK  + " where " + TaskSQLiteOpenHelper.COLUMN_ID
                + "=" + id, null);
        cursor.moveToFirst();
        assertEquals("2", cursor.getString(0));
    }

    @Override
    public void testUpdateTask() throws Exception {
        deleteTestData();
        TaskItem taskItem = new TaskItem(0, mTaskDbHelper.getDayType(), null);
        taskItem.setRemain(true);
        taskItem.setRemainTime("1");
        taskItem.setDone(false);
        taskItem.setInformation("a");
        long id = mTaskDbHelper.addTask(taskItem);
        taskItem.setId(id);
        taskItem.setRemain(false);
        taskItem.setDone(true);
        taskItem.setDoneTime("2014");
        taskItem.setInformation("b");
        taskItem.setEvaluation(Util.EVALUATION_GOOD);
        int rows = mTaskDbHelper.updateTask(taskItem);
        assertEquals(1, rows);

        TaskSQLiteOpenHelper taskSQLiteOpenHelper = TaskSQLiteOpenHelper.getInstance(mContext);
        Cursor cursor = taskSQLiteOpenHelper.getReadableDatabase().rawQuery("select * from " + TaskSQLiteOpenHelper.TABLE_TASK
                + " where " + TaskSQLiteOpenHelper.COLUMN_DAY_TYPE + "=" + mTaskDbHelper.getDayType() + " and "
                + TaskSQLiteOpenHelper.COLUMN_ID + "=" + id, null);
        cursor.moveToFirst();
        assertTrue(cursor.getInt(cursor.getColumnIndex(TaskSQLiteOpenHelper.COLUMN_DONE)) == Util.DONE);
        assertEquals(taskItem.getDoneTime(), cursor.getString(cursor.getColumnIndex(TaskSQLiteOpenHelper.COLUMN_DONE_TIME)));
        assertTrue(cursor.getInt(cursor.getColumnIndex(TaskSQLiteOpenHelper.COLUMN_TASK_TO_REMAIN)) == Util.UN_REMAIN);
        assertEquals(taskItem.getInformation(), cursor.getString(cursor.getColumnIndex(TaskSQLiteOpenHelper.COLUMN_TASK_INFORMATION)));
        assertTrue(cursor.getInt(cursor.getColumnIndex(TaskSQLiteOpenHelper.COLUMN_TASK_EVALUATION)) == Util.EVALUATION_GOOD);
    }

    @Override
    public void testSetDone( ) throws Exception{
        deleteTestData();
        TaskItem taskItem = new TaskItem(0, mTaskDbHelper.getDayType(), null);
        taskItem.setDone(false);
        long id = mTaskDbHelper.addTask(taskItem);
        int rows = mTaskDbHelper.setTaskDone(id, true);
        assertEquals(1, rows);

        SQLiteDatabase sqLiteDatabase = TaskSQLiteOpenHelper.getInstance(mContext).getWritableDatabase();
        Cursor cursor = sqLiteDatabase.rawQuery("select " + TaskSQLiteOpenHelper.COLUMN_DONE  +
                " from " + TaskSQLiteOpenHelper.TABLE_TASK  + " where " + TaskSQLiteOpenHelper.COLUMN_ID
                + "=" + id, null);
        cursor.moveToFirst();
        assertTrue(cursor.getInt(0) == Util.DONE);
    }

    @Override
    public void testSetDoneTime( ) throws Exception{
        deleteTestData();
        TaskItem taskItem = new TaskItem(0, mTaskDbHelper.getDayType(), null);
        taskItem.setDone(true);
        taskItem.setDoneTime("1");
        long id = mTaskDbHelper.addTask(taskItem);
        int rows = mTaskDbHelper.setTaskDoneTime(id, "2");
        assertEquals(1, rows);

        SQLiteDatabase sqLiteDatabase = TaskSQLiteOpenHelper.getInstance(mContext).getWritableDatabase();
        Cursor cursor = sqLiteDatabase.rawQuery("select " + TaskSQLiteOpenHelper.COLUMN_DONE_TIME  +
                " from " + TaskSQLiteOpenHelper.TABLE_TASK  + " where " + TaskSQLiteOpenHelper.COLUMN_ID
                + "=" + id, null);
        cursor.moveToFirst();
        assertEquals("2", cursor.getString(0));
    }

    @Override
    public void testSetEvaluation( ) throws Exception{
        deleteTestData();
        TaskItem taskItem = new TaskItem(0, mTaskDbHelper.getDayType(), null);
        taskItem.setEvaluation(Util.EVALUATION_BAD);
        long id = mTaskDbHelper.addTask(taskItem);
        int rows = mTaskDbHelper.setTaskEvaluation(id, Util.EVALUATION_GOOD);
        assertEquals(1, rows);

        SQLiteDatabase sqLiteDatabase = TaskSQLiteOpenHelper.getInstance(mContext).getWritableDatabase();
        Cursor cursor = sqLiteDatabase.rawQuery("select " + TaskSQLiteOpenHelper.COLUMN_TASK_EVALUATION  +
                " from " + TaskSQLiteOpenHelper.TABLE_TASK  + " where " + TaskSQLiteOpenHelper.COLUMN_ID
                + "=" + id, null);
        cursor.moveToFirst();
        assertEquals(Util.EVALUATION_GOOD, cursor.getInt(0));
    }
}
