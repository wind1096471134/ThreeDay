package com.android.threeday.fragment;

import android.content.Context;

import com.android.threeday.view.RotePageLayout;

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

    public BaseDayFragment getBaseDayFragment( ){
        return this.mBaseDayFragment;
    }

    @Override
    public void testMember() throws Exception {
        assertNotNull(this.mBaseDayFragment.mModel);
        assertNotNull(this.mBaseDayFragment.mMainLayout);
    }

    @Override
    public void testMainLayoutFetchFragment() throws Exception {
        //assertEquals(this.mBaseDayFragment.mMainLayout, this.mBaseDayFragment.getView());
    }
}
