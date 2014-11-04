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
    protected ArrayList<TaskItem> mTaskItems;
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
        this.mTaskItems = this.mTaskDbHelper.getTasks();
        SharedPreferences sharedPreferences = this.mContext.getSharedPreferences(Util.PREFERENCE_NAME, Context.MODE_PRIVATE);
        this.dayEvaluation = sharedPreferences.getInt(Util.PREFERENCE_KEY_DAY_EVALUATION, Util.EVALUATION_DEFAULT);
    }

    public ArrayList<TaskItem> getTasks( ){
        return this.mTaskItems;
    }

    public boolean addTask(TaskItem taskItem){
        long id = this.mTaskDbHelper.addTask(taskItem);
        if(id != -1){
            taskItem.setId(id);
            this.mTaskItems.add(taskItem);
            return true;
        }
        return false;
    }

    public boolean deleteTask(int position){
        if(position < this.mTaskItems.size()){
            long id = this.mTaskItems.get(position).getId();
            if(this.mTaskDbHelper.deleteTask(id) != 0){
                this.mTaskItems.remove(position);
                return true;
            }
        }
        return false;
    }

    public boolean setTaskRemain(int position, boolean remain){
        if(position < this.mTaskItems.size()){
            TaskItem taskItem = this.mTaskItems.get(position);
            long id = taskItem.getId();
            if(this.mTaskDbHelper.setTaskRemain(id, remain) == 1){
                taskItem.setRemain(remain);
                return true;
            }
        }
        return false;
    }

    public boolean setTaskRemainTime(int position, String time){
        if(position < this.mTaskItems.size()){
            TaskItem taskItem = this.mTaskItems.get(position);
            long id = taskItem.getId();
            if(this.mTaskDbHelper.setTaskRemainTime(id, time) == 1){
                taskItem.setRemainTime(time);
                return true;
            }
        }
        return false;
    }

    public boolean setTaskDone(int position, boolean done){
        if(position < this.mTaskItems.size()){
            TaskItem taskItem = this.mTaskItems.get(position);
            long id = taskItem.getId();
            if(this.mTaskDbHelper.setTaskDone(id, done) == 1){
                taskItem.setDone(done);
                return true;
            }
        }
        return false;
    }

    public boolean setTaskDoneTime(int position, String time){
        if(position < this.mTaskItems.size()){
            TaskItem taskItem = this.mTaskItems.get(position);
            long id = taskItem.getId();
            if(this.mTaskDbHelper.setTaskDoneTime(id, time) == 1){
                taskItem.setDoneTime(time);
                return true;
            }
        }
        return false;
    }

    public boolean setTaskEvaluation(int position, int evaluation){
        if(position < this.mTaskItems.size()){
            TaskItem taskItem = this.mTaskItems.get(position);
            long id = taskItem.getId();
            if(this.mTaskDbHelper.setTaskEvaluation(id, evaluation) == 1){
                taskItem.setEvaluation(evaluation);
                return true;
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
