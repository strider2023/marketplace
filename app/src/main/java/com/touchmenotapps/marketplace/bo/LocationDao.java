package com.touchmenotapps.marketplace.bo;

import com.touchmenotapps.marketplace.framework.bo.BaseDao;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

/**
 * Created by arindamnath on 27/01/18.
 */

public class LocationDao extends BaseDao {

    private String country;
    private String state;
    private String city;
    private double latitude;
    private double longitude;

    public LocationDao() {

    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    @Override
    public void parse(JSONParser jsonParser, JSONObject jsonObject) throws Exception {
        if(jsonObject.containsKey("country")) {
            setCountry(jsonObject.get("country").toString());
        }
        if(jsonObject.containsKey("state")) {
            setCountry(jsonObject.get("country").toString());
        }
        if(jsonObject.containsKey("city")) {
            setCountry(jsonObject.get("country").toString());
        }
        if(jsonObject.containsKey("lat")) {
            setLatitude(Double.parseDouble(jsonObject.get("lat").toString()));
        }
        if(jsonObject.containsKey("lng")) {
            setLongitude(Double.parseDouble(jsonObject.get("lng").toString()));
        }
    }
}