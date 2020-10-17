package com.rlm.Fragments;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.rlm.Activities.FirstActivity;
import com.rlm.Activities.HomeActivity;
import com.rlm.Adapters.AdapterService;
import com.rlm.Constant.BaseClass;
import com.rlm.Interfaces.onClickServiceListener;
import com.rlm.Models.ModelService;
import com.rlm.R;
import com.rlm.databinding.FragmentOurMissionBinding;
import com.rlm.databinding.FragmentOurServiceBinding;
import com.utils.Session.SessionManager;
import com.utils.Utils.Tools;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import www.develpoeramit.mapicall.ApiCallBuilder;


/**
 * A simple {@link Fragment} subclass.
 */
public class FragmentOurService extends Fragment implements onClickServiceListener {
    private FragmentOurServiceBinding binding;
    private ArrayList<ModelService>arrayList=new ArrayList<>();
    private AdapterService adapter;

    public FragmentOurService() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Tools.get().updateResources(getActivity());
        binding= DataBindingUtil.inflate(inflater, R.layout.fragment_our_service, container, false);
        BindView();
        return binding.getRoot();
    }

    private void BindView() {
        binding.loading.setGifImageResource(R.drawable.loading);
        adapter=new AdapterService(getContext(),arrayList).Callback(this);
        binding.recycleView.setAdapter(adapter);
        binding.swipeRefresh.setOnRefreshListener(this::getService);
        getService();
    }

    private void getService(){
        binding.loading.setVisibility(View.VISIBLE);
        boolean isArabic= SessionManager.get(getContext()).getSelectedLanguage().equals("ar");
        ApiCallBuilder.build(getActivity()).setUrl(BaseClass.get().getService())
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
                                    ModelService service=new ModelService();
                                    service.setId(jsonObject.getString("id"));
                                    service.setTitle(isArabic?jsonObject.getString("ServiceNameAR"):jsonObject.getString("ServiceNameEN"));
                                    service.setInfo(isArabic?jsonObject.getString("ServiceDescAR"):jsonObject.getString("ServiceDescEn"));
                                    service.setAvg_rating(jsonObject.getString("avg_rating"));
                                    service.setImage("http://rlm.com.sa/RLMAPP/images/Our_Services_images/"+jsonObject.getString("ServiceLogo"));
                                    arrayList.add(service);
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
    public void onClickView(ModelService service) {
        Bundle bundle=new Bundle();
        bundle.putSerializable("data",service);
        Navigation.findNavController(binding.getRoot()).navigate(R.id.action_details,bundle);
    }

    @Override
    public void onClickbook(ModelService service) {
        Navigation.findNavController(binding.getRoot()).navigate(R.id.action_appointment);
    }
}
