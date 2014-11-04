package com.android.threeday.fragment;

import android.content.Context;

import static junit.framework.Assert.*;

/**
 * Created by user on 2014/11/3.
 */
public class DayFragmentTest implements FragmentInterface{
    private BaseDayFragment mBaseDayFragment;
    private Context mContext;

    public DayFragmentTest(Context context, BaseDayFragment baseDayFragment){
        this.mContext = context;
        this.mBaseDayFragment = baseDayFragment;
    }

    @Override
    public void testMember() throws Exception {
        assertNotNull(this.mBaseDayFragment.mModel);
    }
}
