package com.touchmenotapps.marketplace.consumer.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.touchmenotapps.marketplace.R;
import com.touchmenotapps.marketplace.bo.HomeCategoryDao;
import com.touchmenotapps.marketplace.consumer.interfaces.CategorySelectionListener;
import com.touchmenotapps.marketplace.consumer.views.CategoryViewHolder;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by i7 on 20-10-2017.
 */

public class CategoriesAdapter extends RecyclerView.Adapter<CategoryViewHolder> {

    private List<HomeCategoryDao> homeCategoryDaoList = new ArrayList<>();
    private CategorySelectionListener categorySelectionListener;

    public CategoriesAdapter(CategorySelectionListener categorySelectionListener) {
        this.categorySelectionListener = categorySelectionListener;
    }

    public void setData(List<HomeCategoryDao> homeCategoryDaoList) {
        this.homeCategoryDaoList = homeCategoryDaoList;
        notifyDataSetChanged();
    }

    @Override
    public CategoryViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_categories,
                parent, false);
        return new CategoryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(CategoryViewHolder holder, int position) {
        holder.setCategorySelectionListener(categorySelectionListener);
        holder.setData(homeCategoryDaoList.get(position));
    }

    @Override
    public int getItemCount() {
        return homeCategoryDaoList.size();
    }
}
