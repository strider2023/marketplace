package com.touchmenotapps.marketplace.common.interfaces;

import android.graphics.Bitmap;

/**
 * Created by i7 on 04-01-2018.
 */

public interface ImageEndcoderListener {

    void onImageEncoded(int id, Bitmap bitmap, String base64String);

    void onImageDecoded(int id, Bitmap bitmap);

    void onImageProcessFailed(int id);
}
