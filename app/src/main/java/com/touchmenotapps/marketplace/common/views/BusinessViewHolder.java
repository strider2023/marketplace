package com.touchmenotapps.marketplace.common.views;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.net.Uri;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.touchmenotapps.marketplace.R;
import com.touchmenotapps.marketplace.bo.CategoryDao;
import com.touchmenotapps.marketplace.common.interfaces.BusinessSelectedListener;
import com.touchmenotapps.marketplace.bo.BusinessDao;
import com.touchmenotapps.marketplace.framework.enums.UserType;
import com.touchmenotapps.marketplace.framework.persist.AppPreferences;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by arindamnath on 30/12/17.
 */

public class BusinessViewHolder extends RecyclerView.ViewHolder {

    @BindView(R.id.business_image)
    ImageView image;
    @BindView(R.id.business_name)
    AppCompatTextView name;
    @BindView(R.id.business_category)
    AppCompatTextView category;
    @BindView(R.id.business_rating)
    AppCompatTextView rating;
    @BindView(R.id.business_time)
    AppCompatTextView time;
    @BindView(R.id.image_count)
    AppCompatTextView image_count;
    @BindView(R.id.feed_count)
    AppCompatTextView feed_count;
    @BindView(R.id.business_call_button)
    FloatingActionButton callBtn;
    @BindView(R.id.business_location_button)
    FloatingActionButton locationBtn;
    @BindView(R.id.business_fav)
    ImageView fav;

    private BusinessDao businessDao;
    private BusinessSelectedListener businessSelectedListener;
    private AppPreferences appPreferences;
    private Context context;

    public BusinessViewHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
        context = itemView.getContext();
        appPreferences = new AppPreferences(context);
        if(appPreferences.getUserType() == UserType.BUSINESS) {
            time.setVisibility(View.GONE);
            category.setVisibility(View.GONE);
            callBtn.setVisibility(View.GONE);
            locationBtn.setVisibility(View.GONE);
        }
        Typeface myTypeface = Typeface.createFromAsset(context.getAssets(), "fonts/font.ttf");
        //name.setTypeface(myTypeface);
    }

    @OnClick(R.id.business_base_container)
    public void onBusinessClicked() {
        if(businessSelectedListener != null){
            businessSelectedListener.onBusinessSelected(businessDao);
        }
    }

    @OnClick(R.id.business_call_button)
    public void onCallSelected() {
        Intent intent = new Intent(Intent.ACTION_DIAL, Uri.fromParts("tel",
                businessDao.getPhoneNumber().iterator().next(), null));
        context.startActivity(intent);
    }

    @OnClick(R.id.business_location_button)
    public void onLocationSelected() {
        String url = "http://maps.google.com/maps?daddr=" +
                businessDao.getBusinessAddressDao().getAddress() + "," +
                businessDao.getBusinessAddressDao().getCity() + "," +
                businessDao.getBusinessAddressDao().getState() + "," +
                businessDao.getBusinessAddressDao().getZip();
        Intent intent = new Intent(android.content.Intent.ACTION_VIEW, Uri.parse(url));
        context.startActivity(intent);
    }

    public void setBusinessSelectedListener(BusinessSelectedListener businessSelectedListener) {
        this.businessSelectedListener = businessSelectedListener;
    }

    public void setData(BusinessDao businessDao) {
        this.businessDao = businessDao;
        this.name.setText(businessDao.getName());
        this.category.setText(businessDao.getCategory());
        if(businessDao.isBookmarked()) {
            fav.setVisibility(View.VISIBLE);
        }
        if(appPreferences.getUserType() == UserType.CONSUMER) {
            /*this.location.setText(businessDao.getBusinessAddressDao().getAddress() + ", " +
                    businessDao.getBusinessAddressDao().getCity() + ", " +
                    businessDao.getBusinessAddressDao().getState() + " - " +
                    businessDao.getBusinessAddressDao().getZip());*/
            this.time.setText(businessDao.getHoursOfOperationDao().getCurrentDayStatus());
        }
        this.rating.setText(String.valueOf(businessDao.getSingleScoreRating()));
        this.image_count.setText(String.valueOf(businessDao.getBusinessPhotosCount()));
        this.feed_count.setText(String.valueOf(businessDao.getBusinessFeedCount()));
        Glide.with(context)
                .load(businessDao.getBusinessProfileImage())
                .error(R.drawable.ic_shop)
                .placeholder(R.drawable.ic_shop)
                .centerCrop()
                .into(image);
    }
}
