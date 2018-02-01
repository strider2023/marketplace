package com.touchmenotapps.marketplace.consumer.interfaces;

import com.touchmenotapps.marketplace.bo.CategoryDao;

/**
 * Created by i7 on 01-02-2018.
 */

public interface CategoryFilterSelectionListener {

    void onCategorySelected(CategoryDao categoryDao);
}
