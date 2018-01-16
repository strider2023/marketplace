package com.touchmenotapps.marketplace.bo;

import android.support.v4.app.Fragment;

/**
 * Created by arindamnath on 16/01/18.
 */

public class ViewPagerDao {

    private String fragmentName;
    private Fragment fragment;

    public ViewPagerDao(String fragmentName, Fragment fragment) {
        this.fragmentName = fragmentName;
        this.fragment = fragment;
    }

    public String getFragmentName() {
        return fragmentName;
    }

    public void setFragmentName(String fragmentName) {
        this.fragmentName = fragmentName;
    }

    public Fragment getFragment() {
        return fragment;
    }

    public void setFragment(Fragment fragment) {
        this.fragment = fragment;
    }
}
