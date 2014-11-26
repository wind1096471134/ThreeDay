package com.android.threeday.view;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.graphics.Bitmap;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;

/**
 * Created by user on 2014/10/30.
 */
public class PageSweepLayout extends FrameLayout {
    public static final int DIRECTION_FORWARD = 0;
    public static final int DIRECTION_BACKWARD = 1;
    public static final int PAGE_FIRST = 0;
    public static final int PAGE_SECOND = 1;

    private final long mSweepAnimationDuration = 500;
    private final float mScaleXDes = 0.03f;
    private final float mScaleYDes = 0.03f;
    private final float mTranslationDes = 0.03f;
    private float mMovEndDis;
    private float mScaleXChangeWhenMov;
    private float mScaleYChangeWhenMov;
    private float mTranslationChangeWhenMov;
    private float mAlphaChangeWhenMov;
    private final float DIS_EDGE_STAND = 3F;
    private float mDisEdge = DIS_EDGE_STAND;
    private float mPivotX;
    private float mPivotY;
    private float mDensity = 1F;
    private float mY;
    private float mVelocityEdge = 50;
    private int mHeight;
    private int mWidth;
    private int mCurrentPage;
    private int mSweepDirection;
    private int mSweepStartDirection;
    private boolean mCanSweep;
    private boolean mAnimationRunning;

