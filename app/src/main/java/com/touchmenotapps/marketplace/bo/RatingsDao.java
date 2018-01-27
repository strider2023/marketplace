package com.touchmenotapps.marketplace.bo;

import com.touchmenotapps.marketplace.framework.bo.BaseDao;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

/**
 * Created by arindamnath on 30/12/17.
 */

public class RatingsDao extends BaseDao {

    private String type;
    private long neutral;
    private long down;
    private long up;

    public RatingsDao() {

    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public long getNeutral() {
        return neutral;
    }

    public void setNeutral(long neutral) {
        this.neutral = neutral;
    }

    public long getDown() {
        return down;
    }

    public void setDown(long down) {
        this.down = down;
    }

    public long getUp() {
        return up;
    }

    public void setUp(long up) {
        this.up = up;
    }

    @Override
    public void parse(JSONParser jsonParser, JSONObject jsonObject) throws Exception {
        if(jsonObject.containsKey("NEUTRAL")) {
            setNeutral(Long.parseLong(jsonObject.get("NEUTRAL").toString()));
        }
        if(jsonObject.containsKey("DOWN")) {
            setDown(Long.parseLong(jsonObject.get("DOWN").toString()));
        }
        if(jsonObject.containsKey("UP")) {
            setUp(Long.parseLong(jsonObject.get("UP").toString()));
        }
    }
}
