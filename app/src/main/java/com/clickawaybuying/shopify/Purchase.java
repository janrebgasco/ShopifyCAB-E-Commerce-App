package com.clickawaybuying.shopify;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Point;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.DecelerateInterpolator;
import android.webkit.PermissionRequest;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.clickawaybuying.shopify.Adapters.ColorAdapter;
import com.clickawaybuying.shopify.Adapters.FeedbackAdapter;
import com.clickawaybuying.shopify.Adapters.SizeAdapter;
import com.clickawaybuying.shopify.Adapters.SizeAdapter2;
import com.clickawaybuying.shopify.classes.Product;
import com.clickawaybuying.shopify.utils.PreferenceUtils;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Console;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;


public class Purchase extends AppCompatActivity {
    //initialize varibles
    List<String> quanAndSize = new LinkedList<>();
    List<String> linky = new ArrayList<>();
    List<Double> Ratings = new ArrayList<>();
    List<String> color = new ArrayList<>();
    List<Integer> maxLength = new ArrayList<>();
    List<Product> feedbacks = new ArrayList<>();
    public static List<Integer> selectedparentColorsQuan = new LinkedList<>();
    public static List<Integer> cartSelectedColorQuan = new LinkedList<>();
    public static List<Integer> cartSelectedColors = new LinkedList<>();
    public static List<Integer> cartSelectedSizeQuan = new LinkedList<>();
    public static List<Integer> cartSelectedSizes= new LinkedList<>();
    public static List<String> parentColors = new LinkedList<>();
    public static List<Integer> selectedSizeQuan = new LinkedList<>();
    public static List<Integer> selectedColorQuan = new LinkedList<>();
    public static List<Integer> selectedColorQuan2 = new LinkedList<>();
    public static List<String> selectedSize = new LinkedList<>();
    public static List<String> size = new ArrayList<>();
    public static List<String> selectedColors = new ArrayList<>();
    public static List<String> colorIndexes = new LinkedList<>();
    public static List<String> sizeIndexes = new LinkedList<>();
    List<String> combinedData = new LinkedList<>();
    RecyclerView colorRecycler,sizeRecycler,fBackRecycler,sizeRecycler2;
    ColorAdapter colorAdapter;
    SizeAdapter sizeAdapter;
    SizeAdapter2 sizeAdapter2;
    FeedbackAdapter feedbackAdapter;
    public static TextView JProdname, JProdPrice, JProdesc,JBrand,JStocks,JSold,mSeller,mColorTxt,mSizeTxt,mtxtQuan,mFeedback,mFeedbackTxt;
    public static ImageView JImage,mSellerImg;
    public static TextView quantity;
    public static String seller,colors,sizes,sellerImage;
    public static int price,id;
    String image;
    float rate;
    double rating;

