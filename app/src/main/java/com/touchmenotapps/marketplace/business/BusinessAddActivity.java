package com.touchmenotapps.marketplace.business;

import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.AppCompatSpinner;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.touchmenotapps.marketplace.R;
import com.touchmenotapps.marketplace.business.threads.AddBusinessTask;
import com.touchmenotapps.marketplace.common.GetCategoriesTask;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_business);
        ButterKnife.bind(this);

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

        categoriesAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, new String[]{});
        categoriesAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        categoriesSpinner.setAdapter(categoriesAdapter);
        subCategoriesAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, new String[]{});
        subCategoriesAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        subCategoriesSpinner.setAdapter(subCategoriesAdapter);

        businessAddressDao = new BusinessAddressDao(this);
        businessDao = new BusinessDao(this);
        businessDao.setHoursOfOperationDao(hoursOfOperationDao);
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

            new AddBusinessTask(2, this, this)
                    .execute(new JSONObject[]{businessDao.toJSON()});
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
        }
    }

    @Override
    public void onFaliure(ServerEvents serverEvents, Object object) {
        Snackbar.make(name, object.toString(), Snackbar.LENGTH_LONG).show();
    }
}
