package com.touchmenotapps.marketplace.consumer.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import com.touchmenotapps.marketplace.R;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by i7 on 18-10-2017.
 */

public class FeedFragment extends Fragment {

    @BindView(R.id.refresh_layout)
    SwipeRefreshLayout refreshLayout;
    @BindView(R.id.details_list)
    RecyclerView detailsList;

    private View mViewHolder;
    private Animation animFast, animSlow;

    public static FeedFragment newInstance() {
        FeedFragment fragment = new FeedFragment();
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mViewHolder = inflater.inflate(R.layout.fragment_purchases, container, false);
        ButterKnife.bind(this, mViewHolder);

        animFast = AnimationUtils.loadAnimation(getActivity(), R.anim.float_anim);
        animSlow = AnimationUtils.loadAnimation(getActivity(), R.anim.float_anim_slow);
        mViewHolder.findViewById(R.id.cloud_image_1).startAnimation(animFast);
        mViewHolder.findViewById(R.id.cloud_image_2).startAnimation(animSlow);

        return mViewHolder;
    }
}
