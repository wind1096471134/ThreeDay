package com.android.threeday.activity.mainActivity;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.HandlerThread;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.text.format.Time;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.threeday.R;
import com.android.threeday.fragment.dialogFragment.TaskDoneMenuFragment;
import com.android.threeday.fragment.dialogFragment.TaskUndoneMenuFragment;
import com.android.threeday.model.updateData.UpdateDataModel;
import com.android.threeday.service.EveningCheckService;
import com.android.threeday.service.NewDaySettingService;
import com.android.threeday.util.Util;
import com.android.threeday.view.SlideLayer;

/**
 * Created by user on 2014/11/11.
 */
public class MainActivityManager {
    private final long mAnimationDuration = 500;
    static final int DAY_NUM = 3;
    static final int YESTERDAY_INDEX = 0;
    static final int TODAY_INDEX = 1;
    static final int TOMORROW_INDEX = 2;

    private FragmentActivity mActivity;
    private TaskUndoneMenuFragment mTaskUndoneMenuFragment;
    private TaskDoneMenuFragment mTaskDoneMenuFragment;
    private ViewPager mViewPager;
    private SlideLayer mSlideLayer;
    private TextView mTitleTextView;
    private TextView mWordsTextView;
    private ImageView mDayEvaluationImageView;
    private View mCheckTasksView;
    private AnimatorSet mTopTitleViewChangeAnimator;
    private AnimatorSet mTopDayEvaluationViewChangeAnimator;
    private AnimatorSet mTopCheckViewChangeAnimator;
    private AnimatorSet mBottomChangeAnimator;
    private SharedPreferences mSharedPreferences;
    private static final HandlerThread mHandlerThread = new HandlerThread("HandlerThread");

    MainActivityManager(FragmentActivity activity){
        this.mActivity = activity;
        initView( );
        initData( );
        if(!mHandlerThread.isAlive()){
            mHandlerThread.start();
        }
    }

    private void initView( ){
        this.mViewPager = (ViewPager) mActivity.findViewById(R.id.fragmentViewPager);

        this.mTaskDoneMenuFragment = new TaskDoneMenuFragment();
        this.mTaskUndoneMenuFragment = new TaskUndoneMenuFragment();

        this.mTitleTextView = (TextView) mActivity.findViewById(R.id.titleTextView);
        this.mWordsTextView = (TextView) mActivity.findViewById(R.id.wordsTextView);
        this.mDayEvaluationImageView = (ImageView) mActivity.findViewById(R.id.dayEvaluationImageView);
        this.mCheckTasksView = mActivity.findViewById(R.id.checkTaskButton);
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

        startY = this.mActivity.getResources().getDimensionPixelOffset(R.dimen.main_activity_bottom_animation_startY);
        this.mBottomChangeAnimator = new AnimatorSet();
        ObjectAnimator bottomTranslationAnimator = ObjectAnimator.ofFloat(this.mWordsTextView, "translationY", startY, 0f);
        ObjectAnimator bottomAlphaAnimator = ObjectAnimator.ofFloat(this.mWordsTextView, "alpha", 0f, 1f);
        this.mBottomChangeAnimator.playTogether(bottomTranslationAnimator, bottomAlphaAnimator);
        this.mBottomChangeAnimator.setDuration(this.mAnimationDuration);
        this.mBottomChangeAnimator.setTarget(this.mWordsTextView);
        this.mBottomChangeAnimator.setInterpolator(new DecelerateInterpolator());

        this.mSharedPreferences = this.mActivity.getSharedPreferences(Util.PREFERENCE_NAME, Context.MODE_PRIVATE);
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
        this.mTopDayEvaluationViewChangeAnimator.cancel();
        this.mTopDayEvaluationViewChangeAnimator.start();
    }

