package com.android.threeday.fragment;

import android.test.ActivityInstrumentationTestCase2;

import com.android.threeday.activity.MainActivity;

/**
 * Created by user on 2014/11/3.
 */
public class YesterdayFragmentTest extends ActivityInstrumentationTestCase2<MainActivity> implements FragmentInterface {
    private DayFragmentTest mDayFragmentTest;

    public YesterdayFragmentTest( ){
        super(MainActivity.class);
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        this.mDayFragmentTest = new DayFragmentTest(getActivity(), new YesterdayFragment());
    }

    @Override
    protected void tearDown() throws Exception {
        super.tearDown();
    }

    @Override
    public void testMember() throws Exception {
        this.mDayFragmentTest.testMember();
    }
}
