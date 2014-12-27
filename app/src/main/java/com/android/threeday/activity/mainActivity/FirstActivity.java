package com.android.threeday.activity.mainActivity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.format.Time;
import android.util.Log;

import com.android.threeday.R;
import com.android.threeday.activity.lockActivity.LockActivity;
import com.android.threeday.model.addTask.AddTaskLabelModel;
import com.android.threeday.model.setting.LockModel;
import com.android.threeday.model.threeDay.TaskItem;
import com.android.threeday.model.threeDay.TodayModel;
import com.android.threeday.util.Util;

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
        if(this.mSharedPreferences.getBoolean(Util.PREFERENCE_KEY_FIRST_USING, true)){
            setContentView(R.layout.introduction_main);
            setFirstUsingData();
        }else{
            this.mLockModel = new LockModel(this);
            Intent intent;
            if(this.mLockModel.isLockSet()){
                intent = new Intent(this, LockActivity.class);
                intent.putExtra(Util.EXTRA_KEY_LOCK_START_ACTIVITY, MainActivity.class);
                intent.putExtra(Util.EXTRA_KEY_LOCK_ACTIVITY_STATE, LockActivity.STATE_LOCK_IN);
            }else{
                intent = new Intent(this, MainActivity.class);
            }
            startActivity(intent);
            finish();
        }

    }

    private void setFirstUsingData(){
        Time time = new Time();
        time.setToNow();
        long timeMills = time.toMillis(false);
        this.mSharedPreferences.edit().putBoolean(Util.PREFERENCE_KEY_FIRST_USING, false)
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
