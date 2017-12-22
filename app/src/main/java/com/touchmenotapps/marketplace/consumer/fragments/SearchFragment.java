package com.touchmenotapps.marketplace.consumer.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.AppCompatEditText;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.touchmenotapps.marketplace.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by i7 on 18-10-2017.
 */

public class SearchFragment extends Fragment {

    private View mViewHolder;

    @BindView(R.id.search_edittext)
    AppCompatEditText searchText;
    @BindView(R.id.search_clear_input)
    ImageView clearInput;

    public static SearchFragment newInstance() {
        SearchFragment fragment = new SearchFragment();
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mViewHolder = inflater.inflate(R.layout.fragment_search, container, false);
        ButterKnife.bind(this, mViewHolder);

        searchText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) { }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(charSequence.toString().length() > 0) {
                    clearInput.setVisibility(View.VISIBLE);
                } else {
                    clearInput.setVisibility(View.GONE);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) { }
        });
        return mViewHolder;
    }

    @OnClick(R.id.search_clear_input)
    public void onClearSearchField() {
        searchText.setText("");
    }
}
