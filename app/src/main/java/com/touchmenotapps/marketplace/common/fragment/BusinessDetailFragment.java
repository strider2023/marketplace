package com.touchmenotapps.marketplace.common.fragment;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.customtabs.CustomTabsIntent;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.AppCompatTextView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.clans.fab.FloatingActionMenu;
import com.touchmenotapps.marketplace.R;
import com.touchmenotapps.marketplace.bo.BusinessDao;
import com.touchmenotapps.marketplace.business.BusinessAddActivity;
import com.touchmenotapps.marketplace.business.BusinessAddFeedActivity;
import com.touchmenotapps.marketplace.common.dialogs.DeleteBusinessDailog;
import com.touchmenotapps.marketplace.common.interfaces.BusinessDeleteListener;
import com.touchmenotapps.marketplace.common.threads.BookmarksTask;
import com.touchmenotapps.marketplace.common.threads.GetBusinessByIdTask;
import com.touchmenotapps.marketplace.framework.enums.ServerEvents;
import com.touchmenotapps.marketplace.framework.enums.UserType;
import com.touchmenotapps.marketplace.framework.interfaces.ServerResponseListener;
import com.touchmenotapps.marketplace.framework.persist.AppPreferences;

import org.json.simple.JSONObject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.touchmenotapps.marketplace.common.BusinessDetailsActivity.SELECTED_BUSINESS_ID;
import static com.touchmenotapps.marketplace.common.BusinessDetailsActivity.SELECTED_BUSINESS_NAME;

/**
 * Created by arindamnath on 16/01/18.
 */

public class BusinessDetailFragment extends Fragment
        implements ServerResponseListener {

    @BindView(R.id.business_phone)
    AppCompatTextView phone;
    @BindView(R.id.business_website)
    AppCompatTextView website;
    @BindView(R.id.business_address)
    AppCompatTextView address;

    private View mViewHolder;
    private Long businessId;
    private BusinessDao businessDao;

    public static BusinessDetailFragment newInstance(long businessId) {
        BusinessDetailFragment fragment = new BusinessDetailFragment();
        Bundle bundle = new Bundle();
        bundle.putLong(SELECTED_BUSINESS_ID, businessId);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle args = getArguments();
        if (args.getLong(SELECTED_BUSINESS_ID, -1l) != -1l) {
            businessId = args.getLong(SELECTED_BUSINESS_ID, -1l);
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mViewHolder = inflater.inflate(R.layout.fragment_business_details, container, false);
        ButterKnife.bind(this, mViewHolder);

        if (businessId != -1l) {
            JSONObject id = new JSONObject();
            id.put("id", String.valueOf(businessId));
            new GetBusinessByIdTask(1, getActivity(), this)
                    .execute(new JSONObject[]{id});
        }

        return mViewHolder;
    }

    @OnClick(R.id.business_phone_btn)
    public void onPhoneClick() {
        Intent intent = new Intent(Intent.ACTION_DIAL, Uri.fromParts("tel",
                phone.getText().toString(), null));
        startActivity(intent);
    }

    @OnClick(R.id.business_website_btn)
    public void onWebisteView() {
        try {
            CustomTabsIntent.Builder builder = new CustomTabsIntent.Builder();
            builder.setToolbarColor(ContextCompat.getColor(getActivity(), R.color.colorPrimary));
            CustomTabsIntent customTabsIntent = builder.build();
            customTabsIntent.launchUrl(getActivity(), Uri.parse(website.getText().toString()));
        } catch (Exception e) {
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setData(Uri.parse(website.getText().toString()));
            startActivity(intent);
        }
    }

    @Override
    public void onSuccess(int threadId, Object object) {
        businessDao = (BusinessDao) object;
        for (String number : businessDao.getPhoneNumber()) {
            phone.setText(number);
        }
        website.setText(businessDao.getWebsite());
        address.setText(businessDao.getBusinessAddressDao().getAddress() + "\n\n" +
                businessDao.getBusinessAddressDao().getCity() + "\n\n" +
                businessDao.getBusinessAddressDao().getState() + "\n\n" +
                businessDao.getBusinessAddressDao().getZip());
    }

    @Override
    public void onFaliure(ServerEvents serverEvents, Object object) {
        Snackbar.make(website, object.toString(), Snackbar.LENGTH_LONG).show();
    }
}
