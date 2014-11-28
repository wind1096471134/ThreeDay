package com.android.threeday.fragment;

import android.app.Activity;
import android.content.Context;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.GridView;
import android.widget.TextView;

import com.android.threeday.R;
import com.android.threeday.activity.mainActivity.MainActivityManager;
import com.android.threeday.fragment.GridAdapter.TaskFinishGridAdapter;
import com.android.threeday.model.threeDay.BaseDayModel;
import com.android.threeday.model.threeDay.YesterdayModel;
import com.android.threeday.util.Util;
import com.android.threeday.view.PageSweepLayout;

/**
 * Created by user on 2014/10/29.
 */
public class YesterdayFragment extends BaseDayFragment {
    private PageSweepLayout mPageSweepLayout;
    private GridView mFrontTaskDoneGridView;
    private GridView mBackTaskUndoneGridView;
    private PageSweepLayout.PageSweepListener mPageSweepListener = new PageSweepLayout.PageSweepListener() {
        @Override
        public void onPageSweepStart(int direction) {
            if(isCurrentUndonePage()){
                mTaskUndoneGridAdapter.onPause();
            }else if(isCurrentDonePage()){
                mTaskDoneGridAdapter.onPause();
            }
        }

        @Override
        public void onPageSelected(int pageIndex) {
            if(isCurrentUndonePage()){
                mTaskUndoneGridAdapter.onResume();
            }else if(isCurrentDonePage()){
                mTaskDoneGridAdapter.onResume();
            }
        }
    };

    public YesterdayFragment( ){
        super();
    }

    @Override
    protected void initView(Context context) {
        this.mPageSweepLayout = new PageSweepLayout(context);
        this.mMainLayout = this.mPageSweepLayout;

        View frontView = ((Activity) context).getLayoutInflater().inflate(R.layout.page_main, null);
        this.mFrontTaskDoneGridView = (GridView) frontView.findViewById(R.id.gridView);
        frontView.findViewById(R.id.addButton).setVisibility(View.GONE);
        ((TextView)frontView.findViewById(R.id.taskStateTextView)).setText(R.string.task_state_done);
        frontView.findViewById(R.id.pageContainer).setBackgroundResource(R.drawable.page_background_gray);

        View backView = ((Activity) context).getLayoutInflater().inflate(R.layout.page_main, null);
        this.mBackTaskUndoneGridView = (GridView) backView.findViewById(R.id.gridView);
        this.mBackTaskUndoneGridView.setClickable(false);
        backView.findViewById(R.id.addButton).setVisibility(View.GONE);
        ((TextView)backView.findViewById(R.id.taskStateTextView)).setText(R.string.task_state_undone);
        backView.findViewById(R.id.pageContainer).setBackgroundResource(R.drawable.page_background_blue);

        this.mPageSweepLayout.setPageView(frontView, backView);
        DisplayMetrics metrics = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(metrics);
        this.mPageSweepLayout.setDensity(metrics.density);
        this.mPageSweepLayout.setPageSweepListener(this.mPageSweepListener);
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
        return this.mPageSweepLayout.getCurrentPage() == PageSweepLayout.PAGE_FIRST;
    }

    @Override
    protected boolean isCurrentUndonePage() {
        return this.mPageSweepLayout.getCurrentPage() == PageSweepLayout.PAGE_SECOND;
    }

    @Override
    public void initMainViewHeightIfNeeded(int width, int height) {
        super.initMainViewHeightIfNeeded(width, height);
        this.mPageSweepLayout.initViewSize(width, height);
    }
}
