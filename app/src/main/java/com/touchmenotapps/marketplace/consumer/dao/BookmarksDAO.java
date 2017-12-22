package com.touchmenotapps.marketplace.consumer.dao;

/**
 * Created by i7 on 21-10-2017.
 */

public class BookmarksDAO {

    private String name;
    private String category;
    private String offers;
    private String rating;
    private String distance;

    public BookmarksDAO() {

    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getOffers() {
        return offers;
    }

    public void setOffers(String offers) {
        this.offers = offers;
    }

    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }

    public String getDistance() {
        return distance;
    }

    public void setDistance(String distance) {
        this.distance = distance;
    }
}
