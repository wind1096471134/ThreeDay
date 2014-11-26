package com.android.threeday.fragment.GridAdapter;

import android.content.Context;
import android.graphics.Color;
import android.text.format.Time;
import android.view.View;
import android.widget.TextView;

import com.android.threeday.R;
import com.android.threeday.model.TaskItem;
import com.android.threeday.view.BaseContentChangeView;

import java.util.ArrayList;

/**
 * Created by user on 2014/11/19.
 */
public class TaskUnFinishGridAdapter extends BaseTaskGridAdapter {

    public TaskUnFinishGridAdapter(Context context, ArrayList<TaskItem> taskItems) {
        super(context, taskItems);
    }

    @Override
    protected View getFirstView() {
        View view = View.inflate(this.mContext, R.layout.content_change_view_first_view, null);
        view.setBackgroundColor(Color.GRAY);
        return view;
    }

    @Override
    protected View getSecondView() {
        View view = View.inflate(this.mContext, R.layout.content_change_view_second_view, null);
        view.setBackgroundColor(Color.GRAY);
        return view;
    }

    @Override
    protected void fetchContentChangeView(BaseContentChangeView contentChangeView, TaskItem taskItem) {
        TextView informationTextView = (TextView) contentChangeView.getFirstContentView().findViewById(R.id.informationTextView);
        informationTextView.setText(taskItem.getInformation());

        View secondView = contentChangeView.getSecondContentView();
        secondView.findViewById(R.id.imageView).setVisibility(View.INVISIBLE);
        TextView textView = (TextView) secondView.findViewById(R.id.textView);
        String remainText = getRemainText(taskItem);
        textView.setText(remainText);
    }

    private String getRemainText(TaskItem taskItem){
        String result = this.mContext.getString(R.string.remain_to_do);
        if(taskItem.getRemain()){
            String remainTime = taskItem.getRemainTime();
            Time remain = new Time();
            remain.parse(remainTime);
            Time now = new Time();
            now.setToNow();
            if(!remain.before(now)){

                result = this.mContext.getString(R.string.about);
                if(remain.hour == now.hour){

                    if(remain.minute == now.minute){
                        result = this.mContext.getString(R.string.now_to_do);
                        return result;
                    }else{
                        int minute = remain.minute - now.minute;
                        if(remain.second - now.second > 30){
                            minute++;
                        }
                        result += minute + this.mContext.getString(R.string.minute);
                    }

                }else{

                    int hour = remain.hour - now.hour;
                    if(remain.minute - now.minute > 30){
                        hour++;
                    }
                    result += hour + this.mContext.getString(R.string.hour);
                }

                result += this.mContext.getString(R.string.after);

            }
        }
        return result;
    }

}
