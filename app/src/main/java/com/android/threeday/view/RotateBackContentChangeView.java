package com.android.threeday.view;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by user on 2014/11/18.
 */
public class RotateBackContentChangeView extends BaseContentChangeView {
    private boolean mRotateBack;
    private ObjectAnimator mFirstViewClockwiseAnimation;
    private ObjectAnimator mFirstViewAnticlockwiseAnimation;
    private ObjectAnimator mSecondViewClockwiseAnimation;
    private ObjectAnimator mSecondViewAnticlockwiseAnimation;

    public RotateBackContentChangeView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        initAnimation();
    }

    public RotateBackContentChangeView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initAnimation();
    }

    public RotateBackContentChangeView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initAnimation();
    }

    public RotateBackContentChangeView(Context context) {
        super(context);
        initAnimation();
    }

    @Override
    public void setContentView(View firstContentView, int firstContentDuration, View secondContentView, int secondContentDuration) {
        super.setContentView(firstContentView, firstContentDuration, secondContentView, secondContentDuration);
        this.mFirstViewClockwiseAnimation.setTarget(this.mFirstContentView);
        this.mFirstViewAnticlockwiseAnimation.setTarget(this.mFirstContentView);
        this.mSecondViewClockwiseAnimation.setTarget(this.mSecondContentView);
        this.mSecondViewAnticlockwiseAnimation.setTarget(this.mSecondContentView);
    }

    private void initAnimation( ){
        long duration = 250;
        this.mFirstViewClockwiseAnimation = ObjectAnimator.ofFloat(this, "rotationX", 0f, 90f).setDuration(duration);
        this.mSecondViewClockwiseAnimation = ObjectAnimator.ofFloat(this, "rotationX", -90f, 0f).setDuration(duration);
        this.mFirstViewAnticlockwiseAnimation = ObjectAnimator.ofFloat(this, "rotationX", 90f, 0f).setDuration(duration);
        this.mSecondViewAnticlockwiseAnimation = ObjectAnimator.ofFloat(this, "rotationX", 0f, -90f).setDuration(duration);

        this.mFirstViewClockwiseAnimation.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                mFirstContentView.setVisibility(INVISIBLE);
                mSecondContentView.setVisibility(VISIBLE);
                mSecondViewClockwiseAnimation.start();
            }
        });
        this.mSecondViewAnticlockwiseAnimation.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                mSecondContentView.setVisibility(INVISIBLE);
                mFirstContentView.setVisibility(VISIBLE);
                mFirstViewAnticlockwiseAnimation.start();
            }
        });
    }

    @Override
    protected void changeContent() {
        if(this.mRotateBack){
            this.mSecondViewAnticlockwiseAnimation.start();
        }else{
            this.mFirstViewClockwiseAnimation.start();
        }
        this.mRotateBack = !this.mRotateBack;
    }

    @Override
    protected void resetData() {
        if(this.mFirstViewClockwiseAnimation.isRunning()){
            this.mFirstViewClockwiseAnimation.cancel();
        }
        if(this.mFirstViewAnticlockwiseAnimation.isRunning()){
            this.mFirstViewAnticlockwiseAnimation.cancel();
        }
        if(this.mSecondViewClockwiseAnimation.isRunning()){
            this.mSecondViewClockwiseAnimation.cancel();
        }
        if(this.mSecondViewAnticlockwiseAnimation.isRunning()){
            this.mSecondViewAnticlockwiseAnimation.cancel();
        }
        this.mFirstContentView.setVisibility(VISIBLE);
        this.mSecondContentView.setVisibility(INVISIBLE);
        this.mFirstContentView.setRotationX(0f);
        this.mSecondContentView.setRotationX(0f);
        this.mRotateBack = false;
    }
}
