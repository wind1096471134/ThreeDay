package com.android.threeday.view;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

/**
 * Created by user on 2014/11/30.
 */
public class PageSwitchLayout extends FrameLayout{
    public static final int PAGE_FIRST = 0;
    public static final int PAGE_SECOND = 1;
    private View mFirstView;
    private View mSecondView;
    private OnPageSwitchListener mOnPageSwitchListener;
    private AnimatorSet mSecondViewUpAnimator;
    private AnimatorSet mSecondViewDownAnimator;
    private ObjectAnimator mFirstViewDisappearAnimator;
    private ObjectAnimator mFirstViewAppearAnimator;
    private AnimatorListenerAdapter mUpAnimatorEndListener = new AnimatorListenerAdapter() {
        @Override
        public void onAnimationStart(Animator animation) {
            mSecondView.setVisibility(VISIBLE);
            if(mOnPageSwitchListener != null){
                mOnPageSwitchListener.onPageSwitchStart(mCurrentPage);
            }
        }

        @Override
        public void onAnimationEnd(Animator animation) {
            mFirstView.setVisibility(INVISIBLE);
            mCurrentPage = PAGE_SECOND;
            if(mOnPageSwitchListener != null){
                mOnPageSwitchListener.onPageSwitchEnd(mCurrentPage);
            }
            mAnimatorRunning = false;
        }
    };
    private AnimatorListenerAdapter mDownAnimatorEndListener = new AnimatorListenerAdapter() {
        @Override
        public void onAnimationStart(Animator animation) {
            mFirstView.setVisibility(VISIBLE);
            if(mOnPageSwitchListener != null){
                mOnPageSwitchListener.onPageSwitchStart(mCurrentPage);
            }
        }

        @Override
        public void onAnimationEnd(Animator animation) {
            mSecondView.setVisibility(INVISIBLE);
            mCurrentPage = PAGE_FIRST;
            if(mOnPageSwitchListener != null){
                mOnPageSwitchListener.onPageSwitchEnd(mCurrentPage);
            }
            mAnimatorRunning = false;
        }
    };

    private int mCurrentPage;
    private int mHeight;
    private boolean mAnimatorRunning;

    public PageSwitchLayout(Context context) {
        super(context);
    }

    public PageSwitchLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public PageSwitchLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public PageSwitchLayout(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    public void setPageView(View firstView, View secondView){
        this.mFirstView = firstView;
        this.mSecondView = secondView;
        addView(this.mFirstView, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        addView(this.mSecondView, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        this.mSecondView.setVisibility(INVISIBLE);
    }

    private void initAnimator( ){
        long duration = 500;
        this.mSecondViewUpAnimator = new AnimatorSet();
        ObjectAnimator upTranslationAnimator = ObjectAnimator.ofFloat(this.mSecondView, "translationY", this.mHeight, 0f)
            .setDuration(duration);
        ObjectAnimator upAlphaAnimator = ObjectAnimator.ofFloat(this.mSecondView, "alpha", 0f, 1f).setDuration(duration);
        this.mSecondViewUpAnimator.playTogether(upTranslationAnimator, upAlphaAnimator);
        this.mSecondViewUpAnimator.addListener(this.mUpAnimatorEndListener);

        this.mSecondViewDownAnimator = new AnimatorSet();
        ObjectAnimator downTranslationAnimator = ObjectAnimator.ofFloat(this.mSecondView, "translationY", 0f, this.mHeight).setDuration(duration);
        ObjectAnimator downAlphaAnimator = ObjectAnimator.ofFloat(this.mSecondView, "alpha", 1f, 0f).setDuration(duration);
        this.mSecondViewDownAnimator.playTogether(downTranslationAnimator, downAlphaAnimator);
        this.mSecondViewDownAnimator.addListener(this.mDownAnimatorEndListener);

        this.mFirstViewAppearAnimator = ObjectAnimator.ofFloat(this.mFirstView, "alpha", 0f, 1f).setDuration(duration);
        this.mFirstViewDisappearAnimator = ObjectAnimator.ofFloat(this.mFirstView, "alpha", 1f, 0f).setDuration(duration);
    }

    public void switchPage( ){
        if(this.mHeight == 0){
            this.mHeight = getHeight();
            initAnimator( );
        }
        if(this.mCurrentPage == PAGE_FIRST){
            this.mSecondViewUpAnimator.start();
            this.mFirstViewDisappearAnimator.start();
        }else{
            this.mSecondViewDownAnimator.start();
            this.mFirstViewAppearAnimator.start();
        }
        this.mAnimatorRunning = true;
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        if(this.mAnimatorRunning){
            return true;
        }else{
            return super.onInterceptTouchEvent(ev);
        }
    }

    public void setOnPageSwitchListener(OnPageSwitchListener onPageSwitchListener){
        this.mOnPageSwitchListener = onPageSwitchListener;
    }

    public int getCurrentPage( ){
        return this.mCurrentPage;
    }

    public View getFirstView( ){
        return this.mFirstView;
    }

    public View getSecondView( ){
        return this.mSecondView;
    }

    public interface OnPageSwitchListener{
        public void onPageSwitchStart(int currentPage);
        public void onPageSwitchEnd(int currentPage);
    }
}
