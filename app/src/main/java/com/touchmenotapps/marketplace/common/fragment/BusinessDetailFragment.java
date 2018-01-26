package com.touchmenotapps.marketplace.common.fragment;

import android.content.Intent;
import android.graphics.Paint;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.customtabs.CustomTabsIntent;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.Loader;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.touchmenotapps.marketplace.R;
import com.touchmenotapps.marketplace.bo.BusinessDao;
import com.touchmenotapps.marketplace.bo.BusinessImageDao;
import com.touchmenotapps.marketplace.common.ViewImageActivity;
import com.touchmenotapps.marketplace.common.adapters.BusinessImageAdapter;
import com.touchmenotapps.marketplace.common.adapters.OperationTimeBaseAdapter;
import com.touchmenotapps.marketplace.common.adapters.PhoneBaseAdapter;
import com.touchmenotapps.marketplace.common.interfaces.BusinessImageSelectedListener;
import com.touchmenotapps.marketplace.framework.enums.LoaderID;
import com.touchmenotapps.marketplace.framework.enums.RequestType;
import com.touchmenotapps.marketplace.framework.enums.UserType;
import com.touchmenotapps.marketplace.framework.persist.AppPreferences;
import com.touchmenotapps.marketplace.threads.asynctasks.BusinessTask;
import com.touchmenotapps.marketplace.framework.enums.ServerEvents;
import com.touchmenotapps.marketplace.framework.interfaces.ServerResponseListener;
import com.touchmenotapps.marketplace.threads.asynctasks.UserKPITask;
import com.touchmenotapps.marketplace.threads.loaders.ImagesLoaderTask;

import org.json.simple.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.touchmenotapps.marketplace.framework.constants.AppConstants.BUSINESS_ID_TAG;
import static com.touchmenotapps.marketplace.framework.constants.AppConstants.BUSINESS_IMAGE_TAG;
import static com.touchmenotapps.marketplace.framework.constants.AppConstants.IMAGE_CAPTION_TAG;
import static com.touchmenotapps.marketplace.framework.constants.AppConstants.IMAGE_URL_TAG;
import static com.touchmenotapps.marketplace.framework.constants.AppConstants.KPI_ADDRESS;

/**
 * Created by arindamnath on 16/01/18.
 */

