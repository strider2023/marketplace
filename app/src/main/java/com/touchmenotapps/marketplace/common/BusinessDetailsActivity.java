package com.touchmenotapps.marketplace.common;

import android.content.Intent;
import android.net.Uri;
import android.support.customtabs.CustomTabsIntent;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatTextView;
import android.view.View;

import com.touchmenotapps.marketplace.R;
import com.touchmenotapps.marketplace.business.BusinessAddActivity;
import com.touchmenotapps.marketplace.business.BusinessAddFeedActivity;
import com.touchmenotapps.marketplace.common.threads.DeleteBusinessTask;
import com.touchmenotapps.marketplace.common.threads.GetBusinessByIdTask;
import com.touchmenotapps.marketplace.common.threads.BookmarksTask;
import com.touchmenotapps.marketplace.framework.enums.ServerEvents;
import com.touchmenotapps.marketplace.bo.BusinessDao;
import com.touchmenotapps.marketplace.framework.enums.UserType;
import com.touchmenotapps.marketplace.framework.interfaces.ServerResponseListener;
import com.touchmenotapps.marketplace.framework.persist.AppPreferences;

import org.json.simple.JSONObject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class BusinessDetailsActivity extends AppCompatActivity implements ServerResponseListener{

    public static final String SELECTED_BUSINESS_ID = "businessId";
    public static final String SELECTED_BUSINESS_NAME = "businessName";

    @BindView(R.id.business_name)
    AppCompatTextView name;
    @BindView(R.id.business_phone)
    AppCompatTextView phone;
    @BindView(R.id.business_website)
    AppCompatTextView website;
    @BindView(R.id.business_address)
    AppCompatTextView address;

    private long businessId = -1l;
    private BusinessDao businessDao;
    private AppPreferences appPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_my_business);
        ButterKnife.bind(this);

        appPreferences = new AppPreferences(this);
        if(appPreferences.getUserType() != UserType.BUSINESS) {
            findViewById(R.id.delete_business_btn).setVisibility(View.GONE);
            findViewById(R.id.edit_business_btn).setVisibility(View.GONE);
            findViewById(R.id.add_business_feed_button).setVisibility(View.GONE);
            findViewById(R.id.bookmark_business_feed_button).setVisibility(View.VISIBLE);
        }

        if(getIntent().getLongExtra(SELECTED_BUSINESS_ID, -1l) != -1l) {
            businessId = getIntent().getLongExtra(SELECTED_BUSINESS_ID, -1l);
            JSONObject id = new JSONObject();
            id.put("id", String.valueOf(businessId));
            new GetBusinessByIdTask(1, this, this)
                    .execute(new JSONObject[]{id});
        }
        if(getIntent().getStringExtra(SELECTED_BUSINESS_NAME) != null) {
            name.setText(getIntent().getStringExtra(SELECTED_BUSINESS_NAME));
        }
    }

    @OnClick(R.id.close_view_btn)
    public void onViewClose() {
        finish();
    }

    @OnClick(R.id.edit_business_btn)
    public void onViewEdit() {
        Intent intent = new Intent(this, BusinessAddActivity.class);
        intent.putExtra(SELECTED_BUSINESS_ID, businessId);
        intent.putExtra(SELECTED_BUSINESS_NAME, name.getText());
        startActivity(intent);
        finish();
    }

    @OnClick(R.id.delete_business_btn)
    public void onViewDelete() {
        JSONObject id = new JSONObject();
        id.put("id", String.valueOf(businessId));
        new DeleteBusinessTask(2, this, this)
                .execute(new JSONObject[]{id});
    }

    @OnClick(R.id.bookmark_business_feed_button)
    public void onBusinessBookmark() {
        JSONObject id = new JSONObject();
        id.put("id", String.valueOf(businessId));
        new BookmarksTask(3, this, this)
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
        Intent intent = new Intent(this, BusinessAddFeedActivity.class);
        intent.putExtra(SELECTED_BUSINESS_ID, businessId);
        startActivity(intent);
    }

    @OnClick(R.id.business_website_btn)
    public void onWebisteView() {
        try {
            CustomTabsIntent.Builder builder = new CustomTabsIntent.Builder();
            builder.setToolbarColor(ContextCompat.getColor(this, R.color.colorPrimary));
            CustomTabsIntent customTabsIntent = builder.build();
            customTabsIntent.launchUrl(this, Uri.parse(website.getText().toString()));
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
                finish();
                break;
            case 3:
                Snackbar.make(name, "Bookmark Saved", Snackbar.LENGTH_LONG).show();
                break;
        }
    }

    @Override
    public void onFaliure(ServerEvents serverEvents, Object object) {
        Snackbar.make(name, object.toString(), Snackbar.LENGTH_LONG).show();
    }
}
