package com.android.threeday.activity.mainActivity;

import com.android.threeday.fragment.BaseDayFragment;

/**
 * Created by user on 2014/11/9.
 */
public interface FragmentTaskLongClickListener {
    public void onTaskUndoneLongClick(BaseDayFragment baseDayFragment, boolean taskShouldRemain);
    public void onTaskDoneLongClick(BaseDayFragment baseDayFragment);
}
