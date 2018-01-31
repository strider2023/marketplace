package com.touchmenotapps.marketplace.threads.loaders;

import android.content.Context;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.content.AsyncTaskLoader;
import android.util.Log;

import com.touchmenotapps.marketplace.R;
import com.touchmenotapps.marketplace.bo.BusinessDao;
import com.touchmenotapps.marketplace.bo.GeoDao;
import com.touchmenotapps.marketplace.bo.LocationDao;
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

import static com.touchmenotapps.marketplace.framework.constants.AppConstants.CITY;
import static com.touchmenotapps.marketplace.framework.constants.AppConstants.COUNTRY;
import static com.touchmenotapps.marketplace.framework.constants.AppConstants.GEO_TAG;
import static com.touchmenotapps.marketplace.framework.constants.AppConstants.STATE;
import static com.touchmenotapps.marketplace.framework.constants.URLConstants.GET_COUNTRIES;
import static com.touchmenotapps.marketplace.framework.constants.URLConstants.GET_GEO;

/**
 * Created by arindamnath on 26/01/18.
 */

public class LocationLoaderTask extends AsyncTaskLoader<List<LocationDao>> {

    private AppPreferences appPreferences;
    private NetworkUtils networkUtil;
    private JSONParser jsonParser;
    private Bundle args;

    private List<LocationDao> data = new ArrayList<>();
    private String decodedString;
    private HttpURLConnection httppost;

    public LocationLoaderTask(Context context, Bundle args) {
        super(context);
        networkUtil = new NetworkUtils(context);
        jsonParser = new JSONParser();
        appPreferences = new AppPreferences(context);
        this.args = args;
    }

    @Override
    public List<LocationDao> loadInBackground() {
        if (networkUtil.isNetworkAvailable()) {
            try {
                data.clear();
                JSONArray response = getServerResponse();
                if (response != null) {
                    if (response.size() > 0) {
                        for (int i = 0; i < response.size(); i++) {
                            LocationDao geoDao = new LocationDao();
                            geoDao.parse(jsonParser, (JSONObject) response.get(i));
                            data.add(geoDao);
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
        String url = "";
        /*switch (args.getInt(GEO_TAG, COUNTRY)) {
            case COUNTRY:
                url = GET_COUNTRIES;
                break;
            case STATE:
                url = GET_GEO + "?country=" + args.getString("country");
                break;
            case CITY:
                url = GET_GEO + "?country="  + args.getString("country") + "&state=" + args.getString("state");
                break;
        }*/
        if(args.getString("city") != null) {
            url = "cb/common/geo/latlong?country=india&city=" + args.getString("city");
        }
        httppost = networkUtil.getHttpURLConInstance(
                getContext().getString(R.string.base_url) + url, RequestType.GET);
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
