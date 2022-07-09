package com.clickawaybuying.shopify;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.media.FaceDetector;
import android.os.Bundle;
import android.view.View;

public class ChooseLock extends AppCompatActivity {
    CardView mPin,mPattern,mFingerprint,mFaceDetect;
    Boolean walletLock = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_lock);
        mPin = findViewById(R.id.pinCardview);
        mPattern = findViewById(R.id.patternCardview);
        mFingerprint = findViewById(R.id.fingerprintCardview);
        mFaceDetect = findViewById(R.id.faceRecCardview);

        catchIntent();

        mPin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                intent(ChildLock.class);
            }
        });
        mPattern.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                intent(SetupPatternLock.class);
            }
        });
        mFingerprint.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {intent(SetupFingerprint.class);}
        });
        mFaceDetect.setOnClickListener(new View.OnClickListener() {@Override
            public void onClick(View view) {intent(FaceRecognition.class);}
        });

    }

    public void LockTypeBack(View view) {
        super.onBackPressed();
    }

    public void intent(Class intentclass){
        Intent i = new Intent(this,intentclass);
        i.putExtra("walletLock",walletLock);
        startActivity(i);
    }
    public void catchIntent(){
        Intent i = getIntent();
        walletLock = i.getBooleanExtra("walletLock",false);
    }
}