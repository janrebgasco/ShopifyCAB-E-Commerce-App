package com.clickawaybuying.shopify;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.clickawaybuying.shopify.utils.PreferenceUtils;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.ActionCodeSettings;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.SignInMethodQueryResult;

import java.util.regex.Pattern;

public class ForgotPassword extends AppCompatActivity {
    EditText mEmail;
    FirebaseAuth mFirebaseAuth;
    int count = 20;
    Button mButton;
    Boolean isEnabled = true;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);
        mEmail = findViewById(R.id.boxRecoverEmail);
        mButton = findViewById(R.id.btnContinue);

        mFirebaseAuth = FirebaseAuth.getInstance();

    }

    public void recoveryBack(View view) {
        super.onBackPressed();
    }

    public void recoverBtn(View view) {
        final String email = mEmail.getText().toString();
        if (!isEnabled){
            Toast.makeText(this, "Try again in "+count+"s", Toast.LENGTH_SHORT).show();
        }
        else if (email.equals("")){
            mEmail.setError("Email is required");
            mEmail.requestFocus();
            return;
        }
        else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            mEmail.setError("Invalid email");
            mEmail.requestFocus();
        }
        else {
            mFirebaseAuth.fetchSignInMethodsForEmail(email)
                    .addOnCompleteListener(new OnCompleteListener<SignInMethodQueryResult>() {
                        @Override
                        public void onComplete(@NonNull Task<SignInMethodQueryResult> task) {
                            boolean emailDontExist = task.getResult().getSignInMethods().isEmpty();
                            if (emailDontExist) {
                                mEmail.setError("Unregistered Email");
                                mEmail.requestFocus();
                            }
                            else {
                                //insertToFirebase(email);
                                mFirebaseAuth.sendPasswordResetEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()){
                                            Toast.makeText(ForgotPassword.this, "Password reset link has been sent to your email", Toast.LENGTH_SHORT).show();
                                            timer();
                                            mButton.setText("Resend Link");
                                            isEnabled = false;
                                        }
                                        else{
                                            Toast.makeText(ForgotPassword.this, "An error occurred", Toast.LENGTH_SHORT).show();

                                        }
                                    }
                                });
                            }
                        }
                    });
        }
    }

    private void timer() {
        new CountDownTimer(20000, 1000) {
            public void onTick(long millisUntilFinished) {
                count--;
                //Toast.makeText(ForgotPassword.this, ""+count, Toast.LENGTH_SHORT).show();
            }
            public void onFinish() {
                isEnabled = true;
                mButton.setEnabled(true);
            }
        }.start();
    }
}