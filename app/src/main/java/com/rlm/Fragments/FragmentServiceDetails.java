package com.rlm.Fragments;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.rlm.Activities.HomeActivity;
import com.rlm.Constant.BaseClass;
import com.rlm.Models.ModelService;
import com.rlm.R;
import com.rlm.databinding.FragmentOurVisionBinding;
import com.rlm.databinding.FragmentServiceDetailsBinding;
import com.squareup.picasso.Picasso;
import com.utils.Session.SessionManager;
import com.utils.Utils.Tools;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

import www.develpoeramit.mapicall.ApiCallBuilder;

public class FragmentServiceDetails extends Fragment {
    private FragmentServiceDetailsBinding binding;
    private ModelService service;

    public FragmentServiceDetails() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Tools.get().updateResources(getActivity());
        binding= DataBindingUtil.inflate(inflater, R.layout.fragment_service_details, container, false);
        BindView();
        return binding.getRoot();
    }

    private void BindView() {
        binding.loading.setGifImageResource(R.drawable.loading);
        if (getArguments()!=null){
            service=(ModelService)getArguments().getSerializable("data");
            binding.tvTitle.setText(service.getTitle());
            binding.tvDescription.setText(service.getInfo());
            if (service.getAvg_rating().length()>0) {
                binding.rbRating.setRating(Float.parseFloat(service.getAvg_rating()));
            }
            if (!service.getImage().isEmpty()){
                Picasso.get().load(service.getImage()).into(binding.image);
            }
        }
        binding.tvRate.setOnClickListener(v->{
            new FragmentRating().Callback(service.getTitle(),this::onServiceRate).show(getChildFragmentManager(),"");
        });
        binding.btnBook.setOnClickListener(v->{
            Navigation.findNavController(binding.getRoot()).navigate(R.id.action_appointment);
        });
    }

    private void onServiceRate(float v, String s) {
        binding.loading.setVisibility(View.VISIBLE);
        HashMap<String,String>param=new HashMap<>();
        param.put("user_id", SessionManager.get(getContext()).getUserID());
        param.put("service_id",service.getId());
        param.put("rating",""+v);
        param.put("comment",""+s);
        ApiCallBuilder.build(getContext()).setUrl(BaseClass.get().addServiceRating())
                .setParam(param).isShowProgressBar(false)
                .execute(new ApiCallBuilder.onResponse() {
                    @Override
                    public void Success(String response) {
                        binding.loading.setVisibility(View.GONE);
                        try {
                            JSONObject object=new JSONObject(response);
                            boolean status=object.getString("status").contains("1");
                            Toast.makeText(getActivity(),""+object.getString("message"),Toast.LENGTH_LONG).show();
                            String message=object.getString("message");
                            if (status){
                                Bundle bundle=new Bundle();
                                bundle.putString("message",message);
                                Navigation.findNavController(binding.getRoot()).navigate(R.id.action_success,bundle);
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
