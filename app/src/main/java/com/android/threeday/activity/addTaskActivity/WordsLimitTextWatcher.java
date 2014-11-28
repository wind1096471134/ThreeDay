package com.android.threeday.activity.addTaskActivity;

import android.text.Editable;
import android.text.TextWatcher;

/**
 * Created by user on 2014/11/28.
 */
public class WordsLimitTextWatcher implements TextWatcher {
    private int mWordsLimitNum = -1;

    public WordsLimitTextWatcher( ){
        super();
    }

    public void setWordsLimit(int limitNum){
        this.mWordsLimitNum = limitNum;
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {
        if(s.length() > this.mWordsLimitNum){
            s.delete(this.mWordsLimitNum, s.length());
        }
    }
}
