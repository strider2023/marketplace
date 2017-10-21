package com.touchmenotapps.marketplace.framework;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.view.View;

import com.touchmenotapps.marketplace.R;
import com.touchmenotapps.marketplace.common.GlobalConstants;

import static android.Manifest.permission.CAMERA;
import static android.Manifest.permission.READ_CONTACTS;
import static android.Manifest.permission.READ_EXTERNAL_STORAGE;
import static android.Manifest.permission.READ_SMS;

/**
 * Created by i7 on 16-10-2017.
 */

public class PermissionsUtil {

    private Activity activity;

    public PermissionsUtil(Activity activity) {
        this.activity = activity;
    }

    public boolean checkSMSPermission(View view) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            return true;
        }
        if (activity.checkSelfPermission(READ_SMS) == PackageManager.PERMISSION_GRANTED) {
            return true;
        }
        if (activity.shouldShowRequestPermissionRationale(READ_CONTACTS)) {
            Snackbar.make(view, "SMS access is required to read OTP.", Snackbar.LENGTH_INDEFINITE)
                    .setAction(android.R.string.ok, new View.OnClickListener() {
                        @Override
                        @TargetApi(Build.VERSION_CODES.M)
                        public void onClick(View v) {
                            activity.requestPermissions(new String[]{READ_SMS}, GlobalConstants.REQUEST_ACCESS_SMS);
                        }
                    });
        } else {
            activity.requestPermissions(new String[]{READ_SMS}, GlobalConstants.REQUEST_ACCESS_SMS);
        }
        return false;
    }

    public boolean checkLocationPermission(View view) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            return true;
        }
        if (activity.checkSelfPermission(READ_SMS) == PackageManager.PERMISSION_GRANTED) {
            return true;
        }
        if (activity.shouldShowRequestPermissionRationale(READ_CONTACTS)) {
            Snackbar.make(view, "SMS access is required to read OTP.", Snackbar.LENGTH_INDEFINITE)
                    .setAction(android.R.string.ok, new View.OnClickListener() {
                        @Override
                        @TargetApi(Build.VERSION_CODES.M)
                        public void onClick(View v) {
                            activity.requestPermissions(new String[]{READ_SMS}, GlobalConstants.REQUEST_ACCESS_SMS);
                        }
                    });
        } else {
            activity.requestPermissions(new String[]{READ_SMS}, GlobalConstants.REQUEST_ACCESS_SMS);
        }
        return false;
    }

//    private boolean requestPermissions(Activity activity) {
//        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
//            return true;
//        }
//        if (checkSelfPermission(READ_CONTACTS) == PackageManager.PERMISSION_GRANTED) {
//            return true;
//        }
//        if (checkSelfPermission(READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
//            return true;
//        }
//        if (checkSelfPermission(READ_SMS) == PackageManager.PERMISSION_GRANTED) {
//            return true;
//        }
//        if (checkSelfPermission(CAMERA) == PackageManager.PERMISSION_GRANTED) {
//            return true;
//        }
//        if (checkSelfPermission(LOCATION_SERVICE) == PackageManager.PERMISSION_GRANTED) {
//            return true;
//        }
//        if (shouldShowRequestPermissionRationale(READ_CONTACTS)) {
//            Snackbar.make(splashText, R.string.permission_rationale, Snackbar.LENGTH_INDEFINITE)
//                    .setAction(android.R.string.ok, new View.OnClickListener() {
//                        @Override
//                        @TargetApi(Build.VERSION_CODES.M)
//                        public void onClick(View v) {
//                            requestPermissions(new String[]{READ_CONTACTS}, GlobalConstants.REQUEST_READ_CONTACTS);
//                        }
//                    });
//        } else {
//            requestPermissions(new String[]{READ_CONTACTS}, GlobalConstants.REQUEST_READ_CONTACTS);
//        }
//        return false;
//    }
}
