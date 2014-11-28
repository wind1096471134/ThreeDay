package com.android.threeday.model.addTask;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.android.threeday.model.DbHelper;

import java.util.ArrayList;

/**
 * Created by user on 2014/11/27.
 */
class AddTaskLabelDbHelper implements DbHelper{
    private AddTaskLabelSQLiteOpenHelper mAddTaskLabelSQLiteOpenHelper;

    private final String QUERY_LABELS = "SELECT * " + " FROM " + AddTaskLabelSQLiteOpenHelper.TABLE_NAME +
            " ORDER BY " + AddTaskLabelSQLiteOpenHelper.COLUMN_ID + " DESC";

    AddTaskLabelDbHelper(Context context){
        this.mAddTaskLabelSQLiteOpenHelper = AddTaskLabelSQLiteOpenHelper.getInstance(context);
    }

    long addLabel(String labelName){
        SQLiteDatabase sqLiteDatabase = this.mAddTaskLabelSQLiteOpenHelper.getWritableDatabase();
        ContentValues contentValues = new ContentValues(1);
        contentValues.put(AddTaskLabelSQLiteOpenHelper.COLUMN_LABEL_NAME, labelName);
        return sqLiteDatabase.insert(AddTaskLabelSQLiteOpenHelper.TABLE_NAME, null, contentValues);
    }

    int deleteLabel(long id){
        SQLiteDatabase sqLiteDatabase = this.mAddTaskLabelSQLiteOpenHelper.getWritableDatabase();
        return sqLiteDatabase.delete(AddTaskLabelSQLiteOpenHelper.TABLE_NAME, AddTaskLabelSQLiteOpenHelper.COLUMN_ID + "=?", new String[]{Long.toString(id)});
    }

    ArrayList<Label> getLabels( ){
        SQLiteDatabase sqLiteDatabase = this.mAddTaskLabelSQLiteOpenHelper.getReadableDatabase();
        Cursor cursor = sqLiteDatabase.rawQuery(QUERY_LABELS, null);
        cursor.moveToFirst();
        ArrayList<Label> result = new ArrayList<Label>(cursor.getCount());
        if(cursor.moveToFirst()){
            Label label;
            do{
                label = new Label(cursor.getLong(cursor.getColumnIndex(AddTaskLabelSQLiteOpenHelper.COLUMN_ID)),
                        cursor.getString(cursor.getColumnIndex(AddTaskLabelSQLiteOpenHelper.COLUMN_LABEL_NAME)));
                result.add(label);
            }while (cursor.moveToNext());
        }
        return result;
    }

}
