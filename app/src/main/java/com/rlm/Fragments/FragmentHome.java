package com.rlm.Fragments;


import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.core.app.NotificationCompat;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.rlm.Activities.HomeActivity;
import com.rlm.Dialog.ProgressDialog;
import com.rlm.R;
import com.rlm.databinding.FragmentHomeBinding;
import com.rlm.databinding.FragmentWhoWeAreBinding;
import com.utils.Utils.Tools;


/**
 * A simple {@link Fragment} subclass.
 */
public class FragmentHome extends Fragment {
    private FragmentHomeBinding binding;

    public FragmentHome() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Tools.get().updateResources(getActivity());
        binding= DataBindingUtil.inflate(inflater, R.layout.fragment_home, container, false);
        BindView();
        return binding.getRoot();
    }


    private void BindView() {
        NavController navControler = Navigation.findNavController(getActivity(),R.id.nav_host_home);
        binding.aboutUs.setOnClickListener(v->{
            navControler.navigate(R.id.action_about_us);
        });
        binding.ourService.setOnClickListener(v->{
            navControler.navigate(R.id.action_service);
        });
        binding.ourDoctors.setOnClickListener(v->{
            navControler.navigate(R.id.action_doctor);
        });
        binding.contactUs.setOnClickListener(v->{
            navControler.navigate(R.id.action_contact_us);
        });
        binding.appointment.setOnClickListener(v->{
            navControler.navigate(R.id.action_appointment);
        });
        binding.ourOffer.setOnClickListener(v->{
            navControler.navigate(R.id.action_offer);
        });
    }

}
