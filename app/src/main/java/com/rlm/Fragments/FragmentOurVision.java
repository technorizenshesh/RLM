package com.rlm.Fragments;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import com.rlm.Models.ModelAboutUs;
import com.rlm.R;
import com.rlm.databinding.FragmentOurVisionBinding;
import com.rlm.databinding.FragmentWhoWeAreBinding;
import com.squareup.picasso.Picasso;
import com.utils.Utils.Tools;


/**
 * A simple {@link Fragment} subclass.
 */
public class FragmentOurVision extends Fragment {
    private ModelAboutUs aboutUs;
    private FragmentOurVisionBinding binding;

    public FragmentOurVision() {

    }
    public FragmentOurVision(ModelAboutUs aboutUs) {
        this.aboutUs=aboutUs;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Tools.get().updateResources(getActivity());
        binding= DataBindingUtil.inflate(inflater, R.layout.fragment_our_vision, container, false);
        BindView();
        return binding.getRoot();
    }

    private void BindView() {
        if (!aboutUs.getOur_Vision_Image().equals("coming soon")){
            Picasso.get().load(aboutUs.getOur_Vision_Image()).placeholder(R.drawable.image).into(binding.image);
        }
        binding.tvTitle.setText(aboutUs.getOur_Vision());
        binding.tvDescription.setText(aboutUs.getOur_Vision_Desc());
    }

}
