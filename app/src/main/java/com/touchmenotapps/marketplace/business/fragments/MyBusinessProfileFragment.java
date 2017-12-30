package com.touchmenotapps.marketplace.business.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.AppCompatTextView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.touchmenotapps.marketplace.R;
import com.touchmenotapps.marketplace.SplashActivity;
import com.touchmenotapps.marketplace.framework.persist.AppPreferences;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by arindamnath on 30/12/17.
 */

public class MyBusinessProfileFragment extends Fragment {

    @BindView(R.id.profile_phone_number)
    AppCompatTextView phoneNumber;
    @BindView(R.id.profile_role)
    AppCompatTextView role;

    private View mViewHolder;
    private AppPreferences appPreferences;

    public static MyBusinessProfileFragment newInstance() {
        MyBusinessProfileFragment fragment = new MyBusinessProfileFragment();
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mViewHolder = inflater.inflate(R.layout.fragment_profile, container, false);
        ButterKnife.bind(this, mViewHolder);

        appPreferences = new AppPreferences(getContext());

        phoneNumber.setText(appPreferences.getUserPhoneNumber());
        role.setText(appPreferences.getUserType().toString());
        return mViewHolder;
    }

    @OnClick(R.id.profile_logout)
    public void onLogout() {
        appPreferences.setLoggedOut();
        startActivity(new Intent(getActivity(), SplashActivity.class));
        getActivity().finish();
    }
}
