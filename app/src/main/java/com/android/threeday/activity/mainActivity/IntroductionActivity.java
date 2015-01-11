package com.android.threeday.activity.mainActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.TextView;

import com.android.threeday.R;
import com.umeng.analytics.MobclickAgent;

public class IntroductionActivity extends Activity {
    private TextView mInformationTextView;
    private TextView mInformationTextView2;
    private Button mYesButton;
    private Button mNoButton;
    private Button mEnterButton;
    private AnimationSet mMessageAnimation;
    private AnimationSet mMessageAnimation2;
    private AnimationSet mButtonAnimation;

    private final int YESTERDAY_INDEX = 0;
    private final int TODAY_INDEX = 1;
    private final int TOMORROW_INDEX = 2;
    private final int ENTER_INDEX = 3;
    private int mCurrentIndex = YESTERDAY_INDEX;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.introduction_main);
        initView( );
        initData( );
        setData();
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

    private void setData( ){
        int textResId;
        int yesId;
        int noId;
        switch (this.mCurrentIndex){
            case YESTERDAY_INDEX:
                textResId = R.string.introduction_yesterday;
                yesId = R.string.introduction_yesterday_yes;
                noId = R.string.introduction_yesterday_no;
                break;
            case TODAY_INDEX:
                textResId = R.string.introduction_today;
                yesId = R.string.introduction_today_yes;
                noId = R.string.introduction_today_no;
                break;
            case TOMORROW_INDEX:
                textResId = R.string.introduction_tomorrow;
                yesId = R.string.introduction_tomorrow_yes;
                noId = R.string.introduction_tomorrow_no;
                break;
            case ENTER_INDEX:
                textResId = R.string.introduction_final;
                this.mInformationTextView.setText(textResId);
                this.mMessageAnimation.setDuration(800);
                this.mMessageAnimation.setAnimationListener(new Animation.AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation animation) {

                    }

                    @Override
                    public void onAnimationEnd(Animation animation) {
                        mInformationTextView2.setText(R.string.introduction_final_2);
                        mInformationTextView2.startAnimation(mMessageAnimation2);
                        mEnterButton.setVisibility(View.VISIBLE);
                        mEnterButton.startAnimation(mButtonAnimation);
                    }

                    @Override
                    public void onAnimationRepeat(Animation animation) {

                    }
                });
                this.mInformationTextView.startAnimation(this.mMessageAnimation);
                this.mYesButton.setVisibility(View.GONE);
                this.mNoButton.setVisibility(View.GONE);
                return;
            default:
                return;

        }
        this.mInformationTextView.setText(textResId);
        this.mYesButton.setText(yesId);
        this.mNoButton.setText(noId);
        if(this.mCurrentIndex != YESTERDAY_INDEX){
            this.mYesButton.startAnimation(this.mButtonAnimation);
            this.mNoButton.startAnimation(this.mButtonAnimation);
            this.mInformationTextView.startAnimation(this.mMessageAnimation);
        }
    }

    private void initView( ){
        this.mInformationTextView = (TextView) findViewById(R.id.informationTextView);
        this.mInformationTextView2 = (TextView) findViewById(R.id.informationTextView2);
        this.mYesButton = (Button) findViewById(R.id.yesButton);
        this.mNoButton = (Button) findViewById(R.id.noButton);
        this.mEnterButton = (Button) findViewById(R.id.enterButton);
    }

    private void initData( ){
        this.mMessageAnimation = new AnimationSet(false);
        TranslateAnimation translateAnimation = new TranslateAnimation(Animation.RELATIVE_TO_SELF, 0, Animation.RELATIVE_TO_SELF, 0,
                Animation.RELATIVE_TO_SELF, -0.3f, Animation.RELATIVE_TO_SELF, 0f);
        AlphaAnimation alphaAnimation = new AlphaAnimation(0f, 1f);
        this.mMessageAnimation.addAnimation(translateAnimation);
        this.mMessageAnimation.addAnimation(alphaAnimation);
        this.mMessageAnimation.setDuration(500);
        this.mMessageAnimation.setInterpolator(new DecelerateInterpolator());

        this.mMessageAnimation2 = new AnimationSet(false);
        translateAnimation = new TranslateAnimation(Animation.RELATIVE_TO_SELF, 0, Animation.RELATIVE_TO_SELF, 0,
                Animation.RELATIVE_TO_SELF, -0.3f, Animation.RELATIVE_TO_SELF, 0f);
        this.mMessageAnimation2.addAnimation(translateAnimation);
        alphaAnimation = new AlphaAnimation(0f, 1f);
        this.mMessageAnimation2.addAnimation(alphaAnimation);
        this.mMessageAnimation2.setDuration(500);
        this.mMessageAnimation2.setInterpolator(new DecelerateInterpolator());

        this.mButtonAnimation = new AnimationSet(false);
        translateAnimation = new TranslateAnimation(Animation.RELATIVE_TO_SELF, 0, Animation.RELATIVE_TO_SELF, 0,
                Animation.RELATIVE_TO_SELF, 0.3f, Animation.RELATIVE_TO_SELF, 0f);
        this.mButtonAnimation.addAnimation(translateAnimation);
        alphaAnimation = new AlphaAnimation(0f, 1f);
        this.mButtonAnimation.addAnimation(alphaAnimation);
        this.mButtonAnimation.setDuration(500);
        this.mButtonAnimation.setInterpolator(new DecelerateInterpolator());
    }

    public void clickYes(View view){
        clickButton();
    }

    public void clickNo(View view){
        clickButton();
    }

    private void clickButton( ){
        this.mCurrentIndex++;
        setData();
    }

    public void clickEnter(View view){
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
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
