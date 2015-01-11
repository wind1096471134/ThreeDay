package com.android.threeday.activity.remindActivity;

import android.app.Activity;
import android.app.NotificationManager;
import android.content.Intent;
import android.os.Bundle;
import android.text.format.Time;
import android.view.View;
import android.widget.TextView;

import com.android.threeday.R;
import com.android.threeday.model.threeDay.TaskItem;
import com.android.threeday.model.threeDay.TodayModel;
import com.android.threeday.util.Util;
import com.android.threeday.view.PageSwitchLayout;
import com.umeng.analytics.MobclickAgent;

import java.util.ArrayList;

/**
 * Created by user on 2014/12/5.
 */
public class RemindTaskActivity extends Activity {
    private int mRemainTaskId;

    private TodayModel mModel;
    private PageSwitchLayout mPageSwitchLayout;
    private TextView mTaskInformationTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.remain_activity_main);
        initData( );
        initView( );
        fetchViewAndData();
    }

    @Override
    protected void onResume() {
        super.onResume();
        MobclickAgent.onResume(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        this.mRemainTaskId = getIntent().getIntExtra(Util.TASK_ID, -1);
        fetchViewAndData( );
    }

    private void initData( ){
        this.mRemainTaskId = getIntent().getIntExtra(Util.TASK_ID, -1);
        this.mModel = new TodayModel(this);
    }

    private void fetchViewAndData( ){
        TaskItem taskItem = this.mModel.getUndoneTasks().get(getTaskPositionById());
        this.mTaskInformationTextView.setText(taskItem.getInformation());
    }

    private boolean isDay( ){
        Time time = new Time();
        time.setToNow();
        return time.hour > 6 && time.hour < 18;
    }

    private void initView( ){
        View mainView = findViewById(R.id.mainContainer);
        int resId = isDay() ? R.drawable.bg_yesterday_day : R.drawable.bg_fine_night;
        mainView.setBackgroundResource(resId);

        this.mPageSwitchLayout = (PageSwitchLayout) findViewById(R.id.pageSwitchLayout);

        View firstView = View.inflate(this, R.layout.check_task_state, null);
        View secondView = View.inflate(this, R.layout.check_task_evaluation, null);
        this.mPageSwitchLayout.setPageView(firstView, secondView);

        secondView.findViewById(R.id.evaluationBad).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onEvaluationCheck(Util.EVALUATION_BAD);
            }
        });
        secondView.findViewById(R.id.evaluationMid).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onEvaluationCheck(Util.EVALUATION_MID);
            }
        });
        secondView.findViewById(R.id.evaluationGood).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onEvaluationCheck(Util.EVALUATION_GOOD);
            }
        });

        this.mTaskInformationTextView = (TextView) firstView.findViewById(R.id.taskInformationTextView);
    }

    private int getTaskPositionById( ){
        ArrayList<TaskItem> arrayList = this.mModel.getUndoneTasks();
        for(int i = 0, size = arrayList.size(); i < size; i++){
            if(arrayList.get(i).getId() == this.mRemainTaskId){
                return i;
            }
        }
        return 0;
    }

    private void onEvaluationCheck(int evaluation){
        Time time = new Time();
        time.setToNow();
        try {
            if(this.mModel.doneTask(getTaskPositionById(), time.format2445(), evaluation)){
                cancelNotification();
                finish();
            }
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }
    }

    public void doneTask(View view){
        this.mPageSwitchLayout.switchPage();
    }

    public void undoneTask(View view){
        cancelNotification();
        this.mModel.cancelUndoneTaskRemain(getTaskPositionById());
        finish();
    }

    private void cancelNotification( ){
        NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        notificationManager.cancel(this.mRemainTaskId);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
