package com.touchmenotapps.marketplace.consumer.views;

import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;

import com.touchmenotapps.marketplace.R;
import com.touchmenotapps.marketplace.consumer.dao.BookmarksDAO;
import com.touchmenotapps.marketplace.consumer.interfaces.BookmarkSelectionListener;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

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

    private BookmarksDAO bookmarksDAO;
    private BookmarkSelectionListener bookmarkSelectionListener;

    public BookmarksViewHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
    }

    @OnClick(R.id.bookmark_base_container)
    public void onBookmarkClicked() {
        if(bookmarkSelectionListener != null){
            bookmarkSelectionListener.onBookmarkSelected(bookmarksDAO);
        }
    }

    public void setBookmarkSelectionListener(BookmarkSelectionListener bookmarkSelectionListener) {
        this.bookmarkSelectionListener = bookmarkSelectionListener;
    }

    public void setData(BookmarksDAO bookmarksDAO) {
        this.bookmarksDAO = bookmarksDAO;
    }
}
