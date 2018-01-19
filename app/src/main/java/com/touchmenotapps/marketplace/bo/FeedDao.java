package com.touchmenotapps.marketplace.bo;

import android.content.Context;
import android.os.Parcel;
import android.os.Parcelable;

import com.touchmenotapps.marketplace.framework.bo.BaseDao;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

/**
 * Created by arindamnath on 30/12/17.
 */

public class FeedDao extends BaseDao implements Parcelable {

    private String name;
    private String data;
    private String caption;
    private String redeeemCode;
    private String startDate;
    private String endDate;
    private String imageURL;
    private boolean canDelete;
    private long businessId = -1l;
    private long startDateFromToday;
    private long endDateFromToday;

    public FeedDao() {

    }

    @Override
    public void parse(JSONParser jsonParser, JSONObject jsonObject) throws Exception {
        if(jsonObject.containsKey("id")) {
            setId(Long.parseLong(jsonObject.get("id").toString()));
        }
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
        if(jsonObject.containsKey("businessId")) {
            setBusinessId(Long.parseLong(jsonObject.get("businessId").toString()));
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

    public long getBusinessId() {
        return businessId;
    }

    public void setBusinessId(long businessId) {
        this.businessId = businessId;
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

    ///////////////////////////////////////////////////////////////////////////////////

    public FeedDao(Parcel in){
        this.setId(in.readLong());
        this.businessId = in.readLong();
        this.caption =  in.readString();
        this.redeeemCode = in.readString();
        this.startDate =  in.readString();
        this.endDate =  in.readString();
        this.imageURL =  in.readString();
        this.canDelete = in.readByte() != 0;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(getId());
        dest.writeLong(businessId);
        dest.writeString(caption);
        dest.writeString(redeeemCode);
        dest.writeString(startDate);
        dest.writeString(endDate);
        dest.writeString(imageURL);
        dest.writeByte((byte) (canDelete ? 1 : 0));
    }

    public static final Parcelable.Creator CREATOR = new Parcelable.Creator() {
        public FeedDao createFromParcel(Parcel in) {
            return new FeedDao(in);
        }

        public FeedDao[] newArray(int size) {
            return new FeedDao[size];
        }
    };
}
