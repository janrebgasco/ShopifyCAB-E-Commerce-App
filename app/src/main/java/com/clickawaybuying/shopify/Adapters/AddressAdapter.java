package com.clickawaybuying.shopify.Adapters;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.clickawaybuying.shopify.AddAddress;
import com.clickawaybuying.shopify.Address;
import com.clickawaybuying.shopify.CartCheckout;
import com.clickawaybuying.shopify.CheckOut;
import com.clickawaybuying.shopify.ChooseAddress;
import com.clickawaybuying.shopify.R;
import com.clickawaybuying.shopify.SellProduct;
import com.clickawaybuying.shopify.sellerRegistration;
import com.clickawaybuying.shopify.utils.PreferenceUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AddressAdapter extends RecyclerView.Adapter<AddressAdapter.ViewHolder> {

    private Context mContext;
    public List<Address> addresses = new ArrayList<>();

    public AddressAdapter(Context context, List<Address> address){
        this.mContext = context;
        this.addresses = address;

    }


    public class ViewHolder extends RecyclerView.ViewHolder {

        private TextView mAddress,mName;
        ConstraintLayout mContainer;
        ImageButton mDelete,mEdit;

        public ViewHolder (View view){
            super(view);
            mAddress = itemView.findViewById(R.id.dispAddress);
            mName = itemView.findViewById(R.id.addressName);
            mContainer = itemView.findViewById(R.id.addressHolder);
            mDelete = itemView.findViewById(R.id.addressDelete);
            mEdit = itemView.findViewById(R.id.addressEditBtn);

        }
    }


    @NonNull
    @Override
    public AddressAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(mContext).inflate(R.layout.address_list,parent,false);
        return new AddressAdapter.ViewHolder(view);


    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull final AddressAdapter.ViewHolder holder, final int position) {

        final Address address = addresses.get(position);
        holder.mName.setText(address.getFullname());
        holder.mAddress.setText( "(+63) " +address.getPhone()+ "\n" +
                address.getCity()+ "\n" +
                "#"+address.getLotNum()+", Barangay " +address.getBarangay()+ ", " +address.getCity()+ ", " +address.getRegion()+ ", " +address.getProvince()+ "\n" +
                address.getPostal());

        final String fulladdress = address.getFullname()+ " | (+63) " +address.getPhone()+ "\n" +
                address.getCity()+ "\n" +
                "#"+address.getLotNum()+", "+address.getBarangay()+ ", " +address.getCity()+" "+ address.getRegion()+ ", " +address.getProvince()+ "\n" +
                "Postal code "+address.getPostal();

                    if (!ChooseAddress.PanelAddress){
                        holder.mContainer.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                if (ChooseAddress.CartAddress){
                                    try {
                                        CartOutAdapter.Address.set(ChooseAddress.listIndex,fulladdress);
                                        CartCheckout.adapter.notifyDataSetChanged();
                                        ((ChooseAddress) mContext).addressBack(view);
                                    }
                                    catch (Exception e){
                                        CartOutAdapter.Address.add(fulladdress);
                                        CartCheckout.adapter.notifyDataSetChanged();
                                        ((ChooseAddress) mContext).addressBack(view);

                                        e.printStackTrace();

                                    }

                    }
                    if (ChooseAddress.OrderAddress){
                        new AlertDialog.Builder(mContext)
                                .setMessage("Use this address?")
                                .setCancelable(false)
                                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {

                                        RequestQueue requestQueue = Volley.newRequestQueue(mContext.getApplicationContext());
                                        final String user_name  = PreferenceUtils.getEmail(mContext.getApplicationContext());

                                        String updateAddress = "https://gascojanreb.000webhostapp.com/ShopifyCAB/updateOrderAddress.php";
                                        StringRequest request = new StringRequest(Request.Method.POST, updateAddress, new Response.Listener<String>() {
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
                                                parameters.put("orderID",String.valueOf(ChooseAddress.orderID));
                                                parameters.put("fullAddress",fulladdress);


                                                return parameters;
                                            }
                                        };
                                        requestQueue.add(request);
                                        ((ChooseAddress) mContext).finisher();
                                    }
                                })
                                .setNegativeButton("No", null)
                                .show();
                    }
                    if (ChooseAddress.SellerAddress){
                        sellerRegistration.mAddress.setText(fulladdress);
                        ((ChooseAddress) mContext).addressBack(view);
                    }
                    else{
                        CheckOut.adress.setText(fulladdress);
                        ((ChooseAddress) mContext).addressBack(view);
                    }

                }
            });
        }
        holder.mDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                new AlertDialog.Builder(mContext)
                        .setMessage("Are you sure you want remove this address?")
                        .setCancelable(false)
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {

                                RequestQueue requestQueue = Volley.newRequestQueue(mContext.getApplicationContext());
                                final String user_name  = PreferenceUtils.getEmail(mContext.getApplicationContext());

                                String deleteURL = "https://gascojanreb.000webhostapp.com/ShopifyCAB/deleteAddress.php";
                                StringRequest request = new StringRequest(Request.Method.POST, deleteURL, new Response.Listener<String>() {
                                    @Override
                                    public void onResponse(String response) {//gets the php json responses
                                        //Toast.makeText(mContext, response, Toast.LENGTH_SHORT).show();
                                    }
                                }, new Response.ErrorListener() {//gets php error messages
                                    @Override
                                    public void onErrorResponse(VolleyError error) {
                                        //Toast.makeText(mContext, ""+error, Toast.LENGTH_SHORT).show();
                                    }
                                }){
                                    @Override
                                    protected Map<String, String> getParams() throws AuthFailureError {
                                        Map<String,String> parameters = new HashMap<String, String>();//putting parameters to pass on the php file

                                        parameters.put("phone",String.valueOf(address.getPhone()));
                                        parameters.put("fullname",address.getFullname());
                                        parameters.put("username",user_name);

                                        return parameters;
                                    }
                                };
                                requestQueue.add(request);
                                ((ChooseAddress) mContext).layoutRefresher();
                            }
                        })
                        .setNegativeButton("No", null)
                        .show();

            }
        });

        holder.mEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(mContext, AddAddress.class);
                intent.putExtra("id",address.getId());
                intent.putExtra("fullname",address.getFullname());
                intent.putExtra("phone",address.getPhone());
                intent.putExtra("lotNum",address.getLotNum());
                intent.putExtra("region",address.getRegion());
                intent.putExtra("barangay",address.getBarangay());
                intent.putExtra("province",address.getProvince());
                intent.putExtra("city",address.getCity());
                intent.putExtra("postal",address.getPostal());
                intent.putExtra("update", true);

                ((ChooseAddress) mContext).finisher();
                mContext.startActivity(intent);

            }
        });
    }


    @Override
    public int getItemCount() {
        return addresses.size();
    }

    public void updateAddress(){

    }


}
