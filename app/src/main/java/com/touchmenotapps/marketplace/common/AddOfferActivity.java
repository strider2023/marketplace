package com.touchmenotapps.marketplace.common;

import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.design.widget.Snackbar;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.AppCompatSpinner;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;

import com.bumptech.glide.Glide;
import com.touchmenotapps.marketplace.R;
import com.touchmenotapps.marketplace.bo.BusinessDao;
import com.touchmenotapps.marketplace.framework.enums.RequestType;
import com.touchmenotapps.marketplace.threads.loaders.BusinessLoaderTask;
import com.touchmenotapps.marketplace.threads.asynctasks.FeedTask;
import com.touchmenotapps.marketplace.common.interfaces.ImageEndcoderListener;
import com.touchmenotapps.marketplace.threads.asynctasks.GetEncodedImageTask;
import com.touchmenotapps.marketplace.framework.enums.LoaderID;
import com.touchmenotapps.marketplace.framework.enums.ServerEvents;
import com.touchmenotapps.marketplace.bo.FeedDao;
import com.touchmenotapps.marketplace.framework.interfaces.ServerResponseListener;

import org.json.simple.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnItemSelected;

import static com.touchmenotapps.marketplace.framework.constants.AppConstants.BUSINESS_ID_TAG;
import static com.touchmenotapps.marketplace.framework.constants.AppConstants.FEED_TAG;

