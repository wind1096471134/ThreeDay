package com.android.threeday.activity.settingActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.android.threeday.R;
import com.android.threeday.activity.mainActivity.IntroductionActivity;

public class AboutActivity extends Activity {
    View mContactMessageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
        this.mContactMessageView = findViewById(R.id.contactMessageView);
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

    @Override
    public void onBackPressed() {
        exit();
    }

    private void exit( ){
        finish();
        overridePendingTransition(android.R.anim.fade_in, R.anim.activity_up_out);
    }

}
