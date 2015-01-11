package com.android.threeday.activity.mainActivity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.format.Time;

import com.android.threeday.R;
import com.android.threeday.activity.lockActivity.LockActivity;
import com.android.threeday.model.addTask.AddTaskLabelModel;
import com.android.threeday.model.setting.LockModel;
import com.android.threeday.model.threeDay.TaskItem;
import com.android.threeday.model.threeDay.TodayModel;
import com.android.threeday.util.Util;
import com.umeng.analytics.MobclickAgent;

/**
 * Created by user on 2014/12/18.
 */
public class FirstActivity extends Activity {
    private LockModel mLockModel;
    private SharedPreferences mSharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.mSharedPreferences = getSharedPreferences(Util.PREFERENCE_NAME, MODE_PRIVATE);

        Intent intent;
        if(this.mSharedPreferences.getBoolean(Util.PREFERENCE_KEY_FIRST_USING, true)){
            setFirstUsingData();
            intent = new Intent(this, IntroductionActivity.class);
        }else{
            this.mLockModel = new LockModel(this);
            if(this.mLockModel.isLockSet()){
                intent = new Intent(this, LockActivity.class);
                intent.putExtra(Util.EXTRA_KEY_LOCK_START_ACTIVITY, MainActivity.class);
                intent.putExtra(Util.EXTRA_KEY_LOCK_ACTIVITY_STATE, LockActivity.STATE_LOCK_IN);
            }else{
                intent = new Intent(this, MainActivity.class);
            }
        }
        startActivity(intent);
        finish();
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

    private void setFirstUsingData(){
        Time time = new Time();
        time.setToNow();
        long timeMills = time.toMillis(false);
        this.mSharedPreferences.edit().putBoolean(Util.PREFERENCE_KEY_MORNING_REMAIN, Util.DEFAULT_MORNING_REMAIN)
                .putInt(Util.PREFERENCE_KEY_MORNING_REMAIN_TIME_HOUR, Util.MORNING_REMAIN_TIME_DEFAULT_HOUR)
                .putInt(Util.PREFERENCE_KEY_MORNING_REMAIN_TIME_MINUTE, Util.MORNING_REMAIN_TIME_DEFAULT_MINUTE)
                .putBoolean(Util.PREFERENCE_KEY_EVENING_CHECK, Util.DEFAULT_EVENING_CHECK)
                .putInt(Util.PREFERENCE_KEY_EVENING_CHECK_TIME_HOUR, Util.EVENING_CHECK_TIME_DEFAULT_HOUR)
                .putInt(Util.PREFERENCE_KEY_EVENING_CHECK_TIME_MINUTE, Util.EVENING_CHECK_TIME_DEFAULT_MINUTE)
                .putBoolean(Util.PREFERENCE_KEY_FIRST_USING, false)
                .putLong(Util.PREFERENCE_KEY_REAL_DAY_TIME_1, timeMills)
                .putLong(Util.PREFERENCE_KEY_REAL_DAY_TIME_2, timeMills)
                .putLong(Util.PREFERENCE_KEY_LAST_IN_DAY_TIME, timeMills).commit();

        /*add default tasks*/
        TodayModel todayModel = new TodayModel(this);
        String[] arrays = getResources().getStringArray(R.array.first_using_task_information);
        for(String string : arrays){
            TaskItem taskItem = new TaskItem(0, Util.TYPE_TODAY, time.format2445());
            taskItem.setInformation(string);
            todayModel.addTask(taskItem);
        }

        /*add default labels*/
        AddTaskLabelModel addTaskLabelModel = new AddTaskLabelModel(this);
        arrays = getResources().getStringArray(R.array.add_label_default_items);
        for(String string : arrays){
            addTaskLabelModel.addLabel(string);
        }
    }

}
