package com.clickawaybuying.shopify.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.clickawaybuying.shopify.Cart;
import com.clickawaybuying.shopify.R;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class CartSizeAdapter extends RecyclerView.Adapter<CartSizeAdapter.MyViewHolder> {
    private RecyclerView.RecycledViewPool viewPool = new RecyclerView.RecycledViewPool();
    private final Context mContext;
    List<String>selectedColors;
    List<String>size;
    int pos;
    List<String>size_indexes;
    List<String>size_quan;




    public CartSizeAdapter(Context context,int position, List<String> size_indexes, List<String> size_quan, List<String> Colors){
        this.mContext = context;
        this.pos = position;
        //this.selectedColors = new LinkedList<>(Cart.parentSelectedColors.get(position));
        //Toast.makeText(context, "toSting::"+Cart.parentSizes.get(position).toString(), Toast.LENGTH_SHORT).show();
        if (!Cart.parentSizes.get(position).toString().trim().equals("[]")){
            this.selectedColors = new LinkedList<>(Colors);
        }
        this.size = new LinkedList<>(Cart.parentSizes.get(position));
        this.size_indexes = size_indexes;
        this.size_quan = size_quan;
        //Toast.makeText(context, ""+size_indexes, Toast.LENGTH_SHORT).show();
    }


    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView mTxt;
        RecyclerView recyclerView;
        ConstraintLayout container;

        public MyViewHolder (View view){
            super(view);
            mTxt = view.findViewById(R.id.textView70);
            recyclerView = view.findViewById(R.id.recyclerInside);
            container = view.findViewById(R.id.itemcons);

        }
    }


    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.size_list_item2,parent,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, final int position) {

        holder.mTxt.setText(selectedColors.get(position));
        //Toast.makeText(mContext, "itemCount::::"+getItemCount()+pos, Toast.LENGTH_SHORT).show();

        if (holder.mTxt.getText().toString().equals("null")){
            //holder.container.setVisibility(View.GONE);
            //holder.recyclerView.setVisibility(View.GONE);
            //holder.mTxt.setVisibility(View.GONE);
        }
        else{
            holder.container.setVisibility(View.VISIBLE);
            holder.recyclerView.setVisibility(View.VISIBLE);
            holder.mTxt.setVisibility(View.VISIBLE);
        }

        // Create layout manager with initial prefetch item count
        GridLayoutManager layoutManager = new GridLayoutManager(
                holder.recyclerView.getContext(),1,
                LinearLayoutManager.HORIZONTAL,
                false
        );
        layoutManager.setInitialPrefetchItemCount(size.size());

        // Create sub item view adapter
        CartNestedSizeAdapter subItemAdapter = new CartNestedSizeAdapter(mContext,size,position,holder.mTxt.getText().toString(),size_indexes,size_quan);
        //Toast.makeText(mContext, "CSAdapter:"+size_indexes, Toast.LENGTH_SHORT).show();

        holder.recyclerView.setLayoutManager(layoutManager);
        holder.recyclerView.setAdapter(subItemAdapter);
        holder.recyclerView.setRecycledViewPool(viewPool);

    }

    @Override
    public int getItemCount() {
        return selectedColors.size();
    }

}
