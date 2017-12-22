package com.touchmenotapps.marketplace.signup;

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
import android.widget.LinearLayout;

import com.touchmenotapps.marketplace.R;
import com.touchmenotapps.marketplace.SplashActivity;
import com.touchmenotapps.marketplace.common.enums.ServerEvents;
import com.touchmenotapps.marketplace.common.enums.UserType;
import com.touchmenotapps.marketplace.dao.SignupDao;
import com.touchmenotapps.marketplace.framework.interfaces.ServerResponseListener;
import com.touchmenotapps.marketplace.framework.persist.ApplicationSharedPreferences;
import com.touchmenotapps.marketplace.signup.threads.UserSignupTask;

import org.json.simple.JSONObject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class UserSignupActivity extends AppCompatActivity implements ServerResponseListener {

    @BindView(R.id.splash_app_name)
    AppCompatTextView splashText;
    @BindView(R.id.splash_app_icon)
    ImageView splashIcon;
    @BindView(R.id.signup_select_user_type)
    LinearLayout signupUserType;
    @BindView(R.id.signup_user_info)
    LinearLayout signupUserInfoForm;
    @BindView(R.id.signup_email)
    AppCompatEditText email;
    @BindView(R.id.signup_phone_number)
    AppCompatEditText phoneNumber;

    private SignupDao signupDao;
    private ApplicationSharedPreferences appPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        ButterKnife.bind(this);

        Typeface myTypeface = Typeface.createFromAsset(getAssets(), "fonts/font.ttf");
        splashText.setTypeface(myTypeface);

        appPreferences = new ApplicationSharedPreferences(this);

        signupDao = new SignupDao(this);
    }

    @OnClick(R.id.sigup_as_seller_btn)
    public void signUpAsSeller() {
        signupUserType.setVisibility(View.GONE);
        signupUserInfoForm.setVisibility(View.VISIBLE);
        signupDao.setType(UserType.BUSINESS);
    }

    @OnClick(R.id.sigup_as_buyer_btn)
    public void signUpAsBuyer() {
        signupUserType.setVisibility(View.GONE);
        signupUserInfoForm.setVisibility(View.VISIBLE);
        signupDao.setType(UserType.CONSUMER);
    }

    @OnClick(R.id.signup_btn)
    public void signupClicked() {
        if(phoneNumber.getEditableText().toString().trim().length() > 0) {
            signupDao.setPhoneNumber(phoneNumber.getEditableText().toString().trim());
            new UserSignupTask(1, this, this)
                    .execute(new JSONObject[]{signupDao.toJSON()});
        } else {
            Snackbar.make(splashText, "Please enter the required details.", Snackbar.LENGTH_LONG).show();
        }
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

    @Override
    public void onSuccess(int threadId, Object object) {
        appPreferences.setUserPhoneNumber(phoneNumber.getEditableText().toString().trim());
        startActivity(new Intent(this, RegistrationOTPActivity.class));
        finish();
    }

    @Override
    public void onFaliure(ServerEvents serverEvents, Object object) {
        Snackbar.make(splashText, object.toString(), Snackbar.LENGTH_LONG).show();
    }
}