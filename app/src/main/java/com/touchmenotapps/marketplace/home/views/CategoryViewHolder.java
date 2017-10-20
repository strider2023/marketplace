package com.touchmenotapps.marketplace.home.views;

import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;

import com.touchmenotapps.marketplace.R;
import com.touchmenotapps.marketplace.home.dao.CategoryDAO;
import com.touchmenotapps.marketplace.home.interfaces.CategorySelectionListener;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by i7 on 20-10-2017.
 */

public class CategoryViewHolder extends RecyclerView.ViewHolder {

    @BindView(R.id.categories_image)
    ImageView categoryIcon;
    @BindView(R.id.categories_name)
    AppCompatTextView categoryName;

    private CategoryDAO categoryDAO;
    private CategorySelectionListener categorySelectionListener;

    public CategoryViewHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
    }

    public void setCategorySelectionListener(CategorySelectionListener categorySelectionListener) {
        this.categorySelectionListener = categorySelectionListener;
    }

    @OnClick(R.id.categories_base_container)
    public void onCategorySelected() {
        if(this.categorySelectionListener != null) {
            this.categorySelectionListener.onCategorySelected(categoryDAO);
        }
    }

    public void setData(CategoryDAO categoryDAO) {
        this.categoryDAO = categoryDAO;
        categoryIcon.setImageResource(categoryDAO.getCategoryIcon());
        categoryName.setText(categoryDAO.getCategoryName());
    }
}
