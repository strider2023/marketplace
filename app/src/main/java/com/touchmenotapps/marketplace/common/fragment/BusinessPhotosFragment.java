package com.touchmenotapps.marketplace.common.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.touchmenotapps.marketplace.R;
import com.touchmenotapps.marketplace.bo.BusinessImageDao;
import com.touchmenotapps.marketplace.common.ViewImageActivity;
import com.touchmenotapps.marketplace.common.adapters.BusinessImageAdapter;
import com.touchmenotapps.marketplace.common.interfaces.BusinessImageSelectedListener;
import com.touchmenotapps.marketplace.framework.enums.LoaderID;
import com.touchmenotapps.marketplace.framework.persist.AppPreferences;
import com.touchmenotapps.marketplace.threads.loaders.ImagesLoaderTask;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.touchmenotapps.marketplace.framework.constants.AppConstants.BUSINESS_ID_TAG;
import static com.touchmenotapps.marketplace.framework.constants.AppConstants.BUSINESS_IMAGE_TAG;

/**
 * Created by i7 on 29-01-2018.
 */

public class BusinessPhotosFragment extends Fragment implements BusinessImageSelectedListener,
        LoaderManager.LoaderCallbacks<List<BusinessImageDao>> {

    @BindView(R.id.images_empty)
    LinearLayout emptyBusiness;
    @BindView(R.id.refresh_layout)
    SwipeRefreshLayout refreshLayout;
    @BindView(R.id.details_list)
    RecyclerView detailsList;

    private View mViewHolder;
    private Long businessId;
    private BusinessImageAdapter businessImageAdapter;
    private Bundle queryData;
    private AppPreferences appPreferences;

    public static BusinessPhotosFragment newInstance(long businessId) {
        BusinessPhotosFragment fragment = new BusinessPhotosFragment();
        Bundle bundle = new Bundle();
        bundle.putLong(BUSINESS_ID_TAG, businessId);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle args = getArguments();
        if (args.getLong(BUSINESS_ID_TAG, -1l) != -1l) {
            businessId = args.getLong(BUSINESS_ID_TAG, -1l);
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mViewHolder = inflater.inflate(R.layout.fragment_photos, container, false);
        ButterKnife.bind(this, mViewHolder);

        businessImageAdapter = new BusinessImageAdapter(this);
        appPreferences = new AppPreferences(getContext());

        refreshLayout.setRefreshing(false);
        detailsList.setLayoutManager(new GridLayoutManager(getContext(), 3));
        detailsList.setAdapter(businessImageAdapter);

        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                queryData = new Bundle();
                queryData.putLong(BUSINESS_ID_TAG, businessId);
                getActivity().getSupportLoaderManager()
                        .initLoader(LoaderID.FETCH_BUSINESS_IMAGES.getValue(), queryData, BusinessPhotosFragment.this).forceLoad();
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
                .initLoader(LoaderID.FETCH_BUSINESS_IMAGES.getValue(), queryData, BusinessPhotosFragment.this).forceLoad();
    }

    @Override
    public Loader<List<BusinessImageDao>> onCreateLoader(int id, Bundle args) {
        refreshLayout.setRefreshing(false);
        return new ImagesLoaderTask(getActivity(), args);
    }

    @Override
    public void onLoadFinished(Loader<List<BusinessImageDao>> loader, List<BusinessImageDao> data) {
        refreshLayout.setRefreshing(false);
        if(data.size() > 0) {
            emptyBusiness.setVisibility(View.GONE);
            refreshLayout.setVisibility(View.VISIBLE);
            businessImageAdapter.setData(data);
        } else {
            emptyBusiness.setVisibility(View.VISIBLE);
            refreshLayout.setVisibility(View.GONE);
        }
    }

    @Override
    public void onLoaderReset(Loader<List<BusinessImageDao>> loader) {
        refreshLayout.setRefreshing(false);
    }

    @Override
    public void onImageClicked(BusinessImageDao businessImageDao) {
        Intent intent = new Intent(getActivity(), ViewImageActivity.class);
        intent.putExtra(BUSINESS_ID_TAG, businessId);
        intent.putExtra(BUSINESS_IMAGE_TAG, businessImageDao);
        startActivity(intent);
    }
}
