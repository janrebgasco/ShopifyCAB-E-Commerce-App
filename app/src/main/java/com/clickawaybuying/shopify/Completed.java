package com.clickawaybuying.shopify;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.clickawaybuying.shopify.Adapters.CompletedAdapter;
import com.clickawaybuying.shopify.classes.Product;
import com.clickawaybuying.shopify.utils.PreferenceUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Completed#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Completed extends Fragment {
    List<Product> products;
    private RecyclerView.Adapter mAdapter;
    RecyclerView.LayoutManager manager;
    RecyclerView recyclerCompleted;
    ImageView imageView;
    TextView textView;

    public static final String PRODUCT_URL = "https://gascojanreb.000webhostapp.com/ShopifyCAB/getCompletedOrders.php";
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public Completed() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Completed.
     */
    // TODO: Rename and change types and number of parameters
    public static Completed newInstance(String param1, String param2) {
        Completed fragment = new Completed();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_completed, container, false);
        recyclerCompleted = view.findViewById(R.id.recyclerCompleted);
        imageView = view.findViewById(R.id.noOrderImg3);
        textView = view.findViewById(R.id.noOrderTxt3);

        loadProducts();
        manager = new GridLayoutManager(getActivity(), 1);
        recyclerCompleted.setLayoutManager(manager);
        products = new ArrayList<>();

        return view;
    }
    public void loadProducts(){

        StringRequest stringRequest = new StringRequest(Request.Method.POST, PRODUCT_URL,
                new Response.Listener<String>()
                {
                    @Override
                    public void onResponse(String response)
                    {
                        try {
                            JSONArray array = new JSONArray(response);

                            for (int i = 0; i<array.length();i++){
                                JSONObject productobject = array.getJSONObject(i);
                                int id = productobject.getInt("id");
                                String product_name = productobject.getString("product_name");
                                int price = productobject.getInt("price");
                                String image = productobject.getString("image");
                                int quantity = productobject.getInt("quantity");
                                int total = productobject.getInt("total");
                                String date = productobject.getString("date");
                                String color = productobject.getString("color");
                                String size = productobject.getString("size");
                                String address = productobject.getString("deliveryInfo");
                                int prodID = productobject.getInt("product_id");

                                if (!(productobject.length() <0)){
                                    imageView.setVisibility(View.INVISIBLE);
                                    textView.setVisibility(View.INVISIBLE);
                                }
                                Product product = new Product(product_name,price, quantity,image,total,id,date,color,size,address,prodID,"null","null");
                                products.add(product);
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        mAdapter = new CompletedAdapter(getActivity(),products);
                        recyclerCompleted.setAdapter(mAdapter);
                    }
                }, new Response.ErrorListener()

        {
            @Override
            public void onErrorResponse(VolleyError error)
            {
                Toast.makeText(getActivity(), error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> parameters = new HashMap<String, String>();//putting parameters to pass on the php file

                //get Shared Preference username
                String user_name  = PreferenceUtils.getEmail(getActivity().getApplicationContext());
                parameters.put("username",user_name);

                return parameters;
            }
        };
        Volley.newRequestQueue(getActivity().getApplicationContext()).add(stringRequest);
    }
}