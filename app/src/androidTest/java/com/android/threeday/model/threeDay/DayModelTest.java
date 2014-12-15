package com.android.threeday.model.threeDay;

import android.content.Context;
import android.content.SharedPreferences;

import com.android.threeday.model.testInterface.DayModelInterface;
import com.android.threeday.util.Util;

import static junit.framework.Assert.*;

import java.util.ArrayList;


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
        assertNotNull(this.mModel.mDoneTaskItems);
        assertNotNull(this.mModel.mUndoneTaskItems);
    }

    @Override
    public void testGetDayEvaluation() throws Exception {
        SharedPreferences sharedPreferences = this.mContext.getSharedPreferences(Util.PREFERENCE_NAME, Context.MODE_PRIVATE);
        int evaluation;
        if(this.mModel.getDayType() == Util.TYPE_YESTERDAY){
           evaluation = sharedPreferences.getInt(Util.PREFERENCE_KEY_YESTERDAY_EVALUATION, Util.EVALUATION_DEFAULT);
        }else if(this.mModel.getDayType() == Util.TYPE_TODAY){
            evaluation = sharedPreferences.getInt(Util.PREFERENCE_KEY_TODAY_EVALUATION, Util.EVALUATION_DEFAULT);
        }else{
            evaluation = Util.EVALUATION_DEFAULT;
        }
        assertEquals(evaluation, this.mModel.getDayEvaluation());
    }

    @Override
    public void testSetDayEvaluation() throws Exception {
        if(this.mModel.getDayType() == Util.TYPE_TOMORROW){
            return;
        }
        SharedPreferences sharedPreferences = this.mContext.getSharedPreferences(Util.PREFERENCE_NAME, Context.MODE_PRIVATE);
        String dayKey = this.mModel.getDayType() == Util.TYPE_YESTERDAY ? Util.PREFERENCE_KEY_YESTERDAY_EVALUATION : Util.PREFERENCE_KEY_TODAY_EVALUATION;
        sharedPreferences.edit().putInt(dayKey, -1).commit();
        boolean result = this.mModel.setDayEvaluation(Util.EVALUATION_GOOD);
        assertTrue(result);
        int evaluation = sharedPreferences.getInt(dayKey, Util.EVALUATION_DEFAULT);
        assertEquals(Util.EVALUATION_GOOD, evaluation);
    }

    @Override
    public void testDoneTask() throws Exception {
        addOneTaskForTest(false);
        TaskItem taskItem = this.mModel.getUndoneTasks().get(0);
        int doneTaskNum = this.mModel.getDoneTasks().size();
        boolean result = this.mModel.doneTask(0, "00", Util.EVALUATION_GOOD);
        assertTrue(result);
        assertFalse(this.mModel.getUndoneTasks().contains(taskItem));

        assertEquals(doneTaskNum + 1, this.mModel.getDoneTasks().size());
        taskItem = this.mModel.getDoneTasks().get(0);
        assertTrue(taskItem.getDone());
        assertEquals("00", taskItem.getDoneTime());
        assertEquals(Util.EVALUATION_GOOD, taskItem.getEvaluation());
    }

    @Override
    public void testUndoneTask() throws Exception {
        addOneTaskForTest(true);
        TaskItem taskItem = this.mModel.getDoneTasks().get(0);
        int undoneTaskNum = this.mModel.getUndoneTasks().size();
        boolean result = this.mModel.undoneTask(0);
        assertTrue(result);
        assertFalse(this.mModel.getDoneTasks().contains(taskItem));
        assertEquals(undoneTaskNum + 1, this.mModel.getUndoneTasks().size());
    }

    @Override
    public void testGetTask() throws Exception {
        ArrayList<TaskItem> arrayList = this.mModel.getDoneTasks();
        assertNotNull(arrayList);

        arrayList = this.mModel.getUndoneTasks();
        assertNotNull(arrayList);
    }

    @Override
    public void testAddTask() throws Exception {
        TaskItem taskItem = addOneTaskForTest(false);
        assertTrue(this.mModel.getUndoneTasks().contains(taskItem));

        taskItem = addOneTaskForTest(true);
        assertTrue(this.mModel.getDoneTasks().contains(taskItem));
    }

    @Override
    public void testDeleteTask() throws Exception {
        addOneTaskForTest(true);
        ArrayList<TaskItem> arrayList = this.mModel.getDoneTasks();
        TaskItem taskItem = arrayList.get(0);
        boolean result = this.mModel.deleteDoneTask(0);
        assertTrue(result);
        assertTrue(arrayList.isEmpty() || arrayList.get(0) != taskItem);

        addOneTaskForTest(false);
        arrayList = this.mModel.getUndoneTasks();
        taskItem = arrayList.get(0);
        result = this.mModel.deleteUndoneTask(0);
        assertTrue(result);
        assertTrue(arrayList.isEmpty() || arrayList.get(0) != taskItem);
    }

    @Override
    public void testSetRemain() throws Exception {
        addOneTaskForTest(false);
        TaskItem taskItem = this.mModel.getUndoneTasks().get(0);
        boolean result = this.mModel.setUndoneTaskRemain(0, "00");
        assertTrue(result);
        assertTrue(taskItem.getRemain());
        assertEquals("00", taskItem.getRemainTime());
    }

    @Override
    public void testCancelRemain() throws Exception {
        addOneTaskForTest(false);
        TaskItem taskItem = this.mModel.getUndoneTasks().get(0);
        boolean result = this.mModel.cancelUndoneTaskRemain(0);
        assertTrue(result);
        assertFalse(taskItem.getRemain());
        assertEquals(null, taskItem.getRemainTime());
    }

    @Override
    public void testChangeRemainTime() throws Exception {
        addOneTaskForTest(false);
        TaskItem taskItem = this.mModel.getUndoneTasks().get(0);
        String time = taskItem.getRemainTime() + "00";
        boolean result = this.mModel.changeUndoneTaskRemainTime(0, time);
        assertTrue(result);
        assertEquals(time, taskItem.getRemainTime());
    }

    private TaskItem addOneTaskForTest(boolean done) throws Exception{
        TaskItem taskItem = new TaskItem(0, this.mModel.getDayType(), "");
        taskItem.setDone(done);
        boolean result = this.mModel.addTask(taskItem);
        assertTrue(result);
        return taskItem;
    }

    @Override
    public void testUpdateTasks( ) throws Exception{
        addOneTaskForTest(false);
        ArrayList<TaskItem> undoneArrayList = this.mModel.getUndoneTasks();
        int size = undoneArrayList.size();

        TaskDbHelper taskDbHelper = new TodayTaskDbHelper(this.mContext);
        int rows = taskDbHelper.deleteTask(undoneArrayList.get(0).getId());
        assertEquals(1, rows);

        this.mModel.updateTasks();
        undoneArrayList = this.mModel.getUndoneTasks();
        assertEquals(size - 1, undoneArrayList.size());
    }

}
