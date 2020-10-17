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
import com.rlm.Models.ModelAboutUs;
import com.rlm.R;
import com.rlm.databinding.FragmentVerifyMobileBinding;
import com.rlm.databinding.FragmentWhoWeAreBinding;
import com.squareup.picasso.Picasso;
import com.utils.Utils.Tools;


/**
 * A simple {@link Fragment} subclass.
 */
public class FragmentWhoWeAre extends Fragment {
    private ModelAboutUs aboutUs;
    private FragmentWhoWeAreBinding binding;

    public FragmentWhoWeAre() {

    } public FragmentWhoWeAre(ModelAboutUs aboutUs) {
        this.aboutUs=aboutUs;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Tools.get().updateResources(getActivity());
        binding= DataBindingUtil.inflate(inflater, R.layout.fragment_who_we_are, container, false);
        BindView();
        return binding.getRoot();
    }

    private void BindView() {
        if (!aboutUs.getWho_We_Are_image().equals("coming soon")){
            Picasso.get().load(aboutUs.getWho_We_Are_image()).placeholder(R.drawable.image).into(binding.image);
        }
        binding.tvTitle.setText(aboutUs.getWho_We_Are());
        binding.tvDescription.setText(aboutUs.getWho_We_Are_desc());
    }

}
