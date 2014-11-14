package com.android.threeday.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;

/**
 * Created by user on 2014/10/30.
 */
public class ContentChangeView extends FrameLayout{
    public static final int FIRST_CONTENT_INDEX = 0;
    public static final int SECOND_CONTENT_INDEX = 1;

    private View mFirstContentView;
    private View mSecondContentView;
    private ContentChangeListener mContentChangeListener;

    private int mFirstContentDuration;
    private int mSecondContentDuration;
    private int mCurrentContentIndex;

    public ContentChangeView(Context context) {
        super(context);
    }

    public ContentChangeView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ContentChangeView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public ContentChangeView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    public void setContentView(View firstContentView, int firstContentDuration, View secondContentView, int secondContentDuration){
        this.mFirstContentView = firstContentView;
        this.mSecondContentView = secondContentView;
        this.mFirstContentDuration = firstContentDuration;
        this.mSecondContentDuration = secondContentDuration;
        super.addView(this.mFirstContentView, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        super.addView(this.mSecondContentView, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        this.mSecondContentView.setVisibility(INVISIBLE);
    }

    public View getFirstContentView( ){
        return this.mFirstContentView;
    }

    public View getSecondContentView( ){
        return this.mSecondContentView;
    }

    public int getFirstContentDuration( ){
        return this.mFirstContentDuration;
    }

    public int getSecondContentDuration( ){
        return this.mSecondContentDuration;
    }

    public void setContentDuration(int firstContentDuration, int secondContentDuration){
        this.mFirstContentDuration = firstContentDuration;
        this.mSecondContentDuration = secondContentDuration;
    }

    public int getCurrentContentIndex( ){
        return this.mCurrentContentIndex;
    }

    public void setContentChangeListener(ContentChangeListener listener){
        this.mContentChangeListener = listener;
    }

    public interface ContentChangeListener{
        public void onContentChange(int currentContentIndex);
    }
}
