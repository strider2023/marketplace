package com.touchmenotapps.marketplace.business;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatEditText;
import android.util.Log;
import android.view.MenuItem;

import com.touchmenotapps.marketplace.R;
import com.touchmenotapps.marketplace.business.threads.AddBusinessTask;
import com.touchmenotapps.marketplace.common.GetCategoriesTask;
import com.touchmenotapps.marketplace.common.constants.AppConstants;
import com.touchmenotapps.marketplace.common.enums.ServerEvents;
import com.touchmenotapps.marketplace.dao.BusinessAddressDao;
import com.touchmenotapps.marketplace.dao.BusinessDao;
import com.touchmenotapps.marketplace.dao.CategoryDao;
import com.touchmenotapps.marketplace.dao.HoursOfOperationDao;
import com.touchmenotapps.marketplace.framework.interfaces.ServerResponseListener;

import org.json.simple.JSONObject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class AddBusinessActivity extends AppCompatActivity implements ServerResponseListener {

    @BindView(R.id.business_name)
    AppCompatEditText name;
    @BindView(R.id.business_website)
    AppCompatEditText website;
    @BindView(R.id.business_phone)
    AppCompatEditText phone;
    @BindView(R.id.business_address_line1)
    AppCompatEditText line1;
    @BindView(R.id.business_address_city)
    AppCompatEditText city;
    @BindView(R.id.business_address_state)
    AppCompatEditText state;
    @BindView(R.id.business_address_zip)
    AppCompatEditText zip;

    private BusinessDao businessDao;
    private BusinessAddressDao businessAddressDao;
    private CategoryDao categoryDao;
    private HoursOfOperationDao hoursOfOperationDao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_business);
        ButterKnife.bind(this);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        new GetCategoriesTask(1, this, this)
                .execute(new JSONObject[]{});

        //Hard coded
        hoursOfOperationDao = new HoursOfOperationDao(this);
        hoursOfOperationDao.addHoursMap("MON", "10AM-10PM");
        hoursOfOperationDao.addHoursMap("TUE", "10AM-10PM");
        hoursOfOperationDao.addHoursMap("WED", "10AM-10PM");
        hoursOfOperationDao.addHoursMap("THUR", "10AM-10PM");
        hoursOfOperationDao.addHoursMap("FRI", "10AM-10PM");
        hoursOfOperationDao.addHoursMap("SAT", "10AM-10PM");
        hoursOfOperationDao.addHoursMap("SUN", "10AM-10PM");

        categoryDao = new CategoryDao(this);
        categoryDao.addCategory("APPARELS","MENS_CLOTHING");

        businessAddressDao = new BusinessAddressDao(this);
        businessDao = new BusinessDao(this);
        businessDao.setCategoryDao(categoryDao);
        businessDao.setHoursOfOperationDao(hoursOfOperationDao);
    }

    @OnClick(R.id.add_business_btn)
    public void addBusiness() {
        businessDao.setName(name.getEditableText().toString().trim());
        businessDao.addPhoneNumber(phone.getEditableText().toString().trim());
        businessDao.setWebsite(website.getEditableText().toString().trim());

        businessAddressDao.setAddress(line1.getEditableText().toString().trim());
        businessAddressDao.setCity(city.getEditableText().toString().trim());
        businessAddressDao.setState(state.getEditableText().toString().trim());
        businessAddressDao.setZip(zip.getEditableText().toString().trim());
        businessDao.setBusinessAddressDao(businessAddressDao);

        new AddBusinessTask(2, this, this)
                .execute(new JSONObject[]{businessDao.toJSON()});
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
        switch (threadId) {
            case 1:
                CategoryDao allCategoryDao = (CategoryDao) object;
                Log.i(AppConstants.APP_TAG, String.valueOf(allCategoryDao.getCategoriesMap().size()));
                break;
            case 2:
                finish();
                break;
        }
    }

    @Override
    public void onFaliure(ServerEvents serverEvents, Object object) {

    }
}
