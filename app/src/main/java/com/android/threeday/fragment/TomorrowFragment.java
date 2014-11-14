package com.android.threeday.fragment;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.TextView;

import com.android.threeday.R;
import com.android.threeday.model.BaseDayModel;
import com.android.threeday.model.TomorrowModel;
import com.android.threeday.util.Util;
import com.android.threeday.view.RotePageLayout;

/**
 * Created by user on 2014/10/29.
 */
public class TomorrowFragment extends BaseDayFragment {
    private RotePageLayout mRotePageLayout;
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

    @Override
    protected void initView(Context context) {
        this.mMainLayout = new RotePageLayout(context);
        this.mRotePageLayout = (RotePageLayout) this.mMainLayout;

        View frontPageView = ((Activity) context).getLayoutInflater().inflate(R.layout.page_main, null);
        this.mFontTaskUndoneGridView = (GridView) frontPageView.findViewById(R.id.gridView);
        this.mFontTaskUndoneGridView.setOnItemLongClickListener(this.mFontTaskUndoneGridViewLongClickListener);

        frontPageView.findViewById(R.id.addButton).setOnClickListener(this.mAddUndoneTaskClickListener);
        ((TextView)frontPageView.findViewById(R.id.taskStateTextView)).setText(R.string.task_state_undone);

        View backPageView = new View(context);
        this.mRotePageLayout.setPageView(frontPageView, backPageView);
    }

    @Override
    protected void setAdapter() {
        this.mTaskUndoneGridAdapter = new TaskGridAdapter(getActivity(), this.mModel.getUndoneTasks());
        ((TaskGridAdapter) this.mTaskUndoneGridAdapter).setItemPressBackgroundResource(R.drawable.content_change_view_press);
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

}
