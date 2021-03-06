package com.touchmenotapps.marketplace.business.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;

import com.getkeepsafe.taptargetview.TapTarget;
import com.getkeepsafe.taptargetview.TapTargetView;
import com.touchmenotapps.marketplace.R;
import com.touchmenotapps.marketplace.common.BusinessAddActivity;
import com.touchmenotapps.marketplace.common.BusinessDetailsActivity;
import com.touchmenotapps.marketplace.common.adapters.BusinessAdapter;
import com.touchmenotapps.marketplace.common.interfaces.BusinessSelectedListener;
import com.touchmenotapps.marketplace.framework.persist.AppPreferences;
import com.touchmenotapps.marketplace.threads.loaders.BusinessLoaderTask;
import com.touchmenotapps.marketplace.framework.enums.LoaderID;
import com.touchmenotapps.marketplace.bo.BusinessDao;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.touchmenotapps.marketplace.framework.constants.AppConstants.BUSINESS_CATEGORY_TAG;
import static com.touchmenotapps.marketplace.framework.constants.AppConstants.BUSINESS_ID_TAG;
import static com.touchmenotapps.marketplace.framework.constants.AppConstants.BUSINESS_NAME_TAG;
import static com.touchmenotapps.marketplace.framework.constants.AppConstants.BUSINESS_RATING_TAG;

/**
 * Created by arindamnath on 30/12/17.
 */

public class MyBusinessesFragment extends Fragment
        implements LoaderManager.LoaderCallbacks<List<BusinessDao>>, BusinessSelectedListener {

    @BindView(R.id.add_business_button)
    FloatingActionButton addBusiness;
    @BindView(R.id.my_business_empty)
    LinearLayout emptyBusiness;
    @BindView(R.id.refresh_layout)
    SwipeRefreshLayout refreshLayout;
    @BindView(R.id.details_list)
    RecyclerView detailsList;

    private View mViewHolder;
    private Bundle queryData;
    private BusinessAdapter adapter;
    private Animation animFast, animSlow;
    private AppPreferences appPreferences;

    public static MyBusinessesFragment newInstance() {
        MyBusinessesFragment fragment = new MyBusinessesFragment();
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mViewHolder = inflater.inflate(R.layout.fragment_my_business, container, false);
        ButterKnife.bind(this, mViewHolder);

        appPreferences = new AppPreferences(getContext());
        adapter = new BusinessAdapter(this);
        refreshLayout.setRefreshing(false);
        detailsList.setLayoutManager(new LinearLayoutManager(getContext()));
        detailsList.setAdapter(adapter);

        animFast = AnimationUtils.loadAnimation(getActivity(), R.anim.float_anim);
        animSlow = AnimationUtils.loadAnimation(getActivity(), R.anim.float_anim_slow);
        mViewHolder.findViewById(R.id.cloud_image_1).startAnimation(animFast);
        mViewHolder.findViewById(R.id.cloud_image_2).startAnimation(animSlow);

        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                queryData = new Bundle();
                getActivity().getSupportLoaderManager()
                        .initLoader(LoaderID.FETCH_MY_BUSINESS.getValue(), queryData, MyBusinessesFragment.this).forceLoad();
            }
        });

        // Prompt user to add a new business
        if(!appPreferences.isSellerAddBusinessShown()) {
            TapTargetView.showFor(getActivity(), TapTarget.forView(
                            mViewHolder.findViewById(R.id.add_business_button),
                            "Add New Business",
                            "Click the add button to list a new business.")
                            .outerCircleColor(R.color.primary)
                            .dimColor(R.color.dark_grey)
                            .cancelable(true)
                            .transparentTarget(true),
                    new TapTargetView.Listener() {
                        @Override
                        public void onTargetClick(TapTargetView view) {
                            super.onTargetClick(view);
                            appPreferences.setSellerAddBusinessShown();
                            onAddBusinessClick();
                        }
                    });
        }

        return mViewHolder;
    }

    @Override
    public void onResume() {
        super.onResume();
        queryData = new Bundle();
        getActivity().getSupportLoaderManager()
                .restartLoader(LoaderID.FETCH_MY_BUSINESS.getValue(), queryData, MyBusinessesFragment.this).forceLoad();
    }

    @OnClick(R.id.add_business_button)
    public void onAddBusinessClick() {
        startActivity(new Intent(getActivity(), BusinessAddActivity.class));
    }

    @Override
    public Loader<List<BusinessDao>> onCreateLoader(int id, Bundle args) {
        refreshLayout.setRefreshing(true);
        return new BusinessLoaderTask(getActivity(), args);
    }

    @Override
    public void onLoadFinished(Loader<List<BusinessDao>> loader, final List<BusinessDao> data) {
        refreshLayout.setRefreshing(false);
        if (data.size() > 0) {
            mViewHolder.findViewById(R.id.cloud_image_1).clearAnimation();
            mViewHolder.findViewById(R.id.cloud_image_2).clearAnimation();
            emptyBusiness.setVisibility(View.GONE);
            refreshLayout.setVisibility(View.VISIBLE);
            adapter.setData(data);
        } else {
            emptyBusiness.setVisibility(View.VISIBLE);
            refreshLayout.setVisibility(View.GONE);
            mViewHolder.findViewById(R.id.cloud_image_1).startAnimation(animFast);
            mViewHolder.findViewById(R.id.cloud_image_2).startAnimation(animSlow);
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
        intent.putExtra(BUSINESS_CATEGORY_TAG, businessDao.getCategory());
        intent.putExtra(BUSINESS_RATING_TAG, businessDao.getSingleScoreRating());
        startActivity(intent);
    }
}
