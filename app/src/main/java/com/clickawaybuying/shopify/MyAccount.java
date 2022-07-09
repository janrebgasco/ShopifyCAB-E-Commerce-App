package com.clickawaybuying.shopify;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NavUtils;

import android.Manifest;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.text.Editable;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.clickawaybuying.shopify.utils.Constants;
import com.clickawaybuying.shopify.utils.PreferenceUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MyAccount extends AppCompatActivity {
    private static final String TAG = "MyAccount";
    public List<UserInfo> userInfos = new ArrayList<>();
    final int CODE_GALLERY_REQUEST = 999;
    private TextView mDisplayDate;
    private DatePickerDialog.OnDateSetListener mDateSetListener;
    Bitmap bitmap;
    RequestQueue requestQueue;
    EditText mName,mMail,mUsername,mPhone;
    ImageView ProfilePic;
    TextView naym,emaiil;
    Spinner mGender;
    ImageButton mUpload;
    Button mSave;
    String result;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_account);
        mDisplayDate = findViewById(R.id.tvDate);
        mName = findViewById(R.id.editproifleName);
        mMail = findViewById(R.id.editprofileEmail);
        mUsername = findViewById(R.id.editprofileUname);
        mGender = findViewById(R.id.selectGender);
        mPhone = findViewById(R.id.editprofilePhone);
        mUpload = findViewById(R.id.uploadBtn);
        mSave = findViewById(R.id.updateAccount);
        naym = findViewById(R.id.sampName);
        emaiil = findViewById(R.id.sampEmail);
        ProfilePic = findViewById(R.id.profileimage);

        result="";
        //showData();
        showInfo();

        String user_name  = PreferenceUtils.getEmail(MyAccount.this);
        String email  = PreferenceUtils.getUsername(MyAccount.this);
        String fullname  = PreferenceUtils.getFullname(MyAccount.this);


        mUsername.setText(user_name);
        mMail.setText(email);
        mName.setText(fullname);

        mUsername.setFocusable(false);
        mMail.setFocusable(false);

        mDisplayDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar cal = Calendar.getInstance();
                int year = cal.get(Calendar.YEAR);
                int month = cal.get(Calendar.MONTH);
                int day = cal.get(Calendar.DAY_OF_MONTH);


                DatePickerDialog dialog = new DatePickerDialog(
                        MyAccount.this,
                        android.R.style.Theme_Holo_Light_Dialog_MinWidth,
                        mDateSetListener,
                        year,month,day);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();
            }
        });

        mDateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                month = month + 1;
                Log.d(TAG, "onDateSet: mm/dd/yyy: " + month + "/" + day + "/" + year);

                String date = month + "/" + day + "/" + year;
                mDisplayDate.setText(date);
            }
        };

        mUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ActivityCompat.requestPermissions(
                        MyAccount.this,
                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                        CODE_GALLERY_REQUEST

                );
            }
        });

        mSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String gender = mGender.getSelectedItem().toString();
                if(gender.equals("Select Gender")){
                    Toast.makeText(MyAccount.this,"Please Select Gender",Toast.LENGTH_SHORT).show();
                }
                else {
                    insertInfo();
                }
            }
        });

    }
    public void btnPrevious(View v){
        NavUtils.navigateUpTo(this, new Intent(this,
                Profile.class));
    }
    public void SaveChanges(View v){
        /*
        String asd= mName.getText().toString();
        String mail= mMail.getText().toString();
        naym.setText(asd);
        String etona = naym.getText().toString();
        emaiil.setText(mail);
        String etona2= emaiil.getText().toString();
        emaiil.getText().toString();
        Profile.Name_Acc.setText(etona);
        Profile.Email_Acc.setText(etona2);

        */

    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == CODE_GALLERY_REQUEST){
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                Intent i = new Intent(Intent.ACTION_PICK);
                i.setType("image/*");
                startActivityForResult(Intent.createChooser(i, "Select Image"),CODE_GALLERY_REQUEST);

            }
            else{
                Toast.makeText(this,"You don't have permission to gallery!", Toast.LENGTH_LONG).show();
            }
            return;
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CODE_GALLERY_REQUEST && resultCode == RESULT_OK && data != null)
        {
            Uri filepath = data.getData();

            try {
                InputStream inputStream = getContentResolver().openInputStream(filepath);
                bitmap = BitmapFactory.decodeStream(inputStream);
                ProfilePic.setImageBitmap(bitmap);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }

        super.onActivityResult(requestCode, resultCode, data);
    }
    public void insertInfo(){
        final String user_name  = PreferenceUtils.getEmail(MyAccount.this);
        final String id  = PreferenceUtils.getID(MyAccount.this);
        String insertURL = "https://gascojanreb.000webhostapp.com/ShopifyCAB/insertUserInfo.php";
        requestQueue = Volley.newRequestQueue(getApplicationContext());//manages worker threads for running the network operations
        StringRequest request = new StringRequest(Request.Method.POST, insertURL, new Response.Listener<String>() {//canned request for retrieving the response body at a given URL as a String
            @Override
            public void onResponse(String response) {//gets the php json responses
                if (response.trim().equals("success")){
                    Toast.makeText(getApplicationContext(), "Changes Saved Sucessfully", Toast.LENGTH_SHORT).show();
                    PreferenceUtils.saveEmail(mUsername.getText().toString(), MyAccount.this);
                    PreferenceUtils.saveUsername(mMail.getText().toString(), MyAccount.this);
                    PreferenceUtils.saveFullname(mName.getText().toString(), MyAccount.this);
                }
                else{
                    Toast.makeText(getApplicationContext(),"An error occurred",Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {//gets php error messages
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(),""+error,Toast.LENGTH_SHORT).show();
                if (String.valueOf(error).length() > 0){
                    Toast.makeText(getApplicationContext(), "No image selected", Toast.LENGTH_SHORT).show();
                }
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> parameters = new HashMap<String, String>();//putting parameters to pass on the php file

                String imageData = imagetoString(bitmap);
                parameters.put("image",imageData);
                parameters.put("fullname",mName.getText().toString());
                parameters.put("username",mUsername.getText().toString());
                parameters.put("gender",mGender.getSelectedItem().toString());
                parameters.put("birthday",mDisplayDate.getText().toString());
                parameters.put("phone",mPhone.getText().toString());
                parameters.put("email",mMail.getText().toString());
                parameters.put("user",user_name);
                parameters.put("id",id);


                return parameters;
            }
        };
        requestQueue.add(request);

    }
    public void showInfo(){
        final String user_name  = PreferenceUtils.getEmail(MyAccount.this);
        String insertURL = "https://gascojanreb.000webhostapp.com/ShopifyCAB/showInfo.php";

        requestQueue = Volley.newRequestQueue(getApplicationContext());//manages worker threads for running the network operations
        StringRequest request = new StringRequest(Request.Method.DEPRECATED_GET_OR_POST, insertURL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {//gets the php json responses


                try {
                    JSONArray array = new JSONArray(response);

                    for (int i = 0; i<array.length();i++){
                        JSONObject account = array.getJSONObject(i);
                            String gender = account.getString("gender");
                            String username = account.getString("username");
                            String email = account.getString("email");
                            String fullname = account.getString("fullname");
                            int phone = account.getInt("phone");
                            String birthday = account.getString("birthday");
                            String image = account.getString("image");

                            UserInfo userInfo = new UserInfo(fullname, phone, image, email, birthday, gender, username);

                            PreferenceUtils.saveEmail(username, MyAccount.this);
                            PreferenceUtils.saveUsername(email, MyAccount.this);
                            PreferenceUtils.saveFullname(fullname, MyAccount.this);
                            PreferenceUtils.saveBirthday(birthday, MyAccount.this);
                            PreferenceUtils.saveImage(image, MyAccount.this);
                            PreferenceUtils.saveGender(gender, MyAccount.this);
                            PreferenceUtils.savePhone(phone, MyAccount.this);
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
                parameters.put("username",user_name);

                return parameters;
            }
        };
        requestQueue.add(request);
        //mDisplayDate.setText(birthday);
        //mMail.setText(email);
        //mUsername.setText(fullname);
        //mName.setText(userInfo.getFullname().toString());
        //Toast.makeText(MyAccount.this, userInfo.getFullname().toString(), Toast.LENGTH_SHORT).show();
    }
    private String imagetoString(Bitmap bitmap){
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream);
        byte[] imageBytes = outputStream.toByteArray();

        String encodedImage = Base64.encodeToString(imageBytes, Base64.DEFAULT);
        return encodedImage;

    }
    public void showData(){
        String user_name  = PreferenceUtils.getEmail(MyAccount.this);
        String email  = PreferenceUtils.getUsername(MyAccount.this);
        String image  = PreferenceUtils.getImage(MyAccount.this);
        //String birthday  = PreferenceUtils.getBirthday(MyAccount.this);
        //String gender  = PreferenceUtils.getGender(MyAccount.this);
        //int phone  = PreferenceUtils.getPhone(MyAccount.this);
        String fullname  = PreferenceUtils.getFullname(MyAccount.this);

    }
}