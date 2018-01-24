package com.touchmenotapps.marketplace.framework.enums;

/**
 * Created by i7 on 03-01-2018.
 */

public enum LoaderID {
    FETCH_MY_BUSINESS(1),
    FETCH_BUSINESS_FEED(2),
    FETCH_BUSINESS_IMAGES(3),
    FETCH_BOOKMARKS(4),
    FETCH_BUSINESS_SEARCH(5);

    private final int value;
    LoaderID (final int newValue) {
        value = newValue;
    }
    public int getValue() { return value; }
}
