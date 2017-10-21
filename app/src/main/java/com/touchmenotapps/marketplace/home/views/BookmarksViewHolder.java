package com.touchmenotapps.marketplace.home.views;

import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;

import com.touchmenotapps.marketplace.R;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by i7 on 21-10-2017.
 */

public class BookmarksViewHolder extends RecyclerView.ViewHolder {

    @BindView(R.id.bookmark_shop_image)
    ImageView shopImage;
    @BindView(R.id.bookmark_shop_name)
    AppCompatTextView shopName;
    @BindView(R.id.bookmark_shop_category)
    AppCompatTextView shopCategory;
    @BindView(R.id.bookmark_shop_offers)
    AppCompatTextView shopOffers;
    @BindView(R.id.bookmark_shop_rating)
    AppCompatTextView shopRating;
    @BindView(R.id.bookmark_shop_distance)
    AppCompatTextView shopDistance;

    public BookmarksViewHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
    }

    public void setData() {

    }
}
