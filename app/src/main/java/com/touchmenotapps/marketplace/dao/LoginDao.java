package com.touchmenotapps.marketplace.dao;

import android.content.Context;
import android.provider.Settings;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

/**
 * Created by i7 on 16-12-2017.
 */
public class LoginDao extends BaseDao {

    private String userMailPhone;
    private String deviceId;

    public LoginDao(Context context) {
        super(context);
        deviceId = Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);
    }

    public String getUserMailPhone() {
        return userMailPhone;
    }

    public void setUserMailPhone(String userMailPhone) {
        this.userMailPhone = userMailPhone;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    @Override
    protected void parse(JSONParser jsonParser, JSONObject jsonObject) {

    }

    public JSONObject toJSON() {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("ph", userMailPhone);
        jsonObject.put("deviceId", deviceId);
        return jsonObject;
    }
}
