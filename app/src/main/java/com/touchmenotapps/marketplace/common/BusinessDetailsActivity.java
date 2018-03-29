package com.touchmenotapps.marketplace.common;

import android.content.Intent;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;

import com.github.clans.fab.FloatingActionMenu;
import com.touchmenotapps.marketplace.R;
import com.touchmenotapps.marketplace.bo.ViewPagerDao;
import com.touchmenotapps.marketplace.common.adapters.ViewPagerAdapter;
import com.touchmenotapps.marketplace.common.dialogs.DeleteDailog;
import com.touchmenotapps.marketplace.common.fragment.BusinessDetailFragment;
import com.touchmenotapps.marketplace.common.fragment.BusinessOffersFragment;
import com.touchmenotapps.marketplace.common.fragment.BusinessInsightsFragment;
import com.touchmenotapps.marketplace.common.fragment.BusinessPhotosFragment;
import com.touchmenotapps.marketplace.common.interfaces.DeleteListener;
import com.touchmenotapps.marketplace.framework.enums.DeleteDialogType;
import com.touchmenotapps.marketplace.threads.asynctasks.BookmarksTask;
import com.touchmenotapps.marketplace.framework.enums.ServerEvents;
import com.touchmenotapps.marketplace.framework.enums.UserType;
import com.touchmenotapps.marketplace.framework.interfaces.ServerResponseListener;
import com.touchmenotapps.marketplace.framework.persist.AppPreferences;

import org.json.simple.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.touchmenotapps.marketplace.framework.constants.AppConstants.BUSINESS_BOOKMARKED_TAG;
import static com.touchmenotapps.marketplace.framework.constants.AppConstants.BUSINESS_BOOKMARK_ID_TAG;
import static com.touchmenotapps.marketplace.framework.constants.AppConstants.BUSINESS_CATEGORY_TAG;
import static com.touchmenotapps.marketplace.framework.constants.AppConstants.BUSINESS_ID_TAG;
import static com.touchmenotapps.marketplace.framework.constants.AppConstants.BUSINESS_NAME_TAG;
import static com.touchmenotapps.marketplace.framework.constants.AppConstants.BUSINESS_RATING_TAG;

