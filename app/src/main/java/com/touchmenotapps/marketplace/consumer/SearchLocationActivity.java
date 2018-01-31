package com.touchmenotapps.marketplace.consumer;

import android.content.Intent;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatEditText;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.touchmenotapps.marketplace.R;
import com.touchmenotapps.marketplace.bo.LocationDao;
import com.touchmenotapps.marketplace.common.adapters.LocationAdapter;
import com.touchmenotapps.marketplace.framework.enums.LoaderID;
import com.touchmenotapps.marketplace.threads.loaders.LocationLoaderTask;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SearchLocationActivity extends AppCompatActivity implements
        LoaderManager.LoaderCallbacks<List<LocationDao>>{

    @BindView(R.id.location_search_view)
    AppCompatEditText locationSearch;
    @BindView(R.id.location_list)
    ListView locationList;

    private Bundle queryData;
    private List<LocationDao> locations = new ArrayList<>();
    private LocationAdapter geoAdapter;
    private LocationDao selectedLocation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_location);
        ButterKnife.bind(this);

        geoAdapter = new LocationAdapter(this);
        locationList.setAdapter(geoAdapter);

        locationSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(s.toString().length() > 3) {
                    queryData = new Bundle();
                    queryData.putString("city", s.toString());
                    getSupportLoaderManager()
                            .initLoader(LoaderID.FETCH_LOCATION_SEARCH.getValue(), queryData, SearchLocationActivity.this).forceLoad();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        locationList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                selectedLocation = locations.get(position);
                locationSearch.setText(selectedLocation.getCity());
            }
        });
    }

    @OnClick(R.id.search_close_btn)
    public void onSearchClose() {
        finish();
    }

    @OnClick(R.id.search_submit)
    public void onSearchSubmit() {
        Intent data = new Intent();
        data.putExtra("selectedLocation", selectedLocation);
        setResult(RESULT_OK, data);
        finish();
    }

    @Override
    public Loader<List<LocationDao>> onCreateLoader(int id, Bundle args) {
        return new LocationLoaderTask(this, args);
    }

    @Override
    public void onLoadFinished(Loader<List<LocationDao>> loader, List<LocationDao> data) {
        if(data.size() > 0) {
            locations = data;
            geoAdapter.setData(data);
        }
    }

    @Override
    public void onLoaderReset(Loader<List<LocationDao>> loader) {

    }
}
