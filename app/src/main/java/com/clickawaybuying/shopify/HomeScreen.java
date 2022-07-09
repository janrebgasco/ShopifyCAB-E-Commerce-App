package com.clickawaybuying.shopify;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.clickawaybuying.shopify.Adapters.Adapter;
import com.clickawaybuying.shopify.Adapters.RecyclerAdapter;
import com.clickawaybuying.shopify.classes.Product;
import com.denzcoskun.imageslider.ImageSlider;
import com.denzcoskun.imageslider.models.SlideModel;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class HomeScreen extends AppCompatActivity {
    //initializing variables
    RecyclerView categ,recyclerView;
    public static List<String> titles;
    List<Integer> images;
    Adapter adapter;
    TextView prodname;
    List<Product> products;
    RecyclerAdapter mAdapter;
    RecyclerView.LayoutManager manager;
    ImageSlider slider;
    BottomNavigationView bot_nav;
    SearchView mSearchView;
    ImageView mCart;
    AdView mAdView;

    //String user_name  = PreferenceUtils.getEmail(HomeScreen.this);
    public static final String PRODUCT_URL = "https://gascojanreb.000webhostapp.com/ShopifyCAB/getitems.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_screen);
        //assigning variable
        categ = findViewById(R.id.categories);
        prodname = findViewById(R.id.textView5);
        recyclerView = findViewById(R.id.items);
        slider = findViewById(R.id.imageSlider);
        bot_nav = findViewById(R.id.buttom_navigation);
        mSearchView = findViewById(R.id.searchHome);
        mCart = findViewById(R.id.btnCart);
        mAdView = findViewById(R.id.adView);
        //call methods
        imageSlider();
        addItemsToCategory();
        loadProducts();
        gridlayoutRecycler();
        setupRecyclerAdapter();

        mSearchView.setOnSearchClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {//when icon search is clicked, go to SearchResult.class
                gotoSearchResultClass();
            }
        });

        bot_nav.setSelectedItemId(R.id.homeScreen);//Set Home Selected
        bot_nav.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {//perform item seleted listener
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                navigationSwitch(item);//call method then pass parameter item
                return false;
            }
        });

    }

    private void setupRecyclerAdapter() {//sets up the layout if recyclerView as  well as the adapter for Categories
        adapter = new Adapter(this,titles,images);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this,1,GridLayoutManager.HORIZONTAL,false);
        categ.setLayoutManager(gridLayoutManager);
        categ.setAdapter(adapter);
    }

    private void gridlayoutRecycler() {//set up gridlayout recyclerView for products
        manager = new GridLayoutManager(HomeScreen.this, 2);
        recyclerView.setLayoutManager(manager);
        products = new ArrayList<>();
    }

    private void addItemsToCategory() {//adds the title and image to category
        titles = new ArrayList<>();
        images = new ArrayList<>();
        titles.add("Gadgets");
        images.add(R.drawable.c1_svg);
        titles.add("Grocery");
        images.add(R.drawable.c2_svg);
        titles.add("Electronic Accessories");
        images.add(R.drawable.c3_svg);
        titles.add("Health and Beauty");
        images.add(R.drawable.c4_svg);
        titles.add("Babies and Toys");
        images.add(R.drawable.c5_svg);
        titles.add("TV and Home appliances");
        images.add(R.drawable.c6_svg);
        titles.add("Home and Living");
        images.add(R.drawable.c7_svg);
        titles.add("Women's Fashion");
        images.add(R.drawable.c8_svg);
        titles.add("Men's Fashion");
        images.add(R.drawable.c9_svg);
        titles.add("Fashion Accessories");
        images.add(R.drawable.c10_svg);
        titles.add("Automotive and Motorcycle");
        images.add(R.drawable.c11_svg);
        titles.add("Sports");
        images.add(R.drawable.c12_svg);
    }

    private void imageSlider() {//image slider
        List<SlideModel>slideModels = new ArrayList<>();
        slideModels.add(new SlideModel("https://gascojanreb.000webhostapp.com/ShopifyCAB/slider/1.png"));
        slideModels.add(new SlideModel("https://gascojanreb.000webhostapp.com/ShopifyCAB/slider/2.png"));
        slideModels.add(new SlideModel("https://gascojanreb.000webhostapp.com/ShopifyCAB/slider/3.png"));
        slideModels.add(new SlideModel("https://gascojanreb.000webhostapp.com/ShopifyCAB/slider/4.png"));
        slideModels.add(new SlideModel("https://gascojanreb.000webhostapp.com/ShopifyCAB/slider/5.png"));
        slideModels.add(new SlideModel("https://gascojanreb.000webhostapp.com/ShopifyCAB/slider/6.png"));
        slideModels.add(new SlideModel("https://gascojanreb.000webhostapp.com/ShopifyCAB/slider/7.png"));
        slider.setImageList(slideModels,true);
    }

    private boolean navigationSwitch(MenuItem item) {//navigation switch method
        switch (item.getItemId()){
            case R.id.chat:
                startActivity(new Intent(getApplicationContext()
                        ,Chat.class));
                overridePendingTransition(0,0);
                finish();
                return true;
            case R.id.homeScreen:
                return true;
            case R.id.search_result:
                startActivity(new Intent(getApplicationContext()
                        ,search_result.class));
                overridePendingTransition(0,0);
                finish();
                return true;
            case R.id.wallet:
                startActivity(new Intent(getApplicationContext()
                        , wallet.class));
                overridePendingTransition(0,0);
                finish();
                return true;
            case R.id.profile:
                startActivity(new Intent(getApplicationContext()
                        ,Profile.class));
                overridePendingTransition(0,0);
                finish();
                return true;
        }
        return false;
    }

    private void gotoSearchResultClass() {//go to another activity
        startActivity(new Intent(getApplicationContext()
                ,search_result.class));
        overridePendingTransition(0,0);
        finish();
    }


    public void loadProducts(){//load all products from database
        StringRequest stringRequest = new StringRequest(Request.Method.GET, PRODUCT_URL,
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
                        mAdapter = new RecyclerAdapter(HomeScreen.this,products);
                        Collections.shuffle(products);
                        recyclerView.setAdapter(mAdapter);
                    }
                }, new Response.ErrorListener()

            {
            @Override
                public void onErrorResponse(VolleyError error)
                {if (error.toString().length()>1){
                    Toast.makeText(HomeScreen.this, "Can't connect to the server.Please check your internet connection.", Toast.LENGTH_SHORT).show();
                }
                    //Toast.makeText(HomeScreen.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        Volley.newRequestQueue(this).add(stringRequest);

    }
    
    public void openCart(View v){//opens the cart when clicked
        Intent i = new Intent(this,Cart.class);
        startActivity(i);
    }
    @Override
    public void onBackPressed() {// ask user when he pressed back button
        new AlertDialog.Builder(this)
                .setMessage("Are you sure you want to exit?")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        HomeScreen.this.finish();
                    }
                })
                .setNegativeButton("No", null)
                .show();
    }
    private void showADS(){
        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {


            }
        });
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);
    }
}