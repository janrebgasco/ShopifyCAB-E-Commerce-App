package com.clickawaybuying.shopify;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.clickawaybuying.shopify.Adapters.CategAdapter;
import com.clickawaybuying.shopify.Adapters.RecyclerAdapter;
import com.clickawaybuying.shopify.classes.Product;
import com.clickawaybuying.shopify.utils.PreferenceUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CategContainer extends AppCompatActivity {
    //intialize variables
    RecyclerView recyclerView;
    RecyclerView.LayoutManager manager;
    List<Product> products;
    CategAdapter mAdapter;
    public static TextView categoryName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_categ_container);
        //assigning variables
        recyclerView = findViewById(R.id.CategRecycler);
        categoryName =findViewById(R.id.txtCategory);


        //catches intent
        Intent intent = getIntent();
        String category = intent.getStringExtra("categoryType");
        categoryName.setText(category);//set text based on intent
        loadProducts(category);//load products method with parameter category as category name
        setupRecycler();
    }

    private void setupRecycler() {
        manager = new GridLayoutManager(CategContainer.this, 2);//setting up gridlayout for recyclerview
        recyclerView.setLayoutManager(manager);//set recyclerview layout
        products = new ArrayList<>();//creates new arraylist
    }

    public void loadProducts(final String categName){//load the products based on category name
        String CATEG_URL = "https://gascojanreb.000webhostapp.com/ShopifyCAB/getcategitems.php";
        StringRequest stringRequest = new StringRequest(Request.Method.POST, CATEG_URL,
                new Response.Listener<String>()
                {
                    @Override
                    public void onResponse(String response)
                    {
                        try {
                            JSONArray array = new JSONArray(response);

                            for (int i = 0; i<array.length();i++){
                                JSONObject productobject = array.getJSONObject(i);
                                int id = productobject.getInt("id");
                                String product_name = productobject.getString("product_name");
                                int price = productobject.getInt("price");
                                String image = productobject.getString("image");
                                double rating = productobject.getDouble("rating");
                                String description = productobject.getString("description");
                                String brand = productobject.getString("brand");
                                int stocks = productobject.getInt("stocks");
                                int sold = productobject.getInt("sold");
                                String seller = productobject.getString("seller");
                                String color = productobject.getString("color");
                                String size = productobject.getString("size");
                                String sellerImage = productobject.getString("sellerImage");

                                Product product = new Product(product_name,price, rating,image,description,brand,stocks,sold,id,seller,color,size,sellerImage);
                                products.add(product);
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        mAdapter = new CategAdapter(CategContainer.this,products);
                        recyclerView.setAdapter(mAdapter);

                    }
                }, new Response.ErrorListener()

        {
            @Override
            public void onErrorResponse(VolleyError error)
            {
                //Toast.makeText(CategContainer.this, error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> parameters = new HashMap<String, String>();//putting parameters to pass on the php file
                String user_name  = PreferenceUtils.getEmail(CategContainer.this);

                parameters.put("categName",categName);


                return parameters;
            }
        };;
        Volley.newRequestQueue(this).add(stringRequest);
    }
    public void btnBack(View v){
        super.onBackPressed();
    }//back button



}