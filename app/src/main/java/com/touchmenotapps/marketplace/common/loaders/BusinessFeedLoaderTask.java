package com.touchmenotapps.marketplace.common.loaders;

import android.content.Context;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.content.AsyncTaskLoader;
import android.util.Log;

import com.touchmenotapps.marketplace.R;
import com.touchmenotapps.marketplace.framework.constants.AppConstants;
import com.touchmenotapps.marketplace.framework.constants.URLConstants;
import com.touchmenotapps.marketplace.framework.enums.RequestType;
import com.touchmenotapps.marketplace.bo.FeedDao;
import com.touchmenotapps.marketplace.framework.NetworkUtils;
import com.touchmenotapps.marketplace.framework.persist.AppPreferences;

import org.apache.commons.lang3.text.StrSubstitutor;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.touchmenotapps.marketplace.common.BusinessDetailsActivity.SELECTED_BUSINESS_ID;

/**
 * Created by arindamnath on 03/01/18.
 */

public class BusinessFeedLoaderTask extends AsyncTaskLoader<List<FeedDao>> {

    private AppPreferences appPreferences;
    private NetworkUtils networkUtil;
    private JSONParser jsonParser;
    private Bundle args;

    private List<FeedDao> data = new ArrayList<>();
    private String decodedString;

    public BusinessFeedLoaderTask(Context context, Bundle args) {
        super(context);
        networkUtil = new NetworkUtils(context);
        jsonParser = new JSONParser();
        appPreferences = new AppPreferences(context);
        this.args = args;
    }

    @Override
    public List<FeedDao> loadInBackground() {
        if(networkUtil.isNetworkAvailable()) {
            try {
                data.clear();
                JSONArray response = getServerResponse(args.getLong(SELECTED_BUSINESS_ID, -1l));
                if(response != null) {
                    if(response.size() > 0) {
                        for (int i = 0; i < response.size(); i++) {
                            FeedDao feedDao = new FeedDao(getContext());
                            feedDao.parse(jsonParser, (JSONObject) response.get(i));
                            data.add(feedDao);
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

    private JSONArray getServerResponse(long businessId) throws Exception {
        HttpURLConnection httppost;
        if(businessId == -1l) {
            httppost = networkUtil.getHttpURLConInstance(
                    getContext().getString(R.string.base_url) + URLConstants.GET_ALL_BUSINESS_FEED_URL, RequestType.GET);
        } else {
            Map<String, String> data = new HashMap<>();
            data.put("businessId", String.valueOf(businessId));
            String url = StrSubstitutor.replace(URLConstants.GET_BUSINESS_FEED_URL, data);
            httppost = networkUtil.getHttpURLConInstance(
                    getContext().getString(R.string.base_url) + url, RequestType.GET);
        }
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
