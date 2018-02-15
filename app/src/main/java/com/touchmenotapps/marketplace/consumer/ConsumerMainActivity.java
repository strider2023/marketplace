package com.touchmenotapps.marketplace.consumer;

import android.Manifest;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.graphics.Typeface;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Parcelable;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.internal.BottomNavigationItemView;
import android.support.design.internal.BottomNavigationMenuView;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.touchmenotapps.marketplace.R;
import com.touchmenotapps.marketplace.bo.CategoryDao;
import com.touchmenotapps.marketplace.bo.HomeCategoryDao;
import com.touchmenotapps.marketplace.bo.LocationDao;
import com.touchmenotapps.marketplace.common.fragment.BusinessOffersFragment;
import com.touchmenotapps.marketplace.common.fragment.ProfileFragment;
import com.touchmenotapps.marketplace.consumer.fragments.BookmarksFragment;
import com.touchmenotapps.marketplace.consumer.fragments.HomeFragment;
import com.touchmenotapps.marketplace.framework.PermissionsUtil;
import com.touchmenotapps.marketplace.framework.constants.AppConstants;
import com.touchmenotapps.marketplace.framework.enums.ServerEvents;
import com.touchmenotapps.marketplace.framework.interfaces.ServerResponseListener;
import com.touchmenotapps.marketplace.threads.asynctasks.FeedCountTask;

import org.json.simple.JSONObject;

import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static android.Manifest.permission.ACCESS_FINE_LOCATION;

