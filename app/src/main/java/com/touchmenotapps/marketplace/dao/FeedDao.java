package com.touchmenotapps.marketplace.dao;

import android.content.Context;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

/**
 * Created by arindamnath on 30/12/17.
 */

public class FeedDao extends BaseDao {

    private String name;
    private String data;
    private String caption;
    private String startDateFromToday;
    private String endDateFromToday;

    public FeedDao(Context context) {
        super(context);
    }

    @Override
    protected void parse(JSONParser jsonParser, JSONObject jsonObject) throws Exception {

    }
}
