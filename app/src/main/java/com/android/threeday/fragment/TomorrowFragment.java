package com.android.threeday.fragment;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.widget.GridView;

import com.android.threeday.R;
import com.android.threeday.model.BaseDayModel;
import com.android.threeday.model.TomorrowModel;
import com.android.threeday.view.RotePageLayout;

/**
 * Created by user on 2014/10/29.
 */
public class TomorrowFragment extends BaseDayFragment {
    private RotePageLayout mRotePageLayout;
    private GridView mFontGridView;

    private TaskGridAdapter mFontGridAdapter;

    @Override
    protected void initView(Context context) {
        this.mMainLayout = new RotePageLayout(context);
        this.mRotePageLayout = (RotePageLayout) this.mMainLayout;
        View frontPageView = ((Activity) context).getLayoutInflater().inflate(R.layout.page_main, null);
        this.mFontGridView = (GridView) frontPageView.findViewById(R.id.gridView);
        View backPageView = new View(context);
        this.mRotePageLayout.setPageView(frontPageView, backPageView);
    }

    @Override
    protected void setAdapter() {
        this.mFontGridAdapter = new TaskGridAdapter(getActivity(), this.mModel.getUndoneTasks());
        if(this.mFontGridView != null){
            this.mFontGridView.setAdapter(this.mFontGridAdapter);
        }
    }

    @Override
    protected BaseDayModel getModel(Context context) {
        return new TomorrowModel(context);
    }
}
