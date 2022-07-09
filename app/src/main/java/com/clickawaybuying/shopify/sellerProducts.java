package com.clickawaybuying.shopify;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.viewpager.widget.ViewPager;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.clickawaybuying.shopify.ui.main.SellerSectionsPagerAdapter;
import com.clickawaybuying.shopify.utils.PreferenceUtils;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.tabs.TabLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class sellerProducts extends AppCompatActivity {
    ConstraintLayout mCons;
    String product_name;

    public static final String URL = "https://gascojanreb.000webhostapp.com/ShopifyCAB/getSellerProducts.php";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seller_products);
        mCons = findViewById(R.id.walletCons);

        SellerSectionsPagerAdapter sectionsPagerAdapter = new SellerSectionsPagerAdapter(this, getSupportFragmentManager());
        ViewPager viewPager = findViewById(R.id.view_pager2);
        viewPager.setAdapter(sectionsPagerAdapter);
        TabLayout tabs = findViewById(R.id.sellerTabs);
        tabs.setupWithViewPager(viewPager);

        showPostedProd();
        final String seller = PreferenceUtils.getSeller(this);


    }

    public void showPostedProd(){
            RequestQueue requestQueue = Volley.newRequestQueue(this);
            StringRequest jsonObjectRequest = new StringRequest(Request.Method.DEPRECATED_GET_OR_POST,
                    URL, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {//gets the php json responses
                    try {
                        JSONArray array = new JSONArray(response);

                        for (int i = 0; i<array.length();i++){
                            JSONObject addressObject = array.getJSONObject(i);

                            product_name = addressObject.getString("product_name");
                            int price = addressObject.getInt("price");
                            String desc = addressObject.getString("description");
                            String image = addressObject.getString("image");
                            //byte[] img = image.getBytes();
                            //Bitmap bm = decodeImage(img);

                        }
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
                    String user_name  = PreferenceUtils.getEmail(sellerProducts.this);
                    parameters.put("username",user_name);

                    return parameters;
                }
            };
            requestQueue.add(jsonObjectRequest);
            //Toast.makeText(getApplicationContext(),""+ img[0],Toast.LENGTH_SHORT).show();
    }
    public Bitmap decodeImage(byte[] blobImage){
        //convert blob to bitmap
        Bitmap bm = BitmapFactory.decodeByteArray(blobImage, 0 ,blobImage.length);

        return bm;
    }
    public void sellerBack(View v){
        super.onBackPressed();
    }
}