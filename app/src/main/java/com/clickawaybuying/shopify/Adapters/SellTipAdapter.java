package com.clickawaybuying.shopify.Adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.RequestQueue;
import com.clickawaybuying.shopify.CategContainer;
import com.clickawaybuying.shopify.R;

import java.util.ArrayList;
import java.util.List;

public class SellTipAdapter extends RecyclerView.Adapter<SellTipAdapter.ViewHolder> {
    private Context mContext;
    List<String> titles;
    List<Integer> images;
    List<String> description;
    LayoutInflater inflater;

    public SellTipAdapter(Context ctx, List<String> titles, List<Integer> images,List<String> description){
        this.mContext = ctx;
        this.titles=titles;
        this.images=images;
        this.description = description;
        this.inflater = LayoutInflater.from(ctx);
    }

    @Override
    public int getItemCount() {
        return titles.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView mTitle,mDesc;
        ImageView mImg;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            mTitle = itemView.findViewById(R.id.sellTipTitle);
            mImg =  itemView.findViewById(R.id.sellTipImg);
            mDesc = itemView.findViewById(R.id.sellTipDesc);
        }
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.sell_tip_list_item,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.mTitle.setText(titles.get(position));
        holder.mDesc.setText(description.get(position));
        if (images.get(position).equals(0)){
            holder.mImg.setVisibility(View.GONE);
        }
    }
}
