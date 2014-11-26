package com.android.threeday.activity.mainActivity;

import android.support.v4.app.Fragment;

/**
 * Created by user on 2014/11/12.
 */
public interface FragmentStateListener {
    public void onFragmentAttach(Fragment fragment);
    public void onFragmentViewCreate(Fragment fragment);
}
