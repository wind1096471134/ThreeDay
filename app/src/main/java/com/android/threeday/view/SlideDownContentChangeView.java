package com.android.threeday.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;

/**
 * Created by user on 2014/11/30.
 */
public class SlideDownContentChangeView extends BaseContentChangeView{
    private TranslateAnimation mSlideUpAnimation;
    private TranslateAnimation mSlideDownAnimation;
    private int mHeight;
    private boolean mSlideUp;

    public SlideDownContentChangeView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    public SlideDownContentChangeView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public SlideDownContentChangeView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public SlideDownContentChangeView(Context context) {
        super(context);
    }

    @Override
    public void setContentView(View firstContentView, int firstContentDuration, View secondContentView, int secondContentDuration) {
        super.setContentView(firstContentView, firstContentDuration, secondContentView, secondContentDuration);
        initAnimation( );
    }

    private void initAnimation( ){
        long duration = 250;
        this.mSlideUpAnimation = new TranslateAnimation(this.mSecondContentView.getTranslationX(), this.mSecondContentView.getTranslationX(),
                0f, -this.mSecondContentView.getHeight());
        this.mSlideUpAnimation.setDuration(duration);
        this.mSlideUpAnimation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                mSecondContentView.setVisibility(INVISIBLE);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

        this.mSlideDownAnimation = new TranslateAnimation(this.mSecondContentView.getTranslationX(), this.mSecondContentView.getTranslationX(),
                -this.mSecondContentView.getHeight(), 0f);
        this.mSlideDownAnimation.setDuration(duration);
        this.mSlideDownAnimation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                mSecondContentView.setVisibility(VISIBLE);
            }

            @Override
            public void onAnimationEnd(Animation animation) {

            }

            @Override
            public void onAnimationRepeat(Animation animation) {

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
            this.mSecondContentView.startAnimation(this.mSlideUpAnimation);
        }else{
            this.mSecondContentView.startAnimation(this.mSlideDownAnimation);
        }
        this.mSlideUp = !this.mSlideUp;
    }

    @Override
    protected void resetData() {
        this.mSecondContentView.setVisibility(INVISIBLE);
        this.mSlideUp = false;
    }
}
