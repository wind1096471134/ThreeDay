package com.android.threeday.model.addTask;

import android.content.Context;

import com.android.threeday.model.BaseModel;

import java.util.ArrayList;

/**
 * Created by user on 2014/11/27.
 */
public class AddTaskLabelModel implements BaseModel {
    private Context mContext;
    private AddTaskLabelDbHelper mAddTaskLabelDbHelper;
    private ArrayList<Label> mLabels;

    public AddTaskLabelModel(Context context){
        this.mContext = context;
        initData( );
    }

    private void initData( ){
        this.mAddTaskLabelDbHelper = new AddTaskLabelDbHelper(this.mContext);
        this.mLabels = this.mAddTaskLabelDbHelper.getLabels();
    }

    public ArrayList<Label> getLabels( ){
        return this.mLabels;
    }

    public boolean addLabel(String labelName){
        long id = this.mAddTaskLabelDbHelper.addLabel(labelName);
        if(id != -1){
            this.mLabels.add(0, new Label(id, labelName));
            return true;
        }
        return false;
    }

    public boolean deleteLabel(int position){
        if(position < this.mLabels.size()){
            long id = this.mLabels.get(position).getId();
            if(this.mAddTaskLabelDbHelper.deleteLabel(id) == 1){
                this.mLabels.remove(position);
                return true;
            }
        }
        return false;
    }
}
