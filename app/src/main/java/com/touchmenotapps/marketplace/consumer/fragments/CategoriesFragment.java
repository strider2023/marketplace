package com.touchmenotapps.marketplace.consumer.fragments;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetDialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.touchmenotapps.marketplace.R;
import com.touchmenotapps.marketplace.framework.enums.ServerEvents;
import com.touchmenotapps.marketplace.framework.interfaces.ServerResponseListener;
import com.touchmenotapps.marketplace.threads.asynctasks.GetCategoriesTask;

import org.json.simple.JSONObject;

import butterknife.ButterKnife;

/**
 * Created by i7 on 30-01-2018.
 */

public class CategoriesFragment extends BottomSheetDialogFragment
        implements ServerResponseListener {

    private View mViewHolder;
    private CategorySelectedListener categorySelectedListener;

    public interface CategorySelectedListener {
        void onCategorySelected(String category);
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
        mViewHolder = inflater.inflate(R.layout.fragment_filter, container, false);
        ButterKnife.bind(this, mViewHolder);
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

    }

    @Override
    public void onFaliure(ServerEvents serverEvents, Object object) {

    }
}
