package com.touchmenotapps.marketplace.consumer;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;

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
import butterknife.OnClick;

/**
 * Created by i7 on 06-02-2018.
 */

public class SelectCategoryActivity extends AppCompatActivity
        implements ServerResponseListener, CategoryFilterSelectionListener {

    @BindView(R.id.categories_list)
    RecyclerView detailsList;

    private CategoryListDao allCategoryDao;
    private List<CategoryDao> categories;
    private CategoriesFilterAdapter categoriesFilterAdapter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_category);
        ButterKnife.bind(this);

        categoriesFilterAdapter = new CategoriesFilterAdapter(this);
        detailsList.setLayoutManager(new GridLayoutManager(this, 2));
        detailsList.setAdapter(categoriesFilterAdapter);
    }

    @OnClick(R.id.close_selection)
    public void onCloseSelection() {
        finish();
    }

    @Override
    public void onResume() {
        super.onResume();
        new GetCategoriesTask(1, this, this, true)
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
        Intent data = new Intent();
        data.putExtra("selectedCategory", categoryDao);
        setResult(RESULT_OK, data);
        finish();
    }
}
