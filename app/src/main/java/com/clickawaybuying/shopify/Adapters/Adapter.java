package com.clickawaybuying.shopify.Adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.clickawaybuying.shopify.AddAddress;
import com.clickawaybuying.shopify.CategContainer;
import com.clickawaybuying.shopify.ChooseAddress;
import com.clickawaybuying.shopify.R;
import com.clickawaybuying.shopify.utils.PreferenceUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Adapter extends RecyclerView.Adapter<Adapter.ViewHolder> {
    private Context mContext;
    List<String> titles;
    List<Integer> images;
    LayoutInflater inflater;
     ConstraintLayout containCateg;
     public static String categType="";

    public Adapter(Context ctx, List<String> titles, List<Integer> images){
        this.mContext = ctx;
        this.titles=titles;
        this.images=images;
        this.inflater = LayoutInflater.from(ctx);
    }

    @Override
    public int getItemCount() {
        return titles.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView Price;
        ImageView GridIcon;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            Price = itemView.findViewById(R.id.txtImg);
            GridIcon = itemView.findViewById(R.id.prodImg);
            containCateg = itemView.findViewById(R.id.categ_container);

            containCateg.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View view) {
                        Intent i = new Intent(mContext, CategContainer.class);
                        i.putExtra("categoryType",Price.getText().toString());
                        mContext.startActivity(i);
                }
            });
        }
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.custom_grid_layout,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.Price.setText(titles.get(position));
        holder.GridIcon.setImageResource(images.get(position));
        categType = titles.get(position);

    }




}
