package com.touchmenotapps.marketplace.business.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.touchmenotapps.marketplace.R;

import butterknife.ButterKnife;

/**
 * Created by arindamnath on 30/12/17.
 */

public class MyBusinessFeedFragment extends Fragment{

    private View mViewHolder;

    public static MyBusinessFeedFragment newInstance() {
        MyBusinessFeedFragment fragment = new MyBusinessFeedFragment();
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mViewHolder = inflater.inflate(R.layout.fragment_profile, container, false);
        ButterKnife.bind(this, mViewHolder);

        return mViewHolder;
    }
}
