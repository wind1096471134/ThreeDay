package com.android.threeday.fragment;

import com.android.threeday.model.BaseDayModel;
import com.android.threeday.model.TodayModel;

/**
 * Created by user on 2014/10/29.
 */
public class TodayFragment extends BaseDayFragment {

    public TodayFragment( ){
        super();
    }

    @Override
    protected BaseDayModel getModel() {
        return new TodayModel(getActivity());
    }

}
