package com.touchmenotapps.marketplace.bo;

import com.touchmenotapps.marketplace.framework.bo.BaseDao;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

/**
 * Created by i7 on 29-01-2018.
 */

public class ProfileDao extends BaseDao {

    private String name = "NA";
    private long country = 1l;

    public ProfileDao() {

    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public long getCountry() {
        return country;
    }

    @Override
    public void parse(JSONParser jsonParser, JSONObject jsonObject) throws Exception {
        if(jsonObject.containsKey("name")) {
            name = jsonObject.get("name").toString();
        }
        if(jsonObject.containsKey("country")) {
            country = Long.parseLong(jsonObject.get("country").toString());
        }
    }

    public JSONObject toJSON() {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("name", name);
        jsonObject.put("country", country);
        return jsonObject;
    }
}
