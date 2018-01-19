package com.touchmenotapps.marketplace.common.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.touchmenotapps.marketplace.R;
import com.touchmenotapps.marketplace.bo.FeedDao;
import com.touchmenotapps.marketplace.common.interfaces.BusinessFeedSelectedListener;
import com.touchmenotapps.marketplace.common.views.BusinessFeedViewHolder;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by arindamnath on 03/01/18.
 */

public class BusinessFeedAdapter extends RecyclerView.Adapter<BusinessFeedViewHolder>{

    private List<FeedDao> feedDaoList = new ArrayList<>();
    private BusinessFeedSelectedListener businessFeedSelectedListener;

    public BusinessFeedAdapter(BusinessFeedSelectedListener callback) {
        this.businessFeedSelectedListener = callback;
    }

    public void setData(List<FeedDao> feedDaoList) {
        this.feedDaoList.clear();
        this.feedDaoList.addAll(feedDaoList);
        notifyDataSetChanged();
    }

    @Override
    public BusinessFeedViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_feed,
                parent, false);
        return new BusinessFeedViewHolder(view);
    }

    @Override
    public void onBindViewHolder(BusinessFeedViewHolder holder, int position) {
        holder.setBusinessFeedSelectedListener(businessFeedSelectedListener);
        holder.setData(feedDaoList.get(position));
    }

    @Override
    public int getItemCount() {
        return feedDaoList.size();
    }
}
