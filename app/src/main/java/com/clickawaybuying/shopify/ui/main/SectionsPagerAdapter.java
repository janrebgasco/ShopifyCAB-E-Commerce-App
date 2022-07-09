package com.clickawaybuying.shopify.ui.main;

import android.content.Context;

import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.clickawaybuying.shopify.Cancelled;
import com.clickawaybuying.shopify.Completed;
import com.clickawaybuying.shopify.R;
import com.clickawaybuying.shopify.ReturnRefund;
import com.clickawaybuying.shopify.ToPay;
import com.clickawaybuying.shopify.ToRecieve;
import com.clickawaybuying.shopify.ToShip;

/**
 * A [FragmentPagerAdapter] that returns a fragment corresponding to
 * one of the sections/tabs/pages.
 */
public class SectionsPagerAdapter extends FragmentPagerAdapter {

    @StringRes
    private static final int[] TAB_TITLES = new int[]{R.string.tab_text_1, R.string.tab_text_2};
    private final Context mContext;

    public SectionsPagerAdapter(Context context, FragmentManager fm) {
        super(fm);
        mContext = context;
    }

    @Override
    public Fragment getItem(int position) {
        Fragment fragment=null;
        switch (position){
            case 0:
                fragment = new ToPay();
                break;
            case 1:
                fragment = new ToShip();
                break;
            case 2:
                fragment = new ToRecieve();
                break;
            case 3:
                fragment = new Completed();
                break;
            case 4:
                fragment = new Cancelled();
                break;
            default:
                fragment = new ReturnRefund();
                break;
        }
        return fragment;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        switch (position){
            case 0:
                return "To Pay";
            case 1:
                return "To Ship";
            case 2:
                return "To Receive";
            case 3:
                return "Completed";
            case 4:
                return "Cancelled";
            default:
                return "Return Refund";
        }
    }

    @Override
    public int getCount() {
        // Show 6 total pages.
        return 6;
    }
}