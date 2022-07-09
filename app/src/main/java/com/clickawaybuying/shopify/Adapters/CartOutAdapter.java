package com.clickawaybuying.shopify.Adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.clickawaybuying.shopify.ChooseAddress;
import com.clickawaybuying.shopify.classes.Product;
import com.clickawaybuying.shopify.R;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;

public class CartOutAdapter extends RecyclerView.Adapter<CartOutAdapter.ViewHolder> {

    private Context mContext;
    public List<Product> products = new ArrayList<>();
    List<String> titles;
    List<String> images;
    List<Integer>price;
    public static List<String>Address = new ArrayList<>();
    public static List<Integer>quantity;
    LayoutInflater inflater;
    public static String address;
    private int totalPrice,Price,shipFee;

    public CartOutAdapter(Context context, List<String> titles, List<String> images,List<Integer>price,List<Integer>quantity,List<String>Address){
        this.mContext = context;
        this.titles=titles;
        this.price=price;
        this.images=images;
        CartOutAdapter.quantity = quantity;
        this.Address = Address;
        this.inflater = LayoutInflater.from(context);

    }


    public class ViewHolder extends RecyclerView.ViewHolder {

        public TextView mProdname,mAddress,mProdPrice,mQuan,mDate,mProdNum;
        ImageView mProdImage;
        ImageButton mChangeAddress;


        public ViewHolder (View view){
            super(view);
            mProdname = itemView.findViewById(R.id.cartOutProdname);
            mAddress = itemView.findViewById(R.id.cartOutAddress);
            mProdPrice = itemView.findViewById(R.id.cartOutPrice);
            mProdImage = itemView.findViewById(R.id.cartOutImage);
            mChangeAddress = itemView.findViewById(R.id.cartOutChangeAddress);
            mQuan = itemView.findViewById(R.id.chkOutQuan);
            mDate = itemView.findViewById(R.id.dateReceive);
            mProdNum = itemView.findViewById(R.id.itemNumber);

        }
    }


    @NonNull
    @Override
    public CartOutAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(mContext).inflate(R.layout.cart_out_item,parent,false);
        return new ViewHolder(view);

    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull final CartOutAdapter.ViewHolder holder, final int position) {
        price.removeAll(Collections.singleton(0));
        quantity.removeAll(Collections.singleton(0));
        holder.mProdname.setText(titles.get(position));
        DecimalFormat formatter = new DecimalFormat("#,###");//format for thousand
        String finalPrice = formatter.format(price.get(position));
        holder.mProdPrice.setText("₱"+finalPrice);
        holder.mQuan.setText("X"+ quantity.get(position));
        Glide.with(mContext).load(images.get(position)).into(holder.mProdImage);

        int cartNum = position + 1;
        holder.mProdNum.setText("Cart Item "+cartNum);

        try {
            holder.mAddress.setText(Address.get(position));
        }
        catch (Exception e){
            e.printStackTrace();
        }
        address = holder.mAddress.getText().toString();

        String dateInString = null;
        //String currentDate = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(new Date());
        Calendar c = Calendar.getInstance();
        @SuppressLint("SimpleDateFormat") SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");// HH:mm:ss");
        //showtoast("Currrent Date Time : "+reg_date);

        c.add(Calendar.DATE, 2);  // number of days to add
        String recievedt1 = df.format(c.getTime());
        c.add(Calendar.DATE, 5);  // number of days to add
        String recievedt2 = df.format(c.getTime());
        holder.mDate.setText(recievedt1+ " to " +recievedt2);


        holder.mChangeAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Boolean CartAddress=true;
                Intent i = new Intent(mContext.getApplicationContext(), ChooseAddress.class);
                i.putExtra("cartaddress",CartAddress);
                i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                i.putExtra("index",position);
                mContext.getApplicationContext().startActivity(i);
            }
        });


    }


    @Override
    public int getItemCount() {
        return titles.size();
    }


}
