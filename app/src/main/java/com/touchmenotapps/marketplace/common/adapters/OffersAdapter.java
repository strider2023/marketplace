package com.touchmenotapps.marketplace.common.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;

import com.touchmenotapps.marketplace.R;
import com.touchmenotapps.marketplace.bo.OffersDao;
import com.touchmenotapps.marketplace.common.interfaces.BusinessFeedSelectedListener;
import com.touchmenotapps.marketplace.common.views.BusinessOfferViewHolder;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by arindamnath on 03/01/18.
 */

public class OffersAdapter extends RecyclerView.Adapter<BusinessOfferViewHolder> implements Filterable {

    private List<OffersDao> offersDaoList = new ArrayList<>();
    private List<OffersDao> offersFilteredList = new ArrayList<>();
    private BusinessFeedSelectedListener businessFeedSelectedListener;

    public OffersAdapter(BusinessFeedSelectedListener callback) {
        this.businessFeedSelectedListener = callback;
    }

    public void setData(List<OffersDao> offersDaoList) {
        this.offersDaoList.clear();
        this.offersDaoList.addAll(offersDaoList);
        this.offersFilteredList.clear();
        this.offersFilteredList.addAll(offersDaoList);
        notifyDataSetChanged();
    }

    @Override
    public BusinessOfferViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_feed,
                parent, false);
        return new BusinessOfferViewHolder(view);
    }

    @Override
    public void onBindViewHolder(BusinessOfferViewHolder holder, int position) {
        holder.setBusinessFeedSelectedListener(businessFeedSelectedListener);
        holder.setData(offersFilteredList.get(position));
    }

    @Override
    public int getItemCount() {
        return offersFilteredList.size();
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                String charString = constraint.toString();
                if (charString.isEmpty()) {
                    offersFilteredList = offersDaoList;
                } else {
                    List<OffersDao> filteredList = new ArrayList<>();
                    for (OffersDao row : offersDaoList) {
                        if (row.getBusinessName().toLowerCase().startsWith(charString.toLowerCase())
                                || row.getCaption().toLowerCase().startsWith(charString.toLowerCase())) {
                            filteredList.add(row);
                        }
                    }
                    offersFilteredList = filteredList;
                }
                FilterResults filterResults = new FilterResults();
                filterResults.values = offersFilteredList;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                notifyDataSetChanged();
            }
        };
    }
}
