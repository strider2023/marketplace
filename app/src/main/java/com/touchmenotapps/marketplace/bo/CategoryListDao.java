package com.touchmenotapps.marketplace.bo;

import com.touchmenotapps.marketplace.framework.bo.BaseDao;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by arindamnath on 26/01/18.
 */

public class CategoryListDao extends BaseDao {

    private Map<CategoryDao, List<CategoryDao>> categoriesMap = new HashMap<>();
    private Map<String, Set<String>> businessCategory = new HashMap<>();

    public CategoryListDao() {

    }

    public Map<CategoryDao, List<CategoryDao>> getCategoriesMap() {
        return categoriesMap;
    }

    public void addCategory(String category, List<CategoryDao> subCategory) {
        Set<String> subCategorySet = new HashSet<>();
        for(CategoryDao subCat : subCategory) {
            subCategorySet.add(subCat.getEnumText());
        }
        this.businessCategory.put(category, subCategorySet);
    }

    @Override
    public void parse(JSONParser jsonParser, JSONObject jsonObject) throws Exception {
        CategoryDao categoryDao = new CategoryDao();
        categoryDao.parse(jsonParser, jsonObject);
        List<CategoryDao> categoryDaoList = new ArrayList<>();

        if(jsonObject.containsKey("subCategories")) {
            JSONArray subCategoryArray = (JSONArray) jsonParser.parse(jsonObject.get("subCategories").toString());
            for(int i = 0; i < subCategoryArray.size(); i++) {
                JSONObject subCat = (JSONObject) subCategoryArray.get(i);
                CategoryDao subCategory = new CategoryDao();
                subCategory.parse(jsonParser, subCat);
                categoryDaoList.add(subCategory);
            }
        }
        if(!categoriesMap.containsKey(categoryDao)) {
            categoriesMap.put(categoryDao, categoryDaoList);
        }
    }

    public JSONObject toJSON() {
        JSONObject jsonObject = new JSONObject();
        for(String key : businessCategory.keySet()) {
            JSONArray categories = new JSONArray();
            for (String category : businessCategory.get(key)) {
                categories.add(category);
            }
            jsonObject.put(key, categories);
        }
        return jsonObject;
    }
}
