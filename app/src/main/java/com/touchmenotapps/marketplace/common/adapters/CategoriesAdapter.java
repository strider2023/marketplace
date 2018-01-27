package com.touchmenotapps.marketplace.common.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatTextView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import com.touchmenotapps.marketplace.R;
import com.touchmenotapps.marketplace.bo.CategoryDao;
import com.touchmenotapps.marketplace.bo.RatingsDao;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by arindamnath on 26/01/18.
 */

public class CategoriesAdapter extends BaseAdapter implements Filterable {

    private List<CategoryDao> categoriesList = new ArrayList<>();
    private List<CategoryDao> mStringFilterList;

    private Context context;
    private LayoutInflater layoutInflater;
    private ValueFilter valueFilter;

    public CategoriesAdapter(Context context) {
        this.context = context;
        layoutInflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public void setData(List<CategoryDao> data) {
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
        return categoriesList.get(i).getDescription();
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
            view = layoutInflater.inflate(R.layout.adapter_category, viewGroup, false);
            holder = new ViewHolder(view);
            view.setTag(holder);
        }

        holder.name.setText(categoriesList.get(i).getDescription());
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
                ArrayList<CategoryDao> filterList = new ArrayList<CategoryDao>();
                for (int i = 0; i < mStringFilterList.size(); i++) {
                    if ((mStringFilterList.get(i).getDescription().toUpperCase())
                            .contains(constraint.toString().toUpperCase())) {
                        CategoryDao country = new CategoryDao();
                        country.setEnumText(mStringFilterList.get(i).getEnumText());
                        country.setDescription(mStringFilterList.get(i).getDescription());
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
            categoriesList = (List<CategoryDao>) results.values;
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
