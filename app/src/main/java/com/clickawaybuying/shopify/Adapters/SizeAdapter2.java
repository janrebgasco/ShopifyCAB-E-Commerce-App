package com.clickawaybuying.shopify.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.clickawaybuying.shopify.Purchase;
import com.clickawaybuying.shopify.R;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class SizeAdapter2 extends RecyclerView.Adapter<SizeAdapter2.MyViewHolder> {
    private RecyclerView.RecycledViewPool viewPool = new RecyclerView.RecycledViewPool();
    private final Context mContext;
    List<String>selectedColors = new ArrayList<>();
    public static List<String>size = new ArrayList<>();



    public SizeAdapter2(Context context, List<String> selectedSize,List<String> size){
        this.mContext = context;
        this.selectedColors = selectedSize;
        this.size = size;
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
        if (holder.mTxt.getText().toString().equals("null")){
            holder.container.setVisibility(View.GONE);
            holder.recyclerView.setVisibility(View.GONE);
            holder.mTxt.setVisibility(View.GONE);
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
        SizeAdapter subItemAdapter = new SizeAdapter(mContext,size,position,holder.mTxt.getText().toString());

        holder.recyclerView.setLayoutManager(layoutManager);
        holder.recyclerView.setAdapter(subItemAdapter);
        holder.recyclerView.setRecycledViewPool(viewPool);

    }

    @Override
    public int getItemCount() {
        return selectedColors.size();
    }

}
