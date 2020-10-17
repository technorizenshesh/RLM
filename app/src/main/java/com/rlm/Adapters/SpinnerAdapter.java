package com.rlm.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.rlm.Interfaces.onSpinnerItemClickListener;
import com.rlm.Models.ModelSpinner;
import com.rlm.R;

import java.util.ArrayList;

public class SpinnerAdapter extends ArrayAdapter<ModelSpinner> {
    LayoutInflater flater;
    private onSpinnerItemClickListener listener;

    public SpinnerAdapter(Context context, int resouceId, int textviewId, ArrayList<ModelSpinner> list){
        super(context,resouceId,textviewId, list);
    }
    public SpinnerAdapter Callback(onSpinnerItemClickListener listener){
        this.listener=listener;
        return this;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        return rowview(convertView,position);
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        return rowview(convertView,position);
    }

    private View rowview(View convertView , int position){
        ModelSpinner rowItem = getItem(position);
        viewHolder holder ;
        View rowview = convertView;
        if (rowview==null) {
            holder = new viewHolder();
            flater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            rowview = flater.inflate(R.layout.layout_spinner, null, false);
            holder.txtTitle = (TextView) rowview.findViewById(R.id.tv_title);
            rowview.setTag(holder);
        }else{
            holder = (viewHolder) rowview.getTag();
        }
        holder.txtTitle.setText(rowItem.getTitle());
//        holder.txtTitle.setOnClickListener(v->listener.onClickItem(rowItem));
        return rowview;
    }

    private class viewHolder{
        TextView txtTitle;
    }
}
