package com.touchmenotapps.marketplace.home;

import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.TextView;

import com.touchmenotapps.marketplace.R;
import com.touchmenotapps.marketplace.home.fragments.BookmarksFragment;
import com.touchmenotapps.marketplace.home.fragments.OffersFragment;
import com.touchmenotapps.marketplace.home.fragments.ProfileFragment;
import com.touchmenotapps.marketplace.home.fragments.PurchasesFragment;
import com.touchmenotapps.marketplace.home.fragments.SearchFragment;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.app_name_text)
    TextView titleText;
    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        Typeface myTypeface = Typeface.createFromAsset(getAssets(), "fonts/font.ttf");
        titleText.setTypeface(myTypeface);

        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        /** Start with the offers page **/
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.content, OffersFragment.newInstance())
                .commit();
    }

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_offers:
                    getSupportFragmentManager().beginTransaction()
                            .replace(R.id.content, OffersFragment.newInstance())
                            .commit();
                    return true;
                case R.id.navigation_search:
                    getSupportFragmentManager().beginTransaction()
                            .replace(R.id.content, SearchFragment.newInstance())
                            .commit();
                    return true;
                case R.id.navigation_purchases:
                    getSupportFragmentManager().beginTransaction()
                            .replace(R.id.content, PurchasesFragment.newInstance())
                            .commit();
                    return true;
                case R.id.navigation_bookmarks:
                    getSupportFragmentManager().beginTransaction()
                            .replace(R.id.content, BookmarksFragment.newInstance())
                            .commit();
                    return true;
                case R.id.navigation_profile:
                    getSupportFragmentManager().beginTransaction()
                            .replace(R.id.content, ProfileFragment.newInstance())
                            .commit();
                    return true;
            }
            return false;
        }

    };
}
