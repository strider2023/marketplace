package com.touchmenotapps.marketplace.consumer.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.touchmenotapps.marketplace.R;
import com.touchmenotapps.marketplace.consumer.adapters.BookmarksAdapter;
import com.touchmenotapps.marketplace.consumer.dao.BookmarksDAO;
import com.touchmenotapps.marketplace.consumer.interfaces.BookmarkSelectionListener;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by i7 on 18-10-2017.
 */

public class BookmarksFragment extends Fragment implements BookmarkSelectionListener{

    @BindView(R.id.bookmarks_list)
    RecyclerView bookmarksList;
    @BindView(R.id.bookmarks_empty)
    LinearLayout emptyList;

    private View mViewHolder;
    private List<BookmarksDAO> bookmarksDAOList = new ArrayList<>();
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

        bookmarksDAOList.add(new BookmarksDAO());
        bookmarksAdapter = new BookmarksAdapter(this);
        linearLayoutManager = new LinearLayoutManager(getContext());

        bookmarksList.setLayoutManager(linearLayoutManager);
        bookmarksList.setAdapter(bookmarksAdapter);
        bookmarksAdapter.setData(bookmarksDAOList);

        emptyList.setVisibility(View.GONE);
        bookmarksList.setVisibility(View.VISIBLE);
        return mViewHolder;
    }

    @Override
    public void onBookmarkSelected(BookmarksDAO bookmarksDAO) {

    }
}
