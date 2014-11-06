package com.android.threeday.fragment;

import android.content.Context;

import com.android.threeday.model.BaseDayModel;
import com.android.threeday.model.TomorrowModel;

/**
 * Created by user on 2014/10/29.
 */
public class TomorrowFragment extends BaseDayFragment {
    @Override
    protected void initView(Context context) {

    }

    @Override
    protected void setAdapter() {

    }

    @Override
    protected BaseDayModel getModel(Context context) {
        return new TomorrowModel(context);
    }
}
