package com.touchmenotapps.marketplace.common.fragment;

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
import com.touchmenotapps.marketplace.common.BusinessOfferActivity;
import com.touchmenotapps.marketplace.common.OfferDetailsActivity;
import com.touchmenotapps.marketplace.common.adapters.BusinessFeedAdapter;
import com.touchmenotapps.marketplace.common.interfaces.BusinessFeedSelectedListener;
import com.touchmenotapps.marketplace.framework.enums.UserType;
import com.touchmenotapps.marketplace.framework.persist.AppPreferences;
import com.touchmenotapps.marketplace.threads.loaders.BusinessFeedLoaderTask;
import com.touchmenotapps.marketplace.framework.enums.LoaderID;
import com.touchmenotapps.marketplace.bo.OffersDao;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.touchmenotapps.marketplace.framework.constants.AppConstants.BUSINESS_ID_TAG;
import static com.touchmenotapps.marketplace.framework.constants.AppConstants.FEED_TAG;

/**
 * Created by arindamnath on 30/12/17.
 */

public class BusinessOffersFragment extends Fragment
        implements LoaderManager.LoaderCallbacks<List<OffersDao>>, BusinessFeedSelectedListener{

    @BindView(R.id.my_business_feed_empty)
    LinearLayout emptyBusiness;
    @BindView(R.id.refresh_layout)
    SwipeRefreshLayout refreshLayout;
    @BindView(R.id.details_list)
    RecyclerView detailsList;

    private View mViewHolder;
    private long businessId = -1l;
    private Bundle queryData;
    private BusinessFeedAdapter businessFeedAdapter;
    private AppPreferences appPreferences;

    public static BusinessOffersFragment newInstance(long businessId) {
        BusinessOffersFragment fragment = new BusinessOffersFragment();
        Bundle bundle = new Bundle();
        bundle.putLong(BUSINESS_ID_TAG, businessId);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle args = getArguments();
        businessId = args.getLong(BUSINESS_ID_TAG, -1l);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mViewHolder = inflater.inflate(R.layout.fragment_my_business_feed, container, false);
        ButterKnife.bind(this, mViewHolder);

        businessFeedAdapter = new BusinessFeedAdapter(this);
        appPreferences = new AppPreferences(getContext());
        refreshLayout.setRefreshing(false);
        detailsList.setLayoutManager(new LinearLayoutManager(getContext()));
        detailsList.setAdapter(businessFeedAdapter);

        if(businessId != -1l) {
            mViewHolder.findViewById(R.id.add_business_feed_button).setVisibility(View.GONE);
        }

        if(appPreferences.getUserType() == UserType.CONSUMER) {
            mViewHolder.findViewById(R.id.add_business_feed_button).setVisibility(View.GONE);
        }

        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                queryData = new Bundle();
                queryData.putLong(BUSINESS_ID_TAG, businessId);
                getActivity().getSupportLoaderManager()
                        .initLoader(LoaderID.FETCH_BUSINESS_FEED.getValue(), queryData, BusinessOffersFragment.this).forceLoad();
            }
        });
        return mViewHolder;
    }

    @Override
    public void onResume() {
        super.onResume();
        queryData = new Bundle();
        queryData.putLong(BUSINESS_ID_TAG, businessId);
        getActivity().getSupportLoaderManager()
                .initLoader(LoaderID.FETCH_BUSINESS_FEED.getValue(), queryData, BusinessOffersFragment.this).forceLoad();
    }

    @OnClick(R.id.add_business_feed_button)
    public void onAddFeed() {
        startActivity(new Intent(getActivity(), BusinessOfferActivity.class));
    }

    @Override
    public Loader<List<OffersDao>> onCreateLoader(int id, Bundle args) {
        refreshLayout.setRefreshing(true);
        return new BusinessFeedLoaderTask(getActivity(), args);
    }

    @Override
    public void onLoadFinished(Loader<List<OffersDao>> loader, List<OffersDao> data) {
        refreshLayout.setRefreshing(false);
        if(data.size() > 0) {
            emptyBusiness.setVisibility(View.GONE);
            refreshLayout.setVisibility(View.VISIBLE);
            businessFeedAdapter.setData(data);
        } else {
            emptyBusiness.setVisibility(View.VISIBLE);
            refreshLayout.setVisibility(View.GONE);
        }
    }

    @Override
    public void onLoaderReset(Loader<List<OffersDao>> loader) {
        refreshLayout.setRefreshing(false);
    }

    @Override
    public void onBusinessFeedSelected(OffersDao offersDao) {
        Intent intent = new Intent(getActivity(), OfferDetailsActivity.class);
        intent.putExtra(FEED_TAG, offersDao);
        getActivity().startActivity(intent);
    }
}
