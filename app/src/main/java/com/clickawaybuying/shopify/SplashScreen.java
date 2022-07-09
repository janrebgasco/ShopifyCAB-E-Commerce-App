package com.clickawaybuying.shopify;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import com.clickawaybuying.shopify.utils.PreferenceUtils;

public class SplashScreen extends AppCompatActivity {
    TextView checker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        checker = findViewById(R.id.lockidentifier);
        final String PIN = PreferenceUtils.getPin(this);
        final String PATTERN = PreferenceUtils.getPattern(this);
        final boolean hasFingerPrint = PreferenceUtils.getFprint(this);
        //Creates a splash screen
        new Thread(new Runnable() {

            @Override
            public void run() {
                // TODO Auto-generated method stub
                try {
                    Thread.sleep(3000);
                } catch (InterruptedException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                finally {
                    finish();
                }
                if (PIN != null){
                    Intent i = new Intent(SplashScreen.this, pin.class);
                    i.putExtra("fromSplash",true);
                    startActivity(i);
                }
                else if (PATTERN != null){
                    Intent i = new Intent(SplashScreen.this, PatternLock.class);
                    i.putExtra("fromSplash",true);
                    startActivity(i);
                }
                else if (hasFingerPrint){
                    Intent i = new Intent(SplashScreen.this, Fingerprint.class);
                    i.putExtra("fromSplash",true);
                    startActivity(i);
                }
                else {
                    Intent i = new Intent(SplashScreen.this, HomeScreen.class);
                    startActivity(i);
                    }
                }

        }).start();

    }
    }
