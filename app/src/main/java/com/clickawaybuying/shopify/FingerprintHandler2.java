package com.clickawaybuying.shopify;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.hardware.fingerprint.FingerprintManager;
import android.os.Build;
import android.os.CancellationSignal;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.core.content.ContextCompat;

import com.clickawaybuying.shopify.utils.PreferenceUtils;

@TargetApi(Build.VERSION_CODES.M)
public class FingerprintHandler2 extends FingerprintManager.AuthenticationCallback {

    private Context context;
    Boolean fromCheckout = false, fromSplash = false;

    public FingerprintHandler2(Context context){

        this.context = context;

    }

    public void startAuth(FingerprintManager fingerprintManager, FingerprintManager.CryptoObject cryptoObject,boolean fromCheckout,boolean fromSplash){

        CancellationSignal cancellationSignal = new CancellationSignal();
        fingerprintManager.authenticate(cryptoObject, cancellationSignal, 0, this, null);
        this.fromCheckout =  fromCheckout;
        this.fromSplash = fromSplash;

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

        this.update("Success", true);

    }

    private void update(String s, boolean b) {

        TextView paraLabel = ((Activity)context).findViewById(R.id.paraLabel);
        ImageView imageView = ((Activity)context).findViewById(R.id.fingerprintImage);
        TextView textView = ((Activity)context).findViewById(R.id.textView68);
        TextView txtHere = ((Activity)context).findViewById(R.id.fingerRelogin);


        paraLabel.setText(s);

        if(!b){

            paraLabel.setTextColor(ContextCompat.getColor(context, R.color.red));
            imageView.setImageResource(R.drawable.fingerprint_svg_red);
            Fingerprint.attempts++;
            if (Fingerprint.attempts == 3){
                textView.setVisibility(View.VISIBLE);
                txtHere.setVisibility(View.VISIBLE);
            }
        } else {

            paraLabel.setTextColor(ContextCompat.getColor(context, R.color.colorAccent));
            imageView.setImageResource(R.drawable.fingerprint_svg_green);
            boolean hasFingerprint = PreferenceUtils.getFprint(context);
            boolean hasFingerprint2 = PreferenceUtils.getFprint2(context);
            if (hasFingerprint2 && fromCheckout){
                gotoCheckout();
            }
            if (hasFingerprint && fromSplash){
                gotoHomescreen();
            }
        }

    }
    public void gotoHomescreen(){
        new CountDownTimer(1000, 1000) {
            public void onTick(long millisUntilFinished) {

            }
            public void onFinish() {
                Intent i = new Intent(context,HomeScreen.class);
                context.startActivity(i);
                ((Fingerprint)context).finish();
            }
        }.start();
    }
    public void gotoCheckout(){
        new CountDownTimer(1000, 1000) {
            public void onTick(long millisUntilFinished) {

            }
            public void onFinish() {
                ((Fingerprint)context).finish();
            }
        }.start();
    }
}