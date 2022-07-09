package com.clickawaybuying.shopify;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
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
import com.clickawaybuying.shopify.classes.Product;
import com.clickawaybuying.shopify.utils.PreferenceUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class sellerRegistration extends AppCompatActivity {
    public static TextView mTxt1,mTxt2,mAddress;
    ImageView mImgSeller,mImgID,mAdd1,mAdd2;
    ImageButton mImgBtn1,mImgBtn2;
    EditText mShopName;
    Button btnRegister;
    CheckBox mCheckBox;
    final int CODE_GALLERY_REQUEST = 999;
    final int CODE_GALLERY_REQUEST2 = 1000;
    Bitmap bitmap1,bitmap2,bitmap;
    ProgressDialog progress;
    RequestQueue requestQueue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seller_registration);
        mShopName = findViewById(R.id.shopName);
        mAddress = findViewById(R.id.slrAddress);
        mImgBtn1 = findViewById(R.id.btnSlrImg);
        mImgBtn2 = findViewById(R.id.btnSlrIDImg);
        mImgSeller = findViewById(R.id.slrImg);
        mImgID = findViewById(R.id.slrIDImg);
        mTxt1 = findViewById(R.id.txtUserImg);
        mTxt2 = findViewById(R.id.textView412);
        mAdd1 = findViewById(R.id.addIcon);
        mAdd2 = findViewById(R.id.addIcon2);
        btnRegister = findViewById(R.id.slrRegisterBtn);
        mCheckBox = findViewById(R.id.slrcheckBoxProceed);


        progress = new ProgressDialog(sellerRegistration.this);
        progress.setTitle("Loading");
        progress.setMessage("Please wait...");
        progress.setCancelable(false); // disable dismiss by tapping outside of the dialog

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                condition();
            }
        });
        mImgBtn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ActivityCompat.requestPermissions(
                        sellerRegistration.this,
                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                        CODE_GALLERY_REQUEST

                );
            }
        });
        mImgBtn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ActivityCompat.requestPermissions(
                        sellerRegistration.this,
                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                        CODE_GALLERY_REQUEST2

                );
            }
        });

    }

    private void condition() {
        if (mShopName.length()<3){
            mShopName.setError("Please enter your shop name\nMinimum of 3 characters");
            return;
        }
        if (mAddress.length()<=0){
            Toast.makeText(this, "Insert your address to continue", Toast.LENGTH_SHORT).show();
            return;
        }
        if (mImgSeller.getDrawable() == null){
            Toast.makeText(this, "Upload your shop image to continue", Toast.LENGTH_SHORT).show();
            return;
        }
        if (mImgID.getDrawable() == null){
            Toast.makeText(this, "Upload a valid id to continue", Toast.LENGTH_SHORT).show();
            return;
        }
        if (!mCheckBox.isChecked()){
            Toast.makeText(this, "Agree to the terms and conditions to continue", Toast.LENGTH_SHORT).show();
        }
        else{
            insertToDB();
        }
    }

    private void insertToDB() {
        progress.show();

        String URL = "https://gascojanreb.000webhostapp.com/ShopifyCAB/sellerRegistration.php";
        requestQueue = Volley.newRequestQueue(getApplicationContext());//manages worker threads for running the network operations
        StringRequest jsonObjectRequest = new StringRequest(Request.Method.POST,
                URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {//gets the php json responses
                //Toast.makeText(sellerRegistration.this,response, Toast.LENGTH_LONG).show();
                progress.dismiss();
                if (response.trim().equals("regsuccess")){
                    PreferenceUtils.saveSlrShopname(mShopName.getText().toString(),getApplicationContext());
                    PreferenceUtils.saveSlrImage(imagetoString(bitmap1),getApplicationContext());
                    PreferenceUtils.saveSlrID(imagetoString(bitmap2),getApplicationContext());
                    Intent i = new Intent(sellerRegistration.this, Profile.class);
                    startActivity(i);
                    sellerRegistration.this.finish();
                }
            }

        }, new Response.ErrorListener() {//gets php error messages
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(sellerRegistration.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                dismiss();
            }
            private void dismiss(){
                progress.dismiss();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> parameters = new HashMap<String, String>();//putting parameters to pass on the php file
                String shopImage = imagetoString(bitmap1);
                String idImage = imagetoString(bitmap2);
                String user_name  = PreferenceUtils.getEmail(sellerRegistration.this);

                parameters.put("shopName", mShopName.getText().toString());
                parameters.put("idImage", idImage);
                parameters.put("image", shopImage);
                parameters.put("seller",user_name);
                parameters.put("sellerAddress",mAddress.getText().toString());
                return parameters;
            }
        };
        requestQueue.add(jsonObjectRequest);
    }

    public void slrBack(View v){
        super.onBackPressed();
    }//back button
    public void slrChooseAddress(View view) {
        Intent i = new Intent(sellerRegistration.this,ChooseAddress.class);
        i.putExtra("fromSellProd",true);
        startActivity(i);
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == CODE_GALLERY_REQUEST){
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                Intent i = new Intent(Intent.ACTION_PICK);
                i.setType("image/*");
                startActivityForResult(Intent.createChooser(i, "Select Image"),CODE_GALLERY_REQUEST);

            }
            else{
                Toast.makeText(this,"You don't have permission to gallery!", Toast.LENGTH_LONG).show();
            }
            return;
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == CODE_GALLERY_REQUEST2){
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                Intent i = new Intent(Intent.ACTION_PICK);
                i.setType("image/*");
                startActivityForResult(Intent.createChooser(i, "Select Image"),CODE_GALLERY_REQUEST2);

            }
            else{
                Toast.makeText(this,"You don't have permission to gallery!", Toast.LENGTH_LONG).show();
            }
            return;
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == CODE_GALLERY_REQUEST && resultCode == RESULT_OK && data != null)
        {
            Uri filepath = data.getData();

            try {
                InputStream inputStream = getContentResolver().openInputStream(filepath);
                bitmap1 = BitmapFactory.decodeStream(inputStream);
                //mImgSeller.setBackground(null);
                mImgSeller.setImageBitmap(bitmap1);
                mTxt1.setVisibility(View.INVISIBLE);
                mAdd1.setVisibility(View.INVISIBLE);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }

        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CODE_GALLERY_REQUEST2 && resultCode == RESULT_OK && data != null)
        {
            Uri filepath = data.getData();

            try {
                InputStream inputStream = getContentResolver().openInputStream(filepath);
                bitmap2 = BitmapFactory.decodeStream(inputStream);
                //mImgID.setBackground(null);
                mImgID.setImageBitmap(bitmap2);
                mTxt2.setVisibility(View.INVISIBLE);
                mAdd2.setVisibility(View.INVISIBLE);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }

        super.onActivityResult(requestCode, resultCode, data);
    }
    private String imagetoString(Bitmap bitmap){
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream);
        byte[] imageBytes = outputStream.toByteArray();

        String encodedImage = Base64.encodeToString(imageBytes, Base64.DEFAULT);
        return encodedImage;

    }private void loadSellerDetails(){
        String URL = "https://gascojanreb.000webhostapp.com/ShopifyCAB/getSellerInfo.php";
        requestQueue = Volley.newRequestQueue(getApplicationContext());//manages worker threads for running the network operations
        StringRequest jsonObjectRequest = new StringRequest(Request.Method.POST,
                URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {//gets the php json responses
                Toast.makeText(sellerRegistration.this,response, Toast.LENGTH_LONG).show();
                try {
                    JSONArray array = new JSONArray(response);

                    for (int i = 0; i<array.length();i++){
                        JSONObject productobject = array.getJSONObject(i);
                        String storeName = productobject.getString("storeName");
                        String storeAddress = productobject.getString("storeAddress");
                        String storeImg = productobject.getString("storeName");
                        String sellerValidId = productobject.getString("storeName");

                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

        }, new Response.ErrorListener() {//gets php error messages
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(sellerRegistration.this, error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> parameters = new HashMap<String, String>();//putting parameters to pass on the php file
                String user_name  = PreferenceUtils.getEmail(sellerRegistration.this);

                parameters.put("username",user_name);
                return parameters;
            }
        };
        requestQueue.add(jsonObjectRequest);
    }


}