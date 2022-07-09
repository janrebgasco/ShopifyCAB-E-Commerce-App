package com.clickawaybuying.shopify;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.clickawaybuying.shopify.utils.PreferenceUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class Transfer extends AppCompatActivity {
    double balance,amount;
    EditText mAmount,mReceiver;
    String username = "No One";
    Button mProceed;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transfer);
        mAmount = findViewById(R.id.transferAmount);
        mReceiver = findViewById(R.id.userTransfer);
        mProceed = findViewById(R.id.transferContinue);

        catchIntent();

        mProceed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                proceed();
            }
        });

    }

    private void proceed() {
        checkUsername();
        try {
            amount = Double.parseDouble(mAmount.getText().toString());
        }
        catch (Exception e){
            e.printStackTrace();
        }

        if (mAmount.getText().toString().trim().length() == 0){
            mAmount.setError("Please enter the amount");
            mAmount.requestFocus();
            return;
        }
        if (amount==0){
            mAmount.setError("Invalid amount");
            mAmount.requestFocus();
            return;
        }
        if (amount > balance){
            mAmount.setError("Not enough balance");
            mAmount.requestFocus();
            return;
        }
        else {
            loading();
        }

    }

    public void transferBack(View view) {
        super.onBackPressed();
    }
    public void catchIntent(){
        Intent i = getIntent();
        balance = i.getDoubleExtra("balance",0);
    }
    public void checkUsername(){
        String URL = "https://gascojanreb.000webhostapp.com/ShopifyCAB/checkUserExist.php";
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        StringRequest jsonObjectRequest = new StringRequest(Request.Method.DEPRECATED_GET_OR_POST,
                URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {//gets the php json responses
                try {
                    JSONArray array = new JSONArray(response);

                    for (int i = 0; i<array.length();i++){
                        JSONObject productobject = array.getJSONObject(i);
                        String user = productobject.getString("user");
                        username = user;
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
                String user_name  = PreferenceUtils.getEmail(Transfer.this);
                parameters.put("username",mReceiver.getText().toString());

                return parameters;
            }
        };
        requestQueue.add(jsonObjectRequest);
    }
    public void loading(){
        final ProgressDialog progress = new ProgressDialog(Transfer.this);
        progress.setTitle("Checking user Existence");
        progress.setMessage("Please wait...");
        progress.setCancelable(false); // disable dismiss by tapping outside of the dialog
        progress.show();
        // To dismiss the dialog
        new CountDownTimer(3000, 1000) {
            public void onTick(long millisUntilFinished) {

            }
            public void onFinish() {
                progress.dismiss();
                checkTxtBoxes();
            }
        }.start();
    }

    private void checkTxtBoxes() {
        if (!mReceiver.getText().toString().equals(username)){
            mReceiver.setError("User does not exist");
            mReceiver.requestFocus();
            return;
        }
        else{
            Intent i = new Intent(Transfer.this,qrConfirm.class);
            i.putExtra("receiver",username);
            i.putExtra("amount",amount);
            startActivity(i);
        }

    }
}