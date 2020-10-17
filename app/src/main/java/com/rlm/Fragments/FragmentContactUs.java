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
import com.rlm.Activities.NavigateAddressActivity;
import com.rlm.Adapters.AdapterDoctor;
import com.rlm.R;
import com.rlm.databinding.FragmentContactUsBinding;
import com.rlm.databinding.FragmentOurDoctorsBinding;
import com.utils.Utils.Tools;


/**
 * A simple {@link Fragment} subclass.
 */
public class FragmentContactUs extends Fragment {
    private FragmentContactUsBinding binding;

    public FragmentContactUs() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Tools.get().updateResources(getActivity());
        binding= DataBindingUtil.inflate(inflater, R.layout.fragment_contact_us, container, false);
        BindView();
        return binding.getRoot();
    }

    private void BindView() {
        binding.tvCall1.setOnClickListener(v->{
            Tools.MakeCall(getContext(),getString(R.string.mob_1));
        });
        binding.tvCall2.setOnClickListener(v->{
            Tools.MakeCall(getContext(),getString(R.string.mob_2));
        });
        binding.tvDirection1.setOnClickListener(v->{
            startActivity(new Intent(getActivity(), NavigateAddressActivity.class).putExtra("address","Aldayer Bani Malik, Main Road"));
        });
        binding.tvDirection2.setOnClickListener(v->{
            startActivity(new Intent(getActivity(), NavigateAddressActivity.class).putExtra("address","Abu Arish Prince Sultan Road,"));
        });
        binding.twitter.setOnClickListener(v->{
            Tools.get().OpenTwitter(getContext());
        });
        binding.facebook.setOnClickListener(v->{
            startActivity(Tools.get().getOpenFacebookIntent(getContext()));
        });
        binding.instagram.setOnClickListener(v->{
            Tools.get().OpenInstagram(getContext());
        });
        binding.layWebsite.setOnClickListener(v->{
            Tools.get().OpenWebPage(getContext());
        });
        binding.whatsup.setOnClickListener(v->{
            Tools.get().OpenWhatsApp(getContext());
        });
        binding.lyEmail.setOnClickListener(v->{
            Tools.get().OpenGmail(getContext());
        });
    }

}
