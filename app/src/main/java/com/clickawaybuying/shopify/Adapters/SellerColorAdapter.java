package com.clickawaybuying.shopify.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.ToggleButton;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.clickawaybuying.shopify.Purchase;
import com.clickawaybuying.shopify.R;
import com.clickawaybuying.shopify.SellProduct;

import java.util.ArrayList;
import java.util.List;

public class SellerColorAdapter extends RecyclerView.Adapter<SellerColorAdapter.MyViewHolder> {

    private Context mContext;
    public static List<String>selectedColors = new ArrayList<>();
    List<String>colors = new ArrayList<>();


    public SellerColorAdapter(Context context, List<String> colors){
        this.mContext = context;
        this.colors = colors;
    }


    public class MyViewHolder extends RecyclerView.ViewHolder {

        ToggleButton mToggle;


        public MyViewHolder (View view){
            super(view);
            mToggle = view.findViewById(R.id.listToggle);
        }
    }


    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(mContext).inflate(R.layout.seller_colors_list_item,parent,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, final int position) {

        holder.mToggle.setText(colors.get(position));
        holder.mToggle.setTextOff(colors.get(position));
        holder.mToggle.setTextOn(colors.get(position));

        holder.mToggle.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                if(isChecked){
                    SellProduct.colors2.add(colors.get(position));
                }
                else{
                    SellProduct.colors2.remove(colors.get(position));
                    SellProduct.colors2.add("null");
                }
            }
        });


    }

    @Override
    public int getItemCount() {
        return colors.size();
    }

}
