package com.android.threeday.model.addTask;

import android.content.ContentValues;
import android.test.ActivityInstrumentationTestCase2;

import com.android.threeday.activity.addTaskActivity.AddTaskActivity;

/**
 * Created by user on 2014/11/27.
 */
public class AddTaskLabelSQLiteOpenHelperTest extends ActivityInstrumentationTestCase2<AddTaskActivity>{
    private AddTaskLabelSQLiteOpenHelper mAddTaskLabelSQLiteOpenHelper;

    public AddTaskLabelSQLiteOpenHelperTest( ) {
        super(AddTaskActivity.class);
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        this.mAddTaskLabelSQLiteOpenHelper = AddTaskLabelSQLiteOpenHelper.getInstance(getActivity());
    }

    public void testPre( ) throws Exception{
        assertNotNull(mAddTaskLabelSQLiteOpenHelper);
    }

    public void testGetDatabaseIsNull( ) throws Exception{
        assertNotNull(mAddTaskLabelSQLiteOpenHelper.getReadableDatabase());
        assertNotNull(mAddTaskLabelSQLiteOpenHelper.getWritableDatabase());
    }

    public void testCreateTableSuccess( ) throws Exception{
        ContentValues contentValues = new ContentValues(1);
        contentValues.put(mAddTaskLabelSQLiteOpenHelper.COLUMN_LABEL_NAME, "label");
        mAddTaskLabelSQLiteOpenHelper.getWritableDatabase().insert(AddTaskLabelSQLiteOpenHelper.TABLE_NAME, null, contentValues);
    }
}
