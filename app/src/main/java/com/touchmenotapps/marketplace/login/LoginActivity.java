package com.touchmenotapps.marketplace.login;

import android.content.Intent;
import android.graphics.Typeface;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.util.Pair;
import android.support.v7.app.AppCompatActivity;

import android.os.Bundle;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.AppCompatTextView;
import android.util.Patterns;
import android.view.View;
import android.widget.ImageView;

import com.touchmenotapps.marketplace.R;
import com.touchmenotapps.marketplace.SplashActivity;
import com.touchmenotapps.marketplace.business.BusinessMainActivity;
import com.touchmenotapps.marketplace.framework.enums.ServerEvents;
import com.touchmenotapps.marketplace.consumer.ConsumerMainActivity;
import com.touchmenotapps.marketplace.bo.LoginDao;
import com.touchmenotapps.marketplace.framework.interfaces.ServerResponseListener;
import com.touchmenotapps.marketplace.framework.persist.AppPreferences;
import com.touchmenotapps.marketplace.threads.asynctasks.LoginTask;
import com.touchmenotapps.marketplace.signup.RegistrationOTPActivity;
import com.touchmenotapps.marketplace.signup.SignupActivity;

import org.json.simple.JSONObject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class LoginActivity extends AppCompatActivity implements ServerResponseListener {

    @BindView(R.id.splash_app_name)
    AppCompatTextView splashText;
    @BindView(R.id.splash_app_icon)
    ImageView splashIcon;
    @BindView(R.id.login_email)
    AppCompatEditText userMail;
    @BindView(R.id.login_password)
    AppCompatEditText userPassword;

    private LoginDao loginDao;
    private AppPreferences appPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);

        Typeface myTypeface = Typeface.createFromAsset(getAssets(), "fonts/font.ttf");
        splashText.setTypeface(myTypeface);

        appPreferences = new AppPreferences(this);

        loginDao = new LoginDao(this);
    }

    @OnClick(R.id.login_btn)
    public void onLoginButtonClicked() {
        String phone = userMail.getEditableText().toString().trim();
        if(phone.length() == 10 && Patterns.PHONE.matcher(phone).matches()) {
            loginDao.setUserMailPhone(phone);
            new LoginTask(1, this, this)
                    .execute(new JSONObject[]{loginDao.toJSON()});
        } else {
            Snackbar.make(userMail, R.string.error_phone, Snackbar.LENGTH_LONG).show();
        }
    }

    @OnClick(R.id.back_btn)
    public void onBackButtonClicked() {
        goPrevious();
    }

    @OnClick(R.id.new_user_signup_btn)
    public void onSignupClicked() {
        Intent intent = new Intent(this, SignupActivity.class);
        Pair<View, String> p1 = Pair.create((View) splashIcon, "splash");
        Pair<View, String> p2 = Pair.create((View) splashText, "splash");
        ActivityOptionsCompat options = ActivityOptionsCompat.
                makeSceneTransitionAnimation(this, p1, p2);
        startActivity(intent, options.toBundle());
    }

    /*@OnClick(R.id.login_forgot_password)
    public void onForgotPasswordClicked() {
        Intent intent = new Intent(this, ForgotPasswordActivity.class);
        Pair<View, String> p1 = Pair.create((View) splashIcon, "splash");
        Pair<View, String> p2 = Pair.create((View) splashText, "splash");
        ActivityOptionsCompat options = ActivityOptionsCompat.
                makeSceneTransitionAnimation(this, p1, p2);
        startActivity(intent, options.toBundle());
    }*/

    @Override
    public void onBackPressed() {
        goPrevious();
    }

    private void goPrevious() {
        Intent intent = new Intent(this, SplashActivity.class);
        Pair<View, String> p1 = Pair.create((View) splashIcon, "splash");
        Pair<View, String> p2 = Pair.create((View) splashText, "splash");
        ActivityOptionsCompat options = ActivityOptionsCompat.
                makeSceneTransitionAnimation(this, p1, p2);
        startActivity(intent, options.toBundle());
        finish();
    }

    @Override
    public void onSuccess(int threadId, Object object) {
        appPreferences.setUserPhoneNumber(userMail.getEditableText().toString().trim());
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

    @Override
    public void onFaliure(ServerEvents serverEvents, Object object) {
        Snackbar.make(splashText, object.toString(), Snackbar.LENGTH_LONG).show();
    }
}

