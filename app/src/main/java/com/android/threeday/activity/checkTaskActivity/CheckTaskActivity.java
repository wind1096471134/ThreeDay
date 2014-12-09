package com.android.threeday.activity.checkTaskActivity;

import android.animation.ObjectAnimator;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.format.Time;
import android.util.Log;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.TranslateAnimation;
import android.widget.TextView;
import android.widget.Toast;

import com.android.threeday.R;
import com.android.threeday.activity.mainActivity.MainActivity;
import com.android.threeday.model.threeDay.TaskItem;
import com.android.threeday.model.threeDay.TodayModel;
import com.android.threeday.util.Util;
import com.android.threeday.view.PageSwitchLayout;

/**
 * Created by user on 2014/12/2.
 */
public class CheckTaskActivity extends Activity {
    private PageSwitchLayout mPageSwitchLayout;
    private View mFirstTaskStateView;
    private View mSecondTaskEvaluationView;
    private View mDayEvaluationView;
    private TextView mTaskCheckMessageTextView;
    private TextView mTaskInformationTextView;
    private TodayModel mTodayModel;
    private String mTasksCheckMessage;
    private AnimationSet mCheckMessageAnimation;
    private PageSwitchLayout.OnPageSwitchListener mOnPageSwitchListener = new PageSwitchLayout.OnPageSwitchListener() {
        @Override
        public void onPageSwitchStart(int currentPage) {
            mTaskCheckMessageTextView.startAnimation(mCheckMessageAnimation);
        }

        @Override
        public void onPageSwitchEnd(int currentPage) {

        }
    };

    private int mCurrentUndoneTaskPosition = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.check_task_main);
        initView( );
        initData( );
    }

    private void initData( ){
        this.mTodayModel = new TodayModel(this);
        this.mCurrentUndoneTaskPosition = 0;
        this.mTasksCheckMessage = getResources().getString(R.string.check_task_message);

        this.mCheckMessageAnimation = new AnimationSet(false);
        TranslateAnimation translateAnimation = new TranslateAnimation(Animation.RELATIVE_TO_SELF, 0, Animation.RELATIVE_TO_SELF, 0,
                Animation.RELATIVE_TO_SELF, -0.3f, Animation.RELATIVE_TO_SELF, 0f);
        AlphaAnimation alphaAnimation = new AlphaAnimation(0f, 1f);
        this.mCheckMessageAnimation.addAnimation(translateAnimation);
        this.mCheckMessageAnimation.addAnimation(alphaAnimation);
        this.mCheckMessageAnimation.setDuration(500);
        this.mCheckMessageAnimation.setInterpolator(new DecelerateInterpolator());

        moveToNext();
    }

    private void initView( ){
        this.mPageSwitchLayout = (PageSwitchLayout) findViewById(R.id.pageSwitchLayout);
        this.mPageSwitchLayout.setOnPageSwitchListener(this.mOnPageSwitchListener);

        this.mFirstTaskStateView = View.inflate(this, R.layout.check_task_state, null);
        this.mSecondTaskEvaluationView = View.inflate(this, R.layout.check_task_evaluation, null);
        this.mPageSwitchLayout.setPageView(this.mFirstTaskStateView, this.mSecondTaskEvaluationView);

        this.mTaskInformationTextView = (TextView) this.mFirstTaskStateView.findViewById(R.id.taskInformationTextView);
        this.mTaskCheckMessageTextView = (TextView) findViewById(R.id.checkTaskMessageTextView);

        this.mSecondTaskEvaluationView.findViewById(R.id.evaluationGood).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onTaskEvaluationCheck(Util.EVALUATION_GOOD);
            }
        });
        this.mSecondTaskEvaluationView.findViewById(R.id.evaluationMid).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onTaskEvaluationCheck(Util.EVALUATION_MID);
            }
        });
        this.mSecondTaskEvaluationView.findViewById(R.id.evaluationBad).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onTaskEvaluationCheck(Util.EVALUATION_BAD);
            }
        });

        this.mDayEvaluationView = findViewById(R.id.dayEvaluationView);
        this.mDayEvaluationView.findViewById(R.id.evaluationGood).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onDayEvaluationCheck(Util.EVALUATION_GOOD);
            }
        });
        this.mDayEvaluationView.findViewById(R.id.evaluationMid).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onDayEvaluationCheck(Util.EVALUATION_MID);
            }
        });
        this.mDayEvaluationView.findViewById(R.id.evaluationBad).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onDayEvaluationCheck(Util.EVALUATION_BAD);
            }
        });
    }

    private void onDayEvaluationCheck(int evaluation){
        SharedPreferences sharedPreferences = getSharedPreferences(Util.PREFERENCE_NAME, MODE_PRIVATE);
        sharedPreferences.edit().putInt(Util.PREFERENCE_KEY_DAY_EVALUATION, evaluation)
                .putBoolean(Util.PREFERENCE_KEY_TODAY_TASKS_CHECK, true).commit();
        Intent intent = new Intent(this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();
    }

    private void onTaskEvaluationCheck(int evaluation){
        Time time = new Time();
        time.setToNow();
        try {
            if(this.mTodayModel.doneTask(this.mCurrentUndoneTaskPosition, time.format2445(), evaluation)){
                this.mPageSwitchLayout.switchPage();
                moveToNext();
            }
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }
    }

    public void doneTask(View view){
        this.mPageSwitchLayout.switchPage();
    }

    public void undoneTask(View view){
        this.mCurrentUndoneTaskPosition++;
        moveToNext( );
    }

    private void moveToNext( ){
        if(this.mCurrentUndoneTaskPosition < this.mTodayModel.getUndoneTasks().size()){
            updateTasksCheckMessage( );
            TaskItem taskItem = this.mTodayModel.getUndoneTasks().get(this.mCurrentUndoneTaskPosition);
            this.mTaskInformationTextView.setText(taskItem.getInformation());
            this.mTaskInformationTextView.startAnimation(this.mCheckMessageAnimation);
        }else{
            this.mTaskCheckMessageTextView.setText(R.string.check_task_day_evaluation);
            this.mTaskCheckMessageTextView.startAnimation(this.mCheckMessageAnimation);

            this.mDayEvaluationView.setVisibility(View.VISIBLE);
            ObjectAnimator objectAnimator = ObjectAnimator.ofFloat(this.mDayEvaluationView, "translationY", this.mDayEvaluationView.getHeight(), 0f)
                    .setDuration(500);
            objectAnimator.start();
        }
    }

    private void updateTasksCheckMessage( ){
        int remainNum = this.mTodayModel.getUndoneTasks().size() - this.mCurrentUndoneTaskPosition;
        this.mTaskCheckMessageTextView.setText(this.mTasksCheckMessage.replace("?", Integer.toString(remainNum)));
    }

    @Override
    public void onBackPressed() {

    }
}
