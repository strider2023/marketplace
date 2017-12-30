package com.touchmenotapps.marketplace;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Handler;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.util.Pair;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatTextView;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import com.touchmenotapps.marketplace.business.BusinessMainActivity;
import com.touchmenotapps.marketplace.consumer.ConsumerMainActivity;
import com.touchmenotapps.marketplace.framework.persist.AppPreferences;
import com.touchmenotapps.marketplace.login.LoginActivity;
import com.touchmenotapps.marketplace.signup.RegistrationOTPActivity;
import com.touchmenotapps.marketplace.signup.UserSignupActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class SplashActivity extends AppCompatActivity {

    @BindView(R.id.splash_app_name)
    AppCompatTextView splashText;
    @BindView(R.id.splash_app_icon)
    ImageView splashIcon;
    @BindView(R.id.splash_login_btn)
    AppCompatButton splashLogin;
    @BindView(R.id.splash_signup_btn)
    AppCompatButton splashSignup;

    private AppPreferences appPreferences;

    private Animation animFast, animSlow, animVerySlow;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        ButterKnife.bind(this);

        Typeface myTypeface = Typeface.createFromAsset(getAssets(), "fonts/font.ttf");
        splashText.setTypeface(myTypeface);

        appPreferences = new AppPreferences(this);
        animFast = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.float_anim);
        animSlow = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.float_anim_slow);
        animVerySlow = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.float_anim_very_slow);

        findViewById(R.id.cloud_image_1).startAnimation(animVerySlow);
        findViewById(R.id.cloud_image_2).startAnimation(animSlow);
        findViewById(R.id.cloud_image_3).startAnimation(animFast);

        if(appPreferences.isUserLoggedIn()) {
            new Handler().postDelayed(new Runnable() {
                public void run() {
                    launchMainActivity();
                }
            }, 2000);
        } else {
            findViewById(R.id.splash_button_holder).setVisibility(View.VISIBLE);
        }

        /*else if(!appPreferences.isRegisterOTPComplete()) {
            new Handler().postDelayed(new Runnable() {
                public void run() {
                    startActivity(new Intent(SplashActivity.this, RegistrationOTPActivity.class));
                }
            }, 2000);
        }*/
    }

    private void launchMainActivity() {
        Intent intent;
        switch (appPreferences.getUserType()) {
            case BUSINESS:
                intent = new Intent(this, BusinessMainActivity.class);
                break;
            case CONSUMER:
                intent = new Intent(this, ConsumerMainActivity.class);
                break;
            default:
                intent = new Intent(this, RegistrationOTPActivity.class);
                break;
        }
        startActivity(intent);
        finish();
    }

    @OnClick(R.id.splash_login_btn)
    public void onLoginClicked() {
        Intent intent = new Intent(this, LoginActivity.class);
        Pair<View, String> p1 = Pair.create((View) splashIcon, "splash");
        Pair<View, String> p2 = Pair.create((View) splashText, "splash");
        ActivityOptionsCompat options = ActivityOptionsCompat.
                makeSceneTransitionAnimation(this, p1, p2);
        startActivity(intent, options.toBundle());
        finish();
    }

    @OnClick(R.id.splash_signup_btn)
    public void onSignupClicked(){
        Intent intent = new Intent(this, UserSignupActivity.class);
        Pair<View, String> p1 = Pair.create((View) splashIcon, "splash");
        Pair<View, String> p2 = Pair.create((View) splashText, "splash");
        ActivityOptionsCompat options = ActivityOptionsCompat.
                makeSceneTransitionAnimation(this, p1, p2);
        startActivity(intent, options.toBundle());
        finish();
    }

    @OnClick(R.id.splash_login_facebook_btn)
    public void onLoginWithFacebook() {
        //startActivity(new Intent(this, ConsumerMainActivity.class));
        //finish();
    }
}
