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
import com.touchmenotapps.marketplace.home.MainActivity;
import com.touchmenotapps.marketplace.onboarding.listeners.IntroOnboardingOnRightOutListener;

import java.util.ArrayList;

public class AppIntroActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_app_intro);

        IntroOnboardingPage scr1 = new IntroOnboardingPage("Offers Near You",
                "Flashy helps users to quickly summarize complex topics via flashcards.",
                Color.parseColor("#4e43f9"), R.drawable.ic_intro_location, R.drawable.ic_point);
        IntroOnboardingPage scr2 = new IntroOnboardingPage("Discounts!",
                "We carefully verify all contents so that you can get the best from the app.",
                Color.parseColor("#7c98fd"), R.drawable.ic_intro_discount, R.drawable.ic_point);
        IntroOnboardingPage scr3 = new IntroOnboardingPage("Go Shopping",
                "Page 3 content has not been decided yet, please be patient!",
                Color.parseColor("#4e43f9"), R.drawable.ic_intro_basket, R.drawable.ic_point);

        ArrayList<IntroOnboardingPage> elements = new ArrayList<>();
        elements.add(scr1);
        elements.add(scr2);
        elements.add(scr3);

        IntroOnboardingFragment onBoardingFragment = IntroOnboardingFragment.newInstance(elements);

        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.add(R.id.intro_fragment_container, onBoardingFragment);
        fragmentTransaction.commit();

        onBoardingFragment.setOnRightOutListener(new IntroOnboardingOnRightOutListener() {
            @Override
            public void onRightOut() {
                Intent loginIntent = new Intent(AppIntroActivity.this, MainActivity.class);
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
