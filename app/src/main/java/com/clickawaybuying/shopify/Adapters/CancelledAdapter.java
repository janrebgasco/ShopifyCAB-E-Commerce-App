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

public class CancelledAdapter extends RecyclerView.Adapter<CancelledAdapter.ViewHolder>{

    private Context mContext;
    private List<Product> products = new ArrayList<>();


    public CancelledAdapter(Context context, List<Product> products){
        this.mContext = context;
        this.products = products;
    }


    public class ViewHolder extends RecyclerView.ViewHolder {

        private TextView mTitle, mPrice,mTotal,mQuantity,mDate;
        ImageView mImg;

        public ViewHolder (View view){
            super(view);

            mTitle = view.findViewById(R.id.cancelledTitle);
            mPrice = view.findViewById(R.id.cancelledPrice);
            mImg = view .findViewById(R.id.cancelledImg);
            mTotal = view.findViewById(R.id.cancelledTotal);
            mQuantity = view.findViewById(R.id.cancelledQuan);
            mDate = view.findViewById(R.id.cancelledRecieveDate);

        }
    }


    @NonNull
    @Override
    public CancelledAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(mContext).inflate(R.layout.cancelled_list_item,parent,false);
        return new CancelledAdapter.ViewHolder(view);

    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull final CancelledAdapter.ViewHolder holder, final int position) {


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


    }


    @Override
    public int getItemCount() {
        return products.size();
    }

}
