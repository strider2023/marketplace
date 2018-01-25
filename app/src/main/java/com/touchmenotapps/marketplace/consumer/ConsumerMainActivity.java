package com.touchmenotapps.marketplace.consumer;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.TextView;

import com.touchmenotapps.marketplace.R;
import com.touchmenotapps.marketplace.consumer.fragments.BookmarksFragment;
import com.touchmenotapps.marketplace.consumer.fragments.BusinessFragment;
import com.touchmenotapps.marketplace.consumer.fragments.ProfileFragment;
import com.touchmenotapps.marketplace.consumer.fragments.FeedFragment;
import com.touchmenotapps.marketplace.framework.PermissionsUtil;
import com.touchmenotapps.marketplace.framework.constants.AppConstants;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ConsumerMainActivity extends AppCompatActivity {

    @BindView(R.id.app_name_text)
    TextView titleText;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.navigation)
    BottomNavigationView navigation;

    private PermissionsUtil permissionsUtil;
    private boolean isLocationAccessible;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_consumer_main);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        Typeface myTypeface = Typeface.createFromAsset(getAssets(), "fonts/font.ttf");
        titleText.setTypeface(myTypeface);

        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        permissionsUtil = new PermissionsUtil(this);
        isLocationAccessible = permissionsUtil.checkLocationPermission(navigation);
        if(isLocationAccessible) {
            /** Start with the offers page **/
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.content, BusinessFragment.newInstance())
                    .commit();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.consumer_main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.navigation_search:
                startActivity(new Intent(this, SearchActivity.class));
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        if (requestCode == AppConstants.REQUEST_ACCESS_LOCATION) {
            if (grantResults.length == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                /** Start with the offers page **/
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.content, BusinessFragment.newInstance())
                        .commit();
            }
        }
    }

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_offers:
                    getSupportFragmentManager().beginTransaction()
                            .replace(R.id.content, BusinessFragment.newInstance())
                            .commit();
                    return true;
                case R.id.navigation_purchases:
                    getSupportFragmentManager().beginTransaction()
                            .replace(R.id.content, FeedFragment.newInstance())
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
