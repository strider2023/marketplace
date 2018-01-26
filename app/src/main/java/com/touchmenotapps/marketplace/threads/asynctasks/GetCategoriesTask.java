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
import com.touchmenotapps.marketplace.bo.CategoryDao;
import com.touchmenotapps.marketplace.framework.interfaces.ServerResponseListener;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;

/**
 * Created by arindamnath on 30/12/17.
 */

public class GetCategoriesTask extends BaseAppTask {

    private String decodedString;
    private String errorMessage;
    private CategoryListDao categoryDao;

    public GetCategoriesTask(int id, Context context, ServerResponseListener serverResponseListener) {
        super(id, context, serverResponseListener);
        categoryDao = new CategoryListDao();
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
        //if(getAppPreferences().getCategories().trim().length() > 0) {
            HttpURLConnection httppost = getNetworkUtils().getHttpURLConInstance(
                    getContext().getString(R.string.base_url) + URLConstants.GET_ALL_CATEGORIES_URL, RequestType.GET);
            httppost.setRequestProperty("uuid", getAppPreferences().getUserToken());
            httppost.setRequestProperty("did", getDeviceId());
            StringBuilder sb = new StringBuilder();
            BufferedReader in = new BufferedReader(new InputStreamReader(
                    httppost.getInputStream()));
            while ((decodedString = in.readLine()) != null)
                sb.append(decodedString);
            in.close();
            Log.i(AppConstants.APP_TAG, sb.toString());
            JSONArray response = (JSONArray) getParser().parse(sb.toString());
            for(int i = 0; i < response.size(); i++) {
                categoryDao.parse(getParser(), (JSONObject) response.get(i));
            }
            getAppPreferences().setCategories(sb.toString());
        /*} else {
            //TODO improve logic to check with server timestamp
            Log.i(AppConstants.APP_TAG, "Present" + getAppPreferences().getCategories());
            JSONObject response = (JSONObject) getParser().parse(getAppPreferences().getCategories());
            categoryDao.parse(getParser(), response);
        }*/
        return ServerEvents.SUCCESS;
    }
}
