package com.android.threeday.activity.addTaskActivity;

import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.text.format.Time;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.android.threeday.R;
import com.android.threeday.fragment.dialogFragment.AddLabelFragment;
import com.android.threeday.fragment.dialogFragment.TaskEvaluationFragment;
import com.android.threeday.fragment.dialogFragment.TaskRemainFragment;
import com.android.threeday.fragment.dialogFragment.TimePickerFragment;
import com.android.threeday.model.addTask.AddTaskLabelModel;
import com.android.threeday.util.Util;

/**
 * Created by user on 2014/11/11.
 */
public class AddTaskActivity extends FragmentActivity {
    private EditText mInformationEditText;
    private View mBackView;
    private View mCompleteView;
    private View mCancelDeleteView;
    private GridView mLabelGridView;
    private LabelGridAdapter mLabelGridAdapter;
    private AddTaskLabelModel mAddTaskLabelModel;
    private AdapterView.OnItemClickListener mOnItemClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            if(!mLabelGridAdapter.isDeleteLabelsEnable()){
                if(position == 0){
                    addLabel();
                }else{
                    labelSelected(position - 1);
                }
            }
        }
    };
    private AdapterView.OnItemLongClickListener mOnItemLongClickListener = new AdapterView.OnItemLongClickListener() {
        @Override
        public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
            if(position != 0){
                mLabelGridAdapter.notifyDataSetChanged(true);
                mBackView.setVisibility(View.INVISIBLE);
                mCompleteView.setVisibility(View.INVISIBLE);
                mCancelDeleteView.setVisibility(View.VISIBLE);
                mInformationEditText.setEnabled(false);
            }
            return true;
        }
    };

    private boolean mTaskDone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_task_main);
        initData( );
        initView( );
    }

    private void addLabel( ){
        AddLabelFragment addLabelFragment = new AddLabelFragment();
        addLabelFragment.setOnLabelAddListener(new AddLabelFragment.OnLabelAddListener() {
            @Override
            public void onLabelAdd(String labelName) {
                addLabel(labelName);
            }
        });
        addLabelFragment.show(getSupportFragmentManager(), "addLabel");
    }

    private void addLabel(String labelName){
        if(this.mAddTaskLabelModel.addLabel(labelName)){
            this.mLabelGridAdapter.notifyDataSetChanged();
        }
    }

    private void labelSelected(int position){
        String labelName = this.mAddTaskLabelModel.getLabels().get(position).getLabelName();
        this.mInformationEditText.setText(labelName);
    }

    private void initData( ){
        this.mTaskDone = getIntent().getBooleanExtra(Util.TASK_DONE, false);
        this.mAddTaskLabelModel = new AddTaskLabelModel(this);
        this.mLabelGridAdapter = new LabelGridAdapter(this, this.mAddTaskLabelModel.getLabels());
        this.mLabelGridAdapter.setItemHeight(getResources().getDimensionPixelSize(R.dimen.label_grid_item_height));
    }

    private void initView( ){
        TextView titleTextView = (TextView) findViewById(R.id.titleTextView);
        titleTextView.setText(this.mTaskDone ? R.string.task_state_done : R.string.task_state_undone);
        this.mBackView = findViewById(R.id.backButton);
        this.mCompleteView = findViewById(R.id.completeButton);
        this.mCancelDeleteView = findViewById(R.id.cancelDeleteButton);
        this.mInformationEditText = (EditText) findViewById(R.id.informationEditText);
        WordsLimitTextWatcher wordsLimitTextWatcher = new WordsLimitTextWatcher( );
        wordsLimitTextWatcher.setWordsLimit(getResources().getInteger(R.integer.words_limit_num));
        this.mInformationEditText.addTextChangedListener(wordsLimitTextWatcher);
        this.mLabelGridView = (GridView) findViewById(R.id.labelGridView);
        this.mLabelGridView.setOnItemClickListener(this.mOnItemClickListener);
        this.mLabelGridView.setOnItemLongClickListener(this.mOnItemLongClickListener);
        this.mLabelGridView.setAdapter(this.mLabelGridAdapter);
    }

    public void complete(View view){
        if(this.mInformationEditText.getText() != null && !this.mInformationEditText.getText().toString().equals("")){
            if(this.mTaskDone){
                TaskEvaluationFragment taskEvaluationFragment = new TaskEvaluationFragment();
                taskEvaluationFragment.setEvaluationChoseListener(new TaskEvaluationFragment.EvaluationChoseListener() {
                    @Override
                    public void onEvaluationChosen(int evaluation) {
                        addDoneTask(evaluation);
                    }
                });
                taskEvaluationFragment.show(getSupportFragmentManager(), "taskEvaluation");
            }else{
                TaskRemainFragment taskRemainFragment = new TaskRemainFragment();
                taskRemainFragment.setOnTaskRemainListener(new TaskRemainFragment.OnTaskRemainListener() {
                    @Override
                    public void onTaskRemain() {
                        setRemainTime( );
                    }

                    @Override
                    public void onTaskNotRemain() {
                        addUndoneTask(false, null);
                    }
                });
                taskRemainFragment.show(getSupportFragmentManager(), "taskRemain");
            }
        }else{
            Toast.makeText(this, R.string.information_empty, Toast.LENGTH_SHORT).show();
        }
    }

    private void setRemainTime( ){
        TimePickerFragment timePickerFragment = new TimePickerFragment();
        timePickerFragment.setTimeSetListener(new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                Time time = new Time();
                time.setToNow();
                time.hour = hourOfDay;
                time.minute = minute;
                if(getIntent().getIntExtra(Util.TASK_DAY_TYPE, Util.TYPE_TODAY) == Util.TYPE_TOMORROW){
                    time.set(time.toMillis(false) + 24 * 3600 * 1000);
                }
                addUndoneTask(true, time.format2445());
            }
        });
        timePickerFragment.show(getSupportFragmentManager(), "timePicker");
    }

    private void addUndoneTask(boolean toRemain, String remainTime){
        Intent intent = new Intent();
        intent.putExtra(Util.TASK_INFORMATION, mInformationEditText.getText().toString());
        intent.putExtra(Util.TASK_DAY_TYPE, getIntent().getIntExtra(Util.TASK_DAY_TYPE, Util.TYPE_TODAY));
        intent.putExtra(Util.TASK_DONE, false);
        if(toRemain){
            intent.putExtra(Util.TASK_REMAIN, true);
            intent.putExtra(Util.TASK_REMAIN_TIME, remainTime);
        }else{
            intent.putExtra(Util.TASK_REMAIN, false);
        }
        setResult(RESULT_OK, intent);
        finish();
    }

    private void addDoneTask(int evaluation){
        Intent intent = new Intent();
        intent.putExtra(Util.TASK_INFORMATION, mInformationEditText.getText().toString());
        intent.putExtra(Util.TASK_DAY_TYPE, getIntent().getIntExtra(Util.TASK_DAY_TYPE, Util.TYPE_TODAY));
        intent.putExtra(Util.TASK_DONE, true);
        Time time = new Time();
        time.setToNow();
        intent.putExtra(Util.TASK_DONE_TIME, time.format2445());
        intent.putExtra(Util.TASK_EVALUATION, evaluation);
        setResult(RESULT_OK, intent);
        finish();
    }

    public void deleteLabel(View view){
        int position = (Integer) view.getTag() - 1;
        if(this.mAddTaskLabelModel.deleteLabel(position)){
            this.mLabelGridAdapter.notifyDataSetChanged(true);
        }
    }

    public void cancelDelete(View view){
        cancelDeleteLabels();
    }

    private void cancelDeleteLabels( ){
        this.mLabelGridAdapter.notifyDataSetChanged(false);
        this.mBackView.setVisibility(View.VISIBLE);
        this.mCompleteView.setVisibility(View.VISIBLE);
        this.mCancelDeleteView.setVisibility(View.INVISIBLE);
        this.mInformationEditText.setEnabled(true);
    }

    public void back(View view){
        finish();
    }

    @Override
    public void onBackPressed() {
        if(this.mLabelGridAdapter.isDeleteLabelsEnable()){
            cancelDeleteLabels();
        }else{
            super.onBackPressed();
        }
    }
}
