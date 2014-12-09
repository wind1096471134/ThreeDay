package com.android.threeday.fragment;

import android.content.Context;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.TranslateAnimation;
import android.widget.GridView;
import android.widget.TextView;

import com.android.threeday.R;
import com.android.threeday.activity.mainActivity.MainActivityManager;
import com.android.threeday.fragment.GridAdapter.TaskFinishGridAdapter;
import com.android.threeday.model.threeDay.BaseDayModel;
import com.android.threeday.model.threeDay.YesterdayModel;
import com.android.threeday.util.Util;
import com.android.threeday.view.PageSwitchLayout;

/**
 * Created by user on 2014/10/29.
 */
public class YesterdayFragment extends BaseDayFragment {
    private PageSwitchLayout mPageSwitchLayout;
    private GridView mFrontTaskDoneGridView;
    private GridView mBackTaskUndoneGridView;
    private TextView mTaskStateTextView;
    private View mSwitchController;
    private AnimationSet mTaskStateAnimation;
    private PageSwitchLayout.OnPageSwitchListener mOnPageSwitchListener = new PageSwitchLayout.OnPageSwitchListener() {
        @Override
        public void onPageSwitchStart(int currentPage) {
            if(isCurrentDonePage()){
                mTaskStateTextView.setText(R.string.task_state_undone);
            }else if(isCurrentUndonePage()){
                mTaskStateTextView.setText(R.string.task_state_done);
            }
            mTaskStateTextView.startAnimation(mTaskStateAnimation);
        }

        @Override
        public void onPageSwitchEnd(int currentPage) {
            if(isCurrentUndonePage()){
                mTaskUndoneGridAdapter.onResume();
                mTaskDoneGridAdapter.onPause();
            }else if(isCurrentDonePage()){
                mTaskDoneGridAdapter.onResume();
                mTaskUndoneGridAdapter.onPause();
            }
        }
    };
    private View.OnClickListener mSwitchControllerClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            mPageSwitchLayout.switchPage();
        }
    };

    public YesterdayFragment( ){
        super();
        initData();
    }

    private void initData( ){
        long duration = 500;
        AlphaAnimation alphaAnimation = new AlphaAnimation(0f, 1f);
        TranslateAnimation translateAnimation = new TranslateAnimation(Animation.RELATIVE_TO_SELF, 0f, Animation.RELATIVE_TO_SELF, 0f, Animation.RELATIVE_TO_SELF, -0.2f, Animation.RELATIVE_TO_SELF , 0f);
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
        this.mMainLayout.findViewById(R.id.addButton).setVisibility(View.INVISIBLE);
        this.mTaskStateTextView = (TextView) this.mMainLayout.findViewById(R.id.taskStateTextView);
        this.mPageSwitchLayout = (PageSwitchLayout) this.mMainLayout.findViewById(R.id.pageSwitchLayout);

        View frontView = View.inflate(context, R.layout.task_container, null);
        this.mFrontTaskDoneGridView = (GridView) frontView.findViewById(R.id.gridView);

        View backView = View.inflate(context, R.layout.task_container, null);
        this.mBackTaskUndoneGridView = (GridView) backView.findViewById(R.id.gridView);
        this.mBackTaskUndoneGridView.setClickable(false);

        this.mPageSwitchLayout.setPageView(frontView, backView);
        this.mPageSwitchLayout.setOnPageSwitchListener(this.mOnPageSwitchListener);

        this.mSwitchController = this.mMainLayout.findViewById(R.id.switchController);
        this.mSwitchController.setOnClickListener(this.mSwitchControllerClickListener);
        this.mTaskStateTextView.setText(R.string.task_state_done);
    }

    @Override
    protected void initAdapter(Context context) {
        int itemHeight = getActivity().getResources().getDimensionPixelSize(R.dimen.grid_item_height);

        this.mTaskDoneGridAdapter = new TaskFinishGridAdapter(getActivity(), this.mModel.getDoneTasks());
        this.mTaskDoneGridAdapter.setGridItemHeight(itemHeight);
        this.mTaskDoneGridAdapter.setLooper(MainActivityManager.getHandlerThread().getLooper());

        this.mTaskUndoneGridAdapter = new TaskFinishGridAdapter(getActivity(), this.mModel.getUndoneTasks());
        this.mTaskUndoneGridAdapter.setGridItemHeight(itemHeight);
        this.mTaskUndoneGridAdapter.setLooper(MainActivityManager.getHandlerThread().getLooper());
    }

    @Override
    protected void setAdapter() {
        if(this.mFrontTaskDoneGridView != null){
            this.mFrontTaskDoneGridView.setAdapter(this.mTaskDoneGridAdapter);
        }

        if(this.mBackTaskUndoneGridView != null){
            this.mBackTaskUndoneGridView.setAdapter(this.mTaskUndoneGridAdapter);
        }      
    }

    @Override
    protected BaseDayModel getModel(Context context) {
        return new YesterdayModel(context);
    }

    @Override
    protected int getDayType() {
        return Util.TYPE_YESTERDAY;
    }

    @Override
    protected boolean isCurrentDonePage() {
        return this.mPageSwitchLayout.getCurrentPage() == PageSwitchLayout.PAGE_FIRST;
    }

    @Override
    protected boolean isCurrentUndonePage() {
        return this.mPageSwitchLayout.getCurrentPage() == PageSwitchLayout.PAGE_SECOND;
    }

}
