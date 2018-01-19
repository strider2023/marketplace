package com.touchmenotapps.marketplace.business;

import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.AppCompatSpinner;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.touchmenotapps.marketplace.R;
import com.touchmenotapps.marketplace.common.threads.AddBusinessTask;
import com.touchmenotapps.marketplace.common.threads.GetBusinessByIdTask;
import com.touchmenotapps.marketplace.common.threads.GetCategoriesTask;
import com.touchmenotapps.marketplace.common.threads.UpdateBusinessTask;
import com.touchmenotapps.marketplace.framework.enums.ServerEvents;
import com.touchmenotapps.marketplace.bo.BusinessAddressDao;
import com.touchmenotapps.marketplace.bo.BusinessDao;
import com.touchmenotapps.marketplace.bo.CategoryDao;
import com.touchmenotapps.marketplace.bo.HoursOfOperationDao;
import com.touchmenotapps.marketplace.framework.interfaces.ServerResponseListener;

import org.json.simple.JSONObject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnItemSelected;

import static com.touchmenotapps.marketplace.common.BusinessDetailsActivity.SELECTED_BUSINESS_ID;
import static com.touchmenotapps.marketplace.common.BusinessDetailsActivity.SELECTED_BUSINESS_NAME;

public class BusinessAddActivity extends AppCompatActivity implements ServerResponseListener {

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
    @BindView(R.id.business_categories_spinner)
    AppCompatSpinner categoriesSpinner;
    @BindView(R.id.business_sub_categories_spinner)
    AppCompatSpinner subCategoriesSpinner;

