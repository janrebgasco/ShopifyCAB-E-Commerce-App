package com.clickawaybuying.shopify.Adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.clickawaybuying.shopify.classes.Product;
import com.clickawaybuying.shopify.R;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class ToShipAdapter extends RecyclerView.Adapter<ToShipAdapter.ViewHolder>{

    private Context mContext;
    private List<Product> products = new ArrayList<>();


    public ToShipAdapter(Context context, List<Product> products){
        this.mContext = context;
        this.products = products;
    }


    public class ViewHolder extends RecyclerView.ViewHolder {

        private final TextView mTitle, mPrice,mTotal,mQuantity,mDate,mColor,mSize,mAddress;
        ImageView mImg;

        public ViewHolder (View view){
            super(view);

            mTitle = view.findViewById(R.id.orderTitle);
            mPrice = view.findViewById(R.id.orderPrice);
            mImg = view .findViewById(R.id.orderImg);
            mTotal = view.findViewById(R.id.orderTotal);
            mQuantity = view.findViewById(R.id.orderQuan);
            mDate = view.findViewById(R.id.toPayRecieveDate);
            mColor = view.findViewById(R.id.toShipColor);
            mSize = view.findViewById(R.id.toShipSize);
            mAddress = view.findViewById(R.id.toShipAddress);

        }
    }


    @NonNull
    @Override
    public ToShipAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(mContext).inflate(R.layout.to_ship_list_item,parent,false);
        return new ToShipAdapter.ViewHolder(view);

    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull final ToShipAdapter.ViewHolder holder, final int position) {


        final Product product = products.get(position);
        int Price = product.getPrice1();
        DecimalFormat formatter = new DecimalFormat("#,###");//format for thousand
        String finalPrice = formatter.format(Price);
        holder.mPrice.setText("₱"+finalPrice);
        holder.mTitle.setText(product.getTitle1());
        Glide.with(mContext).load(product.getImage1()).into(holder.mImg);
        holder.mQuantity.setText("X"+String.valueOf(product.getQuantity()));
        int total = product.getTotal();
        String Total = formatter.format(total);
        holder.mTotal.setText("₱"+Total);
        holder.mDate.setText("Receive date: "+"\n"+product.getDate());
        holder.mAddress.setText(product.getAddress1());

        if(product.getColor1().isEmpty())
        {
            holder.mColor.setVisibility(View.GONE);
        }
        else
        {
            holder.mColor.setVisibility(View.VISIBLE);
        }
        holder.mColor.setText("Color/s : "+product.getColor1());
        if (product.getSize1().isEmpty())
        {
            holder.mSize.setVisibility(View.GONE);
        }
        else
        {
            holder.mSize.setVisibility(View.VISIBLE);
        }
        holder.mSize.setText("Size/s : "+product.getSize1());


    }


    @Override
    public int getItemCount() {
        return products.size();
    }

}
