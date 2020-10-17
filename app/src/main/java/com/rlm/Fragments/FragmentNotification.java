package com.rlm.Fragments;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.rlm.Adapters.AdapterNotification;
import com.rlm.Adapters.AdapterOffers;
import com.rlm.Constant.BaseClass;
import com.rlm.Models.ModelNotification;
import com.rlm.Models.ModelOffer;
import com.rlm.R;
import com.rlm.databinding.FragmentNotificationBinding;
import com.rlm.databinding.FragmentOurOfferBinding;
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
public class FragmentNotification extends Fragment {
    private FragmentNotificationBinding binding;
    private ArrayList<ModelNotification>notifications=new ArrayList<>();
    private AdapterNotification adapter;

    public FragmentNotification() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Tools.get().updateResources(getActivity());
        binding= DataBindingUtil.inflate(inflater, R.layout.fragment_notification, container, false);
        BindView();
        return binding.getRoot();
    }

    private void BindView() {
        binding.loading.setGifImageResource(R.drawable.loading);
        adapter=new AdapterNotification(getContext(),notifications).Callback(this::onDeleteNotification);
        binding.recycleView.setAdapter(adapter);
        binding.swipeRefresh.setOnRefreshListener(this::getNotification);
        getNotification();
    }

    private void onDeleteNotification(int pos) {
        binding.swipeRefresh.setRefreshing(true);
        binding.loading.setVisibility(View.VISIBLE);
        HashMap<String,String>map=new HashMap<>();
        map.put("id", notifications.get(pos).getId());
        ApiCallBuilder.build(getContext()).setUrl(BaseClass.get().DeleteNotification())
                .setParam(map)
                .execute(new ApiCallBuilder.onResponse() {
                    @Override
                    public void Success(String response) {
                        binding.loading.setVisibility(View.GONE);
                        binding.swipeRefresh.setRefreshing(false);
                        try {
                            JSONObject object=new JSONObject(response);
                            boolean status=object.getString("status").contains("1");
                            if (status){
                                notifications.remove(pos);
                                adapter.notifyDataSetChanged();
                            }
                          binding.tvNoRecord.setVisibility(notifications.size()==0?View.VISIBLE:View.GONE);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void Failed(String error) {
                        binding.swipeRefresh.setRefreshing(false);
                        binding.loading.setVisibility(View.GONE);
                    }
                });
    }

    private void getNotification(){
        binding.loading.setVisibility(View.VISIBLE);
        boolean isArabic=SessionManager.get(getContext()).getSelectedLanguage().equals("ar");
        HashMap<String,String>map=new HashMap<>();
        map.put("user_id", SessionManager.get(getContext()).getUserID());
        ApiCallBuilder.build(getContext()).setUrl(BaseClass.get().getNotificationList())
                .setParam(map)
                .execute(new ApiCallBuilder.onResponse() {
                    @Override
                    public void Success(String response) {
                        binding.swipeRefresh.setRefreshing(false);
                        binding.loading.setVisibility(View.GONE);
                        try {
                            JSONObject object=new JSONObject(response);
                            boolean status=object.getString("status").contains("1");
                            if (status){
                                binding.tvNoRecord.setVisibility(View.GONE);
                                notifications.clear();
                                JSONArray  array=object.getJSONArray("result");
                                for ( int i=0;i<array.length();i++){
                                    JSONObject jsonObject=array.getJSONObject(i);
                                    ModelNotification notification=new ModelNotification();
                                    notification.setId(jsonObject.getString("id"));
                                    notification.setMessage(isArabic?"تم حجز موعدك بنجاح":jsonObject.getString("message"));
                                    notification.setDate_time(jsonObject.getString("appointment_date"));
                                    notifications.add(notification);
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
                        binding.loading.setVisibility(View.GONE);
                        binding.swipeRefresh.setRefreshing(false);
                        binding.tvNoRecord.setText(error);
                        binding.tvNoRecord.setVisibility(View.VISIBLE);
                    }
                });
    }



}
