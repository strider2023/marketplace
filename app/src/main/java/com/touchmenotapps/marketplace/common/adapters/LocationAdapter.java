package com.touchmenotapps.marketplace.common.adapters;

import android.content.Context;
import android.support.v7.widget.AppCompatTextView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.touchmenotapps.marketplace.R;
import com.touchmenotapps.marketplace.bo.LocationDao;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by arindamnath on 26/01/18.
 */

public class LocationAdapter extends BaseAdapter {

    private List<LocationDao> locationsList = new ArrayList<>();
    private LayoutInflater layoutInflater;

    public LocationAdapter(Context context) {
        layoutInflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public void setData(List<LocationDao> data) {
        this.locationsList.clear();
        locationsList.addAll(data);
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return locationsList.size();
    }

    @Override
    public Object getItem(int i) {
        return locationsList.get(i);
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
            view = layoutInflater.inflate(R.layout.adapter_location, viewGroup, false);
            holder = new ViewHolder(view);
            view.setTag(holder);
        }
        holder.location.setText(locationsList.get(i).getCity());
        holder.state.setText(locationsList.get(i).getState() + ", " + locationsList.get(i).getCountry());
        return view;
    }

    static class ViewHolder {
        @BindView(R.id.location_name)
        AppCompatTextView location;
        @BindView(R.id.state_country_name)
        AppCompatTextView state;

        public ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
