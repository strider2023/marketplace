package com.touchmenotapps.marketplace.home.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.touchmenotapps.marketplace.R;

import butterknife.ButterKnife;

/**
 * Created by i7 on 18-10-2017.
 */

public class BookmarksFragment extends Fragment {

    private View mViewHolder;

    public static BookmarksFragment newInstance() {
        BookmarksFragment fragment = new BookmarksFragment();
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mViewHolder = inflater.inflate(R.layout.fragment_bookmarks, container, false);
        ButterKnife.bind(this, mViewHolder);

        return mViewHolder;
    }
}