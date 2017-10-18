package com.touchmenotapps.marketplace.signup;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatEditText;
import android.text.Editable;
import android.text.TextWatcher;

import com.touchmenotapps.marketplace.R;
import com.touchmenotapps.marketplace.common.GlobalConstants;
import com.touchmenotapps.marketplace.framework.PermissionsUtil;
import com.touchmenotapps.marketplace.onboarding.AppIntroActivity;

import butterknife.BindView;
import butterknife.ButterKnife;

public class RegistrationOTPActivity extends AppCompatActivity {

    @BindView(R.id.otp_enter_number)
    AppCompatEditText otpText;

    private PermissionsUtil permissionsUtil;
    private boolean isSMSPermissionAvailable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration_otp);
        ButterKnife.bind(this);

        permissionsUtil = new PermissionsUtil(this);
        isSMSPermissionAvailable = permissionsUtil.checkSMSPermission(otpText);

        otpText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) { }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) { }

            @Override
            public void afterTextChanged(Editable editable) {
                if(editable.toString().length() == 4) {
                    startActivity(new Intent(RegistrationOTPActivity.this, AppIntroActivity.class));
                    finish();
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
}
