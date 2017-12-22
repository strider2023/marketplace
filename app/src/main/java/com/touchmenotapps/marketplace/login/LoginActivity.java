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
import android.view.View;
import android.widget.ImageView;

import com.touchmenotapps.marketplace.R;
import com.touchmenotapps.marketplace.SplashActivity;
import com.touchmenotapps.marketplace.common.enums.ServerEvents;
import com.touchmenotapps.marketplace.consumer.ConsumerMainActivity;
import com.touchmenotapps.marketplace.dao.LoginDao;
import com.touchmenotapps.marketplace.framework.interfaces.ServerResponseListener;
import com.touchmenotapps.marketplace.login.threads.UserLoginAsyncTask;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);

        Typeface myTypeface = Typeface.createFromAsset(getAssets(), "fonts/font.ttf");
        splashText.setTypeface(myTypeface);

        loginDao = new LoginDao(this);
    }

    @OnClick(R.id.login_btn)
    public void onLoginButtonClicked() {
        if(userMail.getEditableText().toString().trim().length() > 0) {
            loginDao.setUserMailPhone(userMail.getEditableText().toString().trim());
            new UserLoginAsyncTask(1, this, this)
                    .execute(new JSONObject[]{loginDao.toJSON()});
        }
    }

    @OnClick(R.id.back_btn)
    public void onBackButtonClicked() {
        goPrevious();
    }

    @OnClick(R.id.login_forgot_password)
    public void onForgotPasswordClicked() {
        Intent intent = new Intent(this, ForgotPasswordActivity.class);
        Pair<View, String> p1 = Pair.create((View) splashIcon, "splash");
        Pair<View, String> p2 = Pair.create((View) splashText, "splash");
        ActivityOptionsCompat options = ActivityOptionsCompat.
                makeSceneTransitionAnimation(this, p1, p2);
        startActivity(intent, options.toBundle());
    }

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
        startActivity(new Intent(this, ConsumerMainActivity.class));
        finish();
    }

    @Override
    public void onFaliure(ServerEvents serverEvents, Object object) {
        Snackbar.make(splashText, object.toString(), Snackbar.LENGTH_LONG).show();
    }
}

