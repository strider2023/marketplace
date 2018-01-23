package com.touchmenotapps.marketplace.consumer.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.touchmenotapps.marketplace.R;
import com.touchmenotapps.marketplace.bo.BusinessDao;
import com.touchmenotapps.marketplace.common.BusinessDetailsActivity;
import com.touchmenotapps.marketplace.common.adapters.BusinessAdapter;
import com.touchmenotapps.marketplace.common.interfaces.BusinessSelectedListener;
import com.touchmenotapps.marketplace.threads.loaders.SearchLoaderTask;
import com.touchmenotapps.marketplace.framework.enums.LoaderID;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.touchmenotapps.marketplace.framework.constants.AppConstants.BUSINESS_ID_TAG;
import static com.touchmenotapps.marketplace.framework.constants.AppConstants.BUSINESS_NAME_TAG;

/**
 * Created by i7 on 18-10-2017.
 */

public class SearchFragment extends Fragment
        implements LoaderManager.LoaderCallbacks<List<BusinessDao>>, BusinessSelectedListener {

    private View mViewHolder;

    @BindView(R.id.search_edittext)
    AppCompatEditText searchText;
    @BindView(R.id.search_clear_input)
    ImageView clearInput;
    @BindView(R.id.refresh_layout)
    SwipeRefreshLayout refreshLayout;
    @BindView(R.id.details_list)
    RecyclerView detailsList;
    @BindView(R.id.search_empty)
    LinearLayout emptyList;

    private Bundle queryData;
    private BusinessAdapter businessAdapter;
    private LinearLayoutManager linearLayoutManager;

    public static SearchFragment newInstance() {
        SearchFragment fragment = new SearchFragment();
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mViewHolder = inflater.inflate(R.layout.fragment_search, container, false);
        ButterKnife.bind(this, mViewHolder);

        searchText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) { }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(charSequence.toString().length() > 0) {
                    clearInput.setVisibility(View.VISIBLE);
                    queryData = new Bundle();
                    queryData.putDouble("lat", 0);
                    queryData.putDouble("lng", 0);
                    queryData.putString("name", searchText.getEditableText().toString().trim());
                    getActivity().getSupportLoaderManager()
                            .initLoader(LoaderID.FETCH_MY_BUSINESS.getValue(), queryData, SearchFragment.this).forceLoad();
                } else {
                    clearInput.setVisibility(View.GONE);
                }
                if(charSequence.toString().length() > 3) {
                    queryData = new Bundle();
                    queryData.putDouble("lat", 0);
                    queryData.putDouble("lng", 0);
                    queryData.putString("name", searchText.getEditableText().toString().trim());
                    getActivity().getSupportLoaderManager()
                            .initLoader(LoaderID.FETCH_MY_BUSINESS.getValue(), queryData, SearchFragment.this).forceLoad();
                }
            }

            @Override
            public void afterTextChanged(Editable editable) { }
        });

        refreshLayout.setRefreshing(false);
        businessAdapter = new BusinessAdapter(this);
        linearLayoutManager = new LinearLayoutManager(getContext());
        detailsList.setLayoutManager(linearLayoutManager);
        detailsList.setAdapter(businessAdapter);

        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if(searchText.getEditableText().toString().trim().length() > 0) {
                    queryData = new Bundle();
                    queryData.putDouble("lat", 0);
                    queryData.putDouble("lng", 0);
                    queryData.putString("name", searchText.getEditableText().toString().trim());
                    getActivity().getSupportLoaderManager()
                            .initLoader(LoaderID.FETCH_MY_BUSINESS.getValue(), queryData, SearchFragment.this).forceLoad();
                }
            }
        });
        return mViewHolder;
    }

    @OnClick(R.id.search_clear_input)
    public void onClearSearchField() {
        searchText.setText("");
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
        intent.putExtra(BUSINESS_ID_TAG, businessDao.getId());
        intent.putExtra(BUSINESS_NAME_TAG, businessDao.getName());
        startActivity(intent);
    }
}
