package com.touchmenotapps.marketplace.bo;

import android.os.Parcel;
import android.os.Parcelable;

import com.touchmenotapps.marketplace.framework.bo.BaseDao;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

/**
 * Created by arindamnath on 27/01/18.
 */

public class AnalyticsDao extends BaseDao implements Parcelable{

    private String type;
    private long today;
    private long lastWeek;
    private long lastMonth;
    private long total;

    public AnalyticsDao() {

    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public long getToday() {
        return today;
    }

    public void setToday(long today) {
        this.today = today;
    }

    public long getLastWeek() {
        return lastWeek;
    }

    public void setLastWeek(long lastWeek) {
        this.lastWeek = lastWeek;
    }

    public long getLastMonth() {
        return lastMonth;
    }

    public void setLastMonth(long lastMonth) {
        this.lastMonth = lastMonth;
    }

    public long getTotal() {
        return total;
    }

    public void setTotal(long total) {
        this.total = total;
    }

    @Override
    public void parse(JSONParser jsonParser, JSONObject jsonObject) throws Exception {
        if(jsonObject.containsKey("total")) {
            setTotal(Long.parseLong(jsonObject.get("total").toString()));
        }
        if(jsonObject.containsKey("lastMonth")) {
            setLastMonth(Long.parseLong(jsonObject.get("lastMonth").toString()));
        }
        if(jsonObject.containsKey("lastWeek")) {
            setLastWeek(Long.parseLong(jsonObject.get("lastWeek").toString()));
        }
        if(jsonObject.containsKey("today")) {
            setToday(Long.parseLong(jsonObject.get("today").toString()));
        }
    }

    //////////////////////////////////////////////////
    public AnalyticsDao(Parcel in){
        this.type = in.readString();
        this.today = in.readLong();
        this.lastWeek = in.readLong();
        this.lastMonth = in.readLong();
        this.total = in.readLong();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(type);
        dest.writeLong(today);
        dest.writeLong(lastWeek);
        dest.writeLong(lastMonth);
        dest.writeLong(total);
    }

    public static final Parcelable.Creator CREATOR = new Parcelable.Creator() {
        public AnalyticsDao createFromParcel(Parcel in) {
            return new AnalyticsDao(in);
        }

        public AnalyticsDao[] newArray(int size) {
            return new AnalyticsDao[size];
        }
    };
}

