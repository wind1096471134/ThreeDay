package com.android.threeday.fragment.dialogFragment;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.view.View;

import com.android.threeday.R;
import com.android.threeday.util.Util;

/**
 * Created by user on 2014/11/13.
 */
public class TaskEvaluationFragment extends DialogFragment {
    private EvaluationChoseListener mListener;

    public void setEvaluationChoseListener(EvaluationChoseListener listener){
        this.mListener = listener;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        View mainView = getActivity().getLayoutInflater().inflate(R.layout.task_evaluation_dialog_main, null);
        mainView.findViewById(R.id.evaluationBad).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onItemClick(Util.EVALUATION_BAD);
            }
        });
        mainView.findViewById(R.id.evaluationGood).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onItemClick(Util.EVALUATION_GOOD);
            }
        });
        mainView.findViewById(R.id.evaluationMid).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onItemClick(Util.EVALUATION_MID);
            }
        });
        AlertDialog dialog = new AlertDialog.Builder(getActivity()).setView(mainView).create();
        dialog.setCanceledOnTouchOutside(true);
        return dialog;
    }

    private void onItemClick(int evaluation){
        if(this.mListener != null){
            this.mListener.onEvaluationChosen(evaluation);
        }
    }

    public interface EvaluationChoseListener{
        public void onEvaluationChosen(int evaluation);
    }
}
