package com.touchmenotapps.marketplace.business.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.touchmenotapps.marketplace.R;
import com.touchmenotapps.marketplace.business.AddBusinessActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by arindamnath on 30/12/17.
 */

public class MyBusinessesFragment extends Fragment {

    @BindView(R.id.add_business_button)
    FloatingActionButton addBusiness;
    @BindView(R.id.my_business_empty)
    LinearLayout emptyBusiness;
    @BindView(R.id.business_refresh_layout)
    SwipeRefreshLayout refreshBusiness;
    @BindView(R.id.business_list)
    RecyclerView businessList;

    private View mViewHolder;

    public static MyBusinessesFragment newInstance() {
        MyBusinessesFragment fragment = new MyBusinessesFragment();
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mViewHolder = inflater.inflate(R.layout.fragment_my_business, container, false);
        ButterKnife.bind(this, mViewHolder);

        return mViewHolder;
    }

    @OnClick(R.id.add_business_button)
    public void onAddBusinessClick() {
        startActivity(new Intent(getActivity(), AddBusinessActivity.class));
    }


}
