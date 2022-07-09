package com.clickawaybuying.shopify.Adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.clickawaybuying.shopify.classes.Product;
import com.clickawaybuying.shopify.Purchase;
import com.clickawaybuying.shopify.R;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class PendingProdAdapter extends RecyclerView.Adapter<PendingProdAdapter.MyViewHolder> {

    private Context mContext;
    private List<Product> products = new ArrayList<>();


    public PendingProdAdapter(Context context, List<Product> products){
        this.mContext = context;
        this.products = products;
    }


    public class MyViewHolder extends RecyclerView.ViewHolder {

        private TextView mTitle, mPrice;
        private ImageView mImageView;
        private LinearLayout mContainer;

        public MyViewHolder (View view){
            super(view);
            mTitle = view.findViewById(R.id.pend_product_title);
            mImageView = view.findViewById(R.id.pend_product_image);
            mPrice = view.findViewById(R.id.pend_product_price);
            mContainer = view.findViewById(R.id.pend_product_container);


        }
    }


    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(mContext).inflate(R.layout.pending_prod_list_item_layout,parent,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        final Product product = products.get(position);
        DecimalFormat formatter = new DecimalFormat("#,###");//format for thousand
        String Price = formatter.format(product.getPrice2());
        holder.mPrice.setText("₱"+Price);
        holder.mTitle.setText(product.getTitle2());
        Glide.with(mContext).load(product.getImage2()).into(holder.mImageView);
        holder.mContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(mContext, Purchase.class);

                //intent.putExtra("id",product.getId1());
                intent.putExtra("title",product.getTitle2());
                intent.putExtra("image",product.getImage2());
                intent.putExtra("rate",product.getRating2());
                intent.putExtra("price",product.getPrice2());
                intent.putExtra("description",product.getDescription2());
                intent.putExtra("brand",product.getBrand2());
                intent.putExtra("stocks",product.getStocks2());
                intent.putExtra("sold",0);
                intent.putExtra("seller", product.getSeller2());
                intent.putExtra("color",product.getColor2());
                intent.putExtra("size",product.getSize2());
                intent.putExtra("viewOnly", true);
                mContext.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return products.size();
    }

}