    private View mFirstView;
    private View mSecondView;
    private ImageView mBottomImageView;
    private ImageView mTopImageView;
    private View mLayer_1;
    private View mLayer_2;
    private View mLayer_3;
    private Bitmap mFirstViewBitmap;
    private Bitmap mSecondViewBitmap;
    private PageSweepListener mPageSweepListener;
    private GestureDetector mGestureDetector;
    private VelocityTracker mVelocityTracker;
    private GestureDetector.SimpleOnGestureListener mSimpleOnGestureListener = new GestureDetector.SimpleOnGestureListener(){
        @Override
        public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
            if(Math.abs(distanceY) > Math.abs(distanceX)){
                mCanSweep = true;
            }
            return true;
        }
    };

    public PageSweepLayout(Context context) {
        super(context);
        initData(context);
    }

    public PageSweepLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initData(context);
    }

    public PageSweepLayout(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        initData(context);
    }

    private void initData(Context context){
        this.mGestureDetector = new GestureDetector(context, this.mSimpleOnGestureListener);
        this.mVelocityTracker = VelocityTracker.obtain();
        this.mBottomImageView = new ImageView(context);
        this.mTopImageView = new ImageView(context);
    }

    public void setDensity(float density){
        this.mDensity = density;
        this.mDisEdge = DIS_EDGE_STAND * density;
    }

    private void onPageSweepStart( ){
        if(this.mPageSweepListener != null){
            this.mPageSweepListener.onPageSweepStart(this.mSweepStartDirection);
        }
    }

    private void onPageSelected( ){
        if(this.mPageSweepListener != null){
            this.mPageSweepListener.onPageSelected(this.mCurrentPage);
        }
    }

    private void updateBitmap( ){
        Bitmap temp = null;
        if(this.mCurrentPage == PAGE_FIRST){
            temp = this.mSecondViewBitmap;
            this.mSecondViewBitmap = Bitmap.createBitmap(this.mSecondView.getDrawingCache(true));
        }else{
            temp = this.mFirstViewBitmap;
            this.mFirstViewBitmap = Bitmap.createBitmap(this.mFirstView.getDrawingCache(true));
        }
        temp.recycle();
        temp = null;
    }

    private void initBitmap( ){
        Bitmap bitmap = this.mFirstView.getDrawingCache(true);
        if(bitmap != null){
            this.mFirstViewBitmap = Bitmap.createBitmap(bitmap);
            this.mBottomImageView.setImageBitmap(this.mFirstViewBitmap);
        }
        bitmap = this.mSecondView.getDrawingCache(true);
        if(bitmap != null){
            this.mSecondViewBitmap = Bitmap.createBitmap(bitmap);
            this.mTopImageView.setImageBitmap(this.mSecondViewBitmap);
        }
    }

    private void changePage( ){
        mCurrentPage = mCurrentPage == PAGE_FIRST ? PAGE_SECOND : PAGE_FIRST;
    }

    private void onSweepAnimationEnd( ){
        this.mAnimationRunning = false;
        onPageSelected();
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        return false;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if(!this.mAnimationRunning){

            this.mGestureDetector.onTouchEvent(event);

            switch (event.getAction()){

                case MotionEvent.ACTION_DOWN:
                    this.mCanSweep = false;
                    this.mSweepStartDirection = -1;
                    this.mY = event.getY();
                    this.mVelocityTracker.clear();
                    if(this.mFirstViewBitmap == null || this.mSecondViewBitmap == null){
                        initBitmap();
                    }
                    break;
                case MotionEvent.ACTION_MOVE:

                    if(this.mCanSweep){

                        this.mVelocityTracker.addMovement(event);
                        float y = event.getY();
                        float dis = y - this.mY;

                        if(Math.abs(dis) > this.mDisEdge){

                            if(dis > 0){//backward
                                if(this.mSweepStartDirection == -1){
                                    this.mSweepStartDirection = DIRECTION_BACKWARD;
                                    resetLayer();
                                    onPageSweepStart();
                                }
                                this.mSweepDirection = DIRECTION_BACKWARD;

                                if(this.mLayer_1.getAlpha() < 1){
                                    this.mLayer_1.setAlpha(this.mLayer_1.getAlpha() + this.mAlphaChangeWhenMov);
                                    if(this.mLayer_1.getAlpha() > 1){
                                        this.mLayer_1.setAlpha(1f);
                                    }
                                    this.mLayer_1.setScaleX(this.mLayer_1.getScaleX() + this.mScaleXChangeWhenMov);
                                    this.mLayer_1.setScaleY(this.mLayer_1.getScaleY() + this.mScaleYChangeWhenMov);
                                    this.mLayer_1.setTranslationY(this.mLayer_1.getTranslationY() + this.mTranslationChangeWhenMov);

                                    this.mLayer_2.setScaleX(this.mLayer_2.getScaleX() + this.mScaleXChangeWhenMov);
                                    this.mLayer_2.setScaleY(this.mLayer_2.getScaleY() + this.mScaleYChangeWhenMov);
                                    this.mLayer_2.setTranslationY(this.mLayer_2.getTranslationY() + this.mTranslationChangeWhenMov);

                                    this.mLayer_3.setAlpha(this.mLayer_3.getAlpha() - this.mAlphaChangeWhenMov);
                                    if(this.mLayer_3.getAlpha() < 0){
                                        this.mLayer_3.setAlpha(0f);
                                    }
                                }

                            }else{//forward

                                if(this.mSweepStartDirection == -1){
                                    this.mSweepStartDirection = DIRECTION_FORWARD;
                                    resetLayer();
                                    onPageSweepStart();
                                }
                                this.mSweepDirection = DIRECTION_FORWARD;

                                if(this.mLayer_1.getAlpha() > 0){
                                    this.mLayer_1.setAlpha(this.mLayer_1.getAlpha() - this.mAlphaChangeWhenMov);
                                    if(this.mLayer_1.getAlpha() < 0){
                                        this.mLayer_1.setAlpha(0f);
                                    }
                                    this.mLayer_1.setScaleX(this.mLayer_1.getScaleX() - this.mScaleXChangeWhenMov);
                                    this.mLayer_1.setScaleY(this.mLayer_1.getScaleY() - this.mScaleYChangeWhenMov);
                                    this.mLayer_1.setTranslationY(this.mLayer_1.getTranslationY() - this.mTranslationChangeWhenMov);

                                    this.mLayer_2.setScaleX(this.mLayer_2.getScaleX() - this.mScaleXChangeWhenMov);
                                    this.mLayer_2.setScaleY(this.mLayer_2.getScaleY() - this.mScaleYChangeWhenMov);
                                    this.mLayer_2.setTranslationY(this.mLayer_2.getTranslationY() - this.mTranslationChangeWhenMov);

                                    this.mLayer_3.setAlpha(this.mLayer_3.getAlpha() + this.mAlphaChangeWhenMov);
                                    if(this.mLayer_3.getAlpha() > 1){
                                        this.mLayer_3.setAlpha(1f);
                                    }
                                }

                            }

                            this.mY = y;
                        }

                    }
                    break;
                case MotionEvent.ACTION_UP:
                    if(this.mCanSweep){

                        this.mVelocityTracker.addMovement(event);
                        this.mVelocityTracker.computeCurrentVelocity(1000);
                        ObjectAnimator layer3ObjectAnimator = ObjectAnimator.ofFloat(this.mLayer_3, "alpha", 0f, 0f);

                        if(this.mSweepStartDirection == DIRECTION_BACKWARD){

                            if(this.mLayer_1.getAlpha() > 0.5 || Math.abs(this.mVelocityTracker.getYVelocity()) > this.mVelocityEdge){

                                float alpha = (this.mLayer_1.getAlpha() > 1f) ? 0 : (1f - this.mLayer_1.getAlpha()) ;
                                long duration = (long) (alpha * this.mSweepAnimationDuration);
                                float valueX = 1f - this.mTranslationDes * 2;
                                float valueY = 1f - this.mTranslationDes * 2;
                                layer3ObjectAnimator.setFloatValues(this.mLayer_3.getAlpha(), 0f);
                                layer3ObjectAnimator.setDuration(duration);
                                layer3ObjectAnimator.addListener(new AnimatorListenerAdapter() {
                                    @Override
                                    public void onAnimationEnd(Animator animation) {
                                        changePage();

                                        removeView(mLayer_2);
                                        removeView(mLayer_3);
                                        addView(mLayer_2, 1);
                                        addView(mLayer_3, 2);

                                        resetViewProperty(mTopImageView, mLayer_3, mLayer_2, mBottomImageView);
                                        updateBitmap( );
                                        onSweepAnimationEnd();
                                    }
                                });
                                this.mLayer_2.animate().scaleX(valueX).scaleY(valueY).translationY(this.mHeight * this.mTranslationDes * 2)
                                        .alpha(1f).setDuration(duration);
                                valueX -= this.mScaleXDes;
                                valueY -= this.mScaleYDes;
                                this.mLayer_1.animate().scaleX(valueX).scaleY(valueY).translationY(this.mHeight * this.mTranslationDes)
                                        .alpha(1f).setDuration(duration);

                            }else{//return back

                                long duration = (long) (this.mLayer_1.getAlpha() * this.mSweepAnimationDuration);
                                layer3ObjectAnimator.setFloatValues(this.mLayer_3.getAlpha(), 1f);
                                layer3ObjectAnimator.setDuration(duration);
                                layer3ObjectAnimator.addListener(new AnimatorListenerAdapter() {
                                    @Override
                                    public void onAnimationEnd(Animator animation) {
                                        mLayer_1.setVisibility(INVISIBLE);
                                        onSweepAnimationEnd();
                                    }
                                });
                                float valueX = 1f - this.mTranslationDes * 2 - this.mScaleXDes;
                                float valueY = 1f - this.mTranslationDes * 2 - this.mScaleYDes;
                                this.mLayer_2.animate().scaleX(valueX).scaleY(valueY).translationY(this.mHeight * this.mTranslationDes)
                                        .alpha(1f).setDuration(duration);
                                valueX -= this.mScaleXDes;
                                valueY -= this.mScaleYDes;
                                this.mLayer_1.animate().scaleX(valueX).scaleY(valueY).translationY(0f).alpha(0f).setDuration(duration);

                            }

                        }else if(this.mSweepStartDirection == DIRECTION_FORWARD){

                            if(this.mLayer_1.getAlpha() < 0.5 || Math.abs(this.mVelocityTracker.getYVelocity()) > this.mVelocityEdge){
                                long duration = (long) (this.mLayer_1.getAlpha() * this.mSweepAnimationDuration);
                                layer3ObjectAnimator.setFloatValues(this.mLayer_3.getAlpha(), 1f);
                                layer3ObjectAnimator.setDuration(duration);
                                layer3ObjectAnimator.addListener(new AnimatorListenerAdapter() {
                                    @Override
                                    public void onAnimationEnd(Animator animation) {
                                        changePage();

                                        removeView(mLayer_1);
                                        removeView(mLayer_2);
                                        addView(mLayer_1, 1);
                                        addView(mLayer_2, 2);

                                        resetViewProperty(mTopImageView, mLayer_2, mLayer_1, mBottomImageView);
                                        updateBitmap();
                                        onSweepAnimationEnd();
                                    }
                                });
                                float valueX = 1f - this.mTranslationDes * 2 - this.mScaleXDes;
                                float valueY = 1f - this.mTranslationDes * 2 - this.mScaleYDes;
                                this.mLayer_2.animate().scaleX(valueX).scaleY(valueY).translationY(this.mHeight * this.mTranslationDes)
                                        .alpha(1f).setDuration(duration);
                                valueX -= this.mScaleXDes;
                                valueY -= this.mScaleYDes;
                                this.mLayer_1.animate().scaleX(valueX).scaleY(valueY).translationY(0f).alpha(0f)
                                        .setDuration(duration);

                            }else{//return back

                                float alpha = 1f - this.mLayer_1.getAlpha() ;
                                long duration = (long) (alpha * this.mSweepAnimationDuration);
                                layer3ObjectAnimator.setFloatValues(this.mLayer_3.getAlpha(), 0f);
                                layer3ObjectAnimator.setDuration(duration);
                                layer3ObjectAnimator.addListener(new AnimatorListenerAdapter() {
                                    @Override
                                    public void onAnimationEnd(Animator animation) {
                                        mLayer_3.setVisibility(INVISIBLE);
                                        onSweepAnimationEnd();
                                    }
                                });
                                float valueX = 1f - this.mTranslationDes * 2;
                                float valueY = 1f - this.mTranslationDes * 2;
                                this.mLayer_2.animate().scaleX(valueX).scaleY(valueY).translationY(this.mHeight * this.mTranslationDes * 2)
                                        .alpha(1f).setDuration(duration);
                                valueX -= this.mScaleXDes;
                                valueY -= this.mScaleYDes;
                                this.mLayer_1.animate().scaleX(valueX).scaleY(valueY).translationY(this.mHeight * this.mTranslationDes)
                                        .alpha(1f).setDuration(duration);

                            }

                        }//else if end

                        this.mLayer_1.animate().start();
                        this.mLayer_2.animate().start();
                        layer3ObjectAnimator.start();
                        this.mAnimationRunning = true;

                    }//if canSweep end
                    break;
                default:
                    break;
            }//switch end

            return true;
        }//if end

        return false;
    }

    private void resetLayer( ){
        if(this.mSweepStartDirection == DIRECTION_BACKWARD){
            this.mLayer_1 = this.mTopImageView;
            this.mLayer_1.setVisibility(VISIBLE);
            this.mLayer_1.setAlpha(0f);
            if(this.mCurrentPage == PAGE_FIRST){
                this.mTopImageView.setImageBitmap(this.mSecondViewBitmap);
                this.mLayer_2 = this.mFirstView;
                this.mLayer_3 = this.mSecondView;
            }else if(this.mCurrentPage == PAGE_SECOND){
                this.mTopImageView.setImageBitmap(this.mFirstViewBitmap);
                this.mLayer_2 = this.mSecondView;
                this.mLayer_3 = this.mFirstView;
            }
        }else{
            if(this.mCurrentPage == PAGE_FIRST){
                this.mLayer_1 = this.mFirstView;
                this.mLayer_2 = this.mSecondView;
                this.mBottomImageView.setImageBitmap(this.mFirstViewBitmap);
            }else if(this.mCurrentPage == PAGE_SECOND){
                this.mLayer_1 = this.mSecondView;
                this.mLayer_2 = this.mFirstView;
                this.mBottomImageView.setImageBitmap(this.mSecondViewBitmap);
            }
            this.mLayer_3 = this.mBottomImageView;
            this.mLayer_3.setVisibility(VISIBLE);
            this.mLayer_3.setAlpha(0f);
        }
    }

    public void setPageView(View firstView, View secondView){
        this.mFirstView = firstView;
        this.mSecondView = secondView;
        this.mFirstView.setDrawingCacheEnabled(true);
        this.mSecondView.setDrawingCacheEnabled(true);
        this.mCurrentPage = PAGE_FIRST;
        addPageView();
    }

    private void addPageView( ){
        addView(this.mBottomImageView, 0, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        addView(this.mSecondView, 1, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        addView(this.mFirstView, 2, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        addView(this.mTopImageView, 3, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
    }

    //call this method because view.getHeight return 0 util the view is visible, and we don't know when the view is visible
    public void initViewSize(int width, int height) {
        if(this.mWidth == 0 || this.mHeight == 0){
            this.mWidth = width;
            this.mHeight = height;
            this.mPivotX = this.mWidth / 2;
            this.mPivotY = 0f;
            this.mMovEndDis = this.mHeight / 5;
            int movNum = (int) (this.mMovEndDis / this.mDisEdge);
            this.mScaleXChangeWhenMov = this.mScaleXDes / movNum;
            this.mScaleYChangeWhenMov = this.mScaleYDes / movNum;
            this.mTranslationChangeWhenMov = this.mHeight * this.mTranslationDes / movNum;
            this.mAlphaChangeWhenMov = 1f / movNum;

            this.mFirstView.setPivotX(this.mPivotX);
            this.mFirstView.setPivotY(this.mPivotY);
            this.mSecondView.setPivotX(this.mPivotX);
            this.mSecondView.setPivotY(this.mPivotY);
            this.mBottomImageView.setPivotX(this.mPivotX);
            this.mBottomImageView.setPivotY(this.mPivotY);
            this.mTopImageView.setPivotX(this.mPivotX);
            this.mTopImageView.setPivotY(this.mPivotY);

            resetViewProperty(this.mTopImageView, this.mFirstView, this.mSecondView, this.mBottomImageView);
            initBitmap( );
        }
    }

    private void resetViewProperty(View layer1, View layer2, View layer3, View layer4){
        float valueX = 1f - this.mTranslationDes * 2;
        float valueY = 1f - this.mTranslationDes * 2;
        layer4.setScaleX(valueX);
        layer4.setScaleY(valueY);
        layer3.setScaleX(valueX);
        layer3.setScaleY(valueY);
        valueX -= this.mScaleXDes;
        valueY -= this.mScaleYDes;
        layer2.setScaleX(valueX);
        layer2.setScaleY(valueY);
        valueX -= this.mScaleXDes;
        valueY -= this.mScaleYDes;
        layer1.setScaleX(valueX);
        layer1.setScaleY(valueY);

        float translation = this.mHeight * this.mTranslationDes;
        layer1.setTranslationY(0f);
        layer2.setTranslationY(translation);
        layer3.setTranslationY(translation * 2);
        layer4.setTranslationY(translation * 2);

        layer1.setAlpha(0f);
        layer2.setAlpha(1f);
        layer3.setAlpha(1f);
        layer4.setAlpha(0f);

        layer1.setVisibility(INVISIBLE);
        layer4.setVisibility(INVISIBLE);
    }

    public int getCurrentPage( ){
        return this.mCurrentPage;
    }

    public void setPageSweepListener(PageSweepListener listener){
        this.mPageSweepListener = listener;
    }

    public interface PageSweepListener{
        public void onPageSweepStart(int direction);
        public void onPageSelected(int pageIndex);
    }

    public void onDestroy( ){
        if(this.mVelocityTracker != null){
            this.mVelocityTracker.recycle();
        }
    }

}
