package com.rlm.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.rlm.Interfaces.onClickNotificationListener;
import com.rlm.Interfaces.onClickOfferListener;
import com.rlm.Models.ModelNotification;
import com.rlm.Models.ModelOffer;
import com.rlm.R;
import com.utils.Utils.Tools;

import java.util.ArrayList;

public class AdapterNotification extends RecyclerView.Adapter<AdapterNotification.mViewHolder> {
    private ArrayList<ModelNotification> data;
    Context mContext;
    private onClickNotificationListener listener;

    public AdapterNotification(Context mContext, ArrayList<ModelNotification>doctors) {
        this.mContext = mContext;
        this.data=doctors;
    }
    public AdapterNotification Callback(onClickNotificationListener listener){
        this.listener=listener;
        return this;
    }

    @NonNull
    @Override
    public mViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(mContext).inflate(R.layout.layout_notification,parent,false);
        return new mViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull mViewHolder holder, int position) {
        holder.tv_message.setText(data.get(position).getMessage());
        holder.tv_date.setText(mContext.getResources().getString(R.string.apointment_date)+data.get(position).getDate_time());
        holder.img_delete.setOnClickListener(v->listener.onDelete(position));
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    class mViewHolder extends RecyclerView.ViewHolder{
        TextView tv_message,tv_date;
        ImageView img_delete;
        public mViewHolder(@NonNull View itemView) {
            super(itemView);
            tv_message=itemView.findViewById(R.id.tv_message);
            tv_date=itemView.findViewById(R.id.tv_date);
            img_delete=itemView.findViewById(R.id.img_delete);
        }
    }
}
