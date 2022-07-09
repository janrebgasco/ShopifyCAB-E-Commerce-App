package com.clickawaybuying.shopify.Adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.clickawaybuying.shopify.classes.ImageReply;
import com.clickawaybuying.shopify.classes.Images;
import com.clickawaybuying.shopify.classes.Messages;
import com.clickawaybuying.shopify.classes.Product;
import com.clickawaybuying.shopify.R;

import java.util.ArrayList;
import java.util.List;

public class ChatRoomAdapter extends RecyclerView.Adapter<ChatRoomAdapter.ViewHolder> {

    private Context mContext;
    public List<Product> myMessage;
    public List<Messages> reply;
    List<Images> image;
    List<ImageReply> imageReply;
    public static List<Messages> chat1 = new ArrayList<>();
    ViewHolder holder;

    public ChatRoomAdapter(Context context, List<Product> myMessage,List<Messages>reply,List<Images>imagesList,List<ImageReply>imageReplyList){
        this.mContext = context;
        this.myMessage = myMessage;
        this.reply = reply;
        this.image = imagesList;
        this.imageReply = imageReplyList;
    }


    public class ViewHolder extends RecyclerView.ViewHolder {

        private TextView mChat1, mChat2;
        CardView mChatHolder1,mChatHolder2;
        ImageView mChatImg1,mChatImg2;

        public ViewHolder (View view){
            super(view);
            mChat1 = itemView.findViewById(R.id.person1Chat);
            mChat2 = itemView.findViewById(R.id.person2Chat);
            mChatHolder1 = itemView.findViewById(R.id.chatHolder1);
            mChatHolder2 = itemView.findViewById(R.id.chatHolder2);
            mChatImg1 = itemView.findViewById(R.id.person1Img);
            mChatImg2 = itemView.findViewById(R.id.person2Img);
        }
    }


    @NonNull
    @Override
    public ChatRoomAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(mContext).inflate(R.layout.chat_list_item,parent,false);
        return new ChatRoomAdapter.ViewHolder(view);

    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull final ChatRoomAdapter.ViewHolder holder, final int position) {

        final Messages messages = reply.get(position);
        final Product product = myMessage.get(position);
        final Images images = image.get(position);
        final ImageReply imgReply = imageReply.get(position);

        holder.mChat2.setText(messages.getMessage());
        holder.mChat1.setText(product.getMessage());
        Glide.with(mContext).load(images.getImage()).into(holder.mChatImg1);
        Glide.with(mContext).load(imgReply.getImage()).into(holder.mChatImg2);
        this.holder = holder;
        condition();
        if (!images.getImage().equals("null")){
            holder.mChatImg1.setVisibility(View.VISIBLE);
        }
        if (!imgReply.getImage().equals("null")){
            holder.mChatImg2.setVisibility(View.VISIBLE);
        }
    }

    public void condition() {
        if (!holder.mChat1.getText().toString().trim().equals("null")){
            holder.mChatHolder1.setVisibility(View.VISIBLE);
        }
        if (!holder.mChat2.getText().toString().trim().equals("null")){
            holder.mChatHolder2.setVisibility(View.VISIBLE);
        }

    }


    @Override
    public int getItemCount() {
        return myMessage.size();
    }


}
