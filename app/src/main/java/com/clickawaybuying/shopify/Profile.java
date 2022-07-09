package com.clickawaybuying.shopify;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.NotificationCompat;
import androidx.core.content.ContextCompat;

import android.app.AlertDialog;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
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
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class Profile extends AppCompatActivity {
    public static ImageView click,prof_pic,GotoAddress,GotoSeller;
    public static TextView Name_Acc,Email_Acc,here,textnothing;
    CardView mLogout,mPrivacy,mChangePass,mOrder,mMyAccount,mAddress,mSeller,mTips;
    RequestQueue requestQueue;
    ImageButton mSell;
    CardView imglock;
    ConstraintLayout PanelMyAcc,PanelOrders,PanelChangePass,PanelPrivacy,PanelLogout,PanelAddress,PanelSeller,PanelTips;
    Context context;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        BottomNavigationView bot_nav = findViewById(R.id.buttom_navigation);
        click = findViewById(R.id.orders);
        Name_Acc = findViewById(R.id.txtName);
        Email_Acc = findViewById(R.id.profileEmail);
        prof_pic = findViewById(R.id.profileimaged);
        mLogout= findViewById(R.id.logoutPanel);
        mPrivacy = findViewById(R.id.privacyPanel);
        mChangePass = findViewById(R.id.changepass);
        mOrder = findViewById(R.id.orderPanel);
        mMyAccount = findViewById(R.id.myaccPanel);
        mSell = findViewById(R.id.startSelling);
        PanelMyAcc = findViewById(R.id.MyAccountPanel);
        PanelOrders = findViewById(R.id.ordersPanel);
        PanelChangePass = findViewById(R.id.changePassPanel);
        PanelPrivacy = findViewById(R.id.privacyPanel2);
        PanelLogout = findViewById(R.id.logoutPanel2);
        PanelAddress = findViewById(R.id.AddressPanel);
        GotoAddress = findViewById(R.id.address);
        PanelSeller = findViewById(R.id.SellerPanel);
        GotoSeller = findViewById(R.id.gotoSellerProd);
        mAddress = findViewById(R.id.addressPanel);
        mSeller = findViewById(R.id.sellerPanel);
        imglock = findViewById(R.id.profileLockImg);
        mTips = findViewById(R.id.tipsPanel);
        PanelTips = findViewById(R.id.tipsPanel2);

        if(PreferenceUtils.getSlrShopname(this)!= null){
            //Toast.makeText(getApplicationContext(), "May notif dapat", Toast.LENGTH_SHORT).show();
            String title = "You are now a Seller";
             //push notification
            String message = "Enjoy selling and start earning.";
            NotificationCompat.Builder builder = new NotificationCompat.Builder(
                    getApplicationContext()
            )
                    .setSmallIcon(R.drawable.market_svg)
                    .setContentTitle(title)
                    .setContentText(message)
                    .setAutoCancel(true);

            NotificationManager notificationManager = (NotificationManager)getApplicationContext().getSystemService(NOTIFICATION_SERVICE);
            notificationManager.notify(0,builder.build());
        }



        here=findViewById(R.id.heretxt);
        textnothing = findViewById(R.id.textView37);


        PanelMyAcc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(Profile.this,MyAccount.class);
                startActivity(i);
            }
        });
        PanelOrders.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(Profile.this,Orders.class);
                startActivity(i);
            }
        });
        PanelChangePass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(Profile.this,ChangePass.class);
                startActivity(i);
            }
        });
        PanelPrivacy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(Profile.this,ChooseLock.class);
                startActivity(i);
            }
        });
        PanelAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(Profile.this,ChooseAddress.class);
                i.putExtra("fromPanelAddress",true);
                startActivity(i);
            }
        });
        GotoAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(Profile.this,ChooseAddress.class);
                startActivity(i);
            }
        });
        PanelSeller.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(Profile.this,sellerProducts.class);
                startActivity(i);
            }
        });
        GotoSeller.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(Profile.this,sellerProducts.class);
                startActivity(i);
            }
        });
        PanelTips.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(Profile.this,TipsAndTricks.class);
                startActivity(i);
            }
        });
        PanelLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                new AlertDialog.Builder(Profile.this)
                        .setMessage("Are you sure you want to logout?")
                        .setCancelable(false)
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                PreferenceUtils.removeEmail(Profile.this);
                                PreferenceUtils.removePin(Profile.this);
                                PreferenceUtils.removePassword(Profile.this);
                                PreferenceUtils.removeUsername(Profile.this);
                                PreferenceUtils.clearAll(Profile.this);
                                SharedPreferences colors = getSharedPreferences("COLORS", MODE_PRIVATE);
                                colors.edit().clear().apply();
                                SharedPreferences size = getSharedPreferences("SIZES", MODE_PRIVATE);
                                size.edit().clear().apply();
                                SharedPreferences img = getSharedPreferences("IMG", MODE_PRIVATE);
                                img.edit().clear().apply();
                                FirebaseAuth.getInstance().signOut();
                                Intent i =new Intent(Profile.this,HomeScreen.class);
                                startActivity(i);
                                Profile.this.finish();
                            }
                        })
                        .setNegativeButton("No", null)
                        .show();

            }
        });

        click.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent i= new Intent(Profile.this,Orders.class);
                startActivity(i);
            }
        });

        Email_Acc.setVisibility(View.INVISIBLE);


        String user_name = PreferenceUtils.getEmail(this);
        String email = PreferenceUtils.getUsername(this);
        if (user_name==null){
            mLogout.setVisibility(View.GONE);
            mPrivacy.setVisibility(View.GONE);
            mChangePass.setVisibility(View.GONE);
            mOrder.setVisibility(View.GONE);
            mMyAccount.setVisibility(View.GONE);
            mSell.setVisibility(View.GONE);
            mSeller.setVisibility(View.GONE);
            mAddress.setVisibility(View.GONE);
            mTips.setVisibility(View.GONE);
            int random = new Random().nextInt(61123) + 20412;
            String alphabet = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
            Random rnd = new Random();
            char randomChar = alphabet.charAt(rnd.nextInt(alphabet.length()));
            Name_Acc.setText("User"+random+randomChar);
            imglock.setVisibility(View.VISIBLE);

        }
        else{
            //show image,email,fullname
            showInfo();
        }
        if (user_name!=null){
            here.setVisibility(View.INVISIBLE);
            textnothing.setVisibility(View.INVISIBLE);
        }
        here.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(Profile.this,LoginPage.class);
                startActivity(i);
                finish();
            }
        });
        //Set Home Selected
        bot_nav.setSelectedItemId(R.id.profile);
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
                        startActivity(new Intent(getApplicationContext()
                                , wallet.class));
                        overridePendingTransition(0,0);finish();
                        return true;
                    case R.id.profile:
                        return true;
                }
                return false;
            }
        });
    }
    public void accountClick(View view){
        Intent i = new Intent(Profile.this,MyAccount.class);
        startActivity(i);
    }
    public void btnchangepass(View view){
        Intent i = new Intent(Profile.this,ChangePass.class);
        startActivity(i);
    }
    @Override
    public void onBackPressed() {
        new AlertDialog.Builder(this)
                .setMessage("Are you sure you want to exit?")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        Profile.this.finish();
                    }
                })
                .setNegativeButton("No", null)
                .show();
    }
    public void btnPrivacy(View view){
        Intent i = new Intent(Profile.this,ChooseLock.class);
        startActivity(i);
    }
    public void btnLogout(View view){

        new AlertDialog.Builder(this)
                .setMessage("Are you sure you want to logout?")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        PreferenceUtils.removeEmail(Profile.this);
                        PreferenceUtils.removePin(Profile.this);
                        PreferenceUtils.removePassword(Profile.this);
                        PreferenceUtils.removeUsername(Profile.this);
                        PreferenceUtils.clearAll(Profile.this);
                        SharedPreferences colors = getSharedPreferences("COLORS", MODE_PRIVATE);
                        colors.edit().clear().apply();
                        SharedPreferences size = getSharedPreferences("SIZES", MODE_PRIVATE);
                        size.edit().clear().apply();
                        SharedPreferences img = getSharedPreferences("IMG", MODE_PRIVATE);
                        img.edit().clear().apply();
                        Intent i =new Intent(Profile.this,HomeScreen.class);
                        startActivity(i);
                        Profile.this.finish();

                    }
                })
                .setNegativeButton("No", null)
                .show();

    }
    public void startSellingProduct(View view){
        if(PreferenceUtils.getSlrShopname(this) == null){
            Intent i = new Intent(this,sellerRegistration.class);
            startActivity(i);
        }
        else{
            Intent i = new Intent(this,SellProduct.class);
            startActivity(i);
        }
    }
    public void showInfo(){
        Email_Acc.setVisibility(View.VISIBLE);

        final String user_name  = PreferenceUtils.getEmail(Profile.this);
        String insertURL = "https://gascojanreb.000webhostapp.com/ShopifyCAB/showInfo.php";

        requestQueue = Volley.newRequestQueue(getApplicationContext());//manages worker threads for running the network operations
        StringRequest request = new StringRequest(Request.Method.DEPRECATED_GET_OR_POST, insertURL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {//gets the php json responses

                try {
                    JSONArray array = new JSONArray(response);

                    for (int i = 0; i<array.length();i++){
                        JSONObject account = array.getJSONObject(i);
                        String email = account.getString("email");
                        String fullname = account.getString("fullname");
                        String image = account.getString("image");


                        Email_Acc.setText(email);

                        if (!fullname.equals("null")){
                            Name_Acc.setText(fullname);
                        }else{
                            Name_Acc.setText(user_name);
                        }
                        if(!image.equals("null")){
                            try {
                                Glide.with(Profile.this).load(image).into(prof_pic);
                                prof_pic.setBackground(ContextCompat.getDrawable(Profile.this,R.color.grey2));
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
    public void openTipsAndTricks(View view){
        Intent intent =  new Intent(this,TipsAndTricks.class);
        startActivity(intent);
    }


}