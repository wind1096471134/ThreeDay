package com.android.threeday.model.addTask;

/**
 * Created by user on 2014/11/27.
 */
public class Label {
    private long mId;
    private String mLabelName;

    public Label(long id, String labelName){
        this.mId = id;
        this.mLabelName = labelName;
    }

    public long getId( ){
        return this.mId;
    }

    public String getLabelName( ){
        return this.mLabelName;
    }

    public void setLabelName(String labelName){
        this.mLabelName = labelName;
    }
}
