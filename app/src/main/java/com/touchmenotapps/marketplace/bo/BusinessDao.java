package com.touchmenotapps.marketplace.bo;

import com.touchmenotapps.marketplace.framework.bo.BaseDao;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
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
    private boolean bookmarked;
    private long bookmarkId;
    private String businessProfileImage = "";

    private List<RatingsDao> ratingsDao = new ArrayList<>();
    private List<AnalyticsDao> analyticsDao = new ArrayList<>();
    private CategoryListDao categoryDao;
    private String category, subCategory;
    private HoursOfOperationDao hoursOfOperationDao;
    private BusinessAddressDao businessAddressDao;
    private float singleScoreRating;

    public BusinessDao() {
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

    public List<RatingsDao> getRatingsDao() {
        return ratingsDao;
    }

    public void setRatingsDao(List<RatingsDao> ratingsDao) {
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

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getSubCategory() {
        return subCategory;
    }

    public void setSubCategory(String subCategory) {
        this.subCategory = subCategory;
    }

    public List<AnalyticsDao> getAnalyticsDao() {
        return analyticsDao;
    }

    public void setAnalyticsDao(List<AnalyticsDao> analyticsDao) {
        this.analyticsDao = analyticsDao;
    }

    public boolean isBookmarked() {
        return bookmarked;
    }

    public void setBookmarked(boolean bookmarked) {
        this.bookmarked = bookmarked;
    }

    public long getBookmarkId() {
        return bookmarkId;
    }

    public void setBookmarkId(long bookmarkId) {
        this.bookmarkId = bookmarkId;
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
        if (jsonObject.containsKey("category")) {
            JSONObject cat = (JSONObject) jsonParser.parse(jsonObject.get("category").toString());
            setCategory(cat.keySet().iterator().next().toString());
        }
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
        if(jsonObject.containsKey("ratings")) {
            JSONObject ratings = (JSONObject) jsonParser.parse(jsonObject.get("ratings").toString());
            for(Object type : ratings.keySet()) {
                RatingsDao rating = new RatingsDao();
                rating.setType(type.toString());
                rating.parse(jsonParser, (JSONObject) jsonParser.parse(ratings.get(type).toString()));
                ratingsDao.add(rating);
            }
        }
        if(jsonObject.containsKey("kpi")) {
            JSONObject kpi = (JSONObject) jsonParser.parse(jsonObject.get("kpi").toString());
            for(Object type : kpi.keySet()) {
                AnalyticsDao analytics = new AnalyticsDao();
                analytics.setType(type.toString());
                analytics.parse(jsonParser, (JSONObject) jsonParser.parse(kpi.get(type).toString()));
                analyticsDao.add(analytics);
            }
        }
        if(jsonObject.containsKey("bookmarked")) {
            setBookmarked(Boolean.parseBoolean(jsonObject.get("bookmarked").toString()));
        }
        if(jsonObject.containsKey("bookmarkId")) {
            setBookmarkId(Long.parseLong(jsonObject.get("bookmarkId").toString()));
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
