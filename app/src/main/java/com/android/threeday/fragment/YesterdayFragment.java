package com.android.threeday.fragment;

import com.android.threeday.model.BaseDayModel;
import com.android.threeday.model.YesterdayModel;

/**
 * Created by user on 2014/10/29.
 */
public class YesterdayFragment extends BaseDayFragment {

    @Override
    protected BaseDayModel getModel() {
        return new YesterdayModel(getActivity());
    }
}
