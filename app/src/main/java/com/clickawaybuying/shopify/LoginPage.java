package com.clickawaybuying.shopify;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.clickawaybuying.shopify.utils.PreferenceUtils;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Map;


public class LoginPage extends AppCompatActivity {
    public static Activity la;
    public static EditText JUsername,JPassword;
    CheckBox JCheckbox;
    private TextView BtnClkSignUp,BtnClkForgot;
    public static TextView txtView;
    FirebaseAuth mFirebaseAuth,mAuth;
    private FirebaseAuth.AuthStateListener mAuthStateListener;
    String email = "";
    ImageButton btnGoogleSignIn;
    CallbackManager mCallbackManager;
    LoginButton loginButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_page);
        JUsername = findViewById(R.id.editTextUsername);
        JPassword = findViewById(R.id.editTextTextPassword);
        BtnClkSignUp =findViewById(R.id.clkSignUp);
        BtnClkForgot = findViewById(R.id.txtForgot);
        JCheckbox = findViewById(R.id.checkBoxRemember);
        txtView = findViewById(R.id.txtEmail);
        loginButton = findViewById(R.id.fbBtn);
        btnGoogleSignIn = findViewById(R.id.googleBtn);

        Intent intent = getIntent();
        JUsername.setText(intent.getStringExtra("username"));
        JPassword.setText(intent.getStringExtra("password"));

        mFirebaseAuth = FirebaseAuth.getInstance();
        mAuth = FirebaseAuth.getInstance();
        FacebookSdk.sdkInitialize( LoginPage.this);

        // Initialize Facebook Login button
        mCallbackManager = CallbackManager.Factory.create();
        loginButton.setPermissions("email", "public_profile");
        loginButton.registerCallback(mCallbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                handleFacebookAccessToken(loginResult.getAccessToken());
                Toast.makeText(LoginPage.this, ""+loginResult, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCancel() {
            }

            @Override
            public void onError(FacebookException error) {
                Toast.makeText(LoginPage.this, ""+error, Toast.LENGTH_SHORT).show();
            }
        });

        //button press
        BtnClkSignUp.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent i= new Intent(LoginPage.this,SignUp.class);
                startActivity(i);
                finish();
            }
        });

        BtnClkForgot.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent i= new Intent(LoginPage.this,ForgotPassword.class);
                startActivity(i);
            }
        });

        la = this;
    }
    public void onLogin(View view){

        if (JUsername.length() < 1)
        {
            JUsername.setError("This field is required");
            JUsername.requestFocus();
            return;
        }
        if (JPassword.length() < 1)
        {
            JPassword.setError("This field is required");
            JPassword.requestFocus();
            return;
        }
        if (JPassword.length()<6) {
            JPassword.setError("Password must contain at least 6 letters");
            JPassword.requestFocus();
            return;
        }
        else{
            final ProgressDialog progress = new ProgressDialog(LoginPage.this);
            progress.setTitle("Loading");
            progress.setMessage("Please wait...");
            progress.setCancelable(false); // disable dismiss by tapping outside of the dialog
            progress.show();
            // To dismiss the dialog
            new CountDownTimer(2000, 1000) {
                public void onTick(long millisUntilFinished) {
                    getUsername();
                }
                public void onFinish() {
                    final String user_name = JUsername.getText().toString();
                    final String pass_word = JPassword.getText().toString();
                    if (email.equals("")){
                        Toast.makeText(LoginPage.this, "Login failed incorrect username or password", Toast.LENGTH_SHORT).show();
                    }
                    try {
                        mFirebaseAuth.signInWithEmailAndPassword(email, pass_word).addOnCompleteListener(LoginPage.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if(!task.isSuccessful()){
                                    Toast.makeText(LoginPage.this,"Login failed incorrect username or password",Toast.LENGTH_LONG).show();
                                    email = "";
                                }
                                else{
                                    if (mFirebaseAuth.getCurrentUser().isEmailVerified())
                                    {
                                        //get account info method
                                        getUserInfo();
                                        showNotif(user_name);
                                        /*Mysql Login
                                        String type = "login";
                                        BackgroudWorker backgroudWorker = new BackgroudWorker(getApplicationContext());
                                        backgroudWorker.execute(type, user_name, pass_word);
                                        */
                                    }
                                    else {
                                        Toast.makeText(LoginPage.this, "Please verify your email first.", Toast.LENGTH_LONG).show();
                                    }
                                }
                            }
                        });
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                        progress.dismiss();
                }
            }.start();


        }

    }

    private void showNotif(String user_name) {
        Toast.makeText(this, "Logged in, Welcome "+user_name, Toast.LENGTH_LONG).show();
        String title = "Welcome to Shopify CAB "+user_name;
        //push notification
        String message = "Enjoy safe shopping with thousands of good quality products.";
        NotificationCompat.Builder builder = new NotificationCompat.Builder(
                getApplicationContext()
        )
                .setSmallIcon(R.drawable.logoo)
                .setContentTitle(title)
                .setContentText(message)
                .setAutoCancel(true);

        Intent intent = new Intent(getApplicationContext(),HomeScreen.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.putExtra("message",message);

        PendingIntent pendingIntent = PendingIntent.getActivity(getApplicationContext(),0,intent,PendingIntent.FLAG_UPDATE_CURRENT);
        builder.setContentIntent(pendingIntent);

        NotificationManager notificationManager = (NotificationManager)getApplicationContext().getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(0,builder.build());
        Intent myactivity = new Intent(getApplicationContext(), HomeScreen.class);
        myactivity.addFlags(myactivity.FLAG_ACTIVITY_NEW_TASK);

        //go to Homescreen
        getApplicationContext().startActivity(myactivity);
        //finish activity in Loginpage.class
        finish();
    }
    public void getUserInfo(){
        String showURL = "https://gascojanreb.000webhostapp.com/ShopifyCAB/getAccountInfo.php";
        RequestQueue requestQueue = Volley.newRequestQueue(this.getApplicationContext());
        StringRequest jsonObjectRequest = new StringRequest(Request.Method.POST,
                showURL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {//gets the php json responses
                try {
                    JSONObject jsonObject= new JSONObject(response.toString());
                    JSONArray address = jsonObject.getJSONArray("accounts");
                    for (int i=0;i<address.length();i++){
                        JSONObject addresses = address.getJSONObject(i);

                        String id = addresses.getString("id");
                        String username = addresses.getString("username");
                        String email = addresses.getString("email");

                        //save to shared preference for login sessions
                        PreferenceUtils.saveEmail(username, getApplicationContext());
                        PreferenceUtils.saveUsername(email, getApplicationContext());
                        PreferenceUtils.saveID(id, getApplicationContext());
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
                parameters.put("username",JUsername.getText().toString());

                return parameters;
            }
        };
        requestQueue.add(jsonObjectRequest);
    }
    private void getUsername() {
        String URL = "https://gascojanreb.000webhostapp.com/ShopifyCAB/getEmail.php";
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        StringRequest jsonObjectRequest = new StringRequest(Request.Method.DEPRECATED_GET_OR_POST,
                URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {//gets the php json responses
                try {
                    JSONArray array = new JSONArray(response);

                    for (int i = 0; i<array.length();i++){
                        JSONObject productobject = array.getJSONObject(0);
                        String email = productobject.getString("email");
                        LoginPage.this.email = email;

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {//gets php error messages
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(LoginPage.this, ""+error, Toast.LENGTH_SHORT).show();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> parameters = new HashMap<String, String>();//putting parameters to pass on the php file
                //get Shared Preference username
                final String user_name = JUsername.getText().toString();
                parameters.put("username",user_name);

                return parameters;
            }
        };
        requestQueue.add(jsonObjectRequest);
    }

    @Override
    public void onBackPressed() {
        new AlertDialog.Builder(this)
                .setMessage("Are you sure you want to exit?")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        LoginPage.this.finish();
                    }
                })
                .setNegativeButton("No", null)
                .show();
    }
    private void handleFacebookAccessToken(AccessToken token) {

        AuthCredential credential = FacebookAuthProvider.getCredential(token.getToken());
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            FirebaseUser user = mAuth.getCurrentUser();
                            updateUI(user);
                        } else {
                            // If sign in fails, display a message to the user.
                            Toast.makeText(LoginPage.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                            updateUI(null);
                        }
                    }

                });
    }

    private void updateUI(FirebaseUser firebaseUser) {
        if (firebaseUser != null){
            FirebaseUser mUser = mAuth.getCurrentUser();
            String username = mUser.getDisplayName();
            String email = mUser.getEmail();
            String image = mUser.getPhotoUrl().toString();

            BackgroudWorker backgroudWorker = new BackgroudWorker(getApplicationContext());
            backgroudWorker.execute("fbLogin", username, email, image);
            //Intent i = new Intent(LoginPage.this,HomeScreen.class);
            //startActivity(i);
        }
        else{
            Toast.makeText(this, "Please sign in to continue", Toast.LENGTH_SHORT).show();
        }
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        mCallbackManager.onActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);
    }


}