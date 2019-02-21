package com.daily.android;

import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class MainActivity extends AppCompatActivity {

    TabLayout mTableLayout;
    ViewPager mViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mTableLayout = findViewById(R.id.tabs);
        mViewPager = findViewById(R.id.viewpager);


        mViewPager.setAdapter(new FragmentPagerAdapter(getSupportFragmentManager()) {
            @Override
            public Fragment getItem(int i) {
                return ListFragment.newInstance(100);
            }

            @Override
            public int getCount() {
                return 10;
            }

            @Nullable
            @Override
            public CharSequence getPageTitle(int position) {
                return "index " + position;
            }
        });

        mTableLayout.setupWithViewPager(mViewPager);
    }
}
