package com.rlm.Fragments;


import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.rlm.Activities.HomeActivity;
import com.rlm.Constant.BaseClass;
import com.rlm.R;
import com.rlm.databinding.FragmentForgotPasswordBinding;
import com.rlm.databinding.FragmentLoginBinding;
import com.utils.Session.SessionManager;
import com.utils.Utils.Tools;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

import www.develpoeramit.mapicall.ApiCallBuilder;
import www.develpoeramit.mapicall.ProgressStyle;

public class FragmentForgotPassword extends Fragment {
    private FragmentForgotPasswordBinding binding;
    private SessionManager session;

    public FragmentForgotPassword() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Tools.get().updateResources(getActivity());
        binding= DataBindingUtil.inflate(inflater, R.layout.fragment_forgot_password, container, false);
        BindView();
        return binding.getRoot();
    }

    private void BindView() {
        binding.loading.setGifImageResource(R.drawable.loading);
        session= SessionManager.get(getContext());
        binding.btnSubmit.setOnClickListener(v->{
            if (Validation()){
                Continue();
            }
        });
        binding.imgBack.setOnClickListener(v->{
            Navigation.findNavController(binding.getRoot()).popBackStack();
        });

    }
    private boolean Validation(){
        boolean valid=false;
        if (binding.etEmail.getText().toString().isEmpty()){
            binding.etEmail.setError(getResources().getString(R.string.required));
            binding.etEmail.requestFocus();
        }else if (!Tools.isValidEmail(binding.etEmail.getText().toString())){
            binding.etEmail.setError(getResources().getString(R.string.email_not_valid));
            binding.etEmail.requestFocus();
        }else {
            valid=true;
        }
        return valid;
    }
    private HashMap<String, String> getParam() {
        HashMap<String,String>param=new HashMap<>();
        param.put("email",binding.etEmail.getText().toString());
        return param;
    }
    private void Continue(){
        binding.loading.setVisibility(View.VISIBLE);
        ApiCallBuilder.build(getActivity())
                .isShowProgressBar(true, ProgressStyle.STYLE_2)
                .setParam(getParam())
                .setUrl(BaseClass.get().forgotPassword())
                .execute(new ApiCallBuilder.onResponse() {
                    @Override
                    public void Success(String response) {
                        binding.loading.setVisibility(View.GONE);
                        Log.e("forgotPassword","=====>"+response);
                        try {
                            JSONObject object=new JSONObject(response);
                            boolean status=object.getString("status").contains("1");
                            Toast.makeText(getContext(), ""+object.getString("message"), Toast.LENGTH_SHORT).show();
                            if (status){
                                Navigation.findNavController(binding.getRoot()).popBackStack();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                    @Override
                    public void Failed(String error) {
                        binding.loading.setVisibility(View.GONE);

                    }
                });
    }
}
