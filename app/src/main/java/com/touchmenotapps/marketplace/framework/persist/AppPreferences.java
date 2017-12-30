package com.touchmenotapps.marketplace.framework.persist;

import android.content.Context;
import android.content.SharedPreferences;

import com.touchmenotapps.marketplace.common.enums.UserType;

/**
 * Created by i7 on 17-10-2017.
 */

public class AppPreferences {

    private SharedPreferences mAppPrefs;

    public AppPreferences(Context context) {
        mAppPrefs = context.getSharedPreferences("TheBazaarPreferences", 0);
    }

    public void setRegisterOTPComplete(boolean isComplete) {
        SharedPreferences.Editor edit = mAppPrefs.edit();
        edit.putBoolean("registerOTP", isComplete);
        edit.commit();
    }

    public void setLoggedIn() {
        SharedPreferences.Editor edit = mAppPrefs.edit();
        edit.putBoolean("appLoggedIn", true);
        edit.commit();
    }

    public void setLoggedOut() {
        SharedPreferences.Editor edit = mAppPrefs.edit();
        edit.putBoolean("appLoggedIn", false);
        edit.commit();
    }

    public void setUserPhoneNumber(String number) {
        SharedPreferences.Editor edit = mAppPrefs.edit();
        edit.putString("phoneNumber", number);
        edit.commit();
    }

    public void setUserEmail(String email) {
        SharedPreferences.Editor edit = mAppPrefs.edit();
        edit.putString("email", email);
        edit.commit();
    }

    public void setAuthResponse(String token, String expires, String accountType) {
        SharedPreferences.Editor edit = mAppPrefs.edit();
        edit.putString("token", token);
        edit.putString("expires", expires);
        edit.putString("accountType", accountType);
        edit.commit();
    }

    public String getUserPhoneNumber() {
        return mAppPrefs.getString("phoneNumber", "");
    }

    public String getUserToken() {
        return mAppPrefs.getString("token", "");
    }

    public UserType getUserType() {
        UserType userType = UserType.BUSINESS;
        if(mAppPrefs.getString("accountType", "BUSINESS").equals(UserType.CONSUMER.toString())) {
            userType = UserType.CONSUMER;
        }
        return userType;
    }

    public boolean isUserLoggedIn() {
        return mAppPrefs.getBoolean("appLoggedIn", false);
    }

    public boolean isRegisterOTPComplete() {
        return mAppPrefs.getBoolean("registerOTP", true);
    }
}
