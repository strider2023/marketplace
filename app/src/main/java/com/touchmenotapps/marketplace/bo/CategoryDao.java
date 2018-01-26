package com.touchmenotapps.marketplace.bo;

import com.touchmenotapps.marketplace.framework.bo.BaseDao;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

/**
 * Created by arindamnath on 30/12/17.
 */

public class CategoryDao extends BaseDao {

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
}
