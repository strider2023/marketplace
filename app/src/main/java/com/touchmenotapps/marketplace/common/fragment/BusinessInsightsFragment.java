package com.touchmenotapps.marketplace.common.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.touchmenotapps.marketplace.R;
import com.touchmenotapps.marketplace.bo.BusinessDao;
import com.touchmenotapps.marketplace.common.adapters.AnalyticsAdapter;
import com.touchmenotapps.marketplace.common.adapters.BusinessRatingAdapter;
import com.touchmenotapps.marketplace.framework.enums.RequestType;
import com.touchmenotapps.marketplace.framework.enums.ServerEvents;
import com.touchmenotapps.marketplace.framework.interfaces.ServerResponseListener;
import com.touchmenotapps.marketplace.framework.persist.AppPreferences;
import com.touchmenotapps.marketplace.threads.asynctasks.BusinessTask;

import org.json.simple.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.touchmenotapps.marketplace.framework.constants.AppConstants.BUSINESS_ID_TAG;

/**
 * Created by arindamnath on 26/01/18.
 */

public class BusinessInsightsFragment extends Fragment
        implements ServerResponseListener {

    @BindView(R.id.business_ratings_list)
    ListView ratingsList;
    @BindView(R.id.business_analytics_list)
    ListView kpiList;

    private View mViewHolder;
    private Long businessId;
    private BusinessDao businessDao;
    private BusinessRatingAdapter businessRatingAdapter;
    private AnalyticsAdapter analyticsAdapter;
    private AppPreferences appPreferences;

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

        businessRatingAdapter = new BusinessRatingAdapter(getContext());
        analyticsAdapter = new AnalyticsAdapter(getContext());

        ratingsList.setAdapter(businessRatingAdapter);
        kpiList.setAdapter(analyticsAdapter);
        return mViewHolder;
    }

    @Override
    public void onResume() {
        super.onResume();
        if (businessId != -1l) {
            BusinessTask businessTask = new BusinessTask(1, getActivity(), this);
            businessTask.setBusinessDetails(businessId, RequestType.GET);
            businessTask.execute(new JSONObject[]{});
        }
    }

    @Override
    public void onSuccess(int threadId, Object object) {
        businessDao = (BusinessDao) object;
        businessRatingAdapter.setData(businessDao.getRatingsDao());
        analyticsAdapter.setData(businessDao.getAnalyticsDao());
    }

    @Override
    public void onFaliure(ServerEvents serverEvents, Object object) {
        Snackbar.make(ratingsList, object.toString(), Snackbar.LENGTH_LONG).show();
    }
}
