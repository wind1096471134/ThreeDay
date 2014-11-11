package com.android.threeday.fragment;

import android.test.ActivityInstrumentationTestCase2;

import com.android.threeday.activity.mainActivity.MainActivity;

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
        this.mYesterdayFragment.onAttach(getActivity());
        this.mYesterdayFragment.onCreate(null);
        this.mYesterdayFragment.onCreateView(getActivity().getLayoutInflater(), null, null);
    }

    @Override
    protected void tearDown() throws Exception {
        super.tearDown();
    }

    @Override
    public void testMember() throws Exception {
        this.mDayFragmentTest.testMember();
    }

    @Override
    public void testMainLayoutFetchFragment() throws Exception {
        this.mDayFragmentTest.testMainLayoutFetchFragment();
    }

}
