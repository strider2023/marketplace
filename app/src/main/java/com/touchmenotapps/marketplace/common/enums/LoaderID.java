package com.touchmenotapps.marketplace.common.enums;

/**
 * Created by i7 on 03-01-2018.
 */

public enum LoaderID {
    FETCH_MY_BUSINESS(1);

    private final int value;
    LoaderID (final int newValue) {
        value = newValue;
    }
    public int getValue() { return value; }
}
