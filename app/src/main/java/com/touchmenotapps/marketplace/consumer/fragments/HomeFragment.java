package com.touchmenotapps.marketplace.consumer.fragments;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.touchmenotapps.marketplace.R;
import com.touchmenotapps.marketplace.bo.BusinessDao;
import com.touchmenotapps.marketplace.common.DeleteDetailsActivity;
import com.touchmenotapps.marketplace.common.adapters.BusinessAdapter;
import com.touchmenotapps.marketplace.common.interfaces.BusinessSelectedListener;
import com.touchmenotapps.marketplace.consumer.ConsumerMainActivity;
import com.touchmenotapps.marketplace.consumer.SearchActivity;
import com.touchmenotapps.marketplace.consumer.SelectCategoryActivity;
import com.touchmenotapps.marketplace.consumer.adapters.CategoriesAdapter;
import com.touchmenotapps.marketplace.bo.HomeCategoryDao;
import com.touchmenotapps.marketplace.consumer.interfaces.CategorySelectionListener;
import com.touchmenotapps.marketplace.threads.loaders.SearchLoaderTask;
import com.touchmenotapps.marketplace.framework.enums.LoaderID;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.touchmenotapps.marketplace.framework.constants.AppConstants.BUSINESS_BOOKMARKED_TAG;
import static com.touchmenotapps.marketplace.framework.constants.AppConstants.BUSINESS_BOOKMARK_ID_TAG;
import static com.touchmenotapps.marketplace.framework.constants.AppConstants.BUSINESS_CATEGORY_TAG;
import static com.touchmenotapps.marketplace.framework.constants.AppConstants.BUSINESS_ID_TAG;
import static com.touchmenotapps.marketplace.framework.constants.AppConstants.BUSINESS_NAME_TAG;
import static com.touchmenotapps.marketplace.framework.constants.AppConstants.BUSINESS_RATING_TAG;

/**
 * Created by i7 on 17-10-2017.
 */

