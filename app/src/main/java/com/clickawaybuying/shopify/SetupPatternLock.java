package com.clickawaybuying.shopify;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.Toast;

import com.andrognito.patternlockview.PatternLockView;
import com.andrognito.patternlockview.listener.PatternLockViewListener;
import com.andrognito.patternlockview.utils.PatternLockUtils;
import com.clickawaybuying.shopify.utils.PreferenceUtils;

import java.util.List;

public class SetupPatternLock extends AppCompatActivity implements PatternLockViewListener {
    private PatternLockView mPatternLockView;
    Boolean walletLock = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setup_pattern_lock);
        mPatternLockView = findViewById(R.id.addPattern);

        mPatternLockView.addPatternLockListener(this);
        Intent i = getIntent();
        walletLock = i.getBooleanExtra("walletLock",false);
    }


    @Override
    public void onStarted() {

    }

    @Override
    public void onProgress(List<PatternLockView.Dot> progressPattern) {

    }

    @Override
    public void onComplete(List<PatternLockView.Dot> pattern) {
        if (mPatternLockView.getPattern().size() < 4){
            mPatternLockView.setViewMode(PatternLockView.PatternViewMode.WRONG);
            Toast.makeText(this, "Weak Pattern", Toast.LENGTH_SHORT).show();
        }
        if (walletLock){
            String PATTERN2 = PreferenceUtils.getPattern2(this);
            if (PATTERN2 != null){
                mPatternLockView.setViewMode(PatternLockView.PatternViewMode.WRONG);
                Toast.makeText(this, "Wallet Pattern has already been set", Toast.LENGTH_LONG).show();
            }
            else
            {
                PreferenceUtils.removeFprint2(this);
                PreferenceUtils.removePin2(this);
                mPatternLockView.setViewMode(PatternLockView.PatternViewMode.CORRECT);
                Toast.makeText(this, "Wallet Pattern has been set", Toast.LENGTH_SHORT).show();
                PreferenceUtils.savePattern2(mPatternLockView.getPattern().toString(), SetupPatternLock.this);
                super.onBackPressed();
            }
        }
        else {
            String PATTERN = PreferenceUtils.getPattern(this);
            if (PATTERN != null){
                mPatternLockView.setViewMode(PatternLockView.PatternViewMode.WRONG);
                Toast.makeText(this, "Pattern has already been set", Toast.LENGTH_LONG).show();
            }
            else
            {
                PreferenceUtils.removeFprint(this);
                PreferenceUtils.removePin(this);
                mPatternLockView.setViewMode(PatternLockView.PatternViewMode.CORRECT);
                Toast.makeText(this, "Pattern has been set", Toast.LENGTH_SHORT).show();
                PreferenceUtils.savePattern(mPatternLockView.getPattern().toString(), SetupPatternLock.this);
                super.onBackPressed();
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


    public void removePattern(View view) {
            DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    switch (which) {
                        case DialogInterface.BUTTON_POSITIVE:
                            //Yes button clicked

                            if (walletLock){
                                PreferenceUtils.removePattern2(SetupPatternLock.this);
                                Toast.makeText(SetupPatternLock.this, "Wallet Pattern has been removed.", Toast.LENGTH_SHORT).show();
                            }
                            else {
                                PreferenceUtils.removePattern(SetupPatternLock.this);
                                Toast.makeText(SetupPatternLock.this, "Pattern has been removed.", Toast.LENGTH_SHORT).show();
                            }
                            break;

                        case DialogInterface.BUTTON_NEGATIVE:
                            //No button clicked
                            break;
                    }
                }
            };
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage("Are you sure you want to  remove Pattern?").setPositiveButton("Yes", dialogClickListener)
                    .setNegativeButton("No", dialogClickListener).show();
    }

    public void setupPatternBack(View view) {
        super.onBackPressed();
    }
}