package com.touchmenotapps.marketplace.signup.threads;

import android.content.Context;
import android.util.Log;

import com.touchmenotapps.marketplace.R;
import com.touchmenotapps.marketplace.common.BaseAppTask;
import com.touchmenotapps.marketplace.common.constants.AppConstants;
import com.touchmenotapps.marketplace.common.constants.URLConstants;
import com.touchmenotapps.marketplace.common.enums.RequestType;
import com.touchmenotapps.marketplace.common.enums.ServerEvents;
import com.touchmenotapps.marketplace.framework.interfaces.ServerResponseListener;

import org.json.simple.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;

/**
 * Created by i7 on 26-12-2017.
 */

public class UserOTPTask extends BaseAppTask {

    private String decodedString;
    private String errorMessage;

    public UserOTPTask(int id, Context context, ServerResponseListener serverResponseListener) {
        super(id, context, serverResponseListener);
    }

    @Override
    protected ServerEvents doInBackground(Object... objects) {
        if(getNetworkUtils().isNetworkAvailable()) {
            try {
                JSONObject dato = (JSONObject) objects[0];
                Log.i(AppConstants.APP_TAG, dato.toJSONString());
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

    private ServerEvents getServerResponse(JSONObject object) throws Exception {
        HttpURLConnection httppost = getNetworkUtils().getHttpURLConInstance(
                getContext().getString(R.string.base_url) + URLConstants.OTP_URL, RequestType.POST);
        DataOutputStream out = new DataOutputStream(httppost.getOutputStream());
        out.writeBytes(object.toString());
        out.flush();
        out.close();
        StringBuilder sb = new StringBuilder();
        BufferedReader in = new BufferedReader(new InputStreamReader(
                httppost.getInputStream()));
        while ((decodedString = in.readLine()) != null)
            sb.append(decodedString);
        in.close();
        Log.i(AppConstants.APP_TAG, sb.toString());
        getAppPreferences().setRegisterOTPComplete(true);
        return ServerEvents.SUCCESS;
    }
}
