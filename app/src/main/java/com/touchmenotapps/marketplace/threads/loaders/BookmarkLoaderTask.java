package com.touchmenotapps.marketplace.threads.loaders;

import android.content.Context;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.content.AsyncTaskLoader;
import android.util.Log;

import com.touchmenotapps.marketplace.R;
import com.touchmenotapps.marketplace.bo.BusinessDao;
import com.touchmenotapps.marketplace.framework.NetworkUtils;
import com.touchmenotapps.marketplace.framework.constants.AppConstants;
import com.touchmenotapps.marketplace.framework.constants.URLConstants;
import com.touchmenotapps.marketplace.framework.enums.RequestType;
import com.touchmenotapps.marketplace.framework.persist.AppPreferences;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by i7 on 04-01-2018.
 */

public class BookmarkLoaderTask extends AsyncTaskLoader<List<BusinessDao>> {

    private AppPreferences appPreferences;
    private NetworkUtils networkUtil;
    private JSONParser jsonParser;
    private Bundle args;

    private List<BusinessDao> data = new ArrayList<>();
    private String decodedString;

    public BookmarkLoaderTask(Context context, Bundle args) {
        super(context);
        networkUtil = new NetworkUtils(context);
        jsonParser = new JSONParser();
        appPreferences = new AppPreferences(context);
        this.args = args;
    }

    @Override
    public List<BusinessDao> loadInBackground() {
        if (networkUtil.isNetworkAvailable()) {
            try {
                data.clear();
                JSONArray response = getServerResponse();
                if (response != null) {
                    if (response.size() > 0) {
                        for (int i = 0; i < response.size(); i++) {
                            BusinessDao businessDao = new BusinessDao();
                            businessDao.parse(jsonParser, (JSONObject) response.get(i));
                            data.add(businessDao);
                        }
                    }
                }
            } catch (Exception e) {
                Log.e(AppConstants.APP_TAG, e.toString());
            }
            return data;
        } else {
            return data;
        }
    }

    private JSONArray getServerResponse() throws Exception {
        HttpURLConnection httppost = networkUtil.getHttpURLConInstance(
                getContext().getString(R.string.base_url) + URLConstants.CONSUMER_GET_BOOKMARKS_URL, RequestType.GET);
        httppost.setRequestProperty("uuid", appPreferences.getUserToken());
        httppost.setRequestProperty("did", getDeviceId());

        StringBuilder sb = new StringBuilder();
        BufferedReader in = new BufferedReader(new InputStreamReader(
                httppost.getInputStream()));
        while ((decodedString = in.readLine()) != null)
            sb.append(decodedString);
        in.close();
        Log.i(AppConstants.APP_TAG, sb.toString());
        JSONArray jsonArray = (JSONArray) jsonParser.parse(sb.toString());
        return jsonArray;
    }

    public String getDeviceId() {
        return Settings.Secure.getString(getContext().getContentResolver(), Settings.Secure.ANDROID_ID);
    }
}