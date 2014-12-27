package com.android.threeday.activity.mainActivity;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.HandlerThread;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.text.format.Time;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.threeday.R;
import com.android.threeday.broadcastReceiver.ResendAlarmManager;
import com.android.threeday.fragment.dialogFragment.TaskDoneMenuFragment;
import com.android.threeday.fragment.dialogFragment.TaskUndoneMenuFragment;
import com.android.threeday.model.updateData.UpdateDataModel;
import com.android.threeday.util.Util;
import com.android.threeday.view.BgScrollView;
import com.android.threeday.view.SlideLayer;

/**
 * Created by user on 2014/11/11.
 */
public class MainActivityManager {
    private final long mAnimationDuration = 500;
    private final long mBgViewDuration = 1000;
    static final int DAY_NUM = 3;
    static final int YESTERDAY_INDEX = 0;
    static final int TODAY_INDEX = 1;
    static final int TOMORROW_INDEX = 2;

    private ObjectAnimator mBgView1Animator;
    private ObjectAnimator mBgView2Animator;
    private FragmentActivity mActivity;
    private TaskUndoneMenuFragment mTaskUndoneMenuFragment;
    private TaskDoneMenuFragment mTaskDoneMenuFragment;
    private View mWeatherView;
    private TextView mWeatherTextView;
    private TextView mTemperatureView;
    private View mBgView1;
    private View mBgView2;
    private BgScrollView mBgScrollView;
    private ViewPager mViewPager;
    private SlideLayer mSlideLayer;
    private TextView mTitleTextView;
    private TextView mWordsTextView;
    private ImageView mDayEvaluationImageView;
    private View mCheckTasksView;
    private AnimatorSet mTopTitleViewChangeAnimator;
    private AnimatorSet mTopDayEvaluationViewChangeAnimator;
    private AnimatorSet mWeatherViewChangeAnimator;
    private AnimatorSet mTopCheckViewChangeAnimator;
    private AnimatorSet mBottomChangeAnimator;
    private SharedPreferences mSharedPreferences;
    private WeatherManager mWeatherManager;
    private WeatherManager.WeatherLoadListener mWeatherLoadListener = new WeatherManager.WeatherLoadListener() {
        @Override
        public void onWeatherLoadSuccess() {
            mBgScrollView.setVisibility(View.GONE);
            if(mCurrentPosition != -1){
                initBackground(mCurrentPosition);
            }
            setWeatherView( );
        }

        @Override
        public void onWeatherLoadFail() {

        }
    };
    private static final HandlerThread mHandlerThread = new HandlerThread("HandlerThread");

    private int mPassDay;
    private int mBgResId;
    private int mCurrentPosition = -1;
    private boolean mBg1AnimatorCancel;

    MainActivityManager(FragmentActivity activity){
        this.mActivity = activity;
        initView();
        initData();
        if(!mHandlerThread.isAlive()){
            mHandlerThread.start();
        }
    }

    private void initView( ){
        this.mBgView1 = mActivity.findViewById(R.id.bgView1);
        this.mBgView2 = mActivity.findViewById(R.id.bgView2);
        this.mBgScrollView = (BgScrollView) mActivity.findViewById(R.id.bgScrollView);
        DisplayMetrics displayMetrics = new DisplayMetrics();
        mActivity.getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        this.mBgScrollView.init(DAY_NUM, displayMetrics.widthPixels, displayMetrics.widthPixels * 2);
        this.mBgScrollView.setScrollBackgroundRes(R.drawable.bg_full_day);

        this.mViewPager = (ViewPager) mActivity.findViewById(R.id.fragmentViewPager);

        this.mTaskDoneMenuFragment = new TaskDoneMenuFragment();
        this.mTaskUndoneMenuFragment = new TaskUndoneMenuFragment();

        this.mTitleTextView = (TextView) mActivity.findViewById(R.id.titleTextView);
        this.mWordsTextView = (TextView) mActivity.findViewById(R.id.wordsTextView);
        this.mDayEvaluationImageView = (ImageView) mActivity.findViewById(R.id.dayEvaluationImageView);
        this.mCheckTasksView = mActivity.findViewById(R.id.checkTaskButton);

        this.mWeatherView = mActivity.findViewById(R.id.weatherView);
        this.mWeatherTextView = (TextView) mActivity.findViewById(R.id.weatherTextView);
        this.mTemperatureView = (TextView) mActivity.findViewById(R.id.temperatureTextView);
    }

