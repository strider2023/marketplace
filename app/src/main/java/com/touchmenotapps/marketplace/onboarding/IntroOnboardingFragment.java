package com.touchmenotapps.marketplace.onboarding;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.touchmenotapps.marketplace.R;
import com.touchmenotapps.marketplace.onboarding.listeners.IntroOnboardingOnChangeListener;
import com.touchmenotapps.marketplace.onboarding.listeners.IntroOnboardingOnLeftOutListener;
import com.touchmenotapps.marketplace.onboarding.listeners.IntroOnboardingOnRightOutListener;

import java.util.ArrayList;

public class IntroOnboardingFragment extends Fragment {

    private static final String ELEMENTS_PARAM = "elements";

    private IntroOnboardingOnChangeListener mOnChangeListener;
    private IntroOnboardingOnRightOutListener mOnRightOutListener;
    private IntroOnboardingOnLeftOutListener mOnLeftOutListener;
    private ArrayList<IntroOnboardingPage> mElements;


    public static IntroOnboardingFragment newInstance(ArrayList<IntroOnboardingPage> elements) {
        IntroOnboardingFragment fragment = new IntroOnboardingFragment();
        Bundle args = new Bundle();
        args.putSerializable(ELEMENTS_PARAM, elements);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mElements = (ArrayList<IntroOnboardingPage>) getArguments().get(ELEMENTS_PARAM);
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.onboarding_main_layout, container, false);

        // create engine for onboarding element
        IntroOnboardingEngine introOnboardingEngine = new IntroOnboardingEngine(view.findViewById(R.id.onboardingRootView), mElements, getActivity().getApplicationContext());
        // set listeners
        introOnboardingEngine.setOnChangeListener(mOnChangeListener);
        introOnboardingEngine.setOnLeftOutListener(mOnLeftOutListener);
        introOnboardingEngine.setOnRightOutListener(mOnRightOutListener);

        return view;
    }

    public void setElements(ArrayList<IntroOnboardingPage> elements) {
        this.mElements = elements;
    }

    public ArrayList<IntroOnboardingPage> getElements() {
        return mElements;
    }

    public void setOnChangeListener(IntroOnboardingOnChangeListener onChangeListener) {
        this.mOnChangeListener = onChangeListener;
    }

    public void setOnRightOutListener(IntroOnboardingOnRightOutListener onRightOutListener) {
        this.mOnRightOutListener = onRightOutListener;
    }

    public void setOnLeftOutListener(IntroOnboardingOnLeftOutListener onLeftOutListener) {
        this.mOnLeftOutListener = onLeftOutListener;
    }

}
