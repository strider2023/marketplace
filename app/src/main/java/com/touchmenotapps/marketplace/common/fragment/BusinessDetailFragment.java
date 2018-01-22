package com.touchmenotapps.marketplace.common.fragment;

import android.content.Intent;
import android.graphics.Paint;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.customtabs.CustomTabsIntent;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.AppCompatTextView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.touchmenotapps.marketplace.R;
import com.touchmenotapps.marketplace.bo.BusinessDao;
import com.touchmenotapps.marketplace.common.adapters.OperationTimeBaseAdapter;
import com.touchmenotapps.marketplace.common.adapters.PhoneBaseAdapter;
import com.touchmenotapps.marketplace.common.threads.GetBusinessByIdTask;
import com.touchmenotapps.marketplace.framework.enums.ServerEvents;
import com.touchmenotapps.marketplace.framework.interfaces.ServerResponseListener;

import org.json.simple.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.touchmenotapps.marketplace.framework.constants.AppConstants.BUSINESS_ID_TAG;

/**
 * Created by arindamnath on 16/01/18.
 */

public class BusinessDetailFragment extends Fragment
        implements ServerResponseListener {

    @BindView(R.id.business_website)
    AppCompatTextView website;
    @BindView(R.id.business_address)
    AppCompatTextView address;
    @BindView(R.id.business_hours_of_operation)
    ListView operationTime;
    @BindView(R.id.business_phone_list)
    ListView phoneList;

    private View mViewHolder;
    private Long businessId;
    private BusinessDao businessDao;
    private PhoneBaseAdapter phoneBaseAdapter;
    private OperationTimeBaseAdapter operationTimeBaseAdapter;

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

        phoneBaseAdapter = new PhoneBaseAdapter(getActivity());
        operationTimeBaseAdapter = new OperationTimeBaseAdapter(getContext());
        phoneList.setAdapter(phoneBaseAdapter);
        operationTime.setAdapter(operationTimeBaseAdapter);
        website.setPaintFlags(website.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);

        if (businessId != -1l) {
            JSONObject id = new JSONObject();
            id.put("id", String.valueOf(businessId));
            new GetBusinessByIdTask(1, getActivity(), this)
                    .execute(new JSONObject[]{id});
        }
        return mViewHolder;
    }

    @OnClick(R.id.business_website)
    public void onWebisteView() {
        try {
            CustomTabsIntent.Builder builder = new CustomTabsIntent.Builder();
            builder.setToolbarColor(ContextCompat.getColor(getActivity(), R.color.colorPrimary));
            CustomTabsIntent customTabsIntent = builder.build();
            customTabsIntent.launchUrl(getActivity(), Uri.parse(website.getText().toString()));
        } catch (Exception e) {
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setData(Uri.parse(website.getText().toString()));
            startActivity(intent);
        }
    }

    @Override
    public void onSuccess(int threadId, Object object) {
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
    }

    @Override
    public void onFaliure(ServerEvents serverEvents, Object object) {
        Snackbar.make(website, object.toString(), Snackbar.LENGTH_LONG).show();
    }
}
