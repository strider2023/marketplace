package com.touchmenotapps.marketplace.consumer.fragments;

import android.support.design.widget.BottomSheetDialogFragment;

/**
 * Created by i7 on 30-01-2018.
 */

public class CategoriesFragment extends BottomSheetDialogFragment {

    public interface CategorySelected {
        void onCategorySelected(String category, String subcategory, String filterBy, String filterOrder);
    }
}
