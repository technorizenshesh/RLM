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
import com.rlm.Activities.FirstActivity;
import com.rlm.Activities.HomeActivity;
import com.rlm.Constant.BaseClass;
import com.rlm.R;
import com.rlm.databinding.FragmentRegisterBinding;
import com.utils.Session.SessionManager;
import com.utils.Utils.Tools;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

import www.develpoeramit.mapicall.ApiCallBuilder;
import www.develpoeramit.mapicall.ProgressStyle;

public class FragmentRegister extends Fragment {
    private FragmentRegisterBinding binding;
    private SessionManager session;
    private String FireBaseToken;
    private static final int AUTOCOMPLETE_REQUEST_CODE =111;
    double lat,lng;

    public FragmentRegister() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Tools.get().updateResources(getActivity());
        binding= DataBindingUtil.inflate(inflater, R.layout.fragment_register, container, false);
        BindView();
        return binding.getRoot();
    }

    private void BindView() {
        binding.loading.setGifImageResource(R.drawable.loading);
        session= SessionManager.get(getContext());
        FirebaseInstanceId.getInstance().getInstanceId().addOnSuccessListener(new OnSuccessListener<InstanceIdResult>() {
            @Override
            public void onSuccess(InstanceIdResult instanceIdResult) {
                FireBaseToken=instanceIdResult.getToken();
            }
        });
        binding.btnSubmit.setOnClickListener(v->{
            if (Validation()){
                Continue();
            }
        });
        binding.imgBack.setOnClickListener(v-> Navigation.findNavController(binding.getRoot()).popBackStack());

    }
    private boolean Validation(){
        boolean valid=false;
        if (binding.etFname.getText().toString().isEmpty()){
            binding.etFname.setError(getResources().getString(R.string.required));
            binding.etFname.requestFocus();
        }else if (binding.etLname.getText().toString().isEmpty()){
            binding.etLname.setError(getResources().getString(R.string.required));
            binding.etLname.requestFocus();
        }else if (binding.etEmail.getText().toString().isEmpty()){
            binding.etEmail.setError(getResources().getString(R.string.required));
            binding.etEmail.requestFocus();
        }else if (!Tools.isValidEmail(binding.etEmail.getText().toString())){
            binding.etEmail.setError(getResources().getString(R.string.email_not_valid));
            binding.etEmail.requestFocus();
        }else if (binding.etMobile.getText().toString().isEmpty()){
            binding.etMobile.setError(getResources().getString(R.string.required));
            binding.etMobile.requestFocus();
        }else if (binding.etPassword.getText().toString().isEmpty()){
            binding.etPassword.setError(getResources().getString(R.string.required));
            binding.etPassword.requestFocus();
        }else {
            valid=true;
        }
        return valid;
    }
    private HashMap<String, String> getParam() {
        HashMap<String,String>param=new HashMap<>();
        param.put("first_name",binding.etFname.getText().toString());
        param.put("last_name",binding.etLname.getText().toString());
        param.put("email",binding.etEmail.getText().toString());
        param.put("password",binding.etPassword.getText().toString());
        param.put("mobile",binding.cpp.getSelectedCountryCode()+binding.etMobile.getText().toString());
        param.put("register_id",FireBaseToken);
        return param;
    }
    private void Continue(){
        binding.loading.setVisibility(View.VISIBLE);
        ApiCallBuilder.build(getActivity())
                .isShowProgressBar(true, ProgressStyle.STYLE_2)
                .setParam(getParam())
                .setUrl(BaseClass.get().SignUp())
                .execute(new ApiCallBuilder.onResponse() {
                    @Override
                    public void Success(String response) {
                        binding.loading.setVisibility(View.GONE);
                        Log.e("SignUp","=====>"+response);
                        try {
                            JSONObject object=new JSONObject(response);
                            boolean status=object.getString("status").contains("1");
                            if (status){
                                session.CreateSession(object.getString("result"));
//                                Navigation.findNavController(binding.getRoot()).navigate(R.id.action_verify);
                                startActivity(new Intent(getActivity(), HomeActivity.class));
                                getActivity().finish();
                            }else {
                                Toast.makeText(getContext(), ""+object.getString("message"), Toast.LENGTH_SHORT).show();
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
