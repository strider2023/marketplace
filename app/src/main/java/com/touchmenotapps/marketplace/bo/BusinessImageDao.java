package com.touchmenotapps.marketplace.bo;

import android.os.Parcel;
import android.os.Parcelable;

import com.touchmenotapps.marketplace.framework.bo.BaseDao;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

/**
 * Created by i7 on 24-01-2018.
 */

public class BusinessImageDao extends BaseDao implements Parcelable {

    private String data = "";
    private String name = "";
    private String caption = "";
    private String file;
    private boolean canDelete;

    public BusinessImageDao() {

    }

    @Override
    public void parse(JSONParser jsonParser, JSONObject jsonObject) throws Exception {
        if (jsonObject.containsKey("id")) {
            setId(Long.parseLong(jsonObject.get("id").toString()));
        }
        if (jsonObject.containsKey("file")) {
            setFile(jsonObject.get("file").toString());
        }
        if (jsonObject.containsKey("caption")) {
            setCaption(jsonObject.get("caption").toString());
        }
        if(jsonObject.containsKey("canDelete")) {
            setCanDelete(Boolean.parseBoolean(jsonObject.get("canDelete").toString()));
        }
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCaption() {
        return caption;
    }

    public void setCaption(String caption) {
        this.caption = caption;
    }

    public String getFile() {
        return file;
    }

    public void setFile(String file) {
        this.file = file;
    }

    public boolean isCanDelete() {
        return canDelete;
    }

    public void setCanDelete(boolean canDelete) {
        this.canDelete = canDelete;
    }

    public JSONObject toJSON() {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("name", name);
        jsonObject.put("data", data);
        jsonObject.put("caption", caption);
        return jsonObject;
    }

    ///////////////////////////////////////////////////////////////////////////

    public BusinessImageDao(Parcel in){
        this.setId(in.readLong());
        this.file = in.readString();
        this.caption =  in.readString();
        this.canDelete = in.readByte() != 0;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(getId());
        dest.writeString(file);
        dest.writeString(caption);
        dest.writeByte((byte) (canDelete ? 1 : 0));
    }

    public static final Parcelable.Creator CREATOR = new Parcelable.Creator() {
        public BusinessImageDao createFromParcel(Parcel in) {
            return new BusinessImageDao(in);
        }

        public BusinessImageDao[] newArray(int size) {
            return new BusinessImageDao[size];
        }
    };
}
