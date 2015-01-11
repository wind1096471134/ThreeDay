package com.android.threeday.activity.settingActivity;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.text.format.Time;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.TimePicker;

import com.android.threeday.R;
import com.android.threeday.activity.lockActivity.LockActivity;
import com.android.threeday.fragment.dialogFragment.TimePickerFragment;
import com.android.threeday.model.setting.SettingModel;
import com.android.threeday.service.EveningCheckService;
import com.android.threeday.service.MorningRemainService;
import com.android.threeday.util.Util;
import com.android.threeday.view.CustomSwitch;
import com.umeng.analytics.MobclickAgent;

/**
 * Created by user on 2014/12/9.
 */
public class SettingActivity extends FragmentActivity {
    private SettingModel mSettingModel;

    private CustomSwitch mMorningSwitch;
    private CustomSwitch mEveningSwitch;
    private CustomSwitch mLockSwitch;
    private View mMorningRemainView;
    private View mEveningCheckView;
    private View mLockResetView;
    private TextView mMorningTimeTextView;
    private TextView mEveningCheckTextView;
    private CustomSwitch.SwitchCheckChangeListener mMorningSwitchChangeListener = new CustomSwitch.SwitchCheckChangeListener() {
        @Override
        public void onSwitchCheckChange(boolean check) {
            setMorningRemain(check);
        }
    };
    private CustomSwitch.SwitchCheckChangeListener mEveningSwitchChangeListener = new CustomSwitch.SwitchCheckChangeListener() {
        @Override
        public void onSwitchCheckChange(boolean check) {
            setEveningCheck(check);
        }
    };
    private CustomSwitch.SwitchCheckChangeListener mLockSwitchChangeListener = new CustomSwitch.SwitchCheckChangeListener() {
        @Override
        public void onSwitchCheckChange(boolean check) {
            if(check){
                startLockActivityForFirstSet();
            }else{
                setLockSetView(check);
            }
        }
    };