public class HomeFragment extends Fragment
        implements CategorySelectionListener, LoaderManager.LoaderCallbacks<List<BusinessDao>>,
        BusinessSelectedListener{

    @BindView(R.id.refresh_layout)
    SwipeRefreshLayout refreshLayout;
    @BindView(R.id.details_list)
    RecyclerView detailsList;
    @BindView(R.id.categories_list)
    RecyclerView categoriesList;
    @BindView(R.id.my_business_empty)
    LinearLayout emptyList;
    @BindView(R.id.consumer_location_access)
    LinearLayout locationAccess;

    private View mViewHolder;
    private List<HomeCategoryDao> homeCategoryDaoList = new ArrayList<>();
    private CategoriesAdapter categoriesAdapter;
    private Bundle queryData;
    private BusinessAdapter businessAdapter;
    private HomeFragmentListener homeFragmentListener;

    private double currentLatitude, currentLongitude;
    private String currentCategory = null;

    public interface HomeFragmentListener {
        void onCategorySelected(HomeCategoryDao category);
        void onAllowGPSAccessSelected();
    }

    public static HomeFragment newInstance(double lat, double lng, String category) {
        HomeFragment fragment = new HomeFragment();
        Bundle bundle = new Bundle();
        bundle.putDouble("lat", lat);
        bundle.putDouble("lng", lng);
        bundle.putString("category", category);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            homeFragmentListener = (HomeFragmentListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement FilterListener");
        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        Bundle args = getArguments();
        currentLatitude = args.getDouble("lat", -1d);
        currentLongitude = args.getDouble("lng", -1d);
        currentCategory = args.getString("category");
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mViewHolder = inflater.inflate(R.layout.fragment_consumer_home, container, false);
        ButterKnife.bind(this, mViewHolder);

        homeCategoryDaoList.add(new HomeCategoryDao(R.drawable.ic_restaurants, "Restaurants", "RESTAURANTS_AND_COFFEESHOPS"));
        homeCategoryDaoList.add(new HomeCategoryDao(R.drawable.ic_apparel, "Apparels", "APPARELS"));
        homeCategoryDaoList.add(new HomeCategoryDao(R.drawable.ic_worker, "Assistance", "CLINICS_AND_HOSPITALS"));
        homeCategoryDaoList.add(new HomeCategoryDao(R.drawable.ic_view_all, "View All", "ALL"));
//        homeCategoryDaoList.add(new HomeCategoryDao(R.drawable.ic_disco, "Pubs", "Food"));
//        homeCategoryDaoList.add(new HomeCategoryDao(R.drawable.ic_jwellrey, "Jewellery", "Food"));
//        homeCategoryDaoList.add(new HomeCategoryDao(R.drawable.ic_toys, "Toys", "Food"));
//        homeCategoryDaoList.add(new HomeCategoryDao(R.drawable.ic_travel, "Travel", "Food"));

        categoriesAdapter = new CategoriesAdapter(HomeFragment.this);
        categoriesList.setLayoutManager(new GridLayoutManager(getActivity(), 4));
        categoriesList.setAdapter(categoriesAdapter);
        categoriesAdapter.setData(homeCategoryDaoList);

        refreshLayout.setRefreshing(false);
        businessAdapter = new BusinessAdapter(this);
        detailsList.setLayoutManager(new LinearLayoutManager(getContext()));
        detailsList.setAdapter(businessAdapter);

        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                fetchData(currentLatitude, currentLatitude);
            }
        });

        return mViewHolder;
    }

    @Override
    public void onResume() {
        super.onResume();
        if(currentLatitude != -1l && currentLongitude !=-1d) {
            locationAccess.setVisibility(View.GONE);
            fetchData(currentLatitude, currentLongitude);
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.home_menu, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.home_search:
                Intent intent = new Intent(getActivity(), SearchActivity.class);
                intent.putExtra("lat", currentLatitude);
                intent.putExtra("lng", currentLongitude);
                startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @OnClick(R.id.allow_gps_access)
    public void onAllowGPS() {
        homeFragmentListener.onAllowGPSAccessSelected();
    }

    @Override
    public void onCategorySelected(HomeCategoryDao homeCategoryDao) {
        if(homeCategoryDao.getKeyword().equalsIgnoreCase("all")) {
            startActivityForResult(new Intent(getActivity(), SelectCategoryActivity.class), ConsumerMainActivity.GET_CATEGORY);
        } else {
            homeFragmentListener.onCategorySelected(homeCategoryDao);
        }
    }

    @Override
    public Loader<List<BusinessDao>> onCreateLoader(int id, Bundle args) {
        refreshLayout.setRefreshing(true);
        return new SearchLoaderTask(getActivity(), queryData);
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
        Intent intent = new Intent(getActivity(), DeleteDetailsActivity.class);
        intent.putExtra(BUSINESS_ID_TAG, businessDao.getId());
        intent.putExtra(BUSINESS_NAME_TAG, businessDao.getName());
        intent.putExtra(BUSINESS_CATEGORY_TAG, businessDao.getCategory());
        intent.putExtra(BUSINESS_RATING_TAG, businessDao.getSingleScoreRating());
        intent.putExtra(BUSINESS_BOOKMARKED_TAG, businessDao.isBookmarked());
        intent.putExtra(BUSINESS_BOOKMARK_ID_TAG, businessDao.getBookmarkId());
        startActivity(intent);
    }

    private void fetchData(double lat, double lng) {
        currentLatitude = lat;
        currentLongitude = lng;
        queryData = new Bundle();
        queryData.putDouble("lat", lat);
        queryData.putDouble("lng", lng);
        if(currentCategory != null) {
            queryData.putString("categories", currentCategory);
        } else {
            queryData.putInt("toprated", 1);
        }
        getActivity().getSupportLoaderManager()
                .restartLoader(LoaderID.FETCH_MY_BUSINESS.getValue(), queryData, this).forceLoad();
    }
}
