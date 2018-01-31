package com.touchmenotapps.marketplace.common.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.touchmenotapps.marketplace.R;
import com.touchmenotapps.marketplace.bo.OffersDao;
import com.touchmenotapps.marketplace.common.interfaces.BusinessFeedSelectedListener;
import com.touchmenotapps.marketplace.common.views.BusinessOfferViewHolder;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by arindamnath on 03/01/18.
 */

public class BusinessFeedAdapter extends RecyclerView.Adapter<BusinessOfferViewHolder>{

    private List<OffersDao> offersDaoList = new ArrayList<>();
    private BusinessFeedSelectedListener businessFeedSelectedListener;

    public BusinessFeedAdapter(BusinessFeedSelectedListener callback) {
        this.businessFeedSelectedListener = callback;
    }

    public void setData(List<OffersDao> offersDaoList) {
        this.offersDaoList.clear();
        this.offersDaoList.addAll(offersDaoList);
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
        holder.setData(offersDaoList.get(position));
    }

    @Override
    public int getItemCount() {
        return offersDaoList.size();
    }
}
