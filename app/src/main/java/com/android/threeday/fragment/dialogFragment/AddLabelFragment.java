package com.android.threeday.fragment.dialogFragment;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.widget.EditText;

import com.android.threeday.R;
import com.android.threeday.activity.addTaskActivity.WordsLimitTextWatcher;

/**
 * Created by user on 2014/11/27.
 */
public class AddLabelFragment extends DialogFragment {
    private OnLabelAddListener mOnLabelAddListener;

    public void setOnLabelAddListener(OnLabelAddListener onLabelAddListener){
        this.mOnLabelAddListener = onLabelAddListener;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final EditText editText = new EditText(getActivity());
        editText.setHint(R.string.label_words_limit);
        WordsLimitTextWatcher wordsLimitTextWatcher = new WordsLimitTextWatcher();
        wordsLimitTextWatcher.setWordsLimit(getResources().getInteger(R.integer.words_limit_num));
        editText.addTextChangedListener(wordsLimitTextWatcher);

        AlertDialog alertDialog = new AlertDialog.Builder(getActivity()).setView(editText)
                .setTitle(R.string.add_label_dialog_title).setPositiveButton(R.string.sure, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if(mOnLabelAddListener != null){
                            mOnLabelAddListener.onLabelAdd(editText.getText().toString());
                        }
                    }
                }).setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                }).create();
        alertDialog.setCanceledOnTouchOutside(true);
        return alertDialog;
    }

    public interface OnLabelAddListener{
        public void onLabelAdd(String labelName);
    }
}
