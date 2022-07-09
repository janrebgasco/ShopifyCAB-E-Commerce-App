package com.clickawaybuying.shopify;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.clickawaybuying.shopify.Adapters.AddressAdapter;
import com.clickawaybuying.shopify.utils.PreferenceUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ChooseAddress extends AppCompatActivity {
    //initializing variables
    RecyclerView.LayoutManager manager;
    RecyclerView recyclerAddress;
    List<Address> addresses = new ArrayList<>();
    public static RecyclerView.Adapter mAdapter;
    public static Boolean CartAddress = false,OrderAddress = false,PanelAddress = false,reloadNedeed = false, SellerAddress = false;
    public static int listIndex,orderID;


    public static final String URL = "https://gascojanreb.000webhostapp.com/ShopifyCAB/getAddresses.php";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //assigning value to variables
        setContentView(R.layout.activity_choose_address);
        recyclerAddress = findViewById(R.id.addressRecycler);

        catchIntent();//call method
        //set up recyclerView gridLayout
        manager = new GridLayoutManager(ChooseAddress.this, 1);
        recyclerAddress.setLayoutManager(manager);
        addresses = new ArrayList<>();//creates new arraylist
        //load addresses in adapter
        adapterRefresher();
    }
    public void addressBack(View v){//back button
        super.onBackPressed();
        finish();
    }
    public void getAddress(){//get address in database
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        StringRequest jsonObjectRequest = new StringRequest(Request.Method.DEPRECATED_GET_OR_POST,
                URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {//gets the php json responses
                try {
                    JSONArray array = new JSONArray(response);

                    for (int i = 0; i<array.length();i++){
                        JSONObject addressObject = array.getJSONObject(i);

                        int id = addressObject.getInt("id");
                        String fullname = addressObject.getString("fullname");
                        String phone = addressObject.getString("phone");
                        int lotNum = addressObject.getInt("lot_number");
                        String region = addressObject.getString("region");
                        String province = addressObject.getString("province");
                        String city = addressObject.getString("city");
                        String barangay = addressObject.getString("barangay");
                        int postal = addressObject.getInt("postal");
                        String username = addressObject.getString("username");


                        Address address = new Address(fullname,phone,lotNum, region,province,city,barangay,postal,username,id);
                        addresses.add(address);

                    }
                    mAdapter = new AddressAdapter(ChooseAddress.this,addresses);
                    recyclerAddress.setAdapter(mAdapter);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {//gets php error messages
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> parameters = new HashMap<String, String>();//putting parameters to pass on the php file
                //get Shared Preference username
                String user_name  = PreferenceUtils.getEmail(ChooseAddress.this);
                parameters.put("username",user_name);

                return parameters;
            }
        };
        requestQueue.add(jsonObjectRequest);
    }
    public void gotoAddAddress(View v){//go to AddAddress.class
        Intent i = new Intent(this,AddAddress.class);
        startActivity(i);
    }

    public void adapterRefresher(){//refresh adapter
        addresses.clear();
        getAddress();
    }
    public void layoutRefresher(){//refresh ;ayout withou animations
        finish();
        overridePendingTransition(0, 0);
        startActivity(getIntent());
        overridePendingTransition(0, 0);
    }
    public void finisher(){
        finish();
    }//finish activity
    public void catchIntent(){//catch all intents
        Intent i = getIntent();
        CartAddress = i.getBooleanExtra("cartaddress",false);
        OrderAddress = i.getBooleanExtra("changeToPayAddress",false);
        PanelAddress = i.getBooleanExtra("fromPanelAddress",false);
        SellerAddress = i.getBooleanExtra("fromSellProd",false);
        orderID = i.getIntExtra("orderID",0);
        listIndex = i.getIntExtra("index",0);
    }
    @Override
    public void onResume() {//when the activity resume and reloadNeeded boolean is true refresh layout then make reloadneeded false
        super.onResume();
        if (reloadNedeed){
            layoutRefresher();
            reloadNedeed = false;
        }
    }

}