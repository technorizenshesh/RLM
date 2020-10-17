package com.rlm.Fragments;


import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Toast;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.rlm.Activities.FirstActivity;
import com.rlm.Activities.HomeActivity;
import com.rlm.Adapters.AdapterService;
import com.rlm.Adapters.SpinnerAdapter;
import com.rlm.Constant.BaseClass;
import com.rlm.Models.ModelDoctor;
import com.rlm.Models.ModelSpinner;
import com.rlm.R;
import com.rlm.databinding.FragmentAppointmentPartOneBinding;
import com.rlm.databinding.FragmentOurServiceBinding;
import com.utils.Session.SessionManager;
import com.utils.Utils.Tools;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import www.develpoeramit.mapicall.ApiCallBuilder;


/**
 * A simple {@link Fragment} subclass.
 */
public class FragmentAppointmentPartOne extends Fragment {
    private FragmentAppointmentPartOneBinding binding;
    private ArrayList<ModelSpinner>docList=new ArrayList<>();
    private ArrayList<ModelSpinner>BranchList=new ArrayList<>();
    private ArrayList<ModelSpinner>SectionList=new ArrayList<>();
    private SpinnerAdapter spDocAdapter,spBranchAdapter,spSectionAdapter;
    private ModelDoctor doctor;

    public FragmentAppointmentPartOne() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Tools.get().updateResources(getActivity());
        binding= DataBindingUtil.inflate(inflater, R.layout.fragment_appointment_part_one, container, false);
        Tools.get().updateView(getActivity(),binding.getRoot());
        BindView();
        return binding.getRoot();
    }

    private void BindView() {
        binding.loading.setGifImageResource(R.drawable.loading);
        BranchList.add(new ModelSpinner("0",getString(R.string.select_branch)));
        SectionList.add(new ModelSpinner("0",getString(R.string.select_section)));
        docList.add(new ModelSpinner("0",getString(R.string.select_doctor)));
        if (getArguments()!=null){
            doctor=(ModelDoctor)getArguments().getSerializable("data");
        }
        binding.btnNext.setOnClickListener(v->{
            if (Validation()) {
                Bundle bundle = new Bundle();
                bundle.putSerializable("param", getParam());
                Navigation.findNavController(binding.getRoot()).navigate(R.id.action_appointment_next, bundle);
            }});
        spDocAdapter=new SpinnerAdapter(getContext(),R.layout.layout_spinner,R.id.tv_title,docList);
        spBranchAdapter=new SpinnerAdapter(getContext(),R.layout.layout_spinner,R.id.tv_title,BranchList);
        spSectionAdapter=new SpinnerAdapter(getContext(),R.layout.layout_spinner,R.id.tv_title,SectionList);
        binding.spDoctor.setAdapter(spDocAdapter);
        binding.spBranch.setAdapter(spBranchAdapter);
        binding.spSection.setAdapter(spSectionAdapter);
        binding.tvDate.setOnClickListener(v->Tools.DatePicker(getContext(),binding.tvDate::setText));
        binding.tvStartTime.setOnClickListener(v->Tools.TimePicker(getContext(),binding.tvStartTime::setText,true,true));
        binding.tvEndDate.setOnClickListener(v->Tools.TimePicker(getContext(),binding.tvEndDate::setText,true,true));
        binding.spBranch.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                getSection(BranchList.get(position).getId());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        binding.spSection.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                getDoctors(SectionList.get(position).getId());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
        getBranch();
    }



    private void getDoctors(String id){
        binding.loading.setVisibility(View.VISIBLE);
        HashMap<String,String>map=new HashMap();
        map.put("service_id",id);
        String lng= SessionManager.get(getContext()).getSelectedLanguage();
        ApiCallBuilder.build(getActivity()).setUrl(BaseClass.get().getDoctorByService())
                .isShowProgressBar(false).setParam(map)
                .execute(new ApiCallBuilder.onResponse() {
                    @Override
                    public void Success(String response) {
                        binding.loading.setVisibility(View.GONE);
                        Log.e("DoctorList","=====>"+response);
                        try {
                            JSONObject object=new JSONObject(response);
                            boolean status=object.getString("status").contains("1");
                            if (status){
                                docList.clear();
                                docList.add(new ModelSpinner("0",getString(R.string.select_doctor)));
                                JSONArray array=object.getJSONArray("result");
                                int docSelectedPos=0;
                                for (int i=0;i<array.length();i++){
                                    JSONObject jsonObject=array.getJSONObject(i);
                                    docList.add(new ModelSpinner(jsonObject.getString("id"),lng.equals("ar")?jsonObject.getString("full_name"):jsonObject.getString("english_name")));
                               if (doctor!=null)
                                if (jsonObject.getString("id").equals(doctor.getId())){
                                    docSelectedPos=i;
                                }
                                }
                                spDocAdapter.notifyDataSetChanged();
                                binding.spDoctor.setSelection(docSelectedPos);
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
    private void getBranch(){
        binding.loading.setVisibility(View.VISIBLE);
        String lng= SessionManager.get(getContext()).getSelectedLanguage();
        ApiCallBuilder.build(getActivity()).setUrl(BaseClass.get().getBranchList())
                .isShowProgressBar(false)
                .execute(new ApiCallBuilder.onResponse() {
                    @Override
                    public void Success(String response) {
                        binding.loading.setVisibility(View.GONE);
                        try {
                            JSONObject object=new JSONObject(response);
                            boolean status=object.getString("status").contains("1");
                            if (status){
                                BranchList.clear();
                                BranchList.add(new ModelSpinner("0",getString(R.string.select_branch)));
                                JSONArray array=object.getJSONArray("result");
                                for (int i=0;i<array.length();i++){
                                    JSONObject jsonObject=array.getJSONObject(i);
                                    BranchList.add(new ModelSpinner(jsonObject.getString("id"),lng.equals("ar")?jsonObject.getString("name"):jsonObject.getString("english_name")));
                                }
                                spBranchAdapter.notifyDataSetChanged();
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
    private void getSection(String id){
        binding.loading.setVisibility(View.VISIBLE);
        HashMap<String,String>map=new HashMap();
        map.put("branch_id",id);
        String lng=SessionManager.get(getContext()).getSelectedLanguage();
        ApiCallBuilder.build(getActivity()).setUrl(BaseClass.get().getSection())
                .setParam(map)
                .isShowProgressBar(false)
                .execute(new ApiCallBuilder.onResponse() {
                    @Override
                    public void Success(String response) {
                        binding.loading.setVisibility(View.GONE);
                        try {
                            JSONObject object=new JSONObject(response);
                            boolean status=object.getString("status").contains("1");
                            if (status){
                                SectionList.clear();
                                SectionList.add(new ModelSpinner("0",getString(R.string.select_section)));
                                JSONArray array=object.getJSONArray("result");
                                for (int i=0;i<array.length();i++){
                                    JSONObject jsonObject=array.getJSONObject(i);
                                    SectionList.add(new ModelSpinner(jsonObject.getString("id"),lng.equals("ar")?jsonObject.getString("title"):jsonObject.getString("english_name")));
                                }
                                spSectionAdapter.notifyDataSetChanged();
                                binding.spSection.setSelection(0);
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
    public HashMap<String,String>getParam(){
        HashMap<String,String>param=new HashMap<>();
        param.put("user_id", SessionManager.get(getContext()).getUserID());
        param.put("section_id", SectionList.get(binding.spSection.getSelectedItemPosition()).getId());
        param.put("branch_id", BranchList.get(binding.spBranch.getSelectedItemPosition()).getId());
        param.put("doctor_id", docList.get(binding.spDoctor.getSelectedItemPosition()).getId());
        param.put("date",binding.tvDate.getText().toString());
        param.put("start_time",binding.tvStartTime.getText().toString());
        param.put("end_time",binding.tvEndDate.getText().toString());
        param.put("day","");
        return param;
    }

    private boolean Validation(){
        if(binding.tvDate.getText().toString().isEmpty()){
            Toast.makeText(getContext(), "Select Date", Toast.LENGTH_SHORT).show();
            return false;
        }if(binding.tvStartTime.getText().toString().isEmpty()){
            Toast.makeText(getContext(), "Select Start Time", Toast.LENGTH_SHORT).show();
            return false;
        }if(binding.tvEndDate.getText().toString().isEmpty()){
            Toast.makeText(getContext(), "Select End Time", Toast.LENGTH_SHORT).show();
            return false;
        }if(binding.spBranch.getSelectedItemPosition()==0){
            Toast.makeText(getContext(), getString(R.string.select_branch), Toast.LENGTH_SHORT).show();
            return false;
        }if(binding.spSection.getSelectedItemPosition()==0){
            Toast.makeText(getContext(), getString(R.string.select_section), Toast.LENGTH_SHORT).show();
            return false;
        }if(binding.spDoctor.getSelectedItemPosition()==0){
            Toast.makeText(getContext(), getString(R.string.select_doctor), Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }
}
