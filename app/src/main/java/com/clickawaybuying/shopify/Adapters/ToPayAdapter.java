package com.clickawaybuying.shopify.Adapters;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.CountDownTimer;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.clickawaybuying.shopify.ChooseAddress;
import com.clickawaybuying.shopify.Orders;
import com.clickawaybuying.shopify.classes.Product;
import com.clickawaybuying.shopify.R;
import com.clickawaybuying.shopify.utils.PreferenceUtils;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ToPayAdapter extends RecyclerView.Adapter<ToPayAdapter.ViewHolder>{

    private Context mContext;
    private List<Product> products = new ArrayList<>();


    public ToPayAdapter (Context context,List<Product> products){
        this.mContext = context;
        this.products = products;
    }


    public class ViewHolder extends RecyclerView.ViewHolder {

        private final TextView mTitle, mPrice,mTotal,mQuantity,mDate,mColor,mSize,mAddress,btnChangeAddress;
        ImageButton mCancel;
        ImageView mImg;

        public ViewHolder (View view){
            super(view);

            mTitle = view.findViewById(R.id.orderTitle);
            mPrice = view.findViewById(R.id.orderPrice);
            mImg = view .findViewById(R.id.orderImg);
            mTotal = view.findViewById(R.id.orderTotal);
            mQuantity = view.findViewById(R.id.orderQuan);
            mCancel = view.findViewById(R.id.cancelImg);
            mDate = view.findViewById(R.id.toPayRecieveDate);
            mColor = view.findViewById(R.id.toPayColor);
            mSize = view.findViewById(R.id.toPaySize);
            mAddress = view.findViewById(R.id.toPayAddress);
            btnChangeAddress = view.findViewById(R.id.toPayChangeAddress);

        }
    }


    @NonNull
    @Override
    public ToPayAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(mContext).inflate(R.layout.order_list_item,parent,false);
        return new ToPayAdapter.ViewHolder(view);

    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull final ToPayAdapter.ViewHolder holder, final int position) {


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

        holder.mCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new AlertDialog.Builder(mContext)
                        .setMessage("Cancel this order?")
                        .setCancelable(false)
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                insertToCancelledDB(product);
                                Toast.makeText(mContext.getApplicationContext(),"Loading, Please wait...",Toast.LENGTH_SHORT).show();
                                new CountDownTimer(1500, 1000) {
                                    public void onTick(long millisUntilFinished) {

                                    }
                                    public void onFinish() {
                                        cancelOrder(product);
                                    }
                                }.start();

                            }
                        })
                        .setNegativeButton("No", null)
                        .show();
            }
        });

        holder.btnChangeAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean toPayAddress = true;
                Intent i = new Intent(mContext.getApplicationContext(), ChooseAddress.class);
                i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                i.putExtra("changeToPayAddress",toPayAddress);
                i.putExtra("orderID",product.getId1());
                mContext.getApplicationContext().startActivity(i);
            }
        });



    }


    @Override
    public int getItemCount() {
        return products.size();
    }
    public void cancelOrder(final Product product){
        RequestQueue requestQueue = Volley.newRequestQueue(mContext.getApplicationContext());
        final String user_name  = PreferenceUtils.getEmail(mContext.getApplicationContext());

        String insertURL = "https://gascojanreb.000webhostapp.com/ShopifyCAB/cancelOrder.php";
        StringRequest request = new StringRequest(Request.Method.POST, insertURL, new Response.Listener<String>() {//canned request for retrieving the response body at a given URL as a String
            @Override
            public void onResponse(String response) {//gets the php json responses

            }
        }, new Response.ErrorListener() {//gets php error messages
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> parameters = new HashMap<String, String>();//putting parameters to pass on the php file

                parameters.put("product_name",String.valueOf(product.getId1()));
                parameters.put("order_of",user_name);

                return parameters;
            }
        };
        requestQueue.add(request);
        ((Orders) mContext).layoutRefresher();
    }
    public void insertToCancelledDB(final Product product){
        RequestQueue requestQueue = Volley.newRequestQueue(mContext.getApplicationContext());
        final String user_name  = PreferenceUtils.getEmail(mContext.getApplicationContext());

        String insertURL = "https://gascojanreb.000webhostapp.com/ShopifyCAB/insertCancelled.php";
        StringRequest request = new StringRequest(Request.Method.POST, insertURL, new Response.Listener<String>() {//canned request for retrieving the response body at a given URL as a String
            @Override
            public void onResponse(String response) {//gets the php json responses

            }
        }, new Response.ErrorListener() {//gets php error messages
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(mContext.getApplicationContext(),""+error,Toast.LENGTH_SHORT).show();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> parameters = new HashMap<String, String>();//putting parameters to pass on the php file
                parameters.put("id",String.valueOf(product.getId1()));
                parameters.put("username",user_name);

                return parameters;
            }
        };
        requestQueue.add(request);
    }

}
