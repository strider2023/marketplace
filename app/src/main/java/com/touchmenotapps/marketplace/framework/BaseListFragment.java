package com.touchmenotapps.marketplace.framework;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.touchmenotapps.marketplace.R;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by i7 on 08-01-2018.
 */

public abstract class BaseListFragment extends Fragment {

    private int layoutId = -1;
    private View mViewHolder;

    @BindView(R.id.refresh_layout)
    SwipeRefreshLayout refreshLayout;
    @BindView(R.id.details_list)
    RecyclerView detailsList;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        layoutId = setFragmentLayout();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mViewHolder = inflater.inflate(layoutId, container, false);
        ButterKnife.bind(this, mViewHolder);

        refreshLayout.setRefreshing(false);

        onViewCreated(mViewHolder);
        return mViewHolder;
    }

    protected abstract int setFragmentLayout();

    protected abstract void onViewCreated(View view);

    public int getLayoutId() {
        return layoutId;
    }

    public View getViewHolder() {
        return mViewHolder;
    }

    public SwipeRefreshLayout getRefreshLayout() {
        return refreshLayout;
    }

    public RecyclerView getDetailsList() {
        return detailsList;
    }
}