    private void initData( ){
        int startY = -this.mActivity.getResources().getDimensionPixelOffset(R.dimen.main_activity_top_animation_startY);
        this.mTopTitleViewChangeAnimator = new AnimatorSet();
        ObjectAnimator topTranslationAnimator = ObjectAnimator.ofFloat(this.mTitleTextView, "translationY", startY, 0f);
        ObjectAnimator topAlphaAnimation = ObjectAnimator.ofFloat(this.mTitleTextView, "alpha", 0f, 1f);
        this.mTopTitleViewChangeAnimator.playTogether(topTranslationAnimator, topAlphaAnimation);
        this.mTopTitleViewChangeAnimator.setDuration(this.mAnimationDuration);
        this.mTopTitleViewChangeAnimator.setInterpolator(new DecelerateInterpolator());
        this.mTopTitleViewChangeAnimator.setTarget(this.mTitleTextView);

        this.mTopCheckViewChangeAnimator = this.mTopTitleViewChangeAnimator.clone();
        this.mTopCheckViewChangeAnimator.setTarget(this.mCheckTasksView);

        this.mTopDayEvaluationViewChangeAnimator = this.mTopTitleViewChangeAnimator.clone();
        this.mTopDayEvaluationViewChangeAnimator.setTarget(this.mDayEvaluationImageView);

        this.mWeatherViewChangeAnimator = this.mTopTitleViewChangeAnimator.clone();
        this.mWeatherViewChangeAnimator.setTarget(this.mWeatherView);

        startY = this.mActivity.getResources().getDimensionPixelOffset(R.dimen.main_activity_bottom_animation_startY);
        this.mBottomChangeAnimator = new AnimatorSet();
        ObjectAnimator bottomTranslationAnimator = ObjectAnimator.ofFloat(this.mWordsTextView, "translationY", startY, 0f);
        ObjectAnimator bottomAlphaAnimator = ObjectAnimator.ofFloat(this.mWordsTextView, "alpha", 0f, 1f);
        this.mBottomChangeAnimator.playTogether(bottomTranslationAnimator, bottomAlphaAnimator);
        this.mBottomChangeAnimator.setDuration(this.mAnimationDuration);
        this.mBottomChangeAnimator.setTarget(this.mWordsTextView);
        this.mBottomChangeAnimator.setInterpolator(new DecelerateInterpolator());

        this.mSharedPreferences = this.mActivity.getSharedPreferences(Util.PREFERENCE_NAME, Context.MODE_PRIVATE);

        this.mBgView1Animator = ObjectAnimator.ofFloat(this.mBgView1, "alpha", 1f, 0f)
                .setDuration(this.mBgViewDuration);
        this.mBgView1Animator.addListener(new AnimatorListenerAdapter() {

            @Override
            public void onAnimationEnd(Animator animation) {
                mBgView2Animator.start();
            }

        });
        this.mBgView2Animator = ObjectAnimator.ofFloat(this.mBgView2, "alpha", 0f, 1f).setDuration(this.mBgViewDuration);
        this.mBgView2Animator.addListener(new AnimatorListenerAdapter() {

            @Override
            public void onAnimationEnd(Animator animation) {
                switchBgView();
                if(mBg1AnimatorCancel){
                    startBgAnimator();
                    mBg1AnimatorCancel = false;
                }
            }
        });

        this.mWeatherManager = new WeatherManager(this.mActivity);
        this.mWeatherManager.setWeatherLoadListener(this.mWeatherLoadListener);
    }

    private void switchBgView( ){
        View view = mBgView1;
        mBgView1 = mBgView2;
        mBgView2 = view;
    }

    private void setWeatherView( ){
        if(this.mCurrentPosition != -1){
            if(this.mWeatherManager.isWeatherAvailable()){
                this.mWeatherView.setVisibility(View.VISIBLE);
                if(this.mCurrentPosition == TODAY_INDEX){
                    this.mWeatherTextView.setText(this.mWeatherManager.getTodayWeather());
                    this.mTemperatureView.setText(this.mWeatherManager.getTodayTemperature());
                }else if(this.mCurrentPosition == TOMORROW_INDEX){
                    this.mWeatherTextView.setText(this.mWeatherManager.getTomorrowWeather());
                    this.mTemperatureView.setText(this.mWeatherManager.getTomorrowTemperature());
                }
                if(this.mWeatherViewChangeAnimator.isRunning()){
                    this.mWeatherViewChangeAnimator.cancel();
                }
                this.mWeatherViewChangeAnimator.start();
            }else{
                this.mWordsTextView.setVisibility(View.INVISIBLE);
            }
        }
    }

