package com.rlm.Dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;

import com.rlm.R;
import com.rlm.GifImageView;

public class ProgressDialog extends Dialog {
    public static ProgressDialog get(Context context) {
        return new ProgressDialog(context);
    }
    public ProgressDialog(@NonNull Context context) {
        super(context);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_progress);
        GifImageView gifImageView = (GifImageView) findViewById(R.id.GifImageView);
        gifImageView.setGifImageResource(R.drawable.loading);
        setCanceledOnTouchOutside(false);
    }
}
