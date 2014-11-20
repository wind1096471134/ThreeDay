package com.android.threeday.view;

import android.test.ActivityInstrumentationTestCase2;
import android.view.View;

import com.android.threeday.activity.mainActivity.MainActivity;

/**
 * Created by user on 2014/11/3.
 */
public class ContentChangeViewTest extends ActivityInstrumentationTestCase2<MainActivity> {
    private BaseContentChangeView mBaseContentChangeView;
    private View mFirstContentView;
    private View mSecondContentView;

    public ContentChangeViewTest( ){
        super(MainActivity.class);
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        this.mBaseContentChangeView = new RotateBackContentChangeView(getActivity());
        this.mFirstContentView = new View(getActivity());
        this.mSecondContentView = new View(getActivity());
        this.mBaseContentChangeView.setContentView(this.mFirstContentView, 1000, this.mSecondContentView, 1000);
    }

    public void testGetAndSetMethod( ) throws Exception{
        assertEquals(this.mFirstContentView, this.mBaseContentChangeView.getFirstContentView());
        assertEquals(this.mSecondContentView, this.mBaseContentChangeView.getSecondContentView());
        this.mBaseContentChangeView.setContentDuration(2000,2000);
        assertEquals(2000, this.mBaseContentChangeView.getFirstContentDuration());
        assertEquals(2000, this.mBaseContentChangeView.getSecondContentDuration());
    }

    public void testSetContentChangeListener( ) throws Exception{
        this.mBaseContentChangeView.setContentChangeListener(new BaseContentChangeView.ContentChangeListener(){

            @Override
            public void onContentChange(int contentIndex) {
                assertEquals(mBaseContentChangeView.getCurrentContentIndex(), contentIndex);
            }
        });
    }
}
