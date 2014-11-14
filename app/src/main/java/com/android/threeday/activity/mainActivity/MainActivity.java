package com.android.threeday.activity.mainActivity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;

import com.android.threeday.R;
import com.android.threeday.fragment.BaseDayFragment;
import com.android.threeday.fragment.TodayFragment;
import com.android.threeday.fragment.TomorrowFragment;
import com.android.threeday.fragment.YesterdayFragment;
import com.android.threeday.util.Util;

/**
 * Created by user on 2014/10/29.
 */
public class MainActivity extends FragmentActivity implements FragmentTaskLongClickListener, TaskOperateListener
    , FragmentAttachListener{
    private boolean mFirstCreate;
    private YesterdayFragment mYesterdayFragment;
    private TodayFragment mTodayFragment;
    private TomorrowFragment mTomorrowFragment;
    private BaseDayFragment mTaskLongClickFragment;

    private MainActivityManager mMainActivityManager;

    private FragmentPagerAdapter mFragmentPagerAdapter = new FragmentPagerAdapter(getSupportFragmentManager()) {

        @Override
        public Fragment getItem(int position) {
            Fragment fragment = null;
            switch (position){
                case MainActivityManager.YESTERDAY_INDEX:
                    fragment = mYesterdayFragment;
                    break;
                case MainActivityManager.TODAY_INDEX:
                    fragment = mTodayFragment;
                    break;
                case MainActivityManager.TOMORROW_INDEX:
                    fragment = mTomorrowFragment;
                    break;
            }
            return fragment;
        }

        @Override
        public int getCount() {
            return Util.DAY_NUM;
        }
    };
    private ViewPager.OnPageChangeListener mOnPageChangeListener = new ViewPager.OnPageChangeListener() {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int position) {
            mMainActivityManager.onPageSelected(position);
            mMainActivityManager.setDayEvaluation(getCurrentPageDayEvaluation(position));
        }

        @Override
        public void onPageScrollStateChanged(int state) {

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
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    private void initFragment( ){
        this.mYesterdayFragment = new YesterdayFragment();
        this.mTodayFragment = new TodayFragment();
        this.mTomorrowFragment = new TomorrowFragment();
    }

    private void setViewPagerAdapter( ){
        this.mMainActivityManager.getViewPager().setAdapter(this.mFragmentPagerAdapter);
    }

    private void setViewPagerListener( ){
        this.mMainActivityManager.getViewPager().setOnPageChangeListener(this.mOnPageChangeListener);
    }

    private int getCurrentPageDayEvaluation(int position){
        int evaluation = Util.EVALUATION_DEFAULT;
        switch (position){
            case MainActivityManager.YESTERDAY_INDEX:
                if(this.mYesterdayFragment.isAttach()){
                    evaluation = this.mYesterdayFragment.getDayEvaluation();
                }
                break;
            case MainActivityManager.TODAY_INDEX:
                if(this.mTodayFragment.isAttach()){
                    evaluation = this.mTodayFragment.getDayEvaluation();
                }
                break;
            case MainActivityManager.TOMORROW_INDEX:
                if(this.mTomorrowFragment.isAttach()){
                    evaluation = this.mTomorrowFragment.getDayEvaluation();
                }
                break;
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
}
