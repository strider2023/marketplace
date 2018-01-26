package com.touchmenotapps.marketplace.threads.asynctasks;

import android.content.Context;

import com.touchmenotapps.marketplace.framework.BaseAppTask;
import com.touchmenotapps.marketplace.framework.enums.ServerEvents;
import com.touchmenotapps.marketplace.framework.interfaces.ServerResponseListener;

/**
 * Created by arindamnath on 26/01/18.
 */

public class GetInsightsTask extends BaseAppTask {

    public GetInsightsTask(int id, Context context, ServerResponseListener serverResponseListener) {
        super(id, context, serverResponseListener);
    }

    @Override
    protected ServerEvents doInBackground(Object... objects) {
        return null;
    }
}
