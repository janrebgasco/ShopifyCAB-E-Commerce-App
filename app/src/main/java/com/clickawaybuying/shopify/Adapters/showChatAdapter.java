package com.clickawaybuying.shopify.Adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.clickawaybuying.shopify.ChatRoom;
import com.clickawaybuying.shopify.classes.Messages;
import com.clickawaybuying.shopify.R;

import java.util.ArrayList;
import java.util.List;

public class showChatAdapter extends RecyclerView.Adapter<showChatAdapter.ViewHolder> {

    private Context mContext;
    public List<Messages> chat = new ArrayList<>();

    public showChatAdapter(Context context, List<Messages> chat){
        this.mContext = context;
        this.chat = chat;

    }


    public class ViewHolder extends RecyclerView.ViewHolder {

        private TextView mSeller;
        private ImageView mImageView;
        private CardView mContainer;

        public ViewHolder (View view){
            super(view);
            mSeller = view.findViewById(R.id.showSellerName);
            mImageView = view.findViewById(R.id.show_seller_image);
            mContainer = view.findViewById(R.id.showChatCardview);

        }
    }


    @NonNull
    @Override
    public showChatAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(mContext).inflate(R.layout.show_chat_list_item,parent,false);
        return new showChatAdapter.ViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull final showChatAdapter.ViewHolder holder, final int position) {
        final Messages messages = chat.get(position);
        holder.mSeller.setText(messages.getMessage());

        holder.mContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(mContext.getApplicationContext(), ChatRoom.class);
                i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                i.putExtra("seller",messages.getMessage());
                mContext.getApplicationContext().startActivity(i);
            }
        });

    }


    @Override
    public int getItemCount() {
        return chat.size();
    }


}
