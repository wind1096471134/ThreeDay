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
public class RotateClockwiseContentChangeView extends BaseContentChangeView {
    private ObjectAnimator mFromViewAnimator;
    private ObjectAnimator mToViewAnimator;
    private View mFromView;
    private View mToView;

    public RotateClockwiseContentChangeView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        initAnimation( );
    }

    public RotateClockwiseContentChangeView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initAnimation( );
    }

    public RotateClockwiseContentChangeView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initAnimation( );
    }

    public RotateClockwiseContentChangeView(Context context) {
        super(context);
        initAnimation( );
    }

    private void initAnimation( ){
        long duration = 250;
        this.mFromViewAnimator = ObjectAnimator.ofFloat(this, "rotationX", 0f, 90f).setDuration(duration);
        this.mToViewAnimator = ObjectAnimator.ofFloat(this, "rotationX", -90f, 0f).setDuration(duration);
        this.mFromViewAnimator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                mFromView.setVisibility(INVISIBLE);
                mToView.setVisibility(VISIBLE);
                mToViewAnimator.start();
            }
        });
    }

    @Override
    protected void changeContent() {
        if(mCurrentContentIndex == FIRST_CONTENT_INDEX){
            mFromView = mSecondContentView;
            mToView = mFirstContentView;
        }else if(mCurrentContentIndex == SECOND_CONTENT_INDEX){
            mFromView = mFirstContentView;
            mToView = mSecondContentView;
        }
        startFromViewDisappearAnimation( );
    }

    @Override
    protected void resetData() {
        if(this.mFromViewAnimator.isRunning()){
            this.mFromViewAnimator.cancel();
        }
        if(this.mToViewAnimator.isRunning()){
            this.mToViewAnimator.cancel();
        }
        this.mFirstContentView.setRotationX(0f);
        this.mSecondContentView.setRotationX(0f);
        this.mFirstContentView.setVisibility(VISIBLE);
        this.mSecondContentView.setVisibility(INVISIBLE);
        this.mFromView = null;
        this.mToView = null;
    }

    private void startFromViewDisappearAnimation( ){
        this.mFromViewAnimator.setTarget(this.mFromView);
        this.mToViewAnimator.setTarget(this.mToView);
        this.mFromViewAnimator.start();
    }
}
