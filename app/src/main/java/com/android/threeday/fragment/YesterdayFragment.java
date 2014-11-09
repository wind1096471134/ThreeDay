package com.android.threeday.fragment;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.widget.BaseAdapter;
import android.widget.GridView;

import com.android.threeday.R;
import com.android.threeday.model.BaseDayModel;
import com.android.threeday.model.YesterdayModel;
import com.android.threeday.view.RotePageLayout;

/**
 * Created by user on 2014/10/29.
 */
public class YesterdayFragment extends BaseDayFragment {
    private RotePageLayout mRotePageLayout;
    private GridView mFrontGridView;
    private GridView mBackGridView;

    private BaseAdapter mFrontGridAdapter;
    private BaseAdapter mBackGridAdapter;

    public YesterdayFragment( ){
        super();
    }

    @Override
    protected void initView(Context context) {
        this.mMainLayout = new RotePageLayout(context);
        this.mRotePageLayout = (RotePageLayout) this.mMainLayout;
        View frontView = ((Activity) context).getLayoutInflater().inflate(R.layout.page_main, null);
        this.mFrontGridView = (GridView) frontView.findViewById(R.id.gridView);
        View backView = ((Activity) context).getLayoutInflater().inflate(R.layout.page_main, null);
        this.mBackGridView = (GridView) backView.findViewById(R.id.gridView);
        this.mRotePageLayout.setPageView(frontView, backView);
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
        return new YesterdayModel(context);
    }

}
