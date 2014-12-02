package com.android.threeday.fragment.GridAdapter;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.threeday.R;
import com.android.threeday.model.threeDay.TaskItem;
import com.android.threeday.util.Util;
import com.android.threeday.view.BaseContentChangeView;
import com.android.threeday.view.SlideDownContentChangeView;
import com.android.threeday.view.SlideUpContentChangeView;

import java.util.ArrayList;

/**
 * Created by user on 2014/11/19.
 */
public class TaskFinishGridAdapter extends  BaseTaskGridAdapter{

    public TaskFinishGridAdapter(Context context, ArrayList<TaskItem> taskItems) {
        super(context, taskItems);
    }

    @Override
    protected View getFirstView() {
        View view = View.inflate(this.mContext, R.layout.content_change_view_first_view, null);
        view.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        return view;
    }

    @Override
    protected View getSecondView() {
        View view = View.inflate(this.mContext, R.layout.done_content_change_view_second_view, null);
        view.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        return view;
    }

    @Override
    protected void fetchContentChangeView(BaseContentChangeView contentChangeView, TaskItem taskItem) {
        TextView textView = (TextView) contentChangeView.getFirstContentView().findViewById(R.id.informationTextView);
        textView.setText(taskItem.getInformation());

        View secondView = contentChangeView.getSecondContentView();
        ImageView imageView = (ImageView) secondView.findViewById(R.id.evaluationImageView);
        setEvaluationImageView(imageView, taskItem.getEvaluation());

        startChangeContent(contentChangeView);
    }

    @Override
    protected BaseContentChangeView getContentChangeView() {
        int choice = this.mRandom.nextInt(3);
        if(choice == 1){
            return new SlideDownContentChangeView(this.mContext);
        }else{
            return new SlideUpContentChangeView(this.mContext);
        }
    }

    private void setEvaluationImageView(ImageView imageView, int evaluation){
        int imageSrc;
        switch (evaluation){
            case Util.EVALUATION_BAD:
                imageSrc = R.drawable.ic_launcher;
                break;
            case Util.EVALUATION_MID:
                imageSrc = R.drawable.ic_launcher;
                break;
            case Util.EVALUATION_GOOD:
                imageSrc = R.drawable.ic_launcher;
                break;
            default:
                imageSrc = R.drawable.ic_launcher;
        }
        imageView.setImageResource(imageSrc);
    }
}
