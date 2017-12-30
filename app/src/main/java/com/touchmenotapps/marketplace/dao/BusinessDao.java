package com.touchmenotapps.marketplace.dao;

import android.content.Context;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by arindamnath on 30/12/17.
 */

public class BusinessDao extends BaseDao {

    private String name;
    private String website;
    private Set<String> phoneNumber = new HashSet<>();
    private int businessPhotosCount;
    private int businessFeedCount;

    private RatingsDao ratingsDao;
    private CategoryDao categoryDao;
    private HoursOfOperationDao hoursOfOperationDao;
    private BusinessAddressDao businessAddressDao;

    public BusinessDao(Context context) {
        super(context);
        ratingsDao = new RatingsDao(context);
        categoryDao = new CategoryDao(context);
        hoursOfOperationDao = new HoursOfOperationDao(context);
        businessAddressDao = new BusinessAddressDao(context);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getWebsite() {
        return website;
    }

    public void setWebsite(String website) {
        this.website = website;
    }

    public Set<String> getPhoneNumber() {
        return phoneNumber;
    }

    public void addPhoneNumber(String phoneNumber) {
        this.phoneNumber.add(phoneNumber);
    }

    public void setPhoneNumber(Set<String> phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public int getBusinessPhotosCount() {
        return businessPhotosCount;
    }

    public void setBusinessPhotosCount(int businessPhotosCount) {
        this.businessPhotosCount = businessPhotosCount;
    }

    public int getBusinessFeedCount() {
        return businessFeedCount;
    }

    public void setBusinessFeedCount(int businessFeedCount) {
        this.businessFeedCount = businessFeedCount;
    }

    public RatingsDao getRatingsDao() {
        return ratingsDao;
    }

    public void setRatingsDao(RatingsDao ratingsDao) {
        this.ratingsDao = ratingsDao;
    }

    public CategoryDao getCategoryDao() {
        return categoryDao;
    }

    public void setCategoryDao(CategoryDao categoryDao) {
        this.categoryDao = categoryDao;
    }

    public HoursOfOperationDao getHoursOfOperationDao() {
        return hoursOfOperationDao;
    }

    public void setHoursOfOperationDao(HoursOfOperationDao hoursOfOperationDao) {
        this.hoursOfOperationDao = hoursOfOperationDao;
    }

    public BusinessAddressDao getBusinessAddressDao() {
        return businessAddressDao;
    }

    public void setBusinessAddressDao(BusinessAddressDao businessAddressDao) {
        this.businessAddressDao = businessAddressDao;
    }

    @Override
    public void parse(JSONParser jsonParser, JSONObject jsonObject) throws Exception {
        if (jsonObject.containsKey("id")) {
            setId(Long.parseLong(jsonObject.get("id").toString()));
        }
        if (jsonObject.containsKey("name")) {
            setName(jsonObject.get("name").toString());
        }
        if (jsonObject.containsKey("website")) {
            setWebsite(jsonObject.get("website").toString());
        }
        if (jsonObject.containsKey("phs")) {
            JSONArray phoneNums = (JSONArray) jsonParser.parse(jsonObject.get("phs").toString());
            for (Object phone : phoneNums) {
                addPhoneNumber(phone.toString());
            }
        }
        if (jsonObject.containsKey("hrsOfOperation")) {
            hoursOfOperationDao.parse(jsonParser, (JSONObject) jsonParser.parse("hrsOfOperation"));
            setHoursOfOperationDao(hoursOfOperationDao);
        }
        if (jsonObject.containsKey("category")) {
            categoryDao.parse(jsonParser, (JSONObject) jsonParser.parse("category"));
            setCategoryDao(categoryDao);
        }
        if (jsonObject.containsKey("address")) {
            businessAddressDao.parse(jsonParser, (JSONObject) jsonParser.parse("address"));
            setBusinessAddressDao(businessAddressDao);
        }
    }

    public JSONObject toJSON() {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("name", name);
        jsonObject.put("website", website);
        jsonObject.put("address", businessAddressDao.toJSON());
        jsonObject.put("hrsOfOperation", hoursOfOperationDao.toJSON());
        jsonObject.put("phs", phoneNumber);
        jsonObject.put("category", categoryDao.toJSON());
        return jsonObject;
    }
}
