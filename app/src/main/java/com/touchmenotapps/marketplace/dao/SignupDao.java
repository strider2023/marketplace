package com.touchmenotapps.marketplace.dao;

import android.content.Context;
import android.provider.Settings;

import com.touchmenotapps.marketplace.common.enums.UserType;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

/**
 * Created by i7 on 22-12-2017.
 */

public class SignupDao extends BaseDao {

    private String phoneNumber;
    private String country;
    private UserType type;
    private String deviceId;

    public SignupDao(Context context) {
        super(context);
        deviceId = Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);
        country = "INDIA";
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public UserType getType() {
        return type;
    }

    public void setType(UserType type) {
        this.type = type;
    }

    @Override
    protected void parse(JSONParser jsonParser, JSONObject jsonObject) {

    }

    public JSONObject toJSON() {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("ph", phoneNumber);
        jsonObject.put("country", country);
        jsonObject.put("type", type.toString());
        jsonObject.put("deviceId", deviceId);
        return jsonObject;
    }
}
