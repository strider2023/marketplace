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
    private long startDateFromToday;
    private long endDateFromToday;

    public FeedDao(Context context) {
        super(context);
        name = "1234";
        data = "hello.jpg";
    }

    @Override
    protected void parse(JSONParser jsonParser, JSONObject jsonObject) throws Exception {

    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getCaption() {
        return caption;
    }

    public void setCaption(String caption) {
        this.caption = caption;
    }

    public long getStartDateFromToday() {
        return startDateFromToday;
    }

    public void setStartDateFromToday(long startDateFromToday) {
        this.startDateFromToday = startDateFromToday;
    }

    public long getEndDateFromToday() {
        return endDateFromToday;
    }

    public void setEndDateFromToday(long endDateFromToday) {
        this.endDateFromToday = endDateFromToday;
    }

    public JSONObject toJSON() {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("name", name);
        jsonObject.put("data", data);
        jsonObject.put("caption", caption);
        jsonObject.put("startDateFromToday", startDateFromToday);
        jsonObject.put("endDateFromToday", endDateFromToday);
        return jsonObject;
    }
}
