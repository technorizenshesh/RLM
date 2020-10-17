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
import com.rlm.Models.ModelDoctor;
import com.rlm.R;
import com.rlm.databinding.FragmentDoctorDetailsBinding;
import com.rlm.databinding.FragmentServiceDetailsBinding;
import com.squareup.picasso.Picasso;
import com.utils.Session.SessionManager;
import com.utils.Utils.Tools;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

import www.develpoeramit.mapicall.ApiCallBuilder;


/**
 * A simple {@link Fragment} subclass.
 */
public class FragmentDoctorDetails extends Fragment {
    private FragmentDoctorDetailsBinding binding;
    private ModelDoctor doctor;
    public FragmentDoctorDetails() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Tools.get().updateResources(getActivity());
        binding= DataBindingUtil.inflate(inflater, R.layout.fragment_doctor_details, container, false);
        BindView();
        return binding.getRoot();
    }

    private void BindView() {
        binding.loading.setGifImageResource(R.drawable.loading);
        if (getArguments()!=null){
            String lng=SessionManager.get(getContext()).getSelectedLanguage();
            doctor=(ModelDoctor)getArguments().getSerializable("data");
            binding.tvTitle.setText(lng.equals("ar")?doctor.getFull_name():doctor.getEnglish_name());
            binding.tvDescription.setText(doctor.getInfo());
            if (doctor.getAvg_rating().length()>0) {
                binding.rbRating.setRating(Float.parseFloat(doctor.getAvg_rating()));
            }
            Picasso.get().load(doctor.getImage()).placeholder(R.drawable.m_doc).into(binding.image);

        }
        binding.tvRate.setOnClickListener(v->{
            new FragmentRating().Callback(doctor.getFull_name(),this::setDoctorRating).show(getChildFragmentManager(),"");
        });
        binding.btnBook.setOnClickListener(v->{
            Bundle bundle=new Bundle();
            bundle.putSerializable("data",doctor);
            Navigation.findNavController(binding.getRoot()).navigate(R.id.action_appointment,bundle);
        });
    }

    private void setDoctorRating(float v, String s) {
        binding.loading.setVisibility(View.VISIBLE);
        HashMap<String,String> param=new HashMap<>();
        param.put("user_id", SessionManager.get(getContext()).getUserID());
        param.put("doctor_id",doctor.getId());
        param.put("rating",""+v);
        param.put("comment",""+s);
        ApiCallBuilder.build(getContext()).setUrl(BaseClass.get().addDoctorRating())
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
