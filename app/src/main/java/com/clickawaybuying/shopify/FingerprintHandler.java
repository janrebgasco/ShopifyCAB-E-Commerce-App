package com.clickawaybuying.shopify;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.hardware.fingerprint.FingerprintManager;
import android.media.Image;
import android.os.Build;
import android.os.CancellationSignal;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.widget.SwitchCompat;
import androidx.core.content.ContextCompat;

import com.clickawaybuying.shopify.utils.PreferenceUtils;

@TargetApi(Build.VERSION_CODES.M)
public class FingerprintHandler extends FingerprintManager.AuthenticationCallback {

    private Context context;
    boolean walletLock;

    public FingerprintHandler(Context context){

        this.context = context;

    }

    public void startAuth(FingerprintManager fingerprintManager, FingerprintManager.CryptoObject cryptoObject,boolean walletLock){

        CancellationSignal cancellationSignal = new CancellationSignal();
        fingerprintManager.authenticate(cryptoObject, cancellationSignal, 0, this, null);
        this.walletLock = walletLock;

    }

    @Override
    public void onAuthenticationError(int errorCode, CharSequence errString) {

        this.update("There was an Auth Error. " + errString, false);

    }

    @Override
    public void onAuthenticationFailed() {

        this.update("Authentication Failed. ", false);

    }

    @Override
    public void onAuthenticationHelp(int helpCode, CharSequence helpString) {

        this.update("Error: " + helpString, false);

    }

    @Override
    public void onAuthenticationSucceeded(FingerprintManager.AuthenticationResult result) {
        SwitchCompat mSwitch = ((Activity)context).findViewById(R.id.finger_switch);
        this.update("You can now use this feature.\nUse the switch in the top right of the screen.", true);
        mSwitch.setClickable(true);
        mSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isOn) {
                if (isOn){
                    if (walletLock){
                        PreferenceUtils.removePin2(context);
                        PreferenceUtils.removePattern2(context);
                        PreferenceUtils.saveFprint2(true,context);
                    }
                    else {
                        PreferenceUtils.removePin(context);
                        PreferenceUtils.removePattern(context);
                        PreferenceUtils.saveFprint(true,context);
                    }
                }
                else {
                    if (walletLock){
                        PreferenceUtils.saveFprint2(false,context);
                    }
                    else {
                        PreferenceUtils.saveFprint(false,context);
                    }
                }

            }
        });

    }

    private void update(String s, boolean correctFingerprint) {

        TextView paraLabel = ((Activity)context).findViewById(R.id.paraLabel);
        ImageView imageView = ((Activity)context).findViewById(R.id.fingerprintImage);

        paraLabel.setText(s);

        if(!correctFingerprint){
            paraLabel.setTextColor(ContextCompat.getColor(context, R.color.red));
            imageView.setImageResource(R.drawable.fingerprint_svg_red);

        } else {
            paraLabel.setTextColor(ContextCompat.getColor(context, R.color.colorAccent));
            imageView.setImageResource(R.drawable.fingerprint_svg_green);

        }

    }
}