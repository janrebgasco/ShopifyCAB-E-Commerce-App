package com.clickawaybuying.shopify;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

public class Ordered extends AppCompatActivity {
    ImageView mImage;
    public static String image;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ordered);

    }
    public void onBackPressed(){
        Intent i = new Intent(this,HomeScreen.class);
        startActivity(i);
    }
    public void viewOrders(View v){

        Intent i = new Intent(this,Orders.class);
        startActivity(i);
    }
}