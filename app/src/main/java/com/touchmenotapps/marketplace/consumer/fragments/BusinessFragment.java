package com.touchmenotapps.marketplace.consumer.fragments;

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
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

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
        BusinessSelectedListener {

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

    public static BusinessFragment newInstance() {
        BusinessFragment fragment = new BusinessFragment();
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mViewHolder = inflater.inflate(R.layout.fragment_businesses, container, false);
        ButterKnife.bind(this, mViewHolder);

        categoryDAOList.add(new CategoryDAO(R.drawable.ic_restaurants, "Food", "RESTAURANTS_AND_COFFEESHOPS"));
        categoryDAOList.add(new CategoryDAO(R.drawable.ic_apparel, "Fashion", "APPARELS"));
        categoryDAOList.add(new CategoryDAO(R.drawable.ic_disco, "Pubs", "Food"));
        categoryDAOList.add(new CategoryDAO(R.drawable.ic_jwellrey, "Jewellery", "Food"));
        categoryDAOList.add(new CategoryDAO(R.drawable.ic_toys, "Toys", "Food"));
        categoryDAOList.add(new CategoryDAO(R.drawable.ic_worker, "Assistance", "CLINICS_AND_HOSPITALS"));
        categoryDAOList.add(new CategoryDAO(R.drawable.ic_travel, "Travel", "Food"));
        categoryDAOList.add(new CategoryDAO(R.drawable.ic_view_all, "View All", "Food"));

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
                queryData.putString("categories", "HEALTHCARE");
                getActivity().getSupportLoaderManager()
                        .initLoader(LoaderID.FETCH_MY_BUSINESS.getValue(), queryData, BusinessFragment.this).forceLoad();
            }
        });

        return mViewHolder;
    }

    @Override
    public void onResume() {
        super.onResume();
        queryData = new Bundle();
        queryData.putString("categories", "HEALTHCARE");
        getActivity().getSupportLoaderManager()
                .initLoader(LoaderID.FETCH_MY_BUSINESS.getValue(), queryData,this).forceLoad();
    }

    @Override
    public void onCategorySelected(CategoryDAO categoryDAO) {
        queryData = new Bundle();
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
        if(data.size() > 0) {
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
}
