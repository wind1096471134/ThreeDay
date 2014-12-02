package com.android.threeday.view;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by user on 2014/11/30.
 */
public class SlideUpContentChangeView extends BaseContentChangeView{
    private ObjectAnimator mSlideUpAnimator;
    private ObjectAnimator mSlideDownAnimator;
    private int mHeight;
    private boolean mSlideUp;

    public SlideUpContentChangeView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    public SlideUpContentChangeView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public SlideUpContentChangeView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public SlideUpContentChangeView(Context context) {
        super(context);
    }

    @Override
    public void setContentView(View firstContentView, int firstContentDuration, View secondContentView, int secondContentDuration) {
        super.setContentView(firstContentView, firstContentDuration, secondContentView, secondContentDuration);
    }

    private void initAnimation( ){
        long duration = 250;
        this.mSlideUpAnimator = ObjectAnimator.ofFloat(this.mSecondContentView, "translationY", this.mHeight, this.mHeight - this.mSecondContentView.getHeight());
        this.mSlideUpAnimator.setDuration(duration);
        this.mSlideUpAnimator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationStart(Animator animation) {
                mSecondContentView.setVisibility(VISIBLE);
            }
        });
        this.mSlideDownAnimator = ObjectAnimator.ofFloat(this.mSecondContentView, "translationY", this.mHeight - this.mSecondContentView.getHeight(), this.mHeight);
        this.mSlideDownAnimator.setDuration(duration);
        this.mSlideDownAnimator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                mSecondContentView.setVisibility(INVISIBLE);
            }
        });
    }

    @Override
    protected void changeContent() {
        if(this.mHeight == 0){
            this.mHeight = getHeight();
            initAnimation();
        }
        if(this.mSlideUp){
            this.mSlideUpAnimator.start();
        }else{
            this.mSlideDownAnimator.start();
        }
        this.mSlideUp = !this.mSlideUp;
    }

    @Override
    protected void resetData() {
        if(this.mSlideUpAnimator != null && this.mSlideUpAnimator.isRunning()){
            this.mSlideUpAnimator.cancel();
        }
        if(this.mSlideDownAnimator != null && this.mSlideDownAnimator.isRunning()){
            this.mSlideDownAnimator.cancel();
        }
        this.mSecondContentView.setVisibility(INVISIBLE);
        this.mSlideUp = true;
    }
}
