package com.rlm.Fragments;


import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.rlm.Activities.FirstActivity;
import com.rlm.Activities.HomeActivity;
import com.rlm.Adapters.AdapterTime;
import com.rlm.Constant.BaseClass;
import com.rlm.Interfaces.onTimeSelectListener;
import com.rlm.Models.ModelTime;
import com.rlm.R;
import com.rlm.databinding.FragmentAppointmentPartOneBinding;
import com.rlm.databinding.FragmentAppointmentPartTwoBinding;
import com.utils.Session.SessionManager;
import com.utils.Utils.Tools;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import www.develpoeramit.mapicall.ApiCallBuilder;


/**
 * A simple {@link Fragment} subclass.
 */
public class FragmentAppointmentPartTwo extends Fragment  {
    private FragmentAppointmentPartTwoBinding binding;
    private HashMap<String, String> param;
    HashMap<String,String>map=new HashMap<>();
    private ArrayList<ModelTime>Times=new ArrayList<>();
    private ArrayList<ModelTime>Times2=new ArrayList<>();
    private AdapterTime adapterTime1,adapterTime2;
    private String date1,date2;
    long StartBreak=0,EndBreak=0,StartBreak2=0,EndBreak2=0;
    private int hors=0;
    private boolean isTimeSelected=false;

