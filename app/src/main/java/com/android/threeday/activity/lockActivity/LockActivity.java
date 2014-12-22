package com.android.threeday.activity.lockActivity;

import android.animation.ObjectAnimator;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.android.threeday.R;
import com.android.threeday.activity.mainActivity.MainActivity;
import com.android.threeday.model.setting.LockModel;
import com.android.threeday.util.Util;
import com.android.threeday.view.LockView;

import java.util.ArrayList;

/**
 * Created by user on 2014/12/17.
 */
public class LockActivity extends Activity{
    public static final int STATE_FIRST_SET = 1;
    public static final int STATE_RESET = 2;
    public static final int STATE_LOCK_IN = 3;

    private LockModel mLockModel;
    private LockView mLockView;
    private TextView mTitleTextView;
    private LockView.OnLockListener mOnLockListener = new LockView.OnLockListener() {
        @Override
        public void onLockSet(ArrayList<Integer> lockNumbers) {
            mState.onLockSet(lockNumbers);
        }
    };

    private LockState mState;
    private ObjectAnimator mTitleTextViewAnimator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.lock_activity_main);
        initView( );
        initData( );
        setLockState(getIntent().getIntExtra(Util.EXTRA_KEY_LOCK_ACTIVITY_STATE, STATE_LOCK_IN));
    }

    private void initData( ){
        this.mLockModel = new LockModel(this);
        int startY = -getResources().getDimensionPixelOffset(R.dimen.lock_view_title_translationY_start);
        this.mTitleTextViewAnimator = ObjectAnimator.ofFloat(this.mTitleTextView, "translationY", startY, 0f)
                .setDuration(500);
    }

    private void setLockState(int state){
        switch (state){
            case STATE_LOCK_IN:
                this.mState = new LockStateLockIn(this.mLockModel.getLockPassword());
                break;
            case STATE_FIRST_SET:
                this.mState = new LockStateFirstSet();
                break;
            case STATE_RESET:
                this.mState = new LockStateReset(this.mLockModel.getLockPassword());
                break;
            default:
                this.mState = new LockStateLockIn(this.mLockModel.getLockPassword());
        }
    }

    private void initView( ){
        this.mLockView = (LockView) findViewById(R.id.lockView);
        this.mLockView.setOnLockListener(this.mOnLockListener);
        this.mLockView.setNumberViewBackgroundResource(R.drawable.lock_number_bg);
        this.mLockView.setDrawLineColor(Color.BLUE);
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int size = (int) (displayMetrics.widthPixels * 0.9);
        this.mLockView.setLayoutParams(new FrameLayout.LayoutParams(size, size, Gravity.CENTER_HORIZONTAL | Gravity.BOTTOM));
        this.mLockView.setNumberViewMargin(getResources().getDimensionPixelSize(R.dimen.lock_view_number_view_margin));
        this.mLockView.setDensity(displayMetrics.density);

        this.mTitleTextView = (TextView) findViewById(R.id.titleTextView);
    }

    @Override
    public void onBackPressed() {
        this.mState.onBackPressed( );
    }

    private void exit( ){
        finish();
        overridePendingTransition(android.R.anim.fade_in, R.anim.activity_up_out);
    }

    private abstract class LockState{
        protected abstract void onLockSet(ArrayList<Integer> lockNumbers);

        protected String getPasswordString(ArrayList<Integer> lockNumbers){
            StringBuilder password = new StringBuilder(lockNumbers.size());
            for(Integer integer : lockNumbers){
                password.append(Integer.toString(integer));
            }
            return password.toString();
        }

        protected void setTitleText(int textId){
            mTitleTextView.setText(textId);
            mTitleTextViewAnimator.start();
        }

        protected abstract void onBackPressed( );
    }

    private class LockStateFirstSet extends LockState{
        private String mPassword;

        private LockStateFirstSet( ){
            super();
            mTitleTextView.setText(R.string.input_code);
        }

        @Override
        protected void onLockSet(ArrayList<Integer> lockNumbers) {
            if(this.mPassword == null){
                this.mPassword = getPasswordString(lockNumbers);
                mLockView.clear();
                setTitleText(R.string.input_code_again);
            }else{
                if(this.mPassword.equals(getPasswordString(lockNumbers))){
                    mLockModel.setLock(true);
                    mLockModel.setLockPassword(this.mPassword);
                    Intent intent = new Intent();
                    setResult(RESULT_OK, intent);
                    exit();
                }else{
                    mLockView.clear();
                    setTitleText(R.string.input_code_wrong);
                }
            }
        }

        @Override
        protected void onBackPressed() {
            Intent intent = new Intent();
            intent.putExtra(Util.EXTRA_KEY_LOCK_BACK, true);
            setResult(RESULT_CANCELED, intent);
            exit();
        }
    }

    private class LockStateReset extends LockState{
        private String mOldPassword;
        private String mNewPassword;
        private boolean mInputOldPassword;

        private LockStateReset(String oldPassword){
            this.mOldPassword = oldPassword;
            mTitleTextView.setText(R.string.input_old_password);
        }

        @Override
        protected void onLockSet(ArrayList<Integer> lockNumbers) {
            if(this.mInputOldPassword){
                if(this.mNewPassword == null){
                    setTitleText(R.string.input_code_again);
                    this.mNewPassword = getPasswordString(lockNumbers);
                }else{
                    if(this.mNewPassword.equals(getPasswordString(lockNumbers))){
                        mLockModel.setLock(true);
                        mLockModel.setLockPassword(this.mNewPassword);
                        Intent intent = new Intent();
                        setResult(RESULT_OK, intent);
                        exit();
                        return;
                    }else{
                        setTitleText(R.string.input_code_wrong);
                    }
                }
                mLockView.clear();
            }else{
                if(this.mOldPassword.equals(getPasswordString(lockNumbers))){
                    setTitleText(R.string.input_new_password);
                    this.mInputOldPassword = true;
                }else{
                    setTitleText(R.string.input_code_wrong);
                }
                mLockView.clear();
            }
        }

        @Override
        protected void onBackPressed() {
            Intent intent = new Intent();
            intent.putExtra(Util.EXTRA_KEY_LOCK_BACK, true);
            setResult(RESULT_CANCELED, intent);
            exit();
        }

    }

    private class LockStateLockIn extends LockState{
        private String mPassword;

        private LockStateLockIn(String password){
            super();
            this.mPassword = password;
            mTitleTextView.setText(R.string.input_code);
        }

        @Override
        protected void onLockSet(ArrayList<Integer> lockNumbers) {
            if(getPasswordString(lockNumbers).equals(this.mPassword)){
                Class activityClass = (Class) getIntent().getSerializableExtra(Util.EXTRA_KEY_LOCK_START_ACTIVITY);
                Intent intent = getIntent();
                intent.setClass(LockActivity.this, activityClass);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                exit();
            }else{
                mLockView.clear();
                setTitleText(R.string.input_code_wrong);
            }
        }

        @Override
        protected void onBackPressed() {
            System.exit(0);
            finish();
        }

    }
}
