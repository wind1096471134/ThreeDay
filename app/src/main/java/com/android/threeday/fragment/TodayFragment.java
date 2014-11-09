package com.android.threeday.fragment;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;

import com.android.threeday.R;
import com.android.threeday.model.BaseDayModel;
import com.android.threeday.model.TodayModel;
import com.android.threeday.view.RotePageLayout;

/**
 * Created by user on 2014/10/29.
 */
public class TodayFragment extends BaseDayFragment {
    private RotePageLayout mRotePageLayout;
    private GridView mFrontGridView;
    private GridView mBackGridView;

    private BaseAdapter mFrontGridAdapter;
    private BaseAdapter mBackGridAdapter;

    private AdapterView.OnItemLongClickListener mFontGridViewLongClickListener = new AdapterView.OnItemLongClickListener() {
        @Override
        public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
            if(mFragmentTaskLongClickListener != null){
                mFragmentTaskLongClickListener.onTaskUndoneLongClick();
                return true;
            }
            return false;
        }
    };
    private AdapterView.OnItemLongClickListener mBackGridViewLongClickListener = new AdapterView.OnItemLongClickListener() {
        @Override
        public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
            return false;
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
        this.mFrontGridView = (GridView) frontPageView.findViewById(R.id.gridView);
        View backPageView = ((Activity) context).getLayoutInflater().inflate(R.layout.page_main, null);
        this.mBackGridView = (GridView) backPageView.findViewById(R.id.gridView);
        this.mRotePageLayout.setPageView(frontPageView, backPageView);

        this.mFrontGridView.setOnItemLongClickListener(this.mFontGridViewLongClickListener);
        this.mBackGridView.setOnItemLongClickListener(this.mBackGridViewLongClickListener);
    }

    @Override
    protected void setAdapter() {
        this.mFrontGridAdapter = new TaskGridAdapter(getActivity(), this.mModel.getDoneTasks());
        this.mBackGridAdapter = new TaskGridAdapter(getActivity(), this.mModel.getUndoneTasks());

        if(this.mFrontGridView != null){
            this.mFrontGridView.setAdapter(this.mFrontGridAdapter);
        }
        if(this.mBackGridView != null){
            this.mBackGridView.setAdapter(this.mBackGridAdapter);
        }
    }

    @Override
    protected BaseDayModel getModel(Context context) {
        return new TodayModel(context);
    }

}
