package com.android.threeday.model;

import android.content.Context;
import android.content.SharedPreferences;

import com.android.threeday.util.Util;

import java.util.ArrayList;

/**
 * Created by user on 2014/11/2.
 */
public abstract class BaseDayModel implements BaseModel{
    protected Context mContext;
    protected TaskDbHelper mTaskDbHelper;
    protected ArrayList<TaskItem> mDoneTaskItems;
    protected ArrayList<TaskItem> mUndoneTaskItems;
    protected int dayType;
    protected int dayEvaluation;

    public BaseDayModel(Context context){
        super();
        this.mContext = context;
        initData( );
    }

    protected abstract TaskDbHelper getDbHelper( );

    protected abstract int getDayType( );

    private void initData( ){
        this.mTaskDbHelper = getDbHelper();
        this.dayType = getDayType();
        ArrayList<TaskItem> arrayList = this.mTaskDbHelper.getTasks();
        this.mDoneTaskItems = new ArrayList<TaskItem>();
        this.mUndoneTaskItems = new ArrayList<TaskItem>();
        for(TaskItem taskItem : arrayList){
            if(taskItem.getDone()){
                this.mDoneTaskItems.add(taskItem);
            }else{
                this.mUndoneTaskItems.add(taskItem);
            }
        }
        SharedPreferences sharedPreferences = this.mContext.getSharedPreferences(Util.PREFERENCE_NAME, Context.MODE_PRIVATE);
        this.dayEvaluation = sharedPreferences.getInt(Util.PREFERENCE_KEY_DAY_EVALUATION, Util.EVALUATION_DEFAULT);
    }

    public ArrayList<TaskItem> getDoneTasks( ){
        return this.mDoneTaskItems;
    }

    public ArrayList<TaskItem> getUndoneTasks( ){
        return this.mUndoneTaskItems;
    }

    public boolean addTask(TaskItem taskItem){
        long id = this.mTaskDbHelper.addTask(taskItem);
        if(id != -1){
            taskItem.setId(id);
            if(taskItem.getDone()){
                this.mDoneTaskItems.add(0, taskItem);
            }else{
                this.mUndoneTaskItems.add(0, taskItem);
            }
            return true;
        }
        return false;
    }

    public boolean deleteUndoneTask(int position){
        if(position < this.mUndoneTaskItems.size()){
            long id = this.mUndoneTaskItems.get(position).getId();
            if(this.mTaskDbHelper.deleteTask(id) != 0){
                this.mUndoneTaskItems.remove(position);
                return true;
            }
        }
        return false;
    }

    public boolean deleteDoneTask(int position){
        if(position < this.mDoneTaskItems.size()){
            long id = this.mDoneTaskItems.get(position).getId();
            if(this.mTaskDbHelper.deleteTask(id) != 0){
                this.mDoneTaskItems.remove(position);
                return true;
            }
        }
        return false;
    }

    public boolean setUndoneTaskRemain(int position, boolean remain){
        if(position < this.mUndoneTaskItems.size()){
            TaskItem taskItem = this.mUndoneTaskItems.get(position);
            long id = taskItem.getId();
            if(this.mTaskDbHelper.setTaskRemain(id, remain) == 1){
                taskItem.setRemain(remain);
                return true;
            }
        }
        return false;
    }

    public boolean setUndoneTaskRemainTime(int position, String time){
        if(position < this.mUndoneTaskItems.size()){
            TaskItem taskItem = this.mUndoneTaskItems.get(position);
            long id = taskItem.getId();
            if(this.mTaskDbHelper.setTaskRemainTime(id, time) == 1){
                taskItem.setRemainTime(time);
                return true;
            }
        }
        return false;
    }

    public boolean doneTask(int position, String doneTime, int evaluation){
        if(position < this.mUndoneTaskItems.size()){
            TaskItem taskItem = this.mUndoneTaskItems.get(position);
            taskItem.setDone(true);
            taskItem.setDoneTime(doneTime);
            taskItem.setEvaluation(evaluation);
            if(this.mTaskDbHelper.updateTask(taskItem) == 1){
                this.mUndoneTaskItems.remove(position);
                this.mDoneTaskItems.add(0, taskItem);
                return true;
            }else{
                taskItem.setDone(false);
                taskItem.setEvaluation(Util.EVALUATION_DEFAULT);
            }
        }
        return false;
    }

    public boolean setDayEvaluation(int evaluation){
        return false;
    }

    public int getDayEvaluation( ){
        return this.dayEvaluation;
    }
}
