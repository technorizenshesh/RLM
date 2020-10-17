package com.rlm.Fragments;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.rlm.Adapters.AdapterService;
import com.rlm.R;
import com.rlm.databinding.FragmentOurServiceBinding;
import com.rlm.databinding.FragmentStatisticBinding;
import com.utils.Utils.Tools;


/**
 * A simple {@link Fragment} subclass.
 */
public class FragmentStatistics extends Fragment {
    private FragmentStatisticBinding binding;

    public FragmentStatistics() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Tools.get().updateResources(getActivity());
        binding= DataBindingUtil.inflate(inflater, R.layout.fragment_statistic, container, false);
        BindView();
        return binding.getRoot();
    }

    private void BindView() {
    }

    private void onClick() {
        Navigation.findNavController(binding.getRoot()).navigate(R.id.action_details);
    }

}
