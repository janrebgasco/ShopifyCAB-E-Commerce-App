package com.clickawaybuying.shopify.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.ToggleButton;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.clickawaybuying.shopify.R;
import com.clickawaybuying.shopify.SellProduct;

import java.util.ArrayList;
import java.util.List;

public class SellerSizeAdapter extends RecyclerView.Adapter<SellerSizeAdapter.MyViewHolder> {

    private Context mContext;
    List<String>sizes = new ArrayList<>();


    public SellerSizeAdapter(Context context, List<String> sizes){
        this.mContext = context;
        this.sizes = sizes;
    }


    public class MyViewHolder extends RecyclerView.ViewHolder {

        ToggleButton mToggle;


        public MyViewHolder (View view){
            super(view);
            mToggle = view.findViewById(R.id.sizeToggle);
        }
    }


    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(mContext).inflate(R.layout.number_size_list_item,parent,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, final int position) {

        holder.mToggle.setText(sizes.get(position));
        holder.mToggle.setTextOff(sizes.get(position));
        holder.mToggle.setTextOn(sizes.get(position));

        holder.mToggle.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                if(isChecked){
                    SellProduct.size2.add(sizes.get(position));
                }
                else{
                    SellProduct.size2.remove(sizes.get(position));
                    SellProduct.size2.add("null");
                }
            }
        });


    }

    @Override
    public int getItemCount() {
        return sizes.size();
    }

}
