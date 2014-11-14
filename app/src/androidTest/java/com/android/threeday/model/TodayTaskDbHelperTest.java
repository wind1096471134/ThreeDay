package com.android.threeday.model;

import android.test.ActivityInstrumentationTestCase2;

import com.android.threeday.activity.mainActivity.MainActivity;
import com.android.threeday.model.testInterface.TaskDbInterface;

/**
 * Created by user on 2014/11/1.
 */
public class TodayTaskDbHelperTest extends ActivityInstrumentationTestCase2<MainActivity> implements TaskDbInterface {
    private TaskDbInterface mTaskDbHelperTest;

    public TodayTaskDbHelperTest(){
        super(MainActivity.class);
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        mTaskDbHelperTest = new TaskDbHelperTest(getActivity(), new TodayTaskDbHelper(getActivity()));
    }

    @Override
    public void testFillTasks() throws Exception {
        mTaskDbHelperTest.testFillTasks();
    }

    @Override
    public void testGetTask() throws Exception {
        mTaskDbHelperTest.testGetTask();
    }

    @Override
    public void testAddTask() throws Exception {
        mTaskDbHelperTest.testAddTask();
    }

    @Override
    public void testDeleteTask() throws Exception {
        mTaskDbHelperTest.testDeleteTask();
    }

    @Override
    public void testSetRemain() throws Exception {
        mTaskDbHelperTest.testSetRemain();
    }

    @Override
    public void testChangeRemainTime() throws Exception {
        this.mTaskDbHelperTest.testChangeRemainTime();
    }

    @Override
    public void testCancelRemain() throws Exception {
        this.mTaskDbHelperTest.testCancelRemain();
    }

    @Override
    public void testUpdateTask() throws Exception {
        mTaskDbHelperTest.testUpdateTask();
    }

    @Override
    public void testSetDone() throws Exception {
        mTaskDbHelperTest.testSetDone();
    }

    @Override
    public void testSetDoneTime() throws Exception {
        mTaskDbHelperTest.testSetDoneTime();
    }

    @Override
    public void testSetEvaluation() throws Exception {
        mTaskDbHelperTest.testSetEvaluation();
    }
}
