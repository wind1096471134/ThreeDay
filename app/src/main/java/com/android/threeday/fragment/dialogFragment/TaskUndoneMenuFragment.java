package com.android.threeday.fragment.dialogFragment;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.view.View;

import com.android.threeday.R;

/**
 * Created by user on 2014/11/13.
 */
public class TaskUndoneMenuFragment extends DialogFragment {
    private boolean mTaskShouldRemain = false;
    private boolean mTaskCanDone = true;

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        View mainView = getActivity().getLayoutInflater().inflate(R.layout.task_undone_long_click_menu, null);
        if(this.mTaskShouldRemain){
            mainView.findViewById(R.id.changeRemainTimeButton).setVisibility(View.VISIBLE);
            mainView.findViewById(R.id.cancelRemainButton).setVisibility(View.VISIBLE);
            mainView.findViewById(R.id.setRemianButton).setVisibility(View.GONE);
        }else{
            mainView.findViewById(R.id.changeRemainTimeButton).setVisibility(View.GONE);
            mainView.findViewById(R.id.cancelRemainButton).setVisibility(View.GONE);
            mainView.findViewById(R.id.setRemianButton).setVisibility(View.VISIBLE);
        }
        if(this.mTaskCanDone){
            mainView.findViewById(R.id.doneButton).setVisibility(View.VISIBLE);
        }else{
            mainView.findViewById(R.id.doneButton).setVisibility(View.GONE);
        }

        AlertDialog alertDialog = new AlertDialog.Builder(getActivity()).setView(mainView).create();
        alertDialog.setCanceledOnTouchOutside(true);
        return alertDialog;
    }

    public void setTaskRemain(boolean taskShouldRemain){
        this.mTaskShouldRemain = taskShouldRemain;
    }

    public void setTaskCanDone(boolean canDone){
        this.mTaskCanDone = canDone;
    }
}
