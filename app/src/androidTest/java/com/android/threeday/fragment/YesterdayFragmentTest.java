package com.android.threeday.fragment;

import android.content.Context;
import android.test.ActivityInstrumentationTestCase2;

import com.android.threeday.activity.MainActivity;

import java.lang.reflect.Method;
import java.util.Objects;

/**
 * Created by user on 2014/11/3.
 */
public class YesterdayFragmentTest extends ActivityInstrumentationTestCase2<MainActivity> implements FragmentInterface {
    private DayFragmentTest mDayFragmentTest;
    private YesterdayFragment mYesterdayFragment;

    public YesterdayFragmentTest( ){
        super(MainActivity.class);
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        this.mYesterdayFragment = new YesterdayFragment();
        this.mDayFragmentTest = new DayFragmentTest(getActivity(), this.mYesterdayFragment);
        Class cl = Class.forName("com.android.threeday.fragment.BaseDayFragment");
        Method method = cl.getDeclaredMethod("initData", Context.class);
        method.setAccessible(true);
        method.invoke(this.mDayFragmentTest.getBaseDayFragment(), getActivity());
        method = cl.getDeclaredMethod("initView", Context.class);
        method.setAccessible(true);
        method.invoke(this.mDayFragmentTest.getBaseDayFragment(), getActivity());
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
