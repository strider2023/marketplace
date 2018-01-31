package com.touchmenotapps.marketplace.common.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.AppCompatTextView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.touchmenotapps.marketplace.R;
import com.touchmenotapps.marketplace.SplashActivity;
import com.touchmenotapps.marketplace.bo.ProfileDao;
import com.touchmenotapps.marketplace.framework.enums.RequestType;
import com.touchmenotapps.marketplace.framework.enums.ServerEvents;
import com.touchmenotapps.marketplace.framework.interfaces.ServerResponseListener;
import com.touchmenotapps.marketplace.framework.persist.AppPreferences;
import com.touchmenotapps.marketplace.threads.asynctasks.ProfileTask;

import org.json.simple.JSONObject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by arindamnath on 30/12/17.
 */

public class ProfileFragment extends Fragment
        implements ServerResponseListener {

    @BindView(R.id.profile_phone_number)
    AppCompatTextView phoneNumber;
    @BindView(R.id.profile_role)
    AppCompatTextView role;
    @BindView(R.id.profile_name)
    AppCompatTextView name;
    @BindView(R.id.profile_name_edit)
    AppCompatEditText nameEdit;
    @BindView(R.id.edit_profile_button)
    FloatingActionButton editProfile;

    private View mViewHolder;
    private AppPreferences appPreferences;
    private ProfileTask profileTask;
    private ProfileDao profileDao;
    private boolean isEdit = false;

    public static ProfileFragment newInstance() {
        ProfileFragment fragment = new ProfileFragment();
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mViewHolder = inflater.inflate(R.layout.fragment_profile, container, false);
        ButterKnife.bind(this, mViewHolder);

        appPreferences = new AppPreferences(getContext());

        phoneNumber.setText(appPreferences.getUserPhoneNumber());
        role.setText(appPreferences.getUserType().toString());
        return mViewHolder;
    }

    @Override
    public void onResume() {
        super.onResume();
        profileTask = new ProfileTask(1, getContext(), this, false);
        profileTask.setRequestType(RequestType.GET);
        profileTask.execute(new JSONObject[]{});
    }

    @OnClick(R.id.profile_logout)
    public void onLogout() {
        appPreferences.setLoggedOut();
        startActivity(new Intent(getActivity(), SplashActivity.class));
        getActivity().finish();
    }

    @OnClick(R.id.edit_profile_button)
    public void onEditSelected() {
        if(isEdit) {
            if(nameEdit.getEditableText().toString().trim().length() > 0) {
                profileDao.setName(nameEdit.getEditableText().toString().trim());
                profileTask = new ProfileTask(2, getContext(), this, false);
                profileTask.setRequestType(RequestType.PUT);
                profileTask.execute(new JSONObject[]{profileDao.toJSON()});
            } else {
                Snackbar.make(phoneNumber, "Name cannot be empty!", Snackbar.LENGTH_LONG).show();
            }
        } else {
            isEdit = true;
            name.setVisibility(View.GONE);
            nameEdit.setVisibility(View.VISIBLE);
            editProfile.setImageResource(R.drawable.ic_save_yellow);
        }
    }

    @Override
    public void onSuccess(int threadId, Object object) {
        switch (threadId) {
            case 1:
                profileDao = (ProfileDao) object;
                if(profileDao.getName() != null) {
                    name.setText(profileDao.getName());
                    nameEdit.setText(profileDao.getName());
                }
                break;
            case 2:
                Snackbar.make(phoneNumber, "Profile updated.", Snackbar.LENGTH_LONG).show();
                isEdit = false;
                name.setText(nameEdit.getEditableText().toString().trim());
                name.setVisibility(View.VISIBLE);
                nameEdit.setVisibility(View.GONE);
                editProfile.setImageResource(R.drawable.ic_edit_black_24dp);
                break;
        }
    }

    @Override
    public void onFaliure(ServerEvents serverEvents, Object object) {
        Snackbar.make(phoneNumber, object.toString(), Snackbar.LENGTH_LONG).show();
    }
}
