package com.android.threeday.fragment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.text.format.Time;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.TextView;

import com.android.threeday.R;
import com.android.threeday.activity.AddTaskActivity;
import com.android.threeday.model.BaseDayModel;
import com.android.threeday.model.TaskItem;
import com.android.threeday.model.TodayModel;
import com.android.threeday.util.Util;
import com.android.threeday.view.RotePageLayout;

/**
 * Created by user on 2014/10/29.
 */
public class TodayFragment extends BaseDayFragment {
    private RotePageLayout mRotePageLayout;
    private GridView mFrontTaskUndoneGridView;
    private GridView mBackTaskDoneGridView;

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
        this.mMainLayout = new RotePageLayout(context);
        this.mRotePageLayout = (RotePageLayout) this.mMainLayout;

        View frontPageView = ((Activity) context).getLayoutInflater().inflate(R.layout.page_main, null);
        this.mFrontTaskUndoneGridView = (GridView) frontPageView.findViewById(R.id.gridView);
        frontPageView.findViewById(R.id.addButton).setOnClickListener(this.mAddUndoneTaskClickListener);
        ((TextView)frontPageView.findViewById(R.id.taskStateTextView)).setText(R.string.task_state_undone);

        View backPageView = ((Activity) context).getLayoutInflater().inflate(R.layout.page_main, null);
        this.mBackTaskDoneGridView = (GridView) backPageView.findViewById(R.id.gridView);
        backPageView.findViewById(R.id.addButton).setOnClickListener(this.mAddDoneTaskClickListener);
        ((TextView)backPageView.findViewById(R.id.taskStateTextView)).setText(R.string.task_state_done);

        this.mRotePageLayout.setPageView(frontPageView, backPageView);

        this.mFrontTaskUndoneGridView.setOnItemLongClickListener(this.mFontTaskUndoneGridViewLongClickListener);
        this.mBackTaskDoneGridView.setOnItemLongClickListener(this.mBackTaskDoneGridViewLongClickListener);
    }

    @Override
    protected void setAdapter() {
        this.mTaskDoneGridAdapter = new TaskGridAdapter(getActivity(), this.mModel.getDoneTasks());
        ((TaskGridAdapter) this.mTaskDoneGridAdapter).setItemPressBackgroundResource(R.drawable.content_change_view_press);
        this.mTaskUndoneGridAdapter = new TaskGridAdapter(getActivity(), this.mModel.getUndoneTasks());
        ((TaskGridAdapter) this.mTaskUndoneGridAdapter).setItemPressBackgroundResource(R.drawable.content_change_view_press);

        if(this.mFrontTaskUndoneGridView != null){
            this.mFrontTaskUndoneGridView.setAdapter(this.mTaskUndoneGridAdapter);
        }
        if(this.mBackTaskDoneGridView != null){
            this.mBackTaskDoneGridView.setAdapter(this.mTaskDoneGridAdapter);
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

}
