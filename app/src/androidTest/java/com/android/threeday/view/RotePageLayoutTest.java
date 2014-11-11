package com.android.threeday.view;

import android.test.ActivityInstrumentationTestCase2;
import android.view.View;

import com.android.threeday.activity.mainActivity.MainActivity;

/**
 * Created by user on 2014/11/2.
 */
public class RotePageLayoutTest extends ActivityInstrumentationTestCase2<MainActivity> {
    private RotePageLayout mRotePageLayout;
    private View mFrontView;
    private View mBackView;

    public RotePageLayoutTest( ){
        super(MainActivity.class);
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        setActivityInitialTouchMode(true);
        this.mRotePageLayout = new RotePageLayout(getActivity());
        //getActivity().setContentView(this.mRotePageLayout);
        this.mFrontView = new View(getActivity());
        this.mBackView = new View(getActivity());
        this.mRotePageLayout.setPageView(this.mFrontView, this.mBackView);
    }

    @Override
    protected void tearDown() throws Exception {
        super.tearDown();
    }

    public void testGetChildView( ) throws Exception{
        assertEquals(this.mBackView, this.mRotePageLayout.getBackView());
        assertEquals(this.mFrontView, this.mRotePageLayout.getFrontView());
    }

    public void testGetPageState( ) throws Exception{
        this.mRotePageLayout.roteToBackPage( );
        assertEquals(RotePageLayout.PAGE_STATE_BACK, this.mRotePageLayout.getPageState());
        this.mRotePageLayout.roteToFrontPage( );
        assertEquals(RotePageLayout.PAGE_STATE_FRONT, this.mRotePageLayout.getPageState());
    }

    public void testSetPageStateChangeListener( ) throws Exception{
        this.mRotePageLayout.roteToFrontPage();
        this.mRotePageLayout.setPageStateChangeListener(new RotePageLayout.PageStateChangeListener() {

            @Override
            public void onPageStateChange(int state) {
                assertEquals(RotePageLayout.PAGE_STATE_BACK, state);
            }
        });
        this.mRotePageLayout.roteToBackPage();

        this.mRotePageLayout.setPageStateChangeListener(new RotePageLayout.PageStateChangeListener() {
            @Override
            public void onPageStateChange(int state) {
                assertEquals(RotePageLayout.PAGE_STATE_FRONT, state);
            }
        });
        this.mRotePageLayout.roteToFrontPage();
    }

    public void testSetPageRoteListener( ) throws Exception{
        this.mRotePageLayout.setPageRoteListener(new RotePageLayout.PageRoteListener( ){

            @Override
            public void onPageRote(int currentDegree, int direction) {
                assertEquals(10, currentDegree);
                assertEquals(RotePageLayout.DIRECTION_ANTICLOCKWISE, direction);
            }

            @Override
            public void onPageFling(int speed, int direction) {
                assertEquals(RotePageLayout.FLING_SPEED_FAST, speed);
                assertEquals(RotePageLayout.DIRECTION_CLOCKWISE, direction);
            }
        });
        this.mRotePageLayout.rotePage(10, RotePageLayout.DIRECTION_ANTICLOCKWISE);
        this.mRotePageLayout.flingPage(RotePageLayout.FLING_SPEED_FAST, RotePageLayout.DIRECTION_CLOCKWISE);
    }
}
