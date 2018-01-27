package com.touchmenotapps.marketplace.business;

import android.app.TimePickerDialog;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatCheckBox;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.AppCompatSpinner;
import android.support.v7.widget.AppCompatTextView;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TimePicker;

import com.touchmenotapps.marketplace.R;
import com.touchmenotapps.marketplace.bo.CategoryListDao;
import com.touchmenotapps.marketplace.bo.DailyTimeInfoDao;
import com.touchmenotapps.marketplace.common.adapters.CategoriesAdapter;
import com.touchmenotapps.marketplace.framework.enums.RequestType;
import com.touchmenotapps.marketplace.threads.asynctasks.BusinessTask;
import com.touchmenotapps.marketplace.threads.asynctasks.GetCategoriesTask;
import com.touchmenotapps.marketplace.framework.enums.ServerEvents;
import com.touchmenotapps.marketplace.bo.BusinessAddressDao;
import com.touchmenotapps.marketplace.bo.BusinessDao;
import com.touchmenotapps.marketplace.bo.CategoryDao;
import com.touchmenotapps.marketplace.bo.HoursOfOperationDao;
import com.touchmenotapps.marketplace.framework.interfaces.ServerResponseListener;

import org.json.simple.JSONObject;

import java.sql.Time;
import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnItemSelected;

import static com.touchmenotapps.marketplace.framework.constants.AppConstants.BUSINESS_ID_TAG;
import static com.touchmenotapps.marketplace.framework.constants.AppConstants.BUSINESS_NAME_TAG;

public class BusinessAddActivity extends AppCompatActivity implements ServerResponseListener {

    @BindView(R.id.business_name)
    AppCompatEditText name;
    @BindView(R.id.business_website)
    AppCompatEditText website;
    @BindView(R.id.business_phone_1)
    AppCompatEditText phone;
    @BindView(R.id.business_phone_2)
    AppCompatEditText phone2;
    @BindView(R.id.business_phone_3)
    AppCompatEditText phone3;
    @BindView(R.id.business_address_line1)
    AppCompatEditText line1;
    @BindView(R.id.business_address_landmark)
    AppCompatEditText landmark;
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
    @BindView(R.id.business_start_time)
    AppCompatTextView startTime;
    @BindView(R.id.business_close_time)
    AppCompatTextView closeTime;
    @BindView(R.id.add_day_sunday)
    AppCompatCheckBox sunday;
    @BindView(R.id.add_day_monday)
    AppCompatCheckBox monday;
    @BindView(R.id.add_day_tuesday)
    AppCompatCheckBox tuesday;
    @BindView(R.id.add_day_wednesday)
    AppCompatCheckBox wednesday;
    @BindView(R.id.add_day_thursday)
    AppCompatCheckBox thursday;
    @BindView(R.id.add_day_friday)
    AppCompatCheckBox friday;
    @BindView(R.id.add_day_saturday)
    AppCompatCheckBox saturday;

