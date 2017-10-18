package com.touchmenotapps.marketplace.login;

import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatTextView;

import com.touchmenotapps.marketplace.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ForgotPasswordActivity extends AppCompatActivity {

    @BindView(R.id.splash_app_name)
    AppCompatTextView splashText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);
        ButterKnife.bind(this);

        Typeface myTypeface = Typeface.createFromAsset(getAssets(), "fonts/font.ttf");
        splashText.setTypeface(myTypeface);
    }

    @OnClick(R.id.back_btn)
    public void onBackButtonClicked() {
        finish();
    }
}
