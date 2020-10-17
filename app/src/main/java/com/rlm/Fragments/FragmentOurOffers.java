package com.rlm.Fragments;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.rlm.Adapters.AdapterDoctor;
import com.rlm.Adapters.AdapterOffers;
import com.rlm.Constant.BaseClass;
import com.rlm.Models.ModelDoctor;
import com.rlm.Models.ModelOffer;
import com.rlm.R;
import com.rlm.databinding.FragmentOurDoctorsBinding;
import com.rlm.databinding.FragmentOurOfferBinding;
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
public class FragmentOurOffers extends Fragment {
    private FragmentOurOfferBinding binding;
    private AdapterOffers adapter;
    private ArrayList<ModelOffer>offers=new ArrayList<>();
    public FragmentOurOffers() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Tools.get().updateResources(getActivity());
        binding= DataBindingUtil.inflate(inflater, R.layout.fragment_our_offer, container, false);
        BindView();
        return binding.getRoot();
    }

    private void BindView() {
        binding.loading.setGifImageResource(R.drawable.loading);
        adapter=new AdapterOffers(getContext(),offers).Callback(this::onClick);
        binding.recycleView.setAdapter(adapter);
        binding.swipeRefresh.setOnRefreshListener(this::getOffer);
        getOffer();
    }

    private void onClick(ModelOffer offer) {
        Bundle bundle=new Bundle();
        bundle.putSerializable("data",offer);
        Navigation.findNavController(binding.getRoot()).navigate(R.id.action_details,bundle);
    }
    private void getOffer(){
        binding.loading.setVisibility(View.VISIBLE);
        boolean isArabic= SessionManager.get(getContext()).getSelectedLanguage().equals("ar");
        ApiCallBuilder.build(getContext()).setUrl(BaseClass.get().getOfferList())
              .execute(new ApiCallBuilder.onResponse() {
            @Override
            public void Success(String response) {
                binding.loading.setVisibility(View.GONE);
                binding.swipeRefresh.setRefreshing(false);
                try {
                    JSONObject object=new JSONObject(response);
                    boolean status=object.getString("status").contains("1");
                    offers.clear();
                    if (status){
                        JSONArray array=object.getJSONArray("result");
                        for (int i=0;i<array.length();i++){
                            JSONObject jsonObject=array.getJSONObject(i);
                            ModelOffer offer=new ModelOffer();
                            offer.setTitle(isArabic?jsonObject.getString("OfferName_AR"):jsonObject.getString("OfferName_ER"));
                            offer.setDescription(isArabic?jsonObject.getString("Offer_Desc_AR"):jsonObject.getString("Offer_Desc_EN"));
                            offer.setDiscount(jsonObject.getString("Offer_Discount"));
                            offer.setImage("http://rlm.com.sa/RLMAPP/images/Our_Offers_Images/"+jsonObject.getString("Offer_Image_EN"));
                            offers.add(offer);
                        }
                        adapter.notifyDataSetChanged();
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
