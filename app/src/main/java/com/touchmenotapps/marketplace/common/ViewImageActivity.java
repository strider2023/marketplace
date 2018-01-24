package com.touchmenotapps.marketplace.common;

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

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import uk.co.senab.photoview.PhotoViewAttacher;

import static com.touchmenotapps.marketplace.framework.constants.AppConstants.IMAGE_CAPTION_TAG;
import static com.touchmenotapps.marketplace.framework.constants.AppConstants.IMAGE_URL_TAG;

public class ViewImageActivity extends AppCompatActivity {

    @BindView(R.id.view_image)
    ImageView image;
    @BindView(R.id.image_caption)
    AppCompatTextView caption;

    private PhotoViewAttacher photoAttacher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_image);
        ButterKnife.bind(this);

        if(getIntent().getStringExtra(IMAGE_URL_TAG) != null) {
            Glide.with(this)
                    .load(getIntent().getStringExtra(IMAGE_URL_TAG))
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
        }

        if(getIntent().getStringExtra(IMAGE_CAPTION_TAG) != null) {
            caption.setText(getIntent().getStringExtra(IMAGE_CAPTION_TAG));
        } else {
            caption.setVisibility(View.GONE);
        }
    }

    @OnClick(R.id.close_view)
    public void onClose() {
        finish();
    }
}
