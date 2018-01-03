package com.touchmenotapps.marketplace.onboarding;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.transition.Explode;

import com.touchmenotapps.marketplace.R;
import com.touchmenotapps.marketplace.business.BusinessMainActivity;
import com.touchmenotapps.marketplace.consumer.ConsumerMainActivity;
import com.touchmenotapps.marketplace.framework.enums.UserType;
import com.touchmenotapps.marketplace.framework.persist.AppPreferences;
import com.touchmenotapps.marketplace.onboarding.listeners.IntroOnboardingOnRightOutListener;
import com.touchmenotapps.marketplace.signup.RegistrationOTPActivity;

import java.util.ArrayList;

public class AppIntroActivity extends AppCompatActivity {

    private AppPreferences appPreferences;
    private IntroOnboardingPage scr1, scr2, scr3;
    private ArrayList<IntroOnboardingPage> elements = new ArrayList<>();
    private IntroOnboardingFragment onBoardingFragment;
    private UserType userType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_app_intro);

        appPreferences = new AppPreferences(this);
        userType = appPreferences.getUserType();

        if(userType == UserType.BUSINESS){
            scr1 = new IntroOnboardingPage("Reachability",
                    "Use our platform to reach a large pool of consumers.",
                    Color.parseColor("#4e43f9"), R.drawable.ic_intro_location, R.drawable.ic_point);
            scr2 = new IntroOnboardingPage("Setup Feeds",
                    "Setup feeds for your customers to keep them updated about offers in your store.",
                    Color.parseColor("#7c98fd"), R.drawable.ic_intro_discount, R.drawable.ic_point);
            scr3 = new IntroOnboardingPage("Engage More Customers",
                    "With feeds and precise targeting increase your customer base and retention.",
                    Color.parseColor("#4e43f9"), R.drawable.ic_intro_basket, R.drawable.ic_point);
        } else {
            scr1 = new IntroOnboardingPage("Offers Near You",
                    "The Bazaar helps you to quickly search for offers near you.",
                    Color.parseColor("#4e43f9"), R.drawable.ic_intro_location, R.drawable.ic_point);
            scr2 = new IntroOnboardingPage("Discounts!",
                    "Follow different shops around you and get notified when new offers are posted.",
                    Color.parseColor("#7c98fd"), R.drawable.ic_intro_discount, R.drawable.ic_point);
            scr3 = new IntroOnboardingPage("Go Shopping",
                    "Easily redeem offers by using our apps in-store.",
                    Color.parseColor("#4e43f9"), R.drawable.ic_intro_basket, R.drawable.ic_point);
        }

        elements.add(scr1);
        elements.add(scr2);
        elements.add(scr3);

        onBoardingFragment = IntroOnboardingFragment.newInstance(elements);

        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.add(R.id.intro_fragment_container, onBoardingFragment);
        fragmentTransaction.commit();

        onBoardingFragment.setOnRightOutListener(new IntroOnboardingOnRightOutListener() {
            @Override
            public void onRightOut() {
                Intent loginIntent;
                if(userType == UserType.BUSINESS){
                    loginIntent = new Intent(AppIntroActivity.this, BusinessMainActivity.class);
                } else {
                    loginIntent = new Intent(AppIntroActivity.this, ConsumerMainActivity.class);
                }
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    Explode explode = new Explode();
                    explode.setDuration(500);
                    getWindow().setExitTransition(explode);
                    getWindow().setEnterTransition(explode);
                    ActivityOptionsCompat oc2 = ActivityOptionsCompat.makeSceneTransitionAnimation(AppIntroActivity.this);
                    startActivity(loginIntent, oc2.toBundle());
                } else {
                    startActivity(loginIntent);
                }
                finish();
            }
        });
    }
}
