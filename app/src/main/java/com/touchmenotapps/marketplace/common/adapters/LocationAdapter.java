package com.touchmenotapps.marketplace.common.adapters;

import android.content.Context;
import android.support.v7.widget.AppCompatTextView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;

import com.touchmenotapps.marketplace.R;
import com.touchmenotapps.marketplace.bo.LocationDao;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by arindamnath on 26/01/18.
 */

public class LocationAdapter extends BaseAdapter implements Filterable {

    private List<LocationDao> categoriesList = new ArrayList<>();
    private List<LocationDao> mStringFilterList = new ArrayList<>();

    private LayoutInflater layoutInflater;
    private ValueFilter valueFilter;

    public LocationAdapter(Context context) {
        layoutInflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public void setData(List<LocationDao> data) {
        this.categoriesList = data;
        mStringFilterList = data;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return categoriesList.size();
    }

    @Override
    public Object getItem(int i) {
        return categoriesList.get(i).getCity();
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(final int i, View view, ViewGroup viewGroup) {
        CategoriesAdapter.ViewHolder holder;
        if (view != null) {
            holder = (CategoriesAdapter.ViewHolder) view.getTag();
        } else {
            view = layoutInflater.inflate(R.layout.adapter_category, viewGroup, false);
            holder = new CategoriesAdapter.ViewHolder(view);
            view.setTag(holder);
        }

        holder.name.setText(categoriesList.get(i).getCity());
        return view;
    }

    @Override
    public Filter getFilter() {
        if (valueFilter == null) {
            valueFilter = new ValueFilter();
        }
        return valueFilter;
    }

    private class ValueFilter extends Filter {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            FilterResults results = new FilterResults();
            if (constraint != null && constraint.length() > 0) {
                ArrayList<LocationDao> filterList = new ArrayList<>();
                for (int i = 0; i < mStringFilterList.size(); i++) {
                    if ((mStringFilterList.get(i).getCity().toUpperCase())
                            .contains(constraint.toString().toUpperCase())) {
                        LocationDao country = new LocationDao();
                        country.setCity(mStringFilterList.get(i).getCity());
                        country.setCountry(mStringFilterList.get(i).getCountry());
                        country.setLatitude(mStringFilterList.get(i).getLatitude());
                        country.setLongitude(mStringFilterList.get(i).getLongitude());
                        filterList.add(country);
                    }
                }
                results.count = filterList.size();
                results.values = filterList;
            } else {
                results.count = mStringFilterList.size();
                results.values = mStringFilterList;
            }
            return results;

        }

        @Override
        protected void publishResults(CharSequence constraint,
                                      FilterResults results) {
            categoriesList = (List<LocationDao>) results.values;
            notifyDataSetChanged();
        }
    }

    static class ViewHolder {
        @BindView(R.id.category_name)
        AppCompatTextView name;

        public ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
