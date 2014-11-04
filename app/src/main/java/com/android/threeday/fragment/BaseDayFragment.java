package com.android.threeday.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.threeday.model.BaseDayModel;

/**
 * Created by user on 2014/10/29.
 */
public abstract class BaseDayFragment extends Fragment {
    protected BaseDayModel mModel;

    public BaseDayFragment( ){
        super();
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        initData( );
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return super.onCreateView(inflater, container, savedInstanceState);
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

    private void initData( ){
        this.mModel = getModel();
    }

    protected abstract BaseDayModel getModel( );
}