    public FragmentAppointmentPartTwo() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Tools.get().updateResources(getActivity());
        binding= DataBindingUtil.inflate(inflater, R.layout.fragment_appointment_part_two, container, false);
        BindView();
        return binding.getRoot();
    }

    private void BindView() {
        binding.loading.setGifImageResource(R.drawable.loading);
        param= (HashMap<String,String>)getArguments().getSerializable("param");
        date1=Tools.ChangeDateFormat("yyyy-MM-dd",param.get("date"));
        date2=Tools.get().addDate(param.get("date"),1);
        binding.tvDate.setText(date1);
        binding.tvDate2.setText(date2);
        binding.btnNext.setOnClickListener(v->{
            if (binding.btnNext.getText().toString().equalsIgnoreCase(getString(R.string.back))){
                Navigation.findNavController(binding.getRoot()).popBackStack();
            }else {
                if (isTimeSelected) {
                    AddAppointment();
                }
            }
        });
        adapterTime1=new AdapterTime(getContext(),Times);
        adapterTime2=new AdapterTime(getContext(),Times2);
        binding.recycleViewTime1.setAdapter(adapterTime1);
        binding.recycleViewTime2.setAdapter(adapterTime2);
        adapterTime1.Callback(this::onSelectTimeOne);
        adapterTime2.Callback(this::onSelectTimeTwo);
        GetAvailableTime1();
    }

    private void onSelectTimeOne(int pos) {
        isTimeSelected=true;
        for (int i=0;i<Times.size();i++){
            Times.get(i).setSelected(false);
        }for (int i=0;i<Times2.size();i++){
            Times2.get(i).setSelected(false);
        }
        Times.get(pos).setSelected(true);
        long endTime=Tools.get().getTimeInMillSec("hh:mm a",Times.get(pos).getTime())+hors;
        param.put("start_date",binding.tvDate.getText().toString()+" "+Times.get(pos).getTime());
        param.put("end_date",binding.tvDate.getText().toString()+" "+Tools.get().getMilliToTime(endTime));
        adapterTime1.notifyDataSetChanged();
        adapterTime2.notifyDataSetChanged();

    }
    private void onSelectTimeTwo(int pos) {
        isTimeSelected=true;
        for (int i=0;i<Times.size();i++){
            Times.get(i).setSelected(false);
        }for (int i=0;i<Times2.size();i++){
            Times2.get(i).setSelected(false);
        }
        Times2.get(pos).setSelected(true);
        long endTime=Tools.get().getTimeInMillSec("hh:mm a",Times2.get(pos).getTime())+hors;
        param.put("start_date",binding.tvDate2.getText().toString()+" "+Times2.get(pos).getTime());
        param.put("end_date",binding.tvDate2.getText().toString()+" "+Tools.get().getMilliToTime(endTime));
        adapterTime1.notifyDataSetChanged();
        adapterTime2.notifyDataSetChanged();

    }

    private void AddAppointment(){
        binding.loading.setVisibility(View.VISIBLE);
        map.put("user_id", SessionManager.get(getContext()).getUserID());
        map.put("section_id", param.get("section_id"));
        map.put("branch_id", param.get("branch_id"));
        map.put("doctor_id", param.get("doctor_id"));
        map.put("start_date", Tools.ChangeDateTimeFormat(param.get("start_date")));
        map.put("end_date", Tools.ChangeDateTimeFormat(param.get("end_date")));
        map.put("date", param.get("date"));
        map.put("day","");
        StringBuilder builder=new StringBuilder();
        builder.append(BaseClass.get().addAppointment()+"?");
        for (Map.Entry<String, String> entry : map.entrySet()) {
            builder.append(entry.getKey()+"="+entry.getValue()+"&");
        }
        Log.e("URL","====>"+builder);
        ApiCallBuilder.build(getActivity()).setUrl(BaseClass.get().addAppointment())
                .isShowProgressBar(false).setParam(map)
                .execute(new ApiCallBuilder.onResponse() {
                    @Override
                    public void Success(String response) {
                        binding.loading.setVisibility(View.GONE);
                        Log.e("AddAppointment","=======>"+response);
                        try {
                            JSONObject object=new JSONObject(response);
                            boolean status=object.getString("status").contains("1");
                            String message=object.getString("message");
                            if (status){
                                Bundle bundle=new Bundle();
                                bundle.putSerializable("data",param);
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
    private void GetAvailableTime1(){
        binding.loading.setVisibility(View.VISIBLE);
        int cd=Tools.get().getDay(binding.tvDate.getText().toString());
        HashMap<String,String>map=new HashMap();
        map.put("doctor_id",param.get("doctor_id"));
        map.put("start_date",binding.tvDate.getText().toString());
        Log.e("GetTime","====>"+BaseClass.get().getAvailableTime()+"?doctor_id="+param.get("doctor_id")+"&"+"start_date="+binding.tvDate.getText().toString());
        ApiCallBuilder.build(getActivity()).setUrl(BaseClass.get().getAvailableTime())
                .isShowProgressBar(false).setParam(map)
                .execute(new ApiCallBuilder.onResponse() {
                    @Override
                    public void Success(String response) {
                        binding.loading.setVisibility(View.GONE);
                        Log.e("AvailableTime","======>"+response);
                        try {
                            JSONObject object=new JSONObject(response);
                            boolean status=object.getString("status").contains("1");
                            String message=object.getString("message");
                            if (status){
                                JSONObject data=object.getJSONObject("result").getJSONObject("0");
                                JSONObject schedule_data=data.getJSONArray("schedule_data").getJSONObject(cd);
                                JSONArray holiday_data=data.getJSONArray("holiday_data");
                                if (data.getString("break_data").length()>1){
                                    JSONObject break_data=data.getJSONArray("break_data").getJSONObject(0);
                                     StartBreak=Tools.get().getTimeInMillSec(break_data.getString("start_time"));
                                     EndBreak=Tools.get().getTimeInMillSec(break_data.getString("end_time"));
                                }
                                JSONObject slot_data=data.getJSONArray("slot_data").getJSONObject(0);
                               int slot_duration_time=slot_data.getInt("slot_duration_time");
                                long hors=slot_duration_time*60*1000;
                               for (int i=0;i<holiday_data.length();i++){
                                    JSONObject jsonObject=holiday_data.getJSONObject(i);
                                    String date=jsonObject.getString("date");
                                    if (date1.equals(date)){
                                        binding.tvOff1.setVisibility(View.VISIBLE);
                                        return;
                                    }
                                }
                                String endTime=schedule_data.getString("end_time");
                                String dostartTime=schedule_data.getString("start_time");
                                long DoctorEndTime=Tools.get().getTimeInMillSec(endTime);
                                Log.e("DoctorStartTime","=====>"+dostartTime);
                                Log.e("DoctorEndTime","=====>"+endTime);
                                Log.e("startTime","=====>"+param.get("start_time"));
                                Log.e("EndTime","=====>"+param.get("end_time"));
                                long nextTime=0,UserStartTime=0;
                                Times.clear();
                                long UserEndTime=Tools.get().getTimeInMillSec(param.get("end_time"));
                                String appointmentdata=data.getString("appointment_data");
                                UserStartTime = Tools.get().getTimeInMillSec(param.get("start_time"));
                                    binding.tvOff1.setVisibility(View.GONE);
                                    do {
                                        ModelTime time = new ModelTime();
                                        if (nextTime == 0) {
                                            nextTime = Tools.get().getTimeInMillSec(dostartTime);
                                            time.setTime(Tools.get().getMilliToTime(nextTime));
                                            Log.e("startTime", "====>" + nextTime);
                                        } else {
                                            nextTime = nextTime + hors;
                                            time.setTime(Tools.get().getMilliToTime(nextTime));
                                            Log.e("nextTime", "====>" + Tools.get().getMilliToTime(nextTime));
                                        }
                                        time.setEnable(true);
                                        boolean isAdded=false;
                                        if (!appointmentdata.equals("")) {
                                            JSONArray appointment_data = data.getJSONArray("appointment_data");
                                            for (int i = 0; i < appointment_data.length(); i++) {
                                                JSONObject jsonObject = appointment_data.getJSONObject(i);
                                                String appointment_time = Tools.ChangeDateTimeFormat2(jsonObject.getString("start_date"));
                                                isAdded=Tools.get().getMilliToTime(nextTime).equals(appointment_time);
                                                if (isAdded){
                                                    break;
                                                }
                                            }
                                        }if (!isAdded){
                                            if (nextTime>=UserStartTime) {
                                                if (StartBreak > 0) {
                                                    if (nextTime < StartBreak) {
                                                        Times.add(time);
                                                    } else if (nextTime > EndBreak) {
                                                        Times.add(time);
                                                    }
                                                } else {
                                                    Times.add(time);
                                                }
                                            }

                                        }


                                    } while (nextTime < DoctorEndTime&&nextTime<UserEndTime);

                                    adapterTime1.notifyDataSetChanged();
                                    if (Times.size()==0){
                                        binding.tvOff1.setVisibility(View.VISIBLE);
                                    }
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            binding.tvOff1.setVisibility(View.VISIBLE);
                        }
                        GetAvailableTime2();
                    }

                    @Override
                    public void Failed(String error) {
                        binding.loading.setVisibility(View.GONE);
                    }
                });
    }
    private void GetAvailableTime2(){
        binding.loading.setVisibility(View.VISIBLE);
        int cd=Tools.get().getDay(binding.tvDate2.getText().toString());
        HashMap<String,String>map=new HashMap();
        map.put("doctor_id",param.get("doctor_id"));
        map.put("start_date",binding.tvDate2.getText().toString());
        ApiCallBuilder.build(getActivity()).setUrl(BaseClass.get().getAvailableTime())
                .isShowProgressBar(false).setParam(map)
                .execute(new ApiCallBuilder.onResponse() {
                    @Override
                    public void Success(String response) {
                        binding.loading.setVisibility(View.GONE);
                        Log.e("AvailableTime","======>"+response);
                        try {
                            JSONObject object=new JSONObject(response);
                            boolean status=object.getString("status").contains("1");
                            String message=object.getString("message");
                            if (status){
                                JSONObject data=object.getJSONObject("result").getJSONObject("0");
                                if (data.getString("break_data").length()>1){
                                    JSONObject break_data=data.getJSONArray("break_data").getJSONObject(0);
                                    StartBreak2=Tools.get().getTimeInMillSec(break_data.getString("start_time"));
                                    EndBreak2=Tools.get().getTimeInMillSec(break_data.getString("end_time"));
                                }
                                JSONObject slot_data=data.getJSONArray("slot_data").getJSONObject(0);
                                int slot_duration_time=slot_data.getInt("slot_duration_time");

                                JSONObject schedule_data=data.getJSONArray("schedule_data").getJSONObject(cd);
                                JSONArray holiday_data=data.getJSONArray("holiday_data");
                                for (int i=0;i<holiday_data.length();i++){
                                    JSONObject jsonObject=holiday_data.getJSONObject(i);
                                    String date=jsonObject.getString("date");
                                    if (date2.equals(date)){
                                        binding.tvOff2.setVisibility(View.VISIBLE);
                                        return;
                                    }
                                }
                                String endTime=schedule_data.getString("end_time");
                                long DoctorEndTime=Tools.get().getTimeInMillSec(endTime);
                                String dostartTime=schedule_data.getString("start_time");
                                hors=slot_duration_time*60*1000;
                                long nextTime=0;
                                Times2.clear();
                                long UserEndTime=Tools.get().getTimeInMillSec(param.get("end_time"));
                                long UserStartTime = Tools.get().getTimeInMillSec(param.get("start_time"));
                                String appointmentdata=data.getString("appointment_data");
                                    binding.tvOff2.setVisibility(View.GONE);
                                    do {
                                        ModelTime time = new ModelTime();
                                        if (nextTime == 0) {
                                            nextTime = Tools.get().getTimeInMillSec(dostartTime);
                                            time.setTime(Tools.get().getMilliToTime(nextTime));
                                            Log.e("startTime", "====>" + nextTime);
                                        } else {
                                            nextTime = nextTime + hors;
                                            time.setTime(Tools.get().getMilliToTime(nextTime));
                                            Log.e("nextTime", "====>" + Tools.get().getMilliToTime(nextTime));
                                        }
                                        time.setEnable(true);
                                        boolean isAdded=false;
                                        if (!appointmentdata.equals("")) {
                                            JSONArray appointment_data = data.getJSONArray("appointment_data");
                                            for (int i = 0; i < appointment_data.length(); i++) {
                                                JSONObject jsonObject = appointment_data.getJSONObject(i);
                                                String appointment_time = Tools.ChangeDateTimeFormat2(jsonObject.getString("start_date"));
                                                isAdded=Tools.get().getMilliToTime(nextTime).equals(appointment_time);
                                                if (isAdded){
                                                    break;
                                                }
                                            }
                                        }
                                        if (!isAdded){
                                            if (nextTime >= UserStartTime) {
                                                if (StartBreak2 > 0) {
                                                    if (nextTime < StartBreak2) {
                                                        Times2.add(time);
                                                    } else if (nextTime > EndBreak2) {
                                                        Times2.add(time);
                                                    }
                                                } else {
                                                    Times2.add(time);
                                                }
                                            }
                                        }


                                    } while (nextTime < DoctorEndTime&&nextTime<UserEndTime);
                                    adapterTime2.notifyDataSetChanged();
                                    if (Times2.size()==0){
                                        binding.tvOff2.setVisibility(View.VISIBLE);
                                        if (Times.size()==0) {
                                            binding.btnNext.setText(R.string.back);
                                        }
                                    }

                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            if (Times2.size()==0){
                                binding.tvOff2.setVisibility(View.VISIBLE);
                                if (Times.size()==0) {
                                    binding.btnNext.setText(R.string.back);
                                }
                            }
                        }
                    }

                    @Override
                    public void Failed(String error) {
                        binding.loading.setVisibility(View.GONE);
                    }
                });
    }

}
