package com.touchmenotapps.marketplace.consumer.fragments;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetDialogFragment;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.touchmenotapps.marketplace.R;
import com.touchmenotapps.marketplace.bo.CategoryDao;
import com.touchmenotapps.marketplace.bo.CategoryListDao;
import com.touchmenotapps.marketplace.consumer.adapters.CategoriesFilterAdapter;
import com.touchmenotapps.marketplace.consumer.interfaces.CategoryFilterSelectionListener;
import com.touchmenotapps.marketplace.framework.enums.ServerEvents;
import com.touchmenotapps.marketplace.framework.interfaces.ServerResponseListener;
import com.touchmenotapps.marketplace.threads.asynctasks.GetCategoriesTask;

import org.json.simple.JSONObject;

import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by i7 on 30-01-2018.
 */

public class CategoriesFragment extends BottomSheetDialogFragment
        implements ServerResponseListener, CategoryFilterSelectionListener {

    @BindView(R.id.categories_list)
    RecyclerView detailsList;

    private View mViewHolder;
    private CategoryListDao allCategoryDao;
    private List<CategoryDao> categories;
    private CategoriesFilterAdapter categoriesFilterAdapter;
    private CategorySelectedListener categorySelectedListener;

    public interface CategorySelectedListener {
        void onCategorySelected(CategoryDao categoryDao);
    }

    public static CategoriesFragment newInstance() {
        CategoriesFragment fragment = new CategoriesFragment();
        return fragment;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            categorySelectedListener = (CategorySelectedListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement FilterListener");
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mViewHolder = inflater.inflate(R.layout.fragment_categories, container, false);
        ButterKnife.bind(this, mViewHolder);

        categoriesFilterAdapter = new CategoriesFilterAdapter(this);
        detailsList.setLayoutManager(new GridLayoutManager(getContext(), 2));
        detailsList.setAdapter(categoriesFilterAdapter);
        return mViewHolder;
    }

    @Override
    public void onResume() {
        super.onResume();
        new GetCategoriesTask(1, getContext(), this, true)
                .execute(new JSONObject[]{});
    }

    @Override
    public void onSuccess(int threadId, Object object) {
        allCategoryDao = (CategoryListDao) object;
        categories = Arrays.asList(allCategoryDao.getCategoriesMap().keySet().toArray(
                new CategoryDao[allCategoryDao.getCategoriesMap().keySet().size()]));
        categoriesFilterAdapter.setData(categories);
    }

    @Override
    public void onFaliure(ServerEvents serverEvents, Object object) {
        Snackbar.make(detailsList, "An error occurred.", Snackbar.LENGTH_LONG).show();
    }

    @Override
    public void onCategorySelected(CategoryDao categoryDao) {
        categorySelectedListener.onCategorySelected(categoryDao);
        dismiss();
    }
}
