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
    private BusinessDao businessDao;

    public BookmarksTask(int id, Context context, ServerResponseListener serverResponseListener) {
        super(id, context, serverResponseListener);
        businessDao = new BusinessDao();
    }

    @Override
    protected ServerEvents doInBackground(Object... objects) {
        if(getNetworkUtils().isNetworkAvailable()) {
            try {
                JSONObject dato = (JSONObject) objects[0];
                Map<String, String> data = new HashMap<>();
                data.put("businessId", dato.get("id").toString());
                String url = StrSubstitutor.replace(URLConstants.CONSUMER_ADD_BOOKMARK_URL, data);
                return getServerResponse(url);
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

    private ServerEvents getServerResponse(String url) throws Exception {
        HttpURLConnection httppost = getNetworkUtils().getHttpURLConInstance(
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
        JSONObject response = (JSONObject) getParser().parse(sb.toString());
        return ServerEvents.SUCCESS;
    }
}

