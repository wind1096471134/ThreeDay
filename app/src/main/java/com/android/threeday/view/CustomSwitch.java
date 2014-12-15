package com.android.threeday.view;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

/**
 * Created by user on 2014/12/10.
 */
public class CustomSwitch extends FrameLayout {
    private View mSwitchView;
    private View mCheckColorView;
    private SwitchCheckChangeListener mSwitchCheckChangeListener;
    private ObjectAnimator mSwitchAnimator;
    private AnimatorListenerAdapter mSwitchChangeEndListener = new AnimatorListenerAdapter() {
        @Override
        public void onAnimationEnd(Animator animation) {
            mSwitchCheck = !mSwitchCheck;
            if(mSwitchCheckChangeListener != null){
                mSwitchCheckChangeListener.onSwitchCheckChange(mSwitchCheck);
            }
        }
    };

    private long mSwitchDuration = 300;
    private int mWidth;
    private int mSwitchViewWidth = 50;
    private int mEndPosition;
    private float mX;
    private boolean mSwitchCheck;
    private boolean mReAddSwitchView;
    private final int SINGLE_CLICK_MOV_MAX_NUM = 10;
    private int mMovNum;
    private boolean mCanSwitch;

    public CustomSwitch(Context context, AttributeSet attrs) {
        super(context, attrs);
        initData();
        initView();
    }

    public CustomSwitch(Context context) {
        super(context);
        initData();
        initView();
    }

    public CustomSwitch(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initData();
        initView();
    }

    public CustomSwitch(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        initData();
        initView();
    }

    private void initData( ){
        this.mSwitchAnimator = ObjectAnimator.ofFloat(this, "translationX", 0f, 0f);
    }