public class AddOfferActivity extends AppCompatActivity
        implements ServerResponseListener, ImageEndcoderListener,
        LoaderManager.LoaderCallbacks<List<BusinessDao>> {

    private final int SELECT_PHOTO = 1;
    private final int TAKE_PICTURE = 2;

    @BindView(R.id.feed_image)
    ImageView feedImage;
    @BindView(R.id.feed_description)
    AppCompatEditText description;
    @BindView(R.id.feed_start_date)
    AppCompatSpinner startDate;
    @BindView(R.id.feed_end_date)
    AppCompatSpinner endDate;
    @BindView(R.id.feed_select_business)
    AppCompatSpinner selectBusiness;
    @BindView(R.id.add_new_feed)
    AppCompatButton addButton;

    private long businessId = -1l;
    private FeedDao feedDao;
    private Bitmap selectedImage;
    private boolean isImageAdded = false;
    private Bundle queryData;
    private ArrayAdapter<String> businessAdapter;
    private List<BusinessDao> businessDaoList = new ArrayList<>();
    private FeedTask feedTask;
    private boolean isUpdate;
    private File photo;
    private Uri imageUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_business_feed);
        ButterKnife.bind(this);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        feedDao = new FeedDao();
        feedDao.setStartDateFromToday(1l);
        feedDao.setEndDateFromToday(7l);

        //If activity is launched without a business pre-selected, then show menu by fetching data
        if (getIntent().getLongExtra(BUSINESS_ID_TAG, -1l) != -1l) {
            findViewById(R.id.business_selector_container).setVisibility(View.GONE);
            businessId = getIntent().getLongExtra(BUSINESS_ID_TAG, -1l);
        } else if (getIntent().getParcelableExtra(FEED_TAG) != null) {
            findViewById(R.id.business_selector_container).setVisibility(View.GONE);
            findViewById(R.id.image_btn_holder).setVisibility(View.GONE);
            findViewById(R.id.create_container).setVisibility(View.GONE);
            startDate.setClickable(false);
            endDate.setClickable(false);
            addButton.setText("Update Feed");
            isImageAdded = true;
            isUpdate = true;

            feedDao = getIntent().getParcelableExtra(FEED_TAG);
            businessId = feedDao.getBusinessId();
            description.setText(feedDao.getCaption());
            Glide.with(this)
                    .load(feedDao.getImageURL())
                    .error(R.drawable.ic_shop)
                    .placeholder(R.drawable.ic_shop)
                    .centerCrop()
                    .into(feedImage);
        } else {
            findViewById(R.id.business_selector_container).setVisibility(View.VISIBLE);
            queryData = new Bundle();
            getSupportLoaderManager().initLoader(LoaderID.FETCH_MY_BUSINESS.getValue(), queryData, this).forceLoad();
        }
    }

    @OnClick(R.id.take_image)
    public void onTakeImage() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        File photo = new File(Environment.getExternalStorageDirectory(), "Hyfi_" + String.valueOf(System.currentTimeMillis())+ ".jpg");
        intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(photo));
        imageUri = Uri.fromFile(photo);
        startActivityForResult(intent, TAKE_PICTURE);
    }

    @OnClick(R.id.select_image)
    public void onSelectImage() {
        Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
        photoPickerIntent.setType("image/*");
        startActivityForResult(photoPickerIntent, SELECT_PHOTO);
    }

    @OnItemSelected(R.id.feed_select_business)
    public void startBusinessSelected(Spinner spinner, int position) {
        businessId = businessDaoList.get(position).getId();
    }

    @OnItemSelected(R.id.feed_start_date)
    public void startDateSelected(Spinner spinner, int position) {
        switch (position) {
            case 0:
                feedDao.setStartDateFromToday(0l);
                break;
            case 1:
                feedDao.setStartDateFromToday(1l);
                break;
            case 2:
                feedDao.setStartDateFromToday(7l);
                break;
        }
    }

    @OnItemSelected(R.id.feed_end_date)
    public void endDateSelected(Spinner spinner, int position) {
        switch (position) {
            case 0:
                feedDao.setEndDateFromToday(7l);
                break;
            case 1:
                feedDao.setEndDateFromToday(14l);
                break;
            case 2:
                feedDao.setEndDateFromToday(30l);
                break;
        }
    }

    @OnClick(R.id.add_new_feed)
    public void addNewFeed() {
        if (description.getEditableText().toString().trim().length() > 0
                && isImageAdded) {
            feedDao.setCaption(description.getEditableText().toString().trim());
            feedTask = new FeedTask(1, this, this, true);
            if (isUpdate) {
                feedTask.setFeedDetails(businessId, feedDao.getId(), RequestType.PUT);
            } else {
                feedTask.setFeedDetails(businessId, -1l, RequestType.POST);
            }
            feedTask.execute(new JSONObject[]{feedDao.toJSON()});
        } else {
            Snackbar.make(description, "Description and image fields cannot be empty.", Snackbar.LENGTH_LONG).show();
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent imageReturnedIntent) {
        super.onActivityResult(requestCode, resultCode, imageReturnedIntent);
        switch (requestCode) {
            case SELECT_PHOTO:
                if (resultCode == RESULT_OK) {
                    try {
                        final Uri imageUri = imageReturnedIntent.getData();
                        final InputStream imageStream = getContentResolver().openInputStream(imageUri);
                        selectedImage = BitmapFactory.decodeStream(imageStream);
                        feedImage.setImageBitmap(selectedImage);
                        new GetEncodedImageTask(1, this)
                                .execute(new Bitmap[]{selectedImage});
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                }
                break;
            case TAKE_PICTURE:
                if (resultCode == RESULT_OK) {
                    try {
                        getContentResolver().notifyChange(imageUri, null);
                        ContentResolver cr = getContentResolver();
                        selectedImage = android.provider.MediaStore.Images.Media.getBitmap(cr, imageUri);
                        feedImage.setImageBitmap(selectedImage);
                        new GetEncodedImageTask(1, this)
                                .execute(new Bitmap[]{selectedImage});
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                break;
        }
    }

    @Override
    public void onImageEncoded(int id, Bitmap bitmap, String base64String) {
        feedDao.setName("Image_" + String.valueOf(System.currentTimeMillis()) + ".jpg");
        feedDao.setData(base64String);
        isImageAdded = true;
    }

    @Override
    public void onImageDecoded(int id, Bitmap bitmap) {

    }

    @Override
    public void onImageProcessFailed(int id) {
        Snackbar.make(description, "An error occurred.", Snackbar.LENGTH_LONG).show();
    }

    @Override
    public Loader<List<BusinessDao>> onCreateLoader(int id, Bundle args) {
        return new BusinessLoaderTask(this, args);
    }

    @Override
    public void onLoadFinished(Loader<List<BusinessDao>> loader, List<BusinessDao> data) {
        if (data.size() > 0) {
            businessDaoList.clear();
            businessDaoList.addAll(data);
            businessId = businessDaoList.get(0).getId();

            Set<String> names = new HashSet<>();
            for (BusinessDao businessDao : businessDaoList) {
                names.add(businessDao.getName());
            }
            String[] businesses = names.toArray(new String[names.size()]);
            businessAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, businesses);
            businessAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            selectBusiness.setAdapter(businessAdapter);
        }
    }

    @Override
    public void onLoaderReset(Loader<List<BusinessDao>> loader) {

    }
}
