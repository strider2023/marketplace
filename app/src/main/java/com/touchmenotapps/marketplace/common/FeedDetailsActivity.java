package com.touchmenotapps.marketplace.common;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.touchmenotapps.marketplace.R;
import com.touchmenotapps.marketplace.bo.FeedDao;
import com.touchmenotapps.marketplace.business.BusinessAddFeedActivity;
import com.touchmenotapps.marketplace.common.dialogs.DeleteFeedDialog;
import com.touchmenotapps.marketplace.common.interfaces.FeedDeleteListener;
import com.touchmenotapps.marketplace.framework.enums.ServerEvents;
import com.touchmenotapps.marketplace.framework.interfaces.ServerResponseListener;
import com.touchmenotapps.marketplace.framework.persist.AppPreferences;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.touchmenotapps.marketplace.framework.constants.AppConstants.FEED_TAG;

public class FeedDetailsActivity extends AppCompatActivity
    implements ServerResponseListener, FeedDeleteListener {

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.feed_image)
    ImageView image;
    @BindView(R.id.feed_caption)
    AppCompatTextView caption;
    @BindView(R.id.feed_coupon)
    AppCompatTextView code;

    private FeedDao feedDao;
    private DeleteFeedDialog feedDialog;
    private AppPreferences appPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feed_details);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        appPreferences = new AppPreferences(this);

        if(getIntent().getParcelableExtra(FEED_TAG) != null) {
            feedDao = getIntent().getParcelableExtra(FEED_TAG);
            feedDialog = new DeleteFeedDialog(this, feedDao.getBusinessId(), feedDao.getId(), this);
            caption.setText(feedDao.getCaption());
            code.setText(feedDao.getRedeeemCode());
            Glide.with(this)
                    .load(feedDao.getImageURL())
                    .error(R.drawable.ic_shop)
                    .placeholder(R.drawable.ic_shop)
                    .centerCrop()
                    .into(image);
        }
    }

    @OnClick(R.id.feed_edit_btn)
    public void onFeedEditClicked() {
        switch (appPreferences.getUserType()) {
            case BUSINESS:
                Intent intent = new Intent(this, BusinessAddFeedActivity.class);
                intent.putExtra(FEED_TAG, feedDao);
                startActivity(intent);
                break;
            case CONSUMER:
                break;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.feed_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
            case R.id.navigation_delete:
                feedDialog.show();
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
