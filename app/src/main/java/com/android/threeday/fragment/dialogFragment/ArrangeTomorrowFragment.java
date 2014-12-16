package com.android.threeday.fragment.dialogFragment;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;

import com.android.threeday.R;

/**
 * Created by user on 2014/12/16.
 */
public class ArrangeTomorrowFragment extends DialogFragment {
    private OnArrangeTomorrowListener mOnArrangeTomorrowListener;

    public void setOnArrangeTomorrowListener(OnArrangeTomorrowListener onArrangeTomorrowListener){
        this.mOnArrangeTomorrowListener = onArrangeTomorrowListener;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog alertDialog = new AlertDialog.Builder(getActivity()).setTitle(R.string.arrange_tomorrow_title)
                .setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if(mOnArrangeTomorrowListener != null){
                            mOnArrangeTomorrowListener.onArrangeTomorrowSet();
                        }
                    }
                }).setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if(mOnArrangeTomorrowListener != null){
                            mOnArrangeTomorrowListener.onArrangeTomorrowCancel();
                        }
                    }
                }).create();
        alertDialog.setCanceledOnTouchOutside(true);
        return alertDialog;
    }

    public interface OnArrangeTomorrowListener{
        public void onArrangeTomorrowSet( );
        public void onArrangeTomorrowCancel( );
    }
}
