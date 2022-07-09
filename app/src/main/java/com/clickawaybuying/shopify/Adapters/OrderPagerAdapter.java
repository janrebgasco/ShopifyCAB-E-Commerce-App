package com.clickawaybuying.shopify.Adapters;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.clickawaybuying.shopify.Cancelled;
import com.clickawaybuying.shopify.Completed;
import com.clickawaybuying.shopify.ReturnRefund;
import com.clickawaybuying.shopify.ToPay;
import com.clickawaybuying.shopify.ToRecieve;
import com.clickawaybuying.shopify.ToShip;

public class OrderPagerAdapter extends FragmentStateAdapter {
    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position){
            case 0:
                return new ToShip();
            case 1:
                return new ToRecieve();
            case 2:
                return new Completed();
            case 3:
                return new Cancelled();
            case 4:
                return new ReturnRefund();
            default:
                return new ToPay();

        }
    }

    @Override
    public int getItemCount() {
        return 6;
    }

    public OrderPagerAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }
}