    public static HandlerThread getHandlerThread( ){
        return mHandlerThread;
    }

    ViewPager getViewPager( ){
        return this.mViewPager;
    }

    void showUndoneTaskMenu(boolean taskShouldRemain, boolean taskCanDone){
        this.mTaskUndoneMenuFragment.setTaskRemain(taskShouldRemain);
        this.mTaskUndoneMenuFragment.setTaskCanDone(taskCanDone);
        this.mTaskUndoneMenuFragment.show(this.mActivity.getSupportFragmentManager(), "UndoneMenu");
    }

    void hideUndoneTaskMenu( ){
        this.mTaskUndoneMenuFragment.dismiss();
    }

    void showDoneTaskMenu( ){
        this.mTaskDoneMenuFragment.show(this.mActivity.getSupportFragmentManager(), "DoneMenu");
    }

    void hideDoneTaskMenu( ){
        this.mTaskDoneMenuFragment.dismiss();
    }

    boolean isMenuVisible( ){
        return this.mTaskDoneMenuFragment.isVisible() ||
                this.mTaskUndoneMenuFragment.isVisible();
    }

    void hideMenu( ){
        hideUndoneTaskMenu();
        hideDoneTaskMenu();
    }

    void setDayEvaluation(int evaluation){
        setDayEvaluationImageViewResource(evaluation);
        if(this.mTopDayEvaluationViewChangeAnimator.isRunning()){
            this.mTopDayEvaluationViewChangeAnimator.cancel();
        }
        this.mTopDayEvaluationViewChangeAnimator.start();
    }

    private void setDayEvaluationImageViewResource(int evaluation){
        int resId;
        switch (evaluation){
            case Util.EVALUATION_DEFAULT:
                resId = R.drawable.ic_launcher;
                break;
            case Util.EVALUATION_GOOD:
                resId = R.drawable.ic_launcher;
                break;
            case Util.EVALUATION_MID:
                resId = R.drawable.ic_launcher;
                break;
            case Util.EVALUATION_BAD:
                resId = R.drawable.ic_launcher;
                break;
            default:
                resId = R.drawable.ic_launcher;
                break;
        }
        this.mDayEvaluationImageView.setImageResource(resId);
    }

    void initBackground(int position){
        if(this.mWeatherManager.isWeatherAvailable()){
            setWeatherBackgroundResId(position);
            this.mBgView2.setAlpha(0f);
            this.mBgView1.setBackgroundResource(this.mBgResId);
        }else{
            this.mBgScrollView.scrollToPage(position);
        }
    }

    void setWeatherBackgroundResId(int position){

        switch (position){
            case YESTERDAY_INDEX:
                this.mBgResId = R.drawable.bg_fog_day;
                break;
            case TODAY_INDEX:
                this.mBgResId = R.drawable.bg_snow_night;
                break;
            case TOMORROW_INDEX:
                this.mBgResId = R.drawable.bg_slight_rain_day;
                break;
            default:
                this.mBgResId = R.drawable.bg0_fine_day;
        }
    }

    void changeBackground(int position){
        if(this.mWeatherManager.isWeatherAvailable()){
            setWeatherBackgroundResId(position);
            if(this.mBgView1Animator.isRunning() || this.mBgView2Animator.isRunning()){
                this.mBg1AnimatorCancel = true;
            }else{
                startBgAnimator();
            }
        }else{
            this.mBgScrollView.smoothScrollToPage(position);
        }
    }

    private void startBgAnimator( ){
        this.mBgView2.setBackgroundResource(this.mBgResId);
        this.mBgView1Animator.setTarget(this.mBgView1);
        this.mBgView2Animator.setTarget(this.mBgView2);
        this.mBgView1Animator.start();
    }

