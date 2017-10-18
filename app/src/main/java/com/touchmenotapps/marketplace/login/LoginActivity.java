package com.touchmenotapps.marketplace.login;

import android.content.Intent;
import android.graphics.Typeface;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.util.Pair;
import android.support.v7.app.AppCompatActivity;

import android.os.Bundle;
import android.support.v7.widget.AppCompatTextView;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.touchmenotapps.marketplace.R;
import com.touchmenotapps.marketplace.SplashActivity;
import com.touchmenotapps.marketplace.home.MainActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class LoginActivity extends AppCompatActivity {


    @BindView(R.id.splash_app_name)
    AppCompatTextView splashText;
    @BindView(R.id.splash_app_icon)
    ImageView splashIcon;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);

        Typeface myTypeface = Typeface.createFromAsset(getAssets(), "fonts/font.ttf");
        splashText.setTypeface(myTypeface);
    }

    @OnClick(R.id.login_btn)
    public void onLoginButtonClicked() {
        startActivity(new Intent(this, MainActivity.class));
        finish();
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
}

