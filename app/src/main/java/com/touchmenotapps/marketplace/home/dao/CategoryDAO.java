package com.touchmenotapps.marketplace.home.dao;

/**
 * Created by i7 on 20-10-2017.
 */

public class CategoryDAO {

    private int categoryIcon;
    private String categoryName;

    public CategoryDAO() {

    }

    public CategoryDAO(int categoryIcon, String categoryName) {
        this.categoryIcon = categoryIcon;
        this.categoryName = categoryName;
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
}
