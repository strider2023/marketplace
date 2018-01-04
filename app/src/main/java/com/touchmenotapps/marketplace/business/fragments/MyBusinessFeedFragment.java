package com.touchmenotapps.marketplace.business.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.touchmenotapps.marketplace.R;
import com.touchmenotapps.marketplace.business.BusinessAddFeedActivity;
import com.touchmenotapps.marketplace.business.adapters.BusinessFeedAdapter;
import com.touchmenotapps.marketplace.business.interfaces.BusinessFeedSelectedListener;
import com.touchmenotapps.marketplace.business.loaders.BusinessFeedLoaderTask;
import com.touchmenotapps.marketplace.framework.enums.LoaderID;
import com.touchmenotapps.marketplace.bo.FeedDao;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by arindamnath on 30/12/17.
 */

public class MyBusinessFeedFragment extends Fragment
        implements LoaderManager.LoaderCallbacks<List<FeedDao>>, BusinessFeedSelectedListener{

    @BindView(R.id.my_business_feed_empty)
    LinearLayout emptyBusiness;
    @BindView(R.id.business_feed_refresh_layout)
    SwipeRefreshLayout refreshBusiness;
    @BindView(R.id.business_feed_list)
    RecyclerView businessList;

    private View mViewHolder;
    private Bundle queryData;
    private BusinessFeedAdapter businessFeedAdapter;

    public static MyBusinessFeedFragment newInstance() {
        MyBusinessFeedFragment fragment = new MyBusinessFeedFragment();
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mViewHolder = inflater.inflate(R.layout.fragment_my_business_feed, container, false);
        ButterKnife.bind(this, mViewHolder);

        businessFeedAdapter = new BusinessFeedAdapter(this);
        refreshBusiness.setRefreshing(false);
        businessList.setLayoutManager(new LinearLayoutManager(getContext()));
        businessList.setAdapter(businessFeedAdapter);

        refreshBusiness.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                queryData = new Bundle();
                getActivity().getSupportLoaderManager()
                        .initLoader(LoaderID.FETCH_BUSINESS_FEED.getValue(), queryData, MyBusinessFeedFragment.this).forceLoad();
            }
        });
        return mViewHolder;
    }

    @Override
    public void onResume() {
        super.onResume();
        queryData = new Bundle();
        getActivity().getSupportLoaderManager()
                .initLoader(LoaderID.FETCH_BUSINESS_FEED.getValue(), queryData, MyBusinessFeedFragment.this).forceLoad();
    }

    @OnClick(R.id.add_business_feed_button)
    public void onAddFeed() {
        startActivity(new Intent(getActivity(), BusinessAddFeedActivity.class));
    }

    @Override
    public Loader<List<FeedDao>> onCreateLoader(int id, Bundle args) {
        refreshBusiness.setRefreshing(true);
        return new BusinessFeedLoaderTask(getActivity(), args);
    }

    @Override
    public void onLoadFinished(Loader<List<FeedDao>> loader, List<FeedDao> data) {
        refreshBusiness.setRefreshing(false);
        if(data.size() > 0) {
            emptyBusiness.setVisibility(View.GONE);
            refreshBusiness.setVisibility(View.VISIBLE);
            businessFeedAdapter.setData(data);
        } else {
            emptyBusiness.setVisibility(View.VISIBLE);
            refreshBusiness.setVisibility(View.GONE);
        }
    }

    @Override
    public void onLoaderReset(Loader<List<FeedDao>> loader) {
        refreshBusiness.setRefreshing(false);
    }

    @Override
    public void onBusinessFeedSelected(FeedDao feedDao) {

    }
}
