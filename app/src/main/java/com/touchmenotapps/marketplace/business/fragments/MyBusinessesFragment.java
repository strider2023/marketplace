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

import com.touchmenotapps.marketplace.R;
import com.touchmenotapps.marketplace.business.BusinessAddActivity;
import com.touchmenotapps.marketplace.business.BusinessDetailsActivity;
import com.touchmenotapps.marketplace.business.adapters.BusinessAdapter;
import com.touchmenotapps.marketplace.business.interfaces.BusinessSelectedListener;
import com.touchmenotapps.marketplace.business.loaders.BusinessLoaderTask;
import com.touchmenotapps.marketplace.framework.enums.LoaderID;
import com.touchmenotapps.marketplace.bo.BusinessDao;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.touchmenotapps.marketplace.business.BusinessDetailsActivity.SELECTED_BUSINESS_ID;
import static com.touchmenotapps.marketplace.business.BusinessDetailsActivity.SELECTED_BUSINESS_NAME;

/**
 * Created by arindamnath on 30/12/17.
 */

public class MyBusinessesFragment extends Fragment
        implements LoaderManager.LoaderCallbacks<List<BusinessDao>>, BusinessSelectedListener {

    @BindView(R.id.add_business_button)
    FloatingActionButton addBusiness;
    @BindView(R.id.my_business_empty)
    LinearLayout emptyBusiness;
    @BindView(R.id.business_refresh_layout)
    SwipeRefreshLayout refreshBusiness;
    @BindView(R.id.business_list)
    RecyclerView businessList;

    private View mViewHolder;
    private Bundle queryData;
    private BusinessAdapter adapter;
    private Animation animFast, animSlow;

    public static MyBusinessesFragment newInstance() {
        MyBusinessesFragment fragment = new MyBusinessesFragment();
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mViewHolder = inflater.inflate(R.layout.fragment_my_business, container, false);
        ButterKnife.bind(this, mViewHolder);

        adapter = new BusinessAdapter(this);
        refreshBusiness.setRefreshing(false);
        businessList.setLayoutManager(new LinearLayoutManager(getContext()));
        businessList.setAdapter(adapter);

        animFast = AnimationUtils.loadAnimation(getActivity(), R.anim.float_anim);
        animSlow = AnimationUtils.loadAnimation(getActivity(), R.anim.float_anim_slow);
        mViewHolder.findViewById(R.id.cloud_image_1).startAnimation(animFast);
        mViewHolder.findViewById(R.id.cloud_image_2).startAnimation(animSlow);

        refreshBusiness.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                queryData = new Bundle();
                getActivity().getSupportLoaderManager()
                        .initLoader(LoaderID.FETCH_MY_BUSINESS.getValue(), queryData, MyBusinessesFragment.this).forceLoad();
            }
        });

        return mViewHolder;
    }

    @Override
    public void onResume() {
        super.onResume();
        queryData = new Bundle();
        getActivity().getSupportLoaderManager()
                .initLoader(LoaderID.FETCH_MY_BUSINESS.getValue(), queryData, MyBusinessesFragment.this).forceLoad();
    }

    @OnClick(R.id.add_business_button)
    public void onAddBusinessClick() {
        startActivity(new Intent(getActivity(), BusinessAddActivity.class));
    }

    @Override
    public Loader<List<BusinessDao>> onCreateLoader(int id, Bundle args) {
        refreshBusiness.setRefreshing(true);
        return new BusinessLoaderTask(getActivity(), args);
    }

    @Override
    public void onLoadFinished(Loader<List<BusinessDao>> loader, final List<BusinessDao> data) {
        refreshBusiness.setRefreshing(false);
        if(data.size() > 0) {
            mViewHolder.findViewById(R.id.cloud_image_1).clearAnimation();
            mViewHolder.findViewById(R.id.cloud_image_2).clearAnimation();
            emptyBusiness.setVisibility(View.GONE);
            refreshBusiness.setVisibility(View.VISIBLE);
            adapter.setData(data);
        } else {
            emptyBusiness.setVisibility(View.VISIBLE);
            refreshBusiness.setVisibility(View.GONE);
            mViewHolder.findViewById(R.id.cloud_image_1).startAnimation(animFast);
            mViewHolder.findViewById(R.id.cloud_image_2).startAnimation(animSlow);
        }
    }

    @Override
    public void onLoaderReset(Loader<List<BusinessDao>> loader) {
        refreshBusiness.setRefreshing(false);
    }

    @Override
    public void onBusinessSelected(BusinessDao businessDao) {
        Intent intent = new Intent(getActivity(), BusinessDetailsActivity.class);
        intent.putExtra(SELECTED_BUSINESS_ID, businessDao.getId());
        intent.putExtra(SELECTED_BUSINESS_NAME, businessDao.getName());
        startActivity(intent);
    }
}
