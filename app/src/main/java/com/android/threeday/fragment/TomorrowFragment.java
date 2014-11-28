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
import com.android.threeday.fragment.GridAdapter.TaskUnFinishGridAdapter;
import com.android.threeday.model.threeDay.BaseDayModel;
import com.android.threeday.model.threeDay.TomorrowModel;
import com.android.threeday.util.Util;
import com.android.threeday.view.PageSweepLayout;

/**
 * Created by user on 2014/10/29.
 */
public class TomorrowFragment extends BaseDayFragment {
    private PageSweepLayout mPageSweepLayout;
    private GridView mFontTaskUndoneGridView;

    private View.OnClickListener mAddUndoneTaskClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            startAddTaskActivity(getDayType(), false);
        }
    };
    private AdapterView.OnItemLongClickListener mFontTaskUndoneGridViewLongClickListener = new AdapterView.OnItemLongClickListener() {
        @Override
        public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
            if(mFragmentTaskLongClickListener != null){
                mTaskLongClickPosition = position;
                boolean toRemain = mModel.getUndoneTasks().get(position).getRemain();
                mFragmentTaskLongClickListener.onTaskUndoneLongClick(TomorrowFragment.this, toRemain, false);
                return true;
            }
            return false;
        }
    };
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

    @Override
    protected void initView(Context context) {
        this.mPageSweepLayout = new PageSweepLayout(context);
        this.mMainLayout = this.mPageSweepLayout;

        View frontPageView = ((Activity) context).getLayoutInflater().inflate(R.layout.page_main, null);
        this.mFontTaskUndoneGridView = (GridView) frontPageView.findViewById(R.id.gridView);
        this.mFontTaskUndoneGridView.setOnItemLongClickListener(this.mFontTaskUndoneGridViewLongClickListener);
        frontPageView.findViewById(R.id.addButton).setOnClickListener(this.mAddUndoneTaskClickListener);
        ((TextView)frontPageView.findViewById(R.id.taskStateTextView)).setText(R.string.task_state_undone);
        frontPageView.findViewById(R.id.pageContainer).setBackgroundResource(R.drawable.page_background_gray);

        View backPageView = ((Activity) context).getLayoutInflater().inflate(R.layout.page_back_tomorrow, null);
        backPageView.findViewById(R.id.pageContainer).setBackgroundResource(R.drawable.page_background_blue);
        this.mPageSweepLayout.setPageView(frontPageView, backPageView);
        DisplayMetrics metrics = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(metrics);
        this.mPageSweepLayout.setDensity(metrics.density);
        this.mPageSweepLayout.setPageSweepListener(this.mPageSweepListener);
    }

    @Override
    protected void initAdapter(Context context) {
        int itemHeight = getActivity().getResources().getDimensionPixelSize(R.dimen.grid_item_height);

        this.mTaskUndoneGridAdapter = new TaskUnFinishGridAdapter(getActivity(), this.mModel.getUndoneTasks());
        this.mTaskUndoneGridAdapter.setItemPressBackgroundResource(R.drawable.content_change_view_press);
        this.mTaskUndoneGridAdapter.setGridItemHeight(itemHeight);
        this.mTaskUndoneGridAdapter.setLooper(MainActivityManager.getHandlerThread().getLooper());
    }

    @Override
    protected void setAdapter() {
                if(this.mFontTaskUndoneGridView != null){
            this.mFontTaskUndoneGridView.setAdapter(this.mTaskUndoneGridAdapter);
        }
    }

    @Override
    protected BaseDayModel getModel(Context context) {
        return new TomorrowModel(context);
    }

    @Override
    protected int getDayType() {
        return Util.TYPE_TOMORROW;
    }

    @Override
    protected boolean isCurrentDonePage() {
        return false;
    }

    @Override
    protected boolean isCurrentUndonePage() {
        return this.mPageSweepLayout.getCurrentPage() == PageSweepLayout.PAGE_FIRST;
    }

    @Override
    public void initMainViewHeightIfNeeded(int width, int height) {
        super.initMainViewHeightIfNeeded(width, height);
        this.mPageSweepLayout.initViewSize(width, height);
    }
}
