package com.touchmenotapps.marketplace.common.dialogs;

import android.app.Activity;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;

import com.touchmenotapps.marketplace.common.interfaces.BusinessDeleteListener;
import com.touchmenotapps.marketplace.common.threads.DeleteBusinessTask;
import com.touchmenotapps.marketplace.framework.enums.ServerEvents;
import com.touchmenotapps.marketplace.framework.interfaces.ServerResponseListener;

import org.json.simple.JSONObject;

/**
 * Created by i7 on 08-01-2018.
 */

public class DeleteBusinessDailog implements ServerResponseListener {

    private Activity activity;
    private long businessId;
    private AlertDialog dialog;
    private BusinessDeleteListener businessDeleteListener;

    public DeleteBusinessDailog(Activity activity, long businessId, BusinessDeleteListener businessDeleteListener) {
        this.activity = activity;
        this.businessId = businessId;
        this.businessDeleteListener = businessDeleteListener;
        createDialog();
    }

    private void createDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setMessage("Delete this business?");
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                JSONObject business = new JSONObject();
                business.put("id", String.valueOf(businessId));
                new DeleteBusinessTask(2, activity, DeleteBusinessDailog.this)
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
        businessDeleteListener.onBusinessDeletionSuccess();
    }

    @Override
    public void onFaliure(ServerEvents serverEvents, Object object) {
        businessDeleteListener.onBusinessDeletionFailure();
    }
}
