package com.clickawaybuying.shopify;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.clickawaybuying.shopify.Adapters.showChatAdapter;
import com.clickawaybuying.shopify.classes.Messages;
import com.clickawaybuying.shopify.utils.PreferenceUtils;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

public class Chat extends AppCompatActivity {
    //initialize variables
    RecyclerView.LayoutManager manager;
    RecyclerView recyclerView;
    List<Messages> chat = new ArrayList<>();
    public static RecyclerView.Adapter mAdapter;
    ConstraintLayout mConstraint;
    CardView imgLock;
    TextView mHistory;
    BottomNavigationView bot_nav;
    String user_name;
    private DatabaseReference dbr;
    ImageView mChatImg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        //assigning variables
        bot_nav = findViewById(R.id.buttom_navigation);
        mConstraint = findViewById(R.id.chatsLayout);
        mHistory = findViewById(R.id.noHistory);
        recyclerView = findViewById(R.id.showChatRecycler);
        imgLock = findViewById(R.id.chatLockImg);
        mChatImg = findViewById(R.id.no_chatImg);

        user_name = PreferenceUtils.getEmail(this);//get shared prefs username

        //showMessages();//method for showing messages in mysql
        botNav();

        if (user_name == null){//checks if username is null
            mConstraint.setVisibility(View.INVISIBLE);
            imgLock.setVisibility(View.VISIBLE);
            Toast.makeText(this,"Login first to use this feature",Toast.LENGTH_LONG).show();
        }
        else{//set up recyclerview layout manager
            user_name = user_name.replace(".", "");
            manager = new GridLayoutManager(Chat.this, 1);
            recyclerView.setLayoutManager(manager);
            dbr = FirebaseDatabase.getInstance().getReference(user_name);//.getRoot()
            getFirebaseMessages();
        }

    }

    private void getFirebaseMessages() {
        dbr.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                chat = new ArrayList<>();
                try {
                    Set<String> set = new HashSet<String>();
                    Iterator i = dataSnapshot.getChildren().iterator();

                    while(i.hasNext()){
                        String conversations = ((DataSnapshot)i.next()).getKey();
                        if (conversations != null){
                            mHistory.setVisibility(View.INVISIBLE);
                            mChatImg.setVisibility(View.INVISIBLE);
                        }
                        Messages messages = new Messages(conversations);
                        chat.add(messages);
                    }
                    mAdapter = new showChatAdapter(Chat.this,chat);
                    recyclerView.setAdapter(mAdapter);
                }catch (Exception e){
                    e.printStackTrace();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void botNav() {
        bot_nav.setVisibility(View.VISIBLE);
        //Set Chat Selected
        bot_nav.setSelectedItemId(R.id.chat);
        //perform item seleted listener
        bot_nav.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.chat:
                        return true;
                    case R.id.homeScreen:
                        startActivity(new Intent(getApplicationContext()
                                ,HomeScreen.class));
                        overridePendingTransition(0,0);finish();
                        return true;
                    case R.id.search_result:
                        startActivity(new Intent(getApplicationContext()
                                ,search_result.class));
                        overridePendingTransition(0,0);finish();
                        return true;
                    case R.id.wallet:
                        startActivity(new Intent(getApplicationContext()
                                , wallet.class));
                        overridePendingTransition(0,0);finish();
                        return true;
                    case R.id.profile:
                        startActivity(new Intent(getApplicationContext()
                                ,Profile.class));
                        overridePendingTransition(0,0);finish();
                        return true;
                }
                return false;
            }
        });
    }

    @Override
    public void onBackPressed() {//ask user if he press back button
        new AlertDialog.Builder(this)
                .setMessage("Are you sure you want to exit?")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        Chat.this.finish();
                    }
                })
                .setNegativeButton("No", null)
                .show();
    }
}