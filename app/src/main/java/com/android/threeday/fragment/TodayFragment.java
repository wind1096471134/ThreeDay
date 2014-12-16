package com.android.threeday.fragment;

import android.content.Context;
import android.content.Intent;
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
import com.android.threeday.activity.checkTaskActivity.CheckTaskActivity;
import com.android.threeday.activity.mainActivity.MainActivityManager;
import com.android.threeday.fragment.GridAdapter.TaskFinishGridAdapter;
import com.android.threeday.fragment.GridAdapter.TaskUnFinishGridAdapter;
import com.android.threeday.model.threeDay.BaseDayModel;
import com.android.threeday.model.threeDay.TodayModel;
import com.android.threeday.util.Util;
import com.android.threeday.view.PageSwitchLayout;

import java.util.ArrayList;

/**
 * Created by user on 2014/10/29.
 */
public class TodayFragment extends BaseDayFragment {
    private PageSwitchLayout mPageSwitchLayout;
    private GridView mFrontTaskUndoneGridView;
    private GridView mBackTaskDoneGridView;
    private TextView mTaskStateTextView;
    private View mSwitchController;
    private View mFrontUndoneEmptyView;
    private View mBackDoneEmptyView;
    private AnimationSet mTaskStateAnimation;
    private PageSwitchLayout.OnPageSwitchListener mOnPageSwitchListener = new PageSwitchLayout.OnPageSwitchListener() {
        @Override
        public void onPageSwitchStart(int currentPage) {
            if(isCurrentUndonePage()){
                mTaskStateTextView.setText(R.string.task_state_done);
            }else if(isCurrentDonePage()){
                mTaskStateTextView.setText(R.string.task_state_undone);
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
    private View.OnClickListener mSwitchPageClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            mPageSwitchLayout.switchPage();
        }
    };

    private boolean mTodayTasksCheck;

    public TodayFragment( ){
        super();
        initData( );
    }

    private void initData( ){
        long duration = 600;
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
        this.mTodayTasksCheck = context.getSharedPreferences(Util.PREFERENCE_NAME, Context.MODE_PRIVATE).
                getBoolean(Util.PREFERENCE_KEY_TODAY_TASKS_CHECK, false);

        this.mMainLayout = View.inflate(context, R.layout.fragment_switch_layout, null);
        this.mMainLayout.findViewById(R.id.pageContainer).setBackgroundResource(R.drawable.page_background_gray);
        this.mTaskStateTextView = (TextView) this.mMainLayout.findViewById(R.id.taskStateTextView);
        this.mPageSwitchLayout = (PageSwitchLayout) this.mMainLayout.findViewById(R.id.pageSwitchLayout);
        this.mSwitchController = this.mMainLayout.findViewById(R.id.switchController);
        this.mSwitchController.setOnClickListener(this.mSwitchPageClickListener);

        View frontPageView = View.inflate(context, R.layout.task_container, null);
        this.mFrontTaskUndoneGridView = (GridView) frontPageView.findViewById(R.id.gridView);
        this.mFrontUndoneEmptyView = frontPageView.findViewById(R.id.taskEmptyView);

        View backPageView = View.inflate(context, R.layout.task_container, null);
        this.mBackTaskDoneGridView = (GridView) backPageView.findViewById(R.id.gridView);
        this.mBackDoneEmptyView = backPageView.findViewById(R.id.taskEmptyView);

        this.mPageSwitchLayout.setPageView(frontPageView, backPageView);
        this.mPageSwitchLayout.setOnPageSwitchListener(this.mOnPageSwitchListener);

        if(!this.mTodayTasksCheck){
            this.mBackTaskDoneGridView.setOnItemLongClickListener(this.mBackTaskDoneGridViewLongClickListener);
        }
        this.mFrontTaskUndoneGridView.setOnItemLongClickListener(this.mFontTaskUndoneGridViewLongClickListener);
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

    @Override
    public void checkTasks(View view) {
        Intent intent = new Intent(getActivity(), CheckTaskActivity.class);
        startActivity(intent);
    }

    @Override
    public void addTasks(View view) {
        startAddTaskActivity(getDayType(), isCurrentDonePage());
    }

    private void checkEmptyView(ArrayList tasks, View emptyView, View mainView){
        if(tasks.size() == 0){
            emptyView.setVisibility(View.VISIBLE);
            mainView.setVisibility(View.INVISIBLE);
        }else{
            emptyView.setVisibility(View.INVISIBLE);
            mainView.setVisibility(View.VISIBLE);
        }
    }

    @Override
    protected void checkEmptyView() {
        checkEmptyView(this.mModel.getUndoneTasks(), this.mFrontUndoneEmptyView, this.mFrontTaskUndoneGridView);
        checkEmptyView(this.mModel.getDoneTasks(), this.mBackDoneEmptyView, this.mBackTaskDoneGridView);
    }
}
