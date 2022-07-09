package com.clickawaybuying.shopify.Adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.clickawaybuying.shopify.classes.Product;
import com.clickawaybuying.shopify.Purchase;
import com.clickawaybuying.shopify.R;

import java.util.ArrayList;
import java.util.List;

public class CategAdapter extends RecyclerView.Adapter<CategAdapter.CategViewHolder> {

    private Context mContext;
    private List<Product> products = new ArrayList<>();


    public CategAdapter (Context context,List<Product> products){
        this.mContext = context;
        this.products = products;
    }


    public class CategViewHolder extends RecyclerView.ViewHolder {

        private TextView mTitle, mPrice,mDesciption,mSold;
        private ImageView mImageView,def_image;
        private RatingBar mRate;
        private LinearLayout mContainer;

        public CategViewHolder (View view){
            super(view);
            def_image = itemView.findViewById(R.id.product_image);
            mTitle = view.findViewById(R.id.product_title);
            mImageView = view.findViewById(R.id.product_image);
            mPrice = view.findViewById(R.id.product_price);
            mContainer = view.findViewById(R.id.product_container);
            mSold = view.findViewById(R.id.product_sold);


        }
    }


    @NonNull
    @Override
    public CategAdapter.CategViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(mContext).inflate(R.layout.products_list_item_layout,parent,false);
        return new CategAdapter.CategViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CategAdapter.CategViewHolder holder, int position) {

        final Product product = products.get(position);

        holder.mPrice.setText("₱ "+product.getPrice());
        holder.mTitle.setText(product.getTitle());
        Glide.with(mContext).load(product.getImage()).into(holder.mImageView);
        int sould = (int) product.getSold();
        holder.mSold.setText(String.valueOf(sould+" sold"));

        holder.mContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(mContext, Purchase.class);

                intent.putExtra("title",product.getTitle());
                intent.putExtra("image",product.getImage());
                intent.putExtra("rate",product.getRating());
                intent.putExtra("price",product.getPrice());
                intent.putExtra("description",product.getDescription());
                intent.putExtra("brand",product.getBrand());
                intent.putExtra("stocks",product.getStocks());
                intent.putExtra("sold",product.getSold());
                intent.putExtra("seller", product.getSeller());
                intent.putExtra("color",product.getColor());
                intent.putExtra("size",product.getSize());
                intent.putExtra("sellerImage",product.getSellerImage());
                mContext.startActivity(intent);

            }
        });
    }

    @Override
    public int getItemCount() {
        return products.size();
    }
}
