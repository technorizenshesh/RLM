package com.rlm.Fragments;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.rlm.Constant.BaseClass;
import com.rlm.Models.ModelDoctor;
import com.rlm.Models.ModelOffer;
import com.rlm.R;
import com.rlm.databinding.FragmentDoctorDetailsBinding;
import com.rlm.databinding.FragmentOfferDetailsBinding;
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
public class FragmentOfferDetails extends Fragment {
    private FragmentOfferDetailsBinding binding;
    private ModelOffer offer;

    public FragmentOfferDetails() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Tools.get().updateResources(getActivity());
        binding= DataBindingUtil.inflate(inflater, R.layout.fragment_offer_details, container, false);
        BindView();
        return binding.getRoot();
    }

    private void BindView() {
        if (getArguments()!=null){
            offer=(ModelOffer)getArguments().getSerializable("data");
            Picasso.get().load(offer.getImage()).into(binding.image);
        }
      binding.imgBack.setOnClickListener(v->{
          Navigation.findNavController(binding.getRoot()).popBackStack();
      });
    }


}
