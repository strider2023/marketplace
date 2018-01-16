package com.touchmenotapps.marketplace.common;

import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.touchmenotapps.marketplace.R;
import com.touchmenotapps.marketplace.bo.ViewPagerDao;
import com.touchmenotapps.marketplace.common.adapters.ViewPagerAdapter;
import com.touchmenotapps.marketplace.common.fragment.BusinessDetailFragment;
import com.touchmenotapps.marketplace.framework.persist.AppPreferences;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class BusinessDetailsActivity extends AppCompatActivity {

    public static final String SELECTED_BUSINESS_ID = "businessId";
    public static final String SELECTED_BUSINESS_NAME = "businessName";

    @BindView(R.id.business_name)
    AppCompatTextView name;
    @BindView(R.id.business_deatils_viewpager)
    ViewPager viewPager;
    @BindView(R.id.business_tabs)
    TabLayout tabLayout;
    @BindView(R.id.toolbar)
    Toolbar toolbar;

    private long businessId = -1l;
    private AppPreferences appPreferences;
    private ViewPagerAdapter viewPagerAdapter;
    private List<ViewPagerDao> fragments = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_business_details);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        appPreferences = new AppPreferences(this);
        viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());
        viewPager.setAdapter(viewPagerAdapter);
        tabLayout.setupWithViewPager(viewPager);

        if(getIntent().getLongExtra(SELECTED_BUSINESS_ID, -1l) != -1l) {
            businessId = getIntent().getLongExtra(SELECTED_BUSINESS_ID, -1l);
        }

        if(getIntent().getStringExtra(SELECTED_BUSINESS_NAME) != null) {
            name.setText(getIntent().getStringExtra(SELECTED_BUSINESS_NAME));
        }

        fragments.add(new ViewPagerDao("Details",
                BusinessDetailFragment.newInstance(businessId, name.getText().toString())));

        viewPagerAdapter.setFragments(fragments);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
