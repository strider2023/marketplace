package com.touchmenotapps.marketplace.consumer.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.touchmenotapps.marketplace.R;
import com.touchmenotapps.marketplace.bo.BusinessDao;
import com.touchmenotapps.marketplace.common.BusinessDetailsActivity;
import com.touchmenotapps.marketplace.consumer.adapters.BookmarksAdapter;
import com.touchmenotapps.marketplace.consumer.interfaces.BookmarkSelectionListener;
import com.touchmenotapps.marketplace.threads.loaders.BookmarkLoaderTask;
import com.touchmenotapps.marketplace.framework.enums.LoaderID;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.touchmenotapps.marketplace.framework.constants.AppConstants.BUSINESS_BOOKMARKED_TAG;
import static com.touchmenotapps.marketplace.framework.constants.AppConstants.BUSINESS_BOOKMARK_ID_TAG;
import static com.touchmenotapps.marketplace.framework.constants.AppConstants.BUSINESS_CATEGORY_TAG;
import static com.touchmenotapps.marketplace.framework.constants.AppConstants.BUSINESS_ID_TAG;
import static com.touchmenotapps.marketplace.framework.constants.AppConstants.BUSINESS_NAME_TAG;
import static com.touchmenotapps.marketplace.framework.constants.AppConstants.BUSINESS_RATING_TAG;

/**
 * Created by i7 on 18-10-2017.
 */

public class BookmarksFragment extends Fragment
        implements BookmarkSelectionListener, LoaderManager.LoaderCallbacks<List<BusinessDao>>{

    @BindView(R.id.refresh_layout)
    SwipeRefreshLayout refreshLayout;
    @BindView(R.id.details_list)
    RecyclerView detailsList;
    @BindView(R.id.bookmarks_empty)
    LinearLayout emptyList;
    @BindView(R.id.list_filter_text)
    AppCompatEditText filterText;

    private View mViewHolder;
    private Bundle queryData;
    private BookmarksAdapter bookmarksAdapter;

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
        detailsList.setLayoutManager(new LinearLayoutManager(getContext()));
        detailsList.setAdapter(bookmarksAdapter);
        filterText.setHint("Search Bookmarks");

        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                queryData = new Bundle();
                getActivity().getSupportLoaderManager()
                        .initLoader(LoaderID.FETCH_MY_BUSINESS.getValue(), queryData, BookmarksFragment.this).forceLoad();
            }
        });

        filterText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                bookmarksAdapter.getFilter().filter(s);
            }

            @Override
            public void afterTextChanged(Editable s) { }
        });

        filterText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    bookmarksAdapter.getFilter().filter(filterText.getEditableText().toString().trim());
                    filterText.clearFocus();
                    return true;
                }
                return false;
            }
        });
        return mViewHolder;
    }

    @Override
    public void onBookmarkSelected(BusinessDao businessDao) {
        Intent intent = new Intent(getActivity(), BusinessDetailsActivity.class);
        intent.putExtra(BUSINESS_ID_TAG, businessDao.getId());
        intent.putExtra(BUSINESS_NAME_TAG, businessDao.getName());
        intent.putExtra(BUSINESS_CATEGORY_TAG, businessDao.getCategory());
        intent.putExtra(BUSINESS_RATING_TAG, businessDao.getSingleScoreRating());
        intent.putExtra(BUSINESS_BOOKMARKED_TAG, businessDao.isBookmarked());
        intent.putExtra(BUSINESS_BOOKMARK_ID_TAG, businessDao.getBookmarkId());
        startActivity(intent);
    }

    @Override
    public void onResume() {
        super.onResume();
        queryData = new Bundle();
        getActivity().getSupportLoaderManager()
                .initLoader(LoaderID.FETCH_BOOKMARKS.getValue(), queryData,this).forceLoad();
    }

    @Override
    public Loader<List<BusinessDao>> onCreateLoader(int id, Bundle args) {
        refreshLayout.setRefreshing(true);
        return new BookmarkLoaderTask(getActivity(), args);
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
