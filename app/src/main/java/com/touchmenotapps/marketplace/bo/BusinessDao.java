package com.touchmenotapps.marketplace.bo;

import android.content.Context;

import com.touchmenotapps.marketplace.framework.bo.BaseDao;

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
    private long businessPhotosCount;
    private long businessFeedCount;
    private String businessProfileImage = "";

    private RatingsDao ratingsDao;
    private CategoryListDao categoryDao;
    private HoursOfOperationDao hoursOfOperationDao;
    private BusinessAddressDao businessAddressDao;
    private float singleScoreRating;

    public BusinessDao() {
        ratingsDao = new RatingsDao();
        categoryDao = new CategoryListDao();
        hoursOfOperationDao = new HoursOfOperationDao();
        businessAddressDao = new BusinessAddressDao();
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

    public long getBusinessPhotosCount() {
        return businessPhotosCount;
    }

    public void setBusinessPhotosCount(long businessPhotosCount) {
        this.businessPhotosCount = businessPhotosCount;
    }

    public long getBusinessFeedCount() {
        return businessFeedCount;
    }

    public void setBusinessFeedCount(long businessFeedCount) {
        this.businessFeedCount = businessFeedCount;
    }

    public RatingsDao getRatingsDao() {
        return ratingsDao;
    }

    public void setRatingsDao(RatingsDao ratingsDao) {
        this.ratingsDao = ratingsDao;
    }

    public CategoryListDao getCategoryDao() {
        return categoryDao;
    }

    public void setCategoryDao(CategoryListDao categoryDao) {
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

    public float getSingleScoreRating() {
        return singleScoreRating;
    }

    public void setSingleScoreRating(float singleScoreRating) {
        this.singleScoreRating = singleScoreRating;
    }

    public String getBusinessProfileImage() {
        return businessProfileImage;
    }

    public void setBusinessProfileImage(String businessProfileImage) {
        this.businessProfileImage = businessProfileImage;
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
            hoursOfOperationDao.parse(jsonParser,
                    (JSONObject) jsonParser.parse(jsonObject.get("hrsOfOperation").toString()));
            setHoursOfOperationDao(hoursOfOperationDao);
        }
        /*if (jsonObject.containsKey("category")) {
            categoryDao.parse(jsonParser,
                    (JSONObject) jsonParser.parse(jsonObject.get("category").toString()));
            setCategoryDao(categoryDao);
        }*/
        if (jsonObject.containsKey("address")) {
            businessAddressDao.parse(jsonParser,
                    (JSONObject) jsonParser.parse(jsonObject.get("address").toString()));
            setBusinessAddressDao(businessAddressDao);
        }
        if(jsonObject.containsKey("singleScoreRating")) {
            setSingleScoreRating(Float.parseFloat(jsonObject.get("singleScoreRating").toString()));
        }
        if(jsonObject.containsKey("businessProfileImage")) {
            setBusinessProfileImage(jsonObject.get("businessProfileImage").toString());
        }
        if(jsonObject.containsKey("businessPhotosCount")) {
            setBusinessPhotosCount(Long.parseLong(jsonObject.get("businessPhotosCount").toString()));
        }
        if(jsonObject.containsKey("businessFeedCount")) {
            setBusinessFeedCount(Long.parseLong(jsonObject.get("businessFeedCount").toString()));
        }
    }

    public JSONObject toJSON() {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("name", name);
        jsonObject.put("website", website);
        jsonObject.put("address", businessAddressDao.toJSON());
        jsonObject.put("hrsOfOperation", hoursOfOperationDao.toJSON());
        JSONArray phoneNumbers = new JSONArray();
        for (String phone : phoneNumber) {
            phoneNumbers.add(phone);
        }
        jsonObject.put("phs", phoneNumbers);
        jsonObject.put("category", categoryDao.toJSON());
        return jsonObject;
    }
}
