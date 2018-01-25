package com.touchmenotapps.marketplace.common.views;

import android.graphics.Typeface;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;

import com.touchmenotapps.marketplace.R;
import com.touchmenotapps.marketplace.common.interfaces.BusinessSelectedListener;
import com.touchmenotapps.marketplace.bo.BusinessDao;

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
    @BindView(R.id.business_location)
    AppCompatTextView location;
    @BindView(R.id.business_rating)
    AppCompatTextView rating;
    @BindView(R.id.business_time)
    AppCompatTextView time;

    private BusinessDao businessDao;
    private BusinessSelectedListener businessSelectedListener;

    public BusinessViewHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
        Typeface myTypeface = Typeface.createFromAsset(itemView.getContext().getAssets(), "fonts/font.ttf");
        name.setTypeface(myTypeface);
    }

    @OnClick(R.id.business_base_container)
    public void onBusinessClicked() {
        if(businessSelectedListener != null){
            businessSelectedListener.onBusinessSelected(businessDao);
        }
    }

    public void setBusinessSelectedListener(BusinessSelectedListener businessSelectedListener) {
        this.businessSelectedListener = businessSelectedListener;
    }

    public void setData(BusinessDao businessDao) {
        this.businessDao = businessDao;
        this.name.setText(businessDao.getName());
        for(String categoryName : businessDao.getCategoryDao().getCategoriesMap().keySet()) {
            this.category.setText(categoryName);
        }
        this.location.setText(businessDao.getBusinessAddressDao().getAddress() + ", " +
                businessDao.getBusinessAddressDao().getCity() + ", " +
                businessDao.getBusinessAddressDao().getState() + " - " +
                businessDao.getBusinessAddressDao().getZip());
        this.time.setText(businessDao.getHoursOfOperationDao().getCurrentDayStatus());
        this.rating.setText(String.valueOf(businessDao.getSingleScoreRating()));
    }
}