public class ConsumerMainActivity extends AppCompatActivity
        implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, LocationListener,
        BottomNavigationView.OnNavigationItemSelectedListener, ServerResponseListener, HomeFragment.HomeFragmentListener {

    private final int GET_LOCATION = 1;
    public static final int GET_CATEGORY = 2;

    @BindView(R.id.app_name_text)
    TextView titleText;
    @BindView(R.id.app_location_text)
    AppCompatTextView locationText;
    @BindView(R.id.app_category_text)
    AppCompatTextView categoryText;
    @BindView(R.id.toolbar_contianer)
    LinearLayout toolbarContainer;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.navigation)
    BottomNavigationView navigation;

    private PermissionsUtil permissionsUtil;
    private boolean isLocationAccessible, isUserDefined;

    //Define a request code to send to Google Play services
    private final static int CONNECTION_FAILURE_RESOLUTION_REQUEST = 9000;
    private GoogleApiClient mGoogleApiClient;
    private LocationRequest mLocationRequest;
    private double currentLatitude = -1d, currentLongitude = -1d;
    private Geocoder geoCoder;
    private LocationManager locationManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_consumer_main);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        Typeface myTypeface = Typeface.createFromAsset(getAssets(), "fonts/font.ttf");
        titleText.setTypeface(myTypeface);

        navigation.setOnNavigationItemSelectedListener(this);
        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

        permissionsUtil = new PermissionsUtil(this);
        geoCoder = new Geocoder(this, Locale.getDefault());
    }

    @OnClick(R.id.app_location_text)
    public void onLocationSelectClicked() {
        startActivityForResult(new Intent(this, SearchLocationActivity.class), GET_LOCATION);
    }

    @Override
    public void onResume() {
        super.onResume();
        if(navigation.getSelectedItemId() == R.id.navigation_home) {
            onActivityInit();
        }
        new FeedCountTask(1, this, this, false)
                .execute(new JSONObject[]{});
        if (mGoogleApiClient != null) {
            mGoogleApiClient.connect();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if (mGoogleApiClient != null) {
            if (mGoogleApiClient.isConnected()) {
                LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
                mGoogleApiClient.disconnect();
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        if (requestCode == AppConstants.REQUEST_ACCESS_LOCATION) {
            if (grantResults.length == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                isLocationAccessible = true;
                if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                    initGeo();
                } else {
                    final Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                    startActivity(intent);
                }
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case GET_LOCATION:
                    LocationDao locationDao = data.getParcelableExtra("selectedLocation");
                    if (locationDao != null) {
                        currentLatitude = locationDao.getLatitude();
                        currentLongitude = locationDao.getLongitude();
                        locationText.setText(locationDao.getCity());
                        isUserDefined = true;
                        getSupportFragmentManager().beginTransaction()
                                .replace(R.id.content, HomeFragment.newInstance(currentLatitude, currentLongitude, null))
                                .commit();
                    }
                    break;
                default: //TODO Check Bug as to why the set ID is not returned
                    CategoryDao categoryDao = data.getParcelableExtra("selectedCategory");
                    categoryText.setText(categoryDao.getDescription());
                    getSupportFragmentManager().beginTransaction()
                            .replace(R.id.content,
                                    HomeFragment.newInstance(
                                            currentLatitude,
                                            currentLongitude,
                                            categoryDao.getEnumText()))
                            .commit();
                    break;
            }
        }
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        Location location = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
        if (location == null) {
            LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
        } else {
            //If everything went fine lets get latitude and longitude
            currentLatitude = location.getLatitude();
            currentLongitude = location.getLongitude();
            locationText.setText(getCity(location));
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.content, HomeFragment.newInstance(currentLatitude, currentLongitude, null))
                    .commit();
            Log.i(AppConstants.APP_TAG, currentLatitude + ", " + currentLongitude + " - Location");
        }
    }

    @Override
    public void onConnectionSuspended(int i) {
        currentLatitude = -1d;
        currentLongitude = -1d;
        categoryText.setText("Recommended");
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.content, HomeFragment.newInstance(currentLatitude, currentLongitude, null))
                .commit();
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        if (connectionResult.hasResolution()) {
            try {
                // Start an Activity that tries to resolve the error
                connectionResult.startResolutionForResult(this, CONNECTION_FAILURE_RESOLUTION_REQUEST);
            } catch (IntentSender.SendIntentException e) {
                e.printStackTrace();
            }
        } else {
            Log.e("Error", "Location services connection failed with code " + connectionResult.getErrorCode());
        }
    }

    @Override
    public void onLocationChanged(Location location) {
        currentLatitude = location.getLatitude();
        currentLongitude = location.getLongitude();
        Log.i(AppConstants.APP_TAG, currentLatitude + ", " + currentLongitude + " - Location");
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.navigation_home:
                toolbarContainer.setVisibility(View.VISIBLE);
                titleText.setVisibility(View.GONE);
                categoryText.setText("Recommended");
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.content, HomeFragment.newInstance(currentLatitude, currentLongitude, null))
                        .commit();
                return true;
            case R.id.navigation_offers:
                toolbarContainer.setVisibility(View.GONE);
                titleText.setVisibility(View.VISIBLE);
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.content, BusinessOffersFragment.newInstance(-1l))
                        .commit();
                return true;
            case R.id.navigation_bookmarks:
                toolbarContainer.setVisibility(View.GONE);
                titleText.setVisibility(View.VISIBLE);
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.content, BookmarksFragment.newInstance())
                        .commit();
                return true;
            case R.id.navigation_profile:
                toolbarContainer.setVisibility(View.GONE);
                titleText.setVisibility(View.VISIBLE);
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.content, ProfileFragment.newInstance())
                        .commit();
                return true;
        }
        return false;
    }

    @Override
    public void onSuccess(int threadId, Object object) {
        BottomNavigationMenuView bottomNavigationMenuView = (BottomNavigationMenuView) navigation.getChildAt(0);
        View v = bottomNavigationMenuView.getChildAt(1);
        BottomNavigationItemView itemView = (BottomNavigationItemView) v;
        View badgeView = LayoutInflater.from(this).inflate(R.layout.view_badge, bottomNavigationMenuView, false);
        itemView.addView(badgeView);
        TextView badge = badgeView.findViewById(R.id.notifications_badge);
        badge.setText(object.toString());
    }

    @Override
    public void onFaliure(ServerEvents serverEvents, Object object) {

    }

    @Override
    public void onCategorySelected(HomeCategoryDao category) {
        categoryText.setText(category.getCategoryName());
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.content, HomeFragment.newInstance(currentLatitude, currentLongitude, category.getKeyword()))
                .commit();
    }

    @Override
    public void onAllowGPSAccessSelected() {
        //If location permission present switch on gps
        if (isLocationAccessible) {
            final Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
            startActivity(intent);
        } else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions(new String[]{ACCESS_FINE_LOCATION}, AppConstants.REQUEST_ACCESS_LOCATION);
            }
        }
    }
    private void onActivityInit() {
        isLocationAccessible = permissionsUtil.checkLocationPermission(navigation);
        if (isLocationAccessible) {
            //If yes check if gps connection is present and show
            if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                initGeo();
            } else {
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.content, HomeFragment.newInstance(currentLatitude, currentLongitude, null))
                        .commit();
            }
        } else {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.content, HomeFragment.newInstance(currentLatitude, currentLongitude, null))
                    .commit();
        }
    }

    private void initGeo() {
        //Init location builder
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();

        mLocationRequest = LocationRequest.create()
                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                .setInterval(10 * 1000)
                .setFastestInterval(1 * 1000);
    }

    private String getCity(Location location) {
        String result = "";
        try {
            List<Address> list = geoCoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
            if (list != null & list.size() > 0) {
                Address address = list.get(0);
                result = address.getLocality();
                return result;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }
}
