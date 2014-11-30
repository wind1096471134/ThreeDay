package com.android.threeday.model.threeDay;

import android.content.ContentValues;
import android.test.ActivityInstrumentationTestCase2;

import com.android.threeday.activity.mainActivity.MainActivity;


/**
 * Created by user on 2014/10/30.
 */
public class TaskSQLiteOpenHelperTest extends ActivityInstrumentationTestCase2<MainActivity> {
    private TaskSQLiteOpenHelper taskSQLiteOpenHelper;

    public TaskSQLiteOpenHelperTest( ){
        super(MainActivity.class);
    }

    @Override
    protected void tearDown() throws Exception {
        super.tearDown();
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        taskSQLiteOpenHelper = TaskSQLiteOpenHelper.getInstance(getActivity());
    }

    public void testPre( ) throws Exception{
        assertNotNull(taskSQLiteOpenHelper);
    }

    public void testGetDatabaseIsNull( ) throws Exception{
        assertNotNull(taskSQLiteOpenHelper.getReadableDatabase());
        assertNotNull(taskSQLiteOpenHelper.getWritableDatabase());
    }

    public void testCreateTableSuccess( ) throws Exception{
        ContentValues contentValues = new ContentValues(1);
        contentValues.put(TaskSQLiteOpenHelper.COLUMN_TASK_INFORMATION, "test");
        taskSQLiteOpenHelper.getWritableDatabase().insert(TaskSQLiteOpenHelper.TABLE_TASK, null, contentValues);
    }
}
