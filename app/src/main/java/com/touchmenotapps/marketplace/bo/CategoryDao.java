package com.touchmenotapps.marketplace.bo;

import android.os.Parcel;
import android.os.Parcelable;

import com.touchmenotapps.marketplace.framework.bo.BaseDao;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

/**
 * Created by arindamnath on 30/12/17.
 */

public class CategoryDao extends BaseDao implements Parcelable {

    private String enumText;
    private String description;

    public CategoryDao() {

    }

    @Override
    protected void parse(JSONParser jsonParser, JSONObject jsonObject) throws Exception {
        if(jsonObject.containsKey("enumText")) {
            setEnumText(jsonObject.get("enumText").toString());
        }
        if(jsonObject.containsKey("description")) {
            setDescription(jsonObject.get("description").toString());
        }
    }

    public String getEnumText() {
        return enumText;
    }

    public void setEnumText(String enumText) {
        this.enumText = enumText;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public CategoryDao(Parcel in){
        this.enumText = in.readString();
        this.description =  in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(enumText);
        dest.writeString(description);
    }

    public static final Parcelable.Creator CREATOR = new Parcelable.Creator() {
        public CategoryDao createFromParcel(Parcel in) {
            return new CategoryDao(in);
        }

        public CategoryDao[] newArray(int size) {
            return new CategoryDao[size];
        }
    };
}
