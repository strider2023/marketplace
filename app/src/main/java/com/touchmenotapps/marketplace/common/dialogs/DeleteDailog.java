package com.touchmenotapps.marketplace.common.dialogs;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;

import com.touchmenotapps.marketplace.common.interfaces.DeleteListener;
import com.touchmenotapps.marketplace.framework.enums.DeleteDialogType;
import com.touchmenotapps.marketplace.framework.enums.RequestType;
import com.touchmenotapps.marketplace.threads.asynctasks.BusinessTask;
import com.touchmenotapps.marketplace.framework.enums.ServerEvents;
import com.touchmenotapps.marketplace.framework.interfaces.ServerResponseListener;
import com.touchmenotapps.marketplace.threads.asynctasks.FeedTask;
import com.touchmenotapps.marketplace.threads.asynctasks.ImageTask;

import org.json.simple.JSONObject;

/**
 * Created by i7 on 08-01-2018.
 */

public class DeleteDailog implements ServerResponseListener {

    private Context context;
    private AlertDialog.Builder builder;
    private AlertDialog dialog;
    private DeleteListener deleteListener;
    private BusinessTask businessTask;
    private FeedTask feedTask;
    private ImageTask imageTask;
    private DeleteDialogType deleteDialogType;

    public DeleteDailog (Context context, DeleteDialogType type, DeleteListener callback) {
        this.context = context;
        this.deleteDialogType = type;
        this.deleteListener = callback;
        builder = new AlertDialog.Builder(context);
        switch (type) {
            case BUSINESS:
                builder.setMessage("Delete this business?");
                break;
            case OFFER:
                builder.setMessage("Delete this offer?");
                break;
            case IMAGE:
                builder.setMessage("Delete this image?");
                break;
        }
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dialog.dismiss();
            }
        });
    }

    public void show(final long businessId) {
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                businessTask = new BusinessTask(1, context, DeleteDailog.this, false);
                businessTask.setBusinessDetails(businessId, RequestType.DELETE);
                businessTask.execute(new JSONObject[]{});
            }
        });
        dialog = builder.create();
        dialog.show();
    }

    public void show(final long businessId, final long itemId) {
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                switch (deleteDialogType) {
                    case OFFER:
                        feedTask = new FeedTask(1, context, DeleteDailog.this, false);
                        feedTask.setFeedDetails(businessId, itemId, RequestType.DELETE);
                        feedTask.execute(new JSONObject[]{});
                        break;
                    case IMAGE:
                        imageTask = new ImageTask(1, context, DeleteDailog.this, false);
                        imageTask.setImageDetails(businessId, itemId, RequestType.DELETE);
                        imageTask.execute(new JSONObject[]{});
                        break;
                }
            }
        });
        dialog = builder.create();
        dialog.show();
    }

    @Override
    public void onSuccess(int threadId, Object object) {
        deleteListener.onBusinessDeletionSuccess();
    }

    @Override
    public void onFaliure(ServerEvents serverEvents, Object object) {
        deleteListener.onBusinessDeletionFailure();
    }
}
