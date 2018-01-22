package com.touchmenotapps.marketplace.common.adapters;

import android.content.Context;
import android.support.v7.widget.AppCompatTextView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.touchmenotapps.marketplace.R;
import com.touchmenotapps.marketplace.bo.DailyTimeInfoDao;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by i7 on 22-01-2018.
 */

public class OperationTimeBaseAdapter extends BaseAdapter {

    private List<DailyTimeInfoDao> activityTypeDAOList = new ArrayList<>();
    private Context context;
    private LayoutInflater layoutInflater;

    public OperationTimeBaseAdapter(Context context) {
        this.context = context;
        layoutInflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public void setData(List<DailyTimeInfoDao> data) {
        this.activityTypeDAOList = data;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return activityTypeDAOList.size();
    }

    @Override
    public Object getItem(int i) {
        return activityTypeDAOList.get(i);
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
            view = layoutInflater.inflate(R.layout.adapter_hours_of_operation, viewGroup, false);
            holder = new ViewHolder(view);
            view.setTag(holder);
        }

        holder.day.setText(Html.fromHtml(activityTypeDAOList.get(i).getDay()));
        holder.time.setText(Html.fromHtml(activityTypeDAOList.get(i).getTime()));
        return view;
    }

    static class ViewHolder {
        @BindView(R.id.adapter_day)
        AppCompatTextView day;
        @BindView(R.id.adapter_time)
        AppCompatTextView time;

        public ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
