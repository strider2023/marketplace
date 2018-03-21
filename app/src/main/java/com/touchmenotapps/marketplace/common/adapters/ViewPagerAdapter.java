package com.touchmenotapps.marketplace.common.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.touchmenotapps.marketplace.bo.ViewPagerDao;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by arindamnath on 16/01/18.
 */

public class ViewPagerAdapter extends FragmentPagerAdapter {

    private List<ViewPagerDao> fragments = new ArrayList<>();

    public ViewPagerAdapter(FragmentManager manager) {
        super(manager);
    }

    public void setFragments(List<ViewPagerDao> data) {
        fragments.clear();
        fragments.addAll(data);
        notifyDataSetChanged();
    }

    @Override
    public int getItemPosition(Object object) {
        return super.getItemPosition(object);
    }

    @Override
    public Fragment getItem(int position) {
        return fragments.get(position).getFragment();
    }

    @Override
    public int getCount() {
        return fragments.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return fragments.get(position).getFragmentName();
    }
}
