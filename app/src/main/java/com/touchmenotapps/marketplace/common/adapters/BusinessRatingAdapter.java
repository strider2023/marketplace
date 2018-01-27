package com.touchmenotapps.marketplace.common.adapters;

import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.AppCompatTextView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.touchmenotapps.marketplace.R;
import com.touchmenotapps.marketplace.bo.RatingsDao;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by arindamnath on 27/01/18.
 */

public class BusinessRatingAdapter extends BaseAdapter {

    private List<RatingsDao> ratingsDaos = new ArrayList<>();
    private Context context;
    private LayoutInflater layoutInflater;

    public BusinessRatingAdapter(Context context) {
        this.context = context;
        layoutInflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public void setData(List<RatingsDao> data) {
        this.ratingsDaos = data;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return ratingsDaos.size();
    }

    @Override
    public Object getItem(int i) {
        return ratingsDaos.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(final int i, View view, ViewGroup viewGroup) {
        ViewHolder holder;
        if (view != null) {
            holder = (ViewHolder) view.getTag();
        } else {
            view = layoutInflater.inflate(R.layout.adapter_rating, viewGroup, false);
            holder = new ViewHolder(view);
            view.setTag(holder);
        }

        holder.ratingType.setText(ratingsDaos.get(i).getType().replace("_", " "));
        holder.good.setText(String.valueOf(ratingsDaos.get(i).getUp()));
        holder.neutral.setText(String.valueOf(ratingsDaos.get(i).getNeutral()));
        holder.bad.setText(String.valueOf(ratingsDaos.get(i).getDown()));
        return view;
    }

    static class ViewHolder {
        @BindView(R.id.rating_type)
        AppCompatTextView ratingType;
        @BindView(R.id.rating_good)
        AppCompatTextView good;
        @BindView(R.id.rating_neutral)
        AppCompatTextView neutral;
        @BindView(R.id.rating_bad)
        AppCompatTextView bad;

        public ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
