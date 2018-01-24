package com.touchmenotapps.marketplace.common.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.touchmenotapps.marketplace.R;
import com.touchmenotapps.marketplace.bo.BusinessImageDao;
import com.touchmenotapps.marketplace.common.interfaces.BusinessImageSelectedListener;
import com.touchmenotapps.marketplace.common.views.BusinessImageViewHolder;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by i7 on 24-01-2018.
 */

public class BusinessImageAdapter extends RecyclerView.Adapter<BusinessImageViewHolder> {

    private List<BusinessImageDao> businessImageDaos = new ArrayList<>();
    private BusinessImageSelectedListener businessImageSelectedListener;

    public BusinessImageAdapter(BusinessImageSelectedListener businessImageSelectedListener) {
        this.businessImageSelectedListener = businessImageSelectedListener;
    }

    public void setData(List<BusinessImageDao> businessImageDaos) {
        this.businessImageDaos.clear();
        this.businessImageDaos.addAll(businessImageDaos);
        notifyDataSetChanged();
    }

    @Override
    public BusinessImageViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_business_images,
                parent, false);
        return new BusinessImageViewHolder(view);
    }

    @Override
    public void onBindViewHolder(BusinessImageViewHolder holder, int position) {
        holder.setBusinessFeedSelectedListener(businessImageSelectedListener);
        holder.setData(businessImageDaos.get(position));
    }

    @Override
    public int getItemCount() {
        return businessImageDaos.size();
    }
}