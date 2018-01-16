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
        implements ServerResponseListener, BusinessDeleteListener {

    @BindView(R.id.business_name)
    AppCompatTextView name;
    @BindView(R.id.business_phone)
    AppCompatTextView phone;
    @BindView(R.id.business_website)
    AppCompatTextView website;
    @BindView(R.id.business_address)
    AppCompatTextView address;
    @BindView(R.id.business_options)
    FloatingActionMenu options;

    private View mViewHolder;
    private Long businessId;
    private BusinessDao businessDao;
    private AppPreferences appPreferences;
    private DeleteBusinessDailog deleteBusinessDailog;

    public static BusinessDetailFragment newInstance(long businessId, String name) {
        BusinessDetailFragment fragment = new BusinessDetailFragment();
        Bundle bundle = new Bundle();
        bundle.putLong(SELECTED_BUSINESS_ID, businessId);
        bundle.putString(SELECTED_BUSINESS_NAME, name);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle args = getArguments();
        if(args.getLong(SELECTED_BUSINESS_ID, -1l) != -1l) {
            businessId = args.getLong(SELECTED_BUSINESS_ID, -1l);
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mViewHolder = inflater.inflate(R.layout.fragment_my_business, container, false);
        ButterKnife.bind(this, mViewHolder);

        appPreferences = new AppPreferences(getContext());

        if(appPreferences.getUserType() != UserType.BUSINESS) {
            mViewHolder.findViewById(R.id.delete_business_btn).setVisibility(View.GONE);
            mViewHolder.findViewById(R.id.edit_business_btn).setVisibility(View.GONE);
            options.setVisibility(View.GONE);
            mViewHolder.findViewById(R.id.bookmark_business_feed_button).setVisibility(View.VISIBLE);
        }

        if(businessId != -1l) {
            deleteBusinessDailog = new DeleteBusinessDailog(getActivity(), businessId, this);
            JSONObject id = new JSONObject();
            id.put("id", String.valueOf(businessId));
            new GetBusinessByIdTask(1, getActivity(), this)
                    .execute(new JSONObject[]{id});
        }

        return mViewHolder;
    }

    @OnClick(R.id.edit_business_btn)
    public void onViewEdit() {
        Intent intent = new Intent(getActivity(), BusinessAddActivity.class);
        intent.putExtra(SELECTED_BUSINESS_ID, businessId);
        intent.putExtra(SELECTED_BUSINESS_NAME, name.getText());
        startActivity(intent);
    }

    @OnClick(R.id.delete_business_btn)
    public void onViewDelete() {
        deleteBusinessDailog.show();
    }

    @OnClick(R.id.bookmark_business_feed_button)
    public void onBusinessBookmark() {
        JSONObject id = new JSONObject();
        id.put("id", String.valueOf(businessId));
        new BookmarksTask(2, getActivity(), this)
                .execute(new JSONObject[]{id});
    }

    @OnClick(R.id.business_phone_btn)
    public void onPhoneClick() {
        Intent intent = new Intent(Intent.ACTION_DIAL, Uri.fromParts("tel",
                phone.getText().toString(), null));
        startActivity(intent);
    }

    @OnClick(R.id.add_business_feed_button)
    public void onAddFeed() {
//        Intent intent = new Intent(getActivity(), BusinessAddFeedActivity.class);
//        intent.putExtra(SELECTED_BUSINESS_ID, businessId);
//        startActivity(intent);
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
        switch (threadId) {
            case 1:
                businessDao = (BusinessDao) object;
                for(String number : businessDao.getPhoneNumber()) {
                    phone.setText(number);
                }
                website.setText(businessDao.getWebsite());
                address.setText(businessDao.getBusinessAddressDao().getAddress() + "\n\n" +
                        businessDao.getBusinessAddressDao().getCity() + "\n\n" +
                        businessDao.getBusinessAddressDao().getState() + "\n\n" +
                        businessDao.getBusinessAddressDao().getZip());
                break;
            case 2:
                Snackbar.make(name, "Bookmark Saved", Snackbar.LENGTH_LONG).show();
                break;
        }
    }

    @Override
    public void onFaliure(ServerEvents serverEvents, Object object) {
        Snackbar.make(name, object.toString(), Snackbar.LENGTH_LONG).show();
    }

    @Override
    public void onBusinessDeletionSuccess() {
        getActivity().finish();
    }

    @Override
    public void onBusinessDeletionFailure() {
        Snackbar.make(name, "Something went wrong.", Snackbar.LENGTH_LONG).show();
    }
}
