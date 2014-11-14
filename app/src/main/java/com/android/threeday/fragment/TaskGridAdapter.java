package com.android.threeday.fragment;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.android.threeday.model.TaskItem;
import com.android.threeday.view.ContentChangeView;

import java.util.ArrayList;

/**
 * Created by user on 2014/11/9.
 */
class TaskGridAdapter extends BaseAdapter {
    private int mItemPressBackgroundResId = -1;
    private Context mContext;
    private ArrayList<TaskItem> mTaskItems;

    TaskGridAdapter(Context context, ArrayList<TaskItem> taskItems){
        this.mContext = context;
        this.mTaskItems = taskItems;
    }

    public void setItemPressBackgroundResource(int resId){
        this.mItemPressBackgroundResId = resId;
    }

    @Override
    public int getCount() {
        return this.mTaskItems.size();
    }

    @Override
    public Object getItem(int position) {
        return this.mTaskItems.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        return fetchViewAndData(position, convertView);
    }

    protected View fetchViewAndData(int position, View convertView){
        TextView informationTextView = null;
        TextView evaluationTextView = null;
        if(convertView == null){
            ContentChangeView contentChangeView = new ContentChangeView(mContext);
            informationTextView = new TextView(mContext);
            evaluationTextView = new TextView(mContext);
            contentChangeView.setContentView(informationTextView, 1000, evaluationTextView, 500);
            if(this.mItemPressBackgroundResId != -1){
                contentChangeView.setBackgroundResource(this.mItemPressBackgroundResId);
            }
            convertView = contentChangeView;
        }else{
            ContentChangeView contentChangeView = (ContentChangeView) convertView;
            informationTextView = (TextView) contentChangeView.getFirstContentView();
            evaluationTextView = (TextView) contentChangeView.getSecondContentView();
        }
        informationTextView.setText(this.mTaskItems.get(position).getInformation());
        evaluationTextView.setText(Integer.toString(mTaskItems.get(position).getEvaluation()));
        return convertView;
    }

}
