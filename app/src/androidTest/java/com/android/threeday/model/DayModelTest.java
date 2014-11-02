package com.android.threeday.model;

import android.content.Context;
import android.content.SharedPreferences;

import com.android.threeday.model.testInterface.DayModelInterface;
import com.android.threeday.util.Util;

import static junit.framework.Assert.*;

import java.util.ArrayList;

/**
 * Created by user on 2014/11/2.
 */
public class DayModelTest implements DayModelInterface {
    private BaseDayModel mModel;
    private Context mContext;

    public DayModelTest(Context context, BaseDayModel model){
        this.mContext = context;
        this.mModel = model;
    }

    @Override
    public void testMemberNull() throws Exception {
        assertNotNull(this.mModel.mContext);
        assertNotNull(this.mModel.mTaskDbHelper);
        assertNotNull(this.mModel.mTaskItems);
    }

    @Override
    public void testGetDayEvaluation() throws Exception {
        SharedPreferences sharedPreferences = this.mContext.getSharedPreferences(Util.PREFERENCE_NAME, Context.MODE_PRIVATE);
        boolean result = sharedPreferences.edit().putInt(Util.PREFERENCE_KEY_DAY_EVALUATION, Util.EVALUATION_BAD).commit();
        assertTrue(result);
        int evaluation = this.mModel.getDayEvaluation();
        assertEquals(Util.EVALUATION_BAD, evaluation);
    }

    @Override
    public void testSetDayEvaluation() throws Exception {
        SharedPreferences sharedPreferences = this.mContext.getSharedPreferences(Util.PREFERENCE_NAME, Context.MODE_PRIVATE);
        sharedPreferences.edit().putInt(Util.PREFERENCE_KEY_DAY_EVALUATION, -1).commit();
        boolean result = this.mModel.setDayEvaluation(Util.EVALUATION_GOOD);
        assertTrue(result);
        int evaluation = sharedPreferences.getInt(Util.PREFERENCE_KEY_DAY_EVALUATION, Util.EVALUATION_DEFAULT);
        assertEquals(Util.EVALUATION_GOOD, evaluation);
    }

    @Override
    public void testGetTask() throws Exception {
        ArrayList<TaskItem> arrayList = this.mModel.getTasks();
        assertNotNull(arrayList);
    }

    @Override
    public void testAddTask() throws Exception {
        TaskItem taskItem = addOneTaskForTest();
        assertTrue(this.mModel.getTasks().contains(taskItem));
    }

    @Override
    public void testDeleteTask() throws Exception {
        addOneTaskForTest();
        ArrayList<TaskItem> arrayList = this.mModel.getTasks();
        TaskItem taskItem = arrayList.get(0);
        boolean result = this.mModel.deleteTask(0);
        assertTrue(result);
        assertTrue(arrayList.isEmpty() || arrayList.get(0) != taskItem);
    }

    @Override
    public void testSetRemain() throws Exception {
        addOneTaskForTest();
        TaskItem taskItem = this.mModel.getTasks().get(0);
        boolean remain = !taskItem.getRemain();
        boolean result = this.mModel.setTaskRemain(0, remain);
        assertTrue(result);
        assertTrue(taskItem.getRemain() == remain);
    }

    @Override
    public void testSetRemainTime() throws Exception {
        addOneTaskForTest();
        TaskItem taskItem = this.mModel.getTasks().get(0);
        String time = taskItem.getRemainTime() + "00";
        boolean result = this.mModel.setTaskRemainTime(0, time);
        assertTrue(result);
        assertEquals(time, taskItem.getRemainTime());
    }

    @Override
    public void testSetDone() throws Exception {
        addOneTaskForTest();
        TaskItem taskItem = this.mModel.getTasks().get(0);
        boolean done = !taskItem.getDone();
        boolean result = this.mModel.setTaskDone(0, done);
        assertTrue(result);
        assertTrue(taskItem.getDone() == done);
    }

    @Override
    public void testSetDoneTime() throws Exception {
        addOneTaskForTest();
        TaskItem taskItem = this.mModel.getTasks().get(0);
        String time = taskItem.getDoneTime() + "00";
        boolean result = this.mModel.setTaskDoneTime(0, time);
        assertTrue(result);
        assertEquals(time, taskItem.getDoneTime());
    }

    @Override
    public void testSetEvaluation() throws Exception {
        addOneTaskForTest();
        TaskItem taskItem = this.mModel.getTasks().get(0);
        int evaluation = taskItem.getEvaluation();
        switch (evaluation){
            case Util.EVALUATION_BAD:
                evaluation = Util.EVALUATION_GOOD;
                break;
            case Util.EVALUATION_GOOD:
                evaluation = Util.EVALUATION_BAD;
                break;
            case Util.EVALUATION_MID:
                evaluation = Util.EVALUATION_BAD;
                break;
            case Util.EVALUATION_DEFAULT:
                evaluation = Util.EVALUATION_BAD;
        }
        boolean result = this.mModel.setTaskEvaluation(0, evaluation);
        assertTrue(result);
        assertEquals(evaluation, taskItem.getEvaluation());
    }

    private TaskItem addOneTaskForTest( ) throws Exception{
        TaskItem taskItem = new TaskItem(0, this.mModel.getDayType(), "");
        boolean result = this.mModel.addTask(taskItem);
        assertTrue(result);
        return taskItem;
    }
}
