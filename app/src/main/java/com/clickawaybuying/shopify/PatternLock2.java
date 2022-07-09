package com.clickawaybuying.shopify;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.andrognito.patternlockview.PatternLockView;
import com.andrognito.patternlockview.listener.PatternLockViewListener;
import com.clickawaybuying.shopify.utils.PreferenceUtils;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.List;

public class PatternLock2 extends AppCompatActivity implements PatternLockViewListener{
    private PatternLockView mPatternLockView;
    String PATTERN2;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pattern_lock2);
        mPatternLockView = findViewById(R.id.patter_lock_view);
        BottomNavigationView bot_nav = findViewById(R.id.buttom_navigation);

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

        mPatternLockView.addPatternLockListener(this);
        PATTERN2 = PreferenceUtils.getPattern2(this);

    }

    @Override
    public void onStarted() {

    }

    @Override
    public void onProgress(List<PatternLockView.Dot> progressPattern) {

    }


    @Override
    public void onComplete(List<PatternLockView.Dot> pattern) {

        if (PATTERN2 != null){
            if (mPatternLockView.getPattern().toString().equals(PATTERN2)){
                mPatternLockView.setViewMode(PatternLockView.PatternViewMode.CORRECT);
                finish();
            }
            else{
                mPatternLockView.setViewMode(PatternLockView.PatternViewMode.WRONG);
                Toast.makeText(this, "Wrong Password", Toast.LENGTH_SHORT).show();
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
}