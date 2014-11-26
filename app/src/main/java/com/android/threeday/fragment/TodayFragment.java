package com.android.threeday.fragment;

import android.app.Activity;
import android.content.Context;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.TextView;

import com.android.threeday.R;
import com.android.threeday.activity.mainActivity.MainActivityManager;
import com.android.threeday.fragment.GridAdapter.TaskFinishGridAdapter;
import com.android.threeday.fragment.GridAdapter.TaskUnFinishGridAdapter;
import com.android.threeday.model.BaseDayModel;
import com.android.threeday.model.TodayModel;
import com.android.threeday.util.Util;
import com.android.threeday.view.PageSweepLayout;

/**
 * Created by user on 2014/10/29.
 */
public class TodayFragment extends BaseDayFragment {
    private PageSweepLayout mPageSweepLayout;
    private GridView mFrontTaskUndoneGridView;
    private GridView mBackTaskDoneGridView;
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
    private View.OnClickListener mAddUndoneTaskClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            startAddTaskActivity(getDayType(), false);
        }
    };
    private View.OnClickListener mAddDoneTaskClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            startAddTaskActivity(getDayType(), true);
        }
    };

    public TodayFragment( ){
        super();
    }

    @Override
    protected void initView(Context context) {
        this.mPageSweepLayout = new PageSweepLayout(context);
        this.mMainLayout = this.mPageSweepLayout;

        View frontPageView = ((Activity) context).getLayoutInflater().inflate(R.layout.page_main, null);
        this.mFrontTaskUndoneGridView = (GridView) frontPageView.findViewById(R.id.gridView);
        frontPageView.findViewById(R.id.addButton).setOnClickListener(this.mAddUndoneTaskClickListener);
        ((TextView)frontPageView.findViewById(R.id.taskStateTextView)).setText(R.string.task_state_undone);
        frontPageView.findViewById(R.id.pageContainer).setBackgroundResource(R.drawable.page_background_gray);

        View backPageView = ((Activity) context).getLayoutInflater().inflate(R.layout.page_main, null);
        this.mBackTaskDoneGridView = (GridView) backPageView.findViewById(R.id.gridView);
        backPageView.findViewById(R.id.addButton).setOnClickListener(this.mAddDoneTaskClickListener);
        ((TextView)backPageView.findViewById(R.id.taskStateTextView)).setText(R.string.task_state_done);
        backPageView.findViewById(R.id.pageContainer).setBackgroundResource(R.drawable.page_background_blue);

        this.mPageSweepLayout.setPageView(frontPageView, backPageView);
        DisplayMetrics metrics = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(metrics);
        this.mPageSweepLayout.setDensity(metrics.density);
        this.mPageSweepLayout.setPageSweepListener(this.mPageSweepListener);

        this.mFrontTaskUndoneGridView.setOnItemLongClickListener(this.mFontTaskUndoneGridViewLongClickListener);
        this.mBackTaskDoneGridView.setOnItemLongClickListener(this.mBackTaskDoneGridViewLongClickListener);
    }

    @Override
    protected void setAdapter() {
        int itemHeight = getActivity().getResources().getDimensionPixelSize(R.dimen.grid_item_height);

        this.mTaskDoneGridAdapter = new TaskFinishGridAdapter(getActivity(), this.mModel.getDoneTasks());
        this.mTaskDoneGridAdapter.setItemPressBackgroundResource(R.drawable.content_change_view_press);
        this.mTaskDoneGridAdapter.setGridItemHeight(itemHeight);
        this.mTaskDoneGridAdapter.setLooper(MainActivityManager.getHandlerThread().getLooper());
        if(this.mBackTaskDoneGridView != null){
            this.mBackTaskDoneGridView.setAdapter(this.mTaskDoneGridAdapter);
        }

        this.mTaskUndoneGridAdapter = new TaskUnFinishGridAdapter(getActivity(), this.mModel.getUndoneTasks());
        this.mTaskUndoneGridAdapter.setItemPressBackgroundResource(R.drawable.content_change_view_press);
        this.mTaskUndoneGridAdapter.setGridItemHeight(itemHeight);
        this.mTaskUndoneGridAdapter.setLooper(MainActivityManager.getHandlerThread().getLooper());
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
        return this.mPageSweepLayout.getCurrentPage() == PageSweepLayout.PAGE_SECOND;
    }

    @Override
    protected boolean isCurrentUndonePage() {
        return this.mPageSweepLayout.getCurrentPage() == PageSweepLayout.PAGE_FIRST;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        this.mPageSweepLayout.onDestroy();
    }

    @Override
    public void initMainViewHeightIfNeeded(int width, int height) {
        super.initMainViewHeightIfNeeded(width, height);
        this.mPageSweepLayout.initViewSize(width, height);
    }
}
