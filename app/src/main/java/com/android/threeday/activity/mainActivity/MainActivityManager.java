package com.android.threeday.activity.mainActivity;

import android.app.Activity;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.threeday.R;

/**
 * Created by user on 2014/11/11.
 */
class MainActivityManager {
    static final int YESTERDAY_INDEX = 0;
    static final int TODAY_INDEX = 1;
    static final int TOMORROW_INDEX = 2;

    private Activity mActivity;
    private View mTaskUndoneMenu;
    private View mTaskDoneMenu;
    private ViewPager mViewPager;
    private TextView mTitleTextView;
    private ImageView mDayEvaluationImageView;

    MainActivityManager(Activity activity){
        this.mActivity = activity;
        initView( );
    }

    private void initView( ){
        this.mViewPager = (ViewPager) mActivity.findViewById(R.id.fragmentViewPager);

        this.mTaskDoneMenu = mActivity.findViewById(R.id.taskDoneLongClickMenu);
        this.mTaskDoneMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mTaskDoneMenu.getVisibility() == View.VISIBLE){
                    mTaskDoneMenu.setVisibility(View.GONE);
                }
            }
        });
        this.mTaskDoneMenu.findViewById(R.id.menuContainer).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //empty
            }
        });

        this.mTaskUndoneMenu = mActivity.findViewById(R.id.taskUndoneLongClickMenu);
        this.mTaskUndoneMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mTaskUndoneMenu.getVisibility() == View.VISIBLE){
                    mTaskUndoneMenu.setVisibility(View.GONE);
                }
            }
        });
        this.mTaskUndoneMenu.findViewById(R.id.menuContainer).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //empty
            }
        });

        this.mTitleTextView = (TextView) mActivity.findViewById(R.id.titleTextView);
        this.mDayEvaluationImageView = (ImageView) mActivity.findViewById(R.id.dayEvaluationImageView);
    }

    ViewPager getViewPager( ){
        return this.mViewPager;
    }

    void showUndoneTaskMenu(boolean taskShouldRemain){
        this.mTaskUndoneMenu.setVisibility(View.VISIBLE);
        if(taskShouldRemain){
            this.mTaskUndoneMenu.findViewById(R.id.changeRemainTimeButton).setVisibility(View.VISIBLE);
            this.mTaskUndoneMenu.findViewById(R.id.cancelRemainButton).setVisibility(View.VISIBLE);
            this.mTaskUndoneMenu.findViewById(R.id.setRemianButton).setVisibility(View.GONE);
        }else{
            this.mTaskUndoneMenu.findViewById(R.id.changeRemainTimeButton).setVisibility(View.GONE);
            this.mTaskUndoneMenu.findViewById(R.id.cancelRemainButton).setVisibility(View.GONE);
            this.mTaskUndoneMenu.findViewById(R.id.setRemianButton).setVisibility(View.VISIBLE);
        }
    }

    void hideUndoneTaskMenu( ){
        this.mTaskUndoneMenu.setVisibility(View.GONE);
    }

    void showDoneTaskMenu( ){
        this.mTaskDoneMenu.setVisibility(View.VISIBLE);
    }

    void hideDoneTaskMenu( ){
        this.mTaskDoneMenu.setVisibility(View.GONE);
    }

    boolean isMenuVisible( ){
        return this.mTaskDoneMenu.getVisibility() == View.VISIBLE ||
                this.mTaskUndoneMenu.getVisibility() == View.VISIBLE;
    }

    void hideMenu( ){
        hideUndoneTaskMenu();
        hideDoneTaskMenu();
    }

    void onPageSelected(int position){
        switch (position){
            case YESTERDAY_INDEX:
                this.mTitleTextView.setText(R.string.title_yesterday);
                //TODO
                break;
            case TODAY_INDEX:
                this.mTitleTextView.setText(R.string.title_today);
                //TODO
                break;
            case TOMORROW_INDEX:
                this.mTitleTextView.setText(R.string.title_tomorrow);
                //TODO
                break;
        }
    }
}
