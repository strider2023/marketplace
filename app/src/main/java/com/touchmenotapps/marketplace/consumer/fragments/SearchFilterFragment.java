package com.touchmenotapps.marketplace.consumer.fragments;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetDialogFragment;
import android.support.v7.widget.AppCompatMultiAutoCompleteTextView;
import android.support.v7.widget.AppCompatSpinner;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.RadioGroup;
import android.widget.Spinner;

import com.touchmenotapps.marketplace.R;
import com.touchmenotapps.marketplace.bo.CategoryDao;
import com.touchmenotapps.marketplace.bo.CategoryListDao;
import com.touchmenotapps.marketplace.common.adapters.CategoriesAdapter;
import com.touchmenotapps.marketplace.framework.enums.ServerEvents;
import com.touchmenotapps.marketplace.framework.interfaces.ServerResponseListener;
import com.touchmenotapps.marketplace.threads.asynctasks.GetCategoriesTask;

import org.json.simple.JSONObject;

import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnItemSelected;

/**
 * Created by arindamnath on 27/01/18.
 */

public class SearchFilterFragment extends BottomSheetDialogFragment
        implements ServerResponseListener {

    @BindView(R.id.filter_category)
    AppCompatSpinner category;
    @BindView(R.id.filter_subcategory)
    AppCompatMultiAutoCompleteTextView subCategory;
    @BindView(R.id.filter_by)
    RadioGroup filterBy;
    @BindView(R.id.filter_sort_by)
    RadioGroup sortBy;

    private View mViewHolder;
    private CategoriesAdapter categoriesAdapter, subCategoriesAdapter;
    private CategoryListDao allCategoryDao;
    private List<CategoryDao> categories, subCategories;
    private FilterListener filterListener;
    private CategoryDao selectedCategory;
    private String selectedSubCategories, filterdBy, sortedBy;

    public interface FilterListener {
        void onFilterSelected(String category, String subcategory, String filterBy, String filterOrder);
    }

    public static SearchFilterFragment newInstance() {
        SearchFilterFragment fragment = new SearchFilterFragment();
        return fragment;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            filterListener = (FilterListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement FilterListener");
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mViewHolder = inflater.inflate(R.layout.fragment_filter, container, false);
        ButterKnife.bind(this, mViewHolder);

        categoriesAdapter = new CategoriesAdapter(getContext());
        subCategoriesAdapter = new CategoriesAdapter(getContext());
        selectedCategory = new CategoryDao();

        category.setAdapter(categoriesAdapter);
        subCategory.setAdapter(subCategoriesAdapter);
        subCategory.setTokenizer(new AppCompatMultiAutoCompleteTextView.CommaTokenizer());

        subCategory.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if(selectedSubCategories == null) {
                    selectedSubCategories = subCategories.get(position).getEnumText();
                } else {
                    selectedSubCategories += "," + subCategories.get(position).getEnumText();
                }
            }
        });
        return mViewHolder;
    }

    @OnItemSelected(R.id.filter_category)
    public void categorySelected(Spinner spinner, int position) {
        selectedSubCategories = null;
        selectedCategory = categories.get(position);
        subCategories = allCategoryDao.getCategoriesMap().get(selectedCategory);
        subCategoriesAdapter.setData(subCategories);
        subCategory.setText("");
    }

    @Override
    public void onResume() {
        super.onResume();
        new GetCategoriesTask(1, getContext(), this, true)
                .execute(new JSONObject[]{});
    }

    @OnClick(R.id.filter_submit)
    public void onFilterSubmit() {
        switch (filterBy.getCheckedRadioButtonId()) {
            case R.id.filter_by_distance:
                filterdBy = "DISTANCE";
                break;
            case R.id.filter_by_rating:
                filterdBy = "ONE_SCORE_RATING";
                break;
        }
        switch (sortBy.getCheckedRadioButtonId()) {
            case R.id.filter_sort_ascend:
                sortedBy = "ASC";
                break;
            case R.id.filter_sort_descend:
                sortedBy = "DESC";
                break;
        }
        if(filterListener != null) {
            filterListener.onFilterSelected(selectedCategory.getEnumText(), selectedSubCategories, filterdBy, sortedBy);
        }
        this.dismiss();
    }

    @Override
    public void onSuccess(int threadId, Object object) {
        allCategoryDao = (CategoryListDao) object;
        categories = Arrays.asList(allCategoryDao.getCategoriesMap().keySet().toArray(
                new CategoryDao[allCategoryDao.getCategoriesMap().keySet().size()]));
        categoriesAdapter.setData(categories);
    }

    @Override
    public void onFaliure(ServerEvents serverEvents, Object object) {

    }
}
