package com.android.threeday.fragment;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.TextView;

import com.android.threeday.R;
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
        this.mTaskDoneGridAdapter = new TaskGridAdapter(getActivity(), this.mModel.getDoneTasks());
        this.mTaskUndoneGridAdapter = new TaskGridAdapter(getActivity(), this.mModel.getUndoneTasks());

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

}
