package com.android.threeday.view;

import android.test.ActivityInstrumentationTestCase2;
import android.view.View;

import com.android.threeday.activity.mainActivity.MainActivity;

/**
 * Created by user on 2014/11/2.
 */
public class PageSweepLayoutTest extends ActivityInstrumentationTestCase2<MainActivity> {
    private PageSweepLayout mPageSweepLayout;
    private View mFrontView;
    private View mBackView;

    public PageSweepLayoutTest(){
        super(MainActivity.class);
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        setActivityInitialTouchMode(true);
        this.mPageSweepLayout = new PageSweepLayout(getActivity());
        //getActivity().setContentView(this.mRotePageLayout);
        this.mFrontView = new View(getActivity());
        this.mBackView = new View(getActivity());
        this.mPageSweepLayout.setPageView(this.mFrontView, this.mBackView);
    }

    @Override
    protected void tearDown() throws Exception {
        super.tearDown();
    }

    public void testGetChildView( ) throws Exception{
        assertEquals(this.mBackView, this.mPageSweepLayout.getSecondView());
        assertEquals(this.mFrontView, this.mPageSweepLayout.getFirstView());
    }

 }
