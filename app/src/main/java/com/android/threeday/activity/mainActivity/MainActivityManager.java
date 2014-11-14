package com.android.threeday.activity.mainActivity;

import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.threeday.R;
import com.android.threeday.fragment.dialogFragment.TaskDoneMenuFragment;
import com.android.threeday.fragment.dialogFragment.TaskUndoneMenuFragment;
import com.android.threeday.util.Util;

/**
 * Created by user on 2014/11/11.
 */
class MainActivityManager {
    static final int YESTERDAY_INDEX = 0;
    static final int TODAY_INDEX = 1;
    static final int TOMORROW_INDEX = 2;

    private FragmentActivity mActivity;
    private TaskUndoneMenuFragment mTaskUndoneMenuFragment;
    private TaskDoneMenuFragment mTaskDoneMenuFragment;
    private ViewPager mViewPager;
    private TextView mTitleTextView;
    private TextView mWordsTextView;
    private ImageView mDayEvaluationImageView;

    MainActivityManager(FragmentActivity activity){
        this.mActivity = activity;
        initView( );
    }

    private void initView( ){
        this.mViewPager = (ViewPager) mActivity.findViewById(R.id.fragmentViewPager);

        this.mTaskDoneMenuFragment = new TaskDoneMenuFragment();
        this.mTaskUndoneMenuFragment = new TaskUndoneMenuFragment();

        this.mTitleTextView = (TextView) mActivity.findViewById(R.id.titleTextView);
        this.mWordsTextView = (TextView) mActivity.findViewById(R.id.wordsTextView);
        this.mDayEvaluationImageView = (ImageView) mActivity.findViewById(R.id.dayEvaluationImageView);
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
    }

    void onPageSelected(int position){
        switch (position){
            case YESTERDAY_INDEX:
                this.mTitleTextView.setText(R.string.title_yesterday);
                this.mWordsTextView.setText(R.string.words_yesterday);
                //TODO
                break;
            case TODAY_INDEX:
                this.mTitleTextView.setText(R.string.title_today);
                this.mWordsTextView.setText(R.string.words_today);
                //TODO
                break;
            case TOMORROW_INDEX:
                this.mTitleTextView.setText(R.string.title_tomorrow);
                this.mWordsTextView.setText(R.string.words_tomorrow);
                //TODO
                break;
        }
    }
}
