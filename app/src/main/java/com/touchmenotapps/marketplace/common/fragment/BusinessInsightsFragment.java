package com.touchmenotapps.marketplace.common.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.touchmenotapps.marketplace.R;

import butterknife.ButterKnife;

import static com.touchmenotapps.marketplace.framework.constants.AppConstants.BUSINESS_ID_TAG;

/**
 * Created by arindamnath on 26/01/18.
 */

public class BusinessInsightsFragment extends Fragment {

    private View mViewHolder;
    private long businessId = -1l;
    private Bundle queryData;

    public static BusinessInsightsFragment newInstance(long businessId) {
        BusinessInsightsFragment fragment = new BusinessInsightsFragment();
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
        mViewHolder = inflater.inflate(R.layout.fragment_insghts, container, false);
        ButterKnife.bind(this, mViewHolder);

        return mViewHolder;
    }
}
