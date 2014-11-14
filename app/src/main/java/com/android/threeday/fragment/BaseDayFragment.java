package com.android.threeday.fragment;

import android.app.Activity;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.format.Time;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TimePicker;
import android.widget.Toast;

import com.android.threeday.activity.AddTaskActivity;
import com.android.threeday.activity.mainActivity.FragmentAttachListener;
import com.android.threeday.activity.mainActivity.FragmentTaskLongClickListener;
import com.android.threeday.activity.mainActivity.TaskOperateListener;
import com.android.threeday.fragment.dialogFragment.TaskEvaluationFragment;
import com.android.threeday.fragment.dialogFragment.TimePickerFragment;
import com.android.threeday.model.BaseDayModel;
import com.android.threeday.model.TaskItem;
import com.android.threeday.util.Util;

/**
 * Created by user on 2014/10/29.
 */
public abstract class BaseDayFragment extends Fragment implements TaskOperateListener{
    protected BaseDayModel mModel;
    protected View mMainLayout;
    protected FragmentTaskLongClickListener mFragmentTaskLongClickListener;
    protected FragmentAttachListener mFragmentAttachListener;

    protected BaseAdapter mTaskDoneGridAdapter;
    protected BaseAdapter mTaskUndoneGridAdapter;

    protected int mTaskLongClickPosition;
    protected boolean mAttach;

    protected BaseDayFragment( ){
        super();
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        this.mAttach = true;
        initData(activity);
        initView(activity);
        setAdapter();
        this.mFragmentTaskLongClickListener = (FragmentTaskLongClickListener) activity;
        this.mFragmentAttachListener = (FragmentAttachListener) activity;
        this.mFragmentAttachListener.onFragmentAttach(this);
    }

