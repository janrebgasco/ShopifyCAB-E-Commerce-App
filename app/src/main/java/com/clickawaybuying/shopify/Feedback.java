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
import android.os.CountDownTimer;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;
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

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

public class Feedback extends AppCompatActivity {
    TextView mProdname;
    ImageView mImage,mFbackImg;
    Button mSubmit,mUpload;
    int id = 0;
    EditText mFeedback;
    RatingBar mRatingbar;
    final int CODE_GALLERY_REQUEST = 999;
    Bitmap bitmap1;
    String uploadedImg;
    ProgressDialog progress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedback);
        mProdname = findViewById(R.id.fBackProdName);
        mImage = findViewById(R.id.fBackImage);
        mSubmit = findViewById(R.id.fBackBtn);
        mFeedback = findViewById(R.id.fbackTxt);
        mRatingbar = findViewById(R.id.fbackRating);
        mUpload = findViewById(R.id.uploadImg);
        mFbackImg = findViewById(R.id.fbackImg);

        progress = new ProgressDialog(Feedback.this);
        progress.setTitle("Loading");
        progress.setMessage("Please wait...");
        progress.setCancelable(false); // disable dismiss by tapping outside of the dialog

        Intent i = getIntent();
        String image = i.getStringExtra("image");
        String prodName = i.getStringExtra("prodName");
        id = i.getIntExtra("prodID",0);

        mProdname.setText(prodName);
        Glide.with(this).load(image).into(mImage);

        mSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mFeedback.getText().toString().isEmpty()){
                    Toast.makeText(Feedback.this, "Please fill in the feedback field."+id, Toast.LENGTH_SHORT).show();
                }else {
                    insertFeedback();
                }
            }
        });
        mUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ActivityCompat.requestPermissions(
                        Feedback.this,
                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                        CODE_GALLERY_REQUEST

                );
            }
        });

    }

    public void fedBack(View view) {
        super.onBackPressed();
    }
    public void insertFeedback() {//insert data to database
        progress.show();
        RequestQueue requestQueue;
        String URL = "https://gascojanreb.000webhostapp.com/ShopifyCAB/insertFeedback.php";
        requestQueue = Volley.newRequestQueue(getApplicationContext());//manages worker threads for running the network operations
        StringRequest jsonObjectRequest = new StringRequest(Request.Method.POST,
                URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {//gets the php json responses
                //Toast.makeText(Feedback.this,response, Toast.LENGTH_LONG).show();
                if (response.isEmpty()){
                    progress.dismiss();
                    Feedback.super.onBackPressed();
                    Toast.makeText(Feedback.this, "Feedback Submitted", Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {//gets php error messages
            @Override
            public void onErrorResponse(VolleyError error) {
                progress.dismiss();
                Toast.makeText(Feedback.this, ""+error, Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> parameters = new HashMap<String, String>();//putting parameters to pass on the php file
                uploadedImg = imagetoString(bitmap1);
                //get Shared Preference username
                String user_name = PreferenceUtils.getEmail(Feedback.this);
                parameters.put("prodID", String.valueOf(id));
                parameters.put("username", user_name);
                parameters.put("feedback", mFeedback.getText().toString());
                parameters.put("rating", String.valueOf(mRatingbar.getRating()));
                parameters.put("image", uploadedImg);

                return parameters;
            }
        };
        requestQueue.add(jsonObjectRequest);
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

    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == CODE_GALLERY_REQUEST && resultCode == RESULT_OK && data != null)
        {
            Uri filepath = data.getData();

            try {
                InputStream inputStream = getContentResolver().openInputStream(filepath);
                bitmap1 = BitmapFactory.decodeStream(inputStream);
                mFbackImg.setImageBitmap(bitmap1);
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

    }

}