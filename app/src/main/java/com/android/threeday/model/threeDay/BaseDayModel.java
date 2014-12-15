package com.android.threeday.model.threeDay;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.format.Time;
import android.util.Log;

import com.android.threeday.model.BaseModel;
import com.android.threeday.util.Util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

/**
 * Created by user on 2014/11/2.
 */
public abstract class BaseDayModel implements BaseModel {
    protected Context mContext;
    protected TaskDbHelper mTaskDbHelper;
    protected ArrayList<TaskItem> mDoneTaskItems;
    protected ArrayList<TaskItem> mUndoneTaskItems;
    private Comparator<TaskItem> mDoneTasksComparator = new Comparator<TaskItem>() {
        @Override
        public int compare(TaskItem lhs, TaskItem rhs) {
            return -lhs.getDoneTime().compareTo(rhs.getDoneTime());
        }
    };
    private Comparator<TaskItem> mUndoneTasksComparator = new Comparator<TaskItem>() {
        @Override
        public int compare(TaskItem lhs, TaskItem rhs) {
            return -lhs.getTime().compareTo(rhs.getTime());
        }
    };

    protected int dayType;
    protected int dayEvaluation;

    public BaseDayModel(Context context){
        super();
        this.mContext = context;
        initData( );
    }

    protected abstract TaskDbHelper getDbHelper( );

    protected abstract int getDayType( );

    protected abstract int getBaseDayEvaluation( );

    private void initData( ){
        this.mTaskDbHelper = getDbHelper();
        this.dayType = getDayType();
        this.mDoneTaskItems = new ArrayList<TaskItem>();
        this.mUndoneTaskItems = new ArrayList<TaskItem>();
        reloadData();
    }

    public void updateTasks( ){
        this.mUndoneTaskItems.clear();
        this.mDoneTaskItems.clear();
        ArrayList<TaskItem> arrayList = this.mTaskDbHelper.getTasks();
        for(TaskItem taskItem : arrayList){
            if(taskItem.getDone()){
                this.mDoneTaskItems.add(taskItem);
            }else{
                this.mUndoneTaskItems.add(taskItem);
            }
        }
        sortDoneTasks();
        sortUndoneTask();
    }

    public void reloadData( ){
        updateTasks();
        this.dayEvaluation = getBaseDayEvaluation();
    }

    public ArrayList<TaskItem> getDoneTasks( ){
        return this.mDoneTaskItems;
    }

    public ArrayList<TaskItem> getUndoneTasks( ){
        return this.mUndoneTaskItems;
    }

    public boolean addTask(TaskItem taskItem) {
        long id = this.mTaskDbHelper.addTask(taskItem);
        Log.e("wind", "id " + id);
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

    public void deleteAllDayTasks( ){
        this.mTaskDbHelper.deleteAllDayTasks( );
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

    public boolean setUndoneTaskRemain(int position, String remainTime){
        if(position < this.mUndoneTaskItems.size()){
            TaskItem taskItem = this.mUndoneTaskItems.get(position);
            long id = taskItem.getId();
            if(this.mTaskDbHelper.setTaskRemain(id, remainTime) == 1){
                taskItem.setRemain(true);
                taskItem.setRemainTime(remainTime);
                return true;
            }
        }
        return false;
    }

    public boolean cancelUndoneTaskRemain(int position){
        if(position < this.mUndoneTaskItems.size()){
            TaskItem taskItem = this.mUndoneTaskItems.get(position);
            long id = taskItem.getId();
            if(this.mTaskDbHelper.cancelTaskRemain(id) == 1){
                taskItem.setRemain(false);
                return true;
            }
        }
        return false;
    }

    public boolean changeUndoneTaskRemainTime(int position, String remainTime){
        if(position < this.mUndoneTaskItems.size()){
            TaskItem taskItem = this.mUndoneTaskItems.get(position);
            long id = taskItem.getId();
            if(this.mTaskDbHelper.changeTaskRemainTime(id, remainTime) == 1){
                taskItem.setRemainTime(remainTime);
                return true;
            }
        }
        return false;
    }

    public boolean doneTask(int position, String doneTime, int evaluation) throws CloneNotSupportedException {
        if(position < this.mUndoneTaskItems.size()){
            TaskItem taskItem = this.mUndoneTaskItems.get(position);
            TaskItem tempTaskItem = (TaskItem) taskItem.clone();
            tempTaskItem.setDone(true);
            tempTaskItem.setDoneTime(doneTime);
            tempTaskItem.setEvaluation(evaluation);
            if(this.mTaskDbHelper.updateTask(tempTaskItem) == 1){
                this.mUndoneTaskItems.remove(position);
                this.mDoneTaskItems.add(0, tempTaskItem);
                return true;
            }
        }
        return false;
    }

    public boolean undoneTask(int position) throws CloneNotSupportedException {
        if(position < this.mDoneTaskItems.size()){
            TaskItem taskItem = this.mDoneTaskItems.get(position);
            TaskItem tempTaskItem = (TaskItem) taskItem.clone();
            tempTaskItem.setDone(false);
            Time time = new Time();
            time.setToNow();
            tempTaskItem.setTime(time.format2445());
            if(this.mTaskDbHelper.updateTask(tempTaskItem) == 1){
                this.mDoneTaskItems.remove(position);
                this.mUndoneTaskItems.add(0, tempTaskItem);
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

    public void sortDoneTasks( ){
        Collections.sort(this.mDoneTaskItems, this.mDoneTasksComparator);
    }

    public void sortUndoneTask( ){
        Collections.sort(this.mUndoneTaskItems, this.mUndoneTasksComparator);
    }

    public void resetDataBase( ){
        this.mTaskDbHelper.resetSQLiteIdToZero( );
    }
}
