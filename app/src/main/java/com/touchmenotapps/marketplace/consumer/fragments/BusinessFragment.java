package com.touchmenotapps.marketplace.consumer.fragments;

import android.Manifest;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.touchmenotapps.marketplace.R;
import com.touchmenotapps.marketplace.bo.BusinessDao;
import com.touchmenotapps.marketplace.common.BusinessDetailsActivity;
import com.touchmenotapps.marketplace.common.adapters.BusinessAdapter;
import com.touchmenotapps.marketplace.common.interfaces.BusinessSelectedListener;
import com.touchmenotapps.marketplace.consumer.adapters.BookmarksAdapter;
import com.touchmenotapps.marketplace.consumer.adapters.CategoriesAdapter;
import com.touchmenotapps.marketplace.consumer.dao.CategoryDAO;
import com.touchmenotapps.marketplace.consumer.interfaces.BookmarkSelectionListener;
import com.touchmenotapps.marketplace.consumer.interfaces.CategorySelectionListener;
import com.touchmenotapps.marketplace.consumer.loaders.SearchLoaderTask;
import com.touchmenotapps.marketplace.framework.PermissionsUtil;
import com.touchmenotapps.marketplace.framework.constants.AppConstants;
import com.touchmenotapps.marketplace.framework.enums.LoaderID;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.touchmenotapps.marketplace.common.BusinessDetailsActivity.SELECTED_BUSINESS_ID;
import static com.touchmenotapps.marketplace.common.BusinessDetailsActivity.SELECTED_BUSINESS_NAME;

/**
 * Created by i7 on 17-10-2017.
 */

