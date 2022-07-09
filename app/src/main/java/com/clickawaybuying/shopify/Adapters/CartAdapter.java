package com.clickawaybuying.shopify.Adapters;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.clickawaybuying.shopify.Cart;
import com.clickawaybuying.shopify.classes.Product;
import com.clickawaybuying.shopify.Purchase;
import com.clickawaybuying.shopify.R;
import com.clickawaybuying.shopify.utils.PreferenceUtils;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.ViewHolder> {
    private RecyclerView.RecycledViewPool viewPool = new RecyclerView.RecycledViewPool();
    private RecyclerView.RecycledViewPool viewPool2 = new RecyclerView.RecycledViewPool();
    private Context mContext;
    public List<Product> products = new ArrayList<>();
    public List<LinkedList<String>> parentColors = new ArrayList<>();
    public List<LinkedList<String>> parentSizes = new ArrayList<>();
    public List<LinkedList<String>> color_indexes = new ArrayList<>();
    public List<LinkedList<String>> size_indexes = new ArrayList<>();
    public List<LinkedList<String>> color_quan = new ArrayList<>();
    public List<LinkedList<String>> size_quan = new ArrayList<>();
    public List<String> sizes = new ArrayList<>();
    public static List<String> selectedProduct = new ArrayList<>();
    public static List<String> selectedImage = new ArrayList<>();
    public static List<Integer> selectedPrice = new ArrayList<>();
    public static List<Integer> selectedQuan = new ArrayList<>();
    public static List<Integer> selectedID = new ArrayList<>();
    private int totalPrice,Price,shipFee;
    public static String prodname;
    Boolean isShown = false;

    public CartAdapter (Context context, List<Product> products, List<LinkedList<String>> color, List<LinkedList<String>> size, List<LinkedList<String>> color_indexes, List<LinkedList<String>> size_indexes, List<LinkedList<String>> color_quan, List<LinkedList<String>> size_quan){
        this.mContext = context;
        this.products = products;
        this.parentColors = color;
        this.parentSizes = size;
        this.color_indexes = color_indexes;
        this.size_indexes = size_indexes;
        this.color_quan = color_quan;
        this.size_quan = size_quan;
    }


    public class ViewHolder extends RecyclerView.ViewHolder {

        private TextView mProdname, mPrice,mQuantity,colorText,sizeText;
        private ImageView mImageView;
        private ConstraintLayout mContainer;
        private Button mBtnAdd,mBtnMinus;
        ImageButton mDelete;
        CardView mCard,mCardVari;
        private int quantity=1;
        CheckBox mCheckBox;
        ImageButton ddownBtn;
        CardView mVariations;
        RecyclerView colorRecycler,sizeRecycler,sizeRecycler2;


        public ViewHolder (View view){
            super(view);
            mProdname = itemView.findViewById(R.id.cartProdName);
            mPrice = view.findViewById(R.id.cartPrice);
            mImageView = view.findViewById(R.id.cartImage);
            mContainer = view.findViewById(R.id.cartContainer);
            mBtnAdd = view.findViewById(R.id.btnIncrease);
            mBtnMinus = view.findViewById(R.id.btnDecrease);
            mQuantity = view.findViewById(R.id.prodQuantity);
            mDelete = view.findViewById(R.id.btnDelete);
            mCard = view.findViewById(R.id.cartCard);
            mCheckBox = view.findViewById(R.id.checkBoxCart);
            ddownBtn = view.findViewById(R.id.variShowBtn);
            mVariations = view.findViewById(R.id.cart_vari);
            mCardVari = view.findViewById(R.id.variCrd);
            colorRecycler = view.findViewById(R.id.cartColorRecycler);
            sizeRecycler = view.findViewById(R.id.cartSizeRecycler);
            sizeRecycler2 = view.findViewById(R.id.cartSizeRecycler2);
            colorText = view.findViewById(R.id.cartColorText);
            sizeText = view.findViewById(R.id.cartColorText);
        }
    }


    @NonNull
    @Override
    public CartAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(mContext).inflate(R.layout.cart_list_item,parent,false);
        return new CartAdapter.ViewHolder(view);


    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull final CartAdapter.ViewHolder holder, final int position) {

        final Product product = products.get(position);
        Price = product.getPrice();
        DecimalFormat formatter = new DecimalFormat("#,###");//format for thousand
        String finalPrice = formatter.format(Price);
        holder.mPrice.setText("₱"+finalPrice);
        holder.mProdname.setText(product.getTitle());
        Glide.with(mContext).load(product.getImage()).into(holder.mImageView);
        holder.mQuantity.setText(String.valueOf(product.getQuantity1()));

        setupColorRecycler(holder,position);
        ((Cart) mContext).setupParentColorArray(position);
        setupSizeRecycler(holder,position);

        //Toast.makeText(mContext, ""+color_indexes.get(position).toString(), Toast.LENGTH_SHORT).show();
        if (color_indexes.isEmpty() || color_indexes.get(position).toString().trim().equals("[null]")){
            holder.colorText.setVisibility(View.GONE);
            //Toast.makeText(mContext, "NAG TRU", Toast.LENGTH_SHORT).show();
        }
        if (size_indexes.isEmpty() || size_indexes.get(position).toString().equals("[null]")){
            holder.sizeText.setVisibility(View.GONE);
        }

        totalPrice=0;
        shipFee = 40*products.size();
        for (int i = 0; i<products.size(); i++)
        {
            totalPrice += products.get(i).getPrice() * products.get(i).getQuantity1();

        }
        String quan = holder.mQuantity.getText().toString();
        holder.quantity = Integer.parseInt(quan);
        if(holder.quantity == 1)
        {
            holder.mBtnMinus.setEnabled(false);
        }

        //on activity load
        if(holder.mCheckBox.isChecked()) {
            selectedProduct.add(product.getTitle().replace("[", "").replace("]", ""));
            selectedPrice.add(product.getPrice());
            selectedImage.add(product.getImage().replace("[", "").replace("]", ""));
            selectedQuan.add(position,holder.quantity);
            selectedID.add(product.getId());
        }
        else{
            selectedProduct.remove(product.getTitle());
            selectedPrice.set(position,0);
            selectedImage.remove(product.getImage());
            selectedQuan.set(position,0);
            selectedID.set(position,0);
        }


        holder.mCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(holder.mCheckBox.isChecked()){
                    //btnMinus would be enabled if not equal to 1
                    holder.mBtnMinus.setEnabled(holder.quantity != 1);
                    holder.mBtnAdd.setEnabled(true);

                    int posPrice = product.getPrice();
                    int itemPrice = posPrice * holder.quantity;
                    totalPrice = totalPrice + itemPrice;

                    ((Cart) mContext).dispTotal(totalPrice,shipFee);

                    selectedProduct.add(product.getTitle().replace("[", "").replace("]", ""));
                    selectedPrice.add(product.getPrice());
                    selectedImage.add(product.getImage().replace("[", "").replace("]", ""));
                    selectedQuan.add(position,holder.quantity);
                    selectedID.add(product.getId());
                }
                //checkbox is not checked
                else{
                    int posPrice = product.getPrice();
                    int itemPrice = posPrice * holder.quantity;
                    totalPrice = totalPrice - itemPrice;

                    ((Cart) mContext).dispTotal(totalPrice,shipFee);
                    holder.mBtnAdd.setEnabled(false);
                    holder.mBtnMinus.setEnabled(false);

                    selectedProduct.remove(product.getTitle());
                    selectedPrice.set(position,0);
                    selectedImage.remove(product.getImage());
                    //replace with 0
                    selectedQuan.set(position,0);
                    selectedID.set(position,0);

                }
            }
        });

        ((Cart) mContext).dispTotal(totalPrice,shipFee);

        //quantity add button
        holder.mBtnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                holder.quantity++;
                holder.mQuantity.setText(String.valueOf(holder.quantity));
                if(holder.mCheckBox.isChecked()) {
                    selectedQuan.set(position,holder.quantity);
                    int posPrice = product.getPrice();
                    totalPrice = totalPrice + posPrice;
                    holder.mBtnMinus.setEnabled(true);
                    ((Cart) mContext).dispTotal(totalPrice, shipFee);
                }
            }
        });
        //quantity minus button
        holder.mBtnMinus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                holder.quantity--;
                if(holder.mCheckBox.isChecked()) {
                if(holder.quantity==1)
                {
                    holder.mQuantity.setText(String.valueOf(holder.quantity));
                    selectedQuan.set(position,holder.quantity);
                    int posPrice = product.getPrice();
                    totalPrice = totalPrice - posPrice;
                    ((Cart) mContext).dispTotal(totalPrice, shipFee);
                    holder.mBtnMinus.setEnabled(false);
                }
                else {
                    holder.mQuantity.setText(String.valueOf(holder.quantity));
                    selectedQuan.set(position,holder.quantity);
                    int posPrice = product.getPrice();
                    totalPrice = totalPrice - posPrice;
                    ((Cart) mContext).dispTotal(totalPrice, shipFee);
                    }
                }
            }
        });
        holder.mDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                new AlertDialog.Builder(mContext)
                        .setMessage("Are you sure you want remove item to cart?")
                        .setCancelable(false)
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {

                                RequestQueue requestQueue = Volley.newRequestQueue(mContext.getApplicationContext());
                                final String user_name  = PreferenceUtils.getEmail(mContext.getApplicationContext());

                                String insertURL = "https://gascojanreb.000webhostapp.com/ShopifyCAB/deleteCartItem.php";
                                StringRequest request = new StringRequest(Request.Method.POST, insertURL, new Response.Listener<String>() {//canned request for retrieving the response body at a given URL as a String
                                    @Override
                                    public void onResponse(String response) {//gets the php json responses

                                    }
                                }, new Response.ErrorListener() {//gets php error messages
                                    @Override
                                    public void onErrorResponse(VolleyError error) {

                                    }
                                }){
                                    @Override
                                    protected Map<String, String> getParams() throws AuthFailureError {
                                        Map<String,String> parameters = new HashMap<String, String>();//putting parameters to pass on the php file

                                        parameters.put("product_name",product.getTitle());
                                        parameters.put("cart_of",user_name);

                                        return parameters;
                                    }
                                };
                                requestQueue.add(request);
                                ((Cart) mContext).layoutRefresher();
                            }
                        })
                        .setNegativeButton("No", null)
                        .show();

            }
        });
        holder.mContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(mContext, Purchase.class);
                intent.putExtra("id",product.getId());
                intent.putExtra("title",product.getTitle());
                intent.putExtra("image",product.getImage());
                intent.putExtra("rate",product.getRating());
                intent.putExtra("price",product.getPrice());
                intent.putExtra("description",product.getDescription());
                intent.putExtra("brand",product.getBrand());
                intent.putExtra("stocks",product.getStocks());
                intent.putExtra("sold",product.getSold());
                intent.putExtra("seller", product.getSeller());
                intent.putExtra("color",product.getColor());
                intent.putExtra("size",product.getSize());

                mContext.startActivity(intent);

            }
        });
        holder.ddownBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isShown){
                    holder.ddownBtn.setRotation(-90);
                    holder.mVariations.setVisibility(View.GONE);
                    isShown = false;
                }
                else{
                    holder.ddownBtn.setRotation(90);
                    holder.mVariations.setVisibility(View.VISIBLE);
                    isShown = true;
                }
            }
        });
        holder.mCardVari.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isShown){
                    holder.ddownBtn.setRotation(-90);
                    holder.mVariations.setVisibility(View.GONE);
                    isShown = false;
                }
                else{
                    holder.ddownBtn.setRotation(90);
                    holder.mVariations.setVisibility(View.VISIBLE);
                    isShown = true;
                }
            }
        });
    }


    @Override
    public int getItemCount() {
        return products.size();
    }

    private void setupColorRecycler(final CartAdapter.ViewHolder holder,final int position){
        // Create layout manager with initial prefetch item count
        GridLayoutManager layoutManager = new GridLayoutManager(
                holder.colorRecycler.getContext(),3,
                LinearLayoutManager.VERTICAL,
                false
        );
        layoutManager.setInitialPrefetchItemCount(parentColors.get(position).size());

        // Create sub item view adapter
        CartColorAdapter subItemAdapter = new CartColorAdapter(mContext,parentColors.get(position),color_indexes.get(position),color_quan.get(position));

        holder.colorRecycler.setLayoutManager(layoutManager);
        holder.colorRecycler.setAdapter(subItemAdapter);
        holder.colorRecycler.setRecycledViewPool(viewPool);

    }
    private void setupSizeRecycler(final CartAdapter.ViewHolder holder,final int position){

        // Create layout manager with initial prefetch item count
        GridLayoutManager layoutManager = new GridLayoutManager(
                holder.sizeRecycler2.getContext(),1,
                LinearLayoutManager.VERTICAL,
                false
        );

        // Create sub item view adapter
        CartSizeAdapter subItemAdapter = new CartSizeAdapter(mContext,position,size_indexes.get(position),size_quan.get(position),parentColors.get(position));

        holder.sizeRecycler2.setLayoutManager(layoutManager);
        holder.sizeRecycler2.setAdapter(subItemAdapter);
        holder.sizeRecycler2.setRecycledViewPool(viewPool2);
        //Toast.makeText(mContext, ""+parentSizes.get(position), Toast.LENGTH_SHORT).show();
    }
}
