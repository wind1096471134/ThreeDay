package com.android.threeday.activity.remainActivity;

import android.app.Activity;
import android.app.NotificationManager;
import android.content.Intent;
import android.os.Bundle;
import android.text.format.Time;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.android.threeday.R;
import com.android.threeday.model.threeDay.TaskItem;
import com.android.threeday.model.threeDay.TodayModel;
import com.android.threeday.util.Util;
import com.android.threeday.view.PageSwitchLayout;

import java.util.ArrayList;

/**
 * Created by user on 2014/12/5.
 */
public class RemainTaskActivity extends Activity {

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

        Log.e("wind","remain create " + this.mRemainTaskId);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        this.mRemainTaskId = getIntent().getIntExtra(Util.TASK_ID, -1);
        Log.e("wind","remain new intent " + this.mRemainTaskId);
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

    private void initView( ){
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
        finish();
    }

    private void cancelNotification( ){
        NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        notificationManager.cancel(this.mRemainTaskId);
    }
/*
    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        if(keyCode == KeyEvent.KEYCODE_BACK){
            Toast.makeText(this, R.string.not_leave_without_finish_remain, Toast.LENGTH_SHORT).show();
            return true;
        }
        return super.onKeyUp(keyCode, event);
    }*/

    @Override
    protected void onDestroy() {
        Log.e("wind", "remain destroy");
        super.onDestroy();
    }
}
