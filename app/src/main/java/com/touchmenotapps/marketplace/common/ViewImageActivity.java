package com.touchmenotapps.marketplace.common;

import android.content.Intent;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatTextView;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.touchmenotapps.marketplace.R;
import com.touchmenotapps.marketplace.bo.BusinessImageDao;
import com.touchmenotapps.marketplace.common.dialogs.DeleteImageDialog;
import com.touchmenotapps.marketplace.common.interfaces.FeedDeleteListener;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import uk.co.senab.photoview.PhotoViewAttacher;

import static com.touchmenotapps.marketplace.framework.constants.AppConstants.BUSINESS_ID_TAG;
import static com.touchmenotapps.marketplace.framework.constants.AppConstants.BUSINESS_IMAGE_TAG;

public class ViewImageActivity extends AppCompatActivity
    implements FeedDeleteListener{

    @BindView(R.id.view_image)
    ImageView image;
    @BindView(R.id.image_caption)
    AppCompatTextView caption;

    private BusinessImageDao businessImageDao;
    private PhotoViewAttacher photoAttacher;
    private DeleteImageDialog deleteImageDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_image);
        ButterKnife.bind(this);

        if(getIntent().getParcelableExtra(BUSINESS_IMAGE_TAG) != null) {
            businessImageDao = getIntent().getParcelableExtra(BUSINESS_IMAGE_TAG);
            if(!businessImageDao.isCanDelete()) {
                findViewById(R.id.edit_image).setVisibility(View.GONE);
                findViewById(R.id.delete_image).setVisibility(View.GONE);
            }
            Glide.with(this)
                    .load(businessImageDao.getFile())
                    .error(R.drawable.ic_shop)
                    .placeholder(R.drawable.ic_shop)
                    .listener(new RequestListener<String, GlideDrawable>() {
                        @Override
                        public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
                            return false;
                        }

                        @Override
                        public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
                            photoAttacher= new PhotoViewAttacher(image);
                            photoAttacher.update();
                            return false;
                        }
                    })
                    .into(image);
            if(businessImageDao.getCaption().length() > 0) {
                caption.setText(businessImageDao.getCaption());
            } else {
                caption.setVisibility(View.GONE);
            }
            deleteImageDialog = new DeleteImageDialog(this,
                    getIntent().getLongExtra(BUSINESS_ID_TAG, -1l),
                    businessImageDao.getId(), this);
        }
    }

    @OnClick(R.id.close_view)
    public void onClose() {
        finish();
    }

    @OnClick(R.id.edit_image)
    public void onEdit() {
        Intent intent = new Intent(this, BusinessImageActivity.class);
        intent.putExtra(BUSINESS_ID_TAG, getIntent().getLongExtra(BUSINESS_ID_TAG, -1l));
        intent.putExtra(BUSINESS_IMAGE_TAG, businessImageDao);
        startActivity(intent);
    }

    @OnClick(R.id.delete_image)
    public void onDelete() {
        deleteImageDialog.show();
    }

    @Override
    public void onBusinessDeletionSuccess() {
        finish();
    }

    @Override
    public void onBusinessDeletionFailure() {
        Snackbar.make(image, "Unable to delete file.", Snackbar.LENGTH_LONG).show();
    }
}
