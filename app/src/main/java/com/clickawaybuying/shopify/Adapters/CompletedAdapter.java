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
import android.widget.Button;
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
import com.clickawaybuying.shopify.Feedback;
import com.clickawaybuying.shopify.classes.Product;
import com.clickawaybuying.shopify.R;
import com.clickawaybuying.shopify.utils.PreferenceUtils;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CompletedAdapter extends RecyclerView.Adapter<CompletedAdapter.ViewHolder>{

    private Context mContext;
    private List<Product> products = new ArrayList<>();


    public CompletedAdapter(Context context, List<Product> products){
        this.mContext = context;
        this.products = products;
    }


    public class ViewHolder extends RecyclerView.ViewHolder {

        private final TextView mTitle, mPrice,mTotal,mQuantity,mDate,mColor,mSize,mAddress;
        Button mRefund,mFeedback;
        ImageView mImg;

        public ViewHolder (View view){
            super(view);

            mTitle = view.findViewById(R.id.orderTitle);
            mPrice = view.findViewById(R.id.orderPrice);
            mImg = view .findViewById(R.id.orderImg);
            mTotal = view.findViewById(R.id.orderTotal);
            mQuantity = view.findViewById(R.id.orderQuan);
            mDate = view.findViewById(R.id.toPayRecieveDate);
            mColor = view.findViewById(R.id.completedColor);
            mSize = view.findViewById(R.id.completedSize);
            mAddress = view.findViewById(R.id.CompletedAddress);
            mRefund = view.findViewById(R.id.btnRefund);
            mFeedback = view.findViewById(R.id.btnFeedback);

        }
    }


    @NonNull
    @Override
    public CompletedAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(mContext).inflate(R.layout.completed_list_item,parent,false);
        return new CompletedAdapter.ViewHolder(view);

    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull final CompletedAdapter.ViewHolder holder, final int position) {


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

        holder.mRefund.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new AlertDialog.Builder(mContext)
                        .setMessage("Are you sure you want a refund?")
                        .setCancelable(false)
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                insertRefund(product);
                                Toast.makeText(mContext.getApplicationContext(),"Item added to refund request.",Toast.LENGTH_SHORT).show();
                                new CountDownTimer(1500, 1000) {
                                    public void onTick(long millisUntilFinished) {

                                    }
                                    public void onFinish() {
                                        removeOrder(product);
                                    }
                                }.start();

                            }
                        })
                        .setNegativeButton("No", null)
                        .show();
            }
        });
        holder.mFeedback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(mContext.getApplicationContext(), Feedback.class);
                i.putExtra("image",product.getImage1());
                i.putExtra("prodName",product.getTitle1());
                i.putExtra("prodID",product.getProdID());
                i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                mContext.getApplicationContext().startActivity(i);
            }
        });

    }


    @Override
    public int getItemCount() {
        return products.size();
    }

    public void removeOrder(final Product product){
        RequestQueue requestQueue = Volley.newRequestQueue(mContext.getApplicationContext());
        final String user_name  = PreferenceUtils.getEmail(mContext.getApplicationContext());

        String insertURL = "https://gascojanreb.000webhostapp.com/ShopifyCAB/removeCompleted.php";
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

                parameters.put("id",String.valueOf(product.getId1()));
                parameters.put("order_of",user_name);

                return parameters;
            }
        };
        requestQueue.add(request);
    }
    public void insertRefund(final Product product){

        RequestQueue requestQueue = Volley.newRequestQueue(mContext.getApplicationContext());
        final String user_name  = PreferenceUtils.getEmail(mContext.getApplicationContext());

        String insertURL = "https://gascojanreb.000webhostapp.com/ShopifyCAB/insertRefund.php";
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

                parameters.put("username",user_name);
                parameters.put("id",String.valueOf(product.getId1()));

                return parameters;
            }
        };
        requestQueue.add(request);
    }


}
