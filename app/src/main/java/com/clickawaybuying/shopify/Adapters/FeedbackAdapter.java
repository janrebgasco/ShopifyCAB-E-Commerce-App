package com.clickawaybuying.shopify.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.clickawaybuying.shopify.classes.Product;
import com.clickawaybuying.shopify.R;

import java.util.ArrayList;
import java.util.List;

public class FeedbackAdapter extends RecyclerView.Adapter<FeedbackAdapter.MyViewHolder> {

    private final Context mContext;
    public List<Product>feedback = new ArrayList<>();


    public FeedbackAdapter(Context context, List<Product> feedback){
        this.mContext = context;
        this.feedback = feedback;
    }


    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView mUsername,mFeedback;
        ImageView mImg;
        RatingBar mRating;

        public MyViewHolder (View view){
            super(view);
            mUsername = view.findViewById(R.id.fbackUsername);
            mImg = view.findViewById(R.id.FBackImg);
            mRating = view.findViewById(R.id.listRatingbar);
            mFeedback = view.findViewById(R.id.feedback);

        }
    }


    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(mContext).inflate(R.layout.feedback_list_item,parent,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, final int position) {
        Product fb = feedback.get(position);
        holder.mUsername.setText(fb.getUsername());
        holder.mRating.setRating(fb.getRating3());
        holder.mFeedback.setText(fb.getFeedback());
        Glide.with(mContext).load(fb.getImg()).into(holder.mImg);

    }

    @Override
    public int getItemCount() {
        return feedback.size();
    }

}
