package com.touchmenotapps.marketplace.framework.persist;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by i7 on 17-10-2017.
 */

public class ApplicationSharedPreferences {

    private SharedPreferences mAppPrefs;

    public ApplicationSharedPreferences(Context context) {
        mAppPrefs = context.getSharedPreferences("TheBazaarPreferences", 0);
    }

    public void setLoggedIn() {
        SharedPreferences.Editor edit = mAppPrefs.edit();
        edit.putBoolean("appLoggedIn", true);
        edit.commit();
    }

    public void setLopggedOut() {
        SharedPreferences.Editor edit = mAppPrefs.edit();
        edit.putBoolean("appLoggedIn", false);
        edit.commit();
    }

    public boolean isUserLoggedIn() {
        return mAppPrefs.getBoolean("appLoggedIn", false);
    }
}
