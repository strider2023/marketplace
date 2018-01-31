package com.touchmenotapps.marketplace.threads.asynctasks;

import android.content.Context;
import android.util.Log;

import com.touchmenotapps.marketplace.R;
import com.touchmenotapps.marketplace.bo.ProfileDao;
import com.touchmenotapps.marketplace.framework.BaseAppTask;
import com.touchmenotapps.marketplace.framework.constants.AppConstants;
import com.touchmenotapps.marketplace.framework.constants.URLConstants;
import com.touchmenotapps.marketplace.framework.enums.RequestType;
import com.touchmenotapps.marketplace.framework.enums.ServerEvents;
import com.touchmenotapps.marketplace.framework.enums.UserType;
import com.touchmenotapps.marketplace.framework.interfaces.ServerResponseListener;

import org.json.simple.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by i7 on 29-01-2018.
 */

public class ProfileTask extends BaseAppTask {

    private String decodedString;
    private String errorMessage;
    private RequestType requestType = RequestType.GET;
    private ProfileDao profileDao;
    private HttpURLConnection httppost;

    public ProfileTask(int id, Context context, ServerResponseListener serverResponseListener, boolean showLoader) {
        super(id, context, serverResponseListener, showLoader);
        profileDao = new ProfileDao();
    }

    public void setRequestType(RequestType requestType) {
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
                getServerResponseListener().onSuccess(getId(), profileDao);
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
        httppost = getNetworkUtils().getHttpURLConInstance(
                getContext().getString(R.string.base_url) + URLConstants.ACCOUNT_URL, requestType);
        httppost.setRequestProperty("uuid", getAppPreferences().getUserToken());
        httppost.setRequestProperty("did", getDeviceId());
    }

    private ServerEvents getServerResponse(JSONObject object) throws Exception {
        if(requestType == RequestType.PUT) {
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
            profileDao.parse(getParser(), response);
        }
        return ServerEvents.SUCCESS;
    }
}
