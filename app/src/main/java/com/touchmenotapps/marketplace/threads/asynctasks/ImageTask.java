package com.touchmenotapps.marketplace.threads.asynctasks;

import android.content.Context;
import android.util.Log;

import com.touchmenotapps.marketplace.R;
import com.touchmenotapps.marketplace.bo.BusinessImageDao;
import com.touchmenotapps.marketplace.bo.FeedDao;
import com.touchmenotapps.marketplace.framework.BaseAppTask;
import com.touchmenotapps.marketplace.framework.constants.AppConstants;
import com.touchmenotapps.marketplace.framework.constants.URLConstants;
import com.touchmenotapps.marketplace.framework.enums.RequestType;
import com.touchmenotapps.marketplace.framework.enums.ServerEvents;
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
 * Created by i7 on 24-01-2018.
 */

public class ImageTask extends BaseAppTask {

    private String decodedString;
    private String errorMessage;
    private long businessId = -1l;
    private long photoId = -1l;
    private RequestType requestType = RequestType.POST;
    private BusinessImageDao businessImageDao;
    private String serverURL = "";
    private HttpURLConnection httppost;

    public ImageTask(int id, Context context, ServerResponseListener serverResponseListener) {
        super(id, context, serverResponseListener);
        businessImageDao = new BusinessImageDao();
    }

    public void setImageDetails(long businessId, long photoId, RequestType requestType) {
        this.businessId = businessId;
        this.photoId = photoId;
        this.requestType = requestType;
    }

    @Override
    protected ServerEvents doInBackground(Object... objects) {
        if(getNetworkUtils().isNetworkAvailable()) {
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
        data.put("businessId", String.valueOf(businessId));
        if(photoId != -1l) {
            data.put("photoId", String.valueOf(photoId));
        }
        switch (requestType) {
            case PUT:
                serverURL = StrSubstitutor.replace(URLConstants.UDPATE_BUSINESS_PHOTO_URL, data);
                break;
            case POST:
                serverURL = StrSubstitutor.replace(URLConstants.UPLOAD_BUSINESS_PHOTO_URL, data);
                break;
            case DELETE:
                serverURL = StrSubstitutor.replace(URLConstants.DELETE_BUSINESS_PHOTOS_URL, data);
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
        Log.i(AppConstants.APP_TAG, sb.toString());
        return ServerEvents.SUCCESS;
    }
}
