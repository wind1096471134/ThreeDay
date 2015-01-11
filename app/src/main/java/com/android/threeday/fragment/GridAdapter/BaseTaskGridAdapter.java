package com.android.threeday.fragment.GridAdapter;

import android.content.Context;
import android.os.Looper;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseAdapter;

import com.android.threeday.model.threeDay.TaskItem;
import com.android.threeday.view.BaseContentChangeView;

import java.util.ArrayList;
import java.util.Random;

/**
 * Created by user on 2014/11/9.
 */
public abstract class BaseTaskGridAdapter extends BaseAdapter {
    private final int mFirstContentDuration = 4000;
    private final int mSecondContentDuration = 2000;
    private int mItemPressBackgroundResId = -1;
    private int mGridItemHeight = 100;
    private boolean mContentChanging;

    private ArrayList<TaskItem> mTaskItems;
    //save all contentChangeView from getView to control their behavior
    private ArrayList<BaseContentChangeView> mBaseContentChangeViews;
    private Looper mLooper;
    protected Random mRandom = new Random(0);
    protected Context mContext;

    BaseTaskGridAdapter(Context context, ArrayList<TaskItem> taskItems){
        this.mContext = context;
        this.mTaskItems = taskItems;
        this.mBaseContentChangeViews = new ArrayList<BaseContentChangeView>(taskItems.size());
    }

    public void setItemPressBackgroundResource(int resId){
        this.mItemPressBackgroundResId = resId;
    }

    public void setLooper(Looper looper){
        this.mLooper = looper;
    }

    public void setGridItemHeight(int height){
        this.mGridItemHeight = height;
    }

    public void notifyDataSetChanged(boolean changeContent) {
        stopAllContentChange();
        this.mContentChanging = changeContent;
        this.mBaseContentChangeViews.clear();
        super.notifyDataSetChanged();
    }

    protected void stopAllContentChange( ){
        this.mContentChanging = false;
        for(BaseContentChangeView baseContentChangeView : this.mBaseContentChangeViews){
            if(baseContentChangeView.isContentChanging()){
                baseContentChangeView.stopChangeContent();
            }
        }
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

    protected int getRandomStartDelay( ){
        return 1000 + mRandom.nextInt(5000);
    }

    protected View fetchViewAndData(int position, View convertView){

        BaseContentChangeView contentChangeView = null;
        TaskItem taskItem = this.mTaskItems.get(position);

        if(convertView == null){
            contentChangeView = getContentChangeView();
            View firstView = getFirstView();
            View secondView = getSecondView();
            contentChangeView.setContentView(firstView, this.mFirstContentDuration, secondView, this.mSecondContentDuration);

            if(this.mItemPressBackgroundResId != -1){
                contentChangeView.setOnPressRes(this.mItemPressBackgroundResId);
            }
            contentChangeView.setLayoutParams(new AbsListView.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, this.mGridItemHeight));

            if(this.mLooper != null){
                contentChangeView.setHandler(taskItem.getId(), this.mLooper);
            }

            convertView = contentChangeView;

        }else{

            contentChangeView = (BaseContentChangeView) convertView;

            if(contentChangeView.getViewId() != taskItem.getId() && contentChangeView.canChangeContent()){
                contentChangeView.reset(taskItem.getId());
            }
        }

        if(!this.mBaseContentChangeViews.contains(contentChangeView)){
            this.mBaseContentChangeViews.add(contentChangeView);
        }
        fetchContentChangeView(contentChangeView, taskItem);

        return convertView;

    }

    protected void startChangeContent(BaseContentChangeView contentChangeView){
        //avoid start twice
        if(this.mContentChanging){
            if(!contentChangeView.isContentChanging()){
                contentChangeView.startChangeContent(getRandomStartDelay());
            }
        }
    }

    public void onResume( ){
        notifyDataSetChanged(true);
    }

    public void updateData( ){
        notifyDataSetChanged(false);
    }

    public void onPause( ){
        stopAllContentChange();
    }

    protected abstract View getFirstView( );

    protected abstract View getSecondView( );

    protected abstract void fetchContentChangeView(BaseContentChangeView contentChangeView, TaskItem taskItem);

    protected abstract BaseContentChangeView getContentChangeView( );
}