    void onPageSelected(int position){
        this.mCurrentPosition = position;
        switch (position){
            case YESTERDAY_INDEX:
                this.mTitleTextView.setText(R.string.title_yesterday);
                this.mWordsTextView.setText(R.string.words_yesterday);
                this.mCheckTasksView.setVisibility(View.INVISIBLE);
                this.mDayEvaluationImageView.setVisibility(View.VISIBLE);
                this.mWeatherView.setVisibility(View.INVISIBLE);
                break;
            case TODAY_INDEX:
                this.mTitleTextView.setText(R.string.title_today);
                this.mWordsTextView.setText(R.string.words_today);
                boolean check = this.mSharedPreferences.getBoolean(Util.PREFERENCE_KEY_TODAY_TASKS_CHECK, false);
                if(check){
                    this.mCheckTasksView.setVisibility(View.INVISIBLE);
                    this.mDayEvaluationImageView.setVisibility(View.VISIBLE);
                }else{
                    this.mCheckTasksView.setVisibility(View.VISIBLE);
                    this.mDayEvaluationImageView.setVisibility(View.INVISIBLE);
                    if(this.mTopCheckViewChangeAnimator.isRunning()){
                        this.mTopCheckViewChangeAnimator.cancel();
                    }
                    this.mTopCheckViewChangeAnimator.start();
                }
                setWeatherView();
                break;
            case TOMORROW_INDEX:
                this.mTitleTextView.setText(R.string.title_tomorrow);
                this.mWordsTextView.setText(R.string.words_tomorrow);
                this.mCheckTasksView.setVisibility(View.INVISIBLE);
                this.mDayEvaluationImageView.setVisibility(View.INVISIBLE);
                setWeatherView();
                break;
        }
        if(this.mTopTitleViewChangeAnimator.isRunning()){
            this.mTopTitleViewChangeAnimator.cancel();
        }
        if(this.mBottomChangeAnimator.isRunning()){
            this.mBottomChangeAnimator.cancel();
        }
        this.mTopTitleViewChangeAnimator.start();
        this.mBottomChangeAnimator.start();
    }

    void initSlideLayer( ){
        this.mSlideLayer = (SlideLayer) this.mActivity.findViewById(R.id.slideLayer);
        this.mSlideLayer.setVisibility(View.VISIBLE);
        View view = View.inflate(this.mActivity, R.layout.slide_layer_main, null);
        this.mSlideLayer.setMainView(view);
    }

    SlideLayer getSlideLayer( ){
        return this.mSlideLayer;
    }

    void checkNewDayPass( ){
        long dayTime = this.mSharedPreferences.getLong(Util.PREFERENCE_KEY_LAST_IN_DAY_TIME, -1);
        this.mPassDay = -1;
        if(dayTime != -1){
            Time time = new Time();
            time.set(dayTime);
            Time now = new Time();
            now.setToNow();
            if(time.before(now)){
                if(time.year == now.year){
                    this.mPassDay = now.yearDay - time.yearDay;
                }else{
                    if(time.year % 4 == 0){// 366 day
                        this.mPassDay = 365 - time.yearDay + now.yearDay + 1;
                    }else{// 365 day
                        this.mPassDay = 364 - time.year + now.yearDay + 1;
                    }
                }
            }
        }
    }

    int getNewDayPass( ){
        return this.mPassDay;
    }

    void setNewDayChecked( ){
        Time time = new Time();
        time.setToNow();
        this.mSharedPreferences.edit().
                putLong(Util.PREFERENCE_KEY_LAST_IN_DAY_TIME, time.toMillis(false))
                .putBoolean(Util.PREFERENCE_KEY_TODAY_TASKS_CHECK, false).commit();
    }

    void resendAlarm(){
        ResendAlarmManager resendAlarmManager = new ResendAlarmManager(this.mActivity);
        resendAlarmManager.sendTaskRemainAlarmsAgain();
    }

    void updateDataAtNewDay( ){
        if(this.mPassDay > 0){
            UpdateDataModel updateDataModel = new UpdateDataModel(this.mActivity);
            if(this.mPassDay > 2){
                updateDataModel.deleteAllData( );
            }else{
                for(int i = 0; i < this.mPassDay; i++){
                    updateDataModel.updateDataAtNewDay();
                }
            }
        }
    }

    long getViewAnimationDuration( ){
        return this.mAnimationDuration;
    }

    void onDestroy( ){
        this.mHandlerThread.quit();
    }

    void checkTodayCheck(int todayEvaluation) {
        boolean todayCheck = this.mSharedPreferences.getBoolean(Util.PREFERENCE_KEY_TODAY_TASKS_CHECK, false);
        if(todayCheck){
            this.mCheckTasksView.setVisibility(View.INVISIBLE);
            this.mDayEvaluationImageView.setVisibility(View.VISIBLE);
            setDayEvaluationImageViewResource(todayEvaluation);
        }else{
            this.mCheckTasksView.setVisibility(View.VISIBLE);
            this.mDayEvaluationImageView.setVisibility(View.INVISIBLE);
        }
    }

    public void checkWeather() {
        this.mWeatherManager.checkWeather();
    }
}
