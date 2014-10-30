package com.android.threeday.activity;

import android.test.ActivityInstrumentationTestCase2;
import android.widget.TextView;

import com.android.threeday.R;
import com.android.threeday.activity.MainActivity;

/**
 * Created by user on 2014/10/28.
 */
public class MainActivityTest extends ActivityInstrumentationTestCase2<MainActivity>{
    private TextView textView;

    public MainActivityTest() {
        super(MainActivity.class);
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        textView = (TextView) getActivity().findViewById(R.id.textView);
    }

    public void testText( ) throws Exception{
        String text = getActivity().getString(R.string.hello_world);
        assertEquals(textView.getText(), text);
    }

    @Override
    protected void tearDown() throws Exception {
        super.tearDown();
    }
}
