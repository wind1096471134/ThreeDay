package com.android.threeday.fragment;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.widget.GridView;
import android.widget.TextView;

import com.android.threeday.R;
import com.android.threeday.activity.mainActivity.MainActivityManager;
import com.android.threeday.fragment.GridAdapter.TaskFinishGridAdapter;
import com.android.threeday.model.BaseDayModel;
import com.android.threeday.model.YesterdayModel;
import com.android.threeday.util.Util;
import com.android.threeday.view.RotePageLayout;

/**
 * Created by user on 2014/10/29.
 */
public class YesterdayFragment extends BaseDayFragment {
    private RotePageLayout mRotePageLayout;
    private GridView mFrontTaskDoneGridView;
    private GridView mBackTaskUndoneGridView;

    public YesterdayFragment( ){
        super();
    }

    @Override
    protected void initView(Context context) {
        this.mMainLayout = new RotePageLayout(context);
        this.mRotePageLayout = (RotePageLayout) this.mMainLayout;

        View frontView = ((Activity) context).getLayoutInflater().inflate(R.layout.page_main, null);
        this.mFrontTaskDoneGridView = (GridView) frontView.findViewById(R.id.gridView);
        frontView.findViewById(R.id.addButton).setVisibility(View.GONE);
        ((TextView)frontView.findViewById(R.id.taskStateTextView)).setText(R.string.task_state_done);

        View backView = ((Activity) context).getLayoutInflater().inflate(R.layout.page_main, null);
        this.mBackTaskUndoneGridView = (GridView) backView.findViewById(R.id.gridView);
        this.mBackTaskUndoneGridView.setClickable(false);
        backView.findViewById(R.id.addButton).setVisibility(View.GONE);
        ((TextView)backView.findViewById(R.id.taskStateTextView)).setText(R.string.task_state_undone);

        this.mRotePageLayout.setPageView(frontView, backView);
    }

    @Override
    protected void setAdapter() {
        int itemHeight = getActivity().getResources().getDimensionPixelSize(R.dimen.grid_item_height);

        this.mTaskDoneGridAdapter = new TaskFinishGridAdapter(getActivity(), this.mModel.getDoneTasks());
        this.mTaskDoneGridAdapter.setGridItemHeight(itemHeight);
        this.mTaskDoneGridAdapter.setLooper(MainActivityManager.getHandlerThread().getLooper());
        if(this.mFrontTaskDoneGridView != null){
            this.mFrontTaskDoneGridView.setAdapter(this.mTaskDoneGridAdapter);
        }

        this.mTaskUndoneGridAdapter = new TaskFinishGridAdapter(getActivity(), this.mModel.getUndoneTasks());
        this.mTaskUndoneGridAdapter.setGridItemHeight(itemHeight);
        this.mTaskUndoneGridAdapter.setLooper(MainActivityManager.getHandlerThread().getLooper());
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
        return this.mRotePageLayout.getPageState() == RotePageLayout.PAGE_STATE_FRONT;
    }

    @Override
    protected boolean isCurrentUndonePage() {
        return this.mRotePageLayout.getPageState() == RotePageLayout.PAGE_STATE_BACK;
    }

}
