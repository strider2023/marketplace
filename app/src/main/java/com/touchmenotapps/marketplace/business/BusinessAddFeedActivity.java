package com.touchmenotapps.marketplace.business;

import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.AppCompatSpinner;
import android.view.MenuItem;
import android.widget.Spinner;

import com.touchmenotapps.marketplace.R;
import com.touchmenotapps.marketplace.business.threads.GetBusinessByIdTask;
import com.touchmenotapps.marketplace.framework.enums.ServerEvents;
import com.touchmenotapps.marketplace.bo.FeedDao;
import com.touchmenotapps.marketplace.framework.interfaces.ServerResponseListener;

import org.json.simple.JSONObject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnItemSelected;

import static com.touchmenotapps.marketplace.business.BusinessDetailsActivity.SELECTED_BUSINESS_ID;

public class BusinessAddFeedActivity extends AppCompatActivity implements ServerResponseListener {

    @BindView(R.id.feed_description)
    AppCompatEditText description;
    @BindView(R.id.feed_start_date)
    AppCompatSpinner startDate;
    @BindView(R.id.feed_end_date)
    AppCompatSpinner endDate;

    private long businessId = -1l;
    private FeedDao feedDao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_business_feed);
        ButterKnife.bind(this);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        feedDao = new FeedDao(this);

        if(getIntent().getLongExtra(SELECTED_BUSINESS_ID, -1l) != -1l) {
            businessId = getIntent().getLongExtra(SELECTED_BUSINESS_ID, -1l);
        }
    }

    @OnItemSelected(R.id.feed_start_date)
    public void startDateSelected(Spinner spinner, int position) {

    }

    @OnItemSelected(R.id.feed_end_date)
    public void endDateSelected(Spinner spinner, int position) {

    }

    @OnClick(R.id.add_new_feed)
    public void addNewFeed() {
        if(description.getEditableText().toString().trim().length() > 0) {
            feedDao.setCaption(description.getEditableText().toString().trim());
            feedDao.setStartDateFromToday(2l);
            feedDao.setEndDateFromToday(31l);

            JSONObject id = new JSONObject();
            id.put("id", String.valueOf(businessId));
            new GetBusinessByIdTask(1, this, this)
                    .execute(new JSONObject[]{id, feedDao.toJSON()});
        } else {
            Snackbar.make(description, "Description cannot be empty.", Snackbar.LENGTH_LONG).show();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onSuccess(int threadId, Object object) {
        finish();
    }

    @Override
    public void onFaliure(ServerEvents serverEvents, Object object) {
        Snackbar.make(description, object.toString(), Snackbar.LENGTH_LONG).show();
    }
}
