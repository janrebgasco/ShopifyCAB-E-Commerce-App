package com.clickawaybuying.shopify;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.clickawaybuying.shopify.utils.PreferenceUtils;

public class pin extends AppCompatActivity {
    EditText pin;
    TextView txt,textView,txtHere;
    String PIN2;
    Boolean fromCheckout = false,fromSplash = false;
    int attempts = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pin);
        pin = findViewById(R.id.txtBoxPin);
        txt = findViewById(R.id.txtWelcome2);
        PIN2 = PreferenceUtils.getPin2(this);
        textView = findViewById(R.id.textView67);
        txtHere = findViewById(R.id.pinRelogin);

        catchIntent();
        txtHere.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PreferenceUtils.clearAll(pin.this);
                SharedPreferences colors = getSharedPreferences("COLORS", MODE_PRIVATE);
                colors.edit().clear().apply();
                SharedPreferences size = getSharedPreferences("SIZES", MODE_PRIVATE);
                size.edit().clear().apply();
                SharedPreferences img = getSharedPreferences("IMG", MODE_PRIVATE);
                img.edit().clear().apply();
                Intent i = new Intent(pin.this,LoginPage.class);
                startActivity(i);
                finish();
            }
        });

    }

    public void btnPinConfirm(View v){
        if (pin.length()<1){
            pin.setError("Enter four digit pin");
            pin.requestFocus();
            return;
        }
        String PIN = PreferenceUtils.getPin(this);
        if (PIN2 != null && fromCheckout){
            if (!pin.getText().toString().equals(PIN2)){
                pin.setError("Incorrect pin");
                pin.requestFocus();
                pin.setBackground(ContextCompat.getDrawable(this, R.drawable.red_rounded_txtbox));
            }
            else{
                finish();
            }
        }
        if (PIN != null && fromSplash){
            if (!pin.getText().toString().equals(PIN)){
                pin.setError("Incorrect pin");
                pin.requestFocus();
                pin.setBackground(ContextCompat.getDrawable(this, R.drawable.red_rounded_txtbox));
                attempts++;
                if (attempts == 3){
                    textView.setVisibility(View.VISIBLE);
                    txtHere.setVisibility(View.VISIBLE);
                }
            }
            else{
                Intent i = new Intent(this,HomeScreen.class);
                startActivity(i);
                //Toast.makeText(this, "Logged in", Toast.LENGTH_SHORT).show();
                finish();
            }
        }

    }
    @Override
    public void onBackPressed() {
        if (PIN2 !=null) {
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