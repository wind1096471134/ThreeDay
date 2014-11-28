package com.android.threeday.fragment.dialogFragment;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;

import com.android.threeday.R;

/**
 * Created by user on 2014/11/27.
 */
public class TaskRemainFragment extends DialogFragment {
    private OnTaskRemainListener mOnTaskRemainListener;

    public void setOnTaskRemainListener(OnTaskRemainListener onTaskRemainListener){
        this.mOnTaskRemainListener = onTaskRemainListener;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog alertDialog = new AlertDialog.Builder(getActivity()).setTitle(R.string.remain_title)
                .setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if(mOnTaskRemainListener != null){
                            mOnTaskRemainListener.onTaskRemain();
                        }
                    }
                }).setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if(mOnTaskRemainListener != null){
                            mOnTaskRemainListener.onTaskNotRemain();
                        }
                    }
                }).create();
        return alertDialog;
    }

    public interface OnTaskRemainListener{
        public void onTaskRemain( );
        public void onTaskNotRemain( );
    }
}