    private void initView( ){
        this.mSwitchView = new View(getContext());
        this.mCheckColorView = new View(getContext());
        addView(this.mCheckColorView, new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        addView(this.mSwitchView, new LayoutParams(this.mSwitchViewWidth, ViewGroup.LayoutParams.MATCH_PARENT, Gravity.LEFT));
        this.mSwitchAnimator.setTarget(this.mSwitchView);
    }

    public void setSwitchThumbWidth(int width){
        this.mSwitchViewWidth = width;
        removeView(this.mSwitchView);
        addView(this.mSwitchView, new LayoutParams(width, ViewGroup.LayoutParams.MATCH_PARENT, Gravity.LEFT));
    }

    public void setSwitchCheckChangeListener(SwitchCheckChangeListener switchCheckChangeListener){
        this.mSwitchCheckChangeListener = switchCheckChangeListener;
    }

    public void setSwitchThumbBackgroundRes(int resId){
        this.mSwitchView.setBackgroundResource(resId);
    }

    public void setSwitchThumbBackgroundColor(int color){
        this.mSwitchView.setBackgroundColor(color);
    }

    public void setCheckBackgroundColor(int color){
        this.mCheckColorView.setBackgroundColor(color);
    }

    public void setSwitchCheck(boolean check){
        this.mSwitchCheck = check;
        float alpha;
        if(check){
            if(this.mWidth == 0){//we don't know the width now
                this.mReAddSwitchView = true;
                removeView(this.mSwitchView);
                addView(this.mSwitchView, new LayoutParams(this.mSwitchViewWidth, ViewGroup.LayoutParams.MATCH_PARENT, Gravity.RIGHT));
            }else{
                this.mSwitchView.setTranslationX(this.mEndPosition);
            }
            alpha = 1f;
        }else{
            this.mSwitchView.setTranslationY(0f);
            alpha = 0f;
        }
        this.mCheckColorView.setAlpha(alpha);
    }

    private void animateSwitch( ){
        if(!this.mSwitchAnimator.isRunning()){
            float fromX = this.mSwitchView.getTranslationX();
            float toX;
            float toAlpha;
            if(this.mSwitchCheck){
                toX = 0f;
                toAlpha = 0f;
            }else{
                toX = this.mEndPosition;
                toAlpha = 1f;
            }
            this.mSwitchAnimator.setDuration(this.mSwitchDuration).setFloatValues(fromX, toX);
            this.mSwitchAnimator.removeAllListeners();
            this.mSwitchAnimator.addListener(this.mSwitchChangeEndListener);
            this.mSwitchAnimator.start();
            this.mCheckColorView.animate().alpha(toAlpha).setDuration(this.mSwitchDuration).start();
        }
    }

    private void onSwitchClick( ){
        animateSwitch();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if(this.mWidth == 0){
            this.mWidth = getWidth();
            this.mEndPosition = this.mWidth - this.mSwitchViewWidth;
            if(this.mReAddSwitchView){
                this.mReAddSwitchView = false;
                removeView(this.mSwitchView);
                addView(this.mSwitchView, new LayoutParams(this.mSwitchViewWidth, ViewGroup.LayoutParams.MATCH_PARENT, Gravity.LEFT));
                this.mSwitchView.setTranslationX(this.mEndPosition);
            }
        }

        if(!this.mSwitchAnimator.isRunning()){
            if(event.getAction() == MotionEvent.ACTION_DOWN){
                this.mMovNum = 0;
                this.mCanSwitch = true;
                this.mX = event.getX();
            }else{
                if(this.mCanSwitch){//avoid switching when action_down during animation's running and still moving after animation end
                    switch (event.getAction()){
                        case MotionEvent.ACTION_MOVE:
                            this.mMovNum++;
                            float x;
                            if(this.mSwitchCheck){
                                float dis = this.mX - event.getX();
                                x = dis <= 0 ? this.mEndPosition : (dis <= this.mEndPosition ? (this.mEndPosition - dis) : 0);
                            }else{
                                float dis = event.getX() - this.mX;
                                x = dis <= 0 ? 0 : (dis > this.mEndPosition ? this.mEndPosition : dis);
                            }
                            float alpha = x / this.mEndPosition;
                            this.mSwitchView.setX(x);
                            this.mCheckColorView.setAlpha(alpha);
                            break;
                        case MotionEvent.ACTION_CANCEL:
                            onActionEnd(event);
                            break;
                        case MotionEvent.ACTION_UP :
                            onActionEnd(event);
                            break;
                    }
                }//switch end
            }//else end
        }//if end

        return true;
    }

    private void onActionEnd(MotionEvent event){
        this.mCanSwitch = false;

        if(this.mMovNum <= this.SINGLE_CLICK_MOV_MAX_NUM){
            onSwitchClick();
        }else{
            float upX = this.mSwitchView.getX();
            float toX;
            float toAlpha;
            long duration;
            this.mSwitchAnimator.removeAllListeners();
            if(upX < (this.mWidth - this.mSwitchViewWidth) / 2){//to left, not check
                duration = (long) ((upX / this.mEndPosition) * this.mSwitchDuration);
                toX = 0f;
                toAlpha = 0f;
                if(this.mSwitchCheck){
                    this.mSwitchAnimator.addListener(this.mSwitchChangeEndListener);
                }
            }else{//to right, check
                duration = (long) ((1 - upX / this.mEndPosition) * this.mSwitchDuration);
                toX = this.mEndPosition;
                toAlpha = 1f;
                if(!this.mSwitchCheck){
                    this.mSwitchAnimator.addListener(this.mSwitchChangeEndListener);
                }
            }
            this.mSwitchAnimator.setFloatValues(this.mSwitchView.getTranslationX(), toX);
            this.mSwitchAnimator.setDuration(duration);
            this.mSwitchAnimator.start();
            this.mCheckColorView.animate().alpha(toAlpha).setDuration(duration).start();
        }
    }

    public interface SwitchCheckChangeListener{
        public void onSwitchCheckChange(boolean check);
    }
}
