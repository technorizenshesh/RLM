package com.rlm.Fragments;


import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.rlm.Activities.HomeActivity;
import com.rlm.Interfaces.onRatingListener;
import com.rlm.R;
import com.rlm.databinding.FragmentRatingBinding;
import com.rlm.databinding.FragmentSuccessBinding;
import com.utils.Utils.Tools;


/**
 * A simple {@link Fragment} subclass.
 */
public class FragmentRating extends BottomSheetDialogFragment {
    private FragmentRatingBinding binding;
    private String title;
    private onRatingListener listener;

    public FragmentRating() {

    }
    public FragmentRating Callback(String title, onRatingListener listener) {
        this.title=title;
        this.listener=listener;
        return this;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Tools.get().updateResources(getActivity());
        BottomSheetDialog dialog=(BottomSheetDialog)super.onCreateDialog(savedInstanceState);
        binding= DataBindingUtil.inflate(LayoutInflater.from(getContext()), R.layout.fragment_rating, null, false);
        dialog.setContentView(binding.getRoot());
        BottomSheetBehavior behavior=BottomSheetBehavior.from((View)binding.getRoot().getParent());
        behavior.setState(BottomSheetBehavior.STATE_EXPANDED);
        BindView();
        return dialog;
    }


    private void BindView() {
        binding.tvTitle.setText(title);
        binding.btnSubmit.setOnClickListener(v->{
            if (binding.etComment.getText().toString().isEmpty()){
                binding.etComment.setError(getString(R.string.required));
                binding.etComment.requestFocus();
                return;
            }
            listener.Success(binding.rbRating.getRating(),binding.etComment.getText().toString());
            dismiss();
        });
        binding.imgClose.setOnClickListener(v->dismiss());
    }


}
