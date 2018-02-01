package com.touchmenotapps.marketplace.consumer.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.touchmenotapps.marketplace.R;
import com.touchmenotapps.marketplace.bo.CategoryDao;
import com.touchmenotapps.marketplace.consumer.interfaces.CategoryFilterSelectionListener;
import com.touchmenotapps.marketplace.consumer.views.CategoryFilterViewHolder;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by i7 on 01-02-2018.
 */

public class CategoriesFilterAdapter extends RecyclerView.Adapter<CategoryFilterViewHolder>{

    private List<CategoryDao> categories = new ArrayList<>();
    private CategoryFilterSelectionListener categoryFilterSelectionListener;

    public CategoriesFilterAdapter(CategoryFilterSelectionListener callback) {
        this.categoryFilterSelectionListener = callback;
    }

    public void setData(List<CategoryDao> offersDaoList) {
        this.categories.clear();
        this.categories.addAll(offersDaoList);
        notifyDataSetChanged();
    }

    @Override
    public CategoryFilterViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_category_filter,
                parent, false);
        return new CategoryFilterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(CategoryFilterViewHolder holder, int position) {
        holder.setCategoryFilterSelectionListener(categoryFilterSelectionListener);
        holder.setData(categories.get(position));
    }

    @Override
    public int getItemCount() {
        return categories.size();
    }
}
