package com.rlm.Fragments;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import com.rlm.Models.ModelAboutUs;
import com.rlm.R;
import com.rlm.databinding.FragmentOurMissionBinding;
import com.rlm.databinding.FragmentOurVisionBinding;
import com.squareup.picasso.Picasso;
import com.utils.Utils.Tools;


/**
 * A simple {@link Fragment} subclass.
 */
public class FragmentOurMission extends Fragment {
    private ModelAboutUs aboutUs;
    private FragmentOurMissionBinding binding;

    public FragmentOurMission() {

    }
    public FragmentOurMission(ModelAboutUs aboutUs) {
        this.aboutUs=aboutUs;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Tools.get().updateResources(getActivity());
        binding= DataBindingUtil.inflate(inflater, R.layout.fragment_our_mission, container, false);
        BindView();
        return binding.getRoot();
    }

    private void BindView() {
        if (!aboutUs.getOur_Mission_Image().equals("coming soon")){
            Picasso.get().load(aboutUs.getOur_Mission_Image()).placeholder(R.drawable.img_05).into(binding.image);
        }
        binding.tvTitle.setText(aboutUs.getOur_Mission());
        binding.tvDescription.setText(aboutUs.getOur_Mission_Desc());
    }

}
