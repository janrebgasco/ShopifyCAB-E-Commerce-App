package com.clickawaybuying.shopify;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;
import androidx.viewpager2.widget.ViewPager2;

import android.os.Bundle;
import android.widget.TableLayout;

import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

import java.util.ArrayList;
import java.util.List;

public class MyOrders extends AppCompatActivity {
    TabLayout mTabLayout;
    ViewPager mViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_orders);

        mTabLayout = findViewById(R.id.orderTab);
        mViewPager = findViewById(R.id.orderPager);

        ArrayList<String> arrayList = new ArrayList<>();

        arrayList.add("To Pay");
        arrayList.add("To Ship");
        arrayList.add("To Recieve");
        arrayList.add("Completed");
        arrayList.add("Cancelled");
        arrayList.add("Return Refund");

        prepareViewPager(mViewPager,arrayList);

        mTabLayout.setupWithViewPager(mViewPager);
    }

    private void prepareViewPager(ViewPager mViewPager, ArrayList<String> arrayList) {
        MainAdapter adapter = new  MainAdapter(getSupportFragmentManager());
        ToPay toPay = new ToPay();

        for (int i=0;i<=5;i++){
            Bundle bundle = new Bundle();
            bundle.putString("title",arrayList.get(i));
            toPay.setArguments(bundle);
            adapter.addFragment(toPay,arrayList.get(i));
            toPay = new ToPay();
        }
        mViewPager.setAdapter(adapter);
    }

    private class MainAdapter extends FragmentPagerAdapter{
        ArrayList<String> arrayList = new ArrayList<>();
        List<Fragment> fragmentList = new ArrayList<>();

        public void addFragment(Fragment fragment,String title){
            arrayList.add(title);
            fragmentList.add(fragment);
        }

        public MainAdapter(@NonNull FragmentManager fm) {
            super(fm);
        }

        @NonNull
        @Override
        public Fragment getItem(int position) {
            return fragmentList.get(position);
        }

        @Override
        public int getCount() {
            return fragmentList.size();
        }

        @Nullable
        @Override
        public CharSequence getPageTitle(int position) {
            return arrayList.get(position);
        }
    }
}