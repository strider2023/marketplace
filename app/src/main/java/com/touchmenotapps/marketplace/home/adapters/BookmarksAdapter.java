package com.touchmenotapps.marketplace.home.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.touchmenotapps.marketplace.R;
import com.touchmenotapps.marketplace.home.dao.BookmarksDAO;
import com.touchmenotapps.marketplace.home.interfaces.BookmarkSelectionListener;
import com.touchmenotapps.marketplace.home.views.BookmarksViewHolder;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by i7 on 21-10-2017.
 */

public class BookmarksAdapter extends RecyclerView.Adapter<BookmarksViewHolder> {

    private List<BookmarksDAO> bookmarksDAOList = new ArrayList<>();
    private BookmarkSelectionListener bookmarkSelectionListener;

    public BookmarksAdapter(BookmarkSelectionListener bookmarkSelectionListener) {
        this.bookmarkSelectionListener = bookmarkSelectionListener;
    }

    public void setData(List<BookmarksDAO> bookmarksDAOList) {
        this.bookmarksDAOList = bookmarksDAOList;
        notifyDataSetChanged();
    }

    @Override
    public BookmarksViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_bookmarks,
                parent, false);
        return new BookmarksViewHolder(view);
    }

    @Override
    public void onBindViewHolder(BookmarksViewHolder holder, int position) {
        holder.setBookmarkSelectionListener(bookmarkSelectionListener);
        holder.setData(bookmarksDAOList.get(position));
    }

    @Override
    public int getItemCount() {
        return bookmarksDAOList.size();
    }
}