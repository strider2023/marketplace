package com.touchmenotapps.marketplace.bo;

import android.content.Context;

import com.touchmenotapps.marketplace.framework.bo.BaseDao;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

/**
 * Created by arindamnath on 30/12/17.
 */

public class FeedDao extends BaseDao {

    private String name;
    private String data;
    private String caption;
    private String redeeemCode;
    private String startDate;
    private String endDate;
    private String imageURL;
    private boolean canDelete;
    private long startDateFromToday;
    private long endDateFromToday;

    public FeedDao(Context context) {
        super(context);
        name = "hello.jpg";
        data = "1234";
    }

    @Override
    public void parse(JSONParser jsonParser, JSONObject jsonObject) throws Exception {
        if(jsonObject.containsKey("caption")) {
            setCaption(jsonObject.get("caption").toString());
        }
        if(jsonObject.containsKey("redeeemCode")) {
            setRedeeemCode(jsonObject.get("redeeemCode").toString());
        }
        if(jsonObject.containsKey("startDate")) {
            setStartDate(getDate(jsonObject.get("startDate").toString()));
        }
        if(jsonObject.containsKey("endDate")) {
            setEndDate(getDate(jsonObject.get("endDate").toString()));
        }
        if(jsonObject.containsKey("file")) {
            setImageURL(jsonObject.get("file").toString());
        }
        if(jsonObject.containsKey("canDelete")) {
            setCanDelete(Boolean.parseBoolean(jsonObject.get("updatedOn").toString()));
        }
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

    public String getRedeeemCode() {
        return redeeemCode;
    }

    public void setRedeeemCode(String redeeemCode) {
        this.redeeemCode = redeeemCode;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public boolean isCanDelete() {
        return canDelete;
    }

    public void setCanDelete(boolean canDelete) {
        this.canDelete = canDelete;
    }

    public String getImageURL() {
        return imageURL;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
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
