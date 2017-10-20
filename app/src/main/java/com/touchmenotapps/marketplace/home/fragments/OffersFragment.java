package com.touchmenotapps.marketplace.home.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.touchmenotapps.marketplace.R;
import com.touchmenotapps.marketplace.home.adapters.CategoriesAdapter;
import com.touchmenotapps.marketplace.home.dao.CategoryDAO;
import com.touchmenotapps.marketplace.home.interfaces.CategorySelectionListener;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by i7 on 17-10-2017.
 */

public class OffersFragment extends Fragment implements CategorySelectionListener {

    private View mViewHolder;
    @BindView(R.id.categories_list)
    RecyclerView categoriesList;

    private List<CategoryDAO> categoryDAOList = new ArrayList<>();
    private GridLayoutManager gridLayoutManager;
    private CategoriesAdapter categoriesAdapter;

    public static OffersFragment newInstance() {
        OffersFragment fragment = new OffersFragment();
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

        categoriesAdapter = new CategoriesAdapter(OffersFragment.this);
        gridLayoutManager = new GridLayoutManager(getActivity(), 4);
        categoriesList.setLayoutManager(gridLayoutManager);
        categoriesList.setAdapter(categoriesAdapter);
        categoriesAdapter.setCategoryDAOList(categoryDAOList);

        return mViewHolder;
    }

    @Override
    public void onCategorySelected(CategoryDAO categoryDAO) {

    }
}
