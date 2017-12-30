package com.touchmenotapps.marketplace.business;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;

import com.touchmenotapps.marketplace.R;
import com.touchmenotapps.marketplace.common.GetCategoriesTask;
import com.touchmenotapps.marketplace.common.constants.AppConstants;
import com.touchmenotapps.marketplace.common.enums.ServerEvents;
import com.touchmenotapps.marketplace.dao.CategoryDao;
import com.touchmenotapps.marketplace.framework.interfaces.ServerResponseListener;

import org.json.simple.JSONObject;

import butterknife.ButterKnife;

public class AddBusinessActivity extends AppCompatActivity implements ServerResponseListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_business);
        ButterKnife.bind(this);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        new GetCategoriesTask(1, this, this)
                .execute(new JSONObject[]{});
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onSuccess(int threadId, Object object) {
        CategoryDao categoryDao = (CategoryDao) object;
        Log.i(AppConstants.APP_TAG, String.valueOf(categoryDao.getCategoriesMap().size()));
    }

    @Override
    public void onFaliure(ServerEvents serverEvents, Object object) {

    }
}
