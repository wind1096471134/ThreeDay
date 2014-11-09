package com.android.threeday.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

import com.android.threeday.R;
import com.android.threeday.fragment.TodayFragment;
import com.android.threeday.fragment.TomorrowFragment;
import com.android.threeday.fragment.YesterdayFragment;
import com.android.threeday.util.Util;

/**
 * Created by user on 2014/10/29.
 */
public class MainActivity extends FragmentActivity implements FragmentTaskLongClickListener{
    private YesterdayFragment mYesterdayFragment;
    private TodayFragment mTodayFragment;
    private TomorrowFragment mTomorrowFragment;

    private View mTaskUndoneMenu;
    private View mTaskDoneMenu;
    private ViewPager mViewPager;
    private FragmentPagerAdapter mFragmentPagerAdapter = new FragmentPagerAdapter(getSupportFragmentManager()) {

        @Override
        public Fragment getItem(int position) {
            Fragment fragment = null;
            switch (position){
                case 0:
                    fragment = mYesterdayFragment;
                    break;
                case 1:
                    fragment = mTodayFragment;
                    break;
                case 2:
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initFragment( );
        initViewPager( );
        initView( );
    }

    private void initFragment( ){
        this.mYesterdayFragment = new YesterdayFragment();
        this.mTodayFragment = new TodayFragment();
        this.mTomorrowFragment = new TomorrowFragment();
    }

    private void initViewPager( ){
        this.mViewPager = (ViewPager) findViewById(R.id.fragmentViewPager);
        this.mViewPager.setAdapter(this.mFragmentPagerAdapter);
    }

    private void initView( ){
        mTaskUndoneMenu = findViewById(R.id.taskUndoneLongClickMenu);
    }

    @Override
    public void onTaskUndoneLongClick() {
        this.mTaskUndoneMenu.setVisibility(View.VISIBLE);
    }

    @Override
    public void onTaskDoneLongClick() {

    }
}
