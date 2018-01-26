package com.touchmenotapps.marketplace.bo;

import com.touchmenotapps.marketplace.framework.bo.BaseDao;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

/**
 * Created by arindamnath on 26/01/18.
 */

public class GeoDao extends BaseDao {

    private String name;

    public GeoDao() {

    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public void parse(JSONParser jsonParser, JSONObject jsonObject) throws Exception {
        if(jsonObject.containsKey("id")) {
            setId(Long.parseLong(jsonObject.get("id").toString()));
        }
        if(jsonObject.containsKey("name")) {
            setName(jsonObject.get("name").toString());
        }
    }


}
