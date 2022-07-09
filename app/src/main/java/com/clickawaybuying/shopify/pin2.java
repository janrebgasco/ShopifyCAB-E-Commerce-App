package com.clickawaybuying.shopify;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.clickawaybuying.shopify.utils.PreferenceUtils;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class pin2 extends AppCompatActivity {
    EditText pin;
    TextView txt;
    String PIN2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pin2);
        pin = findViewById(R.id.txtBoxPin);
        txt = findViewById(R.id.txtWelcome2);
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

    }

    public void btnPinConfirm(View v){
        if (pin.length()<1){
            pin.setError("Enter four digit pin");
            pin.requestFocus();
            return;
        }
        PIN2 = PreferenceUtils.getPin2(this);
            if (!pin.getText().toString().equals(PIN2)){
                pin.setError("Incorrect pin");
                pin.requestFocus();
                pin.setBackground(ContextCompat.getDrawable(this, R.drawable.red_rounded_txtbox));
            }
            else{
                finish();
            }

    }
    @Override
    public void onBackPressed() {
            finish();
    }
}