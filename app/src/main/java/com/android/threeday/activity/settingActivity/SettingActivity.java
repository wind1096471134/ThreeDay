package com.android.threeday.activity.settingActivity;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.text.format.Time;
import android.view.View;
import android.widget.TextView;
import android.widget.TimePicker;

import com.android.threeday.R;
import com.android.threeday.fragment.dialogFragment.TimePickerFragment;
import com.android.threeday.model.setting.SettingModel;
import com.android.threeday.service.EveningCheckService;
import com.android.threeday.service.MorningRemainService;
import com.android.threeday.util.Util;
import com.android.threeday.view.CustomSwitch;

/**
 * Created by user on 2014/12/9.
 */
public class SettingActivity extends FragmentActivity {
    private SettingModel mSettingModel;

    private CustomSwitch mMorningSwitch;
    private CustomSwitch mEveningSwitch;
    private View mMorningRemainView;
    private View mEveningCheckView;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.setting_main);
        initData( );
        initView( );
    }

    private void initData( ){
        this.mSettingModel = new SettingModel(this);
    }

    private void initView( ){
        int switchThumbWidth = getResources().getDimensionPixelSize(R.dimen.setting_switch_thumb_width);
        this.mMorningSwitch = (CustomSwitch) findViewById(R.id.morningSwitch);
        this.mMorningSwitch.setCheckBackgroundColor(getResources().getColor(R.color.setting_switch_check_background_color));
        this.mMorningSwitch.setSwitchThumbWidth(switchThumbWidth);
        this.mMorningSwitch.setSwitchThumbBackgroundColor(getResources().getColor(R.color.setting_switch_thumb_color));
        this.mMorningSwitch.setSwitchCheckChangeListener(this.mMorningSwitchChangeListener);
        this.mMorningRemainView = findViewById(R.id.morningRemainView);
        this.mMorningTimeTextView = (TextView) findViewById(R.id.morningTimeTextView);
        this.mMorningTimeTextView.setText(this.mSettingModel.getMorningRemainTime());

        this.mEveningSwitch = (CustomSwitch) findViewById(R.id.eveningSwitch);
        this.mEveningSwitch.setSwitchThumbWidth(switchThumbWidth);
        this.mEveningSwitch.setSwitchThumbBackgroundColor(getResources().getColor(R.color.setting_switch_thumb_color));
        this.mEveningSwitch.setCheckBackgroundColor(getResources().getColor(R.color.setting_switch_check_background_color));
        this.mEveningSwitch.setSwitchCheckChangeListener(this.mEveningSwitchChangeListener);
        this.mEveningCheckView = findViewById(R.id.eveningCheckView);
        this.mEveningCheckTextView = (TextView) findViewById(R.id.eveningTimeTextView);
        this.mEveningCheckTextView.setText(this.mSettingModel.getEveningCheckTime());

        boolean check = this.mSettingModel.isMorningRemain();
        this.mMorningSwitch.setSwitchCheck(check);
        setMorningRemainView(check);
        check = this.mSettingModel.isEveningCheck();
        this.mEveningSwitch.setSwitchCheck(check);
        setEveningCheckView(check);
    }

    private void setMorningRemain(boolean remain){
        setMorningRemainView(remain);
        this.mSettingModel.setMorningRemain(remain);
        if(remain){
            setAlarm(getMorningPendingIntent(), this.mSettingModel.getMorningRemainTimeHour(), this.mSettingModel.getMorningRemainTimeMinute());
        }else{
            cancelAlarm(getMorningPendingIntent());
        }
    }

    private void setMorningRemainView(boolean remain){
        int visible = remain ? View.VISIBLE : View.GONE;
        mMorningRemainView.setVisibility(visible);
    }

    private void setEveningCheckView(boolean check){
        int visible = check ? View.VISIBLE : View.GONE;
        mEveningCheckView.setVisibility(visible);
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

    private void onMorningTimeSet(int hour, int minute){
        this.mSettingModel.setMorningRemainTime(hour, minute);
        this.mMorningTimeTextView.setText(this.mSettingModel.getMorningRemainTime());
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
        long startTime = time.before(now) ? (time.toMillis(false) + SettingModel.ALARM_REPEAT_TIME) : time.toMillis(false);
        alarmManager.setRepeating(AlarmManager.RTC, startTime, SettingModel.ALARM_REPEAT_TIME, pendingIntent);
    }

    private void cancelAlarm(PendingIntent pendingIntent){
        AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
        alarmManager.cancel(pendingIntent);
    }

    private PendingIntent getMorningPendingIntent( ){
        Intent intent = new Intent(this, MorningRemainService.class);
        return PendingIntent.getService(this, Util.MORNING_REMAIN_ALARM_ID, intent, PendingIntent.FLAG_UPDATE_CURRENT);
    }

    private PendingIntent getEveningPendingIntent( ){
        Intent intent = new Intent(this, EveningCheckService.class);
        return PendingIntent.getService(this, Util.EVENING_CHECK_ALARM_ID, intent, PendingIntent.FLAG_UPDATE_CURRENT);
    }

    private void onEveningTimeSet(int hour, int minute){
        this.mSettingModel.setEveningCheckTime(hour, minute);
        this.mEveningCheckTextView.setText(this.mSettingModel.getEveningCheckTime());
        setAlarm(getEveningPendingIntent(), hour, minute);
    }

    public void back(View view){
        finish();
    }
}
