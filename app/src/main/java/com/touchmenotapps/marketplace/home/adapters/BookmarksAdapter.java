package com.touchmenotapps.marketplace.home.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.touchmenotapps.marketplace.R;
import com.touchmenotapps.marketplace.home.views.BookmarksViewHolder;

/**
 * Created by i7 on 21-10-2017.
 */

public class BookmarksAdapter extends RecyclerView.Adapter<BookmarksViewHolder> {

    @Override
    public BookmarksViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_bookmarks,
                parent, false);
        return new BookmarksViewHolder(view);
    }

    @Override
    public void onBindViewHolder(BookmarksViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }
}
