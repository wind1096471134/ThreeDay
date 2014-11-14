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

    public TimePickerFragment( ){
        super();
    }

    public void setTimeSetListener(TimePickerDialog.OnTimeSetListener listener){
        this.mTimeSetListener = listener;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Time time = new Time();
        time.setToNow();
        TimePickerDialog timePickerDialog = new TimePickerDialog(getActivity(), this.mTimeSetListener, time.hour,
                time.minute, true);
        return timePickerDialog;
    }

}