    private BusinessDao businessDao;
    private BusinessAddressDao businessAddressDao;
    private CategoryListDao categoryDao;
    private HoursOfOperationDao hoursOfOperationDao;
    private CategoriesAdapter categoriesAdapter, subCategoriesAdapter;
    private String selectedCategory, selectedSubCategory;
    private CategoryListDao allCategoryDao;
    private List<CategoryDao> categories, subCategories;
    private long businessId = -1l;
    private boolean isEdit = false;
    private BusinessTask businessTask;
    private int currentPhoneCount = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_business);
        ButterKnife.bind(this);

        new GetCategoriesTask(1, this, this)
                .execute(new JSONObject[]{});

        hoursOfOperationDao = new HoursOfOperationDao();
        categoryDao = new CategoryListDao();

        businessAddressDao = new BusinessAddressDao();
        businessDao = new BusinessDao();
        categoriesAdapter = new CategoriesAdapter(this);
        subCategoriesAdapter = new CategoriesAdapter(this);

        categoriesSpinner.setAdapter(categoriesAdapter);
        subCategoriesSpinner.setAdapter(subCategoriesAdapter);

        if(getIntent().getLongExtra(BUSINESS_ID_TAG, -1l) != -1l) {
            isEdit = true;
            businessId = getIntent().getLongExtra(BUSINESS_ID_TAG, -1l);
            businessTask = new BusinessTask(3, this, this);
            businessTask.setBusinessDetails(businessId, RequestType.GET);
            businessTask.execute(new JSONObject[]{});
        }

        if(getIntent().getStringExtra(BUSINESS_NAME_TAG) != null) {
            name.setText(getIntent().getStringExtra(BUSINESS_NAME_TAG));
            name.setEnabled(false);
        }
    }

    @OnClick(R.id.add_new_number)
    public void addNewNumber() {
        if(currentPhoneCount < 3) {
            currentPhoneCount++;
            if(currentPhoneCount == 2) {
                findViewById(R.id.phone_2).setVisibility(View.VISIBLE);
            }
            if(currentPhoneCount == 3) {
                findViewById(R.id.phone_3).setVisibility(View.VISIBLE);
            }
        }
    }

    @OnItemSelected(R.id.business_categories_spinner)
    public void categorySelected(Spinner spinner, int position) {
        selectedCategory = categories.get(position).getEnumText();
        subCategories = allCategoryDao.getCategoriesMap().get(categories.get(position));
        subCategoriesAdapter.setData(subCategories);
    }

    @OnItemSelected(R.id.business_sub_categories_spinner)
    public void subCategorySelected(Spinner spinner, int position) {
        selectedSubCategory = subCategories.get(position).getEnumText();
    }

    @OnClick(R.id.business_start_time)
    public void onSelectStartTime() {
        showTimePicker(startTime);
    }

    @OnClick(R.id.business_close_time)
    public void onSelectCloseTime() {
        showTimePicker(closeTime);
    }

    @OnClick(R.id.add_business_btn)
    public void addBusiness() {
        if(name.getEditableText().toString().trim().length() > 0
                && phone.getEditableText().toString().trim().length() > 0) {
            categoryDao.addCategory(selectedCategory,selectedSubCategory);
            businessDao.setCategoryDao(categoryDao);

            businessDao.setName(name.getEditableText().toString().trim());
            businessDao.addPhoneNumber(phone.getEditableText().toString().trim());
            if(currentPhoneCount == 2) {
                businessDao.addPhoneNumber(phone2.getEditableText().toString().trim());
            }
            if(currentPhoneCount == 3) {
                businessDao.addPhoneNumber(phone3.getEditableText().toString().trim());
            }
            businessDao.setWebsite(website.getEditableText().toString().trim());

            businessAddressDao.setAddress(line1.getEditableText().toString().trim());
            businessAddressDao.setCity(city.getEditableText().toString().trim());
            businessAddressDao.setState(state.getEditableText().toString().trim());
            businessAddressDao.setZip(zip.getEditableText().toString().trim());
            businessAddressDao.setLandmark(landmark.getEditableText().toString().trim());
            businessDao.setBusinessAddressDao(businessAddressDao);

            addHoursOfOperation();
            businessDao.setHoursOfOperationDao(hoursOfOperationDao);

            if(isEdit) {
                //If edit then update
                businessTask = new BusinessTask(4, this, this);
                businessTask.setBusinessDetails(businessId, RequestType.PUT);
                businessTask.execute(new JSONObject[]{businessDao.toJSON()});
            } else {
                //Add new business
                businessTask = new BusinessTask(2, this, this);
                businessTask.execute(new JSONObject[]{businessDao.toJSON()});
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
                allCategoryDao = (CategoryListDao) object;
                categories = Arrays.asList(allCategoryDao.getCategoriesMap().keySet().toArray(
                        new CategoryDao[allCategoryDao.getCategoriesMap().keySet().size()]));
                categoriesAdapter.setData(categories);

                subCategories = Arrays.asList(allCategoryDao.getCategoriesMap().get(categories.get(0)).toArray(
                        new CategoryDao[allCategoryDao.getCategoriesMap().get(categories.get(0)).size()]));
                subCategoriesAdapter.setData(subCategories);
                break;
            case 2:
                //New business added. Close activity.
                finish();
                break;
            case 3:
                //Map Business data
                businessDao = (BusinessDao) object;
                for(String number : businessDao.getPhoneNumber()) {
                    if(currentPhoneCount == 1)
                        phone.setText(number);
                    if(currentPhoneCount == 2) {
                        phone2.setText(number);
                        findViewById(R.id.phone_2).setVisibility(View.VISIBLE);
                    }
                    if(currentPhoneCount == 3) {
                        phone3.setText(number);
                        findViewById(R.id.phone_3).setVisibility(View.VISIBLE);
                    }
                    currentPhoneCount++;
                }
                website.setText(businessDao.getWebsite());
                line1.setText(businessDao.getBusinessAddressDao().getAddress());
                city.setText(businessDao.getBusinessAddressDao().getCity());
                state.setText(businessDao.getBusinessAddressDao().getState());
                zip.setText(businessDao.getBusinessAddressDao().getZip());
                setHoursOfOperation(businessDao.getHoursOfOperationDao().getDailyTimeInfoList());
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

    private void showTimePicker(final AppCompatTextView textView) {
        final Calendar c = Calendar.getInstance();
        int mHour = c.get(Calendar.HOUR_OF_DAY);
        int mMinute = c.get(Calendar.MINUTE);

        TimePickerDialog timePickerDialog = new TimePickerDialog(this,
                new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        Time tme = new Time(hourOfDay, minute, 0);//seconds by default set to zero
                        Format formatter = new SimpleDateFormat("h:mm a");
                        textView.setText(formatter.format(tme));
                    }
                }, mHour, mMinute, false);
        timePickerDialog.show();
    }

    private void setHoursOfOperation(List<DailyTimeInfoDao> data) {
        String time = "";
        for(DailyTimeInfoDao dao : data) {
            if (!dao.getTime().contains("<b>") && !dao.getTime().equalsIgnoreCase("Closed")) {
                time = dao.getTime();
            }
            setChecked(dao);
        }
        startTime.setText(time.split("-")[0].trim());
        closeTime.setText(time.split("-")[1].trim());
    }

    private void setChecked(DailyTimeInfoDao info) {
        if(info.getDay().toLowerCase().contains("sun") && !info.getTime().equalsIgnoreCase("Closed"))
            sunday.setChecked(true);
        if(info.getDay().toLowerCase().contains("mon") && !info.getTime().equalsIgnoreCase("Closed"))
            monday.setChecked(true);
        if(info.getDay().toLowerCase().contains("tue") && !info.getTime().equalsIgnoreCase("Closed"))
            tuesday.setChecked(true);
        if(info.getDay().toLowerCase().contains("wed") && !info.getTime().equalsIgnoreCase("Closed"))
            wednesday.setChecked(true);
        if(info.getDay().toLowerCase().contains("thu") && !info.getTime().equalsIgnoreCase("Closed"))
            thursday.setChecked(true);
        if(info.getDay().toLowerCase().contains("fri") && !info.getTime().equalsIgnoreCase("Closed"))
            friday.setChecked(true);
        if(info.getDay().toLowerCase().contains("sat") && !info.getTime().equalsIgnoreCase("Closed"))
            saturday.setChecked(true);
    }

    private void addHoursOfOperation() {
        String time = startTime.getText().toString() + " - " + closeTime.getText().toString();
        hoursOfOperationDao.addHoursMap("SUN", sunday.isChecked() ? time : "Closed");
        hoursOfOperationDao.addHoursMap("MON", monday.isChecked() ? time : "Closed");
        hoursOfOperationDao.addHoursMap("TUE", tuesday.isChecked() ? time : "Closed");
        hoursOfOperationDao.addHoursMap("WED", wednesday.isChecked() ? time : "Closed");
        hoursOfOperationDao.addHoursMap("THU", thursday.isChecked() ? time : "Closed");
        hoursOfOperationDao.addHoursMap("FRI", friday.isChecked() ? time : "Closed");
        hoursOfOperationDao.addHoursMap("SAT", saturday.isChecked() ? time : "Closed");
    }
}
