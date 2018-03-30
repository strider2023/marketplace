package com.touchmenotapps.marketplace.framework;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.provider.Settings.Secure;

import com.touchmenotapps.marketplace.framework.enums.ServerEvents;
import com.touchmenotapps.marketplace.framework.NetworkUtils;
import com.touchmenotapps.marketplace.framework.interfaces.ServerResponseListener;
import com.touchmenotapps.marketplace.framework.persist.AppPreferences;

import org.json.simple.parser.JSONParser;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Created by i7 on 16-12-2017.
 */

public abstract class BaseAppTask extends AsyncTask<Object, Void, ServerEvents> {

    private int id;
    private ServerResponseListener serverResponseListener;
    private NetworkUtils networkUtils;
    private AppPreferences appPreferences;
    private JSONParser parser;
    private ProgressDialog mProgress;
    private Context context;
    private boolean showLoader = true;
    public int timeOut = 20000;
    public String timeOutMessage = "Server is taking longer than expected to process your request. Please try again.";

    public BaseAppTask(int id, Context context, ServerResponseListener serverResponseListener, boolean showLoader){
        this.id = id;
        this.context = context;
        this.showLoader = showLoader;
        this.serverResponseListener = serverResponseListener;
        this.networkUtils = new NetworkUtils(context);
        this.appPreferences = new AppPreferences(context);
        this.mProgress = new ProgressDialog(context, AlertDialog.THEME_HOLO_LIGHT);
        this.mProgress.setMessage("Loading...");
        this.mProgress.setCancelable(false);
        this.parser = new JSONParser();
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        if(showLoader) {
            mProgress.show();
        }
    }

    @Override
    protected void onPostExecute(ServerEvents serverEvents) {
        super.onPostExecute(serverEvents);
        if(showLoader) {
            mProgress.dismiss();
        }
    }

    public int getId() {
        return id;
    }

    public ServerResponseListener getServerResponseListener() {
        return serverResponseListener;
    }

    public NetworkUtils getNetworkUtils() {
        return networkUtils;
    }

    public AppPreferences getAppPreferences() {
        return appPreferences;
    }

    public JSONParser getParser() {
        return parser;
    }

    public ProgressDialog getmProgress() {
        return mProgress;
    }

    public Context getContext() {
        return context;
    }

    public String getDeviceId() {
        return Secure.getString(context.getContentResolver(), Secure.ANDROID_ID);
    }

    private final String md5(final String s) {
        try {
            MessageDigest digest = MessageDigest
                    .getInstance("MD5");
            digest.update(s.getBytes());
            byte messageDigest[] = digest.digest();

            StringBuffer hexString = new StringBuffer();
            for (int i = 0; i < messageDigest.length; i++) {
                String h = Integer.toHexString(0xFF & messageDigest[i]);
                while (h.length() < 2)
                    h = "0" + h;
                hexString.append(h);
            }
            return hexString.toString();

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return "";
    }
}
