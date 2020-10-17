package com.rlm.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.rlm.Interfaces.onClickDoctorListener;
import com.rlm.Interfaces.onClickOfferListener;
import com.rlm.Models.ModelDoctor;
import com.rlm.Models.ModelOffer;
import com.rlm.R;

import java.util.ArrayList;

public class AdapterOffers extends RecyclerView.Adapter<AdapterOffers.mViewHolder> {
    private ArrayList<ModelOffer> data;
    Context mContext;
    private onClickOfferListener listener;

    public AdapterOffers(Context mContext, ArrayList<ModelOffer>doctors) {
        this.mContext = mContext;
        this.data=doctors;
    }
    public AdapterOffers Callback(onClickOfferListener listener){
        this.listener=listener;
        return this;
    }

    @NonNull
    @Override
    public mViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(mContext).inflate(R.layout.layout_offer,parent,false);
        return new mViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull mViewHolder holder, int position) {
        holder.tv_view.setOnClickListener(v->listener.onClick(data.get(position)));
        holder.tv_title.setText(data.get(position).getTitle());
        holder.tv_discount.setText(data.get(position).getDiscount());
        holder.tv_description.setText(data.get(position).getDescription());
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    class mViewHolder extends RecyclerView.ViewHolder{
        TextView tv_title,tv_discount,tv_description,tv_view;
        public mViewHolder(@NonNull View itemView) {
            super(itemView);
            tv_title=itemView.findViewById(R.id.tv_title);
            tv_discount=itemView.findViewById(R.id.tv_discount);
            tv_description=itemView.findViewById(R.id.tv_description);
            tv_view=itemView.findViewById(R.id.tv_view);
        }
    }
}
