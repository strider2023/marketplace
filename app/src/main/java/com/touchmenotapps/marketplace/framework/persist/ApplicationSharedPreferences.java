package com.touchmenotapps.marketplace.framework.persist;

import android.content.Context;
import android.content.SharedPreferences;

import com.touchmenotapps.marketplace.common.enums.UserType;

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

    public void setAuthResponse(String token, String expires, String accountType) {
        SharedPreferences.Editor edit = mAppPrefs.edit();
        edit.putString("token", token);
        edit.putString("expires", expires);
        edit.putString("accountType", accountType);
        edit.commit();
    }

    public UserType getUserType() {
        return UserType.BUSINESS;
    }

    public boolean isUserLoggedIn() {
        return mAppPrefs.getBoolean("appLoggedIn", false);
    }
}
