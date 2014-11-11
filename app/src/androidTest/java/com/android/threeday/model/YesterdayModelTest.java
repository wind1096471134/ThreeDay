package com.android.threeday.model;

import android.test.ActivityInstrumentationTestCase2;

import com.android.threeday.activity.mainActivity.MainActivity;
import com.android.threeday.model.testInterface.DayModelInterface;


public class YesterdayModelTest extends ActivityInstrumentationTestCase2<MainActivity> implements DayModelInterface {
    private DayModelTest mDayModelTest;

    public YesterdayModelTest( ){
        super(MainActivity.class);
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        mDayModelTest = new DayModelTest(getActivity(), new YesterdayModel(getActivity()));
    }

    @Override
    protected void tearDown() throws Exception {
        super.tearDown();
    }

    @Override
    public void testMemberNull() throws Exception {
        mDayModelTest.testMemberNull();
    }

    @Override
    public void testGetDayEvaluation() throws Exception {
        this.mDayModelTest.testGetDayEvaluation();
    }

    @Override
    public void testSetDayEvaluation() throws Exception {
        //no need in yesterday
    }

    @Override
    public void testDoneTask() throws Exception {
        mDayModelTest.testDoneTask();
    }

    @Override
    public void testGetTask() throws Exception {
        mDayModelTest.testGetTask();
    }

    @Override
    public void testAddTask() throws Exception {
        mDayModelTest.testGetTask();
    }

    @Override
    public void testDeleteTask() throws Exception {
        mDayModelTest.testDeleteTask();
    }

    @Override
    public void testSetRemain() throws Exception {
        mDayModelTest.testSetRemain();
    }

    @Override
    public void testSetRemainTime() throws Exception {
        mDayModelTest.testSetRemainTime();
    }

}
