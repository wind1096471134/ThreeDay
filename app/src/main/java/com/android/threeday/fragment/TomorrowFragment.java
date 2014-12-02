package com.android.threeday.fragment;

import android.content.Context;
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

/**
 * Created by user on 2014/10/29.
 */
public class TomorrowFragment extends BaseDayFragment {
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
        this.mMainLayout = View.inflate(context, R.layout.fragment_tomorrow_layout, null);
        this.mFontTaskUndoneGridView = (GridView) this.mMainLayout.findViewById(R.id.gridView);
        this.mFontTaskUndoneGridView.setOnItemLongClickListener(this.mFontTaskUndoneGridViewLongClickListener);
        this.mMainLayout.findViewById(R.id.addButton).setOnClickListener(this.mAddUndoneTaskClickListener);
        ((TextView)this.mMainLayout.findViewById(R.id.taskStateTextView)).setText(R.string.task_state_undone);
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
        return true;
    }
}
