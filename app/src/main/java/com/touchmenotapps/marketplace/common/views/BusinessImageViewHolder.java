package com.touchmenotapps.marketplace.common.views;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.touchmenotapps.marketplace.R;
import com.touchmenotapps.marketplace.bo.BusinessImageDao;
import com.touchmenotapps.marketplace.common.interfaces.BusinessImageSelectedListener;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by i7 on 24-01-2018.
 */

public class BusinessImageViewHolder extends RecyclerView.ViewHolder {

    @BindView(R.id.business_image)
    ImageView image;

    private BusinessImageSelectedListener businessImageSelectedListener;
    private BusinessImageDao businessImageDao;
    private Context context;

    public BusinessImageViewHolder(View itemView) {
        super(itemView);
        context = itemView.getContext();
        ButterKnife.bind(this, itemView);
    }

    @OnClick(R.id.business_image)
    public void onBusinessClicked() {
        if(businessImageSelectedListener != null){
            businessImageSelectedListener.onImageClicked(businessImageDao);
        }
    }

    public void setBusinessFeedSelectedListener(BusinessImageSelectedListener businessImageSelectedListener) {
        this.businessImageSelectedListener = businessImageSelectedListener;
    }

    public void setData(BusinessImageDao businessImageDao) {
        this.businessImageDao = businessImageDao;
        Glide.with(context)
                .load(businessImageDao.getFile())
                .error(R.drawable.ic_shop)
                .placeholder(R.drawable.ic_shop)
                .centerCrop()
                .into(image);
    }
}
