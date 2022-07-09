package com.clickawaybuying.shopify;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;

import com.clickawaybuying.shopify.Adapters.Adapter;
import com.clickawaybuying.shopify.Adapters.BuyTipAdapter;
import com.clickawaybuying.shopify.Adapters.SellTipAdapter;

import java.util.ArrayList;
import java.util.List;

public class TipsAndTricks extends AppCompatActivity {
    RecyclerView SellTipRecycler,buyTipRecycler;
    List<String> titles = new ArrayList<>();
    List<Integer> images = new ArrayList<>();
    List<String> description = new ArrayList<>();
    List<String> titles2 = new ArrayList<>();
    List<Integer> images2 = new ArrayList<>();
    List<String> description2 = new ArrayList<>();
    SellTipAdapter adapter;
    BuyTipAdapter buyTipAdapter;
    ConstraintLayout mCons1,mCons2;
    boolean hideP1 = false,hideP2 = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tips_and_tricks);
        SellTipRecycler = findViewById(R.id.tipsRecycler1);
        buyTipRecycler = findViewById(R.id.tipsRecycler2);
        mCons1 = findViewById(R.id.R1Parent);
        mCons2 = findViewById(R.id.R2Parent);

        titles = new ArrayList<>();
        images = new ArrayList<>();
        description = new ArrayList<>();
        titles2 = new ArrayList<>();
        images2 = new ArrayList<>();
        description2 = new ArrayList<>();

        titles.add("1. A good picture is worth a thousand words");
        images.add(0);
        description.add("Bring your products to life with great quality pictures to entice the buyer to get a 'feel' of the product. When staging your photoshoot, try to use a polished background, clean lighting and creative angles.");
        titles.add("2. Give detailed and engaging descriptions");
        images.add(0);
        description.add("Once you have captured the buyer's attention and he/she peeks at your listing, you're now halfway through to closing the deal. Just make sure you include everything the customer may need to know about your product. Give the buyer descriptive details of your product (e.g. colour, size, material, measurements) as if he/she is going to buy it from a real store. Be detailed, be honest! You can't go wrong there.");
        titles.add("3. Set a reasonable price");
        images.add(0);
        description.add("\"How much does this cost?\" This is genrally the first question that pops into the mind of a buyer. No surprises there though, we are always on the hunt for the best prices. Do some research on market prices from other sellers and aim to offer a better deal. The buyer will continuously search until they find the best offer.");
        titles.add("4. Build customer loyalty");
        images.add(4);
        description.add("Be thoughtful, friendly and responsive to your customers. Give them incentives to return to your shop. Repeat business plays an important role in keeping your shop successful. Include small personal gestures that will give your buyer a different mobile shopping experience. Some tips are offering same-day delivery, discount for self-collections, little thank you notes or any other perks you can provide will be a bonus!");
        titles.add("5. Run regular promotions");
        images.add(0);
        description.add("Who doesn't love a sale? Running promotions will keep your buyers happy and give them something to look forward to. Run small experiments with different promotional mechanics to see what works best for you. Just make sure you don't overdo it; after all... You want your customers to be excited about your products, not your sales.");
        titles.add("6. Be social");
        images.add(0);
        description.add("Share your listings using ShopifyCAB’s social media sharing function! With a simple tap, get more views and likes to drive traffic to your shop. Doesn't hurt...");
        titles.add("7. Keep your documents");
        images.add(0);
        description.add("You've always been told to \"keep supporting documents\". The same applies here. Update your customers by providing shipment proof to let them know the products are on the way. This will show that you have completed your part of the transaction. This will also protect you when the customer forgets to confirm the receipt of his/her purchase to release the payment or if the shipment is lost.");
        titles.add("8. Stay up to date!");
        images.add(0);
        description.add("If someone has purchased something from your shop, you can assume they will be interested in similar products in the future. Ensure that your customers check back from time to time by adding new listings or take new product shots to improve your visuals. With all these tips, your shop is ready to make its debut on ShopifyCAB mobile marketplace!");

        titles2.add("1. ShopifyCAB Guarantee got you covered!");
        images2.add(0);
        description2.add("One of the problems with online shopping is ensuring secure secure payments. We understand that safety is important to you. That's why we created ShopifyCAB Guarantee to protect all of our users. It is a payment method that is covered under ShopifyCAB whether you pay by a credit card or bank transfer. With ShopifyCAB Guarantee, you are protected with every purchase you make. Your money will be held by ShopifyCAB until you confirm the arrival of your purchase. The funds will then be transferred to the seller. Receive your order, or get your money back. Find out more details on ShopifyCAB Guarantee here.");
        titles2.add("2. Know your seller");
        images2.add(0);
        description2.add("Do your homework. Check your seller's feedback, reviews and ratings on his/her profile. Make sure you are purchasing from a reputable seller to ensure quality products. If there are any discrepancies regarding your purchase, please click here for our Dispute Guidelines.");
        titles2.add("3. Read the information");
        images2.add(0);
        description2.add("Every seller will have his/her unique house rules. Read them thoroughly as this information will be displayed on the seller's profile (e.g. trades, price negotiations or any preferences). The seller would appreciate it if you read them beforehand to ensure a smooth and pleasant shopping experience.");
        titles2.add("4. Use ShopifyCAB's in-app tools");
        images2.add(0);
        description2.add("Keep track of items and sellers you’re interested in by marking them as favourites or adding them to your shopping cart. This helps you to compare prices and allows you to quickly contact the seller once you've made up your mind.\n" +
                "\n" +
                "You can also contact the seller directly through ShopifyCAB's \"Chat Now\" function to negotiate the price or request for more information. Please be aware that conversations made through the app will be recorded and handled in confidence by ShopifyCAB.\n");
        titles2.add("5. Check listing details");
        images2.add(0);
        description2.add("Before placing an order, check all listing information provided (e.g. delivery time, mailing address, contact numbers, etc). If necessary, don't hesitate to ask for more details to ensure a smooth transaction.");
        titles2.add("6. Check your item");
        images2.add(0);
        description2.add("Once you have received your product, do a thorough check. If your product is damaged or not as described, you can request for a return or refund within 24 hours. If you are meeting up with the seller personally, ensure the product is exactly as described before making the payment.");
        titles2.add("7. Share your experience");
        images2.add(0);
        description2.add("We are building a community here at ShopifyCAB so share your experience! Show appreciation for your seller and remember to rate or write a review for the product and/or seller after the deal has been made. Your feedback will help fellow buyers make a better choice.");

        setupRecyclerAdapter();
    }

    public void backTips(View view) {
        super.onBackPressed();
    }
    private void setupRecyclerAdapter() {//sets up the layout if recyclerView as  well as the adapter for Categories
        adapter = new SellTipAdapter(this,titles,images,description);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this,1,GridLayoutManager.VERTICAL,false);
        SellTipRecycler.setLayoutManager(gridLayoutManager);
        SellTipRecycler.setAdapter(adapter);

        buyTipAdapter = new BuyTipAdapter(this,titles2,images2,description2);
        GridLayoutManager gridLayoutManager2 = new GridLayoutManager(this,1,GridLayoutManager.VERTICAL,false);
        buyTipRecycler.setLayoutManager(gridLayoutManager2);
        buyTipRecycler.setAdapter(buyTipAdapter);
    }

    public void showSellTips(View view) {
        if (hideP1){
            mCons1.setVisibility(View.GONE);
            hideP1 = false;
        }else{
            mCons1.setVisibility(View.VISIBLE);
            hideP1 = true;
        }

    }
    public void showBuyTips(View view) {
        if (hideP2){
            mCons2.setVisibility(View.GONE);
            hideP2 = false;
        }else{
            mCons2.setVisibility(View.VISIBLE);
            hideP2 = true;
        }

    }
}