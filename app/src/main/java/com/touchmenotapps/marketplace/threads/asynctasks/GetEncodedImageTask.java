package com.touchmenotapps.marketplace.threads.asynctasks;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Base64;

import com.touchmenotapps.marketplace.common.interfaces.ImageEndcoderListener;

import java.io.ByteArrayOutputStream;

/**
 * Created by i7 on 04-01-2018.
 */

public class GetEncodedImageTask extends AsyncTask<Bitmap, Void, Boolean> {

    private int threadId;
    private ImageEndcoderListener imageEndcoderListener;
    private Bitmap bitmap;
    private String encodedImage;

    public GetEncodedImageTask(int id, ImageEndcoderListener imageEndcoderListener) {
        this.threadId = id;
        this.imageEndcoderListener = imageEndcoderListener;
    }

    @Override
    protected Boolean doInBackground(Bitmap... bitmaps) {
        try {
            bitmap = bitmaps[0];
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 50, baos); //bm is the bitmap object
            byte[] byteArrayImage = baos.toByteArray();
            encodedImage = Base64.encodeToString(byteArrayImage, Base64.DEFAULT);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    protected void onPostExecute(Boolean b) {
        super.onPostExecute(b);
        if(b) {
            imageEndcoderListener.onImageEncoded(threadId, bitmap, encodedImage);
        } else {
            imageEndcoderListener.onImageProcessFailed(threadId);
        }
    }
}
