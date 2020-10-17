package com.rlm.Fragments;


import android.annotation.SuppressLint;
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
import com.rlm.databinding.FragmentAppointmentPartTwoBinding;
import com.rlm.databinding.FragmentSuccessBinding;
import com.utils.Utils.Tools;

import java.util.HashMap;


/**
 * A simple {@link Fragment} subclass.
 */
public class FragmentSuccess extends Fragment {
    private FragmentSuccessBinding binding;
    public FragmentSuccess() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Tools.get().updateResources(getActivity());
        binding= DataBindingUtil.inflate(inflater, R.layout.fragment_success, container, false);
        BindView();
        return binding.getRoot();
    }

    private void BindView() {
        binding.tvGoToHome.setOnClickListener(v->{
            startActivity(new Intent(getActivity(),HomeActivity.class));
            getActivity().finish();
        });
        if (getArguments()!=null){
            HashMap<String,String>data=(HashMap<String, String>) getArguments().getSerializable("data");
            if (data!=null) {
                binding.tvMessage.setText(R.string.appointment_msg);
                binding.tvDate.setText(getString(R.string.appointment_date) + data.get("start_date"));
            }else {
                String message=getArguments().getString("message");
                binding.tvMessage.setText(message);
                binding.tvDate.setVisibility(View.GONE);
            }
        }
    }
}
