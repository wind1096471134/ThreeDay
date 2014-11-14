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
public class TaskDoneMenuFragment extends DialogFragment {

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        View mainView = getActivity().getLayoutInflater().inflate(R.layout.task_done_long_click_menu, null);
        AlertDialog alertDialog = new AlertDialog.Builder(getActivity()).setView(mainView).create();
        alertDialog.setCanceledOnTouchOutside(true);
        return alertDialog;
    }
}
