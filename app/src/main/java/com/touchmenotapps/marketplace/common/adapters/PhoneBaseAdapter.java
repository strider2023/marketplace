package com.touchmenotapps.marketplace.common.adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.AppCompatTextView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.touchmenotapps.marketplace.R;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by i7 on 22-01-2018.
 */

public class PhoneBaseAdapter extends BaseAdapter {

    private List<String> activityTypeDAOList = new ArrayList<>();
    private Activity activity;
    private LayoutInflater layoutInflater;

    public PhoneBaseAdapter(Activity activity) {
        this.activity = activity;
        layoutInflater = (LayoutInflater) activity
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public void setData(List<String> data) {
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
            view = layoutInflater.inflate(R.layout.adapter_phone, viewGroup, false);
            holder = new ViewHolder(view);
            view.setTag(holder);
        }

        holder.phoneNumber.setText(activityTypeDAOList.get(i));
        holder.phone.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_DIAL, Uri.fromParts("tel",
                        activityTypeDAOList.get(i), null));
                activity.startActivity(intent);
            }
        });
        holder.msg.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

            }
        });
        return view;
    }

    static class ViewHolder {
        @BindView(R.id.adapter_phone_number)
        AppCompatTextView phoneNumber;
        @BindView(R.id.adapter_call_btn)
        ImageView phone;
        @BindView(R.id.adapter_msg_btn)
        ImageView msg;

        public ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
