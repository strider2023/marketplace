package com.touchmenotapps.marketplace.consumer.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.touchmenotapps.marketplace.R;
import com.touchmenotapps.marketplace.bo.BusinessDao;
import com.touchmenotapps.marketplace.consumer.interfaces.BookmarkSelectionListener;
import com.touchmenotapps.marketplace.consumer.views.BookmarksViewHolder;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by i7 on 21-10-2017.
 */

public class BookmarksAdapter extends RecyclerView.Adapter<BookmarksViewHolder> {

    private List<BusinessDao> businessDaoList = new ArrayList<>();
    private BookmarkSelectionListener bookmarkSelectionListener;

    public BookmarksAdapter(BookmarkSelectionListener bookmarkSelectionListener) {
        this.bookmarkSelectionListener = bookmarkSelectionListener;
    }

    public void setData(List<BusinessDao> businessDaoList) {
        this.businessDaoList.clear();
        this.businessDaoList.addAll(businessDaoList);
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
        holder.setData(businessDaoList.get(position));
    }

    @Override
    public int getItemCount() {
        return businessDaoList.size();
    }
}
