package com.touchmenotapps.marketplace.common.dialogs;

import android.app.Activity;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;

import com.touchmenotapps.marketplace.common.interfaces.FeedDeleteListener;
import com.touchmenotapps.marketplace.threads.asynctasks.DeleteFeedTask;
import com.touchmenotapps.marketplace.framework.enums.ServerEvents;
import com.touchmenotapps.marketplace.framework.interfaces.ServerResponseListener;

import org.json.simple.JSONObject;

/**
 * Created by i7 on 22-01-2018.
 */

public class DeleteFeedDialog implements ServerResponseListener {

    private Activity activity;
    private long businessId, feedId;
    private AlertDialog dialog;
    private FeedDeleteListener feedDeleteListener;

    public DeleteFeedDialog(Activity activity, long businessId, long feedId, FeedDeleteListener feedDeleteListener) {
        this.activity = activity;
        this.businessId = businessId;
        this.feedId = feedId;
        this.feedDeleteListener = feedDeleteListener;
        createDialog();
    }

    private void createDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setMessage("Delete this business?");
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                JSONObject business = new JSONObject();
                business.put("id", String.valueOf(businessId));
                business.put("feedId", String.valueOf(feedId));
                new DeleteFeedTask(2, activity, DeleteFeedDialog.this)
                        .execute(new JSONObject[]{business});
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
