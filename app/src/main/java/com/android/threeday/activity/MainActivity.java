package com.android.threeday.activity;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;

import com.android.threeday.R;

/**
 * Created by user on 2014/10/29.
 */
public class MainActivity extends FragmentActivity{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

}
