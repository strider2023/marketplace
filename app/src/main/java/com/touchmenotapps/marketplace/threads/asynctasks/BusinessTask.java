package com.touchmenotapps.marketplace.threads.asynctasks;

import android.content.Context;
import android.util.Log;

import com.touchmenotapps.marketplace.R;
import com.touchmenotapps.marketplace.bo.BusinessDao;
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
 * Created by arindamnath on 30/12/17.
 */

public class BusinessTask extends BaseAppTask {

    private String decodedString;
    private String errorMessage;
    private long businessId = -1l;
    private RequestType requestType = RequestType.POST;
    private BusinessDao businessDao;
    private String serverURL = "";
    private HttpURLConnection httppost;

    public BusinessTask(int id, Context context, ServerResponseListener serverResponseListener, boolean showLoader) {
        super(id, context, serverResponseListener, showLoader);
        businessDao = new BusinessDao();
    }

    public void setBusinessDetails(long businessId, RequestType requestType) {
        this.businessId = businessId;
        this.requestType = requestType;
    }

    @Override
    protected ServerEvents doInBackground(Object... objects) {
        if(getNetworkUtils().isNetworkAvailable()) {
            try {
                JSONObject dato = new JSONObject();
                if(objects.length > 0) {
                    dato = (JSONObject) objects[0];
                    Log.i(AppConstants.APP_TAG, dato.toJSONString());
                }
                configureHTTPConnection();
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
                getServerResponseListener().onSuccess(getId(), businessDao);
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
        switch (requestType) {
            case GET:
                if(getAppPreferences().getUserType() == UserType.BUSINESS) {
                    serverURL = StrSubstitutor.replace(URLConstants.GET_BUSINESS_BY_ID_URL, data);
                } else {
                    serverURL = StrSubstitutor.replace(URLConstants.CONSUMER_GET_BUSINESS_INFO_URL, data);
                }
                break;
            case PUT:
                serverURL = StrSubstitutor.replace(URLConstants.UPDATE_BUSINESS_URL, data);
                break;
            case POST:
                if(getAppPreferences().getUserType() == UserType.BUSINESS) {
                    serverURL = URLConstants.ADD_BUSINESS_URL;
                } else {
                    serverURL = URLConstants.CONSUMER_ADD_BUSINESS_URL;
                }
                break;
            case DELETE:
                serverURL = StrSubstitutor.replace(URLConstants.DELETE_BUSINESS_URL, data);
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
        if(requestType == RequestType.GET) {
            JSONObject response = (JSONObject) getParser().parse(sb.toString());
            businessDao.parse(getParser(), response);
        }
        return ServerEvents.SUCCESS;
    }
}
