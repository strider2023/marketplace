package com.touchmenotapps.marketplace.consumer.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.touchmenotapps.marketplace.R;
import com.touchmenotapps.marketplace.bo.BusinessDao;
import com.touchmenotapps.marketplace.business.adapters.BusinessAdapter;
import com.touchmenotapps.marketplace.business.fragments.MyBusinessesFragment;
import com.touchmenotapps.marketplace.consumer.adapters.CategoriesAdapter;
import com.touchmenotapps.marketplace.consumer.dao.CategoryDAO;
import com.touchmenotapps.marketplace.consumer.interfaces.CategorySelectionListener;
import com.touchmenotapps.marketplace.consumer.loaders.SearchLoaderTask;
import com.touchmenotapps.marketplace.framework.enums.LoaderID;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by i7 on 17-10-2017.
 */

public class BusinessFragment extends Fragment
        implements CategorySelectionListener, LoaderManager.LoaderCallbacks<List<BusinessDao>> {

    @BindView(R.id.categories_list)
    RecyclerView categoriesList;

    private View mViewHolder;
    private List<CategoryDAO> categoryDAOList = new ArrayList<>();
    private GridLayoutManager gridLayoutManager;
    private CategoriesAdapter categoriesAdapter;
    private Bundle queryData;
    private BusinessAdapter adapter;

    public static BusinessFragment newInstance() {
        BusinessFragment fragment = new BusinessFragment();
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mViewHolder = inflater.inflate(R.layout.fragment_offers, container, false);
        ButterKnife.bind(this, mViewHolder);

        categoryDAOList.add(new CategoryDAO(R.drawable.ic_restaurants, "Food"));
        categoryDAOList.add(new CategoryDAO(R.drawable.ic_apparel, "Fashion"));
        categoryDAOList.add(new CategoryDAO(R.drawable.ic_disco, "Pubs"));
        categoryDAOList.add(new CategoryDAO(R.drawable.ic_jwellrey, "Jewellery"));
        categoryDAOList.add(new CategoryDAO(R.drawable.ic_toys, "Toys"));
        categoryDAOList.add(new CategoryDAO(R.drawable.ic_worker, "Assistance"));
        categoryDAOList.add(new CategoryDAO(R.drawable.ic_travel, "Travel"));
        categoryDAOList.add(new CategoryDAO(R.drawable.ic_view_all, "View All"));

        categoriesAdapter = new CategoriesAdapter(BusinessFragment.this);
        gridLayoutManager = new GridLayoutManager(getActivity(), 4);
        categoriesList.setLayoutManager(gridLayoutManager);
        categoriesList.setAdapter(categoriesAdapter);
        categoriesAdapter.setData(categoryDAOList);

        return mViewHolder;
    }

    @Override
    public void onResume() {
        super.onResume();
        queryData = new Bundle();
        getActivity().getSupportLoaderManager()
                .initLoader(LoaderID.FETCH_MY_BUSINESS.getValue(), queryData,this).forceLoad();
    }

    @Override
    public void onCategorySelected(CategoryDAO categoryDAO) {

    }

    @Override
    public Loader<List<BusinessDao>> onCreateLoader(int id, Bundle args) {
        return new SearchLoaderTask(getActivity(), args);
    }

    @Override
    public void onLoadFinished(Loader<List<BusinessDao>> loader, List<BusinessDao> data) {

    }

    @Override
    public void onLoaderReset(Loader<List<BusinessDao>> loader) {

    }
}
