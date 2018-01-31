package com.touchmenotapps.marketplace.common;

import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatEditText;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.touchmenotapps.marketplace.R;
import com.touchmenotapps.marketplace.bo.BusinessImageDao;
import com.touchmenotapps.marketplace.common.interfaces.ImageEndcoderListener;
import com.touchmenotapps.marketplace.framework.PermissionsUtil;
import com.touchmenotapps.marketplace.framework.enums.RequestType;
import com.touchmenotapps.marketplace.framework.enums.ServerEvents;
import com.touchmenotapps.marketplace.framework.interfaces.ServerResponseListener;
import com.touchmenotapps.marketplace.threads.asynctasks.ImageTask;
import com.touchmenotapps.marketplace.threads.asynctasks.GetEncodedImageTask;

import org.json.simple.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.lang.reflect.Method;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.touchmenotapps.marketplace.framework.constants.AppConstants.BUSINESS_ID_TAG;
import static com.touchmenotapps.marketplace.framework.constants.AppConstants.BUSINESS_IMAGE_TAG;

public class BusinessImageActivity extends AppCompatActivity
        implements ServerResponseListener, ImageEndcoderListener {

    private final int SELECT_PHOTO = 1;
    private final int TAKE_PICTURE = 2;

    @BindView(R.id.business_image)
    ImageView image;
    @BindView(R.id.image_caption)
    AppCompatEditText caption;

    private long businessId = -1l;
    private boolean isImageSelected;
    private Bitmap selectedImage;
    private PermissionsUtil permissionsUtil;
    private BusinessImageDao businessImageDao;
    private ImageTask imageTask;
    private boolean isUpdate, isCameraEnabled;
    private Uri imageUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_business_image);
        ButterKnife.bind(this);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        if(Build.VERSION.SDK_INT >= 24){
            try{
                Method m = StrictMode.class.getMethod("disableDeathOnFileUriExposure");
                m.invoke(null);
            }catch(Exception e){
                e.printStackTrace();
            }
        }

        businessImageDao = new BusinessImageDao();
        permissionsUtil = new PermissionsUtil(this);
        isCameraEnabled = permissionsUtil.checkCameraPermission(image);

        if(getIntent().getLongExtra(BUSINESS_ID_TAG, -1l) != -1l) {
            businessId = getIntent().getLongExtra(BUSINESS_ID_TAG, -1l);
        }
        if(getIntent().getParcelableExtra(BUSINESS_IMAGE_TAG) != null) {
            businessImageDao = getIntent().getParcelableExtra(BUSINESS_IMAGE_TAG);
            isUpdate = true;
            Glide.with(this)
                    .load(businessImageDao.getFile())
                    .error(R.drawable.ic_shop)
                    .placeholder(R.drawable.ic_shop)
                    .into(image);
        } else {
            findViewById(R.id.image_btn_holder).setVisibility(View.VISIBLE);
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

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @OnClick(R.id.upload_image)
    public void onUploadSelected() {
        String input = caption.getEditableText().toString().trim();
        if(isUpdate) {
            businessImageDao.setCaption(input);
            imageTask = new ImageTask(2, this, this, true);
            imageTask.setImageDetails(businessId, businessImageDao.getId(), RequestType.PUT);
            imageTask.execute(new JSONObject[]{businessImageDao.toJSON()});
        } else {
            if (isImageSelected) {
                businessImageDao.setCaption(input);
                imageTask = new ImageTask(1, this, this, true);
                imageTask.setImageDetails(businessId, -1l, RequestType.POST);
                imageTask.execute(new JSONObject[]{businessImageDao.toJSON()});
            } else {
                Snackbar.make(image, "Please select an image.", Snackbar.LENGTH_LONG).show();
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
                        isImageSelected = true;
                        final Uri imageUri = imageReturnedIntent.getData();
                        final InputStream imageStream = getContentResolver().openInputStream(imageUri);
                        selectedImage = BitmapFactory.decodeStream(imageStream);
                        image.setImageBitmap(selectedImage);
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
                        isImageSelected = true;
                        getContentResolver().notifyChange(imageUri, null);
                        ContentResolver cr = getContentResolver();
                        selectedImage = android.provider.MediaStore.Images.Media.getBitmap(cr, imageUri);
                        image.setImageBitmap(selectedImage);
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
        businessImageDao.setData(base64String);
        businessImageDao.setName("Image.jpg");
    }

    @Override
    public void onImageDecoded(int id, Bitmap bitmap) {

    }

    @Override
    public void onImageProcessFailed(int id) {

    }

    @Override
    public void onSuccess(int threadId, Object object) {
        finish();
    }

    @Override
    public void onFaliure(ServerEvents serverEvents, Object object) {
        Snackbar.make(image, object.toString(), Snackbar.LENGTH_LONG).show();
    }
}
