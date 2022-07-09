package com.clickawaybuying.shopify;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
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
import com.clickawaybuying.shopify.Adapters.CartAdapter;
import com.clickawaybuying.shopify.utils.PreferenceUtils;
import com.google.android.gms.ads.AdError;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.rewarded.RewardItem;
import com.google.android.gms.ads.rewarded.RewardedAd;
import com.google.android.gms.ads.rewarded.RewardedAdCallback;
import com.google.android.gms.ads.rewarded.RewardedAdLoadCallback;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.AppSettingsDialog;
import pub.devrel.easypermissions.EasyPermissions;

public class wallet extends AppCompatActivity implements EasyPermissions.PermissionCallbacks{
    ConstraintLayout mCons;
    ImageButton qrScanner,mTransfer;
    TextView mBalance,mID;
    double balance2;
    String formattedDate,date2="empty",PIN,PATTERN;
    Button mClaimTaskLogin,mClaimAds;
    Boolean hasFingerPrint;
    private InterstitialAd mInterstitialAd;
    private RewardedAd rewardedAd;
    CardView imgLock;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wallet);
        mCons = findViewById(R.id.walletCons);
        BottomNavigationView bot_nav = findViewById(R.id.buttom_navigation);
        qrScanner = findViewById(R.id.scan);
        mBalance = findViewById(R.id.balance);
        mID = findViewById(R.id.user_id);
        mTransfer = findViewById(R.id.transfer);
        mClaimTaskLogin = findViewById(R.id.btnClaim2);
        PIN = PreferenceUtils.getPin2(wallet.this);
        PATTERN = PreferenceUtils.getPattern2(wallet.this);
        hasFingerPrint = PreferenceUtils.getFprint2(wallet.this);
        mClaimAds = findViewById(R.id.btnClaim1);
        imgLock = findViewById(R.id.walletLockImg);

        final String user_name  = PreferenceUtils.getEmail(wallet.this);
        if (user_name==null){
            mCons.setVisibility(View.INVISIBLE);
            imgLock.setVisibility(View.VISIBLE);
            Toast.makeText(this,"Login first to use this feature",Toast.LENGTH_LONG).show();

        }
        else {
            lockWallet();
            getCurrentDate();
            dispWalletInfo();
            checkLoginTask();
        }


        SharedPreferences sharedPrefs = getSharedPreferences("Tasks", MODE_PRIVATE);
        boolean claimed = sharedPrefs.getBoolean("KEY_LOG_IN_TASK"+user_name, false);
        if (claimed){
            mClaimTaskLogin.setBackground(ContextCompat.getDrawable(wallet.this, R.drawable.grey_rounded_button));
            mClaimTaskLogin.setText("Claimed");
            mClaimTaskLogin.setTextSize(14);
            mClaimTaskLogin.setClickable(false);
        }
        else{
            mClaimTaskLogin.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mClaimTaskLogin.setBackground(ContextCompat.getDrawable(wallet.this, R.drawable.grey_rounded_button));
                    mClaimTaskLogin.setText("Claimed");
                    mClaimTaskLogin.setTextSize(14);
                    mClaimTaskLogin.setClickable(false);
                    SharedPreferences.Editor editor = getSharedPreferences("Tasks", MODE_PRIVATE).edit();
                    editor.putBoolean("KEY_LOG_IN_TASK"+user_name, true);
                    editor.apply();
                    updateLoginTask(formattedDate);
                    updateBalance(.25);
                }
            });
        }
        rewardedAd = new RewardedAd(this,
                "ca-app-pub-3940256099942544/5224354917");
        RewardedAdLoadCallback adLoadCallback = new RewardedAdLoadCallback() {
            @Override
            public void onRewardedAdLoaded() {
                // Ad successfully loaded.
            }

            @Override
            public void onRewardedAdFailedToLoad(LoadAdError adError) {
                // Ad failed to load.
            }
        };
        rewardedAd.loadAd(new AdRequest.Builder().build(), adLoadCallback);
        //interstitialAD
        mInterstitialAd = new InterstitialAd(this);
        mInterstitialAd.setAdUnitId("ca-app-pub-3940256099942544/1033173712");
        mInterstitialAd.loadAd(new AdRequest.Builder().build());

        qrScanner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openCamera();

            }
        });

        mTransfer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openTransfer();
            }
        });
        mClaimAds.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (rewardedAd.isLoaded()) {
                    Activity activityContext = wallet.this;
                    RewardedAdCallback adCallback = new RewardedAdCallback() {
                        @Override
                        public void onRewardedAdOpened() {
                            // Ad opened.
                        }

                        @Override
                        public void onRewardedAdClosed() {
                            // Ad closed.
                        }

                        @Override
                        public void onUserEarnedReward(@NonNull RewardItem reward) {
                            // User earned reward.
                            updateBalance(.10);
                            dispWalletInfo();
                        }

                        @Override
                        public void onRewardedAdFailedToShow(AdError adError) {
                            // Ad failed to display.
                            Toast.makeText(wallet.this, "Failed to load ADS.\nPlease try again later.", Toast.LENGTH_SHORT).show();
                        }
                    };
                    rewardedAd.show(activityContext, adCallback);
                }
                else if(balance2 >= 50000){
                    Toast.makeText(wallet.this, "Wallet limit has been exceeded", Toast.LENGTH_SHORT).show();
                }
                else {
                    Toast.makeText(wallet.this, "Failed to load ADS.\nPlease try again later.", Toast.LENGTH_SHORT).show();
                }
            }
        });


        bot_nav.setVisibility(View.VISIBLE);
        //Set wallet.class Selected
        bot_nav.setSelectedItemId(R.id.wallet);
        //perform item seleted listener
        bot_nav.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.chat:
                        startActivity(new Intent(getApplicationContext()
                                ,Chat.class));
                        overridePendingTransition(0,0);finish();
                        return true;
                    case R.id.homeScreen:
                        startActivity(new Intent(getApplicationContext()
                                ,HomeScreen.class));
                        overridePendingTransition(0,0);finish();
                        return true;
                    case R.id.search_result:
                        startActivity(new Intent(getApplicationContext()
                                ,search_result.class));
                        overridePendingTransition(0,0);finish();
                        return true;
                    case R.id.wallet:
                        return true;
                    case R.id.profile:
                        startActivity(new Intent(getApplicationContext()
                                ,Profile.class));
                        overridePendingTransition(0,0);finish();
                        return true;
                }
                return false;
            }
        });
    }


    private void openTransfer() {
        Intent i = new Intent(this,Transfer.class);
        i.putExtra("balance",balance2);
        startActivity(i);
    }

    @Override
    public void onBackPressed() {
        new AlertDialog.Builder(this)
                .setMessage("Are you sure you want to exit?")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        wallet.this.finish();
                    }
                })
                .setNegativeButton("No", null)
                .show();
    }
    @AfterPermissionGranted(123)
    private void openCamera() {
        String[] perms = {Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE};
        if (EasyPermissions.hasPermissions(this, perms)) {
            Toast.makeText(this, "Opening camera", Toast.LENGTH_SHORT).show();
            Intent i = new Intent(wallet.this,ScanActivity.class);
            i.putExtra("balance",balance2);
            startActivity(i);
        } else {
            EasyPermissions.requestPermissions(this, "ShopifyCAB needs permissions to scan a barcode.",
                    123, perms);
        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }


    @Override
    public void onPermissionsGranted(int requestCode, @NonNull List<String> perms) {

    }

    @Override
    public void onPermissionsDenied(int requestCode, @NonNull List<String> perms) {
        if (EasyPermissions.somePermissionPermanentlyDenied(this, perms)) {
            new AppSettingsDialog.Builder(this).build().show();
        }
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == AppSettingsDialog.DEFAULT_SETTINGS_REQ_CODE) {
        }
    }
    public void dispWalletInfo(){
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
                            balance2 = balance;

                            DecimalFormat formatter = new DecimalFormat("###,###.##");
                            mBalance.setText("₱"+String.valueOf(formatter.format(balance)));
                            mID.setText(String.valueOf(id));
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
                    String user_name  = PreferenceUtils.getEmail(wallet.this);
                    parameters.put("username",user_name);

                    return parameters;
                }
            };
            requestQueue.add(jsonObjectRequest);
    }
    public void insertDateTask(final String date){
        final String user_name  = PreferenceUtils.getEmail(getApplicationContext());
        String insertURL = "https://gascojanreb.000webhostapp.com/ShopifyCAB/insertLoginTask.php";
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());//manages worker threads for running the network operations
        StringRequest request = new StringRequest(Request.Method.POST, insertURL, new Response.Listener<String>() {//canned request for retrieving the response body at a given URL as a String
            @Override
            public void onResponse(String response) {//gets the php json responses

            }
        }, new Response.ErrorListener() {//gets php error messages
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(wallet.this, ""+error, Toast.LENGTH_SHORT).show();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> parameters = new HashMap<String, String>();//putting parameters to pass on the php file

                parameters.put("date",date);
                parameters.put("username",user_name);


                return parameters;
            }
        };
        requestQueue.add(request);
    }
    public void checkLoginTask(){
        final String user_name  = PreferenceUtils.getEmail(getApplicationContext());
        String insertURL = "https://gascojanreb.000webhostapp.com/ShopifyCAB/getLoginTask.php";
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());//manages worker threads for running the network operations
        StringRequest request = new StringRequest(Request.Method.POST, insertURL, new Response.Listener<String>() {//canned request for retrieving the response body at a given URL as a String
            @Override
            public void onResponse(String response) {//gets the php json responses
                if (response.trim().equals("[]")){
                    insertDateTask(formattedDate);
                }
                try {
                    JSONArray array = new JSONArray(response);
                        JSONObject dateObject = array.getJSONObject(0);
                        String date = dateObject.getString("date");
                        checkDate(user_name,date);
                        date2 = date;
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {//gets php error messages
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(wallet.this, ""+error, Toast.LENGTH_SHORT).show();
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
    public void getCurrentDate(){
        Date c = Calendar.getInstance().getTime();
        System.out.println("Current time => " + c);

        SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy", Locale.getDefault());
        formattedDate = df.format(c);
    }
    public void updateLoginTask(final String date){
            final String user_name  = PreferenceUtils.getEmail(getApplicationContext());
            String insertURL = "https://gascojanreb.000webhostapp.com/ShopifyCAB/updateLoginTask.php";
            RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());//manages worker threads for running the network operations
            StringRequest request = new StringRequest(Request.Method.POST, insertURL, new Response.Listener<String>() {//canned request for retrieving the response body at a given URL as a String
                @Override
                public void onResponse(String response) {//gets the php json responses

                }
            }, new Response.ErrorListener() {//gets php error messages
                @Override
                public void onErrorResponse(VolleyError error) {
                    //Toast.makeText(wallet.this, ""+error, Toast.LENGTH_SHORT).show();
                }
            }){
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String,String> parameters = new HashMap<String, String>();//putting parameters to pass on the php file

                    parameters.put("date",date);
                    parameters.put("username",user_name);


                    return parameters;
                }
            };
            requestQueue.add(request);
    }
    public void checkDate(String user_name,String date){
        if (!date.equals(formattedDate)){
            SharedPreferences.Editor editor = getSharedPreferences("Tasks", MODE_PRIVATE).edit();
            editor.putBoolean("KEY_LOG_IN_TASK"+user_name, false);
            editor.apply();
        }
    }
    public void updateBalance(final double amount){
        String URL = "https://gascojanreb.000webhostapp.com/ShopifyCAB/QRSendMoney.php";
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        StringRequest jsonObjectRequest = new StringRequest(Request.Method.POST,
                URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {//gets the php json responses
                //Toast.makeText(wallet.this, ""+response, Toast.LENGTH_SHORT).show();
            }
        }, new Response.ErrorListener() {//gets php error messages
            @Override
            public void onErrorResponse(VolleyError error) {
                //Toast.makeText(wallet.this, ""+error, Toast.LENGTH_SHORT).show();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> parameters = new HashMap<String, String>();//putting parameters to pass on the php file
                //get Shared Preference username
                String user_name  = PreferenceUtils.getEmail(wallet.this);
                parameters.put("username",user_name);
                parameters.put("amount",String.valueOf(amount));

                return parameters;
            }
        };
        requestQueue.add(jsonObjectRequest);
        new CountDownTimer(1000, 1000) {
            public void onTick(long millisUntilFinished) {

            }
            public void onFinish() {
                dispWalletInfo();
            }
        }.start();
    }


    public void openWalletLock(View view) {
        Intent i = new Intent(this,ChooseLock.class);
        i.putExtra("walletLock",true);
        startActivity(i);
    }
    public void lockWallet(){
        if (PIN != null){
            Intent intent = new Intent(wallet.this, pin2.class);
            startActivity(intent);
        }
        if (PATTERN != null){
            Intent intent = new Intent(wallet.this, PatternLock2.class);
            startActivity(intent);
        }
        if (hasFingerPrint){
            Intent intent = new Intent(wallet.this, Fingerprint2.class);
            startActivity(intent);
        }
    }
}