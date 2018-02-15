package com.touchmenotapps.marketplace.common;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatTextView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.touchmenotapps.marketplace.R;
import com.touchmenotapps.marketplace.bo.OffersDao;
import com.touchmenotapps.marketplace.common.dialogs.DeleteDailog;
import com.touchmenotapps.marketplace.common.interfaces.DeleteListener;
import com.touchmenotapps.marketplace.framework.enums.DeleteDialogType;
import com.touchmenotapps.marketplace.framework.enums.ServerEvents;
import com.touchmenotapps.marketplace.framework.enums.UserType;
import com.touchmenotapps.marketplace.framework.interfaces.ServerResponseListener;
import com.touchmenotapps.marketplace.framework.persist.AppPreferences;
import com.touchmenotapps.marketplace.threads.asynctasks.UserKPITask;

import org.json.simple.JSONObject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.touchmenotapps.marketplace.framework.constants.AppConstants.FEED_TAG;
import static com.touchmenotapps.marketplace.framework.constants.AppConstants.KPI_FEED;

public class OfferDetailsActivity extends AppCompatActivity
    implements ServerResponseListener, DeleteListener {

    @BindView(R.id.feed_image)
    ImageView image;
    @BindView(R.id.feed_caption)
    AppCompatTextView caption;
    @BindView(R.id.feed_coupon)
    AppCompatTextView code;
    @BindView(R.id.feed_kpi_today)
    AppCompatTextView today;
    @BindView(R.id.feed_kpi_week)
    AppCompatTextView week;
    @BindView(R.id.feed_kpi_month)
    AppCompatTextView month;
    @BindView(R.id.feed_kpi_total)
    AppCompatTextView total;

    private OffersDao offersDao;
    private DeleteDailog deleteDailog;
    private AppPreferences appPreferences;
    private MenuItem delete;
    private UserKPITask userKPITask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feed_details);
        ButterKnife.bind(this);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        appPreferences = new AppPreferences(this);
        deleteDailog = new DeleteDailog(this, DeleteDialogType.OFFER, this);

        if(getIntent().getParcelableExtra(FEED_TAG) != null) {
            offersDao = getIntent().getParcelableExtra(FEED_TAG);
            caption.setText(offersDao.getCaption());
            code.setText(offersDao.getRedeeemCode());
            Glide.with(this)
                    .load(offersDao.getImageURL())
                    .error(R.drawable.ic_shop)
                    .placeholder(R.drawable.ic_shop)
                    .centerCrop()
                    .into(image);
            if(appPreferences.getUserType() == UserType.BUSINESS) {
                today.setText(String.valueOf(offersDao.getAnalyticsDao().getToday()));
                week.setText(String.valueOf(offersDao.getAnalyticsDao().getLastWeek()));
                month.setText(String.valueOf(offersDao.getAnalyticsDao().getLastMonth()));
                total.setText(String.valueOf(offersDao.getAnalyticsDao().getTotal()));
            } else {
                findViewById(R.id.kpi_contianer).setVisibility(View.GONE);
            }
            if(!offersDao.isCanDelete()) {
                findViewById(R.id.feed_edit_btn).setVisibility(View.INVISIBLE);
            }
            if(appPreferences.getUserType() == UserType.CONSUMER) {
                userKPITask = new UserKPITask(1, this, this, false);
                userKPITask.setBusinessId(offersDao.getBusinessId());
                userKPITask.setFeedId(offersDao.getId());
                userKPITask.setType(KPI_FEED);
                userKPITask.execute(new JSONObject[]{});
            }
        }
    }

    @OnClick(R.id.feed_edit_btn)
    public void onFeedEditClicked() {
        Intent intent = new Intent(this, BusinessOfferActivity.class);
        intent.putExtra(FEED_TAG, offersDao);
        startActivity(intent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.feed_menu, menu);
        delete = menu.findItem(R.id.offer_delete);
        delete.setVisible(offersDao.isCanDelete());
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
            case R.id.offer_delete:
                deleteDailog.show(offersDao.getBusinessId(), offersDao.getId());
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onSuccess(int threadId, Object object) {

    }

    @Override
    public void onFaliure(ServerEvents serverEvents, Object object) {

    }

    @Override
    public void onBusinessDeletionSuccess() {
        finish();
    }

    @Override
    public void onBusinessDeletionFailure() {

    }
}
