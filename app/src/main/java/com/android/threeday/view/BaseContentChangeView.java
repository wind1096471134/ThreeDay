package com.android.threeday.view;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import java.util.Random;

/**
 * Created by user on 2014/10/30.
 */
public abstract class BaseContentChangeView extends FrameLayout{
    public static final int FIRST_CONTENT_INDEX = 0;
    public static final int SECOND_CONTENT_INDEX = 1;
    protected int mId = -1;
    protected int mFirstContentDuration;
    protected int mSecondContentDuration;
    protected int mCurrentContentIndex;
    protected boolean mContentChanging;

    protected Random random;
    protected View mFirstContentView;
    protected View mSecondContentView;
    protected View mForegroundView;
    protected ContentChangeListener mContentChangeListener;
    protected Handler mChangeContentHandler;
    protected Handler.Callback mChangeContentCallback = new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            if(msg.what == mId){
                mCurrentContentIndex = mCurrentContentIndex == FIRST_CONTENT_INDEX ? SECOND_CONTENT_INDEX : FIRST_CONTENT_INDEX;
                post(mChangeViewRunnable);
                if(mCurrentContentIndex == FIRST_CONTENT_INDEX){
                    mChangeContentHandler.sendEmptyMessageDelayed(mId, mFirstContentDuration + getRandomFirstViewDelay());
                }else if(mCurrentContentIndex == SECOND_CONTENT_INDEX){
                    mChangeContentHandler.sendEmptyMessageDelayed(mId, mSecondContentDuration + getRandomSecondViewDelay());
                }
                return true;
            }
            return false;
        }
    };
    protected Runnable mStartChangeRunnable = new Runnable() {
        @Override
        public void run() {
            mChangeContentHandler.sendEmptyMessage(mId);
        }
    };
    protected Runnable mChangeViewRunnable = new Runnable() {
        @Override
        public void run() {
            changeContent( );
        }
    };

    public BaseContentChangeView(Context context) {
        super(context);
        init(context);
    }

    public BaseContentChangeView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public BaseContentChangeView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    public BaseContentChangeView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context);
    }

    private void init(Context context){
        this.mForegroundView = new View(context);
        random = new Random(0);
    }

    protected int getRandomFirstViewDelay( ){
        random.setSeed(random.nextLong());
        return random.nextInt(10000);
    }

    protected int getRandomSecondViewDelay( ){
        random.setSeed(random.nextLong());
        return random.nextInt(2000);
    }

    public void setContentView(View firstContentView, int firstContentDuration, View secondContentView, int secondContentDuration){
        this.mFirstContentView = firstContentView;
        this.mSecondContentView = secondContentView;
        this.mFirstContentDuration = firstContentDuration;
        this.mSecondContentDuration = secondContentDuration;

        super.addView(this.mFirstContentView);
        super.addView(this.mSecondContentView);
        if(this.mForegroundView.getParent() != null){
            ((ViewGroup)this.mForegroundView.getParent()).removeView(this.mForegroundView);
        }
        super.addView(this.mForegroundView, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        resetData();
    }

    public void setHandler(long id, Looper looper){
        this.mId = (int) id;
        this.mChangeContentHandler = new Handler(looper, this.mChangeContentCallback);
    }

    public boolean canChangeContent( ){
        return this.mChangeContentHandler != null && this.mChangeContentHandler.getLooper() != null;
    }

    public void reset(long id){
        this.mChangeContentHandler.removeCallbacks(this.mStartChangeRunnable);
        this.mChangeContentHandler.removeMessages(this.mId);
        this.mId = (int) id;
        reset();
    }

    public int getViewId( ){
        return this.mId;
    }

    private void reset( ){
        resetData();
        this.mCurrentContentIndex = FIRST_CONTENT_INDEX;
        this.mContentChanging = false;
    }

    public void stopChangeContent( ){
        this.mContentChanging = false;
        reset(this.mId);
    }

    public void startChangeContent(long delay){
        this.mContentChanging = true;
        resetData();
        this.mChangeContentHandler.postDelayed(mStartChangeRunnable, delay);
    }

    public boolean isContentChanging( ){
        return this.mContentChanging;
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

    public void setOnPressRes(int resId){
        this.mForegroundView.setBackgroundResource(resId);
    }

    protected abstract void changeContent( );

    protected abstract void resetData( );

    public interface ContentChangeListener{
        public void onContentChange(int currentContentIndex);
    }

}
