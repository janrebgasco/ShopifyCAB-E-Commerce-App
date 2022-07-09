package com.clickawaybuying.shopify;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.util.Patterns;
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
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ChangePass extends AppCompatActivity {

    ArrayList<String> pass = new ArrayList<>();
    EditText JPasswordCurrent,JPassChange,JnewPassChange;
    Button btnPassChange;
    RequestQueue requestQueue;
    private FirebaseAuth firebaseAuth;
    private ProgressDialog pd;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_pass);
        JPasswordCurrent = findViewById(R.id.boxChangePassPassword);
        JPassChange = findViewById(R.id.boxChangePassConfirm);
        JnewPassChange = findViewById(R.id.boxnewPass);
        btnPassChange = findViewById(R.id.btnChangepass);

        //init FirebaseAuth for use authentication
        firebaseAuth = FirebaseAuth.getInstance();

        //setup progress dialog
        pd = new ProgressDialog(this);
        pd.setMessage("Please Wait...");


        btnPassChange.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (JPasswordCurrent.length() < 1) {
                    JPasswordCurrent.setError("This field is required");
                    JPasswordCurrent.requestFocus();
                    return;
                }
                else if (JPasswordCurrent.length() < 8) {
                    JPasswordCurrent.setError("Error must contain at least 8 characters");
                    JPasswordCurrent.requestFocus();
                    return;
                }
                else if (!JPasswordCurrent.getText().toString().isEmpty()){
                    //comparePassword();
                }

                if (JnewPassChange.length() < 1) {
                    JnewPassChange.setError("This field is required");
                    JnewPassChange.requestFocus();
                    return;
                }
                if (JnewPassChange.getText().toString().equals(JPasswordCurrent.getText().toString())){
                    JnewPassChange.setError("You have entered the same password");
                    JnewPassChange.requestFocus();
                    return;
                }
                if (JnewPassChange.length() < 8) {
                    JnewPassChange.setError("Error must contain at least 8 characters");
                    JnewPassChange.requestFocus();
                    return;
                }
                if (JPassChange.length() < 1) {
                    JPassChange.setError("Error must contain at least 8 characters");
                    JPassChange.requestFocus();
                    return;
                }
                if (JPassChange.length() < 8) {
                    JPassChange.setError("Error must contain at least 8 characters");
                    JPassChange.requestFocus();
                    return;
                }
                if (!JnewPassChange.getText().toString().equals(JPassChange.getText().toString()))
                {
                    JPassChange.setError("Password does not match");
                    JPassChange.requestFocus();
                }else {
                    updatePassword(JPasswordCurrent.getText().toString(),JnewPassChange.getText().toString());

                }
            }
        });
    }
    public void changePassBack(View v){
        super.onBackPressed();
    }

    public void changePass(){
        final String user_name  = PreferenceUtils.getEmail(ChangePass.this);
        String updateURL = "https://gascojanreb.000webhostapp.com/ShopifyCAB/updatePassword.php";
        requestQueue = Volley.newRequestQueue(getApplicationContext());//manages worker threads for running the network operations
        StringRequest updateRequest = new StringRequest(Request.Method.POST, updateURL, new Response.Listener<String>() {
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

                parameters.put("password",JPassChange.getText().toString());
                parameters.put("username",user_name);

                return parameters;
            }
        };
        requestQueue.add(updateRequest);

    }
    public void comparePassword(){

        final String user_name  = PreferenceUtils.getEmail(ChangePass.this);
        String updateURL = "https://gascojanreb.000webhostapp.com/ShopifyCAB/getPassword.php";
        requestQueue = Volley.newRequestQueue(getApplicationContext());//manages worker threads for running the network operations
        StringRequest getRequest = new StringRequest(Request.Method.POST, updateURL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {//gets the php json responses
                if (response.trim().equals("failed")){
                    JPasswordCurrent.setError("Password does not match");
                    JPasswordCurrent.requestFocus();
                    return;
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

                parameters.put("enteredPass",JPasswordCurrent.getText().toString());
                parameters.put("username",user_name);

                return parameters;
            }
        };
        requestQueue.add(getRequest);



    }
    public static String md5(final String s) {
        final String MD5 = "MD5";
        try {
            // Create MD5 Hash
            MessageDigest digest = java.security.MessageDigest
                    .getInstance(MD5);
            digest.update(s.getBytes());
            byte messageDigest[] = digest.digest();

            // Create Hex String
            StringBuilder hexString = new StringBuilder();
            for (byte aMessageDigest : messageDigest) {
                String h = Integer.toHexString(0xFF & aMessageDigest);
                while (h.length() < 2)
                    h = "0" + h;
                hexString.append(h);
            }
            return hexString.toString();

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return "";
    }
    //firebase updatePass
    private void updatePassword(String oldPassword, final String newPassword) {
        //show dialog
        pd.show();

        //get current user
        final FirebaseUser user = firebaseAuth.getCurrentUser();

        //before changing password re-authenticate the user
        AuthCredential authCredential = EmailAuthProvider.getCredential(user.getEmail(), oldPassword);
        user.reauthenticate(authCredential)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        //successfully authenticated, begin update

                        user.updatePassword(newPassword)
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        //password updated
                                        changePass();
                                        pd.dismiss();
                                        Toast.makeText(getApplicationContext(), "Password Updated...", Toast.LENGTH_SHORT).show();
                                        ChangePass.super.onBackPressed();
                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        //failed updating password, show reason
                                        pd.dismiss();
                                        Toast.makeText(getApplicationContext(), "" + e.getMessage(), Toast.LENGTH_LONG).show();
                                    }
                                });
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        //authentication failed, show reason
                        pd.dismiss();
                        Toast.makeText(getApplicationContext(), "" + e.getMessage(), Toast.LENGTH_LONG).show();
                    }
                });
    }
}