    private int mSwitchThumbWidth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.setting_main);
        initData( );
        initView( );
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

    private void initData( ){
        this.mSettingModel = new SettingModel(this);
        this.mSwitchThumbWidth = getResources().getDimensionPixelSize(R.dimen.setting_switch_thumb_width);
    }

    private void initView( ){
        this.mMorningSwitch = (CustomSwitch) findViewById(R.id.morningSwitch);
        setCustomSwitch(this.mMorningSwitch);
        this.mMorningRemainView = findViewById(R.id.morningRemainView);
        this.mMorningTimeTextView = (TextView) findViewById(R.id.morningTimeTextView);
        this.mMorningTimeTextView.setText(formatTextViewStringWithTime(this.mMorningTimeTextView.getText().toString(), this.mSettingModel.getMorningRemainTimeText()));

        this.mEveningSwitch = (CustomSwitch) findViewById(R.id.eveningSwitch);
        setCustomSwitch(this.mEveningSwitch);
        this.mEveningCheckView = findViewById(R.id.eveningCheckView);
        this.mEveningCheckTextView = (TextView) findViewById(R.id.eveningTimeTextView);
        this.mEveningCheckTextView.setText(formatTextViewStringWithTime(this.mEveningCheckTextView.getText().toString(), this.mSettingModel.getEveningCheckTimeText()));

        boolean check = this.mSettingModel.isMorningRemain();
        this.mMorningSwitch.setSwitchCheck(check);
        setMorningRemainView(check);
        check = this.mSettingModel.isEveningCheck();
        this.mEveningSwitch.setSwitchCheck(check);
        setEveningCheckView(check);

        this.mLockSwitch = (CustomSwitch) findViewById(R.id.lockSwitch);
        setCustomSwitch(this.mLockSwitch);
        check = this.mSettingModel.isLockSet();
        this.mLockSwitch.setSwitchCheck(check);

        this.mLockResetView = findViewById(R.id.resetLockView);
        setLockSetView(check);

        //not move these before switch.setCheck() above because we don't need to set data at the first time
        this.mEveningSwitch.setSwitchCheckChangeListener(this.mEveningSwitchChangeListener);
        this.mMorningSwitch.setSwitchCheckChangeListener(this.mMorningSwitchChangeListener);
        this.mLockSwitch.setSwitchCheckChangeListener(this.mLockSwitchChangeListener);
    }

    private void setCustomSwitch(CustomSwitch customSwitch){
        customSwitch.setCheckBackgroundColor(getResources().getColor(R.color.setting_switch_check_background_color));
        customSwitch.setSwitchThumbWidth(this.mSwitchThumbWidth);
        customSwitch.setSwitchThumbBackgroundColor(getResources().getColor(R.color.setting_switch_thumb_color));
    }

    private void startLockActivityForFirstSet( ){
        Intent intent = new Intent(this, LockActivity.class);
        intent.putExtra(Util.EXTRA_KEY_LOCK_ACTIVITY_STATE, LockActivity.STATE_FIRST_SET);
        startActivityForResult(intent, Util.REQUEST_FIRST_SET_PASSWORD);
        overridePendingTransition(R.anim.activity_down_in, android.R.anim.fade_out);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(resultCode == RESULT_OK){
            if(requestCode == Util.REQUEST_FIRST_SET_PASSWORD || requestCode == Util.REQUEST_RESET_PASSWORD){
                this.mLockResetView.setVisibility(View.VISIBLE);
            }
        }else if(resultCode == RESULT_CANCELED){
            if(requestCode == Util.REQUEST_FIRST_SET_PASSWORD){
                this.mLockSwitch.setSwitchCheck(false);
            }
        }
    }

    private void setMorningRemain(boolean remain){
        setMorningRemainView(remain);
        this.mSettingModel.setMorningRemain(remain);
        if(remain){
            Log.e("wind", "morning remain");
            setAlarm(getMorningPendingIntent(), this.mSettingModel.getMorningRemainTimeHour(), this.mSettingModel.getMorningRemainTimeMinute());
        }else{
            cancelAlarm(getMorningPendingIntent());
        }
    }

    private void setMorningRemainView(boolean remain){
        int visible = remain ? View.VISIBLE : View.GONE;
        this.mMorningRemainView.setVisibility(visible);
    }

    private void setEveningCheckView(boolean check){
        int visible = check ? View.VISIBLE : View.GONE;
        this.mEveningCheckView.setVisibility(visible);
    }

    private void setLockSetView(boolean set){
        int visible = set ? View.VISIBLE : View.GONE;
        this.mLockResetView.setVisibility(visible);
        this.mSettingModel.setLockSet(set);
    }

    private void setEveningCheck(boolean check){
        setEveningCheckView(check);
        this.mSettingModel.setEveningCheck(check);
        if(check){
            setAlarm(getEveningPendingIntent(), this.mSettingModel.getEveningCheckTimeHour(), this.mSettingModel.getEveningCheckTimeMinute());
        }else{
            cancelAlarm(getEveningPendingIntent());
        }
    }

    public void setMorningRemainTime(View view){
        TimePickerFragment timePickerFragment = new TimePickerFragment();
        timePickerFragment.setInitTime(this.mSettingModel.getMorningRemainTimeHour(), this.mSettingModel.getMorningRemainTimeMinute());
        timePickerFragment.setTimeSetListener(new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                onMorningTimeSet(hourOfDay, minute);
            }
        });
        timePickerFragment.show(getSupportFragmentManager(), "TimePicker");
    }

    public void setEveningCheckTime(View view){
        TimePickerFragment timePickerFragment = new TimePickerFragment();
        timePickerFragment.setInitTime(this.mSettingModel.getEveningCheckTimeHour(), this.mSettingModel.getEveningCheckTimeMinute());
        timePickerFragment.setTimeSetListener(new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                onEveningTimeSet(hourOfDay, minute);
            }
        });
        timePickerFragment.show(getSupportFragmentManager(), "TimePicker");
    }

    public void resetPassword(View view){
        Intent intent = new Intent(this, LockActivity.class);
        intent.putExtra(Util.EXTRA_KEY_LOCK_ACTIVITY_STATE, LockActivity.STATE_RESET);
        startActivityForResult(intent, Util.REQUEST_RESET_PASSWORD);
        overridePendingTransition(R.anim.activity_down_in, android.R.anim.fade_out);
    }

    private void onMorningTimeSet(int hour, int minute){
        this.mSettingModel.setMorningRemainTime(hour, minute);
        this.mMorningTimeTextView.setText(formatTextViewStringWithTime(this.mMorningTimeTextView.getText().toString(), this.mSettingModel.getMorningRemainTimeText()));
        setAlarm(getMorningPendingIntent(), hour, minute);
    }

    private void setAlarm(PendingIntent pendingIntent, int hour, int minute){
        AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
        alarmManager.cancel(pendingIntent);
        Time time = new Time();
        time.setToNow();
        time.hour = hour;
        time.minute = minute;
        Time now = new Time();
        now.setToNow();
        long startTime = time.before(now) ? (time.toMillis(false) + Util.A_DAY_IN_MILLIS) : time.toMillis(false);
        alarmManager.setRepeating(AlarmManager.RTC, startTime, Util.A_DAY_IN_MILLIS, pendingIntent);
    }

    private void cancelAlarm(PendingIntent pendingIntent){
        AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
        alarmManager.cancel(pendingIntent);
    }

    private PendingIntent getMorningPendingIntent( ){
        Intent intent = new Intent(this, MorningRemainService.class);
        return PendingIntent.getService(this, Util.MORNING_REMAIN_PENDING_INTENT_ID, intent, PendingIntent.FLAG_UPDATE_CURRENT);
    }

    private PendingIntent getEveningPendingIntent( ){
        Intent intent = new Intent(this, EveningCheckService.class);
        return PendingIntent.getService(this, Util.EVENING_CHECK_PENDING_INTENT_ID, intent, PendingIntent.FLAG_UPDATE_CURRENT);
    }

    private void onEveningTimeSet(int hour, int minute){
        this.mSettingModel.setEveningCheckTime(hour, minute);
        this.mEveningCheckTextView.setText(formatTextViewStringWithTime(this.mEveningCheckTextView.getText().toString(), this.mSettingModel.getEveningCheckTimeText()));
        setAlarm(getEveningPendingIntent(), hour, minute);
    }

    private String formatTextViewStringWithTime(String textString, String timeString){
        return textString.replaceAll("\\(.*\\)", "(" + timeString + ")");
    }

    public void back(View view){
        exit();
    }

    public void clickAbout(View view){
        Intent intent = new Intent(this, AboutActivity.class);
        startActivity(intent);
        overridePendingTransition(R.anim.activity_down_in, android.R.anim.fade_out);
    }

    @Override
    public void onBackPressed() {
        exit();
    }

    private void exit( ){
        finish();
        overridePendingTransition(android.R.anim.fade_in, R.anim.activity_up_out);
    }
}