public class BusinessFragment extends Fragment
        implements CategorySelectionListener, LoaderManager.LoaderCallbacks<List<BusinessDao>>,
        BusinessSelectedListener, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, LocationListener {

    @BindView(R.id.business_refresh_layout)
    SwipeRefreshLayout refreshLayout;
    @BindView(R.id.categories_list)
    RecyclerView categoriesList;
    @BindView(R.id.offers_list)
    RecyclerView businessList;
    @BindView(R.id.offers_location_access)
    LinearLayout emptyList;

    private View mViewHolder;
    private List<CategoryDAO> categoryDAOList = new ArrayList<>();
    private GridLayoutManager gridLayoutManager;
    private CategoriesAdapter categoriesAdapter;
    private Bundle queryData;
    private BusinessAdapter businessAdapter;
    private LinearLayoutManager linearLayoutManager;

    //Define a request code to send to Google Play services
    private final static int CONNECTION_FAILURE_RESOLUTION_REQUEST = 9000;
    private GoogleApiClient mGoogleApiClient;
    private LocationRequest mLocationRequest;
    private double currentLatitude;
    private double currentLongitude;

    public static BusinessFragment newInstance() {
        BusinessFragment fragment = new BusinessFragment();
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mViewHolder = inflater.inflate(R.layout.fragment_businesses, container, false);
        ButterKnife.bind(this, mViewHolder);

        categoryDAOList.add(new CategoryDAO(R.drawable.ic_restaurants, "Restaurants", "RESTAURANTS_AND_COFFEESHOPS"));
        categoryDAOList.add(new CategoryDAO(R.drawable.ic_apparel, "Apparels", "APPARELS"));
        categoryDAOList.add(new CategoryDAO(R.drawable.ic_worker, "Assistance", "CLINICS_AND_HOSPITALS"));
        categoryDAOList.add(new CategoryDAO(R.drawable.ic_view_all, "View All", "Food"));
//        categoryDAOList.add(new CategoryDAO(R.drawable.ic_disco, "Pubs", "Food"));
//        categoryDAOList.add(new CategoryDAO(R.drawable.ic_jwellrey, "Jewellery", "Food"));
//        categoryDAOList.add(new CategoryDAO(R.drawable.ic_toys, "Toys", "Food"));
//        categoryDAOList.add(new CategoryDAO(R.drawable.ic_travel, "Travel", "Food"));


        categoriesAdapter = new CategoriesAdapter(BusinessFragment.this);
        gridLayoutManager = new GridLayoutManager(getActivity(), 4);
        categoriesList.setLayoutManager(gridLayoutManager);
        categoriesList.setAdapter(categoriesAdapter);
        categoriesAdapter.setData(categoryDAOList);

        refreshLayout.setRefreshing(false);
        businessAdapter = new BusinessAdapter(this);
        linearLayoutManager = new LinearLayoutManager(getContext());
        businessList.setLayoutManager(linearLayoutManager);
        businessList.setAdapter(businessAdapter);

        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                queryData = new Bundle();
                queryData.putDouble("lat", 0);//currentLatitude);
                queryData.putDouble("lng", 0);//currentLongitude);
                queryData.putString("categories", "HEALTHCARE");
                getActivity().getSupportLoaderManager()
                        .initLoader(LoaderID.FETCH_MY_BUSINESS.getValue(), queryData, BusinessFragment.this).forceLoad();
            }
        });

        mGoogleApiClient = new GoogleApiClient.Builder(getActivity())
                // The next two lines tell the new client that “this” current class will handle connection stuff
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                //fourth line adds the LocationServices API endpoint from GooglePlayServices
                .addApi(LocationServices.API)
                .build();

        // Create the LocationRequest object
        mLocationRequest = LocationRequest.create()
                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                .setInterval(10 * 1000)        // 10 seconds, in milliseconds
                .setFastestInterval(1 * 1000); // 1 second, in milliseconds

        return mViewHolder;
    }

    @Override
    public void onResume() {
        super.onResume();
        mGoogleApiClient.connect();
        queryData = new Bundle();
        queryData.putDouble("lat", 0);
        queryData.putDouble("lng", 0);
        queryData.putString("categories", "HEALTHCARE");
        getActivity().getSupportLoaderManager()
                .initLoader(LoaderID.FETCH_MY_BUSINESS.getValue(), queryData, BusinessFragment.this).forceLoad();
    }

    @Override
    public void onPause() {
        super.onPause();
        //Disconnect from API onPause()
        if (mGoogleApiClient.isConnected()) {
            LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
            mGoogleApiClient.disconnect();
        }
    }

    @Override
    public void onCategorySelected(CategoryDAO categoryDAO) {
        queryData = new Bundle();
        queryData.putDouble("lat", currentLatitude);
        queryData.putDouble("lng", currentLongitude);
        queryData.putString("categories", categoryDAO.getKeyword());
        getActivity().getSupportLoaderManager()
                .initLoader(LoaderID.FETCH_MY_BUSINESS.getValue(), queryData, BusinessFragment.this).forceLoad();
    }

    @Override
    public Loader<List<BusinessDao>> onCreateLoader(int id, Bundle args) {
        refreshLayout.setRefreshing(true);
        return new SearchLoaderTask(getActivity(), args);
    }

    @Override
    public void onLoadFinished(Loader<List<BusinessDao>> loader, List<BusinessDao> data) {
        refreshLayout.setRefreshing(false);
        if (data.size() > 0) {
            refreshLayout.setVisibility(View.VISIBLE);
            emptyList.setVisibility(View.GONE);
            businessAdapter.setData(data);
        } else {
            refreshLayout.setVisibility(View.GONE);
            emptyList.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onLoaderReset(Loader<List<BusinessDao>> loader) {
        refreshLayout.setRefreshing(false);
    }

    @Override
    public void onBusinessSelected(BusinessDao businessDao) {
        Intent intent = new Intent(getActivity(), BusinessDetailsActivity.class);
        intent.putExtra(SELECTED_BUSINESS_ID, businessDao.getId());
        intent.putExtra(SELECTED_BUSINESS_NAME, businessDao.getName());
        startActivity(intent);
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION)
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
            /*queryData = new Bundle();
            queryData.putDouble("lat", currentLatitude);
            queryData.putDouble("lng", currentLongitude);
            queryData.putString("categories", "HEALTHCARE");
            getActivity().getSupportLoaderManager()
                    .initLoader(LoaderID.FETCH_MY_BUSINESS.getValue(), queryData, this).forceLoad();*/
            Log.i(AppConstants.APP_TAG, currentLatitude + ", " + currentLongitude + " - Location");
        }
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
         /*
             * Google Play services can resolve some errors it detects.
             * If the error has a resolution, try sending an Intent to
             * start a Google Play services activity that can resolve
             * error.
             */
        if (connectionResult.hasResolution()) {
            try {
                // Start an Activity that tries to resolve the error
                connectionResult.startResolutionForResult(getActivity(), CONNECTION_FAILURE_RESOLUTION_REQUEST);
                    /*
                     * Thrown if Google Play services canceled the original
                     * PendingIntent
                     */
            } catch (IntentSender.SendIntentException e) {
                e.printStackTrace();
            }
        } else {
            /*queryData = new Bundle();
            queryData.putDouble("lat", 0);
            queryData.putDouble("lng", 0);
            queryData.putString("categories", "HEALTHCARE");
            getActivity().getSupportLoaderManager()
                    .initLoader(LoaderID.FETCH_MY_BUSINESS.getValue(), queryData, this).forceLoad();*/
            Log.e("Error", "Location services connection failed with code " + connectionResult.getErrorCode());
        }
    }

    @Override
    public void onLocationChanged(Location location) {
        currentLatitude = location.getLatitude();
        currentLongitude = location.getLongitude();
        Log.i(AppConstants.APP_TAG, currentLatitude + ", " + currentLongitude + " - Location");
    }
}
