package com.android.threeday.view;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;

/**
 * Created by user on 2014/12/27.
 */
public class BgScrollView extends HorizontalScrollView{
    private ImageView mBgImageView;

    private int mPageNum;
    private int mBgWidth;
    private int mWidth;
    private int mPageScrollDis;

    public BgScrollView(Context context) {
        super(context);
        init( );
    }

    public BgScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init( );
    }

    public BgScrollView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init( );
    }

    public BgScrollView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init( );
    }

    private void init( ){
        setHorizontalScrollBarEnabled(false);
        setHorizontalFadingEdgeEnabled(false);
        this.mBgImageView = new ImageView(getContext());
        this.mBgImageView.setScaleType(ImageView.ScaleType.FIT_XY);
    }

    public void setScrollBackgroundRes(int resId){
        this.mBgImageView.setBackgroundResource(resId);
    }

    public void init(int pageNum, int viewWidth, int backgroundWidth){
        this.mPageNum = pageNum;
        this.mWidth = viewWidth;
        this.mBgWidth = backgroundWidth;
        if(this.mBgImageView.getParent() != null){
            ((ViewGroup)this.mBgImageView.getParent()).removeView(this.mBgImageView);
        }        FrameLayout frameLayout = new FrameLayout(getContext());
        frameLayout.addView(this.mBgImageView, new LayoutParams(backgroundWidth, ViewGroup.LayoutParams.MATCH_PARENT));
        addView(frameLayout, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.MATCH_PARENT));
        countPageScrollDis( );
    }

    private void countPageScrollDis( ){
        int length = this.mPageNum * this.mWidth;
        if(this.mPageNum == 1 || length <= this.mBgWidth){
            this.mPageScrollDis = this.mWidth;
        }else{
            this.mPageScrollDis = this.mWidth - (length - this.mBgWidth) / (this.mPageNum - 1);
        }
    }

    public void scrollToPage(int pageIndex){
        scrollTo(this.mPageScrollDis * pageIndex, 0);
    }

    public void smoothScrollToPage(int pageIndex){
        smoothScrollTo(this.mPageScrollDis * pageIndex, 0);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        return false;
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        return false;
    }
}
