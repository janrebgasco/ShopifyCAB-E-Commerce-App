package com.clickawaybuying.shopify;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Base64;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
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
import com.bumptech.glide.Glide;
import com.clickawaybuying.shopify.Adapters.ColorAdapter;
import com.clickawaybuying.shopify.Adapters.SellerColorAdapter;
import com.clickawaybuying.shopify.Adapters.SellerSizeAdapter;
import com.clickawaybuying.shopify.utils.PreferenceUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SellProduct extends AppCompatActivity {
    public static List<String> colors2 = new ArrayList<>();
    public static List<String> size2 = new ArrayList<>();
    private List<String> colors = new ArrayList<>();
    private List<String> sizes = new ArrayList<>();
    private List<String> color = new ArrayList<>();
    private List<String> size = new ArrayList<>();
    SellerColorAdapter colorAdapter;
    SellerSizeAdapter sizeAdapter;
    Spinner mCategory,mShippingFee,mCondition,mSizeDdown;
    EditText mProdName,mDesc,mPrice,mStock,mBrand,mTxtBoxColor,mTxtBoxSize;
    Button mPublish,mSave,mAddColor,mAddSize;
    ImageButton mUpload,mBtnVerify;
    ImageView mViewImage,mImgVerify,mDottedImgView,mDottedImgView2;
    RequestQueue requestQueue;
    final int CODE_GALLERY_REQUEST = 999;
    final int CODE_GALLERY_REQUEST2 = 1000;
    Bitmap bitmap1,bitmap2,bitmap;
    String prodName,prodDESC,prodNBRAND,prodIMAGE,prodIMAGEID;
    String prodImage,idImage;
    String prodShipFEE;
    int prodPRICE,prodSTOCKS,prodCATEG,prodCONDITION;
    ToggleButton mBtnWhite,mBtnBlack,mBtnYellow,mBtnGreen,mBtnRed,mBtnBlue,mBtnSmall,mBtnMedium,mBtnLarge,mBtnXLarge,mBtnXS,mBtnXXXL,mBtnXXL,mBtnXXS;
    public static TextView mTxt1,mTxt2,address;
    RecyclerView colorRecycler,sizeRecycler;
    String res = "emp";
    ProgressDialog progress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sell_product);
        mUpload = findViewById(R.id.sellUploadImage);
        mViewImage =findViewById(R.id.uploadedImage);
        mPublish = findViewById(R.id.sellPublishBtn);
        mSave = findViewById(R.id.sellSaveBtn);
        mProdName = findViewById(R.id.sellProdName);
        mPrice = findViewById(R.id.sellPrice);
        mDesc = findViewById(R.id.sellDescription);
        mStock = findViewById(R.id.sellStocks);
        mBrand = findViewById(R.id.sellBrand);
        mCategory = findViewById(R.id.sellCateg);
        mShippingFee = findViewById(R.id.sellShipFee);
        mCondition = findViewById(R.id.sellCondition);
        mBtnVerify = findViewById(R.id.btnSellerVerification);
        mImgVerify = findViewById(R.id.imgSelerVerification);
        mBtnWhite = findViewById(R.id.toggleButton);
        mBtnBlack = findViewById(R.id.toggleButton2);
        mBtnYellow = findViewById(R.id.toggleButton3);
        mBtnGreen = findViewById(R.id.toggleButton4);
        mBtnRed = findViewById(R.id.toggleButton5);
        mBtnBlue = findViewById(R.id.toggleButton7);
        mBtnSmall = findViewById(R.id.toggleButton9);
        mBtnMedium = findViewById(R.id.toggleButton10);
        mBtnLarge = findViewById(R.id.toggleButton11);
        mBtnXLarge = findViewById(R.id.toggleButton12);
        mDottedImgView = findViewById(R.id.uploadedbackg);
        mDottedImgView2 = findViewById(R.id.verifback);
        mBtnXXXL = findViewById(R.id.togggleXXXL);
        mBtnXXL = findViewById(R.id.togggleXXL);
        mBtnXXS = findViewById(R.id.togggleXXS);
        mBtnXS = findViewById(R.id.togggleXS);
        mTxt1 = findViewById(R.id.textView45);
        mTxt2 = findViewById(R.id.textView412);
        mAddColor = findViewById(R.id.btnAddColor);
        mTxtBoxColor = findViewById(R.id.txtBoxAddColor);
        mAddSize = findViewById(R.id.btnAddSize);
        mTxtBoxSize = findViewById(R.id.txtBoxAddSize);
        colorRecycler = findViewById(R.id.sellProdColorRecycler);
        sizeRecycler = findViewById(R.id.sellProdSizeRecycler);
        mSizeDdown = findViewById(R.id.sizeDdown);
        address = findViewById(R.id.sellAddress);

        progress = new ProgressDialog(SellProduct.this);
        progress.setTitle("Loading");
        progress.setMessage("Please wait...");
        progress.setCancelable(false); // disable dismiss by tapping outside of the dialog


        loadSharedPrefs();
        setSavedData();
        getAddress();

        mUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ActivityCompat.requestPermissions(
                        SellProduct.this,
                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                        CODE_GALLERY_REQUEST

                );
            }
        });

        mBtnVerify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //ActivityCompat.requestPermissions(SellProduct.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, CODE_GALLERY_REQUEST2);
            }
        });
        mAddColor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!mTxtBoxColor.getText().toString().trim().equals("")){
                    color.add(mTxtBoxColor.getText().toString());
                    colorAdapter.notifyDataSetChanged();
                    mTxtBoxColor.setText("");
                }
            }
        });
        mAddSize.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!mTxtBoxSize.getText().toString().trim().equals("")){
                    size.add(mTxtBoxSize.getText().toString());
                    sizeAdapter.notifyDataSetChanged();
                    mTxtBoxSize.setText("");
                }
            }
        });
        mSizeDdown.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (mSizeDdown.getSelectedItemPosition() == 0){
                    hideToggles();
                }
                if (mSizeDdown.getSelectedItemPosition() == 1){
                    clothesToggles();
                }
                if (mSizeDdown.getSelectedItemPosition() == 2){
                    shoeSizeUS();
                }
                if (mSizeDdown.getSelectedItemPosition() == 3){
                    shoeSizeUK();
                }
                if (mSizeDdown.getSelectedItemPosition() == 4){
                    shoeSizeEU();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });


        colorAdapter = new SellerColorAdapter(this,color);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this,3,GridLayoutManager.VERTICAL,false);
        colorRecycler.setLayoutManager(gridLayoutManager);
        colorRecycler.setAdapter(colorAdapter);

        sizeAdapter = new SellerSizeAdapter(this,size);
        GridLayoutManager gridLayoutManager2 = new GridLayoutManager(this,5,GridLayoutManager.VERTICAL,false);
        sizeRecycler.setLayoutManager(gridLayoutManager2);
        sizeRecycler.setAdapter(sizeAdapter);

        mPublish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String s = mStock.getText().toString();
                int stocks = Integer.parseInt(s);
                if (mViewImage.getDrawable() == null){
                    Toast.makeText(SellProduct.this,"Insert a valid id to continue",Toast.LENGTH_SHORT).show();
                }
                if (stocks > 3000){
                    mStock.setError("Max stocks has been exceeded.\nMax stocks: 3000");
                    mStock.requestFocus();
                }
                if (stocks < 30){
                    mStock.setError("Sorry you can't post products that doesn't reach min stocks.\nMinimum stocks: 30");
                    mStock.requestFocus();
                }
                else{
                    insertToDb();
                }
            }
        });
        mSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PrefOnOffListener();
                saveToSharedPref();

            }
        });
    }

    private void insertToDb() {
        progress.show();
        colors2.removeAll(Collections.singleton("null"));
        colors2.removeAll(Collections.singleton(""));
        size2.removeAll(Collections.singleton("null"));
        size2.removeAll(Collections.singleton(""));
        OnOffListener();
        String URL = "https://gascojanreb.000webhostapp.com/ShopifyCAB/insertProduct.php";
        requestQueue = Volley.newRequestQueue(getApplicationContext());//manages worker threads for running the network operations
        StringRequest jsonObjectRequest = new StringRequest(Request.Method.POST,
                URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {//gets the php json responses
                Toast.makeText(SellProduct.this,response, Toast.LENGTH_LONG).show();
                progress.dismiss();
                if (response.length() == 0){
                    progress.dismiss();
                    Toast.makeText(SellProduct.this,"Product Published waiting for approval....", Toast.LENGTH_LONG).show();
                    clearPrefs();
                    Intent i = new Intent(SellProduct.this,Profile.class);
                    startActivity(i);
                }
            }
        }, new Response.ErrorListener() {//gets php error messages
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(SellProduct.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                dismiss();
                try {
                    if (error.getMessage().length()>0){
                        //Toast.makeText(SellProduct.this, "Please re-attach product image", Toast.LENGTH_SHORT).show();
                        //dismiss();
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }

            }
            private void dismiss(){
                progress.dismiss();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> parameters = new HashMap<String, String>();//putting parameters to pass on the php file
                //prodImage = bitmapToBlob(bitmap1);
                //idImage = bitmapToBlob(bitmap2);
                prodImage = imagetoString(bitmap1);
                //idImage = imagetoString(bitmap2);
                String idImg  = PreferenceUtils.getSlrID(SellProduct.this);


                //parameters.put("idImage", Arrays.toString(idImage));
                //parameters.put("image", Arrays.toString(prodImage));
                //parameters.put("idImage", idImage);//recently
                parameters.put("idImage", idImg);
                parameters.put("image", prodImage);
                //get Shared Preference username
                String user_name  = PreferenceUtils.getSlrShopname(SellProduct.this);
                parameters.put("seller",user_name);
                parameters.put("product_name",mProdName.getText().toString());
                parameters.put("price",mPrice.getText().toString());
                parameters.put("description",mDesc.getText().toString());
                parameters.put("stocks",mStock.getText().toString());
                parameters.put("brand",mBrand.getText().toString());
                int sold=0;
                parameters.put("sold",String.valueOf(sold));
                //parameters.put("category",mCategory.getSelectedItem().toString());
                parameters.put("category","");
                parameters.put("shipping_fee",mShippingFee.getSelectedItem().toString());
                parameters.put("condition",mCondition.getSelectedItem().toString());
                parameters.put("color", colors + "," + colors2);
                parameters.put("size", sizes + "," + size2);
                parameters.put("sellerAddress",address.getText().toString());
                return parameters;
            }
        };
        requestQueue.add(jsonObjectRequest);
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
        if (requestCode == CODE_GALLERY_REQUEST2){
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                Intent i = new Intent(Intent.ACTION_PICK);
                i.setType("image/*");
                startActivityForResult(Intent.createChooser(i, "Select Image"),CODE_GALLERY_REQUEST2);

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
        if (requestCode == CODE_GALLERY_REQUEST && resultCode == RESULT_OK && data != null)
        {
            Uri filepath = data.getData();

            try {
                InputStream inputStream = getContentResolver().openInputStream(filepath);
                bitmap1 = BitmapFactory.decodeStream(inputStream);
                mViewImage.setImageBitmap(bitmap1);
                mTxt1.setVisibility(View.INVISIBLE);
                mDottedImgView.setVisibility(View.INVISIBLE);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }

        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CODE_GALLERY_REQUEST2 && resultCode == RESULT_OK && data != null)
        {
            Uri filepath = data.getData();

            try {
                InputStream inputStream = getContentResolver().openInputStream(filepath);
                bitmap2 = BitmapFactory.decodeStream(inputStream);
                //mImgVerify.setImageBitmap(bitmap2);
                mTxt2.setVisibility(View.INVISIBLE);
                mDottedImgView2.setVisibility(View.INVISIBLE);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }

        super.onActivityResult(requestCode, resultCode, data);
    }


    public void backSell(View v){
        super.onBackPressed();
    }
    private String imagetoString(Bitmap bitmap){
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream);
        byte[] imageBytes = outputStream.toByteArray();

        String encodedImage = Base64.encodeToString(imageBytes, Base64.DEFAULT);
        return encodedImage;

    }

    public byte[] bitmapToBlob(Bitmap bitmap){
        //convert bitmap to byteArray or blob
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, bos);
        byte[] bArray = bos.toByteArray();

        return bArray;
    }
    public Bitmap decodeImage(byte[] blobImage){
        //convert blob to bitmap
        Bitmap bm = BitmapFactory.decodeByteArray(blobImage, 0 ,blobImage.length);

        return bm;
    }
    public void saveToSharedPref(){

    if (mPrice.getText().toString().isEmpty()){
        mPrice.setError("Please enter a value");
        mPrice.requestFocus();
        return;
    }
    if (mStock.getText().toString().isEmpty()){
        mStock.setError("Please enter a value");
        mStock.requestFocus();
        return;
    }
    if (mViewImage.getDrawable() == null){
        Toast.makeText(getApplicationContext(),"No Image Attached",Toast.LENGTH_SHORT).show();
    }
    else{
        String price = mPrice.getText().toString();
        String stock = mStock.getText().toString();
        int finalPrice = Integer.parseInt(price);
        int finalStock = Integer.parseInt(stock);
        PreferenceUtils.saveProdNAME(mProdName.getText().toString(), SellProduct.this);
        PreferenceUtils.saveProdDESC(mDesc.getText().toString(), SellProduct.this);
        PreferenceUtils.saveProdCATEG(mCategory.getSelectedItemPosition(), SellProduct.this);
        PreferenceUtils.saveProdPRICE(finalPrice, SellProduct.this);
        PreferenceUtils.saveProdSTOCKS(finalStock, SellProduct.this);
        PreferenceUtils.saveProdSHIPPING(mShippingFee.getSelectedItem().toString(), SellProduct.this);
        PreferenceUtils.saveProdCONDITION(mCondition.getSelectedItemPosition(), SellProduct.this);
        PreferenceUtils.saveProdBRAND(mBrand.getText().toString(), SellProduct.this);

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        if (!(bitmap1 == null)){
            bitmap1.compress(Bitmap.CompressFormat.PNG, 100, baos);
        }
        else {
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
        }
        byte[] b = baos.toByteArray();
        String temp = Base64.encodeToString(b, Base64.DEFAULT);
        SharedPreferences.Editor imgPref = getSharedPreferences("IMG", MODE_PRIVATE).edit();
        imgPref.putString("KEY_IMG", temp);
        imgPref.apply();

        Toast.makeText(getApplicationContext(),"Data has been saved",Toast.LENGTH_SHORT).show();
    }



    }
    public void loadSharedPrefs(){
        prodName  = PreferenceUtils.getProdNAME(SellProduct.this);
        prodDESC  = PreferenceUtils.getProdDESC(SellProduct.this);
        prodCATEG  = PreferenceUtils.getProdCATEG(SellProduct.this);
        prodPRICE  = PreferenceUtils.getProdPrice(SellProduct.this);
        prodSTOCKS  = PreferenceUtils.getProdSTOCKS(SellProduct.this);
        prodShipFEE  = PreferenceUtils.getProdSHIPPING(SellProduct.this);
        prodCONDITION  = PreferenceUtils.getProdCONDITION(SellProduct.this);
        prodNBRAND  = PreferenceUtils.getProdBRAND(SellProduct.this);
        prodIMAGE  = PreferenceUtils.getProdIMAGE(SellProduct.this);
        prodIMAGEID  = PreferenceUtils.getProdIMAGEID(SellProduct.this);
        SharedPreferences sharedPrefs = getSharedPreferences("COLORS", MODE_PRIVATE);
        mBtnWhite.setChecked(sharedPrefs.getBoolean("KEY_WHITE", false));
        mBtnBlack.setChecked(sharedPrefs.getBoolean("KEY_BLACK", false));
        mBtnYellow.setChecked(sharedPrefs.getBoolean("KEY_YELLOW", false));
        mBtnGreen.setChecked(sharedPrefs.getBoolean("KEY_GREEN", false));
        mBtnRed.setChecked(sharedPrefs.getBoolean("KEY_RED", false));
        mBtnBlue.setChecked(sharedPrefs.getBoolean("KEY_BLUE", false));
        SharedPreferences sizePrefs = getSharedPreferences("SIZES", MODE_PRIVATE);
        mBtnXS.setChecked(sizePrefs.getBoolean("KEY_XS", false));
        mBtnXXS.setChecked(sizePrefs.getBoolean("KEY_XXS", false));
        mBtnXXL.setChecked(sizePrefs.getBoolean("KEY_XXL", false));
        mBtnXXXL.setChecked(sizePrefs.getBoolean("KEY_XXXL", false));
        mBtnSmall.setChecked(sizePrefs.getBoolean("KEY_S", false));
        mBtnMedium.setChecked(sizePrefs.getBoolean("KEY_M", false));
        mBtnLarge.setChecked(sizePrefs.getBoolean("KEY_L", false));
        mBtnXLarge.setChecked(sizePrefs.getBoolean("KEY_XL", false));

        SharedPreferences mypreference = getSharedPreferences("IMG", MODE_PRIVATE);
        String temp = mypreference.getString("KEY_IMG", "defaultString");
        try {
            byte[] encodeByte = Base64.decode(temp, Base64.DEFAULT);
            bitmap = BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.length);
            mViewImage.setImageBitmap(bitmap);
            if (!(bitmap == null)){
                mTxt1.setVisibility(View.INVISIBLE);
                mDottedImgView.setVisibility(View.INVISIBLE);
            }
        } catch (Exception e) {
            e.getMessage();
        }


    }
    public void setSavedData(){
        mProdName.setText(prodName);
        mDesc.setText(prodDESC);
        mCategory.setSelection(prodCATEG);
        if(prodPRICE == 0 ||prodSTOCKS == 0){
            //do nothing
        }
        else{
            mPrice.setText(String.valueOf(prodPRICE));
            mStock.setText(String.valueOf(prodSTOCKS));
        }
        if (!(prodCONDITION == 0)){
            mCondition.setSelection(prodCONDITION);
        }

        mBrand.setText(prodNBRAND);

    }
    public void OnOffListener(){
        if (mBtnWhite.isChecked()){
            colors.add("White");
        }if (mBtnBlack.isChecked()){
            colors.add("Black");
        }if (mBtnYellow.isChecked()){
            colors.add("Yellow");
        }if (mBtnGreen.isChecked()){
            colors.add("Green");
        }if (mBtnRed.isChecked()){
            colors.add("Red");
        }if (mBtnBlue.isChecked()){
            colors.add("Blue");
        }if (mBtnXS.isChecked() && mSizeDdown.getSelectedItemPosition() == 1){
            sizes.add("XS");
        }if (mBtnSmall.isChecked() && mSizeDdown.getSelectedItemPosition() == 1){
            sizes.add("S");
        }if (mBtnMedium.isChecked() && mSizeDdown.getSelectedItemPosition() == 1){
            sizes.add("M");
        }if (mBtnLarge.isChecked() && mSizeDdown.getSelectedItemPosition() == 1){
            sizes.add("L");
        }if (mBtnXLarge.isChecked() && mSizeDdown.getSelectedItemPosition() == 1){
            sizes.add("XL");
        }if (mBtnXXS.isChecked() && mSizeDdown.getSelectedItemPosition() == 1){
            sizes.add("XXS");
        }if (mBtnXXL.isChecked() && mSizeDdown.getSelectedItemPosition() == 1){
            sizes.add("XXL");
        }if (mBtnXXXL.isChecked() && mSizeDdown.getSelectedItemPosition() == 1){
            sizes.add("XXXL");
        }if (mBtnXS.isChecked() && mSizeDdown.getSelectedItemPosition() == 2){
            sizes.add("6");
        }if (mBtnSmall.isChecked() && mSizeDdown.getSelectedItemPosition() == 2){
            sizes.add("6.5");
        }if (mBtnMedium.isChecked() && mSizeDdown.getSelectedItemPosition() == 2){
            sizes.add("7");
        }if (mBtnLarge.isChecked() && mSizeDdown.getSelectedItemPosition() == 2){
            sizes.add("7.5");
        }if (mBtnXLarge.isChecked() && mSizeDdown.getSelectedItemPosition() == 2){
            sizes.add("8");
        }if (mBtnXXS.isChecked() && mSizeDdown.getSelectedItemPosition() == 2){
            sizes.add("8.5");
        }if (mBtnXXL.isChecked() && mSizeDdown.getSelectedItemPosition() == 2){
            sizes.add("9");
        }if (mBtnXXXL.isChecked() && mSizeDdown.getSelectedItemPosition() == 2){
            sizes.add("9.5");
        }if (mBtnXS.isChecked() && mSizeDdown.getSelectedItemPosition() == 3){
            sizes.add("4");
        }if (mBtnSmall.isChecked() && mSizeDdown.getSelectedItemPosition() == 3){
            sizes.add("4.5");
        }if (mBtnMedium.isChecked() && mSizeDdown.getSelectedItemPosition() == 3){
            sizes.add("5");
        }if (mBtnLarge.isChecked() && mSizeDdown.getSelectedItemPosition() == 3){
            sizes.add("5.5");
        }if (mBtnXLarge.isChecked() && mSizeDdown.getSelectedItemPosition() == 3){
            sizes.add("6");
        }if (mBtnXXS.isChecked() && mSizeDdown.getSelectedItemPosition() == 3){
            sizes.add("6.5");
        }if (mBtnXXL.isChecked() && mSizeDdown.getSelectedItemPosition() == 3){
            sizes.add("7");
        }if (mBtnXXXL.isChecked() && mSizeDdown.getSelectedItemPosition() == 3){
            sizes.add("7.5");
        }if (mBtnXS.isChecked() && mSizeDdown.getSelectedItemPosition() == 4){
            sizes.add("36-37");
        }if (mBtnSmall.isChecked() && mSizeDdown.getSelectedItemPosition() == 4){
            sizes.add("37");
        }if (mBtnMedium.isChecked() && mSizeDdown.getSelectedItemPosition() == 4){
            sizes.add("37-38");
        }if (mBtnLarge.isChecked() && mSizeDdown.getSelectedItemPosition() == 4){
            sizes.add("38");
        }if (mBtnXLarge.isChecked() && mSizeDdown.getSelectedItemPosition() == 4){
            sizes.add("38-39");
        }if (mBtnXXS.isChecked() && mSizeDdown.getSelectedItemPosition() == 4){
            sizes.add("39");
        }if (mBtnXXL.isChecked() && mSizeDdown.getSelectedItemPosition() == 4){
            sizes.add("39-40");
        }if (mBtnXXXL.isChecked() && mSizeDdown.getSelectedItemPosition() == 4){
            sizes.add("40");
        }
    }

    public void PrefOnOffListener(){
        if (mBtnWhite.isChecked()){
            SharedPreferences.Editor editor = getSharedPreferences("COLORS", MODE_PRIVATE).edit();
            editor.putBoolean("KEY_WHITE", true);
            editor.apply();
        }
        else if (!mBtnWhite.isChecked()){
            SharedPreferences.Editor editor = getSharedPreferences("COLORS", MODE_PRIVATE).edit();
            editor.putBoolean("KEY_WHITE", false);
            editor.apply();
        }
        if (mBtnBlack.isChecked()){
            SharedPreferences.Editor editor = getSharedPreferences("COLORS", MODE_PRIVATE).edit();
            editor.putBoolean("KEY_BLACK", true);
            editor.apply();
        }
        else if (!mBtnBlack.isChecked()){
            SharedPreferences.Editor editor = getSharedPreferences("COLORS", MODE_PRIVATE).edit();
            editor.putBoolean("KEY_BLACK", false);
            editor.apply();
        }
        if (mBtnYellow.isChecked()){
            SharedPreferences.Editor editor = getSharedPreferences("COLORS", MODE_PRIVATE).edit();
            editor.putBoolean("KEY_YELLOW", true);
            editor.apply();
        }
        else if (!mBtnYellow.isChecked()){
            SharedPreferences.Editor editor = getSharedPreferences("COLORS", MODE_PRIVATE).edit();
            editor.putBoolean("KEY_YELLOW", false);
            editor.apply();
        }
        if (mBtnGreen.isChecked()){
            SharedPreferences.Editor editor = getSharedPreferences("COLORS", MODE_PRIVATE).edit();
            editor.putBoolean("KEY_GREEN", true);
            editor.apply();
        }
        else if (!mBtnGreen.isChecked()){
            SharedPreferences.Editor editor = getSharedPreferences("COLORS", MODE_PRIVATE).edit();
            editor.putBoolean("KEY_GREEN", false);
            editor.apply();
        }
        if (mBtnRed.isChecked()){
            SharedPreferences.Editor editor = getSharedPreferences("COLORS", MODE_PRIVATE).edit();
            editor.putBoolean("KEY_RED", true);
            editor.apply();
        }
        else if (!mBtnRed.isChecked()){
            SharedPreferences.Editor editor = getSharedPreferences("COLORS", MODE_PRIVATE).edit();
            editor.putBoolean("KEY_RED", false);
            editor.apply();
        }
        if (mBtnBlue.isChecked()){
            SharedPreferences.Editor editor = getSharedPreferences("COLORS", MODE_PRIVATE).edit();
            editor.putBoolean("KEY_BLUE", true);
            editor.apply();
        }
        else if (!mBtnBlue.isChecked()){
            SharedPreferences.Editor editor = getSharedPreferences("COLORS", MODE_PRIVATE).edit();
            editor.putBoolean("KEY_BLUE", false);
            editor.apply();
        }
        if (mBtnXS.isChecked()){
            SharedPreferences.Editor editor = getSharedPreferences("SIZES", MODE_PRIVATE).edit();
            editor.putBoolean("KEY_XS", true);
            editor.apply();
        }
        else if (!mBtnXS.isChecked()){
            SharedPreferences.Editor editor = getSharedPreferences("SIZES", MODE_PRIVATE).edit();
            editor.putBoolean("KEY_XS", false);
            editor.apply();
        }
        if (mBtnXXS.isChecked()){
            SharedPreferences.Editor editor = getSharedPreferences("SIZES", MODE_PRIVATE).edit();
            editor.putBoolean("KEY_XXS", true);
            editor.apply();
        }
        else if (!mBtnXXS.isChecked()){
            SharedPreferences.Editor editor = getSharedPreferences("SIZES", MODE_PRIVATE).edit();
            editor.putBoolean("KEY_XXS", false);
            editor.apply();
        }
        if (mBtnXXL.isChecked()){
            SharedPreferences.Editor editor = getSharedPreferences("SIZES", MODE_PRIVATE).edit();
            editor.putBoolean("KEY_XXL", true);
            editor.apply();
        }
        else if (!mBtnXXL.isChecked()){
            SharedPreferences.Editor editor = getSharedPreferences("SIZES", MODE_PRIVATE).edit();
            editor.putBoolean("KEY_XXL", false);
            editor.apply();
        }
        if (mBtnXXXL.isChecked()){
            SharedPreferences.Editor editor = getSharedPreferences("SIZES", MODE_PRIVATE).edit();
            editor.putBoolean("KEY_XXXL", true);
            editor.apply();
        }
        else if (!mBtnXXXL.isChecked()){
            SharedPreferences.Editor editor = getSharedPreferences("SIZES", MODE_PRIVATE).edit();
            editor.putBoolean("KEY_XXXL", false);
            editor.apply();
        }
        if (mBtnSmall.isChecked()){
            SharedPreferences.Editor editor = getSharedPreferences("SIZES", MODE_PRIVATE).edit();
            editor.putBoolean("KEY_S", true);
            editor.apply();
        }
        else if (!mBtnSmall.isChecked()){
            SharedPreferences.Editor editor = getSharedPreferences("SIZES", MODE_PRIVATE).edit();
            editor.putBoolean("KEY_S", false);
            editor.apply();
        }
        if (mBtnMedium.isChecked()){
            SharedPreferences.Editor editor = getSharedPreferences("SIZES", MODE_PRIVATE).edit();
            editor.putBoolean("KEY_M", true);
            editor.apply();
        }
        else if (!mBtnMedium.isChecked()){
            SharedPreferences.Editor editor = getSharedPreferences("SIZES", MODE_PRIVATE).edit();
            editor.putBoolean("KEY_M", false);
            editor.apply();
        }
        if (mBtnLarge.isChecked()){
            SharedPreferences.Editor editor = getSharedPreferences("SIZES", MODE_PRIVATE).edit();
            editor.putBoolean("KEY_L", true);
            editor.apply();
        }
        else if (!mBtnLarge.isChecked()){
            SharedPreferences.Editor editor = getSharedPreferences("SIZES", MODE_PRIVATE).edit();
            editor.putBoolean("KEY_L", false);
            editor.apply();
        }
        if (mBtnXLarge.isChecked()){
            SharedPreferences.Editor editor = getSharedPreferences("SIZES", MODE_PRIVATE).edit();
            editor.putBoolean("KEY_XL", true);
            editor.apply();
        }
        else if (!mBtnXLarge.isChecked()){
            SharedPreferences.Editor editor = getSharedPreferences("SIZES", MODE_PRIVATE).edit();
            editor.putBoolean("KEY_XL", false);
            editor.apply();
        }

    }
    public void clearPrefs(){
        PreferenceUtils.removeProdNAME(SellProduct.this);
        PreferenceUtils.removeProdDESC(SellProduct.this);
        PreferenceUtils.removeProdCATEG(SellProduct.this);
        PreferenceUtils.removeProdPrice(SellProduct.this);
        PreferenceUtils.removeProdSTOCKS(SellProduct.this);
        PreferenceUtils.removeProdCONDITION(SellProduct.this);
        PreferenceUtils.removeProdBRAND(SellProduct.this);
        SharedPreferences colors = getSharedPreferences("COLORS", MODE_PRIVATE);
        colors.edit().clear().apply();
        SharedPreferences size = getSharedPreferences("SIZES", MODE_PRIVATE);
        size.edit().clear().apply();
        SharedPreferences img = getSharedPreferences("IMG", MODE_PRIVATE);
        img.edit().clear().apply();
    }
    private void hideToggles(){
        mBtnSmall.setVisibility(View.GONE);
        mBtnMedium.setVisibility(View.GONE);
        mBtnLarge.setVisibility(View.GONE);
        mBtnXLarge.setVisibility(View.GONE);
        mBtnXS.setVisibility(View.GONE);
        mBtnXXXL.setVisibility(View.GONE);
        mBtnXXL.setVisibility(View.GONE);
        mBtnXXS.setVisibility(View.GONE);

    }
    private void clothesToggles(){
        mBtnSmall.setVisibility(View.VISIBLE);
        mBtnSmall.setTextOff("S");
        mBtnSmall.setTextOn("S");
        mBtnSmall.setText("S");
        mBtnMedium.setVisibility(View.VISIBLE);
        mBtnMedium.setTextOff("M");
        mBtnMedium.setTextOn("M");
        mBtnMedium.setText("M");
        mBtnLarge.setVisibility(View.VISIBLE);
        mBtnLarge.setTextOff("L");
        mBtnLarge.setTextOn("L");
        mBtnLarge.setText("L");
        mBtnXLarge.setVisibility(View.VISIBLE);
        mBtnXLarge.setTextOff("XL");
        mBtnXLarge.setTextOn("XL");
        mBtnXLarge.setText("XL");
        mBtnXS.setVisibility(View.VISIBLE);
        mBtnXS.setTextOff("XS");
        mBtnXS.setTextOn("XS");
        mBtnXS.setText("XS");
        mBtnXXXL.setVisibility(View.VISIBLE);
        mBtnXXXL.setTextOff("XXXL");
        mBtnXXXL.setTextOn("XXXL");
        mBtnXXXL.setText("XXXL");
        mBtnXXL.setVisibility(View.VISIBLE);
        mBtnXXL.setTextOff("XXL");
        mBtnXXL.setTextOn("XXL");
        mBtnXXL.setText("XXL");
        mBtnXXS.setVisibility(View.VISIBLE);
        mBtnXXS.setTextOff("XXS");
        mBtnXXS.setTextOn("XXS");
        mBtnXXS.setText("XXS");
    }
    private void shoeSizeUS(){
        mBtnXS.setVisibility(View.VISIBLE);
        mBtnXS.setTextOff("6");
        mBtnXS.setTextOn("6");
        mBtnXS.setText("6");
        mBtnSmall.setVisibility(View.VISIBLE);
        mBtnSmall.setText("6.5");
        mBtnSmall.setTextOff("6.5");
        mBtnSmall.setTextOn("6.5");
        mBtnMedium.setVisibility(View.VISIBLE);
        mBtnMedium.setTextOff("7");
        mBtnMedium.setTextOn("7");
        mBtnMedium.setText("7");
        mBtnLarge.setVisibility(View.VISIBLE);
        mBtnLarge.setTextOff("7.5");
        mBtnLarge.setTextOn("7.5");
        mBtnLarge.setText("7.5");
        mBtnXLarge.setVisibility(View.VISIBLE);
        mBtnXLarge.setTextOff("8");
        mBtnXLarge.setTextOn("8");
        mBtnXLarge.setText("8");
        mBtnXXS.setVisibility(View.VISIBLE);
        mBtnXXS.setTextOff("8.5");
        mBtnXXS.setTextOn("8.5");
        mBtnXXS.setText("8.5");
        mBtnXXL.setVisibility(View.VISIBLE);
        mBtnXXL.setTextOff("9");
        mBtnXXL.setTextOn("9");
        mBtnXXL.setText("9");
        mBtnXXXL.setVisibility(View.VISIBLE);
        mBtnXXXL.setTextOff("9.5");
        mBtnXXXL.setTextOn("9.5");
        mBtnXXXL.setText("9.5");
    }
    private void shoeSizeUK(){
        mBtnXS.setVisibility(View.VISIBLE);
        mBtnXS.setTextOff("4");
        mBtnXS.setTextOn("4");
        mBtnXS.setText("4");
        mBtnSmall.setVisibility(View.VISIBLE);
        mBtnSmall.setText("4.5");
        mBtnSmall.setTextOff("4.5");
        mBtnSmall.setTextOn("4.5");
        mBtnMedium.setVisibility(View.VISIBLE);
        mBtnMedium.setTextOff("5");
        mBtnMedium.setTextOn("5");
        mBtnMedium.setText("5");
        mBtnLarge.setVisibility(View.VISIBLE);
        mBtnLarge.setTextOff("5.5");
        mBtnLarge.setTextOn("5.5");
        mBtnLarge.setText("5.5");
        mBtnXLarge.setVisibility(View.VISIBLE);
        mBtnXLarge.setTextOff("6");
        mBtnXLarge.setTextOn("6");
        mBtnXLarge.setText("6");
        mBtnXXS.setVisibility(View.VISIBLE);
        mBtnXXS.setTextOff("6.5");
        mBtnXXS.setTextOn("6.5");
        mBtnXXS.setText("6.5");
        mBtnXXL.setVisibility(View.VISIBLE);
        mBtnXXL.setTextOff("7");
        mBtnXXL.setTextOn("7");
        mBtnXXL.setText("7");
        mBtnXXXL.setVisibility(View.VISIBLE);
        mBtnXXXL.setTextOff("7.5");
        mBtnXXXL.setTextOn("7.5");
        mBtnXXXL.setText("7.5");
    }
    private void shoeSizeEU(){
        mBtnXS.setVisibility(View.VISIBLE);
        mBtnXS.setTextOff("36-37");
        mBtnXS.setTextOn("36-37");
        mBtnXS.setText("36-37");
        mBtnSmall.setVisibility(View.VISIBLE);
        mBtnSmall.setText("37");
        mBtnSmall.setTextOff("37");
        mBtnSmall.setTextOn("37");
        mBtnMedium.setVisibility(View.VISIBLE);
        mBtnMedium.setTextOff("37-38");
        mBtnMedium.setTextOn("37-38");
        mBtnMedium.setText("37-38");
        mBtnLarge.setVisibility(View.VISIBLE);
        mBtnLarge.setTextOff("38");
        mBtnLarge.setTextOn("38");
        mBtnLarge.setText("38");
        mBtnXLarge.setVisibility(View.VISIBLE);
        mBtnXLarge.setTextOff("38-39");
        mBtnXLarge.setTextOn("38-39");
        mBtnXLarge.setText("38-39");
        mBtnXXS.setVisibility(View.VISIBLE);
        mBtnXXS.setTextOff("39");
        mBtnXXS.setTextOn("39");
        mBtnXXS.setText("39");
        mBtnXXL.setVisibility(View.VISIBLE);
        mBtnXXL.setTextOff("39-40");
        mBtnXXL.setTextOn("39-40");
        mBtnXXL.setText("39-40");
        mBtnXXXL.setVisibility(View.VISIBLE);
        mBtnXXXL.setTextOff("40");
        mBtnXXXL.setTextOn("40");
        mBtnXXXL.setText("40");
    }

    public void sellerChooseAddress(View view) {
        Intent i =new Intent(SellProduct.this,ChooseAddress.class);
        i.putExtra("fromSellProd",true);
        startActivity(i);
    }
    public void SellprodBack(){//back button
        super.onBackPressed();
        finish();
    }
    public void getAddress(){//gets the addresses
        final String URL = "https://gascojanreb.000webhostapp.com/ShopifyCAB/getAddresses.php";
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        StringRequest jsonObjectRequest = new StringRequest(Request.Method.DEPRECATED_GET_OR_POST,
                URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {//gets the php json responses
                try {
                    JSONArray array = new JSONArray(response);

                    JSONObject addressObject = array.getJSONObject(0);

                    int id = addressObject.getInt("id");
                    String fullname = addressObject.getString("fullname");
                    int phone = addressObject.getInt("phone");
                    int lotNum = addressObject.getInt("lot_number");
                    String region = addressObject.getString("region");
                    String province = addressObject.getString("province");
                    String city = addressObject.getString("city");
                    String barangay = addressObject.getString("barangay");
                    int postal = addressObject.getInt("postal");
                    String username = addressObject.getString("username");

                    if (!(addressObject.length() < 0)){
                        address.append(fullname+ " | (+63) " +phone+ "\n" +
                                city+ "\n" +
                                "#"+lotNum+", "+barangay+ ", " +city+ region+ ", " +province+ "\n" +
                                "Postal code "+postal);
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
                String user_name  = PreferenceUtils.getEmail(SellProduct.this);
                parameters.put("username",user_name);

                return parameters;
            }
        };
        requestQueue.add(jsonObjectRequest);
    }
}