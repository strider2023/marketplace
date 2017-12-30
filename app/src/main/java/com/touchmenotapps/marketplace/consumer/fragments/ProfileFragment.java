package com.touchmenotapps.marketplace.consumer.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.touchmenotapps.marketplace.R;
import com.touchmenotapps.marketplace.SplashActivity;
import com.touchmenotapps.marketplace.framework.persist.AppPreferences;

import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by i7 on 18-10-2017.
 */

public class ProfileFragment extends Fragment {

    private View mViewHolder;
    private AppPreferences appPreferences;

    public static ProfileFragment newInstance() {
        ProfileFragment fragment = new ProfileFragment();
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mViewHolder = inflater.inflate(R.layout.fragment_profile, container, false);
        ButterKnife.bind(this, mViewHolder);

        appPreferences = new AppPreferences(getContext());

        return mViewHolder;
    }

    @OnClick(R.id.profile_logout)
    public void onLogout() {
        appPreferences.setLoggedOut();
        startActivity(new Intent(getActivity(), SplashActivity.class));
        getActivity().finish();
    }
}
