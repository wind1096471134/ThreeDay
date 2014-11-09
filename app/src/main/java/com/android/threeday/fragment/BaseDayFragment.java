package com.android.threeday.fragment;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.threeday.activity.FragmentTaskLongClickListener;
import com.android.threeday.model.BaseDayModel;

/**
 * Created by user on 2014/10/29.
 */
public abstract class BaseDayFragment extends Fragment {
    protected BaseDayModel mModel;
    protected View mMainLayout;
    protected FragmentTaskLongClickListener mFragmentTaskLongClickListener;

    protected BaseDayFragment( ){
        super();
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        initData(activity);
        initView(activity);
        setAdapter();
        this.mFragmentTaskLongClickListener = (FragmentTaskLongClickListener) activity;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if(this.mMainLayout == null){
            initView(getActivity());
        }
        if(this.mMainLayout.getParent() != null){
            ((ViewGroup)this.mMainLayout.getParent()).removeView(this.mMainLayout);
        }
        return this.mMainLayout;
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    private void initData(Context context){
        this.mModel = getModel(context);
    }

    protected abstract void initView(Context context);

    protected abstract void setAdapter( );

    protected abstract BaseDayModel getModel(Context context);
}
