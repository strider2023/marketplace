package com.touchmenotapps.marketplace.signup;

import android.content.Intent;
import android.graphics.Typeface;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.util.Pair;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatTextView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.touchmenotapps.marketplace.R;
import com.touchmenotapps.marketplace.SplashActivity;
import com.touchmenotapps.marketplace.common.enums.UserType;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SignupActivity extends AppCompatActivity {

    @BindView(R.id.splash_app_name)
    AppCompatTextView splashText;
    @BindView(R.id.splash_app_icon)
    ImageView splashIcon;
    @BindView(R.id.signup_select_user_type)
    LinearLayout signupUserType;
    @BindView(R.id.signup_user_info)
    LinearLayout signupUserInfoForm;

    private UserType selectedUserType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        ButterKnife.bind(this);

        Typeface myTypeface = Typeface.createFromAsset(getAssets(), "fonts/font.ttf");
        splashText.setTypeface(myTypeface);
    }

    @OnClick(R.id.sigup_as_seller_btn)
    public void signUpAsSeller() {
        signupUserType.setVisibility(View.GONE);
        signupUserInfoForm.setVisibility(View.VISIBLE);
        selectedUserType = UserType.BUSINESS;
    }

    @OnClick(R.id.sigup_as_buyer_btn)
    public void signUpAsBuyer() {
        signupUserType.setVisibility(View.GONE);
        signupUserInfoForm.setVisibility(View.VISIBLE);
        selectedUserType = UserType.CONSUMER;
    }

    @OnClick(R.id.signup_btn)
    public void signupClicked() {
        startActivity(new Intent(this, RegistrationOTPActivity.class));
        finish();
    }

    @OnClick(R.id.back_btn)
    public void onBackButtonClicked() {
        goPrevious();
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
