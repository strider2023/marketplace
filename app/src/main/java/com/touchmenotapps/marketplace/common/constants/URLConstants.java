package com.touchmenotapps.marketplace.common.constants;

/**
 * Created by i7 on 16-12-2017.
 */

public class URLConstants {
    public static final String AUTH_URL = "cb/account/login";
    public static final String REGISTER_URL = "cb/account/register";
    public static final String OTP_URL = "cb/account/otp";

    public static final String ADD_BUSINESS_URL = "cb/business/entity";
    public static final String GET_BUSINESS_BY_ID_URL = "cb/business/entity/${businessid}";
    public static final String GET_ALL_BUSINESS_URL = "cb/business/entity";
    public static final String DELETE_BUSINESS_URL = "cb/business/entity/${businessid}";
    public static final String UPDATE_BUSINESS_URL = "cb/business/entity/${businessId}";
    public static final String UPLOAD_BUSINESS_PHOTO_URL = "cb/business/entity/photos/${businessId}";
    public static final String GET_ALL_BUSINESS_PHOTOS_URL = "cb/business/entity/photos/${businessId}";
    public static final String UDPATE_BUSINESS_PHOTO_URL = "cb/business/entity/photos/${businessId}/${photoId}";
    public static final String DELETE_BUSINESS_PHOTOS_URL = "cb/business/entity/photos/${businessId}/${photoId}";
    public static final String CREATE_BUSINESS_FEED_URL = "cb/business/entity/feeds/${businessId}";
    public static final String UPDATE_BUSINESS_FEED_URL = "cb/business/entity/feeds/${businessId}/${feedId}";
    public static final String DELETE_BUSINESS_FEED_URL = "cb/business/entity/feeds/${businessId}/${feedId}";
    public static final String GET_ALL_BUSINESS_FEED_URL = "/cb/business/entity/feeds/${businessId}";

    public static final String GET_ALL_CATEGORIES_URL = "cb/business/categories";

    public static final String CONSUMER_SEARCH_URL = "cb/consumer/search";
    public static final String CONSUMER_GET_BUSINESS_INFO_URL = "cb/consumer/entity/${businessId}";
    public static final String CONSUMER_GET_BUSINESS_PHOTO_URL = "cb/consumer/entity/photos/${businessId}";
    public static final String CONSUMER_UPLOAD_BUSINESS_PHOTO_URL = "cb/consumer/entity/photos/${businessId}/${photoId}";
    public static final String CONSUMER_DELETE_BUSINESS_PHOTO_URL = "cb/consumer/entity/photos/${businessId}/${photoId}";
    public static final String CONSUMER_GET_BUSINESS_FEED_URL = "cb/consumer/entity/feeds/${businessId}";
    public static final String CONSUMER_UPLOAD_BUSINESS_FEED_URL = "cb/consumer/entity/feeds/${businessId}";
    public static final String CONSUMER_EDIT_BUSINESS_FEED_URL = "cb/consumer/entity/feeds/${businessId}/${feedId}";
    public static final String CONSUMER_DELETE_BUSINESS_FEED_URL = "cb/consumer/entity/feeds/${businessId}/${feedId}";
    public static final String CONSUMER_ADD_BOOKMARK_URL = "cb/consumer/bookmark/${businessId}";
    public static final String CONSUMER_GET_BOOKMARKS_URL = "cb/consumer/bookmark";
    public static final String CONSUMER_DELETE_BOOKMARK_URL = "cb/consumer/bookmark/${bookmarkid}";
}
