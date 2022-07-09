package com.clickawaybuying.shopify.classes;

import android.app.Activity;
import android.content.Context;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.clickawaybuying.shopify.R;

public class AddressData {
    public static void BinondoBarangay(){
        String[] strings = {};
        for(int i =187;i <= 296;i++){
           strings = new String[]{"Barangay " + i};
        }
        barangayAdapter(strings);
    }
    private static void barangayAdapter(String[] arraySpinner) {
        Context context = null;
        Spinner s = ((Activity)context).findViewById(R.id.addBarangay);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(context,
                android.R.layout.simple_spinner_item, arraySpinner);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        s.setAdapter(adapter);
    }

}