    void onPageSelected(int position){
        switch (position){
            case YESTERDAY_INDEX:
                this.mTitleTextView.setText(R.string.title_yesterday);
                this.mWordsTextView.setText(R.string.words_yesterday);
                this.mCheckTasksView.setVisibility(View.INVISIBLE);
                this.mDayEvaluationImageView.setVisibility(View.VISIBLE);
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
                    this.mTopCheckViewChangeAnimator.cancel();
                    this.mTopCheckViewChangeAnimator.start();
                }
                break;
            case TOMORROW_INDEX:
                this.mTitleTextView.setText(R.string.title_tomorrow);
                this.mWordsTextView.setText(R.string.words_tomorrow);
                this.mCheckTasksView.setVisibility(View.INVISIBLE);
                this.mDayEvaluationImageView.setVisibility(View.INVISIBLE);
                break;
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

    boolean isNewDayChecked( ){
        return this.mSharedPreferences.getBoolean(Util.PREFERENCE_KEY_NEW_DAY_CHECK, true);
    }

    void setNewDayChecked( ){
        this.mSharedPreferences.edit().putBoolean(Util.PREFERENCE_KEY_NEW_DAY_CHECK, true).commit();
    }

    boolean isFirstUsing( ){
        return this.mSharedPreferences.getBoolean(Util.PREFERENCE_KEY_FIRST_USING, true);
    }

    void setFirstUsingFalse( ){
        this.mSharedPreferences.edit().putBoolean(Util.PREFERENCE_KEY_FIRST_USING, false).commit();
    }

    void initDefaultAlarm( ){
        setNewDayAlarm();
        setDefaultEveningCheckAlarm();
    }

    private void setDefaultEveningCheckAlarm( ){
        Intent intent = new Intent(this.mActivity, EveningCheckService.class);
        PendingIntent pendingIntent = PendingIntent.getService(this.mActivity, Util.EVENING_CHECK_ALARM_ID, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        AlarmManager alarmManager = (AlarmManager) this.mActivity.getSystemService(Context.ALARM_SERVICE);
        Time time = new Time();
        time.setToNow();
        time.hour = Util.EVENING_CHECK_TIME_DEFAULT_HOUR;
        time.minute = Util.EVENING_CHECK_TIME_DEFAULT_MINUTE;
        Time now = new Time();
        now.setToNow();
        long startTime = time.after(now) ? time.toMillis(false) : time.toMillis(false) + Util.A_DAY_IN_MILLIS;
        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, startTime, Util.A_DAY_IN_MILLIS, pendingIntent);
    }

    private void  setNewDayAlarm(){
        Intent intent = new Intent(this.mActivity, NewDaySettingService.class);
        PendingIntent pendingIntent = PendingIntent.getService(this.mActivity, Util.UPDATE_DATA_AT_NEW_DAY_ALARM_ID, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        AlarmManager alarmManager = (AlarmManager) this.mActivity.getSystemService(Context.ALARM_SERVICE);
        Time time = new Time();
        time.setToNow();
        time.hour = Util.NEW_DAY_ALARM_HOUR;
        time.minute = Util.NEW_DAY_ALARM_MINUTE;
        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, time.toMillis(false) + Util.A_DAY_IN_MILLIS, Util.A_DAY_IN_MILLIS, pendingIntent);
    }

    void testSetNewDayAlarm(){
        Intent intent = new Intent(this.mActivity, NewDaySettingService.class);
        PendingIntent pendingIntent = PendingIntent.getService(this.mActivity, Util.UPDATE_DATA_AT_NEW_DAY_ALARM_ID, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        AlarmManager alarmManager = (AlarmManager) this.mActivity.getSystemService(Context.ALARM_SERVICE);
        Time time = new Time();
        time.setToNow();
        alarmManager.set(AlarmManager.RTC_WAKEUP, time.toMillis(false), pendingIntent);
    }

    void updateDataAtNewDay( ){
        UpdateDataModel updateDataModel = new UpdateDataModel(this.mActivity);
        updateDataModel.updateDataAtNewDay();
        this.mSharedPreferences.edit().putBoolean(Util.PREFERENCE_KEY_TODAY_TASKS_CHECK, false).commit();
    }

    long getViewAnimationDuration( ){
        return this.mAnimationDuration;
    }

    void onDestroy( ){
        this.mHandlerThread.quit();
    }
}
