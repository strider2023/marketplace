package com.touchmenotapps.marketplace.consumer.views;

import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.touchmenotapps.marketplace.R;
import com.touchmenotapps.marketplace.bo.CategoryDao;
import com.touchmenotapps.marketplace.consumer.interfaces.CategoryFilterSelectionListener;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by i7 on 01-02-2018.
 */

public class CategoryFilterViewHolder extends RecyclerView.ViewHolder {

    @BindView(R.id.category_name)
    AppCompatTextView categoryName;

    private CategoryDao categoryDao;
    private CategoryFilterSelectionListener categoryFilterSelectionListener;

    public CategoryFilterViewHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
    }

    public void setCategoryFilterSelectionListener(CategoryFilterSelectionListener categoryFilterSelectionListener) {
        this.categoryFilterSelectionListener = categoryFilterSelectionListener;
    }

    @OnClick(R.id.categories_base_container)
    public void onCategorySelected() {
        if(this.categoryFilterSelectionListener != null) {
            this.categoryFilterSelectionListener.onCategorySelected(categoryDao);
        }
    }

    public void setData(CategoryDao categoryDao) {
        this.categoryDao = categoryDao;
        categoryName.setText(categoryDao.getDescription());
    }
}
