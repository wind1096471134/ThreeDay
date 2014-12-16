package com.android.threeday.activity.mainActivity;

import android.view.View;

/**
 * Created by user on 2014/11/9.
 */
public interface TaskOperateListener {

    public void deleteUndoneTask(View view);

    public void doneTask(View view);

    public void setUndoneTaskRemain(View view);

    public void cancelUndoneTaskRemain(View view);

    public void changeUndoneTaskRemainTime(View view);

    public void deleteDoneTask(View view);

    public void undoneTask(View view);

    public void checkTasks(View view);

    public void addTasks(View view);
}
