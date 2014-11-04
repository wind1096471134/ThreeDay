package com.android.threeday.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

/**
 * Created by user on 2014/10/30.
 */
public class RotePageLayout extends FrameLayout {
    public static final int PAGE_STATE_FRONT = 1;
    public static final int PAGE_STATE_BACK = 2;
    public static final int DIRECTION_CLOCKWISE = 0;
    public static final int DIRECTION_ANTICLOCKWISE = 1;
    public static final int FLING_SPEED_FAST = 0;
    public static final int FLING_SPEED_NORMAL = 1;
    public static final int FLING_SPEED_SLOW = 2;

    private int mRoteDirection;
    private int mPageState;
    private View mFrontView;
    private View mBackView;
    private PageStateChangeListener mPageStateChangeListener;
    private PageRoteListener mPageRoteListener;

    public RotePageLayout(Context context) {
        super(context);
    }

    public RotePageLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public RotePageLayout(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    public void setPageView(View frontView, View backView){
        this.mFrontView = frontView;
        this.mBackView = backView;
        if(frontView != null){
            super.addView(frontView, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
            frontView.setVisibility(View.VISIBLE);
        }
        if(backView != null){
            super.addView(backView, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
            backView.setVisibility(View.GONE);
        }
        this.mPageState = PAGE_STATE_FRONT;
    }

    public View getFrontView( ){
        return this.mFrontView;
    }

    public View getBackView( ){
        return this.mBackView;
    }

    public int getPageState( ){
        return this.mPageState;
    }

    public void roteToFrontPage( ){
        if(this.mPageState == PAGE_STATE_BACK){
            //TODO
            this.mPageState = PAGE_STATE_FRONT;
            if(this.mPageStateChangeListener != null){
                this.mPageStateChangeListener.onPageStateChange(this.mPageState);
            }
        }
    }

    public void roteToBackPage( ){
        if(this.mPageState == PAGE_STATE_FRONT){
            //TODO
            this.mPageState = PAGE_STATE_BACK;
            if(this.mPageStateChangeListener != null){
                this.mPageStateChangeListener.onPageStateChange(this.mPageState);
            }
        }
    }

    public void rotePage(int degree, int direction){
        //TODO
        if(this.mPageRoteListener != null){
            this.mPageRoteListener.onPageRote(degree, direction);
        }
    }

    public void flingPage(int speed, int direction){
        //TODO
        if(this.mPageRoteListener != null){
            this.mPageRoteListener.onPageFling(speed, direction);
        }
    }

    public void setPageStateChangeListener(PageStateChangeListener listener){
        this.mPageStateChangeListener = listener;
    }

    public void setPageRoteListener(PageRoteListener listener){
        this.mPageRoteListener = listener;
    }

    public interface PageStateChangeListener{
        public void onPageStateChange(int state);
    }

    public interface PageRoteListener{
        public void onPageRote(int currentDegree, int direction);
        public void onPageFling(int speed, int direction);
    }
}