    @Override
    public void onDetach() {
        this.mAttach = false;
        super.onDetach();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if(this.mMainLayout == null){
            initView(getActivity());
        }
        if(this.mMainLayout.getParent() != null){
            ((ViewGroup)this.mMainLayout.getParent()).removeView(this.mMainLayout);
        }
        return this.mMainLayout;
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    private void initData(Context context){
        this.mModel = getModel(context);
    }

    protected abstract void initView(Context context);

    protected abstract void setAdapter( );

    protected abstract BaseDayModel getModel(Context context);

    protected abstract int getDayType( );

    @Override
    public void deleteUndoneTask(View view) {
        if(this.mModel.deleteUndoneTask(this.mTaskLongClickPosition)){
            if(this.mTaskUndoneGridAdapter != null){
                this.mTaskUndoneGridAdapter.notifyDataSetChanged();
                Toast.makeText(getActivity(),"success", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public void doneTask(View view) {
        final TaskEvaluationFragment taskEvaluationFragment = new TaskEvaluationFragment();
        taskEvaluationFragment.setEvaluationChoseListener(new TaskEvaluationFragment.EvaluationChoseListener() {
            @Override
            public void onEvaluationChosen(int evaluation) {
                doneTask(evaluation);
                taskEvaluationFragment.dismiss();
            }
        });
        taskEvaluationFragment.show(getActivity().getSupportFragmentManager(), "TaskEvaluation");
    }

    private void doneTask(int evaluation){
        Time time = new Time();
        time.setToNow();
        try {
            if(this.mModel.doneTask(this.mTaskLongClickPosition, time.format2445(), evaluation)){
                if(this.mTaskDoneGridAdapter != null){
                    this.mTaskDoneGridAdapter.notifyDataSetChanged();
                }
                if(this.mTaskUndoneGridAdapter != null){
                    this.mTaskUndoneGridAdapter.notifyDataSetChanged();
                }
                Toast.makeText(getActivity(),"success " + time.format2445(), Toast.LENGTH_SHORT).show();
            }
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
            Toast.makeText(getActivity(),"fail", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void setUndoneTaskRemain(View view) {
        TimePickerFragment timePickerFragment = new TimePickerFragment();
        timePickerFragment.setTimeSetListener(new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                setUndoneTaskRemain(hourOfDay, minute);
            }
        });

        timePickerFragment.show(getActivity().getSupportFragmentManager(), "Time");
    }

    private void setUndoneTaskRemain(int hour, int minute){
        Time time = new Time();
        time.setToNow();
        time.hour = hour;
        time.minute = minute;
        if(this.mModel.setUndoneTaskRemain(this.mTaskLongClickPosition, time.format2445())){
            this.mTaskUndoneGridAdapter.notifyDataSetChanged();
            Toast.makeText(getActivity(),"success " + time.format("YYYYMMDD HHMMSS"), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void cancelUndoneTaskRemain(View view) {
        if(this.mModel.cancelUndoneTaskRemain(this.mTaskLongClickPosition)){
            if(this.mTaskUndoneGridAdapter != null){
                this.mTaskUndoneGridAdapter.notifyDataSetChanged();
                Toast.makeText(getActivity(),"success", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public void changeUndoneTaskRemainTime(View view) {
        TimePickerFragment timePickerFragment = new TimePickerFragment();
        timePickerFragment.setTimeSetListener(new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                changeUndoneTaskRemainTime(hourOfDay, minute);
            }
        });

        timePickerFragment.show(getActivity().getSupportFragmentManager(), "Time");
    }

    private void changeUndoneTaskRemainTime(int hour, int minute){
        Time time = new Time();
        time.setToNow();
        time.hour = hour;
        time.minute = minute;
        if(this.mModel.changeUndoneTaskRemainTime(this.mTaskLongClickPosition, time.format2445())){
            this.mTaskUndoneGridAdapter.notifyDataSetChanged();
            Toast.makeText(getActivity(),"success " + time.format("YYYYMMDD HHMMSS"), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void deleteDoneTask(View view) {
        if(this.mModel.deleteDoneTask(this.mTaskLongClickPosition)){
            if(this.mTaskDoneGridAdapter != null){
                this.mTaskDoneGridAdapter.notifyDataSetChanged();
                Toast.makeText(getActivity(),"success", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public void undoneTask(View view) {
        try {
            if(this.mModel.undoneTask(this.mTaskLongClickPosition)){
                if(this.mTaskDoneGridAdapter != null){
                    this.mTaskDoneGridAdapter.notifyDataSetChanged();
                }
                if(this.mTaskUndoneGridAdapter != null){
                    this.mTaskUndoneGridAdapter.notifyDataSetChanged();
                }
                Toast.makeText(getActivity(),"success", Toast.LENGTH_SHORT).show();
            }
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
            Toast.makeText(getActivity(),"fail", Toast.LENGTH_SHORT).show();
        }
    }

    private boolean addTask(Intent data){
        Time time = new Time();
        time.setToNow();
        TaskItem taskItem = new TaskItem(0, data.getIntExtra(Util.TASK_DAY_TYPE, Util.TYPE_TODAY), time.format2445());
        taskItem.setDone(data.getBooleanExtra(Util.TASK_DONE, false));
        taskItem.setDoneTime(data.getStringExtra(Util.TASK_DONE_TIME));
        taskItem.setRemain(data.getBooleanExtra(Util.TASK_REMAIN, false));
        taskItem.setRemainTime(data.getStringExtra(Util.TASK_REMAIN_TIME));
        taskItem.setInformation(data.getStringExtra(Util.TASK_INFORMATION));
        taskItem.setEvaluation(data.getIntExtra(Util.TASK_EVALUATION, Util.EVALUATION_DEFAULT));
        boolean result = this.mModel.addTask(taskItem);
        if(result){
            if(taskItem.getDone()){
                if(this.mTaskDoneGridAdapter != null){
                    this.mTaskDoneGridAdapter.notifyDataSetChanged();
                }
            }else{
                if(this.mTaskUndoneGridAdapter != null) {
                    this.mTaskUndoneGridAdapter.notifyDataSetChanged();
                }
            }
        }
        return result;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(resultCode == Activity.RESULT_OK){
            if(requestCode == Util.REQUEST_ADD_TASK){
                addTask(data);
            }
        }
    }

    public int getDayEvaluation( ){
        return this.mModel.getDayEvaluation();
    }

    public boolean isAttach( ){
        return this.mAttach;
    }

    protected void startAddTaskActivity(int dayType, boolean done){
        Intent intent = new Intent(getActivity(), AddTaskActivity.class);
        intent.putExtra(Util.TASK_DAY_TYPE, dayType);
        intent.putExtra(Util.TASK_DONE, done);
        startActivityForResult(intent, Util.REQUEST_ADD_TASK);
    }

}
