package com.clickawaybuying.shopify;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.Button;
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
import com.clickawaybuying.shopify.utils.PreferenceUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Map;


public class qrConfirm extends AppCompatActivity {
    String receiver;
    double amount,balance2;
    TextView mReceiver,mBalance,mAmount;
    ImageView mImage;
    Button mConfirm;
    RequestQueue requestQueue;
    ProgressDialog progress;
    boolean SOD = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qr_confirm);
        mReceiver = findViewById(R.id.qrReciever);
        mBalance = findViewById(R.id.qrBalance);
        mAmount  = findViewById(R.id.qrConfirmAmount);
        mImage = findViewById(R.id.qrConfirmImage);
        mConfirm = findViewById(R.id.qrConfirmBtn);

        progress = new ProgressDialog(qrConfirm.this);
        progress.setTitle("Loading");
        progress.setMessage("Please wait...");
        progress.setCancelable(false); // disable dismiss by tapping outside of the dialog

        catchIntent();
        getBalance();
        showImg();

        mConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loading();
            }
        });

    }


    public void qrConfirmBack(View view) {
        Intent i = new Intent(this, ScanActivity.class);
        i.putExtra("balance",balance2);
        startActivity(i);
    }
    public void catchIntent(){
        Intent i = getIntent();
        receiver = i.getStringExtra("receiver");
        amount = i.getDoubleExtra("amount",0);
        SOD = i.getBooleanExtra("b",false);

        mReceiver.setText(receiver);
        DecimalFormat formatter = new DecimalFormat("###,###.##");
        mAmount.setText("₱"+String.valueOf(formatter.format(amount)));
    }
    public void getBalance(){
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
                        balance2  = balance;
                        DecimalFormat formatter = new DecimalFormat("###,###.##");
                        mBalance.setText("₱"+String.valueOf(formatter.format(balance)));


                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {//gets php error messages
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(qrConfirm.this, ""+error, Toast.LENGTH_SHORT).show();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> parameters = new HashMap<String, String>();//putting parameters to pass on the php file
                //get Shared Preference username
                String user_name  = PreferenceUtils.getEmail(qrConfirm.this);
                parameters.put("username",user_name);

                return parameters;
            }
        };
        requestQueue.add(jsonObjectRequest);
    }
    public void showImg(){
        final String user_name  = PreferenceUtils.getEmail(qrConfirm.this);
        String insertURL = "https://gascojanreb.000webhostapp.com/ShopifyCAB/showInfo.php";

        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());//manages worker threads for running the network operations
        StringRequest request = new StringRequest(Request.Method.DEPRECATED_GET_OR_POST, insertURL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {//gets the php json responses

                try {
                    JSONArray array = new JSONArray(response);

                    for (int i = 0; i<array.length();i++){
                        JSONObject account = array.getJSONObject(i);
                        String image = account.getString("image");

                        if(!image.equals("null")){
                            try {
                                Glide.with(qrConfirm.this).load(image).into(mImage);
                            }
                            catch (Exception e){
                                e.printStackTrace();
                            }
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
                parameters.put("username",user_name);

                return parameters;
            }
        };
        requestQueue.add(request);
    }
    @Override
    public void onBackPressed() {
        Intent i = new Intent(this,ScanActivity.class);
        i.putExtra("balance",balance2);
        startActivity(i);
        finish();
    }
    public void confirm(final String reciever){
        String URL = "https://gascojanreb.000webhostapp.com/ShopifyCAB/QRSendMoney.php";
        requestQueue = Volley.newRequestQueue(this);
        StringRequest jsonObjectRequest = new StringRequest(Request.Method.POST,
                URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {//gets the php json responses
            }
        }, new Response.ErrorListener() {//gets php error messages
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(qrConfirm.this, ""+error, Toast.LENGTH_SHORT).show();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> parameters = new HashMap<String, String>();//putting parameters to pass on the php file
                //get Shared Preference username
                String user_name  = PreferenceUtils.getEmail(qrConfirm.this);
                parameters.put("username",reciever);
                parameters.put("amount",String.valueOf(amount));

                return parameters;
            }
        };
        requestQueue.add(jsonObjectRequest);
    }
    public void updateBalance(){
        String URL = "https://gascojanreb.000webhostapp.com/ShopifyCAB/QRUpdateBalance.php";
        requestQueue = Volley.newRequestQueue(this);
        StringRequest jsonObjectRequest = new StringRequest(Request.Method.POST,
                URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {//gets the php json responses
                progress.dismiss();
                Toast.makeText(qrConfirm.this, "Balance Transferred Successfully ", Toast.LENGTH_SHORT).show();
            }
        }, new Response.ErrorListener() {//gets php error messages
            @Override
            public void onErrorResponse(VolleyError error) {
                progress.dismiss();
                Toast.makeText(qrConfirm.this, ""+error, Toast.LENGTH_SHORT).show();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> parameters = new HashMap<String, String>();//putting parameters to pass on the php file
                //get Shared Preference username
                String user_name  = PreferenceUtils.getEmail(qrConfirm.this);
                parameters.put("user",user_name);
                parameters.put("amount",String.valueOf(amount));

                return parameters;
            }
        };
        requestQueue.add(jsonObjectRequest);
    }
    public void loading(){
        progress.show();
        confirm(receiver);
        updateBalance();

    }
}