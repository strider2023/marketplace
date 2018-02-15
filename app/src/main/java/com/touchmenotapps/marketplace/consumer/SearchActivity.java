package com.touchmenotapps.marketplace.consumer;

import android.content.Intent;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.LinearLayout;

import com.touchmenotapps.marketplace.R;
import com.touchmenotapps.marketplace.bo.BusinessDao;
import com.touchmenotapps.marketplace.common.DeleteDetailsActivity;
import com.touchmenotapps.marketplace.common.adapters.BusinessAdapter;
import com.touchmenotapps.marketplace.common.interfaces.BusinessSelectedListener;
import com.touchmenotapps.marketplace.consumer.fragments.SearchFilterFragment;
import com.touchmenotapps.marketplace.framework.enums.LoaderID;
import com.touchmenotapps.marketplace.threads.loaders.SearchLoaderTask;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.touchmenotapps.marketplace.framework.constants.AppConstants.BUSINESS_CATEGORY_TAG;
import static com.touchmenotapps.marketplace.framework.constants.AppConstants.BUSINESS_ID_TAG;
import static com.touchmenotapps.marketplace.framework.constants.AppConstants.BUSINESS_NAME_TAG;
import static com.touchmenotapps.marketplace.framework.constants.AppConstants.BUSINESS_RATING_TAG;

public class SearchActivity extends AppCompatActivity
        implements LoaderManager.LoaderCallbacks<List<BusinessDao>>, BusinessSelectedListener, SearchFilterFragment.FilterListener {

    @BindView(R.id.search_edittext)
    AppCompatEditText searchText;
    @BindView(R.id.refresh_layout)
    SwipeRefreshLayout refreshLayout;
    @BindView(R.id.details_list)
    RecyclerView detailsList;
    @BindView(R.id.search_empty)
    LinearLayout emptyList;

    private Bundle queryData;
    private BusinessAdapter businessAdapter;
    private LinearLayoutManager linearLayoutManager;
    private String category, subCategory, filterBy, filterOrder;
    private double currentLatitude, currentLongitude;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        ButterKnife.bind(this);

        currentLatitude = getIntent().getDoubleExtra("lat", 0);
        currentLongitude = getIntent().getDoubleExtra("lng", 0);

        searchText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) { }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(charSequence.toString().length() > 2) {
                    searchUserInput();
                }
            }

            @Override
            public void afterTextChanged(Editable editable) { }
        });

        refreshLayout.setRefreshing(false);
        businessAdapter = new BusinessAdapter(this);
        linearLayoutManager = new LinearLayoutManager(this);
        detailsList.setLayoutManager(linearLayoutManager);
        detailsList.setAdapter(businessAdapter);

        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if(searchText.getEditableText().toString().trim().length() > 2) {
                    searchUserInput();
                }
            }
        });
    }

    @OnClick(R.id.filter_business_btn)
    public void onFilterSelected() {
        SearchFilterFragment bottomSheetFragment = SearchFilterFragment.newInstance();
        bottomSheetFragment.show(getSupportFragmentManager(), bottomSheetFragment.getTag());
    }

    @OnClick(R.id.close_search_btn)
    public void onSearchClose() {
        finish();
    }

    @Override
    public Loader<List<BusinessDao>> onCreateLoader(int id, Bundle args) {
        refreshLayout.setRefreshing(true);
        return new SearchLoaderTask(this, args);
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
        Intent intent = new Intent(this, DeleteDetailsActivity.class);
        intent.putExtra(BUSINESS_ID_TAG, businessDao.getId());
        intent.putExtra(BUSINESS_NAME_TAG, businessDao.getName());
        intent.putExtra(BUSINESS_CATEGORY_TAG, businessDao.getCategory());
        intent.putExtra(BUSINESS_RATING_TAG, businessDao.getSingleScoreRating());
        startActivity(intent);
    }

    @Override
    public void onFilterSelected(String category, String subcategory, String filterBy, String filterOrder) {
        this.category = category;
        this.subCategory = subcategory;
        this.filterBy = filterBy;
        this.filterOrder = filterOrder;
    }

    private void searchUserInput() {
        queryData = new Bundle();
        queryData.putDouble("lat", currentLatitude);
        queryData.putDouble("lng", currentLatitude);
        if(subCategory != null ) {
            queryData.putString("categories", subCategory);
        } else {
            queryData.putString("categories", category);
        }
        queryData.putString("sortby", filterBy);
        queryData.putString("order", filterOrder);
        queryData.putString("name", searchText.getEditableText().toString().trim());
        getSupportLoaderManager()
                .restartLoader(LoaderID.FETCH_BUSINESS_SEARCH.getValue(), queryData, SearchActivity.this).forceLoad();
    }
}
