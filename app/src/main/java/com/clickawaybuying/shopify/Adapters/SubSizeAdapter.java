package com.clickawaybuying.shopify.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.clickawaybuying.shopify.Purchase;
import com.clickawaybuying.shopify.R;

import java.util.ArrayList;
import java.util.List;

public class SubSizeAdapter extends RecyclerView.Adapter<SubSizeAdapter.MyViewHolder> {
    private Context mContext;
    public static List<String>selectedSize = new ArrayList<>();
    public static List<String>selectedSizeQuan = new ArrayList<>();
    public static List<String>size = new ArrayList<>();
    int totalSizeQuan = 0;


    public SubSizeAdapter(List<String> size){
        this.size = size;
    }


    public class MyViewHolder extends RecyclerView.ViewHolder {

        ToggleButton mToggle;
        TextView mQuan;
        Button mAdd,mMinus;
        int sizeQuan=1;

        public MyViewHolder (View view){
            super(view);
            mToggle = view.findViewById(R.id.sizeToggle);
            mQuan = view.findViewById(R.id.sizeQuantity);
            mAdd = view.findViewById(R.id.sizeIncrease);
            mMinus = view.findViewById(R.id.sizeDecrease);

        }
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.size_list_item_copy,parent,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final SubSizeAdapter.MyViewHolder holder, final int position) {
        Toast.makeText(mContext, "Gumana Adapter", Toast.LENGTH_SHORT).show();
        holder.mToggle.setText(size.get(position));
        holder.mToggle.setTextOff(size.get(position));
        holder.mToggle.setTextOn(size.get(position));

        if (!holder.mToggle.isChecked()){
            holder.mQuan.setVisibility(View.INVISIBLE);
            holder.mAdd.setVisibility(View.INVISIBLE);
            holder.mMinus.setVisibility(View.INVISIBLE);
        }

        holder.mToggle.setOnCheckedChangeListener( new CompoundButton.OnCheckedChangeListener()
        {
            @Override
            public void onCheckedChanged(CompoundButton toggleButton, boolean isChecked)
            {
                if(isChecked){
                    if (!Purchase.colors.isEmpty()||Purchase.colors.equals("null")){//check if color arraylist is empty
                        if (totalSizeQuan < ColorAdapter.maxSizeQuan)//if total of size quantity is not equal to total color quantity
                        {
                            selectedSize.add(size.get(position));//add size to arraylist
                            holder.mQuan.setVisibility(View.VISIBLE);//set quantity textview visibility to visible
                            holder.mAdd.setVisibility(View.VISIBLE);//set add button visibility to visible
                            holder.mMinus.setVisibility(View.VISIBLE);//set decrease button visibility to visible
                            holder.mQuan.setText(String.valueOf(holder.sizeQuan));//set size quantity to textview
                            totalSizeQuan = totalSizeQuan + holder.sizeQuan;//adds the quantity of selected to total size quantity
                            ((Purchase) mContext).sizeTotalQuantity(totalSizeQuan);//calls the method sizeTotalQuantity in Purchase.class then pass the value of totalSizeQuan
                        }
                    }

                    else {
                        selectedSize.add(size.get(position));
                        selectedSizeQuan.set(position,"("+holder.mQuan.getText().toString()+"x)"+size.get(position));
                        holder.mQuan.setVisibility(View.VISIBLE);
                        holder.mAdd.setVisibility(View.VISIBLE);
                        holder.mMinus.setVisibility(View.VISIBLE);
                        holder.mQuan.setText(String.valueOf(holder.sizeQuan));
                        totalSizeQuan = totalSizeQuan + holder.sizeQuan;
                        ((Purchase) mContext).sizeTotalQuan(totalSizeQuan);
                    }
                }
                else{
                    if (!Purchase.colors.isEmpty()||Purchase.colors.equals("null")) {
                        if (totalSizeQuan == ColorAdapter.maxSizeQuan){}
                        if (totalSizeQuan < ColorAdapter.maxSizeQuan) {
                            selectedSize.remove(size.get(position));
                            selectedSizeQuan.set(position,"0");
                            selectedSize.add("null");
                            holder.mQuan.setVisibility(View.INVISIBLE);
                            holder.mAdd.setVisibility(View.INVISIBLE);
                            holder.mMinus.setVisibility(View.INVISIBLE);
                            totalSizeQuan = totalSizeQuan - holder.sizeQuan;
                            ((Purchase) mContext).sizeTotalQuantity(totalSizeQuan);
                        }
                    }
                    else {
                        selectedSize.remove(size.get(position));
                        selectedSizeQuan.set(position,"0");
                        selectedSize.add("null");
                        holder.mQuan.setVisibility(View.INVISIBLE);
                        holder.mAdd.setVisibility(View.INVISIBLE);
                        holder.mMinus.setVisibility(View.INVISIBLE);
                        totalSizeQuan = totalSizeQuan - holder.sizeQuan;
                        ((Purchase) mContext).sizeTotalQuan(totalSizeQuan);
                    }
                }
            }
        });

        holder.mAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!Purchase.colors.isEmpty()||Purchase.colors.equals("null")) {
                    if ((totalSizeQuan < ColorAdapter.maxSizeQuan)) {
                        holder.mMinus.setEnabled(true);
                        holder.sizeQuan++;
                        holder.mQuan.setText(String.valueOf(holder.sizeQuan));
                        totalSizeQuan = totalSizeQuan + 1;
                        ((Purchase) mContext).sizeTotalQuantity(totalSizeQuan);
                    }
                }
                else {
                    holder.mMinus.setEnabled(true);
                    holder.sizeQuan++;
                    holder.mQuan.setText(String.valueOf(holder.sizeQuan));
                    totalSizeQuan = totalSizeQuan + 1;
                    selectedSizeQuan.set(position,"("+holder.mQuan.getText().toString()+"x)"+size.get(position));
                    ((Purchase) mContext).sizeTotalQuantity(totalSizeQuan);
                }
            }
        });

        if(holder.sizeQuan==1)
        {
            holder.mQuan.setText(String.valueOf(holder.sizeQuan));
            holder.mMinus.setEnabled(false);
        }

        holder.mMinus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    holder.sizeQuan--;
                    if(holder.sizeQuan==1)
                    {
                        holder.mQuan.setText(String.valueOf(holder.sizeQuan));
                        holder.mMinus.setEnabled(false);
                    }
                    holder.mQuan.setText(String.valueOf(holder.sizeQuan));
                    totalSizeQuan = totalSizeQuan - 1;
                    selectedSizeQuan.set(position,"("+holder.mQuan.getText().toString()+"x)"+size.get(position));
                    ((Purchase) mContext).sizeTotalQuan(totalSizeQuan);
                }
                catch (Exception e){
                    e.printStackTrace();
                }

            }
        });
    }

    @Override
    public int getItemCount() {
        return 0;
    }
}
