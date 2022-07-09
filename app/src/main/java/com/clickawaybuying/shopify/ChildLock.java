package com.clickawaybuying.shopify;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.andrognito.patternlockview.PatternLockView;
import com.clickawaybuying.shopify.utils.Constants;
import com.clickawaybuying.shopify.utils.PreferenceUtils;

public class ChildLock extends AppCompatActivity {
    EditText Child_Lock;
    Boolean walletLock = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_child_lock);
        Child_Lock = findViewById(R.id.txtBoxChildLock);

        Intent i = getIntent();
        walletLock = i.getBooleanExtra("walletLock",false);

    }

    public void onConfirmed(View view) {
        if (Child_Lock.length() < 1) {
            Child_Lock.setError("This field is empty");
            Child_Lock.requestFocus();
            return;
        }
        if (Child_Lock.length() > 4) {
            Child_Lock.setError("Pin must contain maximum of 4 numbers");
            Child_Lock.requestFocus();
            return;
        }
        String PIN = PreferenceUtils.getPin(this);
        String PIN2 = PreferenceUtils.getPin2(this);
        if (walletLock){
            if (PIN2 != null){
                Toast.makeText(this, "Wallet PIN has already been set.\n PIN did not saved", Toast.LENGTH_LONG).show();
                Child_Lock.setBackground(ContextCompat.getDrawable(this, R.drawable.red_rounded_txtbox));
            }
            else {
                Child_Lock.setBackground(ContextCompat.getDrawable(this, R.drawable.rounded_txtbox));
                PreferenceUtils.removeFprint2(this);
                PreferenceUtils.removePattern2(this);
                String pin = Child_Lock.getText().toString();
                PreferenceUtils.savePin2(pin, this);
                Toast.makeText(this, " Wallet PIN saved", Toast.LENGTH_SHORT).show();
                super.onBackPressed();

            }
        }
        else {
            if (PIN != null){
                Toast.makeText(this, "PIN has already been set.\n PIN did not saved", Toast.LENGTH_LONG).show();
                Child_Lock.setBackground(ContextCompat.getDrawable(this, R.drawable.red_rounded_txtbox));
            }
            else {
                Child_Lock.setBackground(ContextCompat.getDrawable(this, R.drawable.rounded_txtbox));
                PreferenceUtils.removeFprint(this);
                PreferenceUtils.removePattern(this);
                String pin = Child_Lock.getText().toString();
                PreferenceUtils.savePin(pin, this);
                Toast.makeText(this, "PIN saved", Toast.LENGTH_SHORT).show();
                super.onBackPressed();

            }
        }


    }

    public void btnBack(View v) {
        super.onBackPressed();
    }

    public void onRemovePin(final View view) {
        DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case DialogInterface.BUTTON_POSITIVE:
                        //Yes button clicked
                        if (walletLock){
                            PreferenceUtils.removePin2(ChildLock.this);
                            Toast.makeText(ChildLock.this, "Wallet PIN has been removed", Toast.LENGTH_SHORT).show();
                        }
                        else {
                            PreferenceUtils.removePin(ChildLock.this);
                            Toast.makeText(ChildLock.this, "PIN lock has been removed", Toast.LENGTH_SHORT).show();
                        }
                        break;

                    case DialogInterface.BUTTON_NEGATIVE:
                        //No button clicked
                        break;
                }
            }
        };
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Are you sure you want to  remove PIN?").setPositiveButton("Yes", dialogClickListener)
                .setNegativeButton("No", dialogClickListener).show();
    }
}