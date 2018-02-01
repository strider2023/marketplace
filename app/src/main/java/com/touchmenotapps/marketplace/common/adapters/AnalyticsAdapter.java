package com.touchmenotapps.marketplace.common.adapters;

import android.content.Context;
import android.support.v7.widget.AppCompatTextView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.touchmenotapps.marketplace.R;
import com.touchmenotapps.marketplace.bo.AnalyticsDao;
import com.touchmenotapps.marketplace.bo.RatingsDao;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by arindamnath on 27/01/18.
 */

public class AnalyticsAdapter extends BaseAdapter {

    private List<AnalyticsDao> ratingsDaos = new ArrayList<>();
    private Context context;
    private LayoutInflater layoutInflater;

    public AnalyticsAdapter(Context context) {
        this.context = context;
        layoutInflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public void setData(List<AnalyticsDao> data) {
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
            view = layoutInflater.inflate(R.layout.adapter_analytics, viewGroup, false);
            holder = new ViewHolder(view);
            view.setTag(holder);
        }

        if(ratingsDaos.get(i).getType().equalsIgnoreCase("adv")) {
            holder.type.setText("Address Viewed");
        } else if(ratingsDaos.get(i).getType().equalsIgnoreCase("phv")) {
            holder.type.setText("Phone Number Viewed");
        }
        holder.today.setText(String.valueOf(ratingsDaos.get(i).getToday()));
        holder.week.setText(String.valueOf(ratingsDaos.get(i).getLastWeek()));
        holder.month.setText(String.valueOf(ratingsDaos.get(i).getLastMonth()));
        holder.total.setText(String.valueOf(ratingsDaos.get(i).getTotal()));
        return view;
    }

    static class ViewHolder {
        @BindView(R.id.kpi_type)
        AppCompatTextView type;
        @BindView(R.id.kpi_today)
        AppCompatTextView today;
        @BindView(R.id.kpi_week)
        AppCompatTextView week;
        @BindView(R.id.kpi_month)
        AppCompatTextView month;
        @BindView(R.id.kpi_total)
        AppCompatTextView total;

        public ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
