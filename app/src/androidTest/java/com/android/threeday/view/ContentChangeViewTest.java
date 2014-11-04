package com.android.threeday.view;

import android.test.ActivityInstrumentationTestCase2;
import android.view.View;

import com.android.threeday.activity.MainActivity;

/**
 * Created by user on 2014/11/3.
 */
public class ContentChangeViewTest extends ActivityInstrumentationTestCase2<MainActivity> {
    private ContentChangeView mContentChangeView;
    private View mFirstContentView;
    private View mSecondContentView;

    public ContentChangeViewTest( ){
        super(MainActivity.class);
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        this.mContentChangeView = new ContentChangeView(getActivity());
        this.mFirstContentView = new View(getActivity());
        this.mSecondContentView = new View(getActivity());
        this.mContentChangeView.setContentView(this.mFirstContentView, 1000, this.mSecondContentView, 1000);
    }

    public void testGetAndSetMethod( ) throws Exception{
        assertEquals(this.mFirstContentView, this.mContentChangeView.getFirstContentView());
        assertEquals(this.mSecondContentView, this.mContentChangeView.getSecondContentView());
        this.mContentChangeView.setContentDuration(2000,2000);
        assertEquals(2000, this.mContentChangeView.getFirstContentDuration());
        assertEquals(2000, this.mContentChangeView.getSecondContentDuration());
    }

    public void testSetContentChangeListener( ) throws Exception{
        this.mContentChangeView.setContentChangeListener(new ContentChangeView.ContentChangeListener(){

            @Override
            public void onContentChange(int contentIndex) {
                assertEquals(mContentChangeView.getCurrentContentIndex(), contentIndex);
            }
        });
    }
}
