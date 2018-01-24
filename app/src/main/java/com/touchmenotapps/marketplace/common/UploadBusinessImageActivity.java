package com.touchmenotapps.marketplace.common;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatEditText;
import android.view.MenuItem;
import android.widget.ImageView;

import com.touchmenotapps.marketplace.R;
import com.touchmenotapps.marketplace.bo.BusinessImageDao;
import com.touchmenotapps.marketplace.common.interfaces.ImageEndcoderListener;
import com.touchmenotapps.marketplace.framework.enums.ServerEvents;
import com.touchmenotapps.marketplace.framework.interfaces.ServerResponseListener;
import com.touchmenotapps.marketplace.threads.asynctasks.AddImageTask;
import com.touchmenotapps.marketplace.threads.asynctasks.GetEncodedImageTask;

import org.json.simple.JSONObject;

import java.io.FileNotFoundException;
import java.io.InputStream;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.touchmenotapps.marketplace.framework.constants.AppConstants.BUSINESS_ID_TAG;

public class UploadBusinessImageActivity extends AppCompatActivity
        implements ServerResponseListener, ImageEndcoderListener {

    private final int SELECT_PHOTO = 1;

    @BindView(R.id.business_image)
    ImageView image;
    @BindView(R.id.image_caption)
    AppCompatEditText caption;

    private long businessId = -1l;
    private boolean isImageSelected;
    private Bitmap selectedImage;
    private BusinessImageDao businessImageDao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_business_image);
        ButterKnife.bind(this);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        businessImageDao = new BusinessImageDao();

        if(getIntent().getLongExtra(BUSINESS_ID_TAG, -1l) != -1l) {
            businessId = getIntent().getLongExtra(BUSINESS_ID_TAG, -1l);
        }

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
        if(isImageSelected && input.length() > 0) {
            businessImageDao.setCaption("Image");
            new AddImageTask(4, this, this, businessId)
                    .execute(new JSONObject[]{businessImageDao.toJSON()});
        } else {
            Snackbar.make(image, "Invalid Input", Snackbar.LENGTH_LONG).show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent imageReturnedIntent) {
        super.onActivityResult(requestCode, resultCode, imageReturnedIntent);
        if (requestCode == SELECT_PHOTO && resultCode == RESULT_OK) {
            try {
                final Uri imageUri = imageReturnedIntent.getData();
                final InputStream imageStream = getContentResolver().openInputStream(imageUri);
                isImageSelected = true;
                selectedImage = BitmapFactory.decodeStream(imageStream);
                image.setImageBitmap(selectedImage);
                new GetEncodedImageTask(1, this)
                        .execute(new Bitmap[]{selectedImage});
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        } else {
            finish();
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
