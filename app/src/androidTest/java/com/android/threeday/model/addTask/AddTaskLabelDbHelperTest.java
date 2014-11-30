package com.android.threeday.model.addTask;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.test.ActivityInstrumentationTestCase2;

import com.android.threeday.activity.addTaskActivity.AddTaskActivity;

import java.util.ArrayList;
import java.util.Random;

/**
 * Created by user on 2014/11/27.
 */
public class AddTaskLabelDbHelperTest extends ActivityInstrumentationTestCase2<AddTaskActivity> {
    private AddTaskLabelDbHelper mAddTaskLabelDbHelper;

    public AddTaskLabelDbHelperTest( ){
        super(AddTaskActivity.class);
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        this.mAddTaskLabelDbHelper = new AddTaskLabelDbHelper(getActivity());
    }

    public void testInstanceNotNull( ) throws Exception{
        assertNotNull(this.mAddTaskLabelDbHelper);
    }

    private void deleteTestData( ){
        AddTaskLabelSQLiteOpenHelper addTaskLabelSQLiteOpenHelper = AddTaskLabelSQLiteOpenHelper.getInstance(getActivity());
        addTaskLabelSQLiteOpenHelper.getWritableDatabase().delete(AddTaskLabelSQLiteOpenHelper.TABLE_NAME, null, null);
    }

    private Cursor putTestData( ){
        deleteTestData();
        int num = new Random().nextInt(3);
        ContentValues contentValues = new ContentValues();
        contentValues.put(AddTaskLabelSQLiteOpenHelper.COLUMN_LABEL_NAME, "");
        SQLiteDatabase database = AddTaskLabelSQLiteOpenHelper.getInstance(getActivity()).getWritableDatabase();
        for(int i = 0; i < num; i++){
            database.insert(AddTaskLabelSQLiteOpenHelper.TABLE_NAME, null, contentValues);
        }
        Cursor cursor = database.rawQuery("select * from " + AddTaskLabelSQLiteOpenHelper.TABLE_NAME
                , null);
        return cursor;
    }

    public void testAddLabel( ) throws Exception{
        deleteTestData();
        long id = this.mAddTaskLabelDbHelper.addLabel("label");
        assertNotSame(-1, id);

        SQLiteDatabase sqLiteDatabase = AddTaskLabelSQLiteOpenHelper.getInstance(getActivity()).getWritableDatabase();
        Cursor cursor = sqLiteDatabase.rawQuery("select * from " + AddTaskLabelSQLiteOpenHelper.TABLE_NAME + " where "
            + AddTaskLabelSQLiteOpenHelper.COLUMN_ID + "=" + id, null);
        cursor.moveToFirst();
        assertEquals("label", cursor.getString(1));
    }

    public void testDeleteLabel( ) throws Exception{
        deleteTestData();
        long id = this.mAddTaskLabelDbHelper.addLabel("label");
        assertNotSame(-1, id);
        int rows = this.mAddTaskLabelDbHelper.deleteLabel(id);
        assertEquals(1, rows);
    }

    public void testGetLabels( ) throws Exception{
        deleteTestData();
        Cursor cursor = putTestData();
        ArrayList<Label> arrayList = this.mAddTaskLabelDbHelper.getLabels( );
        assertNotNull(arrayList);
        assertEquals(cursor.getCount(), arrayList.size());
    }
}
