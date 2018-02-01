package com.touchmenotapps.marketplace.threads.asynctasks;

import android.content.Context;
import android.util.Log;

import com.touchmenotapps.marketplace.R;
import com.touchmenotapps.marketplace.bo.CategoryListDao;
import com.touchmenotapps.marketplace.framework.BaseAppTask;
import com.touchmenotapps.marketplace.framework.constants.AppConstants;
import com.touchmenotapps.marketplace.framework.constants.URLConstants;
import com.touchmenotapps.marketplace.framework.enums.RequestType;
import com.touchmenotapps.marketplace.framework.enums.ServerEvents;
import com.touchmenotapps.marketplace.framework.interfaces.ServerResponseListener;

import org.apache.commons.lang3.text.StrSubstitutor;
import org.json.simple.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.util.HashMap;
import java.util.Map;

import static com.touchmenotapps.marketplace.framework.constants.AppConstants.KPI_ADDRESS;
import static com.touchmenotapps.marketplace.framework.constants.AppConstants.KPI_FEED;
import static com.touchmenotapps.marketplace.framework.constants.AppConstants.KPI_PHONE;

/**
 * Created by arindamnath on 26/01/18.
 */

public class UserKPITask extends BaseAppTask {

    private String decodedString;
    private String errorMessage;
    private CategoryListDao categoryDao;
    private long businessId = -1l;
    private long feedId = -1l;
    private int type;
    private HttpURLConnection httppost;

    public UserKPITask(int id, Context context, ServerResponseListener serverResponseListener, boolean showLoader) {
        super(id, context, serverResponseListener, showLoader);
        categoryDao = new CategoryListDao();
    }

    public void setBusinessId(long businessId) {
        this.businessId = businessId;
    }

    public void setFeedId(long feedId) {
        this.feedId = feedId;
    }

    public void setType(int type) {
        this.type = type;
    }

    @Override
    protected ServerEvents doInBackground(Object... objects) {
        if(getNetworkUtils().isNetworkAvailable()) {
            try {
                return getServerResponse();
            } catch (Exception e) {
                e.printStackTrace();
                Log.e(AppConstants.APP_TAG, e.getMessage());
                errorMessage = "Oops something went wrong!";
                return ServerEvents.FAILURE;
            }
        } else {
            errorMessage = "Oops! Unable to connect to the internet.";
            return ServerEvents.NO_NETWORK;
        }
    }

    @Override
    protected void onPostExecute(ServerEvents serverEvents) {
        super.onPostExecute(serverEvents);
        switch (serverEvents) {
            case SUCCESS:
                getServerResponseListener().onSuccess(getId(), categoryDao);
                break;
            case FAILURE:
                getServerResponseListener().onFaliure(ServerEvents.FAILURE, errorMessage);
                break;
            case NO_NETWORK:
                getServerResponseListener().onFaliure(ServerEvents.NO_NETWORK, errorMessage);
                break;
        }
    }

    private ServerEvents getServerResponse() throws Exception {
        String url = "";
        Map<String, String> data = new HashMap<>();
        data.put("businessId", String.valueOf(businessId));
        if(feedId != -1l) {
            data.put("feedId", String.valueOf(feedId));
        }
        switch (type) {
            case KPI_PHONE:
                url = StrSubstitutor.replace(URLConstants.KPI_PHONE, data);
                break;
            case KPI_ADDRESS:
                url = StrSubstitutor.replace(URLConstants.KPI_ADDRESS, data);
                break;
            case KPI_FEED:
                url = StrSubstitutor.replace(URLConstants.KPI_FEED, data);
                break;
        }
        httppost = getNetworkUtils().getHttpURLConInstance(
                getContext().getString(R.string.base_url) + url, RequestType.POST);
        httppost.setRequestProperty("uuid", getAppPreferences().getUserToken());
        httppost.setRequestProperty("did", getDeviceId());

        StringBuilder sb = new StringBuilder();
        BufferedReader in = new BufferedReader(new InputStreamReader(
                httppost.getInputStream()));
        while ((decodedString = in.readLine()) != null)
            sb.append(decodedString);
        in.close();
        Log.i(AppConstants.APP_TAG, sb.toString());
        return ServerEvents.SUCCESS;
    }
}
