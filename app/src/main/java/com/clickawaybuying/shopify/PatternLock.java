package com.clickawaybuying.shopify;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.andrognito.patternlockview.PatternLockView;
import com.andrognito.patternlockview.listener.PatternLockViewListener;
import com.andrognito.patternlockview.utils.PatternLockUtils;
import com.clickawaybuying.shopify.utils.PreferenceUtils;

import java.util.List;

public class PatternLock extends AppCompatActivity implements PatternLockViewListener{
    private PatternLockView mPatternLockView;
    String PATTERN2,PATTERN;
    Boolean fromCheckout = false,fromSplash = false;
    TextView textView,txtHere;
    int attempts = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pattern_lock);
        mPatternLockView = findViewById(R.id.patter_lock_view);
        textView = findViewById(R.id.textView66);
        txtHere = findViewById(R.id.patternRelogin);

        catchIntent();
        mPatternLockView.addPatternLockListener(this);
        PATTERN2 = PreferenceUtils.getPattern2(this);
        PATTERN = PreferenceUtils.getPattern(this);

        txtHere.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                relogin();
            }
        });

    }

    private void relogin() {
        PreferenceUtils.clearAll(this);
        SharedPreferences colors = getSharedPreferences("COLORS", MODE_PRIVATE);
        colors.edit().clear().apply();
        SharedPreferences size = getSharedPreferences("SIZES", MODE_PRIVATE);
        size.edit().clear().apply();
        SharedPreferences img = getSharedPreferences("IMG", MODE_PRIVATE);
        img.edit().clear().apply();
        Intent i = new Intent(PatternLock.this,LoginPage.class);
        startActivity(i);
        finish();
    }

    @Override
    public void onStarted() {

    }

    @Override
    public void onProgress(List<PatternLockView.Dot> progressPattern) {

    }


    @Override
    public void onComplete(List<PatternLockView.Dot> pattern) {
        if (PATTERN2 != null && fromCheckout){
            if (mPatternLockView.getPattern().toString().equals(PATTERN2)){
                mPatternLockView.setViewMode(PatternLockView.PatternViewMode.CORRECT);
                finish();
            }
            else{
                mPatternLockView.setViewMode(PatternLockView.PatternViewMode.WRONG);
                Toast.makeText(this, "Wrong Password", Toast.LENGTH_SHORT).show();
            }
        }
        if (PATTERN != null && fromSplash){
            if (mPatternLockView.getPattern().toString().equals(PATTERN)){
                mPatternLockView.setViewMode(PatternLockView.PatternViewMode.CORRECT);
                //Toast.makeText(this, "Logged in", Toast.LENGTH_SHORT).show();
                Intent i = new Intent(this,HomeScreen.class);
                startActivity(i);
                finish();
            }
            else{
                mPatternLockView.setViewMode(PatternLockView.PatternViewMode.WRONG);
                Toast.makeText(this, "Wrong Password", Toast.LENGTH_SHORT).show();
                attempts++;
                if (attempts == 3){
                    textView.setVisibility(View.VISIBLE);
                    txtHere.setVisibility(View.VISIBLE);
                }
            }
        }

        new CountDownTimer(1500, 1000) {
            public void onTick(long millisUntilFinished) {

            }
            public void onFinish() {
                mPatternLockView.clearPattern();
            }
        }.start();
    }

    @Override
    public void onCleared() {
    }
    @Override
    public void onBackPressed() {
        if (PATTERN2 != null) {
            CheckOut.mPaymentOptions.setSelection(0);
        }
        finish();
    }
    private void catchIntent() {
        Intent i = getIntent();
        fromCheckout = i.getBooleanExtra("fromCheckout",false);
        fromSplash = i.getBooleanExtra("fromSplash",false);
    }

}