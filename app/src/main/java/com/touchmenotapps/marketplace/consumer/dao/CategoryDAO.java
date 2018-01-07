package com.touchmenotapps.marketplace.consumer.dao;

/**
 * Created by i7 on 20-10-2017.
 */

public class CategoryDAO {

    private int categoryIcon;
    private String categoryName;
    private String keyword;

    public CategoryDAO() {

    }

    public CategoryDAO(int categoryIcon, String categoryName, String keyword) {
        this.categoryIcon = categoryIcon;
        this.categoryName = categoryName;
        this.keyword = keyword;
    }

    public int getCategoryIcon() {
        return categoryIcon;
    }

    public void setCategoryIcon(int categoryIcon) {
        this.categoryIcon = categoryIcon;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }
}
