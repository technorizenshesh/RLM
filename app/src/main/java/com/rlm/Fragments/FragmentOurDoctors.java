package com.rlm.Fragments;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.rlm.Activities.FirstActivity;
import com.rlm.Activities.HomeActivity;
import com.rlm.Adapters.AdapterDoctor;
import com.rlm.Adapters.AdapterService;
import com.rlm.Constant.BaseClass;
import com.rlm.Interfaces.onClickDoctorListener;
import com.rlm.Models.ModelDoctor;
import com.rlm.Models.ModelService;
import com.rlm.R;
import com.rlm.databinding.FragmentOurDoctorsBinding;
import com.rlm.databinding.FragmentOurServiceBinding;
import com.utils.Utils.Tools;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import www.develpoeramit.mapicall.ApiCallBuilder;


/**
 * A simple {@link Fragment} subclass.
 */
public class FragmentOurDoctors extends Fragment implements onClickDoctorListener {
    private FragmentOurDoctorsBinding binding;
    private ArrayList<ModelDoctor>arrayList=new ArrayList<>();
    private AdapterDoctor adapter;

    public FragmentOurDoctors() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Tools.get().updateResources(getActivity());
        binding= DataBindingUtil.inflate(inflater, R.layout.fragment_our_doctors, container, false);
        BindView();
        return binding.getRoot();
    }

    private void BindView() {
        binding.loading.setGifImageResource(R.drawable.loading);
        adapter=new AdapterDoctor(getContext(),arrayList).Callback(this);
        binding.recycleView.setAdapter(adapter);
        binding.swipeRefresh.setOnRefreshListener(this::getDoctors);
        getDoctors();
    }

    private void onClick(ModelDoctor doctor) {

    }
    private void getDoctors(){
        binding.loading.setVisibility(View.VISIBLE);
        ApiCallBuilder.build(getActivity()).setUrl(BaseClass.get().getDoctors())
                .execute(new ApiCallBuilder.onResponse() {
                    @Override
                    public void Success(String response) {
                        binding.loading.setVisibility(View.GONE);
                        binding.swipeRefresh.setRefreshing(false);
                        try {
                            JSONObject object=new JSONObject(response);
                            boolean status=object.getString("status").contains("1");
                            if (status){
                                arrayList.clear();
                                binding.tvNoRecord.setVisibility(View.GONE);
                                JSONArray array=object.getJSONArray("result");
                                for (int i=0;i<array.length();i++){
                                    JSONObject jsonObject=array.getJSONObject(i);
                                    ModelDoctor doctor=new ModelDoctor();
                                    doctor.setId(jsonObject.getString("id"));
                                    doctor.setFull_name(jsonObject.getString("full_name"));
                                    doctor.setEmail(jsonObject.getString("email"));
                                    doctor.setPhone(jsonObject.getString("phone"));
                                    doctor.setInfo(jsonObject.getString("info"));
                                    doctor.setWorking_time_limit(jsonObject.getString("working_time_limit"));
                                    doctor.setAvg_rating(jsonObject.getString("avg_rating"));
                                    doctor.setEnglish_name(jsonObject.getString("english_name"));
                                    doctor.setImage("http://rlm.com.sa/RLMAPP/images/Our_Doctors_Images/"+jsonObject.getString("doctor_image"));
                                    arrayList.add(doctor);
                                }
                                adapter.notifyDataSetChanged();
                            }else {
                                binding.tvNoRecord.setVisibility(View.VISIBLE);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void Failed(String error) {
                        binding.swipeRefresh.setRefreshing(false);
                        binding.tvNoRecord.setVisibility(View.VISIBLE);
                        binding.loading.setVisibility(View.GONE);
                    }
                });
    }

    @Override
    public void onClickView(ModelDoctor doctor) {
        Bundle bundle=new Bundle();
        bundle.putSerializable("data",doctor);
        Navigation.findNavController(binding.getRoot()).navigate(R.id.action_details,bundle);
    }

    @Override
    public void onClickBookNow(ModelDoctor doctor) {
        Bundle bundle=new Bundle();
        bundle.putSerializable("data",doctor);
        Navigation.findNavController(binding.getRoot()).navigate(R.id.action_appointment,bundle);
    }

    @Override
    public void onClickCall(ModelDoctor doctor) {
        if (!doctor.getPhone().isEmpty()){
            Tools.MakeCall(getContext(),doctor.getPhone());
        }else {
            Toast.makeText(getContext(), R.string.num_not, Toast.LENGTH_SHORT).show();
        }
    }
}
