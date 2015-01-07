package com.android.threeday.activity.mainActivity;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Toast;

import com.android.threeday.R;
import com.android.threeday.activity.settingActivity.SettingActivity;
import com.android.threeday.fragment.BaseDayFragment;
import com.android.threeday.fragment.TodayFragment;
import com.android.threeday.fragment.TomorrowFragment;
import com.android.threeday.fragment.YesterdayFragment;
import com.android.threeday.util.Util;
import com.android.threeday.view.SlideLayer;

/**
 * Created by user on 2014/10/29.
 */
public class MainActivity extends FragmentActivity implements FragmentTaskLongClickListener, TaskOperateListener
    , FragmentStateListener {
    private boolean mBackPress;
    private boolean mFirstCreate;
    private boolean mNeedToUpdateDataAtNewDay;
    private boolean mChangeBackground;
    private boolean mInitBackground;
    private boolean mRemoveFirstLayer;
    private boolean mCheckWeatherOnRestart = true;
    private boolean mNotIntBackground;//this is used at NwDayCheck.
    private int mCurrentPageIndex = -1;

    private BaseDayFragment mTaskLongClickFragment;
    private BaseDayFragment[] mFragments;
    private MainActivityManager mMainActivityManager;
    private Handler mHandler  = new Handler();

    private FragmentPagerAdapter mFragmentPagerAdapter = new FragmentPagerAdapter(getSupportFragmentManager()) {

        @Override
        public Fragment getItem(int position) {
            return mFragments[position];
        }

        @Override
        public int getCount() {
            return MainActivityManager.DAY_NUM;
        }
    };
    private ViewPager.OnPageChangeListener mOnPageChangeListener = new ViewPager.OnPageChangeListener() {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            if(positionOffset == 0 && positionOffset == 0){
                if(mChangeBackground){
                    mChangeBackground = false;
                }else if(position != mCurrentPageIndex){
                        /*check this because user hasn't up the finger when the page reach, and
                         the onPageSelected hasn't been called, so we set mChangeBackground true
                         and onPageSelected will set it false later*/
                    mChangeBackground = true;
                }else{
                    return;
                }
                if(mInitBackground){
                    mInitBackground = false;

                    mMainActivityManager.onPageSelected(position);
                    mMainActivityManager.setDayEvaluation(getCurrentPageDayEvaluation(position));
                    onFragmentSelected(position);
                    mCurrentPageIndex = position;
                    mChangeBackground = !mChangeBackground;

                    if(mNotIntBackground){
                        mNotIntBackground = false;
                    }else{
                        mMainActivityManager.initBackground(position);
                    }
                }else{
                    mMainActivityManager.changeBackground(position);
                }
            }
        }

        @Override
        public void onPageSelected(int position) {
            if(mNeedToUpdateDataAtNewDay){
                //we want user to see the change from old day to new day, so here don't need to update the view
                if(mCurrentPageIndex != position){
                    mChangeBackground = !mChangeBackground;
                }
                mNeedToUpdateDataAtNewDay = false;
                mCurrentPageIndex = position;
                mMainActivityManager.updateDataAtNewDay();
                for(BaseDayFragment baseDayFragment : mFragments){
                    baseDayFragment.pause();
                    baseDayFragment.reloadData();
                }
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        mFragments[mCurrentPageIndex].resume();
                        mMainActivityManager.onPageSelected(mCurrentPageIndex);
                        mMainActivityManager.setDayEvaluation(getCurrentPageDayEvaluation(mCurrentPageIndex));
                    }
                }, mMainActivityManager.getViewAnimationDuration() + 500);

            }else{
                if(mCurrentPageIndex != position){
                    mMainActivityManager.onPageSelected(position);
                    mMainActivityManager.setDayEvaluation(getCurrentPageDayEvaluation(position));
                    onFragmentSelected(position);
                    mCurrentPageIndex = position;
                    mChangeBackground = !mChangeBackground;
                }
            }
        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    };
    private SlideLayer.OnLayerSlideListener mOnLayerSlideListener = new SlideLayer.OnLayerSlideListener() {
        @Override
        public void onLayerSlide() {
            mMainActivityManager.initBackground(mCurrentPageIndex);
            checkWeatherDelay();
            mNeedToUpdateDataAtNewDay = true;
            mMainActivityManager.getViewPager().setCurrentItem(MainActivityManager.YESTERDAY_INDEX, true);
            mMainActivityManager.setNewDayChecked();
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        animLogo();
    }

    private void animLogo( ){
        final View logoView = findViewById(R.id.logoView);
        logoView.setScaleX(0f);
        logoView.setScaleY(0f);
        logoView.setAlpha(0f);
        ObjectAnimator scaleXAnimator = ObjectAnimator.ofFloat(logoView, "scaleX", 0f, 1f);
        ObjectAnimator scaleYAnimator = ObjectAnimator.ofFloat(logoView, "scaleY", 0f, 1f);
        ObjectAnimator alphaAnimator = ObjectAnimator.ofFloat(logoView, "alpha", 0f, 1f);
        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.playTogether(scaleXAnimator, scaleYAnimator, alphaAnimator);
        animatorSet.setDuration(500);
        animatorSet.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationStart(Animator animation) {
                logoView.setVisibility(View.VISIBLE);
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                initData();
            }
        });
        animatorSet.setStartDelay(500);
        animatorSet.start();
    }

    private void initData( ){
        this.mFirstCreate = true;
        this.mInitBackground = true;
        this.mMainActivityManager = new MainActivityManager(this);
        initFragment( );
        setViewPagerAdapter( );
        setViewPagerListener( );
        boolean arrangeTomorrow = getIntent().getBooleanExtra(Util.ARRANGE_TOMORROW_KEY, false);
        if(arrangeTomorrow){
            this.mFirstCreate = false;
            arrangeTomorrow();
        }else{
            checkNewDay();
        }
        this.mMainActivityManager.resendAlarm();

        findViewById(R.id.firstLayer).setVisibility(View.GONE);
        this.mRemoveFirstLayer = true;

        if(this.mMainActivityManager.getNewDayPass() < 1){
            checkWeatherDelay();
        }

    }

    private void checkWeatherDelay( ){
        this.mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                mMainActivityManager.checkWeather();
            }
        }, 2000);
    }

    private void arrangeTomorrow( ){
        this.mMainActivityManager.getViewPager().setCurrentItem(MainActivityManager.TOMORROW_INDEX, false);
            /*avoid the situation that the user didn't open the app util arrange tomorrow from EveningCheck,
            because the new day hasn't been check so we should update data first*/
        this.mMainActivityManager.checkNewDayPass();
        if(this.mMainActivityManager.getNewDayPass() > 0){
            this.mMainActivityManager.updateDataAtNewDay();
            this.mMainActivityManager.setNewDayChecked();
        }
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        boolean arrangeTomorrow = intent.getBooleanExtra(Util.ARRANGE_TOMORROW_KEY, false);
        if(arrangeTomorrow){
            arrangeTomorrow();
        }else{
            checkNewDay();
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        //not save
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        //not restore because we want to rebuild all view
    }

    private void checkNewDay( ){
        this.mMainActivityManager.checkNewDayPass();
        int passDay = this.mMainActivityManager.getNewDayPass();
        if(passDay > 0){
            /*because the background will slow down the slide speed we should init background after
             slide finish*/
            this.mMainActivityManager.hideDefaultBackground();
            this.mNotIntBackground = true;
            this.mMainActivityManager.initSlideLayer();
            this.mMainActivityManager.getSlideLayer().setOnLayerSlideListener(this.mOnLayerSlideListener);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onStart() {
        super.onStart();
        if(this.mRemoveFirstLayer){
            if(getIntent().getBooleanExtra(Util.ARRANGE_TOMORROW_KEY, false)){
                getIntent().putExtra(Util.ARRANGE_TOMORROW_KEY, false);
            }else{
            /*if user enter the app not to arrangeTomorrow from eveningCheck, we should checkNewDay even
            we had check in onCreate, because user may enter the app in new day and press home, then he
            enter the app after another day later*/
                checkNewDay();
            }
            if(this.mCheckWeatherOnRestart){
                checkWeatherDelay();
            }else{
                this.mCheckWeatherOnRestart = true;
            }

            if(this.mCurrentPageIndex != -1){
                this.mFragments[this.mCurrentPageIndex].resume();
                if(this.mCurrentPageIndex == MainActivityManager.TODAY_INDEX){
                    this.mMainActivityManager.checkTodayCheck(getCurrentPageDayEvaluation(this.mCurrentPageIndex));
                }
            }
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        if(this.mCurrentPageIndex != -1){
            this.mFragments[this.mMainActivityManager.getViewPager().getCurrentItem()].pause();
        }
    }

    private void initFragment( ){
        this.mFragments = new BaseDayFragment[MainActivityManager.DAY_NUM];
        this.mFragments[MainActivityManager.YESTERDAY_INDEX] = new YesterdayFragment();
        this.mFragments[MainActivityManager.TODAY_INDEX] = new TodayFragment();
        this.mFragments[MainActivityManager.TOMORROW_INDEX] = new TomorrowFragment();
    }

    private void onFragmentSelected(int position){
        this.mFragments[position].onPageSelected();
        if(this.mCurrentPageIndex != -1){
            this.mFragments[this.mCurrentPageIndex].onPagePass();
        }
    }

    private void setViewPagerAdapter( ){
        this.mMainActivityManager.getViewPager().setAdapter(this.mFragmentPagerAdapter);
    }

    private void setViewPagerListener( ){
        this.mMainActivityManager.getViewPager().setOnPageChangeListener(this.mOnPageChangeListener);
    }

    private int getCurrentPageDayEvaluation(int position){
        int evaluation = Util.EVALUATION_DEFAULT;
        if(this.mFragments[position].isAttach()){
            evaluation = this.mFragments[position].getDayEvaluation();
        }
        return evaluation;
    }

    @Override
    public void onTaskUndoneLongClick(BaseDayFragment baseDayFragment, boolean taskShouldRemain, boolean taskCanDone) {
        this.mTaskLongClickFragment = baseDayFragment;
        this.mMainActivityManager.showUndoneTaskMenu(taskShouldRemain, taskCanDone);
    }

    @Override
    public void onTaskDoneLongClick(BaseDayFragment baseDayFragment) {
        this.mTaskLongClickFragment = baseDayFragment;
        this.mMainActivityManager.showDoneTaskMenu();
    }

    @Override
    public void deleteUndoneTask(View view) {
        if(this.mTaskLongClickFragment != null){
            this.mTaskLongClickFragment.deleteUndoneTask(view);
            this.mMainActivityManager.hideUndoneTaskMenu();
        }
    }

    @Override
    public void doneTask(View view) {
        if(this.mTaskLongClickFragment != null){
            this.mTaskLongClickFragment.doneTask(view);
            this.mMainActivityManager.hideUndoneTaskMenu();
        }
    }

    @Override
    public void setUndoneTaskRemain(View view) {
        if(this.mTaskLongClickFragment != null){
            this.mTaskLongClickFragment.setUndoneTaskRemain(view);
            this.mMainActivityManager.hideUndoneTaskMenu();
        }
    }

    @Override
    public void cancelUndoneTaskRemain(View view) {
        if(this.mTaskLongClickFragment != null){
            this.mTaskLongClickFragment.cancelUndoneTaskRemain(view);
            this.mMainActivityManager.hideUndoneTaskMenu();
        }
    }

    @Override
    public void changeUndoneTaskRemainTime(View view) {
        if(this.mTaskLongClickFragment != null){
            this.mTaskLongClickFragment.changeUndoneTaskRemainTime(view);
            this.mMainActivityManager.hideUndoneTaskMenu();
        }
    }

    @Override
    public void deleteDoneTask(View view) {
        if(this.mTaskLongClickFragment != null){
            this.mTaskLongClickFragment.deleteDoneTask(view);
            this.mMainActivityManager.hideDoneTaskMenu();
        }
    }

    @Override
    public void undoneTask(View view) {
        if(this.mTaskLongClickFragment != null){
            this.mTaskLongClickFragment.undoneTask(view);
            this.mMainActivityManager.hideDoneTaskMenu();
        }
    }

    @Override
    public void checkTasks(View view){
        this.mFragments[this.mCurrentPageIndex].checkTasks(view);
    }

    @Override
    public void addTasks(View view) {
        this.mFragments[this.mCurrentPageIndex].addTasks(view);
    }

    public void setting(View view){
        startSettingActivity();
    }

    private void startSettingActivity( ){
        Intent intent = new Intent(this, SettingActivity.class);
        startActivity(intent);
        overridePendingTransition(R.anim.activity_down_in, android.R.anim.fade_out);
    }

    @Override
    public void onBackPressed() {
        if(this.mMainActivityManager != null && this.mMainActivityManager.isMenuVisible()){
            this.mMainActivityManager.hideMenu();
        }else{
            backPressedToExit( );
        }
    }

    private void backPressedToExit( ){
        if(this.mBackPress){
            finish();
        }else{
            this.mBackPress = true;
            Toast.makeText(this, R.string.back_press_again_exit, Toast.LENGTH_SHORT).show();
            new Handler( ).postDelayed(new Runnable() {
                @Override
                public void run() {
                    mBackPress = false;
                }
            }, Util.BACK_PRESS_DELAY_TIME);
        }

    }
    @Override
    public void onFragmentAttach(Fragment fragment) {
        if(fragment instanceof TodayFragment){
            if(this.mFirstCreate){
                this.mMainActivityManager.getViewPager().setCurrentItem(MainActivityManager.TODAY_INDEX, false);
                this.mFirstCreate = false;
            }
        }
    }

    @Override
    public void onFragmentViewCreate(Fragment fragment) {

    }

    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        if(keyCode == KeyEvent.KEYCODE_MENU){
            startSettingActivity();
            return true;
        }
        return super.onKeyUp(keyCode, event);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(this.mMainActivityManager != null){
            this.mMainActivityManager.onDestroy();
        }
        System.exit(0);
    }

}
