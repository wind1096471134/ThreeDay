package com.android.threeday.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.android.threeday.R;
import com.android.threeday.util.Util;

/**
 * Created by user on 2014/11/11.
 */
public class AddTaskActivity extends Activity {
    EditText mEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_done_task_main);
        mEditText = (EditText) findViewById(R.id.taskInformationEditText);
    }

    public void addTask(View view){
        Intent intent = new Intent();
        intent.putExtra(Util.TASK_INFORMATION, mEditText.getText().toString());
        intent.putExtra(Util.TASK_DAY_TYPE, getIntent().getIntExtra(Util.TASK_DAY_TYPE, Util.TYPE_TODAY));
        intent.putExtra(Util.TASK_DONE, getIntent().getBooleanExtra(Util.TASK_DONE, false));
        setResult(RESULT_OK, intent);
        finish();
    }
}
