package com.rlm.Fragments;


import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import com.rlm.Activities.FirstActivity;
import com.rlm.Activities.HomeActivity;
import com.rlm.Adapters.MyFragmentPagerAdapter;
import com.rlm.Constant.BaseClass;
import com.rlm.Models.ModelAboutUs;
import com.rlm.Models.ModelFragmentPager;
import com.rlm.R;
import com.rlm.databinding.FragmentAboutUsBinding;
import com.rlm.databinding.FragmentVerifyMobileBinding;
import com.utils.Session.SessionManager;
import com.utils.Utils.Tools;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import www.develpoeramit.mapicall.ApiCallBuilder;


/**
 * A simple {@link Fragment} subclass.
 */
public class FragmentAboutUs extends Fragment {
    private FragmentAboutUsBinding binding;
    ModelAboutUs aboutUs=new ModelAboutUs();
    List<ModelFragmentPager>pagerList=new ArrayList<>();
    public FragmentAboutUs() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Tools.get().updateResources(getActivity());
        binding= DataBindingUtil.inflate(inflater, R.layout.fragment_about_us, container, false);
        BindView();
        return binding.getRoot();
    }

    private void BindView() {
        binding.loading.setGifImageResource(R.drawable.loading);
        pagerList.add(new ModelFragmentPager(getString(R.string.who_we_are),new FragmentWhoWeAre(aboutUs)));
        pagerList.add(new ModelFragmentPager(getString(R.string.our_vision),new FragmentOurVision(aboutUs)));
        pagerList.add(new ModelFragmentPager(getString(R.string.our_mission),new FragmentOurMission(aboutUs)));
        getAboutUs();
    }
    private void getAboutUs(){
        boolean isArabic= SessionManager.get(getContext()).getSelectedLanguage().equals("ar");
        ApiCallBuilder.build(getContext()).setUrl(BaseClass.get().getAboutUs())
                .isShowProgressBar(false)
                .execute(new ApiCallBuilder.onResponse() {
                    @Override
                    public void Success(String response) {
                        binding.loading.setVisibility(View.GONE);
                        try {
                            JSONObject object=new JSONObject(response);
                            JSONObject jsonObject=object.getJSONArray("result").getJSONObject(0);
                            aboutUs.setWho_We_Are(isArabic?jsonObject.getString("Who_We_Are_AR"):jsonObject.getString("Who_We_Are_EN"));
                            aboutUs.setWho_We_Are_desc(isArabic?jsonObject.getString("Who_We_Are_desc_AR"):jsonObject.getString("Who_We_Are_desc_EN"));
                            aboutUs.setOur_Vision(isArabic?jsonObject.getString("Our_Vision_AR"):jsonObject.getString("Our_Vision_EN"));
                            aboutUs.setOur_Vision_Desc(isArabic?jsonObject.getString("Our_Vision_Desc_AR"):jsonObject.getString("Our_Vision_Desc_EN"));
                            aboutUs.setOur_Mission(isArabic?jsonObject.getString("Our_Mission_AR"):jsonObject.getString("Our_Mission_EN"));
                            aboutUs.setOur_Mission_Desc(isArabic?jsonObject.getString("Our_Mission_Desc_AR"):jsonObject.getString("Our_Mission_Desc_EN"));
                            aboutUs.setWho_We_Are_image("http://rlm.com.sa/RLMAPP/images/Abou_Us_Images/"+jsonObject.getString("Who_We_Are_image"));
                            aboutUs.setOur_Vision_Image("http://rlm.com.sa/RLMAPP/images/Abou_Us_Images/"+jsonObject.getString("Our_Vision_Image"));
                            aboutUs.setOur_Mission_Image("http://rlm.com.sa/RLMAPP/images/Abou_Us_Images/"+jsonObject.getString("Our_Mission_Image"));
                            binding.pager.setAdapter(new MyFragmentPagerAdapter(getChildFragmentManager(),pagerList));
                            binding.tab.setupWithViewPager(binding.pager);
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
