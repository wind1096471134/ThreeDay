package com.android.threeday.activity.addTaskActivity;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.android.threeday.R;
import com.android.threeday.model.addTask.Label;

import java.util.ArrayList;

/**
 * Created by user on 2014/11/26.
 */
class LabelGridAdapter extends BaseAdapter {
    private int mItemHeight = AbsListView.LayoutParams.WRAP_CONTENT;
    private Context mContext;
    private ArrayList<Label> mLabels;

    private boolean mDeleteLabelsEnable;

    LabelGridAdapter(Context context, ArrayList<Label> labels){
        this.mContext = context;
        this.mLabels = labels;
    }

    public void setItemHeight(int height){
        this.mItemHeight = height;
    }

    public void notifyDataSetChanged(boolean enableDeleteLabels){
        this.mDeleteLabelsEnable = enableDeleteLabels;
        notifyDataSetChanged();
    }

    public boolean isDeleteLabelsEnable( ){
        return this.mDeleteLabelsEnable;
    }

    @Override
    public int getCount() {
        return this.mLabels.size() + 1;
    }

    @Override
    public Object getItem(int position) {
        if(position == 0){
            return 0;
        }else{
            return this.mLabels.get(position);
        }
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if(position == 0){
            if(convertView == null || !(Boolean) convertView.getTag()){
                convertView = View.inflate(this.mContext, R.layout.add_task_add_controller, null);
                convertView.setTag(true);
                convertView.setLayoutParams(new AbsListView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, this.mItemHeight));
            }
            int backgroundResId = this.mDeleteLabelsEnable ? R.color.label_normal : R.drawable.label_background;
            convertView.findViewById(R.id.controller).setBackgroundResource(backgroundResId);
            return convertView;
        }

        if(convertView == null || (Boolean) convertView.getTag()){
            convertView = View.inflate(this.mContext, R.layout.add_task_label_view, null);
            convertView.setTag(false);
            convertView.setLayoutParams(new AbsListView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, this.mItemHeight));
        }
        int backgroundResId = this.mDeleteLabelsEnable ? R.color.label_normal : R.drawable.label_background;
        convertView.findViewById(R.id.controller).setBackgroundResource(backgroundResId);

        View deleteView = convertView.findViewById(R.id.deleteButton);
        deleteView.setTag(position);
        int deleteViewVisibility = this.mDeleteLabelsEnable ? View.VISIBLE : View.INVISIBLE;
        deleteView.setVisibility(deleteViewVisibility);

        TextView nameTextView = (TextView) convertView.findViewById(R.id.labelTextView);
        nameTextView.setText(this.mLabels.get(position - 1).getLabelName());
        return convertView;
    }
}