public class BusinessDetailsActivity extends AppCompatActivity
        implements ServerResponseListener, DeleteListener {

    @BindView(R.id.toolbar_layout)
    CollapsingToolbarLayout collapsingToolbarLayout;
    @BindView(R.id.business_deatils_viewpager)
    ViewPager viewPager;
    @BindView(R.id.business_tabs)
    TabLayout tabLayout;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.business_options)
    FloatingActionMenu options;
    @BindView(R.id.business_rating)
    AppCompatTextView rating;
    @BindView(R.id.business_category)
    AppCompatTextView category;
    @BindView(R.id.bookmark_business_button)
    AppCompatTextView bookmark;

    private long businessId = -1l, bookmarkId = -1l;
    private AppPreferences appPreferences;
    private ViewPagerAdapter viewPagerAdapter;
    private List<ViewPagerDao> fragments = new ArrayList<>();
    private DeleteDailog deleteDailog;
    private BookmarksTask bookmarksTask;
    private boolean isBookmarked;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_business_details);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        appPreferences = new AppPreferences(this);
        deleteDailog = new DeleteDailog(this, DeleteDialogType.BUSINESS, this);
        viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());
        viewPager.setAdapter(viewPagerAdapter);
        tabLayout.setupWithViewPager(viewPager);

        businessId = getIntent().getLongExtra(BUSINESS_ID_TAG, -1l);
        rating.setText(String.valueOf(getIntent().getFloatExtra(BUSINESS_RATING_TAG, 0.0f)));

        if(getIntent().getBooleanExtra(BUSINESS_BOOKMARKED_TAG, false)) {
            isBookmarked = true;
            bookmark.setText("Unfollow");
            bookmarkId = getIntent().getLongExtra(BUSINESS_BOOKMARK_ID_TAG, -1l);
        }
        if(getIntent().getStringExtra(BUSINESS_NAME_TAG) != null) {
            getSupportActionBar().setTitle(getIntent().getStringExtra(BUSINESS_NAME_TAG));
        }
        if(getIntent().getStringExtra(BUSINESS_CATEGORY_TAG) != null) {
            category.setText(getIntent().getStringExtra(BUSINESS_CATEGORY_TAG));
        }

        if(appPreferences.getUserType() != UserType.BUSINESS) {
            options.setVisibility(View.GONE);
            findViewById(R.id.bookmark_business_button).setVisibility(View.VISIBLE);
        }

        fragments.add(new ViewPagerDao("Offers", BusinessOffersFragment.newInstance(businessId)));
        if(appPreferences.getUserType() == UserType.BUSINESS) {
            fragments.add(new ViewPagerDao("Insights", BusinessInsightsFragment.newInstance(businessId)));
        }
        fragments.add(new ViewPagerDao("Photos", BusinessPhotosFragment.newInstance(businessId)));
        fragments.add(new ViewPagerDao("About", BusinessDetailFragment.newInstance(businessId)));

        viewPagerAdapter.setFragments(fragments);

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) { }

            @Override
            public void onPageSelected(int position) {
                setFabButton(tabLayout.getTabAt(position));
            }

            @Override
            public void onPageScrollStateChanged(int state) { }
        });
    }

    private void setFabButton(TabLayout.Tab tab) {
        if(tab.getText().toString().equalsIgnoreCase("Offers")) {
            Log.i("Test", tab.getText().toString());
            findViewById(R.id.add_business_feed_button).setVisibility(View.VISIBLE);
            findViewById(R.id.add_business_image_button).setVisibility(View.GONE);
            options.setVisibility(View.GONE);
        }
        if(tab.getText().toString().equalsIgnoreCase("Insights")) {
            findViewById(R.id.add_business_feed_button).setVisibility(View.GONE);
            findViewById(R.id.add_business_image_button).setVisibility(View.GONE);
            options.setVisibility(View.GONE);
        }
        if(tab.getText().toString().equalsIgnoreCase("Photos")) {
            findViewById(R.id.add_business_image_button).setVisibility(View.VISIBLE);
            findViewById(R.id.add_business_feed_button).setVisibility(View.GONE);
            options.setVisibility(View.GONE);
        }
        if(tab.getText().toString().equalsIgnoreCase("About")) {
            if(appPreferences.getUserType() == UserType.BUSINESS) {
                options.setVisibility(View.VISIBLE);
            }
            findViewById(R.id.add_business_feed_button).setVisibility(View.GONE);
            findViewById(R.id.add_business_image_button).setVisibility(View.GONE);
        }
    }

    @OnClick(R.id.add_business_image_button)
    public void onAddImage() {
        options.close(true);
        Intent intent = new Intent(this, BusinessImageActivity.class);
        intent.putExtra(BUSINESS_ID_TAG, businessId);
        startActivity(intent);
    }

    @OnClick(R.id.edit_business_btn)
    public void onViewEdit() {
        options.close(true);
        Intent intent = new Intent(this, BusinessAddActivity.class);
        intent.putExtra(BUSINESS_ID_TAG, businessId);
        intent.putExtra(BUSINESS_NAME_TAG, getSupportActionBar().getTitle().toString());
        startActivity(intent);
    }

    @OnClick(R.id.delete_business_btn)
    public void onViewDelete() {
        options.close(true);
        deleteDailog.show(businessId);
    }

    @OnClick(R.id.bookmark_business_button)
    public void onBusinessBookmark() {
        if(isBookmarked) {
            bookmarksTask = new BookmarksTask(1, this, this, false);
            bookmarksTask.setBookmarkId(bookmarkId);
            bookmarksTask.execute(new JSONObject[]{});
        } else {
            bookmarksTask = new BookmarksTask(2, this, this, false);
            bookmarksTask.setBusinessId(businessId);
            bookmarksTask.execute(new JSONObject[]{});
        }
    }

    @OnClick(R.id.add_business_feed_button)
    public void onAddFeed() {
        Intent intent = new Intent(this, BusinessOfferActivity.class);
        intent.putExtra(BUSINESS_ID_TAG, businessId);
        startActivity(intent);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBusinessDeletionSuccess() {
        finish();
    }

    @Override
    public void onBusinessDeletionFailure() {
        Snackbar.make(options, "Something went wrong.", Snackbar.LENGTH_LONG).show();
    }

    @Override
    public void onSuccess(int threadId, Object object) {
        switch (threadId) {
            case 1:
                bookmark.setText("Follow");
                isBookmarked = false;
                Snackbar.make(options, "Bookmark removed.", Snackbar.LENGTH_LONG).show();
                break;
            case 2:
                bookmark.setText("Unfollow");
                bookmarkId = (long) object;
                isBookmarked = true;
                Snackbar.make(options, "Bookmark saved.", Snackbar.LENGTH_LONG).show();
                break;
        }
    }

    @Override
    public void onFaliure(ServerEvents serverEvents, Object object) {
        Snackbar.make(options, object.toString(), Snackbar.LENGTH_LONG).show();
    }
}
