package com.rlm.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.rlm.Interfaces.onClickDoctorListener;
import com.rlm.Interfaces.onTimeSelectListener;
import com.rlm.Models.ModelDoctor;
import com.rlm.Models.ModelTime;
import com.rlm.R;
import com.utils.Session.SessionManager;

import java.util.ArrayList;

public class AdapterTime extends RecyclerView.Adapter<AdapterTime.mViewHolder> {
    private ArrayList<ModelTime> data;
    Context mContext;
    private onTimeSelectListener listener;

    public AdapterTime(Context mContext, ArrayList<ModelTime>doctors) {
        this.mContext = mContext;
        this.data=doctors;
    }
    public AdapterTime Callback(onTimeSelectListener listener){
        this.listener=listener;
        return this;
    }

    @NonNull
    @Override
    public mViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(mContext).inflate(R.layout.layout_time,parent,false);
        return new mViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull mViewHolder holder, int position) {
        holder.tv_time.setText(data.get(position).getTime());
        holder.tv_time.setBackgroundResource(data.get(position).isSelected()?R.drawable.border_selected:R.drawable.border_gray);
        holder.tv_time.setOnClickListener(v->listener.onSelectTime(position));
        holder.tv_time.setEnabled(data.get(position).isEnable());
        if (!data.get(position).isEnable()){
            holder.tv_time.setBackgroundResource(R.drawable.border_disable);
        }
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    class mViewHolder extends RecyclerView.ViewHolder{
        TextView tv_time;
        public mViewHolder(@NonNull View itemView) {
            super(itemView);
            tv_time=itemView.findViewById(R.id.tv_time);
        }
    }
}
