package com.clickawaybuying.shopify;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.clickawaybuying.shopify.Adapters.CartAdapter;
import com.clickawaybuying.shopify.classes.Product;
import com.clickawaybuying.shopify.utils.PreferenceUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class Cart extends AppCompatActivity {
    //initializing variables
    public static int grand;
    RecyclerView.LayoutManager manager;
    RecyclerView recycleCart;
    List<Product> products = new ArrayList<>();
    private RecyclerView.Adapter mAdapter;
    private TextView subtotal,grandtotal,ship;
    int cartTotal;
    Button mCartCheckOut;
    List<String> colors = new ArrayList<>();
    List<String> sizes = new ArrayList<>();
    List<LinkedList<String>> parentColors = new LinkedList<>();
    LinkedList<String> childColors = new LinkedList<>();
    List<LinkedList<String>> parentColorIndex = new LinkedList<>();
    LinkedList<String> childColorIndex = new LinkedList<>();
    public static List<LinkedList<String>> parentSizeIndex = new LinkedList<>();
    LinkedList<String> childSizeIndex = new LinkedList<>();
    List<LinkedList<String>> parentColorQuan = new LinkedList<>();
    LinkedList<String> childColorQuan = new LinkedList<>();
    public static List<LinkedList<String>> parentSizeQuan = new LinkedList<>();
    LinkedList<String> childSizeQuan = new LinkedList<>();

    public static List<LinkedList<String>> parentSizes = new LinkedList<>();
    LinkedList<String> childSizes = new LinkedList<>();
    public static List<LinkedList<String>> parentSelectedColors = new LinkedList<>();
    public static LinkedList<String> childSelectedColors = new LinkedList<>();

    public static final String PRODUCT_URL = "https://gascojanreb.000webhostapp.com/ShopifyCAB/getCartItems.php";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);
        //variable assignments
        recycleCart = findViewById(R.id.cartRecycler);
        subtotal = findViewById(R.id.subTotal);
        grandtotal = findViewById(R.id.cartTotal);
        ship = findViewById(R.id.shipping);
        mCartCheckOut = findViewById(R.id.btnCheckOut);

        //creates a new arraylist everytime the user uses the activity
        CartAdapter.selectedID = new ArrayList<>();
        CartAdapter.selectedQuan = new ArrayList<>();
        CartAdapter.selectedPrice = new ArrayList<>();
        CartAdapter.selectedProduct = new ArrayList<>();
        CartAdapter.selectedImage = new ArrayList<>();
        colors = new ArrayList<>();
        sizes = new ArrayList<>();

        manager = new GridLayoutManager(Cart.this, 1);//setting up gridlayout
        recycleCart.setLayoutManager(manager);//set recyclerview layout as gridlayout
        products = new ArrayList<>();//creates a new arraylist
        //load cart products
        myCartItems();


        mCartCheckOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {//checkout button onClick Listener
                if (products.size() < 1){//checks if cart is empty
                    Toast.makeText(getApplicationContext(),"Empty cart",Toast.LENGTH_SHORT).show();
                }else{
                    Intent i = new Intent(Cart.this,CartCheckout.class);//go to CartCheckout.class
                    i.putExtra("total",cartTotal);//add intent
                    startActivity(i);//start intent
                }
            }
        });



    }
    public void cartBack(View v){//cart back button
        super.onBackPressed();
        Cart.this.finish();
    }
    public void myCartItems(){//load cart items
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        StringRequest jsonObjectRequest = new StringRequest(Request.Method.DEPRECATED_GET_OR_POST,
                PRODUCT_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {//gets the php json responses
                try {
                    JSONArray array = new JSONArray(response);

                    for (int i = 0; i<array.length();i++){
                        JSONObject productobject = array.getJSONObject(i);
                        int id = productobject.getInt("product_id");
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
                        int quantity = productobject.getInt("quantity");
                        String color_indexes = productobject.getString("color_indexes");
                        String size_indexes = productobject.getString("size_indexes");
                        String color_quan = productobject.getString("color_quan");
                        String size_quan = productobject.getString("size_quan");

                        String[] colorList = color.split(",");
                        childColors.addAll(Arrays.asList(colorList));
                        String[] sizeList = size.split(",");
                        childSizes.addAll(Arrays.asList(sizeList));
                        parentColors.add(childColors);
                        parentSizes.add(childSizes);
                        childColors = new LinkedList<>();
                        childSizes = new LinkedList<>();

                        String[] color_index = color_indexes.split(",");
                        childColorIndex.addAll(Arrays.asList(color_index));
                        String[] size_index = size_indexes.split(",");
                        childSizeIndex.addAll(Arrays.asList(size_index));
                        String[] c_quan = color_quan.split(",");
                        childColorQuan.addAll(Arrays.asList(c_quan));
                        String[] s_quan = size_quan.split(",");
                        childSizeQuan.addAll(Arrays.asList(s_quan));
                        parentColorIndex.add(childColorIndex);
                        parentSizeIndex.add(childSizeIndex);
                        parentColorQuan.add(childColorQuan);
                        parentSizeQuan.add(childSizeQuan);
                        childColorIndex = new LinkedList<>();
                        childSizeIndex = new LinkedList<>();

                        Product product = new Product(product_name,price, rating,image,description,brand,stocks,sold,id,seller,color,size,quantity);
                        products.add(product);

                    }
                    mAdapter = new CartAdapter(Cart.this,products,parentColors,parentSizes,parentColorIndex,parentSizeIndex,parentColorQuan,parentSizeQuan);
                    recycleCart.setAdapter(mAdapter);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {//gets php error messages
            @Override
            public void onErrorResponse(VolleyError error) {
                //Toast.makeText(Cart.this, ""+error, Toast.LENGTH_SHORT).show();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> parameters = new HashMap<String, String>();//putting parameters to pass on the php file
                //get Shared Preference username
                String user_name  = PreferenceUtils.getEmail(Cart.this);
                parameters.put("username",user_name);

                return parameters;
            }
        };
        requestQueue.add(jsonObjectRequest);
    }
    public void dispTotal(final int totalPrice,int shipFee){//display total of cart items
        cartTotal = totalPrice;
        DecimalFormat formatter = new DecimalFormat("#,###");//format for thousand
        String formattedTotal = formatter.format(totalPrice);
        subtotal.setText("₱"+String.valueOf(formattedTotal));

        String formattedFee = formatter.format(shipFee);
        ship.setText("₱"+String.valueOf(formattedFee));

        grand = shipFee+totalPrice;
        String formattedgrand = formatter.format(grand);
        grandtotal.setText("₱"+String.valueOf(formattedgrand));

    }
    public void layoutRefresher(){//refresh layout without animations
        finish();
        overridePendingTransition(0, 0);
        startActivity(getIntent());
        overridePendingTransition(0, 0);
    }
    public void setupParentColorArray(int position) {
        childSelectedColors = new LinkedList<>();
        for (int i = 0;i< parentColors.get(position).size();i++){
            childSelectedColors.add("null");
        }
        if (position == 0){
            childSelectedColors.add("Red");
        }
        if (position == 1){
            childSelectedColors.add("Green");
            childSelectedColors.add("Red");
        }
        if (position == 2){
            childSelectedColors.add("Black");
            childSelectedColors.add("Blue");
            childSelectedColors.add("Pink");
        }
        parentSelectedColors.add(childSelectedColors);
    }

}