    int Quantity=1,totalSelectedLength,stocks,quan = 1;
    Button JAdd,JMinus,mChat,mBuy,mCart,imgProxy;
    RequestQueue requestQueue;
    ImageButton heart,mCartIcon,heartBlue;
    ConstraintLayout mContainer;
    Boolean viewOnly = false;
    RatingBar JRating;
    String[] selectedSizesQuantity;
    boolean isImageFitToScreen;
    Animator currentAnimator;
    int shortAnimationDuration;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_purchase);
        //assigning values to variable
        JProdname = findViewById(R.id.Prodname);
        JProdPrice = findViewById(R.id.Prodprice);
        JProdesc = findViewById(R.id.ProdDescription);
        JImage = findViewById(R.id.ProdImage);
        imgProxy = findViewById(R.id.imageProxy);
        JRating = findViewById(R.id.prodRating);
        JBrand = findViewById(R.id.ProdBrand);
        JStocks = findViewById(R.id.prodStocks);
        JSold = findViewById(R.id.ProdSold);
        quantity = findViewById(R.id.purchQuan);
        JAdd = findViewById(R.id.increaseBtn);
        JMinus = findViewById(R.id.decreaseBtn);
        heart = findViewById(R.id.heartRed);
        mSeller = findViewById(R.id.sellerName);
        mChat = findViewById(R.id.chatSeller);
        requestQueue = Volley.newRequestQueue(getApplicationContext());//manages worker threads for running the network operations
        colorRecycler = findViewById(R.id.colorRecycler);
        sizeRecycler = findViewById(R.id.sizeRecycler);
        sizeRecycler2 = findViewById(R.id.sizeRecycler2);
        fBackRecycler = findViewById(R.id.fBackRecycler);
        mContainer = findViewById(R.id.variationContainer);
        mColorTxt = findViewById(R.id.textView43);
        mSizeTxt = findViewById(R.id.textView44);
        mBuy = findViewById(R.id.btnBuyNow);
        mCart = findViewById(R.id.button6);
        mCartIcon = findViewById(R.id.btnCart2);
        mtxtQuan = findViewById(R.id.quan);
        heartBlue = findViewById(R.id.heartBlue);
        mFeedback = findViewById(R.id.textView19);
        mFeedbackTxt = findViewById(R.id.textView21);
        mSellerImg = findViewById(R.id.seller_image);

        //creates new arraylists
        ColorAdapter.selectedColors = new ArrayList<>();
        ColorAdapter.selectedColorQuan = new ArrayList<>();
        SizeAdapter.selectedSize = new ArrayList<>();
        SizeAdapter.selectedSizeQuan = new ArrayList<>();
        size = new ArrayList<>();
        selectedColors = new ArrayList<>();


        JAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {//add quantity when user clicked
                Quantity++;
                quantity.setText(String.valueOf(Quantity));
            }
        });
        JMinus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {//decrease quantity when user clicks
                if(Quantity<=1){Quantity = 2;}
                Quantity--;
                quantity.setText(String.valueOf(Quantity));
            }
        });

        mChat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {//chect seller on click
                String user_name = PreferenceUtils.getEmail(Purchase.this);// get shared preference username
                if (user_name==null){//checks if username is null
                    Toast.makeText(Purchase.this,"Login first to use this feature",Toast.LENGTH_LONG).show();
                }
                else{// go to chat room
                    if (!seller.equals(user_name)){
                        Intent i = new Intent(Purchase.this,ChatRoom.class);
                        i.putExtra("seller",seller);//pass a value to another activity
                        startActivity(i);
                    }
                    else{
                        Toast.makeText(Purchase.this, "You can't Chat with yourself", Toast.LENGTH_SHORT).show();
                    }
                }

            }
        });
        imgProxy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                requestPermissions();
            }
        });

        //call methods
        catchIntents();
        getFeedbacks();
        arraySplitter();
        conditions();
        setupAdapters();
        checkPreviewOnly();
        setupArrays();
        setupArrayColor();
        setupColorQuanArr();
        setupCartColorArrays();
        setupCartSizeArrays();

        for (int i = 0;i< color.size();i++){
            Purchase.selectedColors.add("null");
        }

        // Hook up clicks on the thumbnail views.

        imgProxy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                zoomImageFromThumb(imgProxy, image);
            }
        });

        // Retrieve and cache the system's default "short" animation time.
        shortAnimationDuration = getResources().getInteger(
                android.R.integer.config_shortAnimTime);

    }
    @RequiresApi(api = Build.VERSION_CODES.HONEYCOMB)
    private void zoomImageFromThumb(final View thumbView, String image) {
        // If there's an animation in progress, cancel it
        // immediately and proceed with this one.
        if (currentAnimator != null) {
            currentAnimator.cancel();
        }

        // Load the high-resolution "zoomed-in" image.
        final ImageView expandedImageView = (ImageView) findViewById(
                R.id.expanded_image);
        Glide.with(Purchase.this).load(image).into(expandedImageView);

        // Calculate the starting and ending bounds for the zoomed-in image.
        // This step involves lots of math. Yay, math.
        final Rect startBounds = new Rect();
        final Rect finalBounds = new Rect();
        final Point globalOffset = new Point();

        // The start bounds are the global visible rectangle of the thumbnail,
        // and the final bounds are the global visible rectangle of the container
        // view. Also set the container view's offset as the origin for the
        // bounds, since that's the origin for the positioning animation
        // properties (X, Y).
        thumbView.getGlobalVisibleRect(startBounds);
        findViewById(R.id.containerParent)
                .getGlobalVisibleRect(finalBounds, globalOffset);
        startBounds.offset(-globalOffset.x, -globalOffset.y);
        finalBounds.offset(-globalOffset.x, -globalOffset.y);

        // Adjust the start bounds to be the same aspect ratio as the final
        // bounds using the "center crop" technique. This prevents undesirable
        // stretching during the animation. Also calculate the start scaling
        // factor (the end scaling factor is always 1.0).
        float startScale;
        if ((float) finalBounds.width() / finalBounds.height()
                > (float) startBounds.width() / startBounds.height()) {
            // Extend start bounds horizontally
            startScale = (float) startBounds.height() / finalBounds.height();
            float startWidth = startScale * finalBounds.width();
            float deltaWidth = (startWidth - startBounds.width()) / 2;
            startBounds.left -= deltaWidth;
            startBounds.right += deltaWidth;
        } else {
            // Extend start bounds vertically
            startScale = (float) startBounds.width() / finalBounds.width();
            float startHeight = startScale * finalBounds.height();
            float deltaHeight = (startHeight - startBounds.height()) / 2;
            startBounds.top -= deltaHeight;
            startBounds.bottom += deltaHeight;
        }

        // Hide the thumbnail and show the zoomed-in view. When the animation
        // begins, it will position the zoomed-in view in the place of the
        // thumbnail.
        thumbView.setAlpha(0f);
        expandedImageView.setVisibility(View.VISIBLE);

        // Set the pivot point for SCALE_X and SCALE_Y transformations
        // to the top-left corner of the zoomed-in view (the default
        // is the center of the view).
        expandedImageView.setPivotX(0f);
        expandedImageView.setPivotY(0f);

        // Construct and run the parallel animation of the four translation and
        // scale properties (X, Y, SCALE_X, and SCALE_Y).
        AnimatorSet set = new AnimatorSet();
        set
                .play(ObjectAnimator.ofFloat(expandedImageView, View.X,
                        startBounds.left, finalBounds.left))
                .with(ObjectAnimator.ofFloat(expandedImageView, View.Y,
                        startBounds.top, finalBounds.top))
                .with(ObjectAnimator.ofFloat(expandedImageView, View.SCALE_X,
                        startScale, 1f))
                .with(ObjectAnimator.ofFloat(expandedImageView,
                        View.SCALE_Y, startScale, 1f));
        set.setDuration(shortAnimationDuration);
        set.setInterpolator(new DecelerateInterpolator());
        set.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                currentAnimator = null;
            }

            @Override
            public void onAnimationCancel(Animator animation) {
                currentAnimator = null;
            }
        });
        set.start();
        currentAnimator = set;

        // Upon clicking the zoomed-in image, it should zoom back down
        // to the original bounds and show the thumbnail instead of
        // the expanded image.
        final float startScaleFinal = startScale;
        expandedImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (currentAnimator != null) {
                    currentAnimator.cancel();
                }

                // Animate the four positioning/sizing properties in parallel,
                // back to their original values.
                AnimatorSet set = new AnimatorSet();
                set.play(ObjectAnimator
                        .ofFloat(expandedImageView, View.X, startBounds.left))
                        .with(ObjectAnimator
                                .ofFloat(expandedImageView,
                                        View.Y,startBounds.top))
                        .with(ObjectAnimator
                                .ofFloat(expandedImageView,
                                        View.SCALE_X, startScaleFinal))
                        .with(ObjectAnimator
                                .ofFloat(expandedImageView,
                                        View.SCALE_Y, startScaleFinal));
                set.setDuration(shortAnimationDuration);
                set.setInterpolator(new DecelerateInterpolator());
                set.addListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        thumbView.setAlpha(1f);
                        expandedImageView.setVisibility(View.GONE);
                        currentAnimator = null;
                    }

                    @Override
                    public void onAnimationCancel(Animator animation) {
                        thumbView.setAlpha(1f);
                        expandedImageView.setVisibility(View.GONE);
                        currentAnimator = null;
                    }
                });
                set.start();
                currentAnimator = set;
            }
        });
    }

    private void showRatings() {
        float ratings = Float.parseFloat(String.valueOf(rating));
        JRating.setRating(ratings);
    }


    private void checkPreviewOnly() {//checks if the product is only for preview
        previewChecker();
        for (int i=0;i<size.size();i++){
            SizeAdapter.selectedSizeQuan.add("0");
        }
    }

    private void setupAdapters() {//set up adapters for color and size variation
        colorAdapter = new ColorAdapter(this,color);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this,3,GridLayoutManager.VERTICAL,false);
        colorRecycler.setLayoutManager(gridLayoutManager);
        colorRecycler.setAdapter(colorAdapter);

        sizeAdapter = new SizeAdapter(this,size,0,"empty");
        GridLayoutManager grid = new GridLayoutManager(this,1,GridLayoutManager.HORIZONTAL,false);
        sizeRecycler.setLayoutManager(grid);
        sizeRecycler.setAdapter(sizeAdapter);


        sizeAdapter2 = new SizeAdapter2(this,selectedColors,size);
        GridLayoutManager grid2 = new GridLayoutManager(this,1,GridLayoutManager.VERTICAL,false);
        sizeRecycler2.setLayoutManager(grid2);
        sizeRecycler2.setAdapter(sizeAdapter2);


        GridLayoutManager fbackLayout = new GridLayoutManager(this,1,GridLayoutManager.VERTICAL,false);
        fBackRecycler.setLayoutManager(fbackLayout);


    }

    private void arraySplitter() {//splits up array values
        String[] colorList = colors.split(",");
        color.addAll(Arrays.asList(colorList));

        String[] sizeList = sizes.split(",");
        size.addAll(Arrays.asList(sizeList));
    }

    private void conditions() {//checks if conditions are met
        if (colors.isEmpty() && sizes.isEmpty()){
            mContainer.setVisibility(View.GONE);
        }
        if (colors.equals("null") && sizes.equals("null")){
            mContainer.setVisibility(View.GONE);
        }
        if (colors.equals("") && sizes.equals("null")){
            mContainer.setVisibility(View.GONE);
        }
        if (colors.equals("null") && sizes.equals("")){
            mContainer.setVisibility(View.GONE);
        }
        if (colors.isEmpty()||colors.equals("null")){
            mColorTxt.setVisibility(View.GONE);
            colorRecycler.setVisibility(View.GONE);
            sizeRecycler.setVisibility(View.VISIBLE);
        }
        if (sizes.isEmpty()||sizes.equals("null")){
            mSizeTxt.setVisibility(View.GONE);
            sizeRecycler.setVisibility(View.GONE);
            sizeRecycler2.setVisibility(View.GONE);
        }
        if (!colors.isEmpty() && sizes.isEmpty()){
            JAdd.setVisibility(View.INVISIBLE);
            JMinus.setVisibility(View.INVISIBLE);
            Quantity = 0;
            quantity.setText(String.valueOf(Quantity));
        }
        if (!sizes.isEmpty() && color.isEmpty()){
            JAdd.setVisibility(View.INVISIBLE);
            JMinus.setVisibility(View.INVISIBLE);
            Quantity = 0;
            quantity.setText(String.valueOf(Quantity));
        }
        if (!colors.equals("null") && sizes.equals("null")){
            JAdd.setVisibility(View.INVISIBLE);
            JMinus.setVisibility(View.INVISIBLE);
            Quantity = 0;
            quantity.setText(String.valueOf(Quantity));
        }
        if (!colors.isEmpty()||colors.equals("null")){
            JAdd.setVisibility(View.INVISIBLE);
            JMinus.setVisibility(View.INVISIBLE);
            Quantity = 0;
            quantity.setText(String.valueOf(Quantity));
        }
        if (!sizes.isEmpty()||sizes.equals("null")){
            JAdd.setVisibility(View.INVISIBLE);
            JMinus.setVisibility(View.INVISIBLE);
            Quantity = 0;
            quantity.setText(String.valueOf(Quantity));
        }
    }

    private void catchIntents() {
        // Catching incoming intent
        Intent intent = getIntent();
        id = intent.getIntExtra("id", 0);
        price = intent.getIntExtra("price", 0);
        stocks = intent.getIntExtra("stocks", 0);
        int sold = intent.getIntExtra("sold", 0);
        //rate = intent.getFloatExtra("rate", 0);
        String title = intent.getStringExtra("title");
        image = intent.getStringExtra("image");
        String brand = intent.getStringExtra("brand");
        String description = intent.getStringExtra("description");
        seller = intent.getStringExtra("seller");
        colors = intent.getStringExtra("color");
        sizes = intent.getStringExtra("size");
        sellerImage = intent.getStringExtra("sellerImage");
        viewOnly = intent.getBooleanExtra("viewOnly",false);

        if (intent != null) {
            //JRating.setRating(rate);
            JProdname.setText(title);
            JBrand.setText(brand);
            JProdesc.setText(description);
            DecimalFormat formatter = new DecimalFormat("#,###");//format for thousand
            String Price = formatter.format(price);
            JProdPrice.setText(String.valueOf(Price));
            JStocks.setText(String.valueOf(stocks));
            JSold.setText(String.valueOf(sold));
            mSeller.setText(seller);
            Glide.with(Purchase.this).load(sellerImage).into(mSellerImg);
            Glide.with(Purchase.this).load(image).into(JImage);
        }
    }

    public void redClick(View v) {
        heart.setVisibility(View.VISIBLE);
    }//heart button
    public void btnPrevious(View v){
        Intent i = new Intent(this,HomeScreen.class);
        startActivity(i);
    }//back button
    public void btnBuy(View v){//buy button
        //filters the arraylist
        /**ColorAdapter.selectedColors.removeAll(Collections.singleton("null"));
        int colorLength = ColorAdapter.selectedColors.size();
        int sizeLength = selectedSizeQuan.size();
        SizeAdapter.selectedSize.removeAll(Collections.singleton("null"));
        SizeAdapter.selectedSizeQuan.removeAll(Collections.singleton("0"));
        selectedSize.removeAll(Collections.singleton("null"));

        if (colors.isEmpty()) {colorLength = -1;}

        if (sizes.isEmpty()) {sizeLength = -1;}

         else if(colorLength == 0){
         Toast.makeText(getApplicationContext(),"Please select a color",Toast.LENGTH_LONG).show();
         }
         else if(sizeLength == 0){
         Toast.makeText(getApplicationContext(),"Please select a size",Toast.LENGTH_LONG).show();
         }
        **/


        maxLength = new ArrayList<>();
        maxLength.add(ColorAdapter.selectedColors.size());
        maxLength.add(SizeAdapter.selectedSize.size());
        String user_name  = PreferenceUtils.getEmail(this);
        String s = quantity.getText().toString();
        int totalQuan = Integer.parseInt(s);

        if (user_name == null || user_name.equals("null")){
            Intent i = new Intent(this,LoginPage.class);
            startActivity(i);
        }

        if(totalQuan > stocks){
            Toast.makeText(getApplicationContext(),"Not enough stocks, STOCKS : "+stocks,Toast.LENGTH_LONG).show();
        }
        else if (totalQuan == 0){
            Toast.makeText(this, "Please select a Variation", Toast.LENGTH_SHORT).show();
        }
        else if (sizeRecycler2.getVisibility() == View.VISIBLE && colorRecycler.getVisibility() == View.VISIBLE){
            if (SizeAdapter.sizeTotal != ColorAdapter.maxSizeQuan){
                Toast.makeText(this, "Please select size/s", Toast.LENGTH_SHORT).show();
            }
            else if (Quantity == 0){
                Toast.makeText(this, "Please select a variation", Toast.LENGTH_SHORT).show();
            }
            else if (user_name != null){
                gotoCheckout();
            }
        }
        else if (user_name != null){
            gotoCheckout();
            //toastCombined();
        }
    }
    public void addToCart(View v){//add product to cart when user clicks
        String user_name = PreferenceUtils.getEmail(this);
        if (user_name==null){
            Toast.makeText(this,"Login first to use this feature",Toast.LENGTH_LONG).show();
        }
        else if(stocks == 0){
            Toast.makeText(this, "No stocks left", Toast.LENGTH_SHORT).show();
        }else{
            //Toast.makeText(this, ""+cartSelectedColors+"\n"+cartSelectedColorQuan+"\n\n"+cartSelectedSizes+"\n"+cartSelectedSizeQuan, Toast.LENGTH_SHORT).show();
            askAddToCart();
        }

    }
    public void onBackPressed() {
        finish();
    }//back button
    public void askAddToCart() {//ask user if he want to add product to cart
        new AlertDialog.Builder(this)
                .setMessage("Add this to cart?")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        insertToCart();
                        //Intent i = new Intent(Purchase.this,Cart.class);
                        //startActivity(i);
                    }
                })
                .setNegativeButton("No", null)
                .show();
    }
    public void insertToCart(){//insert product to database table cart
        Toast.makeText(this, "Please wait....", Toast.LENGTH_SHORT).show();
        final String user_name  = PreferenceUtils.getEmail(Purchase.this);
        String insertURL = "https://gascojanreb.000webhostapp.com/ShopifyCAB/insertToCart.php";
        StringRequest request = new StringRequest(Request.Method.DEPRECATED_GET_OR_POST, insertURL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {//gets the php json responses
                //Toast.makeText(Purchase.this, response, Toast.LENGTH_SHORT).show();
                if (response.length() < 1){
                    Toast.makeText(getApplicationContext(), "Item added to cart", Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {//gets php error messages
            @Override
            public void onErrorResponse(VolleyError error) {
                //Toast.makeText(Purchase.this, ""+error, Toast.LENGTH_SHORT).show();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> parameters = new HashMap<String, String>();//putting parameters to pass on the php file

                String array1 = cartSelectedColors.toString().replace("[","").replace("]","");
                String array2 = cartSelectedColorQuan.toString().replace("[","").replace("]","");
                String array3 = cartSelectedSizes.toString().replace("[","").replace("]","");
                String array4 = cartSelectedSizeQuan.toString().replace("[","").replace("]","");

                parameters.put("product_id",String.valueOf(id));
                parameters.put("product_name",JProdname.getText().toString());
                parameters.put("price",String.valueOf(price));
                //parameters.put("discounted_price",.getText().toString());
                parameters.put("image",image);
                parameters.put("rating",String.valueOf(rating));
                parameters.put("stocks",JStocks.getText().toString());
                parameters.put("brand",JBrand.getText().toString());
                parameters.put("description",JProdesc.getText().toString());
                //parameters.put("category",.getText().toString());
                parameters.put("sold",JSold.getText().toString());
                parameters.put("color",colors);
                parameters.put("size",sizes);
                parameters.put("seller",seller);
                parameters.put("quantity",quantity.getText().toString());
                parameters.put("color_indexes",array1);//
                parameters.put("size_indexes",array2);//color_quan
                if (sizeRecycler.getVisibility()==View.GONE){
                    parameters.put("color_quan","");
                    parameters.put("size_quan","");
                }
                else{
                    parameters.put("color_quan",array3);//size index
                    parameters.put("size_quan",array4);//
                }
                parameters.put("cart_of",user_name);

                return parameters;
            }
        };
        requestQueue.add(request);
    }
    private void getFeedbacks() {
        String URL = "https://gascojanreb.000webhostapp.com/ShopifyCAB/getFeedbacks.php";
        StringRequest jsonObjectRequest = new StringRequest(Request.Method.DEPRECATED_GET_OR_POST,
                URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {//gets the php json responses
                try {
                    JSONArray array = new JSONArray(response);

                    for (int i = 0; i<array.length();i++){
                        JSONObject fBack = array.getJSONObject(i);

                        String img = fBack.getString("image");
                        String username = fBack.getString("username");
                        Double rating = fBack.getDouble("rating");
                        String feedback = fBack.getString("feedback");

                        Product fback = new Product(img,username,rating,feedback);
                        feedbacks.add(fback);
                        Ratings.add(rating);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                feedbackAdapter = new FeedbackAdapter(Purchase.this,feedbacks);
                fBackRecycler.setAdapter(feedbackAdapter);
                calculateAverage(Ratings);
                showRatings();

            }
        }, new Response.ErrorListener() {//gets php error messages
            @Override
            public void onErrorResponse(VolleyError error) {
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> parameters = new HashMap<String, String>();//putting parameters to pass on the php file

                parameters.put("product_id",String.valueOf(id));

                return parameters;
            }
        };
        requestQueue.add(jsonObjectRequest);
    }


    public void openCartPurchase(View view) {//opens the cart when button was clicked
        Intent i = new Intent(this,Cart.class);
        startActivity(i);
    }
    public void gotoCheckout(){// goes to checkout when button was clicked
        combinedData();
        SizeAdapter.selectedSizeQuan.removeAll(Collections.singleton("0"));
        ColorAdapter.selectedColorQuan.removeAll(Collections.singleton("0"));
        Intent i = new Intent(Purchase.this, CheckOut.class);
        i.putExtra("title",JProdname.getText().toString());
        i.putExtra("rate",rating);
        i.putExtra("price",price);
        i.putExtra("description",JProdesc.getText().toString());
        i.putExtra("brand",JBrand.getText().toString());
        i.putExtra("stocks",JStocks.getText().toString());
        i.putExtra("sold",JSold.getText().toString());
        i.putExtra("id",id);
        i.putExtra("image", image);
        i.putExtra("quantity",Quantity);

        if (colorRecycler.getVisibility() == View.VISIBLE) {
            i.putExtra("color", String.valueOf(combinedData));
        }
        else{
            i.putExtra("color","[]");
        }
        if (sizeRecycler.getVisibility() == View.VISIBLE)
        {
            i.putExtra("size",String.valueOf(combinedData));
        }
        else{
            i.putExtra("size","[]");
        }
        if (colorRecycler.getVisibility() == View.VISIBLE && sizeRecycler2.getVisibility() == View.VISIBLE)
        {
            i.putExtra("combi","empty");
        }
        else{
            i.putExtra("combi","[]");
        }
        Purchase.this.startActivity(i);
    }
    public int getMax(List<Integer> maxNum){
        int max = Integer.MIN_VALUE;
        for(int i=0; i<maxNum.size(); i++){
            if(maxNum.get(i) > max){
                max = maxNum.get(i);
            }
        }
        return max;
    }
    public void previewChecker(){
        if (viewOnly){
            mBuy.setVisibility(View.GONE);
            mCart.setVisibility(View.GONE);
            mCartIcon.setVisibility(View.GONE);
            mChat.setVisibility(View.GONE);
            heart.setVisibility(View.GONE);
            JAdd.setVisibility(View.GONE);
            JMinus.setVisibility(View.GONE);
            mtxtQuan.setVisibility(View.GONE);
            quantity.setVisibility(View.GONE);
            heartBlue.setVisibility(View.GONE);
            mFeedback.setVisibility(View.GONE);
            mFeedbackTxt.setVisibility(View.GONE);
        }
    }

    public void colorTotalQuan(int colorTotal){//gets the quantity of color
        if (sizes.isEmpty()||sizes.equals("null")){
            Quantity = colorTotal;
        }
        totalSelectedLength = colorTotal;
        quantity.setText(String.valueOf(colorTotal));
    }
    public int sizeTotalQuan(int sizeTotal){//gets the size quantity then set it in textview
        if (colors.isEmpty()||colors.equals("null")){
            //totalSelectedLength = sizeTotal;
            quantity.setVisibility(View.VISIBLE);
            Quantity = sizeTotal;
            quantity.setText(String.valueOf(Quantity));
        }
        Quantity = sizeTotal;
        int sum = 0;

        for(int i = 0; i < selectedSizeQuan.size(); i++)
        {
            sum += selectedSizeQuan.get(i);
        }
        totalSelectedLength = sum;
        //Toast.makeText(this, ""+selectedSizeQuan+sum, Toast.LENGTH_SHORT).show();
        SizeAdapter.sizeTotal = sum;
        return sum;
    }
    public int sizeTotalQuantity(int sizeTotal){//gets the size quantity without setting it in textview
        if (colors.isEmpty()||colors.equals("null")){
            //totalSelectedLength = sizeTotal;
            quantity.setVisibility(View.VISIBLE);
            Quantity = sizeTotal;
            quantity.setText(String.valueOf(Quantity));
        }
        //Quantity = sizeTotal;
        int sum = 0;

        for(int i = 0; i < selectedSizeQuan.size(); i++)
        {
            sum += selectedSizeQuan.get(i);
        }
        totalSelectedLength = sum;
        SizeAdapter.sizeTotal = sum;
        Quantity = sum;
        //Toast.makeText(this, ""+selectedSizeQuan+sum, Toast.LENGTH_SHORT).show();

        return sum;
    }
    private void splitValue(){
        for (int i=0;i<size.size();i++){
            String[] splitedValue = SizeAdapter.selectedSizeQuan.get(i).split(",");
            for (int j=0;j<splitedValue.length;j++){
                Log.i(i+" at ArrayIndex "+j+" at splitedIndex Value is >> ",splitedValue[j]);
                Toast.makeText(this, ""+i+" at ArrayIndex "+j+" at splitedIndex Value is >> "+splitedValue[j], Toast.LENGTH_SHORT).show();
                String[] splitedValue1 = splitedValue[j].split("~");
                if(splitedValue1.length==1){
                    continue;
                }
                for (int k=0;k<splitedValue1.length;k++){
                    Log.i(j+" at splitedIndex "+k+" at splited1Index Value is >> ",splitedValue1[k]);
                    Toast.makeText(this, ""+j+" at splitedIndex "+k+" at splited1Index Value is >> "+splitedValue1[k], Toast.LENGTH_SHORT).show();
                }
            }

        }
    }
    private double calculateAverage(List <Double> marks) {
        Double sum = 0.0;
        if(!marks.isEmpty()) {
            for (int i = 0;i < marks.size();i++){
                sum += Ratings.get(i);
            }
            rating = sum / marks.size();
        }
        return 0;
    }

    public void notifyChanges() {
        sizeAdapter2.notifyDataSetChanged();
        setupArrays();
    }
    private void sample(){
        SizeAdapter.selectedSizeQuan.removeAll(Collections.singleton("0"));
        for (int j = 0;j < SizeAdapter.selectedSizeQuan.size();j++){
            quanAndSize.add(SizeAdapter.selectedSizeQuan.get(j)+" "+SizeAdapter.selectedSize.get(j));

        }
        Toast.makeText(this, ""+quanAndSize, Toast.LENGTH_SHORT).show();
        quanAndSize = new ArrayList<>();
    }
    public void test(){
        List<List<String>> parent = new ArrayList<>();
        // Initialize child array
        List<String> child = new ArrayList<>();
        child.add("one");
        child.add("two");
        parent.add(child);



        Toast.makeText(this, ""+parentColors, Toast.LENGTH_SHORT).show();
        //selectedSize = new ArrayList<>();

    }
    private void setupArrays(){
        selectedSize = new LinkedList<>();
        parentColors = new LinkedList<>();
        selectedSizeQuan = new LinkedList<>();
        for (int i = 0; i < size.size() * color.size();i++){
            selectedSize.add("null");
            selectedSizeQuan.add(0);
            parentColors.add("null");
        }
    }
    private void setupColorQuanArr(){
        selectedparentColorsQuan = new LinkedList<>();
        for (int i = 0; i < size.size() * color.size();i++){
            selectedparentColorsQuan.add(0);
        }
    }
    private void setupCartColorArrays(){
        cartSelectedColorQuan = new LinkedList<>();
        cartSelectedColors = new LinkedList<>();
        for (int i = 0; i < color.size(); i++){
            cartSelectedColorQuan.add(0);
            cartSelectedColors.add(0);
        }
    }
    private void setupCartSizeArrays(){
        cartSelectedSizeQuan = new LinkedList<>();
        cartSelectedSizes = new LinkedList<>();
        for (int i = 0; i < color.size() * size.size(); i++){
            cartSelectedSizeQuan.add(0);
            cartSelectedSizes.add(0);
        }
    }
    private void setupArrayColor(){
        selectedColorQuan = new LinkedList<>();
        for (int i = 0; i < color.size();i++){
            selectedColorQuan.add(0);
        }
    }
    public void removeArrayObject(){
        selectedSize = new LinkedList<>();
        selectedSizeQuan = new LinkedList<>();
        for (int i = 0; i < size.size() * color.size();i++){
            selectedSize.add("null");
            selectedSizeQuan.add(0);
        }

        int sum = 0;

        for(int i = 0; i < selectedSizeQuan.size(); i++)
        {
            sum += selectedSizeQuan.get(i);
        }
        totalSelectedLength = sum;
        SizeAdapter.sizeTotal = sum;
    }
    private void combinedData(){
        boolean SizeIsVisible = false;
        boolean ColorIsVisible = false;
        int colorSize = 1,sizeSize = 1;

        if (sizeRecycler.getVisibility() == View.VISIBLE){
            SizeIsVisible = true;
            sizeSize = size.size();
        }
        if (colorRecycler.getVisibility() == View.VISIBLE){
            ColorIsVisible = true;
            colorSize = color.size();
        }

        combinedData = new LinkedList<>();
        List<String> A1 = new LinkedList<>(parentColors);
        List<Integer> A2 = new LinkedList<>(selectedColorQuan);
        List<String> A3 = new LinkedList<>(selectedSize);
        List<Integer> A4 = new LinkedList<>(selectedSizeQuan);

        A1.removeAll((Collections.singleton("null")));
        A2.removeAll((Collections.singleton(0)));
        A3.removeAll((Collections.singleton("null")));
        A4.removeAll((Collections.singleton(0)));
        if (ColorIsVisible && sizeRecycler2.getVisibility() == View.VISIBLE){
            for (int i = 0; i < size.size() * color.size(); i++) {
                combinedData.add("(" + parentColors.get(i) + ")" + selectedSize.get(i) +" "+ selectedSizeQuan.get(i)+"x");
            }
            for (int k = 0; k < A1.size(); k++){
                //Toast.makeText(this, ""+i, Toast.LENGTH_SHORT).show();
                //Toast.makeText(this, ""+parentColors+""+selectedColorQuan+""+selectedSize+""+selectedSizeQuan, Toast.LENGTH_SHORT).show();
                //combinedData.add("(" + parentColors.get(i) +" "+selectedColorQuan.get(i)+"x"+ ")" + selectedSize.get(i) +" "+ selectedSizeQuan.get(i)+"x");
                //Toast.makeText(this, ""+A1 +" "+A2+"x"+ ")" + A3+" "+ A4+"x", Toast.LENGTH_SHORT).show();
                //combinedData.add("(" + A1.get(k) +" "+A2.get(k)+"x"+ ")" + A3.get(k) +" "+ A4.get(k)+"x");
                //combinedData.add("(" + parentColors.get(i) + ")" + selectedSize.get(i) +" "+ selectedSizeQuan.get(i)+"x");
            }
        }
        else{
            for (int i = 0; i < sizeSize * colorSize; i++) {
                if (ColorIsVisible){
                    combinedData.add(selectedColors.get(i) +" "+ selectedColorQuan.get(i)+"x");
                }
                if(SizeIsVisible){
                    combinedData.add(selectedSize.get(i) +" "+ selectedSizeQuan.get(i)+"x");
                }
            }
        }
        combinedData.removeAll(Collections.singleton("(null)null 0x"));
        combinedData.removeAll(Collections.singleton("null 0x"));
        //Toast.makeText(this, ""+combinedData, Toast.LENGTH_SHORT).show();

        A1 = new LinkedList<>();
        A2 = new LinkedList<>();
        A3 = new LinkedList<>();
        A4 = new LinkedList<>();
    }

    public void removeArray(int position){
        int pos  = (position - 1) + position;
        for (int i = 0;i < size.size();i++){
            int finalPos = pos + i;
            selectedSize.set(finalPos,"null");
            selectedSizeQuan.set(finalPos,0);

        }
    }



    private void requestPermissions() {
        Dexter.withActivity(this)
                .withPermissions(
                        Manifest.permission.CAMERA,
                        Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE
                ).withListener(new MultiplePermissionsListener() {
            @Override
            public void onPermissionsChecked(MultiplePermissionsReport report) {
            }

            @Override
            public void onPermissionRationaleShouldBeShown(List<com.karumi.dexter.listener.PermissionRequest> list, PermissionToken permissionToken) {
                Toast.makeText(Purchase.this, "Permission not granted !", Toast.LENGTH_SHORT).show();
            }
        })
                .check();
    }

}