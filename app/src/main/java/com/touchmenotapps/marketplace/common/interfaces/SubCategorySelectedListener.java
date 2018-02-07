package com.touchmenotapps.marketplace.common.interfaces;

import com.touchmenotapps.marketplace.bo.CategoryDao;

import java.util.List;

/**
 * Created by i7 on 07-02-2018.
 */

public interface SubCategorySelectedListener {

    void onSubcategoriesSelected(String names, List<CategoryDao> subCategories);
}
