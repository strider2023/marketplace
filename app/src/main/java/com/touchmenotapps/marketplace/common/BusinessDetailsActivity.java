package com.touchmenotapps.marketplace.common;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;

import com.github.clans.fab.FloatingActionMenu;
import com.touchmenotapps.marketplace.R;
import com.touchmenotapps.marketplace.bo.BusinessImageDao;
import com.touchmenotapps.marketplace.bo.ViewPagerDao;
import com.touchmenotapps.marketplace.business.BusinessAddActivity;
import com.touchmenotapps.marketplace.business.BusinessAddFeedActivity;
import com.touchmenotapps.marketplace.common.adapters.ViewPagerAdapter;
import com.touchmenotapps.marketplace.common.dialogs.DeleteBusinessDailog;
import com.touchmenotapps.marketplace.common.fragment.BusinessDetailFragment;
import com.touchmenotapps.marketplace.common.fragment.BusinessFeedFragment;
import com.touchmenotapps.marketplace.common.interfaces.BusinessDeleteListener;
import com.touchmenotapps.marketplace.common.interfaces.ImageEndcoderListener;
import com.touchmenotapps.marketplace.threads.asynctasks.AddImageTask;
import com.touchmenotapps.marketplace.threads.asynctasks.BookmarksTask;
import com.touchmenotapps.marketplace.framework.enums.ServerEvents;
import com.touchmenotapps.marketplace.framework.enums.UserType;
import com.touchmenotapps.marketplace.framework.interfaces.ServerResponseListener;
import com.touchmenotapps.marketplace.framework.persist.AppPreferences;
import com.touchmenotapps.marketplace.threads.asynctasks.GetEncodedImageTask;

import org.json.simple.JSONObject;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.touchmenotapps.marketplace.framework.constants.AppConstants.BUSINESS_ID_TAG;
import static com.touchmenotapps.marketplace.framework.constants.AppConstants.BUSINESS_NAME_TAG;

public class BusinessDetailsActivity extends AppCompatActivity
        implements ServerResponseListener, BusinessDeleteListener, ImageEndcoderListener {

    private final int SELECT_PHOTO = 1;

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

    private long businessId = -1l;
    private AppPreferences appPreferences;
    private ViewPagerAdapter viewPagerAdapter;
    private Bitmap selectedImage;
    private List<ViewPagerDao> fragments = new ArrayList<>();
    private DeleteBusinessDailog deleteBusinessDailog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_business_details);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        appPreferences = new AppPreferences(this);
        viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());
        viewPager.setAdapter(viewPagerAdapter);
        tabLayout.setupWithViewPager(viewPager);

        if(getIntent().getLongExtra(BUSINESS_ID_TAG, -1l) != -1l) {
            businessId = getIntent().getLongExtra(BUSINESS_ID_TAG, -1l);
        }

        if(getIntent().getStringExtra(BUSINESS_NAME_TAG) != null) {
            getSupportActionBar().setTitle(getIntent().getStringExtra(BUSINESS_NAME_TAG));
        }

        if(appPreferences.getUserType() != UserType.BUSINESS) {
            findViewById(R.id.delete_business_btn).setVisibility(View.GONE);
            findViewById(R.id.edit_business_btn).setVisibility(View.GONE);
            options.setVisibility(View.GONE);
            findViewById(R.id.bookmark_business_feed_button).setVisibility(View.VISIBLE);
        }

        fragments.add(new ViewPagerDao("Offers", BusinessFeedFragment.newInstance(businessId)));
        fragments.add(new ViewPagerDao("About", BusinessDetailFragment.newInstance(businessId)));

        viewPagerAdapter.setFragments(fragments);

        if(businessId != -1l) {
            deleteBusinessDailog = new DeleteBusinessDailog(this, businessId, this);
        }
    }

    @OnClick(R.id.add_business_image_button)
    public void onAddImage() {
        options.close(true);
        Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
        photoPickerIntent.setType("image/*");
        startActivityForResult(photoPickerIntent, SELECT_PHOTO);
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
        deleteBusinessDailog.show();
    }

    @OnClick(R.id.bookmark_business_feed_button)
    public void onBusinessBookmark() {
        JSONObject id = new JSONObject();
        id.put("id", String.valueOf(businessId));
        new BookmarksTask(2, this, this)
                .execute(new JSONObject[]{id});
    }

    @OnClick(R.id.add_business_feed_button)
    public void onAddFeed() {
        options.close(true);
        Intent intent = new Intent(this, BusinessAddFeedActivity.class);
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
            case 2:
                Snackbar.make(options, "Bookmark Saved", Snackbar.LENGTH_LONG).show();
                break;
            case 4:
                Snackbar.make(options, "Image Added", Snackbar.LENGTH_LONG).show();
                break;
        }
    }

    @Override
    public void onFaliure(ServerEvents serverEvents, Object object) {
        Snackbar.make(options, object.toString(), Snackbar.LENGTH_LONG).show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent imageReturnedIntent) {
        super.onActivityResult(requestCode, resultCode, imageReturnedIntent);
        if (requestCode == SELECT_PHOTO && resultCode == RESULT_OK) {
            try {
                final Uri imageUri = imageReturnedIntent.getData();
                final InputStream imageStream = getContentResolver().openInputStream(imageUri);
                selectedImage = BitmapFactory.decodeStream(imageStream);
                new GetEncodedImageTask(1, this)
                        .execute(new Bitmap[]{selectedImage});
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onImageEncoded(int id, Bitmap bitmap, String base64String) {
        BusinessImageDao businessImageDao = new BusinessImageDao();
        businessImageDao.setData(base64String);
        businessImageDao.setName("Image.jpg");
        businessImageDao.setCaption("Image");
        new AddImageTask(4, this, this, businessId)
                .execute(new JSONObject[]{businessImageDao.toJSON()});
    }

    @Override
    public void onImageDecoded(int id, Bitmap bitmap) {

    }

    @Override
    public void onImageProcessFailed(int id) {

    }
}