    private BusinessDao businessDao;
    private BusinessAddressDao businessAddressDao;
    private CategoryDao categoryDao;
    private HoursOfOperationDao hoursOfOperationDao;
    private ArrayAdapter<String> categoriesAdapter, subCategoriesAdapter;
    private String selectedCategory, selectedSubCategory;
    private CategoryDao allCategoryDao;
    private String[] categories, subCategories;
    private long businessId = -1l;
    private boolean isEdit = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_business);
        ButterKnife.bind(this);

        new GetCategoriesTask(1, this, this)
                .execute(new JSONObject[]{});

        //Hard coded
        hoursOfOperationDao = new HoursOfOperationDao();
        hoursOfOperationDao.addHoursMap("MON", "10AM-10PM");
        hoursOfOperationDao.addHoursMap("TUE", "10AM-10PM");
        hoursOfOperationDao.addHoursMap("WED", "10AM-10PM");
        hoursOfOperationDao.addHoursMap("THUR", "10AM-10PM");
        hoursOfOperationDao.addHoursMap("FRI", "10AM-10PM");
        hoursOfOperationDao.addHoursMap("SAT", "10AM-10PM");
        hoursOfOperationDao.addHoursMap("SUN", "10AM-10PM");

        categoryDao = new CategoryDao();

        categoriesAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, new String[]{});
        categoriesAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        categoriesSpinner.setAdapter(categoriesAdapter);
        subCategoriesAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, new String[]{});
        subCategoriesAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        subCategoriesSpinner.setAdapter(subCategoriesAdapter);

        businessAddressDao = new BusinessAddressDao();
        businessDao = new BusinessDao();
        businessDao.setHoursOfOperationDao(hoursOfOperationDao);

        if(getIntent().getLongExtra(SELECTED_BUSINESS_ID, -1l) != -1l) {
            isEdit = true;
            businessId = getIntent().getLongExtra(SELECTED_BUSINESS_ID, -1l);
            JSONObject id = new JSONObject();
            id.put("id", String.valueOf(businessId));
            new GetBusinessByIdTask(3, this, this)
                    .execute(new JSONObject[]{id});
        }

        if(getIntent().getStringExtra(SELECTED_BUSINESS_NAME) != null) {
            name.setText(getIntent().getStringExtra(SELECTED_BUSINESS_NAME));
            name.setEnabled(false);
        }
    }

    @OnItemSelected(R.id.business_categories_spinner)
    public void categorySelected(Spinner spinner, int position) {
        selectedCategory = categories[position];
        subCategories = allCategoryDao.getCategoriesMap().get(selectedCategory).toArray(
                new String[allCategoryDao.getCategoriesMap().get(selectedCategory).size()]);
        subCategoriesAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, subCategories);
        subCategoriesAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        subCategoriesSpinner.setAdapter(subCategoriesAdapter);
    }

    @OnItemSelected(R.id.business_sub_categories_spinner)
    public void subCategorySelected(Spinner spinner, int position) {
        selectedSubCategory = subCategories[position];
    }

    @OnClick(R.id.add_business_btn)
    public void addBusiness() {
        if(name.getEditableText().toString().trim().length() > 0
                && phone.getEditableText().toString().trim().length() > 0) {
            categoryDao.addCategory(selectedCategory,selectedSubCategory);
            businessDao.setCategoryDao(categoryDao);

            businessDao.setName(name.getEditableText().toString().trim());
            businessDao.addPhoneNumber(phone.getEditableText().toString().trim());
            businessDao.setWebsite(website.getEditableText().toString().trim());

            businessAddressDao.setAddress(line1.getEditableText().toString().trim());
            businessAddressDao.setCity(city.getEditableText().toString().trim());
            businessAddressDao.setState(state.getEditableText().toString().trim());
            businessAddressDao.setZip(zip.getEditableText().toString().trim());
            businessDao.setBusinessAddressDao(businessAddressDao);

            if(isEdit) {
                //If edit then update
                JSONObject id = new JSONObject();
                id.put("id", String.valueOf(businessId));
                new UpdateBusinessTask(4, this, this)
                        .execute(new JSONObject[]{id, businessDao.toJSON()});
            } else {
                //Add new business
                new AddBusinessTask(2, this, this)
                        .execute(new JSONObject[]{businessDao.toJSON()});
            }
        } else {
            Snackbar.make(name, "Please update the mandatory(*) fields", Snackbar.LENGTH_SHORT).show();
        }
    }

    @OnClick(R.id.close_add_business_btn)
    public void onClose() {
        finish();
    }

    @Override
    public void onSuccess(int threadId, Object object) {
        switch (threadId) {
            case 1:
                //Set categories and sub-categories
                allCategoryDao = (CategoryDao) object;
                categories = allCategoryDao.getCategoriesMap().keySet().toArray(
                        new String[allCategoryDao.getCategoriesMap().keySet().size()]);
                categoriesAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, categories);
                categoriesAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                categoriesSpinner.setAdapter(categoriesAdapter);

                subCategories = allCategoryDao.getCategoriesMap().get(categories[0]).toArray(
                        new String[allCategoryDao.getCategoriesMap().get(categories[0]).size()]);
                subCategoriesAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, subCategories);
                subCategoriesAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                subCategoriesSpinner.setAdapter(subCategoriesAdapter);
                break;
            case 2:
                //New business added. Close activity.
                finish();
                break;
            case 3:
                //Map Business data
                businessDao = (BusinessDao) object;
                for(String number : businessDao.getPhoneNumber()) {
                    phone.setText(number);
                }
                website.setText(businessDao.getWebsite());
                line1.setText(businessDao.getBusinessAddressDao().getAddress());
                city.setText(businessDao.getBusinessAddressDao().getCity());
                state.setText(businessDao.getBusinessAddressDao().getState());
                zip.setText(businessDao.getBusinessAddressDao().getZip());
                break;
            case 4:
                //Business updated. Close activity.
                finish();
                break;
        }
    }

    @Override
    public void onFaliure(ServerEvents serverEvents, Object object) {
        Snackbar.make(name, object.toString(), Snackbar.LENGTH_LONG).show();
    }
}
