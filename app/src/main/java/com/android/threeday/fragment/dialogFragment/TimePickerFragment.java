package com.android.threeday.fragment.dialogFragment;

import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.text.format.Time;
import android.util.Log;

/**
 * Created by user on 2014/11/13.
 */
public class TimePickerFragment extends DialogFragment{
    private TimePickerDialog.OnTimeSetListener mTimeSetListener;
    private boolean mInitTime;
    private int mHour;
    private int mMinute;

    public TimePickerFragment( ){
        super();
    }

    public void setTimeSetListener(TimePickerDialog.OnTimeSetListener listener){
        this.mTimeSetListener = listener;
    }

    public void setInitTime(int hour, int minute){
        this.mHour = hour;
        this.mMinute = minute;
        this.mInitTime = true;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        int hour;
        int minute;
        if(this.mInitTime){
            hour = this.mHour;
            minute = this.mMinute;
        }else{
            Time time = new Time();
            time.setToNow();
            hour = time.hour;
            minute = time.minute;
        }
        TimePickerDialog timePickerDialog = new TimePickerDialog(getActivity(), this.mTimeSetListener, hour,
                minute, true);
        timePickerDialog.setCanceledOnTouchOutside(true);
        return timePickerDialog;
    }

}

