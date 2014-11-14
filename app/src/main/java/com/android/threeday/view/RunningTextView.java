package com.android.threeday.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.TextView;

/**
 * Created by user on 2014/11/12.
 */
public class RunningTextView extends TextView {
    public RunningTextView(Context context) {
        super(context);
    }

    public RunningTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public RunningTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public RunningTextView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    public boolean isFocused() {
        return true;
    }
}
