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

import org.json.simple.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ConnectException;
import java.net.HttpURLConnection;

/**
 * Created by i7 on 16-10-2017.
 */

public class LoginTask extends BaseAppTask {

    private String decodedString;
    private String errorMessage;

    public LoginTask(int id, Context context, ServerResponseListener serverResponseListener, boolean showLoader) {
        super(id, context, serverResponseListener, showLoader);
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
                errorMessage = e.getMessage();
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
        HttpURLConnection httppost = null;
        try {
            httppost = getNetworkUtils().getHttpURLConInstance(
                    getContext().getString(R.string.base_url) + URLConstants.AUTH_URL, RequestType.POST);
            httppost.setConnectTimeout(timeOut);
            httppost.setReadTimeout(timeOut);
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
            JSONObject response = (JSONObject) getParser().parse(sb.toString());
            getAppPreferences().setAuthResponse(
                    response.get("token").toString(),
                    response.get("expires").toString(),
                    response.get("accountType").toString()
            );
            getAppPreferences().setLoggedIn();
            return ServerEvents.SUCCESS;
        } catch (java.net.SocketTimeoutException e) {
            throw new Exception(timeOutMessage);
        } catch(ConnectException ce){
            throw new Exception(timeOutMessage);
        } catch(IOException io) {
            StringBuilder sb = new StringBuilder();
            try {
                BufferedReader in = new BufferedReader(new InputStreamReader(httppost.getErrorStream()));
                String line = "";
                while ((line = in.readLine()) != null)
                    sb.append(line);
                in.close();
            } catch(IOException ex) {
                ex.printStackTrace();
            }
            throw new Exception(sb.toString());
        }
    }
}
