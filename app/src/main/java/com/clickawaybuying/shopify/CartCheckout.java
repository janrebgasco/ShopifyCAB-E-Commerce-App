package com.clickawaybuying.shopify;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NavUtils;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.clickawaybuying.shopify.Adapters.Adapter;
import com.clickawaybuying.shopify.Adapters.CartAdapter;
import com.clickawaybuying.shopify.Adapters.CartOutAdapter;
import com.clickawaybuying.shopify.Adapters.RecyclerAdapter;
import com.clickawaybuying.shopify.utils.PreferenceUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class
CartCheckout extends AppCompatActivity {
    //initialize variables
    List<String> Address = new ArrayList<>();
    List<Integer> Price = new ArrayList<>();
    List<String> DateReceive = new ArrayList<>();
    RecyclerView recyclerView;
    public static CartOutAdapter adapter;
    TextView mTotal,mtxtBalance,mBalance;
    Spinner mPaymentOptions;
    int total,shipFee = 40,itemCount = CartAdapter.selectedProduct.size(),grandTotal;
    RequestQueue requestQueue;
    String recievedt1,recievedt2;
    ImageButton mBack;
    double balance2;
    String payment_option = "Cash on Delivery";
    boolean paid = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart_checkout);
        //assigning variables
        recyclerView = findViewById(R.id.recyclerCartOut);
        mTotal = findViewById(R.id.CartCashoutTotal);
        mBack = findViewById(R.id.cartOutBack);
        mtxtBalance = findViewById(R.id.balTxt2);
        mBalance = findViewById(R.id.checkoutBalance2);
        mPaymentOptions = findViewById(R.id.checkoutDdown2);

        CartOutAdapter.Address = new ArrayList<>();//creates a new arraylist everytime user opens the activity
        //call methods
        receiveDate();
        catchIntent();
        getAddress();
        loading1();
        getBalance();

        mPaymentOptions.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {//payment options spinner onlick listener
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (mPaymentOptions.getSelectedItemPosition() == 1){//if spinner index is 1, make textbox visible
                    mtxtBalance.setVisibility(View.VISIBLE);
                    mBalance.setVisibility(View.VISIBLE);
                    payment_option = "Wallet";
                    paid = true;
                }
                if(mPaymentOptions.getSelectedItemPosition() == 2){
                    mtxtBalance.setVisibility(View.VISIBLE);
                    mBalance.setVisibility(View.VISIBLE);
                    payment_option = "Scan on Deliver";
                    paid  = false;
                }
                else{//hide textboxes
                    mtxtBalance.setVisibility(View.INVISIBLE);
                    mBalance.setVisibility(View.INVISIBLE);
                    payment_option = "Cash on Delivery";
                    paid = false;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        grandTotal = (shipFee * itemCount) + total;//get total value of cart item

        DecimalFormat formatter = new DecimalFormat("#,###");//format for thousand
        String finalPrice = formatter.format(grandTotal);//formats grandtotal then assign as finalPrice variable
        mTotal.setText("₱"+finalPrice);//set textbox text with peso sign

        adapter = new CartOutAdapter(this, CartAdapter.selectedProduct,CartAdapter.selectedImage,CartAdapter.selectedPrice,CartAdapter.selectedQuan,Address);//passing the parameters to the adapter
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this,1,GridLayoutManager.VERTICAL,false);//setting up gridlayout
        recyclerView.setLayoutManager(gridLayoutManager);//set recyclerview as gridlayout
        recyclerView.setAdapter(adapter);//setting recyclerview adatapter

        String receiveDate = recievedt1 + " to " + recievedt2;//assign recievedt1 and 2 as variable recieveDate
        for (int i = 0; i < CartAdapter.selectedPrice.size(); i++) {//loop based on the how many are the selected price
            Price.add(CartAdapter.selectedPrice.get(i) * CartAdapter.selectedQuan.get(i) + 40);//add to Price arraylist the total of price multiplied by quantity + 40(shipping fee)
            DateReceive.add(receiveDate);//add recieveDate value to DateReceive arraylist
        }

    }
    public void cartChkBack(View v){//back button
        NavUtils.navigateUpTo(this, new Intent(this,
                Cart.class));
    }

    public void chkOutPayNow(View view) {//pay now button onclick

        if (CartOutAdapter.address.length()<=0){// checks if address is empty
            Toast.makeText(CartCheckout.this,"Enter delivery address to continue",Toast.LENGTH_SHORT).show();
        }
        if (mPaymentOptions.getSelectedItemPosition() == 1)//checks if selected spinner is second item
        {
            if (grandTotal > balance2){//checks if total of cart item is greater than balance
                Toast.makeText(this, "Insufficient Balance", Toast.LENGTH_SHORT).show();
            }
            else {
                for (int i = 0; i < CartAdapter.selectedProduct.size(); i++) {//loop based on how many are selectedProduct arraylist value
                    insertToDb(i);//insert to databse method with param i which serves as index value
                    deleteCartItem(i);//delete to database method with param i which serves as index value
                }
                updateBalance();//updates the balace
                loading();
            }
        }
        else {
            for (int i = 0; i<CartAdapter.selectedProduct.size(); i++) {//loop based on how many are selectedProduct arraylist value
                insertToDb(i);//insert to databse method with param i which serves as index value
                deleteCartItem(i);//delete to database method with param i which serves as index value
            }
            loading();

        }

    }
    public void getAddress(){//get the addresses method with index 0 as default address
        final String URL = "https://gascojanreb.000webhostapp.com/ShopifyCAB/getAddresses.php";
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        StringRequest jsonObjectRequest = new StringRequest(Request.Method.DEPRECATED_GET_OR_POST,
                URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {//gets the php json responses
                try {
                    JSONArray array = new JSONArray(response);

                    JSONObject addressObject = array.getJSONObject(0);

                    int id = addressObject.getInt("id");
                    String fullname = addressObject.getString("fullname");
                    int phone = addressObject.getInt("phone");
                    int lotNum = addressObject.getInt("lot_number");
                    String region = addressObject.getString("region");
                    String province = addressObject.getString("province");
                    String city = addressObject.getString("city");
                    String barangay = addressObject.getString("barangay");
                    int postal = addressObject.getInt("postal");
                    String username = addressObject.getString("username");


                    if (!(addressObject.length() <0)){//check if there are address on database
                        for (int i = 1; i <= CartAdapter.selectedProduct.size();i++){
                            CartOutAdapter.Address.add(fullname+ " | (+63) " +phone+ "\n" +
                                    city+ "\n" +
                                    "#"+lotNum+", "+barangay+ ", " +city +" "+ region+ ", " +province+ "\n" +
                                    "Postal code "+postal);//add the address to the adapter arraylist
                            adapter.notifyDataSetChanged();//notify that adapter data has changed
                        }
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
                String user_name  = PreferenceUtils.getEmail(CartCheckout.this);
                parameters.put("username",user_name);

                return parameters;
            }
        };
        requestQueue.add(jsonObjectRequest);
    }
    public void loading(){//loading method
        final ProgressDialog progress = new ProgressDialog(CartCheckout.this);
        progress.setTitle("Loading");
        progress.setMessage("Please wait...");
        progress.setCancelable(false); // disable dismiss by tapping outside of the dialog
        progress.show();
        // To dismiss the dialog
        new CountDownTimer(3000, 1000) {
            public void onTick(long millisUntilFinished) {

            }
            public void onFinish() {
                progress.dismiss();
                Intent i = new Intent(CartCheckout.this,Ordered.class);
                startActivity(i);
            }
        }.start();
    }
    public void loading1(){//loading method
        final ProgressDialog progress = new ProgressDialog(CartCheckout.this);
        progress.setTitle("Loading");
        progress.setMessage("Please wait...");
        progress.setCancelable(false); // disable dismiss by tapping outside of the dialog
        progress.show();
        // To dismiss the dialog
        new CountDownTimer(3000, 1000) {
            public void onTick(long millisUntilFinished) {

            }
            public void onFinish() {
                progress.dismiss();
            }
        }.start();
    }
    public void catchIntent(){//get intents
        Intent i = getIntent();
        total = i.getIntExtra("total",0);
    }

    public void insertToDb(final int index){//insert  data to database
        String URL = "https://gascojanreb.000webhostapp.com/ShopifyCAB/insertOrder.php";
        requestQueue = Volley.newRequestQueue(getApplicationContext());//manages worker threads for running the network operations
        StringRequest jsonObjectRequest = new StringRequest(Request.Method.POST,
                URL, new Response.Listener<String>() {
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

                CartAdapter.selectedID.removeAll(Collections.singleton(0));


                    //get Shared Preference username
                    String user_name = PreferenceUtils.getEmail(CartCheckout.this);
                    parameters.put("username", user_name);
                    parameters.put("product_name", String.valueOf(CartAdapter.selectedProduct.get(index)));
                    parameters.put("price", String.valueOf(CartAdapter.selectedPrice.get(index)));
                    parameters.put("image", String.valueOf(CartAdapter.selectedImage.get(index)));
                    parameters.put("quantity", String.valueOf(CartAdapter.selectedQuan.get(index)));
                    parameters.put("total", String.valueOf(Price.get(index)));
                    parameters.put("id", String.valueOf(CartAdapter.selectedID.get(index)));
                    parameters.put("receiveDate", String.valueOf(DateReceive.get(index)));
                    parameters.put("fulladdress",String.valueOf(CartOutAdapter.Address.get(index)));
                    parameters.put("payment_option",payment_option);
                    parameters.put("paid",String.valueOf(paid));

                return parameters;
            }
        };
        requestQueue.add(jsonObjectRequest);
    }
    public void receiveDate(){//create a receive date method
        String dateInString = null;
        //String currentDate = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(new Date());
        Calendar c = Calendar.getInstance();
        @SuppressLint("SimpleDateFormat") SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");// HH:mm:ss");
        //showtoast("Currrent Date Time : "+reg_date);

        c.add(Calendar.DATE, 2);  // number of days to add
        recievedt1 = df.format(c.getTime());
        c.add(Calendar.DATE, 5);  // number of days to add
        recievedt2 = df.format(c.getTime());
    }
    public void deleteCartItem(final int index){//delete the item to cart
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());//manages worker threads for running the network operations
        final String user_name  = PreferenceUtils.getEmail(this);

        String insertURL = "https://gascojanreb.000webhostapp.com/ShopifyCAB/deleteCartItem.php";
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

                parameters.put("product_name",String.valueOf(CartAdapter.selectedProduct.get(index)));
                parameters.put("cart_of",user_name);

                return parameters;
            }
        };
        requestQueue.add(request);
    }
    public void getBalance(){//get the wallet balance
        String URL = "https://gascojanreb.000webhostapp.com/ShopifyCAB/getBalance.php";
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        StringRequest jsonObjectRequest = new StringRequest(Request.Method.DEPRECATED_GET_OR_POST,
                URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {//gets the php json responses
                try {
                    JSONArray array = new JSONArray(response);

                    for (int i = 0; i<array.length();i++){
                        JSONObject productobject = array.getJSONObject(i);
                        int id = productobject.getInt("id");
                        double balance = productobject.getDouble("balance");
                        DecimalFormat formatter = new DecimalFormat("###,###.##");
                        mBalance.setText("₱"+String.valueOf(formatter.format(balance)));
                        balance2 = balance;
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {//gets php error messages
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(CartCheckout.this, ""+error, Toast.LENGTH_SHORT).show();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> parameters = new HashMap<String, String>();//putting parameters to pass on the php file
                //get Shared Preference username
                String user_name  = PreferenceUtils.getEmail(CartCheckout.this);
                parameters.put("username",user_name);

                return parameters;
            }
        };
        requestQueue.add(jsonObjectRequest);
    }
    public void updateBalance(){//update wallet balance
        String URL = "https://gascojanreb.000webhostapp.com/ShopifyCAB/QRUpdateBalance.php";
        requestQueue = Volley.newRequestQueue(this);
        StringRequest jsonObjectRequest = new StringRequest(Request.Method.POST,
                URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {//gets the php json responses
                Toast.makeText(CartCheckout.this, ""+response, Toast.LENGTH_SHORT).show();
            }
        }, new Response.ErrorListener() {//gets php error messages
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(CartCheckout.this, ""+error, Toast.LENGTH_SHORT).show();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> parameters = new HashMap<String, String>();//putting parameters to pass on the php file
                //get Shared Preference username
                String user_name  = PreferenceUtils.getEmail(CartCheckout.this);
                parameters.put("user",user_name);
                parameters.put("amount",String.valueOf(grandTotal));

                return parameters;
            }
        };
        requestQueue.add(jsonObjectRequest);
    }
}