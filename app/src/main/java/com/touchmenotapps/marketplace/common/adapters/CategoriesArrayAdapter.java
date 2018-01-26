package com.touchmenotapps.marketplace.common.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.touchmenotapps.marketplace.R;
import com.touchmenotapps.marketplace.bo.CategoryDao;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by arindamnath on 26/01/18.
 */

public class CategoriesArrayAdapter extends ArrayAdapter<CategoryDao> {

    private Context mContext;
    private List<CategoryDao> categoriesList = new ArrayList<>();

    public CategoriesArrayAdapter(@NonNull Context context, List<CategoryDao> list) {
        super(context, 0 , list);
        mContext = context;
        categoriesList = list;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View listItem = convertView;
        if(listItem == null)
            listItem = LayoutInflater.from(mContext).inflate(R.layout.adapter_category, parent,false);
        CategoryDao currentMovie = categoriesList.get(position);
        TextView name = (TextView) listItem.findViewById(R.id.category_name);
        name.setText(currentMovie.getDescription());
        return listItem;
    }
}
