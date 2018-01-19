package com.touchmenotapps.marketplace.bo;

import com.touchmenotapps.marketplace.framework.bo.BaseDao;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

/**
 * Created by arindamnath on 30/12/17.
 */

public class BusinessAddressDao extends BaseDao {

    private String address;
    private String city;
    private String state;
    private String zip;

    public BusinessAddressDao() {

    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getZip() {
        return zip;
    }

    public void setZip(String zip) {
        this.zip = zip;
    }

    @Override
    public void parse(JSONParser jsonParser, JSONObject jsonObject) {
        if (jsonObject.containsKey("line1")) {
            setAddress(jsonObject.get("line1").toString());
        }
        if (jsonObject.containsKey("city")) {
            setCity(jsonObject.get("city").toString());
        }
        if (jsonObject.containsKey("state")) {
            setState(jsonObject.get("state").toString());
        }
        if (jsonObject.containsKey("zip")) {
            setZip(jsonObject.get("zip").toString());
        }
    }

    public JSONObject toJSON() {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("line1", address);
        jsonObject.put("city", city);
        jsonObject.put("state", state);
        jsonObject.put("zip", zip);
        return jsonObject;
    }
}