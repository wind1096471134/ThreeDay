package com.android.threeday.fragment;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.TranslateAnimation;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.TextView;

import com.android.threeday.R;
import com.android.threeday.activity.mainActivity.MainActivityManager;
import com.android.threeday.fragment.GridAdapter.TaskFinishGridAdapter;
import com.android.threeday.fragment.GridAdapter.TaskUnFinishGridAdapter;
import com.android.threeday.model.threeDay.BaseDayModel;
import com.android.threeday.model.threeDay.TodayModel;
import com.android.threeday.util.Util;
import com.android.threeday.view.PageSwitchLayout;

/**
 * Created by user on 2014/10/29.
 */
public class TodayFragment extends BaseDayFragment {
    private PageSwitchLayout mPageSwitchLayout;
    private GridView mFrontTaskUndoneGridView;
    private GridView mBackTaskDoneGridView;
    private TextView mTaskStateTextView;
    private View mSwitchController;
    private AnimationSet mTaskStateAnimation;
    private PageSwitchLayout.OnPageSwitchListener mOnPageSwitchListener = new PageSwitchLayout.OnPageSwitchListener() {
        @Override
        public void onPageSwitch(int currentPage) {
            if(isCurrentUndonePage()){
                mTaskStateTextView.setText(R.string.task_state_undone);
                mTaskUndoneGridAdapter.onResume();
                mTaskDoneGridAdapter.onPause();
            }else if(isCurrentDonePage()){
                mTaskStateTextView.setText(R.string.task_state_done);
                mTaskDoneGridAdapter.onResume();
                mTaskUndoneGridAdapter.onPause();
            }
            mTaskStateTextView.startAnimation(mTaskStateAnimation);
        }
    };
    private AdapterView.OnItemLongClickListener mFontTaskUndoneGridViewLongClickListener = new AdapterView.OnItemLongClickListener() {
        @Override
        public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
            if(mFragmentTaskLongClickListener != null){
                mTaskLongClickPosition = position;
                boolean toRemain = mModel.getUndoneTasks().get(position).getRemain();
                mFragmentTaskLongClickListener.onTaskUndoneLongClick(TodayFragment.this, toRemain, true);
                return true;
            }
            return false;
        }
    };
    private AdapterView.OnItemLongClickListener mBackTaskDoneGridViewLongClickListener = new AdapterView.OnItemLongClickListener() {
        @Override
        public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
            if(mFragmentTaskLongClickListener != null){
                mTaskLongClickPosition = position;
                mFragmentTaskLongClickListener.onTaskDoneLongClick(TodayFragment.this);
            }
            return false;
        }
    };
    private View.OnClickListener mAddTaskClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            startAddTaskActivity(getDayType(), isCurrentDonePage());
        }
    };
    private View.OnClickListener mSwitchPageClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            mPageSwitchLayout.switchPage();
        }
    };

    public TodayFragment( ){
        super();
        initData( );
    }

    private void initData( ){
        long duration = 500;
        AlphaAnimation alphaAnimation = new AlphaAnimation(0f, 1f);
        TranslateAnimation translateAnimation = new TranslateAnimation(Animation.RELATIVE_TO_SELF, 0f, Animation.RELATIVE_TO_SELF, 0f, Animation.RELATIVE_TO_SELF, -0.3f, Animation.RELATIVE_TO_SELF , 0f);
        this.mTaskStateAnimation = new AnimationSet(false);
        this.mTaskStateAnimation.addAnimation(translateAnimation);
        this.mTaskStateAnimation.addAnimation(alphaAnimation);
        this.mTaskStateAnimation.setDuration(duration);
        this.mTaskStateAnimation.setInterpolator(new DecelerateInterpolator());
    }

    @Override
    protected void initView(Context context) {

        this.mMainLayout = View.inflate(context, R.layout.fragment_switch_layout, null);
        this.mMainLayout.findViewById(R.id.pageContainer).setBackgroundResource(R.drawable.page_background_gray);
        this.mTaskStateTextView = (TextView) this.mMainLayout.findViewById(R.id.taskStateTextView);
        this.mMainLayout.findViewById(R.id.addButton).setOnClickListener(this.mAddTaskClickListener);
        this.mPageSwitchLayout = (PageSwitchLayout) this.mMainLayout.findViewById(R.id.pageSwitchLayout);
        this.mSwitchController = this.mMainLayout.findViewById(R.id.switchController);
        this.mSwitchController.setOnClickListener(this.mSwitchPageClickListener);

        View frontPageView = View.inflate(context, R.layout.task_container, null);
        this.mFrontTaskUndoneGridView = (GridView) frontPageView.findViewById(R.id.gridView);

        View backPageView = View.inflate(context, R.layout.task_container, null);
        this.mBackTaskDoneGridView = (GridView) backPageView.findViewById(R.id.gridView);

        this.mPageSwitchLayout.setPageView(frontPageView, backPageView);
        this.mPageSwitchLayout.setOnPageSwitchListener(this.mOnPageSwitchListener);

        this.mFrontTaskUndoneGridView.setOnItemLongClickListener(this.mFontTaskUndoneGridViewLongClickListener);
        this.mBackTaskDoneGridView.setOnItemLongClickListener(this.mBackTaskDoneGridViewLongClickListener);
        this.mTaskStateTextView.setText(R.string.task_state_undone);
    }

    @Override
    protected void initAdapter(Context context) {
        int itemHeight = getActivity().getResources().getDimensionPixelSize(R.dimen.grid_item_height);

        this.mTaskDoneGridAdapter = new TaskFinishGridAdapter(getActivity(), this.mModel.getDoneTasks());
        this.mTaskDoneGridAdapter.setItemPressBackgroundResource(R.drawable.content_change_view_press);
        this.mTaskDoneGridAdapter.setGridItemHeight(itemHeight);
        this.mTaskDoneGridAdapter.setLooper(MainActivityManager.getHandlerThread().getLooper());

        this.mTaskUndoneGridAdapter = new TaskUnFinishGridAdapter(getActivity(), this.mModel.getUndoneTasks());
        this.mTaskUndoneGridAdapter.setItemPressBackgroundResource(R.drawable.content_change_view_press);
        this.mTaskUndoneGridAdapter.setGridItemHeight(itemHeight);
        this.mTaskUndoneGridAdapter.setLooper(MainActivityManager.getHandlerThread().getLooper());
    }

    @Override
    protected void setAdapter() {
        if(this.mBackTaskDoneGridView != null){
            this.mBackTaskDoneGridView.setAdapter(this.mTaskDoneGridAdapter);
        }

        if(this.mFrontTaskUndoneGridView != null){
            this.mFrontTaskUndoneGridView.setAdapter(this.mTaskUndoneGridAdapter);
        }
    }

    @Override
    protected BaseDayModel getModel(Context context) {
        return new TodayModel(context);
    }

    @Override
    protected int getDayType() {
        return Util.TYPE_TODAY;
    }

    @Override
    protected boolean isCurrentDonePage() {
        return this.mPageSwitchLayout.getCurrentPage() == PageSwitchLayout.PAGE_SECOND;
    }

    @Override
    protected boolean isCurrentUndonePage() {
        return this.mPageSwitchLayout.getCurrentPage() == PageSwitchLayout.PAGE_FIRST;
    }

}
