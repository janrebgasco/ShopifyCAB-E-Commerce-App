package com.clickawaybuying.shopify;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.util.Base64;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.clickawaybuying.shopify.Adapters.ChatRoomAdapter;
import com.clickawaybuying.shopify.classes.ImageReply;
import com.clickawaybuying.shopify.classes.Images;
import com.clickawaybuying.shopify.classes.Messages;
import com.clickawaybuying.shopify.classes.Product;
import com.clickawaybuying.shopify.utils.PreferenceUtils;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class ChatRoom extends AppCompatActivity {
    RecyclerView.LayoutManager manager;
    RecyclerView recyclerChat;
    List<Product> myMessage = new LinkedList<>();
    List<Messages> reply = new LinkedList<>();
    List<Images> image = new LinkedList<>();
    List<ImageReply> imageReply = new LinkedList<>();
    private RecyclerView.Adapter mAdapter;
    private TextView mSellerChat;
    String seller;
    EditText mChatBox;
    ImageButton mSend,mUploadImg,mCancel;
    ConstraintLayout mContainer;
    RequestQueue requestQueue;
    String UserName, sellerName, user_msg_key,imgLink;
    private DatabaseReference dbr,dbrReciever;
    final int CODE_GALLERY_REQUEST = 999;
    Bitmap bitmap1;
    ImageView mUploadedImg;


    public static final String MESSAGE_URL = "https://gascojanreb.000webhostapp.com/ShopifyCAB/getMessage.php";
    public static final String REPLY_URL = "https://gascojanreb.000webhostapp.com/ShopifyCAB/getReply.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_room);
        recyclerChat = findViewById(R.id.recyclerChat);
        mSellerChat = findViewById(R.id.sellernameTop);
        mChatBox = findViewById(R.id.ChatBox);
        mSend = findViewById(R.id.btnChatSend);
        mUploadImg = findViewById(R.id.chatSendImg);
        mUploadedImg = findViewById(R.id.uploadedImg);
        mContainer = findViewById(R.id.imgContainer);
        mCancel = findViewById(R.id.removeImg);

        UserName  = PreferenceUtils.getEmail(ChatRoom.this);
        UserName = UserName.replace(".", "");
        sellerName = getIntent().getExtras().get("seller").toString();
        mSellerChat.setText(sellerName);
        DatabaseReference child = FirebaseDatabase.getInstance().getReference(UserName);
        dbr = child.child(sellerName);

        DatabaseReference child2 = FirebaseDatabase.getInstance().getReference(sellerName);
        dbrReciever = child2.child(UserName);

        mSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!mChatBox.getText().toString().trim().equals("")  && bitmap1 == null){
                    Map<String, Object> map = new HashMap<String, Object>();
                    user_msg_key = dbr.push().getKey();
                    dbr.updateChildren(map);

                    DatabaseReference dbr2 = dbr.child(user_msg_key);
                    Map<String, Object> map2 = new HashMap<String, Object>();
                    map2.put("msg", mChatBox.getText().toString());
                    map2.put("user", UserName);
                    map2.put("img","null");
                    dbr2.updateChildren(map2);

                    DatabaseReference dbrReciever2 = dbrReciever.child(user_msg_key);
                    Map<String, Object> mapR = new HashMap<String, Object>();
                    mapR.put("msg", mChatBox.getText().toString());
                    mapR.put("user", UserName);
                    mapR.put("img","null");
                    dbrReciever2.updateChildren(mapR);

                    mChatBox.setText("");
                }
                if (!mChatBox.getText().toString().trim().equals("") && bitmap1 != null){
                    String image = imagetoString(bitmap1);
                    createImageLink(image,1);
                    mSend.setEnabled(false);
                }
                else if (bitmap1 != null){
                    String image = imagetoString(bitmap1);
                    createImageLink(image,2);
                    mSend.setEnabled(false);
                }


            }
        });
        mUploadImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ActivityCompat.requestPermissions(
                        ChatRoom.this,
                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                        CODE_GALLERY_REQUEST
                );
            }
        });
        mCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mUploadedImg.setImageResource(0);
                mContainer.setVisibility(View.GONE);
                bitmap1 = null;
            }
        });

        dbr.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                updateConversation(dataSnapshot);
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                updateConversation(dataSnapshot);
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        manager = new GridLayoutManager(ChatRoom.this, 1);
        recyclerChat.setLayoutManager(manager);

        reply = new ArrayList<>();
        myMessage = new ArrayList<>();

        recyclerChat.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                ((ChatRoomAdapter)recyclerChat.getAdapter()).condition();
            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                ((ChatRoomAdapter)recyclerChat.getAdapter()).condition();
            }
        });

    }
    public void updateConversation(DataSnapshot dataSnapshot){
        hideKeyboard(this);
        String msg = "", user = "", conversation,img;
        Iterator i = dataSnapshot.getChildren().iterator();
        while(i.hasNext()){
            img = (String) ((DataSnapshot)i.next()).getValue();
            msg = (String) ((DataSnapshot)i.next()).getValue();
            user = (String) ((DataSnapshot)i.next()).getValue();

            String imgs = img.replaceAll("\\\\","").replace("\"","");
            conversation = user + ": " + msg;
            if (user.equals(UserName)){
                Product product = new Product(msg);
                myMessage.add(product);
                Messages messages = new Messages("null");
                reply.add(messages);
                Images images = new Images(imgs);
                image.add(images);
                ImageReply nullImgReply = new ImageReply("null");
                imageReply.add(nullImgReply);
            }
            else{
                Messages messages = new Messages(msg);
                reply.add(messages);
                Product product = new Product("null");
                myMessage.add(product);
                ImageReply images = new ImageReply(imgs);
                imageReply.add(images);
                Images nullImages = new Images("null");
                image.add(nullImages);
            }

            mAdapter = new ChatRoomAdapter(ChatRoom.this,myMessage,reply,image,imageReply);
            recyclerChat.setAdapter(mAdapter);
            try{
                mAdapter.notifyDataSetChanged();
            }catch (Exception e){
                e.printStackTrace();
            }
            recyclerChat.scrollToPosition(reply.size()-1);
        }
    }

    public void chatBackBtn(View v){
        super.onBackPressed();
    }
    public static void hideKeyboard(ChatRoom activity) {
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(ChatRoom.INPUT_METHOD_SERVICE);
        //Find the currently focused view, so we can grab the correct window token from it.
        View view = activity.getCurrentFocus();
        //If no view currently has focus, create a new one, just so we can grab a window token from it
        if (view == null) {
            view = new View(activity);
        }
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }
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
        if (requestCode == CODE_GALLERY_REQUEST && resultCode == RESULT_OK && data != null)
        {
            Uri filepath = data.getData();

            try {
                InputStream inputStream = getContentResolver().openInputStream(filepath);
                bitmap1 = BitmapFactory.decodeStream(inputStream);
                mUploadedImg.setImageBitmap(bitmap1);
                if (mUploadedImg.getDrawable() != null){
                    mContainer.setVisibility(View.VISIBLE);
                }
                else{
                    mContainer.setVisibility(View.GONE);
                }
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }

        super.onActivityResult(requestCode, resultCode, data);
    }
    private String imagetoString(Bitmap bitmap){
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream);
        byte[] imageBytes = outputStream.toByteArray();

        String encodedImage = Base64.encodeToString(imageBytes, Base64.DEFAULT);
        return encodedImage;
    }
    private void createImageLink(final String image,final int num){
        String URL = "https://gascojanreb.000webhostapp.com/ShopifyCAB/createImageLink.php";
        requestQueue = Volley.newRequestQueue(getApplicationContext());//manages worker threads for running the network operations
        StringRequest jsonObjectRequest = new StringRequest(Request.Method.POST,
                URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {//gets the php json responses
                //Toast.makeText(ChatRoom.this, ""+response, Toast.LENGTH_SHORT).show();
                if (num == 1){
                    condition1(response);
                }
                if (num == 2){
                    condition2(response);
                }

            }
        }, new Response.ErrorListener() {//gets php error messages
            @Override
            public void onErrorResponse(VolleyError error) {
                try {
                    if (error.getMessage().length()>0){
                        //Toast.makeText(ChatRoom.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }

            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> parameters = new HashMap<String, String>();//putting parameters to pass on the php file

                parameters.put("image", image);
                return parameters;
            }
        };
        requestQueue.add(jsonObjectRequest);
    }
    private void condition1(String img){
        String img1 = img.replace("\\", "");
        String image = img1.replaceAll("\\\\", "");

        Map<String, Object> map = new HashMap<String, Object>();
        user_msg_key = dbr.push().getKey();
        dbr.updateChildren(map);

        DatabaseReference dbr2 = dbr.child(user_msg_key);
        Map<String, Object> map2 = new HashMap<String, Object>();
        map2.put("msg", mChatBox.getText().toString());
        map2.put("user", UserName);
        map2.put("img",image);
        dbr2.updateChildren(map2);

        DatabaseReference dbrReciever2 = dbrReciever.child(user_msg_key);
        Map<String, Object> mapR = new HashMap<String, Object>();
        mapR.put("msg", mChatBox.getText().toString());
        mapR.put("user", UserName);
        mapR.put("img",image);
        dbrReciever2.updateChildren(mapR);

        mChatBox.setText("");

        mUploadedImg.setImageResource(0);
        mContainer.setVisibility(View.GONE);
        bitmap1 = null;
        mSend.setEnabled(true);
    }
    private void condition2(String img){
        String img1 = img.replace("\\", "");
        String image = img1.replaceAll("\\\\", "");

        Map<String, Object> map = new HashMap<String, Object>();
        user_msg_key = dbr.push().getKey();
        dbr.updateChildren(map);

        DatabaseReference dbr2 = dbr.child(user_msg_key);
        Map<String, Object> map2 = new HashMap<String, Object>();
        map2.put("msg", "null");
        map2.put("user", UserName);
        map2.put("img",image);
        dbr2.updateChildren(map2);

        DatabaseReference dbrReciever2 = dbrReciever.child(user_msg_key);
        Map<String, Object> mapR = new HashMap<String, Object>();
        mapR.put("msg", "null");
        mapR.put("user", UserName);
        mapR.put("img",image);
        dbrReciever2.updateChildren(mapR);

        mChatBox.setText("");
        mUploadedImg.setImageResource(0);
        mContainer.setVisibility(View.GONE);
        bitmap1 = null;
        mSend.setEnabled(true);
    }

}