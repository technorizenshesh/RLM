package com.rlm.Adapters;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.rlm.Interfaces.onClickServiceListener;
import com.rlm.Models.ModelService;
import com.rlm.R;
import com.squareup.picasso.Picasso;
import com.utils.Session.SessionManager;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class AdapterService extends RecyclerView.Adapter<AdapterService.mViewHolder> {
    private ArrayList<ModelService> data;
    Context mContext;
    private onClickServiceListener listener;

    public AdapterService(Context mContext, ArrayList<ModelService>services) {
        this.mContext = mContext;
        this.data=services;
    }
    public AdapterService Callback(onClickServiceListener listener){
        this.listener=listener;
        return this;
    }

    @NonNull
    @Override
    public mViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(mContext).inflate(R.layout.layout_service,parent,false);
        return new mViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull mViewHolder holder, int position) {
        holder.tv_more.setOnClickListener(v->listener.onClickView(data.get(position)));
        holder.tv_book.setOnClickListener(v->listener.onClickbook(data.get(position)));
        holder.tv_title.setText(data.get(position).getTitle());
        holder.tv_rating.setRating(Float.parseFloat(data.get(position).getAvg_rating()));
        if (!data.get(position).getImage().isEmpty()){
            Picasso.get().load(data.get(position).getImage()).into(holder.image);
        }
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    class mViewHolder extends RecyclerView.ViewHolder{
        TextView tv_title,tv_book,tv_more;
        RatingBar tv_rating;
        RelativeLayout rl_main;
        CircleImageView image;
        public mViewHolder(@NonNull View itemView) {
            super(itemView);
            tv_title=itemView.findViewById(R.id.tv_title);
            tv_rating=itemView.findViewById(R.id.tv_rating);
            rl_main=itemView.findViewById(R.id.rl_main);
            tv_book=itemView.findViewById(R.id.tv_book);
            image=itemView.findViewById(R.id.image);
            tv_more=itemView.findViewById(R.id.tv_more);
        }
    }
}
