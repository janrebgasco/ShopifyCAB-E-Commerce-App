package com.clickawaybuying.shopify;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Camera;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.SparseArray;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.clickawaybuying.shopify.utils.PreferenceUtils;
import com.google.android.gms.vision.CameraSource;
import com.google.android.gms.vision.Detector;
import com.google.android.gms.vision.barcode.Barcode;
import com.google.android.gms.vision.barcode.BarcodeDetector;
import com.google.zxing.qrcode.encoder.QRCode;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.AppSettingsDialog;
import pub.devrel.easypermissions.EasyPermissions;

public class ScanActivity extends AppCompatActivity {
    ToggleButton changeCam,SODBtn;
    private SurfaceView surfaceView;
    private CameraSource cameraSource;
    private TextView textView;
    private BarcodeDetector barcodeDetector;
    EditText mAmount;
    SparseArray<Barcode> qrcode = new SparseArray<>();
    public static final int CAMERA_PERMISSION_CODE = 100;
    double balance2,amount = 0,balance;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan);
        surfaceView = findViewById(R.id.camera);
        textView = findViewById(R.id.scanTxt);
        changeCam = findViewById(R.id.scanToggle);
        mAmount = findViewById(R.id.qrAmount);
        SODBtn = findViewById(R.id.SODswitch);

        catchIntent();

        barcodeDetector = new BarcodeDetector.Builder(getApplicationContext())
                .setBarcodeFormats(Barcode.QR_CODE).build();

        cameraSource = new CameraSource.Builder(getApplicationContext(),barcodeDetector)
                .setRequestedPreviewSize(640,480)
                .setAutoFocusEnabled(true)
                .setFacing(CameraSource.CAMERA_FACING_BACK)
                .setRequestedFps(30.0f)
                .build();

        surfaceView.getHolder().addCallback(new SurfaceHolder.Callback() {
            @Override
            public void surfaceCreated(@NonNull SurfaceHolder surfaceHolder) {
                if (ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.CAMERA)
                        != PackageManager.PERMISSION_GRANTED){
                    return;
                }
                try {
                    cameraSource.start(surfaceHolder);
                }
                catch (IOException e){
                    e.printStackTrace();
                }
            }

            @Override
            public void surfaceChanged(@NonNull SurfaceHolder surfaceHolder, int i, int i1, int i2) {

            }

            @Override
            public void surfaceDestroyed(@NonNull SurfaceHolder surfaceHolder) {

            }
        });
        barcodeDetector.setProcessor(new Detector.Processor<Barcode>() {
            @Override
            public void release() {

            }

            @Override
            public void receiveDetections(Detector.Detections<Barcode> detections) {
                qrcode = detections.getDetectedItems();
                if (qrcode.size() != 0){
                    textView.post(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                amount = Double.parseDouble(mAmount.getText().toString());
                            }
                            catch (Exception e){
                                e.printStackTrace();
                            }

                            if (mAmount.getText().toString().trim().length() == 0){
                                mAmount.setError("Please enter the amount");
                                mAmount.requestFocus();
                                return;
                            }
                            if (amount==0){
                                mAmount.setError("Invalid amount");
                                mAmount.requestFocus();
                                return;
                            }
                            if (amount > balance){
                                mAmount.setError("Not enough balance");
                                mAmount.requestFocus();
                                return;
                            }

                            else {
                                Intent i = new Intent(ScanActivity.this,qrConfirm.class);
                                i.putExtra("receiver",qrcode.valueAt(0).displayValue);
                                i.putExtra("amount",amount);
                                if (SODBtn.isChecked()){
                                    i.putExtra("b",true);
                                }
                                else{
                                    i.putExtra("b",false);
                                }
                                startActivity(i);
                                //qrSend(qrcode.valueAt(0).displayValue);
                                //loading();
                            }
                        }
                    });
                }
            }
        });
        SODBtn.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b){

                }
                else{

                }
            }
        });

    }


    public void checkPermission(String permission, int requestCode){
        if(ContextCompat.checkSelfPermission(ScanActivity.this, permission)
                == PackageManager.PERMISSION_DENIED){
            ActivityCompat.requestPermissions(ScanActivity.this, new String[] {permission},
                    requestCode);
        }
        else{
            Toast.makeText(this, "Permission Already Granted", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode == CAMERA_PERMISSION_CODE){
            if(grantResults.length > 0
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                Toast.makeText(this, "Permission Granted", Toast.LENGTH_SHORT).show();
            }
            else{
                Toast.makeText(this, "Permission Denied", Toast.LENGTH_SHORT).show();
            }
        }
    }




    @Override
    public void onBackPressed() {
        Intent i = new Intent(this,wallet.class);
        startActivity(i);
        finish();
    }
    public void scanBack(View v){
        cameraSource.release();
        Intent i = new Intent(this,wallet.class);
        startActivity(i);
        finish();
    }
    public void showQRCode(View v){
        Intent i = new Intent(this,ShowQrCode.class);
        startActivity(i);
    }


    public void getBalance(){
        String URL = "https://gascojanreb.000webhostapp.com/ShopifyCAB/getBalance.php";
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        StringRequest jsonObjectRequest = new StringRequest(Request.Method.DEPRECATED_GET_OR_POST,
                URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {//gets the php json responses
                try {
                    JSONArray array = new JSONArray(response);

                    for (int i = 0; i<array.length();i++){
                        JSONObject productobject = array.getJSONObject(i);
                        int id = productobject.getInt("id");
                        double balance = productobject.getDouble("balance");
                        balance2 = balance;
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {//gets php error messages
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> parameters = new HashMap<String, String>();//putting parameters to pass on the php file
                //get Shared Preference username
                String user_name  = PreferenceUtils.getEmail(ScanActivity.this);
                parameters.put("username",user_name);

                return parameters;
            }
        };
        requestQueue.add(jsonObjectRequest);
    }
    public void catchIntent(){
        Intent i = getIntent();
        balance = i.getDoubleExtra("balance",0);
    }
}