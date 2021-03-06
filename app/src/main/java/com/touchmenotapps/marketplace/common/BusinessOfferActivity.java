package com.touchmenotapps.marketplace.common;

import android.content.ContentResolver;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.FileProvider;
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
import com.touchmenotapps.marketplace.framework.PermissionsUtil;
import com.touchmenotapps.marketplace.framework.constants.AppConstants;
import com.touchmenotapps.marketplace.framework.enums.RequestType;
import com.touchmenotapps.marketplace.threads.loaders.BusinessLoaderTask;
import com.touchmenotapps.marketplace.threads.asynctasks.FeedTask;
import com.touchmenotapps.marketplace.common.interfaces.ImageEndcoderListener;
import com.touchmenotapps.marketplace.threads.asynctasks.GetEncodedImageTask;
import com.touchmenotapps.marketplace.framework.enums.LoaderID;
import com.touchmenotapps.marketplace.framework.enums.ServerEvents;
import com.touchmenotapps.marketplace.bo.OffersDao;
import com.touchmenotapps.marketplace.framework.interfaces.ServerResponseListener;

import org.json.simple.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnItemSelected;

import static android.Manifest.permission.CAMERA;
import static android.Manifest.permission.READ_EXTERNAL_STORAGE;
import static com.touchmenotapps.marketplace.framework.constants.AppConstants.BUSINESS_ID_TAG;
import static com.touchmenotapps.marketplace.framework.constants.AppConstants.FEED_TAG;

public class BusinessOfferActivity extends AppCompatActivity
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
    private OffersDao offersDao;
    private Bitmap selectedImage;
    private boolean isImageAdded = false;
    private Bundle queryData;
    private ArrayAdapter<String> businessAdapter;
    private List<BusinessDao> businessDaoList = new ArrayList<>();
    private FeedTask feedTask;
    private boolean isUpdate, isCameraEnabled;
    private Uri imageUri;
    private PermissionsUtil permissionsUtil;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_offer);
        ButterKnife.bind(this);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        permissionsUtil = new PermissionsUtil(this);

        offersDao = new OffersDao();
        offersDao.setStartDateFromToday(1l);
        offersDao.setEndDateFromToday(7l);

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

            offersDao = getIntent().getParcelableExtra(FEED_TAG);
            businessId = offersDao.getBusinessId();
            description.setText(offersDao.getCaption());
            Glide.with(this)
                    .load(offersDao.getImageURL())
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
        isCameraEnabled = permissionsUtil.checkCameraPermission(feedImage);
        if(isCameraEnabled) {
            captureImage();
        } else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions(new String[]{CAMERA, READ_EXTERNAL_STORAGE}, AppConstants.REQUEST_ACCESS_CAMERA);
            }
        }
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
                offersDao.setStartDateFromToday(0l);
                break;
            case 1:
                offersDao.setStartDateFromToday(1l);
                break;
            case 2:
                offersDao.setStartDateFromToday(7l);
                break;
        }
    }

    @OnItemSelected(R.id.feed_end_date)
    public void endDateSelected(Spinner spinner, int position) {
        switch (position) {
            case 0:
                offersDao.setEndDateFromToday(7l);
                break;
            case 1:
                offersDao.setEndDateFromToday(14l);
                break;
            case 2:
                offersDao.setEndDateFromToday(30l);
                break;
        }
    }

    @OnClick(R.id.add_new_feed)
    public void addNewFeed() {
        if (description.getEditableText().toString().trim().length() > 0
                && isImageAdded) {
            offersDao.setCaption(description.getEditableText().toString().trim());
            feedTask = new FeedTask(1, this, this, true);
            if (isUpdate) {
                feedTask.setFeedDetails(businessId, offersDao.getId(), RequestType.PUT);
            } else {
                feedTask.setFeedDetails(businessId, -1l, RequestType.POST);
            }
            feedTask.execute(new JSONObject[]{offersDao.toJSON()});
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
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        if (requestCode == AppConstants.REQUEST_ACCESS_CAMERA) {
            if (grantResults.length == 2 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                isCameraEnabled = true;
                captureImage();
            }
        }
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
        offersDao.setName("Image_" + String.valueOf(System.currentTimeMillis()) + ".jpg");
        offersDao.setData(base64String);
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

    private void captureImage() {
        File photo = new File(Environment.getExternalStorageDirectory(), "Hyfi_" + String.valueOf(System.currentTimeMillis()) + ".jpg");
        imageUri = FileProvider.getUriForFile(this, "hyfi.provider", photo);

        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
        startActivityForResult(intent, TAKE_PICTURE);
    }
}
