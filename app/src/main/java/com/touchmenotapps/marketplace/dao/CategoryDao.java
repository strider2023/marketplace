package com.touchmenotapps.marketplace.dao;

import android.content.Context;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Created by arindamnath on 30/12/17.
 */

public class CategoryDao extends BaseDao {

    private Map<String, Set<String>> categoriesMap = new HashMap<>();

    public CategoryDao(Context context) {
        super(context);
    }

    public Map<String, Set<String>> getCategoriesMap() {
        return categoriesMap;
    }

    public void addCategory(String category, String subCategory) {
        Set<String> subCategorySet = new HashSet<>();
        subCategorySet.add(subCategory);
        this.categoriesMap.put(category, subCategorySet);
    }

    public void setCategoriesMap(Map<String, Set<String>> categoriesMap) {
        this.categoriesMap = categoriesMap;
    }

    @Override
    public void parse(JSONParser jsonParser, JSONObject jsonObject) throws Exception {
        for (Object key : jsonObject.keySet()) {
            JSONArray subCategoryArray = (JSONArray) jsonParser.parse(jsonObject.get(key).toString());
            Set<String> subCategorySet = new HashSet<>();
            subCategorySet.add(subCategoryArray.get(0).toString());
            categoriesMap.put(key.toString(), subCategorySet);
        }
    }

    public JSONObject toJSON() {
        JSONObject jsonObject = new JSONObject();
        //jsonObject.putAll(categoriesMap);
        for(String key : categoriesMap.keySet()) {
            JSONArray categories = new JSONArray();
            for (String category : categoriesMap.get(key)) {
                categories.add(category);
            }
            //timeArray.add(categoriesMap.get(key));
            jsonObject.put(key, categories);
        }
        return jsonObject;
    }
}
