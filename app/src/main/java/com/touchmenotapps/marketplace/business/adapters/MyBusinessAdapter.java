package com.touchmenotapps.marketplace.business.adapters;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.touchmenotapps.marketplace.R;
import com.touchmenotapps.marketplace.business.interfaces.BusinessSelectedListener;
import com.touchmenotapps.marketplace.business.views.BusinessViewHolder;
import com.touchmenotapps.marketplace.dao.BusinessDao;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by arindamnath on 30/12/17.
 */

public class MyBusinessAdapter extends RecyclerView.Adapter<BusinessViewHolder> {

    private List<BusinessDao> businessDaoList = new ArrayList<>();
    private BusinessSelectedListener businessSelectedListener;

    public MyBusinessAdapter(BusinessSelectedListener businessSelectedListener) {
        this.businessSelectedListener = businessSelectedListener;
    }

    public void setData(List<BusinessDao> businessDaoList) {
        this.businessDaoList.clear();
        this.businessDaoList.addAll(businessDaoList);
        notifyDataSetChanged();
    }

    @Override
    public BusinessViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_business,
                parent, false);
        return new BusinessViewHolder(view);
    }

    @Override
    public void onBindViewHolder(BusinessViewHolder holder, int position) {
        holder.setBusinessSelectedListener(businessSelectedListener);
        holder.setData(businessDaoList.get(position));
    }

    @Override
    public int getItemCount() {
        return businessDaoList.size();
    }
}
