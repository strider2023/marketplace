package com.touchmenotapps.marketplace.business;

import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.TextView;

import com.touchmenotapps.marketplace.R;
import com.touchmenotapps.marketplace.common.fragment.BusinessFeedFragment;
import com.touchmenotapps.marketplace.common.fragment.ProfileFragment;
import com.touchmenotapps.marketplace.business.fragments.MyBusinessesFragment;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by i7 on 22-12-2017.
 */

public class BusinessMainActivity extends AppCompatActivity {

    @BindView(R.id.app_name_text)
    TextView titleText;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.business_navigation)
    BottomNavigationView navigation;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_business_main);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        Typeface myTypeface = Typeface.createFromAsset(getAssets(), "fonts/font.ttf");
        titleText.setTypeface(myTypeface);

        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        /** Start with the offers page **/
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.business_content, MyBusinessesFragment.newInstance())
                .commit();
    }

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_businesses:
                    getSupportFragmentManager().beginTransaction()
                            .replace(R.id.business_content, MyBusinessesFragment.newInstance())
                            .commit();
                    return true;
                case R.id.navigation_business_feed:
                    getSupportFragmentManager().beginTransaction()
                            .replace(R.id.business_content, BusinessFeedFragment.newInstance(-1l))
                            .commit();
                    return true;
                case R.id.navigation_profile:
                    getSupportFragmentManager().beginTransaction()
                            .replace(R.id.business_content, ProfileFragment.newInstance())
                            .commit();
                    return true;
            }
            return false;
        }

    };
}
