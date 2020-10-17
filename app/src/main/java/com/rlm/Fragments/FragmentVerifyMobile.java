package com.rlm.Fragments;


import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import com.rlm.Activities.FirstActivity;
import com.rlm.Activities.HomeActivity;
import com.rlm.R;
import com.rlm.databinding.FragmentVerifyMobileBinding;
import com.utils.Utils.Tools;


/**
 * A simple {@link Fragment} subclass.
 */
public class FragmentVerifyMobile extends Fragment {
    private FragmentVerifyMobileBinding binding;

    public FragmentVerifyMobile() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Tools.get().updateResources(getActivity());
        binding= DataBindingUtil.inflate(inflater, R.layout.fragment_verify_mobile, container, false);
        BindView();
        return binding.getRoot();
    }

    private void BindView() {
        binding.imgBack.setOnClickListener(v->((FirstActivity)getActivity()).onBackPressed());
        binding.btnGetStart.setOnClickListener(v->{
            startActivity(new Intent(getActivity(), HomeActivity.class));
            getActivity().finish();
        });
    }

}
