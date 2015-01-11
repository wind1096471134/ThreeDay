package com.android.threeday.activity.settingActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.android.threeday.R;
import com.android.threeday.activity.mainActivity.IntroductionActivity;
import com.umeng.analytics.MobclickAgent;
import com.umeng.update.UmengUpdateAgent;

public class AboutActivity extends Activity {
    View mContactMessageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
        this.mContactMessageView = findViewById(R.id.contactMessageView);
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

    public void showWelcomePage(View view){
        Intent intent = new Intent(this, IntroductionActivity.class);
        startActivity(intent);
        overridePendingTransition(R.anim.activity_down_in, android.R.anim.fade_out);
    }

    public void showContactMessage(View view){
        int visibility = this.mContactMessageView.getVisibility() == View.VISIBLE ? View.GONE : View.VISIBLE;
        this.mContactMessageView.setVisibility(visibility);
    }

    public void back(View view){
        exit();
    }

    public void checkUpdate(View view){
        UmengUpdateAgent.forceUpdate(this);
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
