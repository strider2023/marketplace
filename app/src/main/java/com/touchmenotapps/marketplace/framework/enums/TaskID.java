package com.touchmenotapps.marketplace.framework.enums;

/**
 * Created by i7 on 08-01-2018.
 */

public enum TaskID {
    GET_CATEGORIES(1),
    SAVE_BUSINESS_DATA(2),
    GET_BUSINESS_DATA(3),
    UPDATE_BUSINESS_DATA(4);

    private final int value;

    TaskID (final int newValue) {
        value = newValue;
    }

    public int getValue() { return value; }
}