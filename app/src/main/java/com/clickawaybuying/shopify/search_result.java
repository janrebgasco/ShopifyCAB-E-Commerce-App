package com.clickawaybuying.shopify;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.MenuItemCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.app.SearchManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.provider.SearchRecentSuggestions;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.CursorAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SearchView;
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
import com.clickawaybuying.shopify.Adapters.ResultAdapter;
import com.clickawaybuying.shopify.classes.Product;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class search_result extends AppCompatActivity {
    RecyclerView recyclerView;
    List<Product> products;
    private RecyclerView.Adapter mAdapter;
    RecyclerView.LayoutManager manager;
    ListView lv;
    ArrayAdapter adapter;
    ArrayList<String>list1;
    public SearchView sv;
    private String newText;
    TextView mTextResult;
    AutoCompleteTextView text;
    CursorAdapter cursorAdapter;
    Spinner mSortPrice;
    ImageView mSearchImg;
    SearchRecentSuggestions suggestions;
    public static final String SEARCH_URL = "https://gascojanreb.000webhostapp.com/ShopifyCAB/getSearchResult.php";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_result);
        lv=findViewById(R.id.searchRecycler);
        sv=findViewById(R.id.searchResult);
        recyclerView = findViewById(R.id.resultRecycler);
        mTextResult = findViewById(R.id.searchResTxt);
        mSortPrice = findViewById(R.id.sortSpinner);
        mSearchImg = findViewById(R.id.no_searchImg);
        suggestions = new SearchRecentSuggestions(this,
                MySuggestionProvider.AUTHORITY, MySuggestionProvider.MODE);

        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        if (searchManager != null) {
            sv.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        }
        sv.setOnSuggestionListener(new SearchView.OnSuggestionListener() {
            @Override
            public boolean onSuggestionSelect(int position) {
                return true;
            }

            @Override
            public boolean onSuggestionClick(int position) {
                CursorAdapter selectedView = sv.getSuggestionsAdapter();
                Cursor cursor = (Cursor) selectedView.getItem(position);
                int index = cursor.getColumnIndexOrThrow(SearchManager.SUGGEST_COLUMN_TEXT_1);
                sv.setQuery(cursor.getString(index), true);
                return true;
            }
        });


        Intent intent = getIntent();
        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            String query = intent.getStringExtra(SearchManager.QUERY);
            SearchRecentSuggestions suggestions = new SearchRecentSuggestions(search_result.this,
                    MySuggestionProvider.AUTHORITY, MySuggestionProvider.MODE);
            suggestions.saveRecentQuery(query, null);
        }


        list1= new ArrayList<String>();

        sv.setIconified(false);

        adapter=new ArrayAdapter(this, android.R.layout.simple_list_item_1,list1);
        lv.setAdapter(adapter);

        bottomNavigation();
        mTextResult.setVisibility(View.INVISIBLE);
        mSearchImg.setVisibility(View.INVISIBLE);

        manager = new GridLayoutManager(search_result.this, 2);
        recyclerView.setLayoutManager(manager);
        products = new ArrayList<>();
        sv.setSuggestionsAdapter(cursorAdapter);
        sv.setOnCloseListener(new SearchView.OnCloseListener() {
            @Override
            public boolean onClose() {
                Intent i = new Intent(search_result.this,HomeScreen.class);
                startActivity(i);
                return false;
            }
        });
        sv.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(final String searchText) {
                SearchRecentSuggestions suggestions = new SearchRecentSuggestions(search_result.this,
                        MySuggestionProvider.AUTHORITY, MySuggestionProvider.MODE);
                suggestions.saveRecentQuery(searchText, null);

                products.clear();
                products = new ArrayList<>();

                final ProgressDialog progress = new ProgressDialog(search_result.this);
                progress.setTitle("Search in progress");
                progress.setMessage("Please wait...");
                progress.setCancelable(false); // disable dismiss by tapping outside of the dialog
                progress.show();
                // To dismiss the dialog
                new CountDownTimer(1000, 300) {
                    public void onTick(long millisUntilFinished) {

                    }
                    public void onFinish() {
                        dispSearch(searchText);
                        progress.dismiss();
                    }
                }.start();

                return false;
            }

            @Override
            public boolean onQueryTextChange(String Text) {
                newText=Text;
                return false;

            }
        });
        mSortPrice.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (mSortPrice.getSelectedItemPosition() == 1){
                    Collections.sort(products, new Comparator<Product>() {//lowest to highest price
                        @RequiresApi(api = Build.VERSION_CODES.KITKAT)
                        @Override
                        public int compare(Product product, Product t1) {
                            return Integer.compare(product.getPrice(),t1.getPrice());
                        }

                    });
                    mAdapter.notifyDataSetChanged();
                }
                if (mSortPrice.getSelectedItemPosition() == 2){
                    Collections.sort(products, new Comparator<Product>() {//highest to lowest price
                        @RequiresApi(api = Build.VERSION_CODES.KITKAT)
                        @Override
                        public int compare(Product product, Product t1) {
                            return Integer.compare(t1.getPrice(), product.getPrice());
                        }

                    });
                    mAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

    }

    @Override
    public void onBackPressed() {
        new AlertDialog.Builder(this)
                .setMessage("Are you sure you want to exit?")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        search_result.this.finish();
                    }
                })
                .setNegativeButton("No", null)
                .show();
    }


    public void dispSearch(final String searchRes){
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        StringRequest jsonObjectRequest = new StringRequest(Request.Method.DEPRECATED_GET_OR_POST,
                SEARCH_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {//gets the php json responses
                try {
                    JSONArray array = new JSONArray(response);

                    for (int i = 0; i<array.length();i++){
                        JSONObject productobject = array.getJSONObject(i);
                        int id = productobject.getInt("id");
                        String product_name = productobject.getString("product_name");
                        int price = productobject.getInt("price");
                        String image = productobject.getString("image");
                        double rating = productobject.getDouble("rating");
                        String description = productobject.getString("description");
                        String brand = productobject.getString("brand");
                        int stocks = productobject.getInt("stocks");
                        int sold = productobject.getInt("sold");
                        String seller = productobject.getString("seller");
                        String color = productobject.getString("color");
                        String size = productobject.getString("size");
                        String sellerImage = productobject.getString("sellerImage");

                        Product product = new Product(product_name,price, rating,image,description,brand,stocks,sold,id,seller,color,size,sellerImage);
                        products.add(product);
                    }
                    mAdapter = new ResultAdapter(search_result.this,products);
                    recyclerView.setAdapter(mAdapter);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                if (response.equals("failed")){
                    mTextResult.setText("No search result for "+"\""+searchRes+"\"");
                    mTextResult.setVisibility(View.VISIBLE);
                    mSearchImg.setVisibility(View.VISIBLE);
                    mSortPrice.setVisibility(View.INVISIBLE);
                }
                else{
                    mTextResult.setVisibility(View.INVISIBLE);
                    mSearchImg.setVisibility(View.INVISIBLE);
                    mSortPrice.setVisibility(View.VISIBLE);
                    mSortPrice.setSelection(0);
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
                parameters.put("searchtext",newText);

                return parameters;
            }
        };
        requestQueue.add(jsonObjectRequest);

    }

    public void bottomNavigation(){

        BottomNavigationView bot_nav = findViewById(R.id.buttom_navigation);

        //Set Home Selected
        bot_nav.setSelectedItemId(R.id.search_result);
        //perform item seleted listener
        bot_nav.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.chat:
                        startActivity(new Intent(getApplicationContext()
                                ,Chat.class));
                        overridePendingTransition(0,0);finish();
                        return true;
                    case R.id.homeScreen:
                        startActivity(new Intent(getApplicationContext()
                                ,HomeScreen.class));
                        overridePendingTransition(0,0);finish();
                        return true;
                    case R.id.searchResult:
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
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu,menu);

        SearchManager searchManager=(SearchManager)getSystemService(Context.SEARCH_SERVICE);

        SearchView searchView;

        MenuItem menuItem=menu.findItem(R.id.action_search);

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB)
        {
            searchView=(SearchView)menuItem.getActionView();

        }
        else
        {
            searchView=(SearchView) MenuItemCompat.getActionView(menuItem);

        }

        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        searchView.setQueryHint(getResources().getString(R.string.search_hint));

        return super.onCreateOptionsMenu(menu);
        }
    @Override
    protected void onNewIntent(Intent intent) {
        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            handledearch(intent);
            super.onNewIntent(intent);
        }
    }
    public void handledearch( Intent intent)
    {
        if (Intent.ACTION_SEARCH.equalsIgnoreCase(intent.getAction())) {
            String query = intent.getStringExtra(SearchManager.QUERY);


            SearchRecentSuggestions searchRecentSuggestions=new SearchRecentSuggestions(this,
                    MySuggestionProvider.AUTHORITY,MySuggestionProvider.MODE);
            Toast.makeText(this, query, Toast.LENGTH_SHORT).show();
            searchRecentSuggestions.saveRecentQuery(query,null);
        }
    }

}