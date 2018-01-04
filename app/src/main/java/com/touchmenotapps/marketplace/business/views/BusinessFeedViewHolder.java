package com.touchmenotapps.marketplace.business.views;

import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;

import com.touchmenotapps.marketplace.R;
import com.touchmenotapps.marketplace.bo.FeedDao;
import com.touchmenotapps.marketplace.business.interfaces.BusinessFeedSelectedListener;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by arindamnath on 03/01/18.
 */

public class BusinessFeedViewHolder extends RecyclerView.ViewHolder {

    @BindView(R.id.feed_image)
    ImageView image;
    @BindView(R.id.feed_description)
    AppCompatTextView description;
    @BindView(R.id.feed_start_date)
    AppCompatTextView startsOn;
    @BindView(R.id.feed_end_date)
    AppCompatTextView endsOn;

    private BusinessFeedSelectedListener businessFeedSelectedListener;
    private FeedDao feedDao;

    public BusinessFeedViewHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
    }

    @OnClick(R.id.business_feed_base_container)
    public void onBusinessClicked() {
        if(businessFeedSelectedListener != null){
            businessFeedSelectedListener.onBusinessFeedSelected(feedDao);
        }
    }

    public void setBusinessFeedSelectedListener(BusinessFeedSelectedListener businessFeedSelectedListener) {
        this.businessFeedSelectedListener = businessFeedSelectedListener;
    }

    public void setData(FeedDao feedDao) {
        this.feedDao = feedDao;
        this.description.setText(feedDao.getCaption());
        this.startsOn.setText(feedDao.getStartDate());
        this.endsOn.setText(feedDao.getEndDate());
    }
}
