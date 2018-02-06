package com.touchmenotapps.marketplace.consumer.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;

import com.touchmenotapps.marketplace.R;
import com.touchmenotapps.marketplace.bo.BusinessDao;
import com.touchmenotapps.marketplace.consumer.interfaces.BookmarkSelectionListener;
import com.touchmenotapps.marketplace.consumer.views.BookmarksViewHolder;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by i7 on 21-10-2017.
 */

public class BookmarksAdapter extends RecyclerView.Adapter<BookmarksViewHolder> implements Filterable {

    private List<BusinessDao> businessDaoList = new ArrayList<>();
    private List<BusinessDao> businessFilterList = new ArrayList<>();
    private BookmarkSelectionListener bookmarkSelectionListener;

    public BookmarksAdapter(BookmarkSelectionListener bookmarkSelectionListener) {
        this.bookmarkSelectionListener = bookmarkSelectionListener;
    }

    public void setData(List<BusinessDao> businessDaoList) {
        this.businessDaoList.clear();
        this.businessDaoList.addAll(businessDaoList);
        this.businessFilterList.clear();
        this.businessFilterList.addAll(businessDaoList);
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
        holder.setData(businessFilterList.get(position));
    }

    @Override
    public int getItemCount() {
        return businessFilterList.size();
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                String charString = constraint.toString();
                if (charString.isEmpty()) {
                    businessFilterList = businessDaoList;
                } else {
                    List<BusinessDao> filteredList = new ArrayList<>();
                    for (BusinessDao row : businessFilterList) {
                        if (row.getName().toLowerCase().startsWith(charString.toLowerCase())
                                || row.getCategory().toLowerCase().startsWith(charString.toLowerCase())) {
                            filteredList.add(row);
                        }
                    }
                    businessFilterList = filteredList;
                }
                FilterResults filterResults = new FilterResults();
                filterResults.values = businessFilterList;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                notifyDataSetChanged();
            }
        };
    }
}
