package com.touchmenotapps.marketplace.threads.asynctasks;

import android.content.Context;
import android.util.Log;

import com.touchmenotapps.marketplace.R;
import com.touchmenotapps.marketplace.bo.FeedDao;
import com.touchmenotapps.marketplace.framework.BaseAppTask;
import com.touchmenotapps.marketplace.framework.constants.AppConstants;
import com.touchmenotapps.marketplace.framework.constants.URLConstants;
import com.touchmenotapps.marketplace.framework.enums.RequestType;
import com.touchmenotapps.marketplace.framework.enums.ServerEvents;
import com.touchmenotapps.marketplace.framework.enums.UserType;
import com.touchmenotapps.marketplace.framework.interfaces.ServerResponseListener;

import org.apache.commons.lang3.text.StrSubstitutor;
import org.json.simple.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by i7 on 03-01-2018.
 */

public class FeedTask extends BaseAppTask {

    private String decodedString;
    private String errorMessage;
    private long businessId = -1l;
    private long feedId = -1l;
    private RequestType requestType = RequestType.POST;
    private FeedDao feedDao;
    private String serverURL = "";
    private HttpURLConnection httppost;

    public FeedTask(int id, Context context, ServerResponseListener serverResponseListener) {
        super(id, context, serverResponseListener);
        feedDao = new FeedDao();
    }

    public void setFeedDetails(long businessId, long feedId, RequestType requestType) {
        this.businessId = businessId;
        this.feedId = feedId;
        this.requestType = requestType;
    }

    @Override
    protected ServerEvents doInBackground(Object... objects) {
        if (getNetworkUtils().isNetworkAvailable()) {
            try {
                configureHTTPConnection();
                JSONObject dato = new JSONObject();
                if(objects.length > 0) {
                    dato = (JSONObject) objects[0];
                    Log.i(AppConstants.APP_TAG, dato.toJSONString());
                }
                return getServerResponse(dato);
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
                getServerResponseListener().onSuccess(getId(), feedDao);
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
        data.put("businessId", String.valueOf(businessId));
        if(feedId != -1l) {
            data.put("feedId", String.valueOf(feedId));
        }
        switch (requestType) {
            case PUT:
                serverURL = StrSubstitutor.replace(URLConstants.UPDATE_BUSINESS_FEED_URL, data);
                break;
            case POST:
                serverURL = StrSubstitutor.replace(URLConstants.CREATE_BUSINESS_FEED_URL, data);
                break;
            case DELETE:
                serverURL = StrSubstitutor.replace(URLConstants.DELETE_BUSINESS_FEED_URL, data);
                break;
        }
        httppost = getNetworkUtils().getHttpURLConInstance(
                getContext().getString(R.string.base_url) + serverURL, requestType);
        httppost.setRequestProperty("uuid", getAppPreferences().getUserToken());
        httppost.setRequestProperty("did", getDeviceId());
    }

    private ServerEvents getServerResponse(JSONObject object) throws Exception {
        if(requestType == RequestType.PUT || requestType == RequestType.POST) {
            DataOutputStream out = new DataOutputStream(httppost.getOutputStream());
            out.writeBytes(object.toString());
            out.flush();
            out.close();
        }
        StringBuilder sb = new StringBuilder();
        BufferedReader in = new BufferedReader(new InputStreamReader(
                httppost.getInputStream()));
        while ((decodedString = in.readLine()) != null)
            sb.append(decodedString);
        in.close();
        int statusCode = httppost.getResponseCode();
        Log.i(AppConstants.APP_TAG, String.valueOf(statusCode));
        return ServerEvents.SUCCESS;
    }
}
