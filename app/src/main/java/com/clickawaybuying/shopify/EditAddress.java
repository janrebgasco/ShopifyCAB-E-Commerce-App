package com.clickawaybuying.shopify;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class EditAddress extends AppCompatActivity {
    public static EditText editAddressed;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_address);
        editAddressed = findViewById(R.id.editAddressed);


    }
    public void addressBack(View view){
        super.onBackPressed();
    }
    public void insertAdd(View view){
        String edits= editAddressed.getText().toString();
        CheckOut.adress.setText(edits);
        Toast.makeText(EditAddress.this, "Address sucessfully inserted", Toast.LENGTH_SHORT).show();
    }
}