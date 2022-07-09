package com.clickawaybuying.shopify.ui.main;

import android.content.Context;

import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.clickawaybuying.shopify.ApprovedProduct;
import com.clickawaybuying.shopify.Cancelled;
import com.clickawaybuying.shopify.Completed;
import com.clickawaybuying.shopify.PendingProduct;
import com.clickawaybuying.shopify.R;
import com.clickawaybuying.shopify.ReturnRefund;
import com.clickawaybuying.shopify.ToPay;
import com.clickawaybuying.shopify.ToRecieve;
import com.clickawaybuying.shopify.ToShip;

/**
 * A [FragmentPagerAdapter] that returns a fragment corresponding to
 * one of the sections/tabs/pages.
 */
public class SellerSectionsPagerAdapter extends FragmentPagerAdapter {

    @StringRes
    private static final int[] TAB_TITLES = new int[]{R.string.tab_text_1, R.string.tab_text_2};
    private final Context mContext;

    public SellerSectionsPagerAdapter(Context context, FragmentManager fm) {
        super(fm);
        mContext = context;
    }

    @Override
    public Fragment getItem(int position) {
        Fragment fragment=null;
        if (position == 0) {
            fragment = new PendingProduct();
        } else {
            fragment = new ApprovedProduct();
        }
        return fragment;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        if (position == 0) {
            return "Pending Product";
        }
        return "Approved Product";
    }

    @Override
    public int getCount() {
        // Show 2 total pages.
        return 2;
    }
}