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

public class ColorAdapter extends RecyclerView.Adapter<ColorAdapter.MyViewHolder> {

    private Context mContext;
    public static List<String>selectedColors = new ArrayList<>();
    public static List<String>selectedColorQuan = new ArrayList<>();
    List<String>colors = new ArrayList<>();
    int totalColorQuan = 0;
    public static int maxSizeQuan = 0;


    public ColorAdapter(Context context, List<String> colors){
        this.mContext = context;
        this.colors = colors;
    }


    public class MyViewHolder extends RecyclerView.ViewHolder {

        ToggleButton mToggle;
        TextView mQuan;
        Button mAdd,mMinus;
        int colorQuan=1;


        public MyViewHolder (View view){
            super(view);
            mToggle = view.findViewById(R.id.listToggle);
            mQuan = view.findViewById(R.id.colorQuantity);
            mAdd = view.findViewById(R.id.colorIncrease);
            mMinus = view.findViewById(R.id.colorDecrease);

        }
    }


    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(mContext).inflate(R.layout.colors_list_item,parent,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, final int position) {

        holder.mToggle.setText(colors.get(position));
        holder.mToggle.setTextOff(colors.get(position));
        holder.mToggle.setTextOn(colors.get(position));


        if (!holder.mToggle.isChecked()){
            holder.mQuan.setVisibility(View.INVISIBLE);
            holder.mAdd.setVisibility(View.INVISIBLE);
            holder.mMinus.setVisibility(View.INVISIBLE);
        }
        final int pos = (position + 1) * Purchase.size.size();
        final int pos1 = position * Purchase.size.size();
        holder.mToggle.setOnCheckedChangeListener( new CompoundButton.OnCheckedChangeListener()
        {
            @Override
            public void onCheckedChanged(CompoundButton toggleButton, boolean isChecked)
            {
                if(isChecked){
                    for (int i = pos1; i < pos; i++){
                        Purchase.selectedparentColorsQuan.set(i,1);
                    }
                    Purchase.cartSelectedColors.set(position,1);
                    Purchase.cartSelectedColorQuan.set(position,1);
                    selectedColors.add(colors.get(position));
                    holder.mQuan.setVisibility(View.VISIBLE);
                    holder.mAdd.setVisibility(View.VISIBLE);
                    holder.mMinus.setVisibility(View.VISIBLE);
                    holder.mQuan.setText(String.valueOf(holder.colorQuan));
                    totalColorQuan = totalColorQuan + holder.colorQuan;
                    maxSizeQuan = totalColorQuan;
                    ((Purchase) mContext).colorTotalQuan(totalColorQuan);
                    Purchase.selectedColorQuan.set(position,holder.colorQuan);
                    Purchase.selectedColors.set(position,colors.get(position));
                    ((Purchase) mContext).notifyChanges();
                }
                else{
                    for (int i = pos1; i < pos; i++){
                        Purchase.selectedparentColorsQuan.set(i,0);
                    }
                    Purchase.cartSelectedColors.set(position,0);
                    Purchase.cartSelectedColorQuan.set(position,0);
                    selectedColors.remove(colors.get(position));
                    selectedColors.add("null");
                    holder.mQuan.setVisibility(View.INVISIBLE);
                    holder.mAdd.setVisibility(View.INVISIBLE);
                    holder.mMinus.setVisibility(View.INVISIBLE);
                    totalColorQuan = totalColorQuan - holder.colorQuan;
                    holder.colorQuan = 1;
                    maxSizeQuan = totalColorQuan;
                    ((Purchase) mContext).colorTotalQuan(totalColorQuan);
                    Purchase.selectedColorQuan.set(position,0);
                    Purchase.selectedColors.set(position,"null");
                    ((Purchase) mContext).notifyChanges();
                }
                ((Purchase) mContext).removeArrayObject();
            }
        });

        holder.mAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                holder.mMinus.setEnabled(true);
                holder.colorQuan++;
                for (int i = pos1; i < pos; i++){
                    Purchase.selectedparentColorsQuan.set(i,holder.colorQuan);
                }
                Purchase.cartSelectedColorQuan.set(position,holder.colorQuan);
                holder.mQuan.setText(String.valueOf(holder.colorQuan));
                totalColorQuan = totalColorQuan + 1;
                maxSizeQuan = totalColorQuan;
                Purchase.selectedColorQuan.set(position,holder.colorQuan);
                ((Purchase) mContext).colorTotalQuan(totalColorQuan);
            }
        });

        if(holder.colorQuan==1)
        {
            holder.mQuan.setText(String.valueOf(holder.colorQuan));
            holder.mMinus.setEnabled(false);
        }

        holder.mMinus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                holder.colorQuan--;
                for (int i = pos1; i < pos; i++){
                    Purchase.selectedparentColorsQuan.set(i,holder.colorQuan);
                }
                Purchase.cartSelectedColorQuan.set(position,holder.colorQuan);
                if(holder.colorQuan==1)
                    {
                        holder.mQuan.setText(String.valueOf(holder.colorQuan));
                        holder.mMinus.setEnabled(false);
                    }
                holder.mQuan.setText(String.valueOf(holder.colorQuan));
                totalColorQuan = totalColorQuan - 1;
                maxSizeQuan = totalColorQuan;
                Purchase.selectedColorQuan.set(position,holder.colorQuan);
                ((Purchase) mContext).colorTotalQuan(totalColorQuan);
            }
        });


    }

    @Override
    public int getItemCount() {
        return colors.size();
    }

}
