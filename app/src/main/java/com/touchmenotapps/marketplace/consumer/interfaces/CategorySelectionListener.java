package com.touchmenotapps.marketplace.consumer.interfaces;

import com.touchmenotapps.marketplace.consumer.dao.HomeCategoryDao;

/**
 * Created by i7 on 20-10-2017.
 */

public interface CategorySelectionListener {

    void onCategorySelected(HomeCategoryDao homeCategoryDao);
}
