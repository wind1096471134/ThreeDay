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
        assertEquals(this.mBackView, this.mPageSweepLayout.getBackView());
        assertEquals(this.mFrontView, this.mPageSweepLayout.getFrontView());
    }

    public void testGetPageState( ) throws Exception{
        this.mPageSweepLayout.roteToBackPage( );
        assertEquals(PageSweepLayout.PAGE_BACK, this.mPageSweepLayout.getPageState());
        this.mPageSweepLayout.roteToFrontPage( );
        assertEquals(PageSweepLayout.PAGE_FRONT, this.mPageSweepLayout.getPageState());
    }

    public void testSetPageStateChangeListener( ) throws Exception{
        this.mPageSweepLayout.roteToFrontPage();
        this.mPageSweepLayout.setPageStateChangeListener(new PageSweepLayout.PageStateChangeListener() {

            @Override
            public void onPageStateChange(int state) {
                assertEquals(PageSweepLayout.PAGE_BACK, state);
            }
        });
        this.mPageSweepLayout.roteToBackPage();

        this.mPageSweepLayout.setPageStateChangeListener(new PageSweepLayout.PageStateChangeListener() {
            @Override
            public void onPageStateChange(int state) {
                assertEquals(PageSweepLayout.PAGE_FRONT, state);
            }
        });
        this.mPageSweepLayout.roteToFrontPage();
    }

    public void testSetPageRoteListener( ) throws Exception{
        this.mPageSweepLayout.setPageRoteListener(new PageSweepLayout.PageRoteListener( ){

            @Override
            public void onPageRote(int currentDegree, int direction) {
                assertEquals(10, currentDegree);
                assertEquals(PageSweepLayout.DIRECTION_ANTICLOCKWISE, direction);
            }

            @Override
            public void onPageFling(int speed, int direction) {
                assertEquals(PageSweepLayout.FLING_SPEED_FAST, speed);
                assertEquals(PageSweepLayout.DIRECTION_CLOCKWISE, direction);
            }
        });
        this.mPageSweepLayout.rotePage(10, PageSweepLayout.DIRECTION_ANTICLOCKWISE);
        this.mPageSweepLayout.flingPage(PageSweepLayout.FLING_SPEED_FAST, PageSweepLayout.DIRECTION_CLOCKWISE);
    }
}
