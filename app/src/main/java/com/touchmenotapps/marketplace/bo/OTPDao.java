package com.touchmenotapps.marketplace.bo;

import android.content.Context;
import android.provider.Settings;

import com.touchmenotapps.marketplace.framework.bo.BaseDao;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

/**
 * Created by i7 on 26-12-2017.
 */

public class OTPDao extends BaseDao {

    private String userOTP;
    private String phoneNumber;
    private String deviceId;

    public OTPDao(Context context) {
        super(context);
        deviceId = Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);
    }

    @Override
    protected void parse(JSONParser jsonParser, JSONObject jsonObject) {

    }

    public String getUserOTP() {
        return userOTP;
    }

    public void setUserOTP(String userOTP) {
        this.userOTP = userOTP;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public JSONObject toJSON() {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("ph", phoneNumber);
        jsonObject.put("otp", userOTP);
        jsonObject.put("deviceId", deviceId);
        return jsonObject;
    }
}