public class BusinessDetailFragment extends Fragment
        implements ServerResponseListener, BusinessImageSelectedListener,
        LoaderManager.LoaderCallbacks<List<BusinessImageDao>> {

    @BindView(R.id.business_website)
    AppCompatTextView website;
    @BindView(R.id.business_address)
    AppCompatTextView address;
    @BindView(R.id.business_hours_of_operation)
    ListView operationTime;
    @BindView(R.id.business_phone_list)
    ListView phoneList;
    @BindView(R.id.business_images)
    RecyclerView imagesList;
    @BindView(R.id.business_no_images)
    AppCompatTextView noImages;

    private View mViewHolder;
    private Long businessId;
    private BusinessDao businessDao;
    private PhoneBaseAdapter phoneBaseAdapter;
    private BusinessImageAdapter businessImageAdapter;
    private OperationTimeBaseAdapter operationTimeBaseAdapter;
    private Bundle queryData;
    private AppPreferences appPreferences;
    private UserKPITask userKPITask;

    public static BusinessDetailFragment newInstance(long businessId) {
        BusinessDetailFragment fragment = new BusinessDetailFragment();
        Bundle bundle = new Bundle();
        bundle.putLong(BUSINESS_ID_TAG, businessId);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle args = getArguments();
        if (args.getLong(BUSINESS_ID_TAG, -1l) != -1l) {
            businessId = args.getLong(BUSINESS_ID_TAG, -1l);
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mViewHolder = inflater.inflate(R.layout.fragment_business_details, container, false);
        ButterKnife.bind(this, mViewHolder);

        appPreferences = new AppPreferences(getActivity());
        phoneBaseAdapter = new PhoneBaseAdapter(getActivity());
        operationTimeBaseAdapter = new OperationTimeBaseAdapter(getContext());
        phoneList.setAdapter(phoneBaseAdapter);
        operationTime.setAdapter(operationTimeBaseAdapter);
        website.setPaintFlags(website.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);

        businessImageAdapter = new BusinessImageAdapter(this);
        StaggeredGridLayoutManager staggeredGridLayoutManager = new StaggeredGridLayoutManager(1, LinearLayoutManager.HORIZONTAL);
        imagesList.setLayoutManager(staggeredGridLayoutManager);
        imagesList.setAdapter(businessImageAdapter);
        return mViewHolder;
    }

    @Override
    public void onResume() {
        super.onResume();
        if (businessId != -1l) {
            BusinessTask businessTask = new BusinessTask(1, getActivity(), this);
            businessTask.setBusinessDetails(businessId, RequestType.GET);
            businessTask.execute(new JSONObject[]{});

            queryData = new Bundle();
            queryData.putLong(BUSINESS_ID_TAG, businessId);
            getActivity().getSupportLoaderManager()
                    .initLoader(LoaderID.FETCH_BUSINESS_IMAGES.getValue(), queryData, BusinessDetailFragment.this).forceLoad();
        }
    }

    @OnClick(R.id.business_address)
    public void onLocationSelected() {
        if(appPreferences.getUserType() == UserType.CONSUMER) {
            userKPITask = new UserKPITask(2, getActivity(), this);
            userKPITask.setBusinessId(businessId);
            userKPITask.setType(KPI_ADDRESS);
            userKPITask.execute(new JSONObject[]{});
        }
    }

    @OnClick(R.id.business_website)
    public void onWebisteView() {
        try {
            CustomTabsIntent.Builder builder = new CustomTabsIntent.Builder();
            builder.setToolbarColor(ContextCompat.getColor(getActivity(), R.color.colorPrimary));
            CustomTabsIntent customTabsIntent = builder.build();
            customTabsIntent.launchUrl(getActivity(), Uri.parse("http://" + website.getText().toString()));
        } catch (Exception e) {
            e.printStackTrace();
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setData(Uri.parse("http://" + website.getText().toString()));
            startActivity(intent);
        }
    }

    @Override
    public void onSuccess(int threadId, Object object) {
        switch (threadId) {
            case 1:
                businessDao = (BusinessDao) object;
                List<String> phoneNumbers = new ArrayList<>();
                for (String number : businessDao.getPhoneNumber()) {
                    phoneNumbers.add(number);
                }
                phoneBaseAdapter.setData(phoneNumbers);
                website.setText(businessDao.getWebsite());
                address.setText(businessDao.getBusinessAddressDao().getAddress() + "\n" +
                        businessDao.getBusinessAddressDao().getCity() + "\n" +
                        businessDao.getBusinessAddressDao().getState() + "\n" +
                        businessDao.getBusinessAddressDao().getZip());
                operationTimeBaseAdapter.setData(businessDao.getHoursOfOperationDao().getDailyTimeInfoList());
                break;
            case 2:
                String url = "http://maps.google.com/maps?daddr=" +
                        businessDao.getBusinessAddressDao().getAddress() + "," +
                        businessDao.getBusinessAddressDao().getCity() + "," +
                        businessDao.getBusinessAddressDao().getState() + "," +
                        businessDao.getBusinessAddressDao().getZip();
                Intent intent = new Intent(android.content.Intent.ACTION_VIEW, Uri.parse(url));
                startActivity(intent);
                break;
            case 3:
                break;
        }
    }

    @Override
    public void onFaliure(ServerEvents serverEvents, Object object) {
        Snackbar.make(website, object.toString(), Snackbar.LENGTH_LONG).show();
    }

    @Override
    public void onImageClicked(BusinessImageDao businessImageDao) {
        Intent intent = new Intent(getActivity(), ViewImageActivity.class);
        intent.putExtra(BUSINESS_ID_TAG, businessId);
        intent.putExtra(BUSINESS_IMAGE_TAG, businessImageDao);
        startActivity(intent);
    }

    @Override
    public Loader<List<BusinessImageDao>> onCreateLoader(int id, Bundle args) {
        return new ImagesLoaderTask(getActivity(), args);
    }

    @Override
    public void onLoadFinished(Loader<List<BusinessImageDao>> loader, List<BusinessImageDao> data) {
        if(data.size() > 0) {
            noImages.setVisibility(View.GONE);
            imagesList.setVisibility(View.VISIBLE);
            businessImageAdapter.setData(data);
        } else {
            noImages.setVisibility(View.VISIBLE);
            imagesList.setVisibility(View.GONE);
        }
    }

    @Override
    public void onLoaderReset(Loader<List<BusinessImageDao>> loader) {

    }
}
