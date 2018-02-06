package com.touchmenotapps.marketplace.common.fragment;

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
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.touchmenotapps.marketplace.R;
import com.touchmenotapps.marketplace.common.BusinessOfferActivity;
import com.touchmenotapps.marketplace.common.OfferDetailsActivity;
import com.touchmenotapps.marketplace.common.adapters.OffersAdapter;
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
    @BindView(R.id.list_filter_text)
    AppCompatEditText filterText;

    private View mViewHolder;
    private long businessId = -1l;
    private Bundle queryData;
    private OffersAdapter offersAdapter;
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
        mViewHolder = inflater.inflate(R.layout.fragment_offers, container, false);
        ButterKnife.bind(this, mViewHolder);

        offersAdapter = new OffersAdapter(this);
        appPreferences = new AppPreferences(getContext());
        refreshLayout.setRefreshing(false);
        detailsList.setLayoutManager(new LinearLayoutManager(getContext()));
        detailsList.setAdapter(offersAdapter);
        filterText.setHint("Search Offers");

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

        filterText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                offersAdapter.getFilter().filter(s);
            }

            @Override
            public void afterTextChanged(Editable s) { }
        });

        filterText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    offersAdapter.getFilter().filter(filterText.getEditableText().toString().trim());
                    filterText.clearFocus();
                    return true;
                }
                return false;
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
            offersAdapter.setData(data);
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
