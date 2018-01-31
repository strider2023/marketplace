package com.touchmenotapps.marketplace.common.views;

import android.content.Context;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.touchmenotapps.marketplace.R;
import com.touchmenotapps.marketplace.bo.OffersDao;
import com.touchmenotapps.marketplace.common.interfaces.BusinessFeedSelectedListener;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by arindamnath on 03/01/18.
 */

public class BusinessOfferViewHolder extends RecyclerView.ViewHolder {

    @BindView(R.id.feed_image)
    ImageView image;
    @BindView(R.id.feed_description)
    AppCompatTextView description;
    @BindView(R.id.feed_coupon)
    AppCompatTextView coupon;
    @BindView(R.id.feed_start_date)
    AppCompatTextView startsOn;
    @BindView(R.id.feed_end_date)
    AppCompatTextView endsOn;
    @BindView(R.id.feed_business_name)
    AppCompatTextView businessName;

    private BusinessFeedSelectedListener businessFeedSelectedListener;
    private OffersDao offersDao;
    private Context context;

    public BusinessOfferViewHolder(View itemView) {
        super(itemView);
        context = itemView.getContext();
        ButterKnife.bind(this, itemView);
    }

    @OnClick(R.id.business_feed_base_container)
    public void onBusinessClicked() {
        if(businessFeedSelectedListener != null){
            businessFeedSelectedListener.onBusinessFeedSelected(offersDao);
        }
    }

    public void setBusinessFeedSelectedListener(BusinessFeedSelectedListener businessFeedSelectedListener) {
        this.businessFeedSelectedListener = businessFeedSelectedListener;
    }

    public void setData(OffersDao offersDao) {
        this.offersDao = offersDao;
        this.description.setText(offersDao.getCaption());
        this.startsOn.setText(offersDao.getStartDate());
        this.endsOn.setText(offersDao.getEndDate());
        this.coupon.setText(offersDao.getRedeeemCode());
        this.businessName.setText(offersDao.getBusinessName());
        Glide.with(context)
                .load(offersDao.getImageURL())
                .error(R.drawable.ic_shop)
                .placeholder(R.drawable.ic_shop)
                .centerCrop()
                .into(image);
    }
}
