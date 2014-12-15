package com.android.threeday.activity.mainActivity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;

import com.android.threeday.R;
import com.android.threeday.activity.checkTaskActivity.CheckTaskActivity;
import com.android.threeday.activity.settingActivity.SettingActivity;
import com.android.threeday.fragment.BaseDayFragment;
import com.android.threeday.fragment.TodayFragment;
import com.android.threeday.fragment.TomorrowFragment;
import com.android.threeday.fragment.YesterdayFragment;
import com.android.threeday.model.updateData.UpdateDataModel;
import com.android.threeday.util.Util;
import com.android.threeday.view.SlideLayer;

/**
 * Created by user on 2014/10/29.
 */
public class MainActivity extends FragmentActivity implements FragmentTaskLongClickListener, TaskOperateListener
    , FragmentStateListener {
    private boolean mFirstCreate;
    private boolean mNeedToUpdateDataAtNewDay;
    private int mCurrentPageIndex = -1;

    private BaseDayFragment mTaskLongClickFragment;
    private BaseDayFragment[] mFragments;
    private MainActivityManager mMainActivityManager;

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

        }

        @Override
        public void onPageSelected(int position) {
            if(mNeedToUpdateDataAtNewDay){
            //we want user to see the change from old day to new day, so here don't need to update the view
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
            mNeedToUpdateDataAtNewDay = true;
            mMainActivityManager.getViewPager().setCurrentItem(MainActivityManager.YESTERDAY_INDEX, true);
            mMainActivityManager.setNewDayChecked();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        this.mFirstCreate = true;
        this.mMainActivityManager = new MainActivityManager(this);
        initFragment();
        setViewPagerAdapter();
        setViewPagerListener( );
        checkNewDay( );
        checkFirstUsing( );
        //this.mMainActivityManager.testSetNewDayAlarm();

        Log.e("wind", "main create " + android.os.Process.myPid());
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        checkNewDay();
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
        boolean checked = this.mMainActivityManager.isNewDayChecked();
        if(!checked){
            this.mMainActivityManager.initSlideLayer();
            this.mMainActivityManager.getSlideLayer().setOnLayerSlideListener(this.mOnLayerSlideListener);
        }
    }

    private void checkFirstUsing( ){
        boolean isFirstUsing = this.mMainActivityManager.isFirstUsing( );
        if(isFirstUsing){
            this.mMainActivityManager.setFirstUsingFalse();
            this.mMainActivityManager.setNewDayAlarm();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(this.mCurrentPageIndex != -1){
            this.mFragments[this.mCurrentPageIndex].resume();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        this.mFragments[this.mMainActivityManager.getViewPager().getCurrentItem()].pause();
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

    public void checkTasks(View view){
        Intent intent = new Intent(this, CheckTaskActivity.class);
        startActivity(intent);
    }

    public void setting(View view){
        startSettingActivity();
    }

    private void startSettingActivity( ){
        Intent intent = new Intent(this, SettingActivity.class);
        startActivity(intent);
    }

    @Override
    public void onBackPressed() {
        if(this.mMainActivityManager.isMenuVisible()){
            this.mMainActivityManager.hideMenu();
        }else{
            super.onBackPressed();
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
        super.onDestroy();Log.e("wind", "main destroy");
        this.mMainActivityManager.onDestroy();
        System.exit(0);
    }

}
