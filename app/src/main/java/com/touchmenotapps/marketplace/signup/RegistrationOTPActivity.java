package com.touchmenotapps.marketplace.signup;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatEditText;
import android.text.Editable;
import android.text.TextWatcher;

import com.touchmenotapps.marketplace.R;
import com.touchmenotapps.marketplace.common.GlobalConstants;
import com.touchmenotapps.marketplace.common.enums.ServerEvents;
import com.touchmenotapps.marketplace.dao.OTPDao;
import com.touchmenotapps.marketplace.framework.PermissionsUtil;
import com.touchmenotapps.marketplace.framework.interfaces.ServerResponseListener;
import com.touchmenotapps.marketplace.framework.persist.ApplicationSharedPreferences;
import com.touchmenotapps.marketplace.onboarding.AppIntroActivity;
import com.touchmenotapps.marketplace.signup.threads.UserOTPTask;

import org.json.simple.JSONObject;

import butterknife.BindView;
import butterknife.ButterKnife;

public class RegistrationOTPActivity extends AppCompatActivity implements ServerResponseListener{

    @BindView(R.id.otp_enter_number)
    AppCompatEditText otpText;

    private OTPDao otpDao;
    private PermissionsUtil permissionsUtil;
    private ApplicationSharedPreferences appPreference;
    private boolean isSMSPermissionAvailable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration_otp);
        ButterKnife.bind(this);

        permissionsUtil = new PermissionsUtil(this);
        appPreference = new ApplicationSharedPreferences(this);
        otpDao = new OTPDao(this);

        isSMSPermissionAvailable = permissionsUtil.checkSMSPermission(otpText);
        otpDao.setPhoneNumber(appPreference.getUserPhoneNumber());

        otpText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) { }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) { }

            @Override
            public void afterTextChanged(Editable editable) {
                //916286
                if(editable.toString().trim().length() == 6) {
                    otpDao.setUserOTP(editable.toString().trim());
                    new UserOTPTask(1, RegistrationOTPActivity.this, RegistrationOTPActivity.this)
                            .execute(new JSONObject[]{otpDao.toJSON()});
                }
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        if (requestCode == GlobalConstants.REQUEST_ACCESS_SMS) {
            if (grantResults.length == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                //TODO Send server request
            }
        }
    }

    @Override
    public void onSuccess(int threadId, Object object) {
        startActivity(new Intent(RegistrationOTPActivity.this, AppIntroActivity.class));
        finish();
    }

    @Override
    public void onFaliure(ServerEvents serverEvents, Object object) {
        Snackbar.make(otpText, object.toString(), Snackbar.LENGTH_LONG).show();
    }
}
