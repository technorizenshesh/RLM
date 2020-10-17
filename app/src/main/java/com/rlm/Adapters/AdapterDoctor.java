package com.rlm.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.rlm.Interfaces.onClickDoctorListener;
import com.rlm.Models.ModelDoctor;
import com.rlm.R;
import com.squareup.picasso.Picasso;
import com.utils.Session.SessionManager;

import java.util.ArrayList;

public class AdapterDoctor extends RecyclerView.Adapter<AdapterDoctor.mViewHolder> {
    private ArrayList<ModelDoctor> data;
    Context mContext;
    private onClickDoctorListener listener;

    public AdapterDoctor(Context mContext, ArrayList<ModelDoctor> doctors) {
        this.mContext = mContext;
        this.data = doctors;
    }

    public AdapterDoctor Callback(onClickDoctorListener listener) {
        this.listener = listener;
        return this;
    }

    @NonNull
    @Override
    public mViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.layout_doctor, parent, false);
        return new mViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull mViewHolder holder, int position) {
        holder.tv_more.setOnClickListener(v -> listener.onClickView(data.get(position)));
        holder.tv_book.setOnClickListener(v -> listener.onClickBookNow(data.get(position)));
        String lng = SessionManager.get(mContext).getSelectedLanguage();
        holder.tv_title.setText(lng.equals("ar") ? data.get(position).getFull_name() : data.get(position).getEnglish_name());
        holder.tv_rating.setRating(Float.valueOf(data.get(position).getAvg_rating()));
        Picasso.get().load(data.get(position).getImage()).placeholder(R.drawable.m_doc).into(holder.image);
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    class mViewHolder extends RecyclerView.ViewHolder {
        TextView tv_title, tv_book, tv_more;
        RatingBar tv_rating;
        ImageView image;

        public mViewHolder(@NonNull View itemView) {
            super(itemView);
            tv_title = itemView.findViewById(R.id.tv_title);
            tv_book = itemView.findViewById(R.id.tv_book);
            tv_more = itemView.findViewById(R.id.tv_more);
            tv_rating = itemView.findViewById(R.id.tv_rating);
            image = itemView.findViewById(R.id.image);
        }
    }
}
