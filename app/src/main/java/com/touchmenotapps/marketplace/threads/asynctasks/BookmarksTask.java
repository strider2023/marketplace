package com.touchmenotapps.marketplace.threads.asynctasks;

import android.content.Context;
import android.util.Log;

import com.touchmenotapps.marketplace.R;
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

/**
 * Created by arindamnath on 07/01/18.
 */

public class BookmarksTask extends BaseAppTask {

    private String decodedString;
    private String errorMessage;
    private long businessId = -1l;
    private long bookmarkId = -1l;
    private RequestType requestType = RequestType.POST;
    private HttpURLConnection httppost;
    private String serverURL = "";

    public BookmarksTask(int id, Context context, ServerResponseListener serverResponseListener, boolean showLoader) {
        super(id, context, serverResponseListener, showLoader);
    }

    public void setBusinessId(long businessId) {
        this.businessId = businessId;
        requestType = RequestType.POST;
    }

    public void setBookmarkId(long bookmarkId) {
        this.bookmarkId = bookmarkId;
        requestType = RequestType.DELETE;
    }

    @Override
    protected ServerEvents doInBackground(Object... objects) {
        if(getNetworkUtils().isNetworkAvailable()) {
            try {
                configureHTTPConnection();
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
                getServerResponseListener().onSuccess(getId(), null);
                break;
            case FAILURE:
                getServerResponseListener().onFaliure(ServerEvents.FAILURE, errorMessage);
                break;
            case NO_NETWORK:
                getServerResponseListener().onFaliure(ServerEvents.NO_NETWORK, errorMessage);
                break;
        }
    }

    private void configureHTTPConnection() throws Exception {
        Map<String, String> data = new HashMap<>();
        if(businessId != -1l) {
            data.put("businessId", String.valueOf(businessId));
        }
        if(bookmarkId != -1l) {
            data.put("feedId", String.valueOf(bookmarkId));
        }
        switch (requestType) {
            case POST:
                serverURL = StrSubstitutor.replace(URLConstants.CONSUMER_ADD_BOOKMARK_URL, data);
                break;
            case DELETE:
                serverURL = StrSubstitutor.replace(URLConstants.CONSUMER_DELETE_BOOKMARK_URL, data);
                break;
        }
        httppost = getNetworkUtils().getHttpURLConInstance(
                getContext().getString(R.string.base_url) + serverURL, requestType);
        httppost.setRequestProperty("uuid", getAppPreferences().getUserToken());
        httppost.setRequestProperty("did", getDeviceId());
    }

    private ServerEvents getServerResponse() throws Exception {
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

