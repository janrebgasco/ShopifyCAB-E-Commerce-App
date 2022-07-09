package com.clickawaybuying.shopify;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
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
import com.bumptech.glide.Glide;
import com.clickawaybuying.shopify.utils.PreferenceUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class CheckOut extends AppCompatActivity {
    //intializing variables
    public static TextView mProdname,mPrice,quantityed,total,adress;
    public static ImageView prodimg;
    TextView recieveDate,mtxtColor, mtxtSize,mtxtBalance,mBalance;
    public static String image,quan;
    int Total,id,price,quantity;
    String recievedt1, recievedt2,size,colors,replacedColor,replacedSize,combinedData;
    RequestQueue requestQueue;
    public  static Spinner mPaymentOptions;
    double balance2;
    String payment_option = "Cash on Delivery";
    boolean paid = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_out);
        //assigning variables
        mProdname = findViewById(R.id.cashoutProdName);
        mPrice = findViewById(R.id.cashoutPrice);
        prodimg = findViewById(R.id.cashoutImage);
        quantityed =findViewById(R.id.cashoutQuantity);
        total = findViewById(R.id.cashoutTotal);
        recieveDate = findViewById(R.id.dateRecieve);
        adress = findViewById(R.id.Addressed);
        mtxtColor = findViewById(R.id.txtViewColor);
        mtxtSize = findViewById(R.id.txtViewSize);
        mtxtBalance = findViewById(R.id.balTxt);
        mBalance = findViewById(R.id.checkoutBalance);
        mPaymentOptions = findViewById(R.id.checkoutDdown);
        //call methods
        catchIntent();
        getAddress();
        displayData();
        getBalance();

        mPaymentOptions.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {//payment options spinner onclick listener
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                paymentConditions();//call method
                if (mPaymentOptions.getSelectedItemPosition() == 1){
                    payment_option = "Wallet";
                    paid  = true;
                }
                if(mPaymentOptions.getSelectedItemPosition() == 2){
                    mtxtBalance.setVisibility(View.VISIBLE);
                    mBalance.setVisibility(View.VISIBLE);
                    payment_option = "Scan on Deliver";
                    paid  = false;
                }
                else{
                    payment_option = "Cash on Delivery";
                    paid = false;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

    }

    private void paymentConditions() {//checks if user selects the Shopify CAB Wallet as payment option
        if (mPaymentOptions.getSelectedItemPosition() == 1){
            mtxtBalance.setVisibility(View.VISIBLE);
            mBalance.setVisibility(View.VISIBLE);
            String PIN = PreferenceUtils.getPin2(CheckOut.this);
            if (PIN != null){
                Intent intent = new Intent(CheckOut.this, pin.class);
                intent.putExtra("fromCheckout",true);
                startActivity(intent);
            }
            String PATTERN2 = PreferenceUtils.getPattern2(CheckOut.this);
            if (PATTERN2 != null){
                Intent intent = new Intent(CheckOut.this, PatternLock.class);
                intent.putExtra("fromCheckout",true);
                startActivity(intent);
            }
            boolean hasFingerPrint = PreferenceUtils.getFprint2(CheckOut.this);
            if (hasFingerPrint){
                Intent intent = new Intent(CheckOut.this, Fingerprint.class);
                intent.putExtra("fromCheckout",true);
                startActivity(intent);
            }
        }
        else{
            mtxtBalance.setVisibility(View.INVISIBLE);
            mBalance.setVisibility(View.INVISIBLE);
        }
    }

    public void backCheck(View view){//back button
        super.onBackPressed();
        finish();
    }
    public void gotoEditAddress(View view){//go to ChooseAddress.class on button click
        Intent i =new Intent(CheckOut.this,ChooseAddress.class);
        startActivity(i);
        }

    public void payNow(View view){//button pay now onclick
        if (adress.length()<=0){
            Toast.makeText(CheckOut.this,"Enter delivery address to continue",Toast.LENGTH_SHORT).show();
        }

        else if (mPaymentOptions.getSelectedItemPosition() == 1 || mPaymentOptions.getSelectedItemPosition() == 2)
        {
            if (Total > balance2){
                Toast.makeText(this, "Insufficient Balance", Toast.LENGTH_SHORT).show();
            }
            else {
                insertToDb();
                updateBalance();
            }
        }

        else {
            insertToDb();
        }
    }
    public void catchIntent(){//catch the intents
        // Catching incoming intent
        Intent intent = getIntent();
        id = intent.getIntExtra("id", 0);
        price = intent.getIntExtra("price", 0);
        double stocks = intent.getDoubleExtra("stocks", 0);
        double sold = intent.getDoubleExtra("sold", 0);
        float rate = intent.getFloatExtra("rate", 0);
        String title = intent.getStringExtra("title");
        String brand = intent.getStringExtra("brand");
        String description = intent.getStringExtra("description");
        quantity = intent.getIntExtra("quantity",0);
        colors = intent.getStringExtra("color");
        size = intent.getStringExtra("size");
        combinedData = intent.getStringExtra("combi");
    }
    @SuppressLint("SetTextI18n")
    public void displayData(){//display all the info that is needed by the user
        mProdname.setText(Purchase.JProdname.getText().toString());
        mPrice.setText("₱ "+price);
        Intent i = getIntent();
        image = i.getStringExtra("image");
        Glide.with(CheckOut.this).load(image).into(prodimg);

        if (colors.equals("[]"))
        {
            mtxtColor.setVisibility(View.GONE);
        }
        if (size.equals("[]"))
        {
            mtxtSize.setVisibility(View.GONE);
        }
        replacedColor = colors.replace("[", "").replace("]", "");
        replacedSize = size.replace("[", "").replace("]", "");
        if (combinedData.equals("empty")){
            mtxtColor.setText("Color/s and Size/s : "+replacedColor);
        }else{
            mtxtColor.setText("Color/s : "+replacedColor);
            mtxtSize.setText("Size/s : "+replacedSize);
        }
        quan = Purchase.quantity.getText().toString();
        quantityed.setText("X"+quan);
        int Quantity = Integer.parseInt(quan);
        int Price = Purchase.price;
        int deliver = 40;

        Total = (Price * quantity) + deliver;
        DecimalFormat formatter = new DecimalFormat("#,###");//format for thousand
        String TotalFormat = formatter.format(Total);
        total.setText("P"+ TotalFormat);

        String dateInString = null;
        //String currentDate = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(new Date());
        Calendar c = Calendar.getInstance();
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");// HH:mm:ss");
        //showtoast("Currrent Date Time : "+reg_date);

        c.add(Calendar.DATE, 2);  // number of days to add
        recievedt1 = df.format(c.getTime());
        c.add(Calendar.DATE, 5);  // number of days to add
        recievedt2 = df.format(c.getTime());
        //showtoast("end Time : "+end_date);
        recieveDate.setText(recievedt1+ " to " +recievedt2);
    }
    public void insertToDb(){//insert data to database
        String URL = "https://gascojanreb.000webhostapp.com/ShopifyCAB/insertOrder.php";
        requestQueue = Volley.newRequestQueue(getApplicationContext());//manages worker threads for running the network operations
        StringRequest jsonObjectRequest = new StringRequest(Request.Method.POST,
                URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {//gets the php json responses
                //Toast.makeText(CheckOut.this,""+response, Toast.LENGTH_LONG).show();
                if (response.isEmpty()){
                    orderSuccess();
                }
            }

            private void orderSuccess() {
                Intent i = new Intent(CheckOut.this, Ordered.class);
                i.putExtra("image", image);
                startActivity(i);
            }
        }, new Response.ErrorListener() {//gets php error messages
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(CheckOut.this, error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> parameters = new HashMap<String, String>();//putting parameters to pass on the php file

                //get Shared Preference username
                String user_name  = PreferenceUtils.getEmail(CheckOut.this);
                parameters.put("username",user_name);
                parameters.put("product_name",mProdname.getText().toString());
                parameters.put("price",String.valueOf(price));
                parameters.put("image",image);
                parameters.put("quantity",String.valueOf(quantity));
                parameters.put("total",String.valueOf(Total));
                parameters.put("id",String.valueOf(id));
                parameters.put("receiveDate",recievedt1+ " to " +recievedt2);
                parameters.put("fulladdress",adress.getText().toString());
                parameters.put("color",String.valueOf(replacedColor));
                parameters.put("size",String.valueOf(replacedSize));
                parameters.put("payment_option",payment_option);
                parameters.put("paid",String.valueOf(paid));

                return parameters;
            }
        };
        requestQueue.add(jsonObjectRequest);

    }
    public void getAddress(){//gets the addresses
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

                        if (!(addressObject.length() < 0)){
                            adress.append(fullname+ " | (+63) " +phone+ "\n" +
                                    city+ "\n" +
                                    "#"+lotNum+", "+barangay+ ", " +city+ region+ ", " +province+ "\n" +
                                    "Postal code "+postal);
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
                String user_name  = PreferenceUtils.getEmail(CheckOut.this);
                parameters.put("username",user_name);

                return parameters;
            }
        };
        requestQueue.add(jsonObjectRequest);
    }
    public void getBalance(){//get wallet balance in databse
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
                Toast.makeText(CheckOut.this, ""+error, Toast.LENGTH_SHORT).show();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> parameters = new HashMap<String, String>();//putting parameters to pass on the php file
                //get Shared Preference username
                String user_name  = PreferenceUtils.getEmail(CheckOut.this);
                parameters.put("username",user_name);

                return parameters;
            }
        };
        requestQueue.add(jsonObjectRequest);
    }
    public void updateBalance(){//update wallet balance in databse
        String URL = "https://gascojanreb.000webhostapp.com/ShopifyCAB/QRUpdateBalance.php";
        requestQueue = Volley.newRequestQueue(this);
        StringRequest jsonObjectRequest = new StringRequest(Request.Method.POST,
                URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {//gets the php json responses
                //Toast.makeText(CheckOut.this, ""+response, Toast.LENGTH_SHORT).show();
            }
        }, new Response.ErrorListener() {//gets php error messages
            @Override
            public void onErrorResponse(VolleyError error) {
                //Toast.makeText(CheckOut.this, ""+error, Toast.LENGTH_SHORT).show();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> parameters = new HashMap<String, String>();//putting parameters to pass on the php file
                //get Shared Preference username
                String user_name  = PreferenceUtils.getEmail(CheckOut.this);
                parameters.put("user",user_name);
                parameters.put("amount",String.valueOf(Total));

                return parameters;
            }
        };
        requestQueue.add(jsonObjectRequest);
    }
}