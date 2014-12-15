package com.android.threeday.model.updateData;

import android.content.Context;

import com.android.threeday.model.BaseModel;
import com.android.threeday.model.threeDay.BaseDayModel;
import com.android.threeday.model.threeDay.TaskItem;
import com.android.threeday.model.threeDay.TodayModel;
import com.android.threeday.model.threeDay.TomorrowModel;
import com.android.threeday.model.threeDay.YesterdayModel;
import com.android.threeday.util.Util;

import java.util.ArrayList;

/**
 * Created by user on 2014/12/13.
 */
public class UpdateDataModel implements BaseModel {
    private Context mContext;
    private YesterdayModel mYesterdayModel;
    private TodayModel mTodayModel;
    private TomorrowModel mTomorrowModel;

    public UpdateDataModel(Context context){
        this.mContext = context;
        initData(context);
    }

    private void initData(Context context){
        this.mYesterdayModel = new YesterdayModel(context);
        this.mTodayModel = new TodayModel(context);
        this.mTomorrowModel = new TomorrowModel(context);
    }

    private void addTasks(ArrayList<TaskItem> arrayList, BaseDayModel model, int dayType){
        for(TaskItem taskItem : arrayList){
            taskItem.setDayType(dayType);
            model.addTask(taskItem);
        }
    }

    public void updateDataAtNewDay( ){
        ArrayList<TaskItem> todayDoneArrayList = this.mTodayModel.getDoneTasks();
        ArrayList<TaskItem> todayUndoneArrayList = this.mTodayModel.getUndoneTasks();
        ArrayList<TaskItem> tomorrowArrayList = this.mTomorrowModel.getUndoneTasks();
        this.mYesterdayModel.deleteAllDayTasks();
        this.mTodayModel.deleteAllDayTasks();
        this.mTomorrowModel.deleteAllDayTasks();
        this.mTodayModel.resetDataBase();
        addTasks(todayDoneArrayList, this.mYesterdayModel, Util.TYPE_YESTERDAY);
        addTasks(todayUndoneArrayList, this.mYesterdayModel, Util.TYPE_YESTERDAY);
        addTasks(tomorrowArrayList, this.mTodayModel, Util.TYPE_TODAY);

        this.mYesterdayModel.setDayEvaluation(this.mTodayModel.getDayEvaluation());
        this.mTodayModel.setDayEvaluation(Util.EVALUATION_DEFAULT);
    }
}
