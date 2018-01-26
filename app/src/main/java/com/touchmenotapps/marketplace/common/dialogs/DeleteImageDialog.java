package com.touchmenotapps.marketplace.common.dialogs;

import android.app.Activity;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;

import com.touchmenotapps.marketplace.common.interfaces.FeedDeleteListener;
import com.touchmenotapps.marketplace.framework.enums.RequestType;
import com.touchmenotapps.marketplace.framework.enums.ServerEvents;
import com.touchmenotapps.marketplace.framework.interfaces.ServerResponseListener;
import com.touchmenotapps.marketplace.threads.asynctasks.FeedTask;
import com.touchmenotapps.marketplace.threads.asynctasks.ImageTask;

import org.json.simple.JSONObject;

/**
 * Created by arindamnath on 26/01/18.
 */

public class DeleteImageDialog implements ServerResponseListener {

    private Activity activity;
    private long businessId, photoId;
    private AlertDialog dialog;
    private FeedDeleteListener feedDeleteListener;
    private ImageTask imageTask;

    public DeleteImageDialog(Activity activity, long businessId, long photoId, FeedDeleteListener feedDeleteListener) {
        this.activity = activity;
        this.businessId = businessId;
        this.photoId = photoId;
        this.feedDeleteListener = feedDeleteListener;
        createDialog();
    }

    private void createDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setMessage("Delete this image?");
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                imageTask = new ImageTask(1, activity, DeleteImageDialog.this);
                imageTask.setImageDetails(businessId, photoId, RequestType.DELETE);
                imageTask.execute(new JSONObject[]{});
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dialog.dismiss();
            }
        });
        dialog = builder.create();
    }

    public void show() {
        dialog.show();
    }

    @Override
    public void onSuccess(int threadId, Object object) {
        feedDeleteListener.onBusinessDeletionSuccess();
    }

    @Override
    public void onFaliure(ServerEvents serverEvents, Object object) {
        feedDeleteListener.onBusinessDeletionFailure();
    }
}
