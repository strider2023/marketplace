package com.touchmenotapps.marketplace.consumer.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.touchmenotapps.marketplace.R;
import com.touchmenotapps.marketplace.bo.BusinessDao;
import com.touchmenotapps.marketplace.business.fragments.MyBusinessesFragment;
import com.touchmenotapps.marketplace.consumer.adapters.BookmarksAdapter;
import com.touchmenotapps.marketplace.consumer.dao.BookmarksDAO;
import com.touchmenotapps.marketplace.consumer.interfaces.BookmarkSelectionListener;
import com.touchmenotapps.marketplace.framework.enums.LoaderID;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by i7 on 18-10-2017.
 */

public class BookmarksFragment extends Fragment
        implements BookmarkSelectionListener, LoaderManager.LoaderCallbacks<List<BusinessDao>>{

    @BindView(R.id.bookmarks_refresh_layout)
    SwipeRefreshLayout refreshLayout;
    @BindView(R.id.bookmarks_list)
    RecyclerView bookmarksList;
    @BindView(R.id.bookmarks_empty)
    LinearLayout emptyList;

    private View mViewHolder;
    private Bundle queryData;
    private BookmarksAdapter bookmarksAdapter;
    private LinearLayoutManager linearLayoutManager;

    public static BookmarksFragment newInstance() {
        BookmarksFragment fragment = new BookmarksFragment();
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mViewHolder = inflater.inflate(R.layout.fragment_bookmarks, container, false);
        ButterKnife.bind(this, mViewHolder);

        refreshLayout.setRefreshing(false);
        bookmarksAdapter = new BookmarksAdapter(this);
        linearLayoutManager = new LinearLayoutManager(getContext());
        bookmarksList.setLayoutManager(linearLayoutManager);
        bookmarksList.setAdapter(bookmarksAdapter);

        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                queryData = new Bundle();
                getActivity().getSupportLoaderManager()
                        .initLoader(LoaderID.FETCH_MY_BUSINESS.getValue(), queryData, BookmarksFragment.this).forceLoad();
            }
        });
        return mViewHolder;
    }

    @Override
    public void onBookmarkSelected(BusinessDao businessDao) {

    }

    @Override
    public void onResume() {
        super.onResume();
        queryData = new Bundle();
        getActivity().getSupportLoaderManager()
                .initLoader(LoaderID.FETCH_MY_BUSINESS.getValue(), queryData,this).forceLoad();
    }

    @Override
    public Loader<List<BusinessDao>> onCreateLoader(int id, Bundle args) {
        refreshLayout.setRefreshing(true);
        return null;
    }

    @Override
    public void onLoadFinished(Loader<List<BusinessDao>> loader, List<BusinessDao> data) {
        refreshLayout.setRefreshing(false);
        if(data.size() > 0) {
            refreshLayout.setVisibility(View.VISIBLE);
            emptyList.setVisibility(View.GONE);
            bookmarksAdapter.setData(data);
        } else {
            refreshLayout.setVisibility(View.GONE);
            emptyList.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onLoaderReset(Loader<List<BusinessDao>> loader) {
        refreshLayout.setRefreshing(false);
    }
}
