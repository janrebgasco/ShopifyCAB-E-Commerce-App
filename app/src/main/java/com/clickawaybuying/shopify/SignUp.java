package com.clickawaybuying.shopify;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.telephony.SmsManager;
import android.text.Editable;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.clickawaybuying.shopify.utils.PreferenceUtils;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.ActionCodeSettings;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.SignInMethodQueryResult;

import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;
import java.util.regex.Pattern;

import static android.content.ContentValues.TAG;


public class SignUp extends AppCompatActivity {
    public static Activity sa;
    private static final Pattern PASSWORD_PATTERN =
            Pattern.compile("^" +
                    //"(?=.*[0-9])" +         //at least 1 digit
                    //"(?=.*[a-z])" +         //at least 1 lower case letter
                    //"(?=.*[A-Z])" +         //at least 1 upper case letter
                    "(?=.*[a-zA-Z])" +      //any letter
                    "(?=.*[@#$%^&+=])" +    //at least 1 special character
                    "(?=\\S+$)" +           //no white spaces
                    ".{6,}" +               //at least 6 characters
                    "$");

    TextView txtClkLogin;
    public static EditText JName,JEmail,JPassword,JConfirm;
    Button SignUpBtn;
    CheckBox JCheckbox;
    FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private static final String TAG = "SignUp";
    Editable txtConfirm;
    public static FirebaseAuth mFirebaseAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        mAuth = FirebaseAuth.getInstance();
        sa = this;
        txtClkLogin = findViewById(R.id.clkLogin);
        JName = findViewById(R.id.boxName);
        JEmail = findViewById(R.id.boxSignUpEmail);
        JPassword = findViewById(R.id.boxSignUpPassword);
        JConfirm = findViewById(R.id.boxConfirm);
        SignUpBtn = findViewById(R.id.btnSignUp);
        JCheckbox = findViewById(R.id.checkBoxProceed);

        //requestSMSPermission();
        mFirebaseAuth = FirebaseAuth.getInstance();

        //Opens the login page on click
        txtClkLogin.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                openLogin();
            }
        });
        //Authenticate information
        SignUpBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                userValidation();
                //sendEmail(JEmail.getText().toString());
            }});

        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    // User is signed in
                    Log.d(TAG, "onAuthStateChanged:signed_in:" + user.getUid());
                    toastMessage("Successfully signed in with: " + user.getEmail());
                } else {
                    // User is signed out
                    Log.d(TAG, "onAuthStateChanged:signed_out");
                    toastMessage("Successfully signed out.");
                }
                // ...
            }
        };


    }

    private void requestSMSPermission() {
        ActivityCompat.requestPermissions(SignUp.this, new String[]{Manifest.permission.SEND_SMS, Manifest.permission.READ_SMS}, PackageManager.PERMISSION_GRANTED);
    }

    public void openLogin(){
        Intent i = new Intent(this,LoginPage.class);
        startActivity(i);
        finish();
    }

    public void SignedUp(){
        Intent i = new Intent(this,HomeScreen.class);
        startActivity(i);
        finish();
    }
    @Override
    public void onBackPressed() {
        new AlertDialog.Builder(this)
                .setMessage("Are you sure you want to exit?")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        SignUp.this.finish();
                    }
                })
                .setNegativeButton("No", null)
                .show();
    }
    public void userValidation(){
        if(JName.length()<6){
            JName.setError("Error must contain at least 6 characters");
            JName.requestFocus();
            return;
        }
        if (JName.length() < 1)
        {
            JName.setError("This field is required");
            JName.requestFocus();
            return;
        }
        String emailInput= JEmail.getText().toString().trim();
        if (emailInput.length() < 1)
        {
            JEmail.setError("This field is required");
            JEmail.requestFocus();
            return;
        }
        else if (!Patterns.EMAIL_ADDRESS.matcher(emailInput).matches()) {
            JEmail.setError("Error invalid Email");
            JEmail.requestFocus();
            return;
        }
        if (JPassword.length() < 1)
        {
            JPassword.setError("This field is required");
            JPassword.requestFocus();
            return;
        }
        else if (JPassword.length()<8) {
            JPassword.setError("Error must contain at least 8 characters");
            JPassword.requestFocus();
            return;
        }
        if (JConfirm.length() < 1)
        {
            JConfirm.setError("This field is required");
            JConfirm.requestFocus();
            return;
        }
        if (!JPassword.getText().toString().equals(JConfirm.getText().toString()))
        {
            JConfirm.setError("Password does not match");
            JConfirm.requestFocus();
        }
        else if (!JCheckbox.isChecked()){
            Toast.makeText(SignUp.this,"Agree to the terms and conditions to continue.",Toast.LENGTH_SHORT).show();
        }

        else{
            if (Patterns.EMAIL_ADDRESS.matcher(emailInput).matches()){
                mFirebaseAuth.fetchSignInMethodsForEmail(emailInput)
                        .addOnCompleteListener(new OnCompleteListener<SignInMethodQueryResult>() {
                            @Override
                            public void onComplete(@NonNull Task<SignInMethodQueryResult> task) {
                                boolean emailDontExist = task.getResult().getSignInMethods().isEmpty();
                                if (!emailDontExist) {
                                    JEmail.setError("Email is already taken");
                                    JEmail.requestFocus();
                                }
                                else {
                                    String username = JName.getText().toString();
                                    String email = JEmail.getText().toString();
                                    String password = JPassword.getText().toString();
                                    PreferenceUtils.saveUsername(email, SignUp.this);
                                    String type = "reg";
                                    BackgroudWorker backgroudWorker = new BackgroudWorker(getApplicationContext());
                                    backgroudWorker.execute(type, username, email, password);
                                    loading();
                                }
                            }
                        });
            }
        }
    }


    private void toastMessage(String message){
        Toast.makeText(this,message,Toast.LENGTH_SHORT).show();
    }
    public void sendSMS(View view){

        //String message = editTextMessage.getText().toString();
        //String number = editTextNumber.getText().toString();

        //SmsManager mySmsManager = SmsManager.getDefault();
        //mySmsManager.sendTextMessage(number,null, message, null, null);
    }
    public void loading(){
        final ProgressDialog progress = new ProgressDialog(this);
        progress.setTitle("Loading");
        progress.setMessage("Please wait...");
        progress.setCancelable(false); // disable dismiss by tapping outside of the dialog
        progress.show();
        // To dismiss the dialog
        new CountDownTimer(3000, 1000) {
            public void onTick(long millisUntilFinished) {

            }
            public void onFinish() {
                progress.dismiss();
            }
        }.start();
    }
}